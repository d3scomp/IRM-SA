package cz.cuni.mff.d3s.deeco.am;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.irm.Assumption;
import cz.cuni.mff.d3s.deeco.irm.ExchangeInvariant;
import cz.cuni.mff.d3s.deeco.irm.IRMPrimitive;
import cz.cuni.mff.d3s.deeco.irm.Invariant;
import cz.cuni.mff.d3s.deeco.irm.Operation;
import cz.cuni.mff.d3s.deeco.irm.ProcessInvariant;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.monitor.Active;
import cz.cuni.mff.d3s.deeco.monitor.Monitoring;
import cz.cuni.mff.d3s.deeco.monitor.Predictive;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;
import cz.cuni.mff.d3s.deeco.sat.SAT4JSolver;
import cz.cuni.mff.d3s.deeco.sat.SATSolver;
import cz.cuni.mff.d3s.deeco.scheduling.AdaptationRealTimeScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.ComponentProcessJob;
import cz.cuni.mff.d3s.deeco.scheduling.EnsembleJob;
import cz.cuni.mff.d3s.deeco.scheduling.Job;

public class AdaptationManager {

	private final Map<ProcessInvariant, ComponentProcess> piToCp;
	private final Map<ExchangeInvariant, Ensemble> eiToE;
	private final Map<Invariant, Invariant> leafToParent;
	private final KnowledgeManager km;
	private final Map<String, Active> activeMonitors;
	private final Map<String, Predictive> predictiveMonitors;
	private AdaptationRealTimeScheduler scheduler;

	public AdaptationManager(KnowledgeManager km,
			Map<String, Active> activeMonitors,
			Map<String, Predictive> predictiveMonitors) {
		this.piToCp = new HashMap<>();
		this.eiToE = new HashMap<>();
		this.leafToParent = new HashMap<>();
		this.activeMonitors = activeMonitors;
		this.predictiveMonitors = predictiveMonitors;
		this.km = km;
	}

	public void setScheduler(AdaptationRealTimeScheduler scheduler) {
		this.scheduler = scheduler;
	}

	public boolean isToBeScheduled(Job job) {
		assert (job != null);
		List<String> assignedRoles = null;
		List<List<String>> assignments = new LinkedList<>();
		Invariant top = null, leaf = null;
		if (job instanceof ComponentProcessJob) {
			ComponentProcessJob cpj = (ComponentProcessJob) job;
			ComponentProcess jobProcess = cpj.getComponentProcess();
			ProcessInvariant processInvariant = null;
			for (ProcessInvariant pi : piToCp.keySet())
				if (jobProcess.equals(piToCp.get(pi))) {
					processInvariant = pi;
					break;
				}
			assert (processInvariant != null);
			leaf = processInvariant;
			top = leafToParent.get(processInvariant);
			List<String> topRoles = top.getRoles();
			assignedRoles = Arrays.asList(new String[topRoles.size()]);
			assignedRoles.set(topRoles.indexOf(processInvariant.getOwner()),
					cpj.getComponentId());
		} else if (job instanceof EnsembleJob) {
			EnsembleJob ej = (EnsembleJob) job;
			Ensemble jobEnsemble = ej.getEnsemble();
			ExchangeInvariant exchangeInvariant = null;
			for (ExchangeInvariant ei : eiToE.keySet())
				if (jobEnsemble.equals(eiToE.get(ei))) {
					exchangeInvariant = ei;
					break;
				}

			assert (exchangeInvariant != null);
			leaf = exchangeInvariant;
			top = leafToParent.get(exchangeInvariant);
			List<String> topRoles = top.getRoles();
			assignedRoles = Arrays.asList(new String[topRoles.size()]);
			assignedRoles.set(
					topRoles.indexOf(exchangeInvariant.getCoordinatorRole()),
					ej.getCoordinator());
			assignedRoles.set(
					topRoles.indexOf(exchangeInvariant.getMemberRole()),
					ej.getMember());
		}
		// find other valid assignments
		assert (assignedRoles != null);
		assert (top != null);
		assert (leaf != null);
		findAssignments(null, getRoleFilter(top), assignedRoles, assignments);
		if (!assignments.isEmpty()) {
			for (List<String> assignment : assignments)
				if (!holds(assignment, top, leaf))
					return false;
			return true;
		}
		return false;
	}

	public void addIRMInvariant(Invariant invariant) {
		List<ProcessInvariant> processInvariants = findProcessInvariant(invariant);
		for (ProcessInvariant pi : processInvariants)
			if (!piToCp.containsKey(pi)) {
				piToCp.put(pi, pi.getProcess());
				leafToParent.put(pi, invariant);
			}
		List<ExchangeInvariant> exchangeInvariants = findExchangeInvariant(invariant);
		for (ExchangeInvariant ei : exchangeInvariants)
			if (!eiToE.containsKey(ei)) {
				eiToE.put(ei, ei.getEnsemble());
				leafToParent.put(ei, invariant);
			}
	}

	public Monitoring createMonitoringFor(Job job) {
		return new Monitoring(activeMonitors.get(job.getModelId()),
				predictiveMonitors.get(job.getModelId()), job, scheduler, km);
	}

	private void findAssignments(Object[] ids, Assumption filter,
			List<String> partialRoleAssignment, List<List<String>> result) {
		Object[] currentIds = null;
		;
		if (ids == null) {
			try {
				currentIds = (Object[]) km
						.getKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID);

			} catch (KMException kme) {
				Log.e("AdaptationManager exception", kme);
				return;
			}
		} else
			currentIds = ids;
		int index = partialRoleAssignment.indexOf(null);
		if (index < 0) {
			if (filter.evaluate(partialRoleAssignment))
				result.add(partialRoleAssignment);
		} else {
			List<String> newPartialRoleAssignment;
			for (Object id : currentIds) {
				newPartialRoleAssignment = new LinkedList<>(
						partialRoleAssignment);
				newPartialRoleAssignment.set(index, (String) id);
				findAssignments(currentIds, filter, newPartialRoleAssignment,
						result);
			}
		}
	}

	private boolean holds(List<String> roleAssignment, Invariant top,
			Invariant leaf) {
		SATSolver solver = new SAT4JSolver(top, roleAssignment);
		return isPathSelected(leaf, solver.solve());
	}

	private boolean isPathSelected(Invariant leaf, List<IRMPrimitive> solution) {
		IRMPrimitive node = leaf;
		do {
			node = node.getParent();
		} while (node.getParent() != null && solution.contains(node));
		return node.isRoot();
	}

	private Assumption getRoleFilter(Invariant top) {
		assert (top != null);
		Operation o = top.getOperation();
		if (o != null) {
			for (IRMPrimitive primitive : o.getChildren())
				if (primitive instanceof Assumption)
					return (Assumption) primitive;
		}
		return null;
	}

	private List<ProcessInvariant> findProcessInvariant(IRMPrimitive primitive) {
		List<ProcessInvariant> result = new LinkedList<>();
		if (primitive instanceof ProcessInvariant) {
			result.add((ProcessInvariant) primitive);
		} else if (primitive instanceof Operation) {
			for (IRMPrimitive child : ((Operation) primitive).getChildren())
				result.addAll(findProcessInvariant(child));
		} else if (!(primitive instanceof Assumption)
				&& !(primitive instanceof ExchangeInvariant)) {
			result.addAll(findProcessInvariant(((Invariant) primitive)
					.getOperation()));
		}
		return result;
	}

	private List<ExchangeInvariant> findExchangeInvariant(IRMPrimitive primitive) {
		List<ExchangeInvariant> result = new LinkedList<>();
		if (primitive instanceof ExchangeInvariant) {
			result.add((ExchangeInvariant) primitive);
		} else if (primitive instanceof Operation) {
			for (IRMPrimitive child : ((Operation) primitive).getChildren())
				result.addAll(findExchangeInvariant(child));
		} else if (!(primitive instanceof Assumption)
				&& !(primitive instanceof ProcessInvariant)) {
			result.addAll(findExchangeInvariant(((Invariant) primitive)
					.getOperation()));
		}
		return result;
	}

	// private ProcessInvariant findProcessInvariantForComponentProcess(
	// List<ProcessInvariant> processInvariants,
	// ComponentProcess componentProcess) {
	// for (ProcessInvariant processInvariant : processInvariants)
	// if (componentProcess.equals(processInvariant.getProcess()))
	// return processInvariant;
	// return null;
	// }
	//
	// private ExchangeInvariant findProcessInvariantForComponentProcess(
	// List<ExchangeInvariant> exchangeInvariants,
	// Ensemble ensemble) {
	// for (ExchangeInvariant exchangeInvariant : exchangeInvariants)
	// if (ensemble.equals(exchangeInvariant.getEnsemble()))
	// return exchangeInvariant;
	// return null;
	// }

}
