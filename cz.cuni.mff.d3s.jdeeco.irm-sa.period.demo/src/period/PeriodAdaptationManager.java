/*******************************************************************************
 * Copyright 2014 Charles University in Prague
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package period;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import org.eclipse.emf.common.util.EMap;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irmsa.IRMInstanceGenerator;

/**
 * Handles adaptation of invariant periods.
 */
@Component
@SystemComponent
public final class PeriodAdaptationManager {

	/** Trace model stored in internal data under this key. */
	static private final String TRACE_MODEL = "trace";

	/** Design model stored in internal data under this key. */
	static private final String DESIGN_MODEL = "design";

	/** InvariantFitnessCombiner stored in internal data under this key. */
	static private final String INVARIANT_FITNESS_COMBINER = "invariantFitnessCombiner";

	/** AdapteeSelector stored in internal data under this key. */
	static private final String ADAPTEE_SELECTOR = "adapteeSelector";

	/** DirectionSelector stored in internal data under this key. */
	static private final String DIRECTION_SELECTOR = "directionSelector";

	/** DeltaComputor stored in internal data under this key. */
	static private final String DELTA_COMPUTOR = "deltaComputor";

	/**
	 * Mapping of ids to builders.
	 * TODO think of a better way to pass data from builder to component internal data as it may be not the best approach to store them statically
	 */
	static final private Map<String, PeriodAdaptationManagerBuilder> toPrepare =
			Collections.synchronizedMap(new HashMap<String, PeriodAdaptationManagerBuilder>());

	/** Manager ID. */
	public String id;

	/** Holds the state of the adaptPeriods method. */
	public StateHolder state = new StateHolder();

	/**
	 * Prepares PeriodAdaptationManagers in model.
	 * Ie. passes them design and trace by internal data hack.
	 * @param model runtime model
	 * @param design IRM
	 * @param trace TraceModel
	 */
	public static void prepare(RuntimeMetadata model, IRM design,
			TraceModel trace) {
		for (ComponentInstance c : model.getComponentInstances()) {
			if (c.getName().equals(PeriodAdaptationManager.class.getName())) {
				final EMap<String, Object> data = c.getInternalData();
				data.put(DESIGN_MODEL, design);
				data.put(TRACE_MODEL, trace);
				//TODO is knowledge manager's id guaranteed to be the same as the component's one?
				final PeriodAdaptationManagerBuilder builder =
						toPrepare.remove(c.getKnowledgeManager().getId());
				if (builder != null) {
					data.put(INVARIANT_FITNESS_COMBINER, builder.invariantFitnessCombiner);
					data.put(ADAPTEE_SELECTOR, builder.adapteeSelector);
					data.put(DIRECTION_SELECTOR, builder.directionSelector);
					data.put(DELTA_COMPUTOR, builder.deltaComputor);
				}
			}
		}
	}

	/**
	 * Factory method for creating builder and manager itself.
	 * @return newly created manager builder
	 */
	public static PeriodAdaptationManagerBuilder create() {
		return new PeriodAdaptationManagerBuilder(toPrepare);
	}

	@Process
	@PeriodicScheduling(period=Settings.ADAPTATION_PERIOD, order=10)
	static public void adaptPeriods(
			@In("id") String id,
			@InOut("state") ParamHolder<StateHolder> stateHolder) {
		final StateHolder state = stateHolder.value;
		final ComponentProcess process = ProcessContext.getCurrentProcess();
		// get runtime model from the process context
		final RuntimeMetadata runtime = (RuntimeMetadata) process.getComponentInstance().eContainer();
		// get simulated time
		final long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		System.out.println("*** Adapting periods in runtime "+ runtime + " at time " + simulatedTime +" ***");
		// skipping the first run of this process as replicas are not disseminated yet
		if (simulatedTime == 0) {
			Log.w("First invocation of the PeriodAdaptationManager. Skipping this reasoning cycle.");
			return;
		}

		// get architecture, design, trace models and plug-ins from the process context
		final Architecture architecture = ProcessContext.getArchitecture();
		final IRM design = retrieveFromInternalData(DESIGN_MODEL);
		final TraceModel trace = retrieveFromInternalData(TRACE_MODEL);
		final InvariantFitnessCombiner invariantFitnessCombiner =
				retrieveFromInternalData(INVARIANT_FITNESS_COMBINER);
		final AdapteeSelector adapteeSelector =
				retrieveFromInternalData(ADAPTEE_SELECTOR);
		final DirectionSelector directionSelector =
				retrieveFromInternalData(DIRECTION_SELECTOR);
		final DeltaComputor deltaComputor =
				retrieveFromInternalData(DELTA_COMPUTOR);

		// generate the IRM runtime model instances
		final IRMInstanceGenerator generator =
				new IRMInstanceGenerator(architecture, design, trace);
		final List<IRMInstance> IRMInstances = generator.generateIRMInstances();

		if (IRMInstances.isEmpty()) {
			//nothing to adapt, reset state
			//TODO reset original period of this process
			//process.setPeriod(state.originalPeriod);
			state.reset();
			return;
		}

		if (state.state == State.STARTED) { //first part of adaptation
			//Create data structure for processing
			final Set<InvariantInfo<?>> infos = extractInvariants(IRMInstances);

			//Compute overall fitness
			computeInvariantsFitness(infos);

			//Compute overall fitness
			state.oldFitness = invariantFitnessCombiner.combineInvariantFitness(infos);

			//Select a (set of) processes to adapt
			final Set<InvariantInfo<?>> adaptees = adapteeSelector.selectAdaptees(infos);

			//Select direction(s)
			for (InvariantInfo<?> invariant: adaptees) {
				directionSelector.selectDirection(invariant);
			}

			//Compute delta(s)
			for (InvariantInfo<?> invariant: adaptees) {
				deltaComputor.computeDelta(invariant);
			}

			//Create child by applying the changes
			state.backup = applyChanges(adaptees);

			//{Compute observe time}
			final long observeTime = computeObserveTime(adaptees, infos);

			//Run for observe time
			state.state = State.OBSERVED;
			//TODO change period of this process
			//state.originalPeriod = process.getPeriod();
			//process.setPeriod(observeTime);
		} else if (state.state == State.OBSERVED) { //observing done
			//Create data structure for processing
			final Set<InvariantInfo<?>> infos = extractInvariants(IRMInstances);

			//Compute fitness functions of invariants again
			computeInvariantsFitness(infos);

			//Compute overall fitness
			final double newFitness = invariantFitnessCombiner.combineInvariantFitness(infos);

			if (newFitness > state.oldFitness) {
				//Take child as new parent
			} else {
				//Keep parent
				restoreBackup(infos, state.backup);
			}

			//{Mark non-prospective specimen as dead end or utilize Simulated annealing}
			state.reset();
			//TODO restore original period
			//process.setPeriod(state.originalPeriod);
		} else {
			Log.w("Unknown state " + state.state);
		}
	}

	/**
	 * Not type-safe method for retrieving objects from component's internal data.
	 * @param key key to search value for
	 * @return typed object from component's internal data
	 * @throws RuntimeException when no, null or wrongly typed data are provided
	 */
	static private <T> T retrieveFromInternalData(final String key) {
		final ComponentInstance instance =
				ProcessContext.getCurrentProcess().getComponentInstance();
		final Object value = instance.getInternalData().get(key);
		try {
			if (value != null) {
				@SuppressWarnings("unchecked")
				final T result = (T) value;
				return result;
			} else {
				throw new NoSuchElementException("No or null data for key " + key);
			}
		} catch (ClassCastException | NoSuchElementException e) {
			throw new RuntimeException(String.format("Could not retrieve %s from internal data. Make sure to run PeriodAdaptationManager.prepare", key), e);
		}
	}

	/**
	 * Extracts invariants from IRMInstances and wraps them into InvariantInfos.
	 * @param irmInstances instances to extract from
	 * @return extracted invariants wrapped into InvariantInfos
	 */
	static private Set<InvariantInfo<?>> extractInvariants(
			final Collection<IRMInstance> irmInstances) {
		final Set<InvariantInfo<?>> infos = new HashSet<>();
		for (IRMInstance irm: irmInstances) {
			for (InvariantInstance invariant: irm.getInvariantInstances()) {
				if (invariant instanceof ProcessInvariantInstance) {
					final ProcessInvariantInstance pii =
							(ProcessInvariantInstance) invariant;
					infos.add(InvariantInfo.create(pii));
				} else if (invariant instanceof ExchangeInvariantInstance) {
					final ExchangeInvariantInstance xii =
							(ExchangeInvariantInstance) invariant;
					infos.add(InvariantInfo.create(xii));
				}
			}
		}
		return infos;
	}

	/**
	 * Computes fitness for invariant instances.
	 * @param infos invariant instances to compute fitness for
	 */
	static private void computeInvariantsFitness(final Set<InvariantInfo<?>> infos) {
		for (InvariantInfo<?> info : infos) {
			InvariantInstance instance = info.getInvariant();
			info.fitness = instance.getFitness();
		}
	}

	/**
	 * Returns current period of given invariant in info.
	 * @param info holder of invariant
	 * @return current period of given invariant
	 */
	static private long getCurrentPeriod(InvariantInfo<?> info) {
		if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ProcessInvariantInstance pii = info.getInvariant();
//			return pii.getComponentProcess().getPeriod(); //TODO extract current period
		} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ExchangeInvariantInstance xii = info.getInvariant();
			//TODO how to get EnsembleController from ExchangeInvariantInstance???
//			return ensembleController.getPeriod();
		}
		return 0;
	}

	/**
	 * Applies changes specified in infos.
	 * @param infos container of desired changes
	 * @return backup to revert the changes
	 */
	static private Backup applyChanges(final Set<InvariantInfo<?>> infos) {
		final Backup backup = new Backup();
		for (InvariantInfo<?> info : infos) {
			final Backup.Change change = new Backup.Change(info.delta, info.direction.opposite());
			final long currentPeriod = getCurrentPeriod(info);
			final long newPeriod = currentPeriod + info.direction.getCoef() * info.delta;
			if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ProcessInvariantInstance pii = info.getInvariant();
//				pii.getComponentProcess().setPeriod(newPeriod); //TODO set new period for process
				final String id = pii.getComponentProcess().getComponentInstance().getKnowledgeManager().getId();
				backup.processes.put(id, change);
			} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ExchangeInvariantInstance xii = info.getInvariant();
				//TODO how to get EnsembleController from ExchangeInvariantInstance???
//				ensembleController.setPeriod(newPeriod);
				final String id = "TODO";
				backup.exchanges.put(id, change);
			}
		}
		return backup;
	}

	/**
	 * Returns observe time for given adaptees and invariant infos.
	 * @param adaptees adapted invariants
	 * @param infos all invariants
	 * @return observe time
	 */
	static private long computeObserveTime(final Set<InvariantInfo<?>> adaptees, final Set<InvariantInfo<?>> infos) {
		//TODO implement more sophisticated algorithm
		long max = 0;
		for (InvariantInfo<?> info : infos) {
			final long period = getCurrentPeriod(info);
			if (period > max) {
				max = period;
			}
		}
		final long minIterations = 10L;
		return minIterations * max;
	}

	static private long computeNewPeriod(InvariantInfo<?> info, Backup.Change change) {
		final long currentPeriod = getCurrentPeriod(info);
		return currentPeriod + change.direction.getCoef() * change.delta;
	}

	/**
	 * Restores backup.
	 * @param backup Backup to restore
	 */
	static private void restoreBackup(final Set<InvariantInfo<?>> infos, final Backup backup) {
		for (InvariantInfo<?> info: infos) {
			if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ProcessInvariantInstance pii = info.getInvariant();
				final String id = pii.getComponentProcess().getComponentInstance().getKnowledgeManager().getId();
				final Backup.Change change = backup.processes.get(id);
				if (change == null) {
					continue;
				}
				final long newPeriod = computeNewPeriod(info, change);
//				pii.getComponentProcess().setPeriod(newPeriod); //TODO set new period for process
			} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ExchangeInvariantInstance xii = info.getInvariant();
				//TODO how to get EnsembleController from ExchangeInvariantInstance???
				final String id = "TODO";
				final Backup.Change change = backup.exchanges.get(id);
				if (change == null) {
					continue;
				}
				final long newPeriod = computeNewPeriod(info, change);
//				ensembleController.setPeriod(newPeriod);
			}
		}
	}

	/**
	 * Only constructor.
	 */
	protected PeriodAdaptationManager(final long id) {
		this.id = String.format("PeriodAdapatationManager_0x%08X", id);
	}

	/**
	 * Class holding the information needed to restore the previous state of the system.
	 */
	private static class Backup extends KnowledgeImpl {

		//TODO maybe one map is enough? if id collision are not possible
		/** Changes of process invariants. */
		public Map<String, Change> processes = new HashMap<>();

		/** Changes of exchange invariants. */
		public Map<String, Change> exchanges = new HashMap<>();

		/**
		 * Only constructor.
		 */
		public Backup() {
			this.name = "PeriodAdaptationManagerBackup";
			this.type = "PeriodAdaptationManagerBackupType";
		}

		static class Change extends KnowledgeImpl {

			/** Period delta. */
			public long delta;

			/** Direction of change. */
			public Direction direction;

			/**
			 * Only constructor.
			 * @param delta period delta
			 * @param direction direction of change
			 */
			public Change(final long delta, final Direction direction) {
				this.name = "PeriodAdaptationManagerBackupItem";
				this.type = "PeriodAdaptationManagerBackupItemType";
				this.delta = delta;
				this.direction = direction;
			}
		}
	}

	/**
	 * Simple enum with possible states of the adaptPeriods method.
	 */
	enum State {
		/** No adaptation in progress, start from beginning. */
		STARTED,

		/** Adaptation in progress, observe time has ended. */
		OBSERVED
	}

	/**
	 * Simple class holding information about state between runs of adaptPeriods.
	 */
	private static class StateHolder extends KnowledgeImpl {

		/** State of the method adaptPeriods. */
		public State state;

		/** Fitness of the previous configuration. */
		public double oldFitness;

		/** Backup to restore the previous configuration. */
		public Backup backup;

		/** Original period of the adaptation process. */
		public long originalPeriod;

		/**
		 * Only constructor.
		 */
		public StateHolder() {
			this.name = "StateHolder";
			this.type = "StateHolderType";
			reset();
		}

		/**
		 * Resets the state.
		 */
		public void reset() {
			state = State.STARTED;
			oldFitness = 0.0;
			backup = null;
			originalPeriod = 0;
		}
	}
}
