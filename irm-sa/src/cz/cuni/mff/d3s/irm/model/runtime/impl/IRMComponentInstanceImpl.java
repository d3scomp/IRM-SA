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
package cz.cuni.mff.d3s.irm.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.irm.model.design.Component;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance;
import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IRM Component Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.impl.IRMComponentInstanceImpl#getIRMcomponent <em>IR Mcomponent</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.impl.IRMComponentInstanceImpl#getArchitectureInstance <em>Architecture Instance</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.impl.IRMComponentInstanceImpl#getKnowledgeSnapshot <em>Knowledge Snapshot</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IRMComponentInstanceImpl extends MinimalEObjectImpl.Container implements IRMComponentInstance {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * The default value of the '{@link #getIRMcomponent() <em>IR Mcomponent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIRMcomponent()
	 * @generated
	 * @ordered
	 */
	protected static final Component IR_MCOMPONENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIRMcomponent() <em>IR Mcomponent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIRMcomponent()
	 * @generated
	 * @ordered
	 */
	protected Component irMcomponent = IR_MCOMPONENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getArchitectureInstance() <em>Architecture Instance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArchitectureInstance()
	 * @generated
	 * @ordered
	 */
	protected static final cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance ARCHITECTURE_INSTANCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getArchitectureInstance() <em>Architecture Instance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArchitectureInstance()
	 * @generated
	 * @ordered
	 */
	protected cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance architectureInstance = ARCHITECTURE_INSTANCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getKnowledgeSnapshot() <em>Knowledge Snapshot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgeSnapshot()
	 * @generated
	 * @ordered
	 */
	protected static final ValueSet KNOWLEDGE_SNAPSHOT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKnowledgeSnapshot() <em>Knowledge Snapshot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgeSnapshot()
	 * @generated
	 * @ordered
	 */
	protected ValueSet knowledgeSnapshot = KNOWLEDGE_SNAPSHOT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IRMComponentInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IRMRuntimePackage.Literals.IRM_COMPONENT_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Component getIRMcomponent() {
		return irMcomponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIRMcomponent(Component newIRMcomponent) {
		Component oldIRMcomponent = irMcomponent;
		irMcomponent = newIRMcomponent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IRMRuntimePackage.IRM_COMPONENT_INSTANCE__IR_MCOMPONENT, oldIRMcomponent, irMcomponent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance getArchitectureInstance() {
		return architectureInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArchitectureInstance(cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance newArchitectureInstance) {
		cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance oldArchitectureInstance = architectureInstance;
		architectureInstance = newArchitectureInstance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IRMRuntimePackage.IRM_COMPONENT_INSTANCE__ARCHITECTURE_INSTANCE, oldArchitectureInstance, architectureInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValueSet getKnowledgeSnapshot() {
		return knowledgeSnapshot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKnowledgeSnapshot(ValueSet newKnowledgeSnapshot) {
		ValueSet oldKnowledgeSnapshot = knowledgeSnapshot;
		knowledgeSnapshot = newKnowledgeSnapshot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IRMRuntimePackage.IRM_COMPONENT_INSTANCE__KNOWLEDGE_SNAPSHOT, oldKnowledgeSnapshot, knowledgeSnapshot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__IR_MCOMPONENT:
				return getIRMcomponent();
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__ARCHITECTURE_INSTANCE:
				return getArchitectureInstance();
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__KNOWLEDGE_SNAPSHOT:
				return getKnowledgeSnapshot();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__IR_MCOMPONENT:
				setIRMcomponent((Component)newValue);
				return;
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__ARCHITECTURE_INSTANCE:
				setArchitectureInstance((cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance)newValue);
				return;
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__KNOWLEDGE_SNAPSHOT:
				setKnowledgeSnapshot((ValueSet)newValue);
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
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__IR_MCOMPONENT:
				setIRMcomponent(IR_MCOMPONENT_EDEFAULT);
				return;
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__ARCHITECTURE_INSTANCE:
				setArchitectureInstance(ARCHITECTURE_INSTANCE_EDEFAULT);
				return;
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__KNOWLEDGE_SNAPSHOT:
				setKnowledgeSnapshot(KNOWLEDGE_SNAPSHOT_EDEFAULT);
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
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__IR_MCOMPONENT:
				return IR_MCOMPONENT_EDEFAULT == null ? irMcomponent != null : !IR_MCOMPONENT_EDEFAULT.equals(irMcomponent);
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__ARCHITECTURE_INSTANCE:
				return ARCHITECTURE_INSTANCE_EDEFAULT == null ? architectureInstance != null : !ARCHITECTURE_INSTANCE_EDEFAULT.equals(architectureInstance);
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE__KNOWLEDGE_SNAPSHOT:
				return KNOWLEDGE_SNAPSHOT_EDEFAULT == null ? knowledgeSnapshot != null : !KNOWLEDGE_SNAPSHOT_EDEFAULT.equals(knowledgeSnapshot);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (IRMcomponent: ");
		result.append(irMcomponent);
		result.append(", architectureInstance: ");
		result.append(architectureInstance);
		result.append(", knowledgeSnapshot: ");
		result.append(knowledgeSnapshot);
		result.append(')');
		return result.toString();
	}

} //IRMComponentInstanceImpl
