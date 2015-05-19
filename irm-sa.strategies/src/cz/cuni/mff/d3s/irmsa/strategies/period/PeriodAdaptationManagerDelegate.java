package cz.cuni.mff.d3s.irmsa.strategies.period;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irmsa.strategies.ComponentHelper;
import cz.cuni.mff.d3s.irmsa.strategies.commons.EvolutionaryAdaptationManagerDelegate;
import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;

public class PeriodAdaptationManagerDelegate implements EvolutionaryAdaptationManagerDelegate<PeriodBackup> {

	public static PrintWriter periodWriter;

	@Override
	public Set<InvariantInfo<?>> extractInvariants(
			final List<IRMInstance> irmInstances) {
		final boolean considerAssumptions =
				ComponentHelper.retrieveFromInternalData(
						PeriodAdaptationManager.CONSIDER_ASSUMPTIONS,
						Boolean.FALSE);
		final Set<InvariantInfo<?>> infos = new HashSet<>();
		for (IRMInstance irm: irmInstances) {
			for (InvariantInstance invariant: irm.getInvariantInstances()) {
				if (invariant instanceof ProcessInvariantInstance) {
					final ProcessInvariantInstance pii =
							(ProcessInvariantInstance) invariant;
					pii.getComponentProcess().setActive(true); //IRM may have turned it off
					infos.add(InvariantInfo.create(pii));
				} else if (invariant instanceof ExchangeInvariantInstance) {
					final ExchangeInvariantInstance xii =
							(ExchangeInvariantInstance) invariant;
					infos.add(InvariantInfo.create(xii));
					//xii.getEnsembleController().setActive(true); //TODO change to getEnsembleController when ready
				} else if (invariant instanceof AssumptionInstance && considerAssumptions) {
					final AssumptionInstance ai = (AssumptionInstance) invariant;
					infos.add(new InvariantInfo<AssumptionInstance>(ai, AssumptionInstance.class) {});
				}
			}
		}
		return infos;
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
	 * Returns id of ProcessInvariantInstance as knowledge manager id, colon,
	 * invariant refId, intended for organizing backups.
	 * @param xii ExchangeInvariantInstance
	 * @return id for ProcessInvariantInstance usable for backup
	 */
	static String getProcessInvariantInstanceId(final ProcessInvariantInstance pii) {
		final ComponentInstance com = pii.getComponentProcess().getComponentInstance();
		return com.getKnowledgeManager().getId() + ":" + pii.getInvariant().getRefID();
	}

	/**
	 * Returns id of ExchangeInvariantInstance as componentId-defName
	 * @param xii ExchangeInvariantInstance
	 * @return componentId-defName
	 */
	static String getExchangeInvariantInstanceId(final ExchangeInvariantInstance xii) {
		final EnsembleDefinition def = xii.getEnsembleDefinition();
//		final EnsembleController cont = null; //TODO getEnsembleController when ready
//		final String id = cont.getComponentInstance().getKnowledgeManager().getId();
		final String id = "TEMPORARY_FIXED_ID";
		return id + "-" + def.getName();
	}

	@Override
	public PeriodBackup applyChanges(
			final Set<InvariantInfo<?>> adaptees, final PeriodBackup backup) {
		backup.exchanges.clear();
		backup.processes.clear();
		for (InvariantInfo<?> info : adaptees) {
			final PeriodBackup.Change change = new PeriodBackup.Change(info.delta.longValue(), info.direction.opposite());
			final long currentPeriod = getCurrentPeriod(info);
			final long newPeriod = currentPeriod + info.direction.getCoef() * info.delta.longValue();
			final TimeTrigger trigger = getTimeTrigger(info);

			StringBuilder builder = new StringBuilder();
			builder.append(ProcessContext.getTimeProvider().getCurrentMilliseconds()).append(';');
			builder.append(currentPeriod).append(';');
			trigger.setPeriod(newPeriod);
			builder.append(newPeriod);

			periodWriter.println(builder.toString());
			periodWriter.flush();

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

	@Override
	public long computeObserveTime(final Set<InvariantInfo<?>> adaptees,
			final Set<InvariantInfo<?>> infos) {
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

	/**
	 * Computes new process/exchange period.
	 * @param info
	 * @param change
	 * @return new period
	 */
	static private long computeNewPeriod(final InvariantInfo<?> info,
			final PeriodBackup.Change change) {
		final long currentPeriod = getCurrentPeriod(info);
		return currentPeriod + change.direction.getCoef() * change.delta;
	}

	@Override
	public void restoreBackup(final Set<InvariantInfo<?>> infos, final PeriodBackup backup) {
		for (InvariantInfo<?> info: infos) {
			long newPeriod;
			if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ProcessInvariantInstance pii = info.getInvariant();
				final String id = getProcessInvariantInstanceId(pii);
				final PeriodBackup.Change change = backup.processes.get(id);
				if (change == null) {
					continue;
				}
				newPeriod = computeNewPeriod(info, change);
			} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ExchangeInvariantInstance xii = info.getInvariant();
				final String id = getExchangeInvariantInstanceId(xii);
				final PeriodBackup.Change change = backup.exchanges.get(id);
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
}
