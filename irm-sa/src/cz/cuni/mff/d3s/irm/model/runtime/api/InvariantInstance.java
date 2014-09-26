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

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Invariant Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getDiagramInstance <em>Diagram Instance</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#isSatisfied <em>Satisfied</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#isSelected <em>Selected</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getAlternatives <em>Alternatives</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getInvariantInstance()
 * @model abstract="true"
 * @generated
 */
public interface InvariantInstance extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * Returns the value of the '<em><b>Diagram Instance</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getInvariantInstances <em>Invariant Instances</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram Instance</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagram Instance</em>' container reference.
	 * @see #setDiagramInstance(IRMInstance)
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getInvariantInstance_DiagramInstance()
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getInvariantInstances
	 * @model opposite="invariantInstances" required="true" transient="false"
	 * @generated
	 */
	IRMInstance getDiagramInstance();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getDiagramInstance <em>Diagram Instance</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagram Instance</em>' container reference.
	 * @see #getDiagramInstance()
	 * @generated
	 */
	void setDiagramInstance(IRMInstance value);

	/**
	 * Returns the value of the '<em><b>Satisfied</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Satisfied</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Satisfied</em>' attribute.
	 * @see #setSatisfied(boolean)
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getInvariantInstance_Satisfied()
	 * @model required="true"
	 * @generated
	 */
	boolean isSatisfied();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#isSatisfied <em>Satisfied</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Satisfied</em>' attribute.
	 * @see #isSatisfied()
	 * @generated
	 */
	void setSatisfied(boolean value);

	/**
	 * Returns the value of the '<em><b>Selected</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selected</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Selected</em>' attribute.
	 * @see #setSelected(boolean)
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getInvariantInstance_Selected()
	 * @model required="true"
	 * @generated
	 */
	boolean isSelected();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#isSelected <em>Selected</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Selected</em>' attribute.
	 * @see #isSelected()
	 * @generated
	 */
	void setSelected(boolean value);

	/**
	 * Returns the value of the '<em><b>Alternatives</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.irm.model.runtime.api.Alternative}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Alternatives</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alternatives</em>' containment reference list.
	 * @see #isSetAlternatives()
	 * @see #unsetAlternatives()
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getInvariantInstance_Alternatives()
	 * @model containment="true" unsettable="true" derived="true"
	 * @generated
	 */
	List<Alternative> getAlternatives();

	/**
	 * Unsets the value of the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getAlternatives <em>Alternatives</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAlternatives()
	 * @see #getAlternatives()
	 * @generated
	 */
	void unsetAlternatives();

	/**
	 * Returns whether the value of the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getAlternatives <em>Alternatives</em>}' containment reference list is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Alternatives</em>' containment reference list is set.
	 * @see #unsetAlternatives()
	 * @see #getAlternatives()
	 * @generated
	 */
	boolean isSetAlternatives();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	InvariantInstance getParent();

} // InvariantInstance
