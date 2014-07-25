/**
 * Copyright 2014 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.cuni.mff.d3s.irm.model.trace.impl;

import cz.cuni.mff.d3s.deeco.annotations.processor.IrmAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.RemoteComponentInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.irm.model.design.Component;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignFactory;
import cz.cuni.mff.d3s.irm.model.design.Invariant;
import cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor;
import cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage;
import cz.cuni.mff.d3s.irmsa.IRMComponentNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.impl.TraceModelImpl#getComponentTraces <em>Component Traces</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.impl.TraceModelImpl#getProcessTraces <em>Process Traces</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.impl.TraceModelImpl#getEnsembleDefinitionTraces <em>Ensemble Definition Traces</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.impl.TraceModelImpl#getInvariantMonitors <em>Invariant Monitors</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TraceModelImpl extends MinimalEObjectImpl.Container implements TraceModel {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * The cached value of the '{@link #getComponentTraces() <em>Component Traces</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponentTraces()
	 * @generated
	 * @ordered
	 */
	protected EList<ComponentTrace> componentTraces;

	/**
	 * The cached value of the '{@link #getProcessTraces() <em>Process Traces</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcessTraces()
	 * @generated
	 * @ordered
	 */
	protected EList<ProcessTrace> processTraces;

	/**
	 * The cached value of the '{@link #getEnsembleDefinitionTraces() <em>Ensemble Definition Traces</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnsembleDefinitionTraces()
	 * @generated
	 * @ordered
	 */
	protected EList<EnsembleDefinitionTrace> ensembleDefinitionTraces;

	/**
	 * The cached value of the '{@link #getInvariantMonitors() <em>Invariant Monitors</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInvariantMonitors()
	 * @generated
	 * @ordered
	 */
	protected EList<InvariantMonitor> invariantMonitors;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TraceModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TracePackage.Literals.TRACE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ComponentTrace> getComponentTraces() {
		if (componentTraces == null) {
			componentTraces = new EObjectContainmentEList<ComponentTrace>(ComponentTrace.class, this, TracePackage.TRACE_MODEL__COMPONENT_TRACES);
		}
		return componentTraces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ProcessTrace> getProcessTraces() {
		if (processTraces == null) {
			processTraces = new EObjectContainmentEList<ProcessTrace>(ProcessTrace.class, this, TracePackage.TRACE_MODEL__PROCESS_TRACES);
		}
		return processTraces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EnsembleDefinitionTrace> getEnsembleDefinitionTraces() {
		if (ensembleDefinitionTraces == null) {
			ensembleDefinitionTraces = new EObjectContainmentEList<EnsembleDefinitionTrace>(EnsembleDefinitionTrace.class, this, TracePackage.TRACE_MODEL__ENSEMBLE_DEFINITION_TRACES);
		}
		return ensembleDefinitionTraces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InvariantMonitor> getInvariantMonitors() {
		if (invariantMonitors == null) {
			invariantMonitors = new EObjectContainmentEList<InvariantMonitor>(InvariantMonitor.class, this, TracePackage.TRACE_MODEL__INVARIANT_MONITORS);
		}
		return invariantMonitors;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Component getIRMComponentForArchitectureComponentInstance(ComponentInstance componentInstance, IRM design) {
		if (componentInstance instanceof RemoteComponentInstance) {
			// this runtime instance is not present in the trace model, so we have to look it up by name in the design model 
			String IRMComponentRole = getComponentRole(componentInstance);
			Component c = getComponentFromDesignModelByNameMatching(design, IRMComponentRole);
			
			if (c == null) {
				try {
					throw new IRMComponentNotFoundException(IRMComponentRole);
				} catch (IRMComponentNotFoundException e) {
					Log.e("Error while trying to find role of remote component with id " + componentInstance.getId() + "in the design model.", e);
				}
			} 
			return c;
		} else {
			// componentInstance is an instance of LocalComponentInstance, so we can use the trace model
			Component ret = null;
			cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance componentRuntimeInstance = 
					((LocalComponentInstance) componentInstance).getRuntimeInstance();
			
			if (componentRuntimeInstance.isSystemComponent()){
				return IRMDesignFactory.eINSTANCE.createComponent();
			}
			
			for (ComponentTrace cTrace : getComponentTraces()) {
				if (cTrace.getFrom().equals(componentRuntimeInstance)) {
					ret =  cTrace.getTo();
				}
			}
			
			if (ret == null) {
				try {
					throw new IRMComponentNotFoundException(componentRuntimeInstance.getName());
				} catch (IRMComponentNotFoundException e) {
					Log.e("Error while trying to get trace of local component instance " + componentRuntimeInstance.getName(), e);
				}
			}
			return ret;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ComponentProcess getComponentProcessFromComponentInstanceAndInvariant(
			cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance componentInstance, Invariant invariant) {
		for (ComponentProcess p : componentInstance.getComponentProcesses()) {
			Invariant i = getInvariantOfProcess(p); 
			if ((i != null) && (i.equals(invariant))) {
				return p;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EnsembleDefinition getEnsembleDefinitionFromInvariant(Invariant invariant) {
		for (EnsembleDefinitionTrace edt : getEnsembleDefinitionTraces()) {
			if (edt.getTo().equals(invariant)) {
				return edt.getFrom();
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	private Invariant getInvariantOfProcess(ComponentProcess p) {
		for (ProcessTrace t : getProcessTraces()) {
			if (t.getFrom().equals(p)) {
				return t.getTo();
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	private String getComponentRole(ComponentInstance componentInstance) {
		KnowledgePath path = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
		PathNodeField pNode = RuntimeMetadataFactoryExt.eINSTANCE.createPathNodeField();
		pNode.setName(IrmAwareAnnotationProcessorExtension.COMPONENT_ROLE);
		path.getNodes().add(pNode);
		ArrayList<KnowledgePath> paths = new ArrayList<>();
		paths.add(path);
		try {
			return (String) componentInstance.getKnowledgeManager().get(paths).getValue(path);
		} catch (KnowledgeNotFoundException e) {
			Log.e("Error while getting the role of the remote component with id "
					+ componentInstance.getId() + " from its KnowledgeManager", e);
			return null;
		}
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	private Component getComponentFromDesignModelByNameMatching(IRM design, String name) {
		for (Component c : design.getComponents()) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		Log.w("Component role " + name + " was not found in the design model.");
		return null;
	}


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TracePackage.TRACE_MODEL__COMPONENT_TRACES:
				return ((InternalEList<?>)getComponentTraces()).basicRemove(otherEnd, msgs);
			case TracePackage.TRACE_MODEL__PROCESS_TRACES:
				return ((InternalEList<?>)getProcessTraces()).basicRemove(otherEnd, msgs);
			case TracePackage.TRACE_MODEL__ENSEMBLE_DEFINITION_TRACES:
				return ((InternalEList<?>)getEnsembleDefinitionTraces()).basicRemove(otherEnd, msgs);
			case TracePackage.TRACE_MODEL__INVARIANT_MONITORS:
				return ((InternalEList<?>)getInvariantMonitors()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TracePackage.TRACE_MODEL__COMPONENT_TRACES:
				return getComponentTraces();
			case TracePackage.TRACE_MODEL__PROCESS_TRACES:
				return getProcessTraces();
			case TracePackage.TRACE_MODEL__ENSEMBLE_DEFINITION_TRACES:
				return getEnsembleDefinitionTraces();
			case TracePackage.TRACE_MODEL__INVARIANT_MONITORS:
				return getInvariantMonitors();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TracePackage.TRACE_MODEL__COMPONENT_TRACES:
				getComponentTraces().clear();
				getComponentTraces().addAll((Collection<? extends ComponentTrace>)newValue);
				return;
			case TracePackage.TRACE_MODEL__PROCESS_TRACES:
				getProcessTraces().clear();
				getProcessTraces().addAll((Collection<? extends ProcessTrace>)newValue);
				return;
			case TracePackage.TRACE_MODEL__ENSEMBLE_DEFINITION_TRACES:
				getEnsembleDefinitionTraces().clear();
				getEnsembleDefinitionTraces().addAll((Collection<? extends EnsembleDefinitionTrace>)newValue);
				return;
			case TracePackage.TRACE_MODEL__INVARIANT_MONITORS:
				getInvariantMonitors().clear();
				getInvariantMonitors().addAll((Collection<? extends InvariantMonitor>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case TracePackage.TRACE_MODEL__COMPONENT_TRACES:
				getComponentTraces().clear();
				return;
			case TracePackage.TRACE_MODEL__PROCESS_TRACES:
				getProcessTraces().clear();
				return;
			case TracePackage.TRACE_MODEL__ENSEMBLE_DEFINITION_TRACES:
				getEnsembleDefinitionTraces().clear();
				return;
			case TracePackage.TRACE_MODEL__INVARIANT_MONITORS:
				getInvariantMonitors().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case TracePackage.TRACE_MODEL__COMPONENT_TRACES:
				return componentTraces != null && !componentTraces.isEmpty();
			case TracePackage.TRACE_MODEL__PROCESS_TRACES:
				return processTraces != null && !processTraces.isEmpty();
			case TracePackage.TRACE_MODEL__ENSEMBLE_DEFINITION_TRACES:
				return ensembleDefinitionTraces != null && !ensembleDefinitionTraces.isEmpty();
			case TracePackage.TRACE_MODEL__INVARIANT_MONITORS:
				return invariantMonitors != null && !invariantMonitors.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case TracePackage.TRACE_MODEL___GET_IRM_COMPONENT_FOR_ARCHITECTURE_COMPONENT_INSTANCE__COMPONENTINSTANCE_IRM:
				return getIRMComponentForArchitectureComponentInstance((ComponentInstance)arguments.get(0), (IRM)arguments.get(1));
			case TracePackage.TRACE_MODEL___GET_COMPONENT_PROCESS_FROM_COMPONENT_INSTANCE_AND_INVARIANT__COMPONENTINSTANCE_INVARIANT:
				return getComponentProcessFromComponentInstanceAndInvariant((cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance)arguments.get(0), (Invariant)arguments.get(1));
			case TracePackage.TRACE_MODEL___GET_ENSEMBLE_DEFINITION_FROM_INVARIANT__INVARIANT:
				return getEnsembleDefinitionFromInvariant((Invariant)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} //TraceModelImpl
