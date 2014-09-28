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

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance;
import cz.cuni.mff.d3s.irm.model.design.Component;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IRM Component Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getIRMcomponent <em>IR Mcomponent</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getArchitectureInstance <em>Architecture Instance</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getKnowledgeSnapshot <em>Knowledge Snapshot</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getIRMComponentInstance()
 * @model
 * @generated
 */
public interface IRMComponentInstance extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * Returns the value of the '<em><b>IR Mcomponent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>IR Mcomponent</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>IR Mcomponent</em>' attribute.
	 * @see #setIRMcomponent(Component)
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getIRMComponentInstance_IRMcomponent()
	 * @model dataType="cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentType" required="true"
	 * @generated
	 */
	Component getIRMcomponent();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getIRMcomponent <em>IR Mcomponent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>IR Mcomponent</em>' attribute.
	 * @see #getIRMcomponent()
	 * @generated
	 */
	void setIRMcomponent(Component value);

	/**
	 * Returns the value of the '<em><b>Architecture Instance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Architecture Instance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Architecture Instance</em>' attribute.
	 * @see #setArchitectureInstance(ComponentInstance)
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getIRMComponentInstance_ArchitectureInstance()
	 * @model dataType="cz.cuni.mff.d3s.irm.model.runtime.api.ComponentInstanceType" required="true"
	 * @generated
	 */
	ComponentInstance getArchitectureInstance();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getArchitectureInstance <em>Architecture Instance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Architecture Instance</em>' attribute.
	 * @see #getArchitectureInstance()
	 * @generated
	 */
	void setArchitectureInstance(ComponentInstance value);

	/**
	 * Returns the value of the '<em><b>Knowledge Snapshot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Knowledge Snapshot</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Knowledge Snapshot</em>' attribute.
	 * @see #setKnowledgeSnapshot(ValueSet)
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getIRMComponentInstance_KnowledgeSnapshot()
	 * @model dataType="cz.cuni.mff.d3s.irm.model.runtime.api.ValueSetType" required="true"
	 * @generated
	 */
	ValueSet getKnowledgeSnapshot();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getKnowledgeSnapshot <em>Knowledge Snapshot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Knowledge Snapshot</em>' attribute.
	 * @see #getKnowledgeSnapshot()
	 * @generated
	 */
	void setKnowledgeSnapshot(ValueSet value);

} // IRMComponentInstance
