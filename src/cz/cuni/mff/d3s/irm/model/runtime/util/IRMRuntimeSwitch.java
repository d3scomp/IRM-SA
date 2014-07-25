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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage
 * @generated
 */
public class IRMRuntimeSwitch<T> extends Switch<T> {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static IRMRuntimePackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IRMRuntimeSwitch() {
		if (modelPackage == null) {
			modelPackage = IRMRuntimePackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case IRMRuntimePackage.IRM_INSTANCE: {
				IRMInstance irmInstance = (IRMInstance)theEObject;
				T result = caseIRMInstance(irmInstance);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case IRMRuntimePackage.INVARIANT_INSTANCE: {
				InvariantInstance invariantInstance = (InvariantInstance)theEObject;
				T result = caseInvariantInstance(invariantInstance);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE: {
				IRMComponentInstance irmComponentInstance = (IRMComponentInstance)theEObject;
				T result = caseIRMComponentInstance(irmComponentInstance);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case IRMRuntimePackage.ALTERNATIVE: {
				Alternative alternative = (Alternative)theEObject;
				T result = caseAlternative(alternative);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case IRMRuntimePackage.PRESENT_INVARIANT_INSTANCE: {
				PresentInvariantInstance presentInvariantInstance = (PresentInvariantInstance)theEObject;
				T result = casePresentInvariantInstance(presentInvariantInstance);
				if (result == null) result = caseInvariantInstance(presentInvariantInstance);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case IRMRuntimePackage.SHADOW_INVARIANT_INSTANCE: {
				ShadowInvariantInstance shadowInvariantInstance = (ShadowInvariantInstance)theEObject;
				T result = caseShadowInvariantInstance(shadowInvariantInstance);
				if (result == null) result = caseInvariantInstance(shadowInvariantInstance);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case IRMRuntimePackage.PROCESS_INVARIANT_INSTANCE: {
				ProcessInvariantInstance processInvariantInstance = (ProcessInvariantInstance)theEObject;
				T result = caseProcessInvariantInstance(processInvariantInstance);
				if (result == null) result = casePresentInvariantInstance(processInvariantInstance);
				if (result == null) result = caseInvariantInstance(processInvariantInstance);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case IRMRuntimePackage.EXCHANGE_INVARIANT_INSTANCE: {
				ExchangeInvariantInstance exchangeInvariantInstance = (ExchangeInvariantInstance)theEObject;
				T result = caseExchangeInvariantInstance(exchangeInvariantInstance);
				if (result == null) result = casePresentInvariantInstance(exchangeInvariantInstance);
				if (result == null) result = caseInvariantInstance(exchangeInvariantInstance);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IRM Instance</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IRM Instance</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIRMInstance(IRMInstance object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Invariant Instance</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Invariant Instance</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInvariantInstance(InvariantInstance object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IRM Component Instance</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IRM Component Instance</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIRMComponentInstance(IRMComponentInstance object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Alternative</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Alternative</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAlternative(Alternative object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Present Invariant Instance</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Present Invariant Instance</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePresentInvariantInstance(PresentInvariantInstance object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Shadow Invariant Instance</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Shadow Invariant Instance</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseShadowInvariantInstance(ShadowInvariantInstance object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Process Invariant Instance</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Process Invariant Instance</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProcessInvariantInstance(ProcessInvariantInstance object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Exchange Invariant Instance</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Exchange Invariant Instance</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExchangeInvariantInstance(ExchangeInvariantInstance object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //IRMRuntimeSwitch
