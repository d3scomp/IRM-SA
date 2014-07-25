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
package cz.cuni.mff.d3s.irm.model.trace.meta;

import cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor;
import cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter;
import cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage
 * @generated
 */
public interface TraceFactory extends EFactory {
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
	TraceFactory eINSTANCE = cz.cuni.mff.d3s.irm.model.trace.impl.TraceFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	TraceModel createTraceModel();

	/**
	 * Returns a new object of class '<em>Component Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Component Trace</em>'.
	 * @generated
	 */
	ComponentTrace createComponentTrace();

	/**
	 * Returns a new object of class '<em>Process Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Process Trace</em>'.
	 * @generated
	 */
	ProcessTrace createProcessTrace();

	/**
	 * Returns a new object of class '<em>Ensemble Definition Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ensemble Definition Trace</em>'.
	 * @generated
	 */
	EnsembleDefinitionTrace createEnsembleDefinitionTrace();

	/**
	 * Returns a new object of class '<em>Invariant Monitor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Invariant Monitor</em>'.
	 * @generated
	 */
	InvariantMonitor createInvariantMonitor();

	/**
	 * Returns a new object of class '<em>Monitor Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Monitor Parameter</em>'.
	 * @generated
	 */
	MonitorParameter createMonitorParameter();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	TracePackage getTracePackage();

} //TraceFactory
