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

import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IRM Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.impl.IRMInstanceImpl#getInvariantInstances <em>Invariant Instances</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.impl.IRMInstanceImpl#getIRMcomponentInstances <em>IR Mcomponent Instances</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.impl.IRMInstanceImpl#getDesignModel <em>Design Model</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.impl.IRMInstanceImpl#getTraceModel <em>Trace Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IRMInstanceImpl extends MinimalEObjectImpl.Container implements IRMInstance {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * The cached value of the '{@link #getInvariantInstances() <em>Invariant Instances</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInvariantInstances()
	 * @generated
	 * @ordered
	 */
	protected EList<InvariantInstance> invariantInstances;

	/**
	 * The cached value of the '{@link #getIRMcomponentInstances() <em>IR Mcomponent Instances</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIRMcomponentInstances()
	 * @generated
	 * @ordered
	 */
	protected EList<IRMComponentInstance> irMcomponentInstances;

	/**
	 * The default value of the '{@link #getDesignModel() <em>Design Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesignModel()
	 * @generated
	 * @ordered
	 */
	protected static final IRM DESIGN_MODEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDesignModel() <em>Design Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesignModel()
	 * @generated
	 * @ordered
	 */
	protected IRM designModel = DESIGN_MODEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getTraceModel() <em>Trace Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraceModel()
	 * @generated
	 * @ordered
	 */
	protected static final TraceModel TRACE_MODEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTraceModel() <em>Trace Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraceModel()
	 * @generated
	 * @ordered
	 */
	protected TraceModel traceModel = TRACE_MODEL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IRMInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IRMRuntimePackage.Literals.IRM_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<InvariantInstance> getInvariantInstances() {
		if (invariantInstances == null) {
			invariantInstances = new EObjectContainmentWithInverseEList<InvariantInstance>(InvariantInstance.class, this, IRMRuntimePackage.IRM_INSTANCE__INVARIANT_INSTANCES, IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE);
		}
		return invariantInstances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<IRMComponentInstance> getIRMcomponentInstances() {
		if (irMcomponentInstances == null) {
			irMcomponentInstances = new EObjectContainmentEList<IRMComponentInstance>(IRMComponentInstance.class, this, IRMRuntimePackage.IRM_INSTANCE__IR_MCOMPONENT_INSTANCES);
		}
		return irMcomponentInstances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IRM getDesignModel() {
		return designModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDesignModel(IRM newDesignModel) {
		IRM oldDesignModel = designModel;
		designModel = newDesignModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IRMRuntimePackage.IRM_INSTANCE__DESIGN_MODEL, oldDesignModel, designModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TraceModel getTraceModel() {
		return traceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTraceModel(TraceModel newTraceModel) {
		TraceModel oldTraceModel = traceModel;
		traceModel = newTraceModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IRMRuntimePackage.IRM_INSTANCE__TRACE_MODEL, oldTraceModel, traceModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public List<InvariantInstance> getRoots() {
		List<InvariantInstance> roots = new ArrayList<>();
		for (InvariantInstance i : getInvariantInstances()) {
			if (i.getParent() == null) {
				roots.add(i);
			}
		}
		return roots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case IRMRuntimePackage.IRM_INSTANCE__INVARIANT_INSTANCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getInvariantInstances()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case IRMRuntimePackage.IRM_INSTANCE__INVARIANT_INSTANCES:
				return ((InternalEList<?>)getInvariantInstances()).basicRemove(otherEnd, msgs);
			case IRMRuntimePackage.IRM_INSTANCE__IR_MCOMPONENT_INSTANCES:
				return ((InternalEList<?>)getIRMcomponentInstances()).basicRemove(otherEnd, msgs);
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
			case IRMRuntimePackage.IRM_INSTANCE__INVARIANT_INSTANCES:
				return getInvariantInstances();
			case IRMRuntimePackage.IRM_INSTANCE__IR_MCOMPONENT_INSTANCES:
				return getIRMcomponentInstances();
			case IRMRuntimePackage.IRM_INSTANCE__DESIGN_MODEL:
				return getDesignModel();
			case IRMRuntimePackage.IRM_INSTANCE__TRACE_MODEL:
				return getTraceModel();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case IRMRuntimePackage.IRM_INSTANCE__INVARIANT_INSTANCES:
				getInvariantInstances().clear();
				getInvariantInstances().addAll((Collection<? extends InvariantInstance>)newValue);
				return;
			case IRMRuntimePackage.IRM_INSTANCE__IR_MCOMPONENT_INSTANCES:
				getIRMcomponentInstances().clear();
				getIRMcomponentInstances().addAll((Collection<? extends IRMComponentInstance>)newValue);
				return;
			case IRMRuntimePackage.IRM_INSTANCE__DESIGN_MODEL:
				setDesignModel((IRM)newValue);
				return;
			case IRMRuntimePackage.IRM_INSTANCE__TRACE_MODEL:
				setTraceModel((TraceModel)newValue);
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
			case IRMRuntimePackage.IRM_INSTANCE__INVARIANT_INSTANCES:
				getInvariantInstances().clear();
				return;
			case IRMRuntimePackage.IRM_INSTANCE__IR_MCOMPONENT_INSTANCES:
				getIRMcomponentInstances().clear();
				return;
			case IRMRuntimePackage.IRM_INSTANCE__DESIGN_MODEL:
				setDesignModel(DESIGN_MODEL_EDEFAULT);
				return;
			case IRMRuntimePackage.IRM_INSTANCE__TRACE_MODEL:
				setTraceModel(TRACE_MODEL_EDEFAULT);
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
			case IRMRuntimePackage.IRM_INSTANCE__INVARIANT_INSTANCES:
				return invariantInstances != null && !invariantInstances.isEmpty();
			case IRMRuntimePackage.IRM_INSTANCE__IR_MCOMPONENT_INSTANCES:
				return irMcomponentInstances != null && !irMcomponentInstances.isEmpty();
			case IRMRuntimePackage.IRM_INSTANCE__DESIGN_MODEL:
				return DESIGN_MODEL_EDEFAULT == null ? designModel != null : !DESIGN_MODEL_EDEFAULT.equals(designModel);
			case IRMRuntimePackage.IRM_INSTANCE__TRACE_MODEL:
				return TRACE_MODEL_EDEFAULT == null ? traceModel != null : !TRACE_MODEL_EDEFAULT.equals(traceModel);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case IRMRuntimePackage.IRM_INSTANCE___GET_ROOTS:
				return getRoots();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (designModel: ");
		result.append(designModel);
		result.append(", traceModel: ");
		result.append(traceModel);
		result.append(')');
		return result.toString();
	}

} //IRMInstanceImpl
