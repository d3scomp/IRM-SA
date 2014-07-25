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
package cz.cuni.mff.d3s.irm.model.runtime.util;

import cz.cuni.mff.d3s.irm.model.runtime.api.*;

import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage
 * @generated
 */
public class IRMRuntimeAdapterFactory extends AdapterFactoryImpl {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static IRMRuntimePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IRMRuntimeAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = IRMRuntimePackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IRMRuntimeSwitch<Adapter> modelSwitch =
		new IRMRuntimeSwitch<Adapter>() {
			@Override
			public Adapter caseIRMInstance(IRMInstance object) {
				return createIRMInstanceAdapter();
			}
			@Override
			public Adapter caseInvariantInstance(InvariantInstance object) {
				return createInvariantInstanceAdapter();
			}
			@Override
			public Adapter caseIRMComponentInstance(IRMComponentInstance object) {
				return createIRMComponentInstanceAdapter();
			}
			@Override
			public Adapter caseAlternative(Alternative object) {
				return createAlternativeAdapter();
			}
			@Override
			public Adapter casePresentInvariantInstance(PresentInvariantInstance object) {
				return createPresentInvariantInstanceAdapter();
			}
			@Override
			public Adapter caseShadowInvariantInstance(ShadowInvariantInstance object) {
				return createShadowInvariantInstanceAdapter();
			}
			@Override
			public Adapter caseProcessInvariantInstance(ProcessInvariantInstance object) {
				return createProcessInvariantInstanceAdapter();
			}
			@Override
			public Adapter caseExchangeInvariantInstance(ExchangeInvariantInstance object) {
				return createExchangeInvariantInstanceAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance <em>IRM Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance
	 * @generated
	 */
	public Adapter createIRMInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance <em>Invariant Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance
	 * @generated
	 */
	public Adapter createInvariantInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance <em>IRM Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance
	 * @generated
	 */
	public Adapter createIRMComponentInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.Alternative <em>Alternative</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.Alternative
	 * @generated
	 */
	public Adapter createAlternativeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance <em>Present Invariant Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance
	 * @generated
	 */
	public Adapter createPresentInvariantInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.ShadowInvariantInstance <em>Shadow Invariant Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.ShadowInvariantInstance
	 * @generated
	 */
	public Adapter createShadowInvariantInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance <em>Process Invariant Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance
	 * @generated
	 */
	public Adapter createProcessInvariantInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance <em>Exchange Invariant Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance
	 * @generated
	 */
	public Adapter createExchangeInvariantInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //IRMRuntimeAdapterFactory
