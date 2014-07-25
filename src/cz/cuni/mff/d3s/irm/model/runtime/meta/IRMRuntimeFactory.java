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
package cz.cuni.mff.d3s.irm.model.runtime.meta;

import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ShadowInvariantInstance;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage
 * @generated
 */
public interface IRMRuntimeFactory extends EFactory {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	IRMRuntimeFactory eINSTANCE = cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimeFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>IRM Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IRM Instance</em>'.
	 * @generated
	 */
	IRMInstance createIRMInstance();

	/**
	 * Returns a new object of class '<em>IRM Component Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IRM Component Instance</em>'.
	 * @generated
	 */
	IRMComponentInstance createIRMComponentInstance();

	/**
	 * Returns a new object of class '<em>Alternative</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Alternative</em>'.
	 * @generated
	 */
	Alternative createAlternative();

	/**
	 * Returns a new object of class '<em>Present Invariant Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Present Invariant Instance</em>'.
	 * @generated
	 */
	PresentInvariantInstance createPresentInvariantInstance();

	/**
	 * Returns a new object of class '<em>Shadow Invariant Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Shadow Invariant Instance</em>'.
	 * @generated
	 */
	ShadowInvariantInstance createShadowInvariantInstance();

	/**
	 * Returns a new object of class '<em>Process Invariant Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Process Invariant Instance</em>'.
	 * @generated
	 */
	ProcessInvariantInstance createProcessInvariantInstance();

	/**
	 * Returns a new object of class '<em>Exchange Invariant Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Exchange Invariant Instance</em>'.
	 * @generated
	 */
	ExchangeInvariantInstance createExchangeInvariantInstance();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	IRMRuntimePackage getIRMRuntimePackage();

} //IRMRuntimeFactory
