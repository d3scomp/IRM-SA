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
package cz.cuni.mff.d3s.irm.model.runtime.api;

import cz.cuni.mff.d3s.irm.model.design.IRM;

import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IRM Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getInvariantInstances <em>Invariant Instances</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getIRMcomponentInstances <em>IR Mcomponent Instances</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getDesignModel <em>Design Model</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getTraceModel <em>Trace Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getIRMInstance()
 * @model
 * @generated
 */
public interface IRMInstance extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * Returns the value of the '<em><b>Invariant Instances</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance}.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getDiagramInstance <em>Diagram Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Invariant Instances</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Invariant Instances</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getIRMInstance_InvariantInstances()
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getDiagramInstance
	 * @model opposite="diagramInstance" containment="true"
	 * @generated
	 */
	List<InvariantInstance> getInvariantInstances();

	/**
	 * Returns the value of the '<em><b>IR Mcomponent Instances</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>IR Mcomponent Instances</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>IR Mcomponent Instances</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getIRMInstance_IRMcomponentInstances()
	 * @model containment="true"
	 * @generated
	 */
	List<IRMComponentInstance> getIRMcomponentInstances();

	/**
	 * Returns the value of the '<em><b>Design Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Design Model</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Design Model</em>' attribute.
	 * @see #setDesignModel(IRM)
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getIRMInstance_DesignModel()
	 * @model dataType="cz.cuni.mff.d3s.irm.model.runtime.api.IRMDesignModelType" required="true"
	 * @generated
	 */
	IRM getDesignModel();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getDesignModel <em>Design Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Design Model</em>' attribute.
	 * @see #getDesignModel()
	 * @generated
	 */
	void setDesignModel(IRM value);

	/**
	 * Returns the value of the '<em><b>Trace Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Trace Model</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Trace Model</em>' attribute.
	 * @see #setTraceModel(TraceModel)
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getIRMInstance_TraceModel()
	 * @model dataType="cz.cuni.mff.d3s.irm.model.runtime.api.TraceModelType" required="true"
	 * @generated
	 */
	TraceModel getTraceModel();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getTraceModel <em>Trace Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Trace Model</em>' attribute.
	 * @see #getTraceModel()
	 * @generated
	 */
	void setTraceModel(TraceModel value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	List<InvariantInstance> getRoots();

} // IRMInstance
