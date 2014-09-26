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
package cz.cuni.mff.d3s.irm.model.trace.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

import cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter;

import cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Monitor Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.impl.MonitorParameterImpl#getType <em>Type</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.trace.impl.MonitorParameterImpl#getKnowledgePath <em>Knowledge Path</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MonitorParameterImpl extends MinimalEObjectImpl.Container implements MonitorParameter {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected Class type;

	/**
	 * The cached value of the '{@link #getKnowledgePath() <em>Knowledge Path</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgePath()
	 * @generated
	 * @ordered
	 */
	protected KnowledgePath knowledgePath;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MonitorParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TracePackage.Literals.MONITOR_PARAMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Class getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(Class newType) {
		Class oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.MONITOR_PARAMETER__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgePath getKnowledgePath() {
		return knowledgePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKnowledgePath(KnowledgePath newKnowledgePath, NotificationChain msgs) {
		KnowledgePath oldKnowledgePath = knowledgePath;
		knowledgePath = newKnowledgePath;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TracePackage.MONITOR_PARAMETER__KNOWLEDGE_PATH, oldKnowledgePath, newKnowledgePath);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKnowledgePath(KnowledgePath newKnowledgePath) {
		if (newKnowledgePath != knowledgePath) {
			NotificationChain msgs = null;
			if (knowledgePath != null)
				msgs = ((InternalEObject)knowledgePath).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TracePackage.MONITOR_PARAMETER__KNOWLEDGE_PATH, null, msgs);
			if (newKnowledgePath != null)
				msgs = ((InternalEObject)newKnowledgePath).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TracePackage.MONITOR_PARAMETER__KNOWLEDGE_PATH, null, msgs);
			msgs = basicSetKnowledgePath(newKnowledgePath, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.MONITOR_PARAMETER__KNOWLEDGE_PATH, newKnowledgePath, newKnowledgePath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TracePackage.MONITOR_PARAMETER__KNOWLEDGE_PATH:
				return basicSetKnowledgePath(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TracePackage.MONITOR_PARAMETER__TYPE:
				return getType();
			case TracePackage.MONITOR_PARAMETER__KNOWLEDGE_PATH:
				return getKnowledgePath();
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
			case TracePackage.MONITOR_PARAMETER__TYPE:
				setType((Class)newValue);
				return;
			case TracePackage.MONITOR_PARAMETER__KNOWLEDGE_PATH:
				setKnowledgePath((KnowledgePath)newValue);
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
			case TracePackage.MONITOR_PARAMETER__TYPE:
				setType((Class)null);
				return;
			case TracePackage.MONITOR_PARAMETER__KNOWLEDGE_PATH:
				setKnowledgePath((KnowledgePath)null);
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
			case TracePackage.MONITOR_PARAMETER__TYPE:
				return type != null;
			case TracePackage.MONITOR_PARAMETER__KNOWLEDGE_PATH:
				return knowledgePath != null;
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
		result.append(" (type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} //MonitorParameterImpl
