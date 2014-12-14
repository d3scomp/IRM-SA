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
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
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

	@Process
	@PeriodicScheduling(period=Settings.ADAPTATION_PERIOD, order=10)
	static public void adaptPeriods(
			@In("id") String id,
			@InOut("state") ParamHolder<StateHolder> stateHolder) {
		final StateHolder state = stateHolder.value;
		// get runtime model from the process context
		final RuntimeMetadata runtime = (RuntimeMetadata) ProcessContext.getCurrentProcess().getComponentInstance().eContainer();
		// get simulated time
		final long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		System.out.println("*** Adapting periods in runtime "+ runtime + " at time " + simulatedTime +" ***");
		// skipping the first run of this process as replicas are not disseminated yet
		if (simulatedTime == 0) {
			Log.w("First invocation of the PeriodAdaptationManager. Skipping this reasoning cycle.");
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

		if (IRMInstances.size() == 0) {
			//nothing to adapt, reset state
			state.reset();
			//TODO reset original period of this process
			return;
		}

		if (state.state == State.STARTED) { //first part of adaptation
			//Create data structure for processing
			final Set<InvariantInfo<?>> infos = new HashSet<>();
			//process invariants
			for (ProcessTrace t: trace.getProcessTraces()) {
				infos.add(InvariantInfo.create(t));
			}
			//exchange invariants
			for (EnsembleDefinitionTrace t: trace.getEnsembleDefinitionTraces()) {
				infos.add(InvariantInfo.create(t));
			}

			//Compute overall fitness
			computeInvariantsFitness(infos);

			//Compute overall fitness
			state.oldFitness = combineInvariantFitness(infos);

			//Select a (set of) processes to adapt
			final Set<InvariantInfo<?>> adaptees = selectAdaptees(infos);

			//Select direction(s)
			selectDirections(adaptees);

			//Compute delta(s)
			computeDeltas(adaptees);

			//Create child by applying the changes
			state.backup = applyChanges(adaptees);

			//{Compute observe time}
			final int observeTime = computeObserveTime(adaptees, infos);

			//Run for observe time
			state.state = State.OBSERVED;
			//??? - change period of this process?
			//state.originalPeriod = ???
		} else if (state.state == State.OBSERVED) { //observing done
			//Compute fitness functions of invariants again

			//Compute overall fitness
			final double newFitness = 0.0; //TODO

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

	static private double computeInvariantLevel(final Invariant invariant) {
		//TODO
		return 1.0;
	}

	static private void computeInvariantsFitness(final Set<InvariantInfo<?>> infos) {
		for (InvariantInfo<?> info : infos) {
			if (ProcessTrace.class.isAssignableFrom(info.clazz)) {
				final ProcessTrace t = info.getTrace();
				final ComponentProcess cp = t.getFrom();
//				final double f = (double) cp.getFitnessMethod().invoke(null, cp.getFitnessParameters());
				final double f = 0;
				info.fitness = f;
			} else if (EnsembleDefinitionTrace.class.isAssignableFrom(info.clazz)) {
				final EnsembleDefinitionTrace t = info.getTrace();
				final EnsembleDefinition ed = t.getFrom();
				final Exchange ke = ed.getKnowledgeExchange();
//				final double f = (double) ke.getFitnessMethod().invoke(null, ke.getFitnessParameters());
				final double f = 0;
				info.fitness = f;
			}
		}
	}

	static private double combineInvariantFitness(final Set<InvariantInfo<?>> infos) {
		double result = 0.0;
		for (InvariantInfo<?> info : infos) {
			if (ProcessTrace.class.isAssignableFrom(info.clazz)) {
				final ProcessTrace t = info.getTrace();
				final ProcessInvariant invariant = (ProcessInvariant) t.getTo();
				//a) weighted average
//				info.weight = info.fitness * invariant.getWeight() /  infos.size();

				//b) upper level monitors more important
				info.level = computeInvariantLevel(invariant);
				info.weight = info.fitness * info.level;
			} else if (EnsembleDefinitionTrace.class.isAssignableFrom(info.clazz)) {
				final EnsembleDefinitionTrace t = info.getTrace();
				final ExchangeInvariant invariant = (ExchangeInvariant) t.getTo();
				//a) weighted average
//				info.weight = info.fitness * invariant.getWeight() /  infos.size();

				//b) upper level monitors more important
				info.level = computeInvariantLevel(invariant);
				info.weight = info.fitness * info.level;
			}
			result += info.weight;
		}
		return result;
	}

	static private Set<InvariantInfo<?>> selectAdaptees(final Set<InvariantInfo<?>> infos) {
		//TODO
		//a)higher ones in tree
		//b)ones with lower fitness
		//c)most/least satisfied
		return infos;
	}

	static private void selectDirections(final Set<InvariantInfo<?>> infos) {
		for (InvariantInfo<?> info : infos) {
			//TODO
			info.direction = Direction.NO;
		}
	}

	static private void computeDeltas(final Set<InvariantInfo<?>> infos) {
		for (InvariantInfo<?> info : infos) {
			//TODO
			//a)fixed
			//b)from distance to bound
			info.delta = 0;
		}
	}

	static private Backup applyChanges(final Set<InvariantInfo<?>> infos) {
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
	 * Direction of period adjustment.
	 */
	private enum Direction {
		UP {
			@Override
			public byte getCoef() {
				return 1;
			}
		},
		DOWN {
			@Override
			public byte getCoef() {
				return  -1;
			}
		},
		NO {
			@Override
			public byte getCoef() {
				return 0;
			}
		};

		/**
		 * Returns coefficient of this direction. Intended to multiply the delta.
		 * @return coefficient of this direction
		 */
		public abstract byte getCoef();
	}

	/**
	 * Class holding the information needed to restore the previous state of the system.
	 */
	private static class Backup {

	}

	/**
	 * Common holder of information about invariants.
	 * @param <T> either ProcessTrace or EnsembleDefinitionTrace
	 */
	private static class InvariantInfo<T> {

		/**
		 * Factory method to wrap given ProcessTrace.
		 * @param processTrace ProcessTrace to wrap
		 * @return newly created InvariantInfo wrapping given ProcessTrace
		 */
		static InvariantInfo<ProcessTrace> create(final ProcessTrace processTrace) {
			return new InvariantInfo<ProcessTrace>(processTrace, ProcessTrace.class);
		}

		/**
		 * Factory method to wrap given EnsembleDefinitionTrace.
		 * @param processTrace EnsembleDefinitionTrace to wrap
		 * @return newly created InvariantInfo wrapping given EnsembleDefinitionTrace
		 */
		static InvariantInfo<EnsembleDefinitionTrace> create(final EnsembleDefinitionTrace ensembleTrace) {
			return new InvariantInfo<EnsembleDefinitionTrace>(ensembleTrace, EnsembleDefinitionTrace.class);
		}

		/** Class of the trace or supertype. */
		final Class<T> clazz;

		/** Wrapped trace. */
		final T trace;

		/** Invariant fitness. */
		double fitness;

		/** Invariant weight. */
		double weight;

		/** Invariant level. */
		double level;

		/** Direction of the adaptation. */
		Direction direction;

		/** Change to the invariant period. Should be always non-negative. */
		int delta;

		/**
		 * Only and private constructor.
		 * @param trace invariant to wrap
		 * @param clazz class of trace
		 */
		private InvariantInfo(final T trace, final Class<T> clazz) {
			this.trace = trace;
			this.clazz = clazz;
		}

		/**
		 * Convenient method to cast trace.
		 * Check clazz before to know the actual
		 * @return
		 */
		@SuppressWarnings("unchecked")
		<X> X getTrace() {
			return (X) trace;
		}

		@Override
		public int hashCode() {
			return trace == null ? 0 : trace.hashCode();
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
