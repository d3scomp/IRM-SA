package cz.cuni.mff.d3s.irmsa.strategies.commons;

import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;

public abstract class StateHolder<T extends Backup> extends KnowledgeImpl {

	/** State of the method adaptPeriods. */
	public State state;

	/** Fitness of the previous configuration. */
	public double oldFitness;

	/** Backup to restore the previous configuration. */
	public T backup;

	/** Observing last at least until this time. */
	public long observeTime;

	/**
	 * Only constructor.
	 */
	public StateHolder() {
		this.name = createStateHolderName();
		this.type = createStateHolderType();
		reset();
	}

	/**
	 * Returns name of the state holder. Called in constructor!
	 * @return name of the state holder
	 */
	protected abstract String createStateHolderName();

	/**
	 * Returns type of the state holder. Called in constructor!
	 * @return type of the state holder
	 */
	protected abstract String createStateHolderType();

	/**
	 * Resets the state.
	 */
	public final void reset() {
		state = State.STARTED;
		oldFitness = 0.0;
		backup = resetBackup(backup);
		observeTime = 0;
	}

	/**
	 * Resets backup. Called in constructor!
	 * @param backup previous backup, may be null, reuse encouraged
	 * @return new backup
	 */
	protected abstract T resetBackup(T backup);
}
