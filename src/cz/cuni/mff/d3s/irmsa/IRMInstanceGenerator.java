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
package cz.cuni.mff.d3s.irmsa;

import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.getAbsoluteStrippedPath;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.KnowledgePathAndRoot;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;
import cz.cuni.mff.d3s.irm.model.design.AndNode;
import cz.cuni.mff.d3s.irm.model.design.Assumption;
import cz.cuni.mff.d3s.irm.model.design.BranchingNode;
import cz.cuni.mff.d3s.irm.model.design.Component;
import cz.cuni.mff.d3s.irm.model.design.Contributes;
import cz.cuni.mff.d3s.irm.model.design.Coordinator;
import cz.cuni.mff.d3s.irm.model.design.Decomposition;
import cz.cuni.mff.d3s.irm.model.design.ExchangeInvariant;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.Invariant;
import cz.cuni.mff.d3s.irm.model.design.Member;
import cz.cuni.mff.d3s.irm.model.design.OrNode;
import cz.cuni.mff.d3s.irm.model.design.ProcessInvariant;
import cz.cuni.mff.d3s.irm.model.design.Refinement;
import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ShadowInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimeFactory;
import cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor;
import cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;

/**
 * Generates the IRM runtime model instances out of the three models: IRM
 * design, jDEECo architecture and trace model.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 *         TODO(IG) method-level comments
 */
public class IRMInstanceGenerator {

	Architecture architecture;
	IRM design; 
	TraceModel trace;
	
	List<IRMInstance> IRMinstances;
	Map<Invariant, PresentInvariantInstance> invariantsToInstances = new HashMap<>();
	
	public IRMInstanceGenerator(Architecture architecture, IRM design, TraceModel trace) {
		this.architecture = architecture;
		this.design = design;
		this.trace = trace;
	}
	
	public List<IRMInstance> generateIRMInstances() {
		IRMinstances = new ArrayList<>();
		constructInstances(new HashMap<Component,ComponentInstance>(), design.getComponents().listIterator(0));
		return IRMinstances;
	}
	
	void constructInstances(Map<Component,ComponentInstance> selectedComponentInstances, ListIterator<Component> it) {
		if (it.hasNext()) {
			Component currentType = it.next(); 
			List<ComponentInstance> componentInstancesInArchitecture = getAllArchitectureInstancesOfDesignComponent(currentType);
			
			for (ComponentInstance c: componentInstancesInArchitecture) {
				HashMap<Component, ComponentInstance> newTuple = new HashMap<>(selectedComponentInstances);
				newTuple.put(currentType,c);
				constructInstances(newTuple, it);
			}
		} else {
			// end of recursion
			// check if the final list contains at least one local component. If not, do not create instance
			if (containsLocalComponent(selectedComponentInstances)) {
				try {
					IRMinstances.add(createIRMInstance(selectedComponentInstances));
				} catch (KnowledgeNotFoundException e) {
					Log.e("Error when creating IRMinstance.", e);
				}
			}
			// backtrack one position
			it.previous();
		}
	}
	
	List<ComponentInstance> getAllArchitectureInstancesOfDesignComponent(Component irmC) {
		List<ComponentInstance> ret = new ArrayList<>();
		for (ComponentInstance c: architecture.getComponentInstances()) {
			if (trace.getIRMComponentForArchitectureComponentInstance(c, design).equals(irmC)) {
				ret.add(c);
			}
		}
		return ret;
	}
	
	boolean containsLocalComponent(Map<Component,ComponentInstance> selectedComponentInstances) {
		for (ComponentInstance ci : selectedComponentInstances.values()) {
			if (ci instanceof LocalComponentInstance) {
				return true;
			}
		}
		return false;
	}
	
	IRMInstance createIRMInstance(Map<Component,ComponentInstance> selectedComponentInstances) throws KnowledgeNotFoundException {
		IRMRuntimeFactory factory = IRMRuntimeFactory.eINSTANCE;
		IRMInstance irmInstance = factory.createIRMInstance();
		irmInstance.setDesignModel(design);
		irmInstance.setTraceModel(trace);
		
		List<IRMComponentInstance> newInstances = new ArrayList<>(); 
		for (Component c: selectedComponentInstances.keySet()) {
			IRMComponentInstance irmComponentInstance = factory.createIRMComponentInstance();
			ComponentInstance ci =  selectedComponentInstances.get(c);
			irmComponentInstance.setArchitectureInstance(selectedComponentInstances.get(c));
			irmComponentInstance.setIRMcomponent(c);
			
			// FIXME(IG) Obtain only knowledge corresponding to the knowledge paths from all the monitors associated with this IRMComponent.
			// The next line is a quick 'n' dirty solution (gets all knowledge fields under the root)
			KnowledgePath emptyPath = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
			ValueSet valueSet = ci.getKnowledgeManager().get(Arrays.asList(emptyPath));
			irmComponentInstance.setKnowledgeSnapshot(valueSet);
			
			irmInstance.getIRMcomponentInstances().add(irmComponentInstance);
			newInstances.add(irmComponentInstance);
		}
		
		for (Invariant i : design.getInvariants()) {
			
			PresentInvariantInstance invariantInstance;	
			if (i instanceof ProcessInvariant) {
				invariantInstance = factory.createProcessInvariantInstance();
				((ProcessInvariantInstance) invariantInstance).setComponentProcess(getComponentProcess(selectedComponentInstances, (ProcessInvariant) i));
				
			} else if (i instanceof ExchangeInvariant) {
				invariantInstance = factory.createExchangeInvariantInstance();
				((ExchangeInvariantInstance) invariantInstance).setEnsembleDefinition(trace.getEnsembleDefinitionFromInvariant(i));
			} else {
				invariantInstance = factory.createPresentInvariantInstance();
			}
			
			invariantInstance.setDiagramInstance(irmInstance);
			invariantInstance.setInvariant(i);
			invariantInstance.setSatisfied(getMonitorValue(i, newInstances));
			
			invariantsToInstances.put(i, invariantInstance);
			irmInstance.getInvariantInstances().add(invariantInstance);
		}
		
		// flush all ensemble instances from architecture model (they are anyway used as volatile data) 
		Log.i("Removing all ensemble instances from architectural model.");
		architecture.getEnsembleInstances().retainAll(Collections.EMPTY_SET);
		
		// FIXME(IG) Make the design model easier to work with, i.e. make it into a tree structure computed on the fly 
		// e.g., by implementing a DecomposableElement.getParents(), DecomposableElement.getChildren()
		// and IRM.getRoots() : Invariants - basically this would result into reusing the current processAlternatives()
		// method on the design model implementation.
		for (PresentInvariantInstance invariantInstance : invariantsToInstances.values()) {
			Invariant invariant = invariantInstance.getInvariant();
			if (!((invariant instanceof Assumption) || 
					(invariant instanceof ProcessInvariant) || 
					(invariant instanceof ExchangeInvariant))) {
				BranchingNode branchingNode = getBranchingNodeBelow(invariant);
				if (branchingNode == null) {
					try {
						throw new IRMDesignTraversalException();
					} catch (IRMDesignTraversalException e) {
						Log.e("Invariant " + invariant + "that is not " +
								"Assumption/ProcessInvariant/ExchangeInvariant was found not decomposed.", e);
					}
				}
				processAlternatives(irmInstance, invariantInstance, branchingNode);	
			}
		}
		
		return irmInstance;
	}

	/**
	 * @param i
	 * @param newInstances
	 * @return
	 * @throws TaskInvocationException 
	 */
	boolean getMonitorValue(Invariant i, List<IRMComponentInstance> IRMComponentInstances) {
		
		if (i instanceof ProcessInvariant) {
			
			IRMComponentInstance contributingComponent = findContributingComponent(i, IRMComponentInstances);
			if (contributingComponent == null) {
				Log.w("No component contributing to process invariant " + i + ", so invariant evaluation trivially returned true.");
				return true;
			} 
			
			InvariantMonitor monitor = getInvariantMonitor(i);
			if (monitor == null) {
				Log.w("No invariant monitor found for process invariant " + i + ", so invariant evaluation trivially returned true.");
				return true;
			} 
			
			Method method = monitor.getMethod();
			ReadOnlyKnowledgeManager knowledgeManager = contributingComponent.getArchitectureInstance().getKnowledgeManager();
			Collection<MonitorParameter> formalParams = monitor.getMonitorParameters();
			
			Collection<KnowledgePath> paths = new ArrayList<KnowledgePath>();
			
			for (MonitorParameter formalParam : formalParams) {
				KnowledgePath absoluteKnowledgePath;
				// FIXME: The call to getAbsolutePath is in theory wrong, because this way we are not obtaining the
				// knowledge within one transaction. But fortunately this is not a problem with the single 
				// threaded scheduler we have at the moment, because once the invoke method starts there is no other
				// activity whatsoever in the system. See also cz.cuni.mff.d3s.deeco.task.ProcessTask.invoke() method.
				try {  
					absoluteKnowledgePath = KnowledgePathHelper.getAbsolutePath(formalParam.getKnowledgePath(), knowledgeManager);
				} catch (KnowledgeNotFoundException e) {
					Log.w("Knowledge path " + e.getNotFoundPath() + " could not be resolved, so invariant evaluation trivially returned true.");
					return true;
				}	
				paths.add(absoluteKnowledgePath);
			}
			
			// Construct the parameters for the process method invocation
			Object[] actualParams = new Object[formalParams.size()];
			int paramIdx = 0;
			for (KnowledgePath absoluteKnowledgePath : paths) {
				actualParams[paramIdx] = contributingComponent.getKnowledgeSnapshot().getValue(absoluteKnowledgePath);				
				paramIdx++;
			}
			
			Boolean ret;
			try {
				ret = (Boolean) method.invoke(null, actualParams);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				Log.e("Error when invoking a monitor method, so invariant evaluation returned false.", e);
				return false;
			}
			System.out.println("++++++++ Monitor " + method + " returned " + ret);
			return ret;
			
		} else if (i instanceof ExchangeInvariant){
			
			EnsembleDefinition ed = trace.getEnsembleDefinitionFromInvariant(i); 
			if (ed == null) {
				Log.i("No trace found for exchange invariant " + i + ", so invariant evaluation trivially returned true.");
				return true;
			}
			if (!isEnsembleInstantiated(IRMComponentInstances, ed)) {
				Log.i("EnsembleDefinition " + ed + " is not instantiated within this runtime, so invariant evaluation trivially returned true.");
				return true;
			}
			
			IRMComponentInstance coord = findCoordinator(i, IRMComponentInstances);
			IRMComponentInstance member = findMember(i, IRMComponentInstances);
			if ((coord == null) || (member == null)) {
				Log.w("No coordinator/member found for exchange invariant " + i + ", so invariant evaluation trivially returned true.");
				return true;
			}
			
			InvariantMonitor monitor = getInvariantMonitor(i);
			if (monitor == null) {
				Log.w("No invariant monitor found for exchange invariant " + i + ", so invariant evaluation trivially returned true.");
				return true;
			}
			
			Method method = monitor.getMethod();
			ReadOnlyKnowledgeManager coordKnowledgeManager = coord.getArchitectureInstance().getKnowledgeManager();
			ReadOnlyKnowledgeManager memberKnowledgeManager = member.getArchitectureInstance().getKnowledgeManager();
			Collection<MonitorParameter> formalParams = monitor.getMonitorParameters();
			
			Collection<KnowledgePathAndRoot> allPathsWithRoots = new LinkedList<KnowledgePathAndRoot>();
			
			for (MonitorParameter formalParam : formalParams) {

				KnowledgePathAndRoot absoluteKnowledgePathAndRoot;
				// FIXME: The call to getAbsoluteStrippedPath is in theory wrong, because this way we are not obtaining the
				// knowledge within one transaction. But fortunately this is not a problem with the single 
				// threaded scheduler we have at the moment, because once the invoke method starts there is no other
				// activity whatsoever in the system. See also cz.cuni.mff.d3s.deeco.task.EnsembleTask.checkMembership() method.
				try {
					absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), coordKnowledgeManager, memberKnowledgeManager);
				} catch (KnowledgeNotFoundException e) {
					Log.w("Not able to resolve the path for monitor " + method + " of invariant " + i 
							+ ", so invariant evaluation trivially returned true.");
					return true;
				}
				allPathsWithRoots.add(absoluteKnowledgePathAndRoot);
			}

			// Construct the parameters for the process method invocation
			Object[] actualParams = new Object[formalParams.size()];
			int paramIdx = 0;
			for (KnowledgePathAndRoot absoluteKnowledgePathAndRoot : allPathsWithRoots) {
				
				if (absoluteKnowledgePathAndRoot.root == PathRoot.COORDINATOR) {
					actualParams[paramIdx] = coord.getKnowledgeSnapshot().getValue(absoluteKnowledgePathAndRoot.knowledgePath);	
				} else {
					actualParams[paramIdx] = member.getKnowledgeSnapshot().getValue(absoluteKnowledgePathAndRoot.knowledgePath);	
				}
				paramIdx++;
			}

			Boolean ret;
			try {
				ret = (Boolean) method.invoke(null, actualParams);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				Log.w("Error when invoking a monitor method, so invariant evaluation returned false.", e);
				return false;
			}
			System.out.println("-------- Monitor " + method + " returned " + ret);
			return ret;
			
		} else { // if i is neither Process nor Exchange invariant
			// TODO(IG) implement monitoring also for Assumption and non-leaf invariants
			return true;
		}
	}

	/**
	 * @param i
	 * @param IRMComponentInstances
	 * @return
	 */
	IRMComponentInstance findMember(Invariant i, List<IRMComponentInstance> IRMComponentInstances) {
		for (IRMComponentInstance ci : IRMComponentInstances) {
			if (isMemberToEnsemble(ci.getIRMcomponent(), i)) {
				return ci;
			}
		}
		return null;
	}

	/**
	 * @param i
	 * @param IRMComponentInstances
	 * @return
	 */
	IRMComponentInstance findCoordinator(Invariant i, List<IRMComponentInstance> IRMComponentInstances) {
		for (IRMComponentInstance ci : IRMComponentInstances) {
			if (isCoordinatorToEnsemble(ci.getIRMcomponent(), i)) {
				return ci;
			}	
		}
		return null;
	}

	/**
	 * @param c
	 * @param i
	 * @return
	 */
	boolean isMemberToEnsemble(Component c, Invariant i) {
		for (Member member : design.getMembers()) {
			if ((member.getStart().equals(c)) && (member.getEnd().equals(i))){
				return true;
			}
		}
		return false;
	}

	/**
	 * @param c
	 * @param i
	 * @return
	 */
	boolean isCoordinatorToEnsemble(Component c, Invariant i) {
		for (Coordinator coord : design.getCoordinators()) {
			if ((coord.getStart().equals(c)) && (coord.getEnd().equals(i))){
				return true;
			}
		}
		return false;
	}

	/**
	 * @param i
	 * @return
	 */
	InvariantMonitor getInvariantMonitor(Invariant i) {
		for (InvariantMonitor m : trace.getInvariantMonitors()) {
			if (m.getInvariant().equals(i)) {
				return m;
			}
		}
		return null;
	}

	/**
	 * @param i
	 * @param IRMComponentInstances
	 * @return
	 */
	IRMComponentInstance findContributingComponent(Invariant i, List<IRMComponentInstance> IRMComponentInstances) {
		for (IRMComponentInstance ci : IRMComponentInstances) {
			if (contributesToInvariant(ci.getIRMcomponent(), i)) {
				return ci;
			}
		}
		return null;
	}
	
	boolean isEnsembleInstantiated(Collection<IRMComponentInstance> IRMComponentInstances, EnsembleDefinition ed) {
		Collection<ComponentInstance> architectureComponentInstances = new ArrayList<>();
		
		for (IRMComponentInstance instance : IRMComponentInstances) {
			architectureComponentInstances.add(instance.getArchitectureInstance());
		}
		
		for (EnsembleInstance ei : architecture.getEnsembleInstances()) {
			if ((ei.getEnsembleDefinition().equals(ed)) 
					&& (architectureComponentInstances.contains(ei.getCoordinator())) 
					&& (architectureComponentInstances.containsAll(ei.getMembers()))){
				return true;	
			}
		}
		
		return false;	
	}

	ComponentProcess getComponentProcess(Map<Component, ComponentInstance> componentsToComponentInstances, ProcessInvariant invariant) {
		for (Component c : componentsToComponentInstances.keySet()) {
			if ((componentsToComponentInstances.get(c) instanceof LocalComponentInstance) && (contributesToInvariant(c, invariant))) {
				cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance runtimeComponentInstance = 
						((LocalComponentInstance) componentsToComponentInstances.get(c)).getRuntimeInstance();
				ComponentProcess ret = trace.getComponentProcessFromComponentInstanceAndInvariant(
						runtimeComponentInstance,invariant);
				if (ret == null) {
					Log.w("No process present for ProcessInvariant " + invariant + " and *local* componentInstance " 
							+ runtimeComponentInstance + " in the trace model.");		
				}
				return ret;
			} 
		}
		Log.w("No process present for ProcessInvariant " + invariant + " among componentInstances " 
				+ componentsToComponentInstances.values() + " in the trace model.");		
		return null;
	}
	
	boolean contributesToInvariant(Component c, Invariant i) {
		for (Contributes contr : design.getContributes()) {
			if ((contr.getStart().equals(c)) && (contr.getEnd().equals(i))){
				return true;
			}
		}
		return false;
	}

	BranchingNode getBranchingNodeBelow(Invariant i) {
		for (Refinement r : design.getRefinements()) {
			if (r.getEnd().equals(i)) {
				return r.getStart();
			}
		}
		// i is leaf
		return null;
	}
	
	void processAlternatives(IRMInstance irmInstance, 
			InvariantInstance invariantInstance, BranchingNode branchingNode) {
		// create an Alternative object with potentially many children
		if (branchingNode instanceof AndNode) {
			
			// Instead of pre-computing the Alternatives here, we could compute the alternatives on the fly, 
			// e.g., by implementing IRMInstance.getAlternatives(). Right now we duplicate the design model information, 
			// so it's harder to change the IRM design model on the fly and keep the IRM runtime model up-to-date. 
			// On the other side, when computing the alternatives on the fly it would be complicated to manipulate shadow invariants. 
			Alternative a = IRMRuntimeFactory.eINSTANCE.createAlternative();
			for (Decomposition decomposition : design.getDecompositions()) {
				if (decomposition.getEnd().equals(branchingNode)) {
					a.getChildren().add(invariantsToInstances.get(decomposition.getStart()));
				}
			}
			invariantInstance.getAlternatives().add(a);
		} 
		// create potentially many Alternative objects with one child each
		if (branchingNode instanceof OrNode) {
			for (Decomposition decomposition : design.getDecompositions()) {
				if (decomposition.getEnd().equals(branchingNode)) {
					Alternative a = IRMRuntimeFactory.eINSTANCE.createAlternative();
					a.getChildren().add(invariantsToInstances.get(decomposition.getStart()));
					invariantInstance.getAlternatives().add(a);
				}
			}
		}
		// in case of consecutive OrNodes and AndNodes, create shadow invariant instances
		for (Refinement r : design.getRefinements()) {
			if (r.getEnd().equals(branchingNode)) {
				Alternative a = IRMRuntimeFactory.eINSTANCE.createAlternative();
				ShadowInvariantInstance shadowInstance = IRMRuntimeFactory.eINSTANCE.createShadowInvariantInstance();
				a.getChildren().add(shadowInstance);
				invariantInstance.getAlternatives().add(a);
				irmInstance.getInvariantInstances().add(shadowInstance);
				processAlternatives(irmInstance, shadowInstance, r.getStart());
			}
		}
	}
	
}
