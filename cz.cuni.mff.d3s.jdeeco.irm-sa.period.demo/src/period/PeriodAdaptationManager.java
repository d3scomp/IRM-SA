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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irm.model.design.ExchangeInvariant;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.Invariant;
import cz.cuni.mff.d3s.irm.model.design.ProcessInvariant;
import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance;
import cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irmsa.ArchitectureReconfigurator;
import cz.cuni.mff.d3s.irmsa.EMFHelper;
import cz.cuni.mff.d3s.irmsa.IRMInstanceGenerator;
import cz.cuni.mff.d3s.irmsa.satsolver.SATSolver;
import cz.cuni.mff.d3s.irmsa.satsolver.SATSolverPreProcessor;

@Component
@SystemComponent
public class PeriodAdaptationManager {
	
	public static final String TRACE_MODEL = "trace";
	public static final String DESIGN_MODEL = "design";  
	
	static final String MODELS_BASE_PATH = "designModels/";
	static final String XMIFILE_PREFIX = "default_";
	static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH + "default.irmdesign";	
	
	@Process
	@PeriodicScheduling(period=Settings.ADAPTATION_PERIOD, order=10)
	static public void reason(@In("id") String id) {
		// get runtime model from the process context
		RuntimeMetadata runtime = (RuntimeMetadata) ProcessContext.getCurrentProcess().getComponentInstance().eContainer();
		// get simulated time
		long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		System.out.println("*** Reasoning in runtime "+ runtime + " at time " + simulatedTime +" ***");
		// skipping the first run of this process as replicas are not disseminated yet
		if (simulatedTime == 0) {
			Log.w("First invocation of the PeriodAdaptationManager. Skipping this reasoning cycle.");
			return;
		}
		// get architecture, design, and trace models from the process context
		Architecture architecture = ProcessContext.getArchitecture();
		IRM design = (IRM) ProcessContext.getCurrentProcess().getComponentInstance().getInternalData().get(DESIGN_MODEL);
		TraceModel trace = (TraceModel) ProcessContext.getCurrentProcess().getComponentInstance().getInternalData().get(TRACE_MODEL);

		
		
		
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
		final double oldFitness = combineInvariantFitness(infos);
		
		//Select a (set of) processes to adapt
		final Set<InvariantInfo<?>> adaptees = selectAdaptees(infos);
		
		//Select direction(s)
		selectDirections(adaptees);
		
		//Compute delta(s)
		computeDeltas(adaptees);
		
		//Create child by applying the changes
		final Backup backup = applyChanges(adaptees);
		
		//{Compute observe time}
		final int observeTime = computeObserveTime(adaptees, infos);
		
		//Run for observe time
		//??? - change period of this process?
		
		//Compute fitness functions of invariants again
		
		//Compute overall fitness
		final double newFitness = 0.0; //TODO
		
		if (newFitness > oldFitness) {
			//Take child as new parent
		} else {
			//Keep parent
			restoreBackup(backup);
		}
		
		//{Mark non-prospective specimen as dead end or utilize Simulated annealing}
		//??? - restore original period?
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
}