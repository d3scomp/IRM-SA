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

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Monitor Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter#getType <em>Type</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter#getKnowledgePath <em>Knowledge Path</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getMonitorParameter()
 * @model
 * @generated
 */
public interface MonitorParameter extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(Class)
	 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getMonitorParameter_Type()
	 * @model required="true"
	 * @generated
	 */
	Class getType();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(Class value);

	/**
	 * Returns the value of the '<em><b>Knowledge Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Knowledge Path</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Knowledge Path</em>' containment reference.
	 * @see #setKnowledgePath(KnowledgePath)
	 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage#getMonitorParameter_KnowledgePath()
	 * @model containment="true" required="true"
	 * @generated
	 */
	KnowledgePath getKnowledgePath();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter#getKnowledgePath <em>Knowledge Path</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Knowledge Path</em>' containment reference.
	 * @see #getKnowledgePath()
	 * @generated
	 */
	void setKnowledgePath(KnowledgePath value);

} // MonitorParameter
