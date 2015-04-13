package cz.cuni.mff.d3s.irmsa.strategies.commons;

import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;

/**
 * EvolutionaryAdaptationManager delegates specific operations to implementation of
 * this interface.
 */
public interface EvolutionaryAdaptationManagerDelegate<T extends Backup> {

	/**
	 * Returns monitoring period.
	 * @return monitoring period
	 */
	default long getMonitorPeriod() {
		return EvolutionaryAdaptationManager.MONITORING_PERIOD;
	}

	/**
	 * Returns default adapting period.
	 * @return default adapting period
	 */
	default long getDefaultAdaptingPeriod() {
		return EvolutionaryAdaptationManager.ADAPTING_PERIOD;
	}

	/**
	 * Extracts invariants from IRMInstances and wraps them into InvariantInfos.
	 * @param irmInstances instances to extract from
	 * @return extracted invariants wrapped into InvariantInfos
	 */
	Set<InvariantInfo<?>> extractInvariants(List<IRMInstance> iRMInstances);

	/**
	 * Applies changes specified in infos.
	 * @param adaptees container of desired changes
	 * @param backup previous backup
	 * @return backup to revert the changes
	 */
	T applyChanges(Set<InvariantInfo<?>> adaptees, T backup);

	/**
	 * Returns observe time for given adaptees and invariant infos.
	 * @param adaptees adapted invariants
	 * @param infos all invariants
	 * @return observe time
	 */
	long computeObserveTime(Set<InvariantInfo<?>> adaptees, Set<InvariantInfo<?>> infos);

	/**
	 * Restores backup.
	 * @param backup Backup to restore
	 */
	void restoreBackup(Set<InvariantInfo<?>> infos, T backup);
}
