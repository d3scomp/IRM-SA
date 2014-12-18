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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irm.model.design.ExchangeInvariant;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.Invariant;
import cz.cuni.mff.d3s.irm.model.design.ProcessInvariant;
import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irmsa.IRMInstanceGenerator;

/**
 * Handles adaptation of invariant periods.
 */
@Component
@SystemComponent
public class PeriodAdaptationManager {

	static private final String TRACE_MODEL = "trace";
	static private final String DESIGN_MODEL = "design";

	/** Holds the state of the adaptPeriods method. */
	public StateHolder state = new StateHolder();

	/** Method for combining overall system fitness. */
	public InvariantFitnessCombiner invariantFitnessCombiner;

	/** Method for selecting invariant instances to adapt. */
	public AdapteeSelector adapteeSelector;

	/** Method for selecting direction of adaptation. */
	public DirectionSelector directionSelector;

	/** Method for computing period delta of adaptation. */
	public DeltaComputor deltaComputor;

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
				c.getInternalData().put(PeriodAdaptationManager.DESIGN_MODEL, design);
				c.getInternalData().put(PeriodAdaptationManager.TRACE_MODEL, trace);
			}
		}
	}

	/**
	 * Factory method for creating builder and manager itself.
	 * @return newly created manager builder
	 */
	public static PeriodAdaptationManagerBuilder create() {
		return new PeriodAdaptationManagerBuilder();
	}

	@Process
	@PeriodicScheduling(period=Settings.ADAPTATION_PERIOD, order=10)
	static public void adaptPeriods(
			@In("id") String id,
			@InOut("state") ParamHolder<StateHolder> stateHolder,
			@In("invariantFitnessCombiner") InvariantFitnessCombiner invariantFitnessCombiner,
			@In("adapteeSelector") AdapteeSelector adapteeSelector,
			@In("directionSelector") DirectionSelector directionSelector,
			@In("deltaComputor") DeltaComputor deltaComputor) {
		final StateHolder state = stateHolder.value;
		// get runtime model from the process context
		final RuntimeMetadata runtime = (RuntimeMetadata) ProcessContext.getCurrentProcess().getComponentInstance().eContainer();
		// get simulated time
		final long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		System.out.println("*** Adapting periods in runtime "+ runtime + " at time " + simulatedTime +" ***");
		// skipping the first run of this process as replicas are not disseminated yet
		if (simulatedTime == 0) {
			Log.w("First invocation of the PeriodAdaptationManager. Skipping this reasoning cycle.");
			//TODO set period of this process
			return;
		}

		// get architecture, design, and trace models from the process context
		final Architecture architecture = ProcessContext.getArchitecture();
		final IRM design = retrieveDesign();
		final TraceModel trace = retrieveTrace();

		// generate the IRM runtime model instances
		final IRMInstanceGenerator generator =
				new IRMInstanceGenerator(architecture, design, trace);
		final List<IRMInstance> IRMInstances = generator.generateIRMInstances();

		if (IRMInstances.isEmpty()) {
			//nothing to adapt, reset state
			state.reset();
			//TODO reset original period of this process
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
			final int observeTime = computeObserveTime(adaptees, infos);

			//Run for observe time
			state.state = State.OBSERVED;
			//??? - change period of this process?
			//state.originalPeriod = ???
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
				restoreBackup(state.backup);
			}

			//{Mark non-prospective specimen as dead end or utilize Simulated annealing}
			state.reset();
			//TODO restore original period?
		} else {
			Log.w("Unknown state " + state.state);
		}
	}

	/**
	 * Retrieves design stored in component's internal data by prepare method.
	 * @return IRM design
	 */
	static private IRM retrieveDesign() {
		final ComponentInstance instance =
				ProcessContext.getCurrentProcess().getComponentInstance();
		final Object design = instance.getInternalData().get(DESIGN_MODEL);
		if (design != null && design instanceof IRM) {
			return (IRM) design;
		} else {
			throw new RuntimeException("Could not retrieve design model. Make sure to run PeriodAdaptationManager.prepare");
		}
	}

	/**
	 * Retrieves trace stored in component's internal data by prepare method.
	 * @return trace model
	 */
	static private TraceModel retrieveTrace() {
		final ComponentInstance instance =
				ProcessContext.getCurrentProcess().getComponentInstance();
		final Object trace = instance.getInternalData().get(TRACE_MODEL);
		if (trace != null && trace instanceof TraceModel) {
			return (TraceModel) trace;
		} else {
			throw new RuntimeException("Could not retrieve trace model. Make sure to run PeriodAdaptationManager.prepare");
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
			if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ProcessInvariantInstance pii = info.getInvariant();
//				final double f = pii.getFitness(); //TODO implement
				final double f = 0.0;
				info.fitness = f;
			} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ExchangeInvariantInstance xii  = info.getInvariant();
//				final double f = xii.getFitness();
				final double f = 0.0;
				info.fitness = f;
			}
		}
	}

	static private Backup applyChanges(final Set<InvariantInfo<?>> infos) {
//		if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
//			final ProcessInvariantInstance pii = info.getInvariant();
//			final ProcessInvariant pi = (ProcessInvariant) pii.getInvariant();
////			final long min = pi.getProcessMinPeriod(); //TODO implement
//			final long min = 0;
////			final long max = pi.getProcessMaxPeriod(); //TODO implement
//			final long max = 0;
//			final long per = pi.getProcessPeriod();
//			final long target = per + info.delta * info.direction.getCoef();
//		} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
//			final ExchangeInvariantInstance xii = info.getInvariant();
//			//a) weighted average
////			info.weight = info.fitness * xii.getWeight() /  infos.size(); //TODO implement
//			info.weight = 1.0;
//		}
		//TODO
		return null;
	}

	static private int computeObserveTime(final Set<InvariantInfo<?>> adaptees, final Set<InvariantInfo<?>> infos) {
		//TODO
		return 0;
	}

	static private void restoreBackup(final Backup backup) {
		//TODO
	}

	/**
	 * Only constructor.
	 * Required fields must be set too!
	 */
	protected PeriodAdaptationManager() {
		//nothing
	}

	/**
	 * Class holding the information needed to restore the previous state of the system.
	 */
	private static class Backup {

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
		public int originalPeriod;

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
