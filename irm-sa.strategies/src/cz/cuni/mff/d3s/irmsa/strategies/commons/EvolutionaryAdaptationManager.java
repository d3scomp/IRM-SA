package cz.cuni.mff.d3s.irmsa.strategies.commons;

import static cz.cuni.mff.d3s.irmsa.strategies.ComponentHelper.retrieveFromInternalData;
import static cz.cuni.mff.d3s.irmsa.strategies.ComponentHelper.storeInInternalData;

import java.util.List;
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
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irmsa.IRMInstanceGenerator;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.AdapteeSelector;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DeltaComputor;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DirectionSelector;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.InvariantFitnessCombiner;

/**
 * Template adaptation manager for 1+1-ONLINE-EA.
 * Uses kind of template method / delegate design pattern.
 */
@Component
@SystemComponent
public abstract class EvolutionaryAdaptationManager {

	/** Default period of monitoring. */
	static public final int MONITORING_PERIOD = 5000;

	/** Default period of adapting. */
	static public final int ADAPTING_PERIOD = 5000;

	/** Run flag stored in internal data under this key. */
	static final String RUN_FLAG = "runFlag";

	/** Done flag stored in internal data under this key. */
	static final String DONE_FLAG = "doneFlag";

	/** Trace model stored in internal data under this key. */
	static final String TRACE_MODEL = "trace";

	/** Design model stored in internal data under this key. */
	static final String DESIGN_MODEL = "design";

	/** AdaptationManagerDelagate stored in internal data under this key. */
	static final String ADAPTATION_DELEGATE = "adaptationManagerDelegate";

	/** InvariantFitnessCombiner stored in internal data under this key. */
	static final String INVARIANT_FITNESS_COMBINER = "invariantFitnessCombiner";

	/** AdapteeSelector stored in internal data under this key. */
	static final String ADAPTEE_SELECTOR = "adapteeSelector";

	/** DirectionSelector stored in internal data under this key. */
	static final String DIRECTION_SELECTOR = "directionSelector";

	/** DeltaComputor stored in internal data under this key. */
	static final String DELTA_COMPUTOR = "deltaComputor";

	/** Adaptation bound stored in internal data under this key. */
	static final String ADAPTATION_BOUND = "adaptationBound";

	/** Constant meaning unbounded tries. */
	static final Integer TRIES_UNBOUDED = new Integer(-1);

	/** Manager ID. */
	public String id;

	/** Holds the state of the adapt method. */
	public StateHolder<? extends Backup> state;

	/** Number of tries to adapt left. -1 for unbounded. */
	public Integer maximumTries;

	/** Number of tries to adapt left. -1 for unbounded. */
	public Integer triesLeft;

	/** Overall system fitness. */
	public Double fitness = 0.0;

	/**
	 * Only constructor.
	 * @param initState initial state
	 * @param maximumTries maximal number of tries for adaptation, -1 for unbounded
	 */
	protected EvolutionaryAdaptationManager(final StateHolder<?> initState,
			final int maximumTries) {
		this.id = createId();
		state = initState;
		this.maximumTries = maximumTries;
		triesLeft = maximumTries;
	}

	/**
	 * Creates id. Called in constructor!
	 * @return new id
	 */
	protected String createId() {
		return String.format("EvolutionaryAdapatationManager_%s", UUID.randomUUID());
	}

	@Process
	@PeriodicScheduling(period = MONITORING_PERIOD, order = 5)
	static public <T extends Backup> void monitorOverallFitness(
			@In("id") String id,
			@Out("fitness") ParamHolder<Double> fitness) {
		final ComponentProcess process = ProcessContext.getCurrentProcess();
		final EvolutionaryAdaptationManagerDelegate<T> delegate = retrieveFromInternalData(ADAPTATION_DELEGATE);
		getTimeTrigger(process).setPeriod(delegate.getMonitorPeriod()); //set monitor period
		//
		final boolean run =  retrieveFromInternalData(RUN_FLAG, false);
		final boolean done =  retrieveFromInternalData(DONE_FLAG, false);
		if (!run || done) {
			return; //manager tells us not to run or our work is done
		}
		fitness.value = 0.0;
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
			InvariantInfosStorage.storeInvariantInfos(id, null);
			return;
		}

		//Create data structure for processing
		final Set<InvariantInfo<?>> infos = delegate.extractInvariants(IRMInstances);

		//Compute processes' fitnesses
		computeInvariantsFitness(infos);

		//Compute overall fitness
		fitness.value = invariantFitnessCombiner.combineInvariantFitness(infos);
		System.out.println("Overall System Fitness: " + fitness.value + "(at " + simulatedTime + ")");
		InvariantInfosStorage.storeInvariantInfos(id, infos);
	}

	@Process
	@PeriodicScheduling(period = ADAPTING_PERIOD, order = 10)
	static public <T extends Backup> void adapt(
			@In("id") String id,
			@In("fitness") Double fitness,
			@InOut("state") ParamHolder<StateHolder<T>> stateHolder,
			@In("maximumTries") Integer maximumTries,
			@InOut("triesLeft") ParamHolder<Integer> triesLeft) {
		final boolean run =  retrieveFromInternalData(RUN_FLAG, false);
		final boolean done =  retrieveFromInternalData(DONE_FLAG, false);
		if (!run || done) {
			return; //manager tells us not to run or our work is done
		}
		final StateHolder<T> state = stateHolder.value;
		final EvolutionaryAdaptationManagerDelegate<T> delegate = retrieveFromInternalData(ADAPTATION_DELEGATE);
		final ComponentProcess process = ProcessContext.getCurrentProcess();
		// get runtime model from the process context
		final RuntimeMetadata runtime = (RuntimeMetadata) process.getComponentInstance().eContainer();
		// get simulated time
		final long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		System.out.println("*** Adapting in runtime "+ runtime + " at time " + simulatedTime +" ***");
		// skipping the first run of this process as replicas are not disseminated yet
		if (simulatedTime <= 0) {
			Log.w("First invocation of the EvolutionaryAdaptationManager. Skipping this adapting cycle.");
			return;
		}

		// get variations from the process context
		final InvariantFitnessCombiner invariantFitnessCombiner =
				retrieveFromInternalData(INVARIANT_FITNESS_COMBINER);
		final AdapteeSelector adapteeSelector =
				retrieveFromInternalData(ADAPTEE_SELECTOR);
		final DirectionSelector directionSelector =
				retrieveFromInternalData(DIRECTION_SELECTOR);
		final DeltaComputor deltaComputor =
				retrieveFromInternalData(DELTA_COMPUTOR);

		//retrieve invariant infos from last monitor run
		final Set<InvariantInfo<?>> infos = InvariantInfosStorage.retrieveInvariantInfos(id);

		if (infos == null) {
			//nothing to adapt, reset state
			resetAdaptState(process, delegate, state);
			return;
		}

		if (state.state == State.STARTED) { //first part of adaptation
			state.oldFitness = fitness;
			System.out.println("OLD FITNESS: " + state.oldFitness + "(at " + simulatedTime + ")");
			final double adaptionBound = retrieveFromInternalData(ADAPTATION_BOUND);
			if (state.oldFitness >= adaptionBound) { //too good to mess with the system
				resetAdaptState(process, delegate, state);
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
			state.backup = delegate.applyChanges(adaptees, state.backup);

			//{Compute observe time}
			final long observeTime = delegate.computeObserveTime(adaptees, infos);

			//Run for observe time
			state.state = State.OBSERVED;
			//change period of this process
			final TimeTrigger trigger = getTimeTrigger(process);
			if (observeTime > trigger.getPeriod()) { //changing period takes effect only the run after the next one
				trigger.setPeriod(observeTime - trigger.getPeriod());
			}
			state.observeTime = simulatedTime + observeTime;
			System.out.println("!!!ADAPTING!!!");
		} else if (state.state == State.OBSERVED) { //observing done
			if (simulatedTime < state.observeTime) {
				System.out.println("Adaptation invoked too soon, waiting");
				return;
			}

			System.out.println("NEW FITNESS: " + fitness);
			if (fitness > state.oldFitness) {
				//Take child as new parent

				//are we done?
				final double adaptionBound = retrieveFromInternalData(ADAPTATION_BOUND);
				if (fitness >= adaptionBound) {
					System.out.println("Adaptation successfully ended.");
					storeInInternalData(DONE_FLAG, true);
				}
			} else {
				//Keep parent
				delegate.restoreBackup(infos, state.backup);

				if (!maximumTries.equals(TRIES_UNBOUDED)) {
					triesLeft.value = triesLeft.value - 1;
					//are we done?
					if (triesLeft.value <= 0) {
						System.out.println("Adaptation failed, abort.");
						storeInInternalData(DONE_FLAG, true);
						triesLeft.value = maximumTries;
					}
				}
			}

			//inform variations about adaptation results
			final double improvement = fitness - state.oldFitness;
			invariantFitnessCombiner.adaptationImprovement(improvement, infos);
			adapteeSelector.adaptationImprovement(improvement, infos);
			directionSelector.adaptationImprovement(improvement, infos);
			deltaComputor.adaptationImprovement(improvement, infos);

			//{Mark non-prospective specimen as dead end or utilize Simulated annealing}
			final TimeTrigger trigger = getTimeTrigger(process);
			trigger.setPeriod(delegate.getDefaultAdaptingPeriod());
			state.reset();
		} else {
			Log.w("Unknown state " + state.state);
		}
	}

	/**
	 * Returns time trigger of the given process or null.
	 * @param process given process
	 * @return time trigger of the given process or null
	 */
	static protected TimeTrigger getTimeTrigger(final ComponentProcess process) {
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
	static protected void computeInvariantsFitness(final Set<InvariantInfo<?>> infos) {
		for (InvariantInfo<?> info : infos) {
			InvariantInstance instance = info.getInvariant();
			info.fitness = instance.getFitness();
		}
	}

	/**
	 * Resets process' period and manager's state.
	 * @param process adapt process
	 * @param delegate adaptation manager delegate, provides period
	 * @param state  manager's current state
	 */
	static protected void resetAdaptState(final ComponentProcess process,
			final EvolutionaryAdaptationManagerDelegate<? extends Backup> delegate,
			final StateHolder<?> state) {
		final TimeTrigger trigger = getTimeTrigger(process);
		trigger.setPeriod(delegate.getDefaultAdaptingPeriod());
		state.reset();
	}
}
