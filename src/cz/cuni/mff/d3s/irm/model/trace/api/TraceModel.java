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
package cz.cuni.mff.d3s.irm.model.trace.api;

import cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

import cz.cuni.mff.d3s.irm.model.design.Component;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.Invariant;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getComponentTraces <em>Component Traces</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getProcessTraces <em>Process Traces</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getEnsembleDefinitionTraces <em>Ensemble Definition Traces</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getInvariantMonitors <em>Invariant Monitors</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getTraceModel()
 * @model
 * @generated
 */
public interface TraceModel extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * Returns the value of the '<em><b>Component Traces</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Traces</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Traces</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getTraceModel_ComponentTraces()
	 * @model containment="true"
	 * @generated
	 */
	EList<ComponentTrace> getComponentTraces();

	/**
	 * Returns the value of the '<em><b>Process Traces</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Process Traces</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Process Traces</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getTraceModel_ProcessTraces()
	 * @model containment="true"
	 * @generated
	 */
	EList<ProcessTrace> getProcessTraces();

	/**
	 * Returns the value of the '<em><b>Ensemble Definition Traces</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ensemble Definition Traces</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ensemble Definition Traces</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getTraceModel_EnsembleDefinitionTraces()
	 * @model containment="true"
	 * @generated
	 */
	EList<EnsembleDefinitionTrace> getEnsembleDefinitionTraces();

	/**
	 * Returns the value of the '<em><b>Invariant Monitors</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Invariant Monitors</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Invariant Monitors</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getTraceModel_InvariantMonitors()
	 * @model containment="true"
	 * @generated
	 */
	EList<InvariantMonitor> getInvariantMonitors();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns the IRM design component that corresponds to the architecture component instance. 
	 * The design model is passed as a second parameter in order to be able to look up the component in the design model by string matching of its role, when the architecture component instance is a remote instance.
	 * <!-- end-model-doc -->
	 * @model dataType="cz.cuni.mff.d3s.irm.model.trace.api.ComponentType" required="true" componentInstanceDataType="cz.cuni.mff.d3s.irm.model.trace.api.ArchitectureComponentInstanceType" designDataType="cz.cuni.mff.d3s.irm.model.trace.api.DesignModelType"
	 * @generated
	 */
	Component getIRMComponentForArchitectureComponentInstance(ComponentInstance componentInstance, IRM design);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns a component process by identifying it in the process traces and in the processes of the runtime component instance.
	 * <!-- end-model-doc -->
	 * @model dataType="cz.cuni.mff.d3s.irm.model.trace.api.ProcessType" componentInstanceDataType="cz.cuni.mff.d3s.irm.model.trace.api.ComponentInstanceType" invariantDataType="cz.cuni.mff.d3s.irm.model.trace.api.InvariantType"
	 * @generated
	 */
	ComponentProcess getComponentProcessFromComponentInstanceAndInvariant(cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance componentInstance, Invariant invariant);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model dataType="cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionType" invariantDataType="cz.cuni.mff.d3s.irm.model.trace.api.InvariantType"
	 * @generated
	 */
	EnsembleDefinition getEnsembleDefinitionFromInvariant(Invariant invariant);

} // TraceModel
