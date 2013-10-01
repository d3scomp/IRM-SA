package cz.cuni.mff.d3s.deeco.am;

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
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;
import cz.cuni.mff.d3s.deeco.sat.SAT4JSolver;
import cz.cuni.mff.d3s.deeco.sat.SATSolver;
import cz.cuni.mff.d3s.deeco.scheduling.ComponentProcessTask;
import cz.cuni.mff.d3s.deeco.scheduling.EnsembleTask;
import cz.cuni.mff.d3s.deeco.scheduling.Task;

public class AdaptationManager {

	private final Map<String, ProcessInvariant> pInvariants;
	private final Map<String, ExchangeInvariant> eInvariants;
	// TODO change the following as one invariant can have multiple roots (i.e.
	// forest).
	private final Map<Invariant, Invariant> leafToParent;
	private KnowledgeManager km;

	public AdaptationManager() {
		this.pInvariants = new HashMap<>();
		this.eInvariants = new HashMap<>();
		this.leafToParent = new HashMap<>();
	}

	public void setKnowledgeManager(KnowledgeManager km) {
		this.km = km;
	}

	public boolean isToBeScheduled(Task task) {
		assert (task != null);
		Map<String, String> assignedRoles = new HashMap<>();
		List<Map<String, String>> assignments = new LinkedList<>();
		Invariant top = null, leaf = null;
		if (task instanceof ComponentProcessTask) {
			ComponentProcessTask cpt = (ComponentProcessTask) task;
			ComponentProcess taskProcess = cpt.getComponentProcess();
			ProcessInvariant processInvariant = pInvariants.get(taskProcess
					.getId());
			assert (processInvariant != null);
			leaf = processInvariant;
			top = leafToParent.get(processInvariant);
			List<String> topRoles = top.getRoles();
			for (String role : topRoles)
				assignedRoles.put(role, "");
			assignedRoles
					.put(processInvariant.getOwner(), cpt.getComponentId());
		} else if (task instanceof EnsembleTask) {
			EnsembleTask et = (EnsembleTask) task;
			Ensemble jobEnsemble = et.getEnsemble();
			ExchangeInvariant exchangeInvariant = eInvariants.get(jobEnsemble
					.getExchangeId());
			assert (exchangeInvariant != null);
			leaf = exchangeInvariant;
			top = leafToParent.get(exchangeInvariant);
			List<String> topRoles = top.getRoles();
			for (String role : topRoles)
				assignedRoles.put(role, "");
			assignedRoles.put(exchangeInvariant.getCoordinatorRole(),
					et.getCoordinator());
			assignedRoles
					.put(exchangeInvariant.getMemberRole(), et.getMember());
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
			if (!pInvariants.containsKey(pi.getId())) {
				pInvariants.put(pi.getId(), pi);
				leafToParent.put(pi, invariant);
			}
		List<ExchangeInvariant> exchangeInvariants = findExchangeInvariant(invariant);
		for (ExchangeInvariant ei : exchangeInvariants)
			if (!eInvariants.containsKey(ei.getId())) {
				eInvariants.put(ei.getId(), ei);
				leafToParent.put(ei, invariant);
			}
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
				newPartialRoleAssignment = new HashMap<>(partialRoleAssignment);
				newPartialRoleAssignment.put(key, (String) id);
				findAssignments(currentIds, filter, newPartialRoleAssignment,
						result);
			}
		}
	}

	private String findKeyFroEmpty(Map<String, String> map) {
		for (String key : map.keySet())
			if (map.get(key).equals(""))
				return key;
		return null;
	}

	private boolean holds(Map<String, String> roleAssignment, Invariant top,
			Invariant leaf) {
		SATSolver solver = new SAT4JSolver(roleAssignment);
		solver.addTopInvariant(top);
		return isPathSelected(leaf, top, solver.solve());
	}

	// TODO
	private boolean isPathSelected(IRMPrimitive fromNode, Invariant top, List<IRMPrimitive> solution) {
		boolean result = solution.contains(fromNode);
		if (!result)
			return false;
		else if (fromNode.isRoot()) {
			return fromNode.equals(top);
		} else {
			for (IRMPrimitive p : fromNode.getParents()) {
				if (isPathSelected(p, top, solution))
					return true;
			}
			return false;
		}
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

	// private List<Assumption> findLeafAssumptions(IRMPrimitive primitive) {
	// List<Assumption> result = new LinkedList<>();
	// if (primitive instanceof Invariant) {
	// Invariant iPrimitive = (Invariant) primitive;
	// if (iPrimitive.isLeaf())
	// if (iPrimitive instanceof Assumption)
	// result.add((Assumption) iPrimitive);
	// else
	// result.addAll(findLeafAssumptions(iPrimitive.getOperation()));
	// else
	// result.addAll(findLeafAssumptions(iPrimitive.getOperation()));
	// } else if (primitive instanceof Operation) {
	// for (IRMPrimitive child : ((Operation) primitive).getChildren())
	// result.addAll(findLeafAssumptions(child));
	// }
	// return result;
	// }

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
