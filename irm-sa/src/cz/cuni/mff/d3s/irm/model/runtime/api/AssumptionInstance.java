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

import cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assumption Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance#getComponentInstance <em>Component Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getAssumptionInstance()
 * @model
 * @generated
 */
public interface AssumptionInstance extends PresentInvariantInstance {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * Returns the value of the '<em><b>Component Instance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Instance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Instance</em>' attribute.
	 * @see #setComponentInstance(ComponentInstance)
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#getAssumptionInstance_ComponentInstance()
	 * @model dataType="cz.cuni.mff.d3s.irm.model.runtime.api.ComponentInstanceType"
	 * @generated
	 */
	ComponentInstance getComponentInstance();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance#getComponentInstance <em>Component Instance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Component Instance</em>' attribute.
	 * @see #getComponentInstance()
	 * @generated
	 */
	void setComponentInstance(ComponentInstance value);

} // AssumptionInstance
