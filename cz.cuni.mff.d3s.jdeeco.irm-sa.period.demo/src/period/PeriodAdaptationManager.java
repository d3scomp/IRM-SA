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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
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
	static final String TRACE_MODEL = "trace";

	/** Design model stored in internal data under this key. */
	static final String DESIGN_MODEL = "design";

	/** InvariantFitnessCombiner stored in internal data under this key. */
	static final String INVARIANT_FITNESS_COMBINER = "invariantFitnessCombiner";

	/** AdapteeSelector stored in internal data under this key. */
	static final String ADAPTEE_SELECTOR = "adapteeSelector";

	/** DirectionSelector stored in internal data under this key. */
	static final String DIRECTION_SELECTOR = "directionSelector";

	/** DeltaComputor stored in internal data under this key. */
	static final String DELTA_COMPUTOR = "deltaComputor";

	/** Manager ID. */
	public String id;

	/** Holds the state of the adaptPeriods method. */
	public StateHolder state = new StateHolder();

	/** Overall system fitness. */
	public Double fitness = 0.0;

	/**
	 * Only constructor.
	 */
	protected PeriodAdaptationManager() {
		this.id = String.format("PeriodAdapatationManager_%s", UUID.randomUUID());
	}

	@Process
	@PeriodicScheduling(period=Settings.MONITOR_PERIOD, order=10)
	static public void monitorOverallFitness(
			@In("id") String id,
			@Out("fitness") ParamHolder<Double> fitness) {
		final ComponentProcess process = ProcessContext.getCurrentProcess();
		// get runtime model from the process context
		final RuntimeMetadata runtime = (RuntimeMetadata) process.getComponentInstance().eContainer();
		// get simulated time
		final long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		System.out.println("*** Monitoring overall system fitness in runtime "+ runtime + " at time " + simulatedTime +" ***");
		// skipping the first run of this process as replicas are not disseminated yet
		if (simulatedTime <= 0) {
			Log.w("First invocation of the fitness monitoring. Skipping this reasoning cycle.");
			return;
		}

		// get architecture, design, trace models and plug-ins from the process context
		final Architecture architecture = ProcessContext.getArchitecture();
		final IRM design = retrieveFromInternalData(DESIGN_MODEL);
		final TraceModel trace = retrieveFromInternalData(TRACE_MODEL);
		final InvariantFitnessCombiner invariantFitnessCombiner =
				retrieveFromInternalData(INVARIANT_FITNESS_COMBINER);

		// generate the IRM runtime model instances
		final IRMInstanceGenerator generator =
				new IRMInstanceGenerator(architecture, design, trace);
		final List<IRMInstance> IRMInstances = generator.generateIRMInstances();

		if (IRMInstances.isEmpty()) {
			//nothing to monitor
			return;
		}

		//Create data structure for processing
		final Set<InvariantInfo<?>> infos = extractInvariants(IRMInstances);

		//Compute processes' fitnesses
		computeInvariantsFitness(infos);

		//Compute overall fitness
		fitness.value = invariantFitnessCombiner.combineInvariantFitness(infos);
		System.out.println("Overall System Fitness: " + fitness.value + "(at " + simulatedTime + ")");
	}

	@Process
	@PeriodicScheduling(period=Settings.ADAPTATION_PERIOD, order=10)
	static public void adaptPeriods(
			@In("id") String id,
			@In("fitness") Double fitness,
			@InOut("state") ParamHolder<StateHolder> stateHolder) {
		final StateHolder state = stateHolder.value;
		final ComponentProcess process = ProcessContext.getCurrentProcess();
		// get runtime model from the process context
		final RuntimeMetadata runtime = (RuntimeMetadata) process.getComponentInstance().eContainer();
		// get simulated time
		final long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		System.out.println("*** Adapting periods in runtime "+ runtime + " at time " + simulatedTime +" ***");
		// skipping the first run of this process as replicas are not disseminated yet
		if (simulatedTime <= 0) {
			Log.w("First invocation of the PeriodAdaptationManager. Skipping this reasoning cycle.");
			return;
		}

		// get architecture, design, trace models and plug-ins from the process context
		final Architecture architecture = ProcessContext.getArchitecture();
		final IRM design = retrieveFromInternalData(DESIGN_MODEL);
		final TraceModel trace = retrieveFromInternalData(TRACE_MODEL);
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
			final TimeTrigger trigger = getTimeTrigger(process);
			trigger.setPeriod(state.originalPeriod);
			state.reset();
			return;
		}

		if (state.state == State.STARTED) { //first part of adaptation
			//Create data structure for processing
			final Set<InvariantInfo<?>> infos = extractInvariants(IRMInstances);

			state.oldFitness = fitness;
			//TODO define clearly condition for adaptation
			if (state.oldFitness >= 0.5) {
				final TimeTrigger trigger = getTimeTrigger(process);
				trigger.setPeriod(Settings.ADAPTATION_PERIOD);
				state.reset();
				return;
			}

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
			//change period of this process
			final TimeTrigger trigger = getTimeTrigger(process);
			state.originalPeriod = trigger.getPeriod();
			if (observeTime > state.originalPeriod) { //changing period takes effect only the run after the next one
				trigger.setPeriod(observeTime - state.originalPeriod);
			}
			state.observeTime = simulatedTime + observeTime;
			System.out.println("!!!ADAPTING!!!");
		} else if (state.state == State.OBSERVED) { //observing done
			if (simulatedTime < state.observeTime) {
				System.out.println("Adaptation invoked too soon, waiting");
				return;
			}
			//Create data structure for processing
			final Set<InvariantInfo<?>> infos = extractInvariants(IRMInstances);

			if (fitness > state.oldFitness) {
				//Take child as new parent
			} else {
				//Keep parent
				restoreBackup(infos, state.backup);
			}

			//{Mark non-prospective specimen as dead end or utilize Simulated annealing}
			final TimeTrigger trigger = getTimeTrigger(process);
			trigger.setPeriod(state.originalPeriod);
			state.reset();
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
	 * Returns time trigger of the given process or null.
	 * @param process given process
	 * @return time trigger of the given process or null
	 */
	static private TimeTrigger getTimeTrigger(final ComponentProcess process) {
		for (Trigger trigger : process.getTriggers()) {
			if (trigger instanceof TimeTrigger) {
				return (TimeTrigger) trigger;
			}
		}
		return null;
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
	 * Returns TimeTrigger for invariant or null.
	 * @param info invariant info
	 * @return TimeTrigger for invariant or null
	 */
	static private TimeTrigger getTimeTrigger(InvariantInfo<?> info) {
		List<Trigger> triggers;
		if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ProcessInvariantInstance pii = info.getInvariant();
			triggers = pii.getComponentProcess().getTriggers();
		} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ExchangeInvariantInstance xii = info.getInvariant();
			triggers = xii.getEnsembleDefinition().getTriggers(); //TODO change to getEnsembleController when ready
		} else {
			return null;
		}
		for (Trigger trigger : triggers) {
			if (trigger instanceof TimeTrigger) {
				return (TimeTrigger) trigger;
			}
		}
		return null;
	}

	/**
	 * Returns current period of given invariant in info.
	 * @param info holder of invariant
	 * @return current period of given invariant
	 */
	static private long getCurrentPeriod(InvariantInfo<?> info) {
		final TimeTrigger trigger = getTimeTrigger(info);
		return trigger != null ? trigger.getPeriod() : 0;
	}

	/**
	 * Returns id of ExchangeInvariantInstance as knowledge manager id
	 * @param xii ExchangeInvariantInstance
	 * @return knowledge manager id
	 */
	static private String getProcessInvariantInstanceId(final ProcessInvariantInstance pii) {
		final ComponentInstance com = pii.getComponentProcess().getComponentInstance();
		return com.getKnowledgeManager().getId();
	}

	/**
	 * Returns id of ExchangeInvariantInstance as componentId-defName
	 * @param xii ExchangeInvariantInstance
	 * @return componentId-defName
	 */
	static private String getExchangeInvariantInstanceId(final ExchangeInvariantInstance xii) {
		final EnsembleDefinition def = xii.getEnsembleDefinition();
		final EnsembleController cont = null; //TODO getEnsembleController when ready
		return cont.getComponentInstance().getKnowledgeManager().getId() + "-" + def.getName();
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
			final TimeTrigger trigger = getTimeTrigger(info);
			if (trigger == null) {
				continue;
			}
			trigger.setPeriod(newPeriod);
			if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ProcessInvariantInstance pii = info.getInvariant();
				final String id = getProcessInvariantInstanceId(pii);
				backup.processes.put(id, change);
			} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ExchangeInvariantInstance xii = info.getInvariant();
				final String id = getExchangeInvariantInstanceId(xii);
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
			long newPeriod;
			if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ProcessInvariantInstance pii = info.getInvariant();
				final String id = getProcessInvariantInstanceId(pii);
				final Backup.Change change = backup.processes.get(id);
				if (change == null) {
					continue;
				}
				newPeriod = computeNewPeriod(info, change);
			} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ExchangeInvariantInstance xii = info.getInvariant();
				final String id = getExchangeInvariantInstanceId(xii);
				final Backup.Change change = backup.exchanges.get(id);
				if (change == null) {
					continue;
				}
				newPeriod = computeNewPeriod(info, change);
			} else {
				continue;
			}
			final TimeTrigger trigger = getTimeTrigger(info);
			if (trigger != null) {
				trigger.setPeriod(newPeriod);
			}
		}
	}

	/**
	 * Class holding the information needed to restore the previous state of the system.
	 */
	private static class Backup extends KnowledgeImpl {

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

		/**
		 * Simple wrapper around period change of an invariant.
		 */
		static class Change extends KnowledgeImpl {

			/** Period delta. */
			public long delta;

			/** Direction of change to revert the change. */
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

		public long observeTime;

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
			observeTime = 0;
		}
	}
}
