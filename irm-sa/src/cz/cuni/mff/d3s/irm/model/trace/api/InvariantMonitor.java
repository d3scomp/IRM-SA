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

import cz.cuni.mff.d3s.irm.model.design.Invariant;

import java.lang.reflect.Method;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Invariant Monitor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor#getInvariant <em>Invariant</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor#getMethod <em>Method</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor#getMonitorParameters <em>Monitor Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getInvariantMonitor()
 * @model
 * @generated
 */
public interface InvariantMonitor extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * Returns the value of the '<em><b>Invariant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Invariant</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Invariant</em>' attribute.
	 * @see #setInvariant(Invariant)
	 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getInvariantMonitor_Invariant()
	 * @model dataType="cz.cuni.mff.d3s.irm.model.trace.api.InvariantType" required="true"
	 * @generated
	 */
	Invariant getInvariant();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor#getInvariant <em>Invariant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Invariant</em>' attribute.
	 * @see #getInvariant()
	 * @generated
	 */
	void setInvariant(Invariant value);

	/**
	 * Returns the value of the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method</em>' attribute.
	 * @see #setMethod(Method)
	 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getInvariantMonitor_Method()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.Method" required="true"
	 * @generated
	 */
	Method getMethod();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor#getMethod <em>Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method</em>' attribute.
	 * @see #getMethod()
	 * @generated
	 */
	void setMethod(Method value);

	/**
	 * Returns the value of the '<em><b>Monitor Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Monitor Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Monitor Parameters</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getInvariantMonitor_MonitorParameters()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<MonitorParameter> getMonitorParameters();

} // InvariantMonitor
