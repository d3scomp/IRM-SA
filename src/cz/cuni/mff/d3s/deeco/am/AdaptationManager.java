package cz.cuni.mff.d3s.deeco.am;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.assumptions.AssumptionMonitoring;
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
import cz.cuni.mff.d3s.deeco.monitor.Predictive;
import cz.cuni.mff.d3s.deeco.monitor.ProcessEnsembleMonitoring;
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
	// TODO change the following as one invariant can have multiple roots (i.e.
	// forest).
	private final Map<Invariant, Invariant> leafToParent;
	private final KnowledgeManager km;
	private final Map<String, Active> activeMonitors;
	private final Map<String, Predictive> predictiveMonitors;
	private final Map<String, AssumptionMonitoring> assumptionMonitors;
	private AdaptationRealTimeScheduler scheduler;

	public AdaptationManager(KnowledgeManager km,
			Map<String, Active> activeMonitors,
			Map<String, Predictive> predictiveMonitors,
			Map<String, AssumptionMonitoring> assumptionMonitors) {
		this.piToCp = new HashMap<>();
		this.eiToE = new HashMap<>();
		this.leafToParent = new HashMap<>();
		this.activeMonitors = activeMonitors;
		this.predictiveMonitors = predictiveMonitors;
		this.assumptionMonitors = assumptionMonitors;
		this.km = km;
	}

	public void setScheduler(AdaptationRealTimeScheduler scheduler) {
		this.scheduler = scheduler;
	}

	public boolean isToBeScheduled(Job job) {
		assert (job != null);
		Map<String, String> assignedRoles = new HashMap<>();
		List<Map<String, String>> assignments = new LinkedList<>();
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
			for (String role : topRoles)
				assignedRoles.put(role, "");
			assignedRoles
					.put(processInvariant.getOwner(), cpj.getComponentId());
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
			for (String role : topRoles)
				assignedRoles.put(role, "");
			assignedRoles.put(
					exchangeInvariant.getCoordinatorRole(),
					ej.getCoordinator());
			assignedRoles.put(
					exchangeInvariant.getMemberRole(),
					ej.getMember());
		}
		// find other valid assignments
		assert (assignedRoles != null);
		assert (top != null);
		assert (leaf != null);
		findAssignments(null, getRoleFilter(top), assignedRoles, assignments);
		if (!assignments.isEmpty()) {
			for (Map<String, String> assignment : assignments)
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
				pi.setMonitorProvider(scheduler);
				leafToParent.put(pi, invariant);
			}
		List<ExchangeInvariant> exchangeInvariants = findExchangeInvariant(invariant);
		for (ExchangeInvariant ei : exchangeInvariants)
			if (!eiToE.containsKey(ei)) {
				eiToE.put(ei, ei.getEnsemble());
				ei.setMonitorProvider(scheduler);
				leafToParent.put(ei, invariant);
			}
	}

	public ProcessEnsembleMonitoring createMonitoringFor(Job job) {
		return new ProcessEnsembleMonitoring(activeMonitors.get(job
				.getModelId()), predictiveMonitors.get(job.getModelId()), job,
				scheduler, km);
	}

	private void findAssignments(Object[] ids, Assumption filter,
			Map<String, String> partialRoleAssignment,
			List<Map<String, String>> result) {
		Object[] currentIds = null;
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
		String key = findKeyFroEmpty(partialRoleAssignment);
		if (key == null) {
			if (filter.evaluate(partialRoleAssignment))
				result.add(partialRoleAssignment);
		} else {
			Map<String, String> newPartialRoleAssignment;
			for (Object id : currentIds) {
				newPartialRoleAssignment = new HashMap<>(
						partialRoleAssignment);
				newPartialRoleAssignment.put(key, (String) id);
				findAssignments(currentIds, filter, newPartialRoleAssignment,
						result);
			}
		}
	}
	
	private String findKeyFroEmpty(Map<String, String> map) {
		for (String key : map.keySet())
			if (key.equals(""))
				return key;
		return null;
	}

	private boolean holds(Map<String, String> roleAssignment, Invariant top,
			Invariant leaf) {
		SATSolver solver = new SAT4JSolver(roleAssignment);
		solver.addTopInvariant(top);
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

	

//	private List<Assumption> findLeafAssumptions(IRMPrimitive primitive) {
//		List<Assumption> result = new LinkedList<>();
//		if (primitive instanceof Invariant) {
//			Invariant iPrimitive = (Invariant) primitive;
//			if (iPrimitive.isLeaf())
//				if (iPrimitive instanceof Assumption)
//					result.add((Assumption) iPrimitive);
//				else
//					result.addAll(findLeafAssumptions(iPrimitive.getOperation()));
//			else
//				result.addAll(findLeafAssumptions(iPrimitive.getOperation()));
//		} else if (primitive instanceof Operation) {
//			for (IRMPrimitive child : ((Operation) primitive).getChildren())
//				result.addAll(findLeafAssumptions(child));
//		}
//		return result;
//	}

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
