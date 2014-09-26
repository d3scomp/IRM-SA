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

import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;

import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage;

import java.lang.reflect.InvocationTargetException;

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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Invariant Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.impl.InvariantInstanceImpl#getDiagramInstance <em>Diagram Instance</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.impl.InvariantInstanceImpl#isSatisfied <em>Satisfied</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.impl.InvariantInstanceImpl#isSelected <em>Selected</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.irm.model.runtime.impl.InvariantInstanceImpl#getAlternatives <em>Alternatives</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class InvariantInstanceImpl extends MinimalEObjectImpl.Container implements InvariantInstance {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * The default value of the '{@link #isSatisfied() <em>Satisfied</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSatisfied()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SATISFIED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSatisfied() <em>Satisfied</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSatisfied()
	 * @generated
	 * @ordered
	 */
	protected boolean satisfied = SATISFIED_EDEFAULT;

	/**
	 * The default value of the '{@link #isSelected() <em>Selected</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSelected()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SELECTED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSelected() <em>Selected</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSelected()
	 * @generated
	 * @ordered
	 */
	protected boolean selected = SELECTED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAlternatives() <em>Alternatives</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlternatives()
	 * @generated
	 * @ordered
	 */
	protected EList<Alternative> alternatives;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InvariantInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IRMRuntimePackage.Literals.INVARIANT_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IRMInstance getDiagramInstance() {
		if (eContainerFeatureID() != IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE) return null;
		return (IRMInstance)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDiagramInstance(IRMInstance newDiagramInstance, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newDiagramInstance, IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiagramInstance(IRMInstance newDiagramInstance) {
		if (newDiagramInstance != eInternalContainer() || (eContainerFeatureID() != IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE && newDiagramInstance != null)) {
			if (EcoreUtil.isAncestor(this, newDiagramInstance))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newDiagramInstance != null)
				msgs = ((InternalEObject)newDiagramInstance).eInverseAdd(this, IRMRuntimePackage.IRM_INSTANCE__INVARIANT_INSTANCES, IRMInstance.class, msgs);
			msgs = basicSetDiagramInstance(newDiagramInstance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE, newDiagramInstance, newDiagramInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSatisfied() {
		return satisfied;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSatisfied(boolean newSatisfied) {
		boolean oldSatisfied = satisfied;
		satisfied = newSatisfied;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IRMRuntimePackage.INVARIANT_INSTANCE__SATISFIED, oldSatisfied, satisfied));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelected(boolean newSelected) {
		boolean oldSelected = selected;
		selected = newSelected;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IRMRuntimePackage.INVARIANT_INSTANCE__SELECTED, oldSelected, selected));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<Alternative> getAlternatives() {
		if (alternatives == null) {
			alternatives = new EObjectContainmentEList.Unsettable<Alternative>(Alternative.class, this, IRMRuntimePackage.INVARIANT_INSTANCE__ALTERNATIVES);
		}
		return alternatives;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAlternatives() {
		if (alternatives != null) ((InternalEList.Unsettable<?>)alternatives).unset();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAlternatives() {
		return alternatives != null && ((InternalEList.Unsettable<?>)alternatives).isSet();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public InvariantInstance getParent() {
		for (InvariantInstance i : getDiagramInstance().getInvariantInstances()) {
			for (Alternative a : i.getAlternatives()) {
				if (a.getChildren().contains(this)) {
					return i;
				}
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetDiagramInstance((IRMInstance)otherEnd, msgs);
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
			case IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE:
				return basicSetDiagramInstance(null, msgs);
			case IRMRuntimePackage.INVARIANT_INSTANCE__ALTERNATIVES:
				return ((InternalEList<?>)getAlternatives()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE:
				return eInternalContainer().eInverseRemove(this, IRMRuntimePackage.IRM_INSTANCE__INVARIANT_INSTANCES, IRMInstance.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE:
				return getDiagramInstance();
			case IRMRuntimePackage.INVARIANT_INSTANCE__SATISFIED:
				return isSatisfied();
			case IRMRuntimePackage.INVARIANT_INSTANCE__SELECTED:
				return isSelected();
			case IRMRuntimePackage.INVARIANT_INSTANCE__ALTERNATIVES:
				return getAlternatives();
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
			case IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE:
				setDiagramInstance((IRMInstance)newValue);
				return;
			case IRMRuntimePackage.INVARIANT_INSTANCE__SATISFIED:
				setSatisfied((Boolean)newValue);
				return;
			case IRMRuntimePackage.INVARIANT_INSTANCE__SELECTED:
				setSelected((Boolean)newValue);
				return;
			case IRMRuntimePackage.INVARIANT_INSTANCE__ALTERNATIVES:
				getAlternatives().clear();
				getAlternatives().addAll((Collection<? extends Alternative>)newValue);
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
			case IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE:
				setDiagramInstance((IRMInstance)null);
				return;
			case IRMRuntimePackage.INVARIANT_INSTANCE__SATISFIED:
				setSatisfied(SATISFIED_EDEFAULT);
				return;
			case IRMRuntimePackage.INVARIANT_INSTANCE__SELECTED:
				setSelected(SELECTED_EDEFAULT);
				return;
			case IRMRuntimePackage.INVARIANT_INSTANCE__ALTERNATIVES:
				unsetAlternatives();
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
			case IRMRuntimePackage.INVARIANT_INSTANCE__DIAGRAM_INSTANCE:
				return getDiagramInstance() != null;
			case IRMRuntimePackage.INVARIANT_INSTANCE__SATISFIED:
				return satisfied != SATISFIED_EDEFAULT;
			case IRMRuntimePackage.INVARIANT_INSTANCE__SELECTED:
				return selected != SELECTED_EDEFAULT;
			case IRMRuntimePackage.INVARIANT_INSTANCE__ALTERNATIVES:
				return isSetAlternatives();
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
			case IRMRuntimePackage.INVARIANT_INSTANCE___GET_PARENT:
				return getParent();
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
		result.append(" (satisfied: ");
		result.append(satisfied);
		result.append(", selected: ");
		result.append(selected);
		result.append(')');
		return result.toString();
	}

} //InvariantInstanceImpl
