package cz.cuni.mff.d3s.irmsa.strategies.commons;

import java.util.List;
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
public abstract class TemplateAdaptationManager {

	/** Default period of monitoring. */
	static public final int MONITORING_PERIOD = 5000;

	/** Default period of adapting. */
	static public final int ADAPTING_PERIOD = 5000;

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

	/** Manager ID. */
	public String id;

	/** Holds the state of the adaptPeriods method. */
	public StateHolder<? extends Backup> state;

	/** Overall system fitness. */
	public Double fitness = 0.0;

	/**
	 * Only constructor.
	 */
	protected TemplateAdaptationManager(final StateHolder<?> initState) {
		this.id = createId();
		state = initState;
	}

	/**
	 * Creates id. Called in constructor!
	 * @return new id
	 */
	protected String createId() {
		return String.format("TemplateAdapatationManager_%s", UUID.randomUUID());
	}

	@Process
	@PeriodicScheduling(period = MONITORING_PERIOD, order = 10)
	static public <T extends Backup> void monitorOverallFitness(
			@In("id") String id,
			@Out("fitness") ParamHolder<Double> fitness) {
		fitness.value = 0.0;
		final ComponentProcess process = ProcessContext.getCurrentProcess();
		final AdaptationManagerDelegate<T> delegate = retrieveFromInternalData(ADAPTATION_DELEGATE);
		getTimeTrigger(process).setPeriod(delegate.getMonitorPeriod()); //set monitor period
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
		InvariantInfosStorage.storeInvariantInfos(id, infos);

		//Compute processes' fitnesses
		computeInvariantsFitness(infos);

		//Compute overall fitness
		fitness.value = invariantFitnessCombiner.combineInvariantFitness(infos);
		System.out.println("Overall System Fitness: " + fitness.value + "(at " + simulatedTime + ")");
	}

	@Process
	@PeriodicScheduling(period = ADAPTING_PERIOD, order = 10)
	static public <T extends Backup> void adapt(
			@In("id") String id,
			@In("fitness") Double fitness,
			@InOut("state") ParamHolder<StateHolder<T>> stateHolder) {
		final StateHolder<T> state = stateHolder.value;
		final AdaptationManagerDelegate<T> delegate = retrieveFromInternalData(ADAPTATION_DELEGATE);
		final ComponentProcess process = ProcessContext.getCurrentProcess();
		// get runtime model from the process context
		final RuntimeMetadata runtime = (RuntimeMetadata) process.getComponentInstance().eContainer();
		// get simulated time
		final long simulatedTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
		System.out.println("*** Adapting in runtime "+ runtime + " at time " + simulatedTime +" ***");
		// skipping the first run of this process as replicas are not disseminated yet
		if (simulatedTime <= 0) {
			Log.w("First invocation of the TemplateAdaptationManager. Skipping this adapting cycle.");
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
			} else {
				//Keep parent
				delegate.restoreBackup(infos, state.backup);
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
	 * Not type-safe method for retrieving objects from component's internal data.
	 * @param key key to search value for
	 * @return typed object from component's internal data
	 * @throws RuntimeException when no, null or wrongly typed data are provided
	 */
	static protected <T> T retrieveFromInternalData(final String key) {
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
			throw new RuntimeException(String.format("Could not retrieve %s from internal data.", key), e);
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
			final AdaptationManagerDelegate<? extends Backup> delegate,
			final StateHolder<?> state) {
		final TimeTrigger trigger = getTimeTrigger(process);
		trigger.setPeriod(delegate.getDefaultAdaptingPeriod());
		state.reset();
	}
}
