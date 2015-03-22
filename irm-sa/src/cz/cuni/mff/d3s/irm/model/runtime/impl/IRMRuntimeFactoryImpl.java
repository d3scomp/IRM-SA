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
import cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.irm.model.design.Component;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.Invariant;
import cz.cuni.mff.d3s.irm.model.runtime.api.*;
import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimeFactory;
import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class IRMRuntimeFactoryImpl extends EFactoryImpl implements IRMRuntimeFactory {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static IRMRuntimeFactory init() {
		try {
			IRMRuntimeFactory theIRMRuntimeFactory = (IRMRuntimeFactory)EPackage.Registry.INSTANCE.getEFactory(IRMRuntimePackage.eNS_URI);
			if (theIRMRuntimeFactory != null) {
				return theIRMRuntimeFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new IRMRuntimeFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IRMRuntimeFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case IRMRuntimePackage.IRM_INSTANCE: return createIRMInstance();
			case IRMRuntimePackage.IRM_COMPONENT_INSTANCE: return createIRMComponentInstance();
			case IRMRuntimePackage.ALTERNATIVE: return createAlternative();
			case IRMRuntimePackage.PRESENT_INVARIANT_INSTANCE: return createPresentInvariantInstance();
			case IRMRuntimePackage.SHADOW_INVARIANT_INSTANCE: return createShadowInvariantInstance();
			case IRMRuntimePackage.PROCESS_INVARIANT_INSTANCE: return createProcessInvariantInstance();
			case IRMRuntimePackage.EXCHANGE_INVARIANT_INSTANCE: return createExchangeInvariantInstance();
			case IRMRuntimePackage.ASSUMPTION_INSTANCE: return createAssumptionInstance();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case IRMRuntimePackage.INVARIANT_TYPE:
				return createInvariantTypeFromString(eDataType, initialValue);
			case IRMRuntimePackage.IRM_COMPONENT_TYPE:
				return createIRMComponentTypeFromString(eDataType, initialValue);
			case IRMRuntimePackage.COMPONENT_INSTANCE_TYPE:
				return createComponentInstanceTypeFromString(eDataType, initialValue);
			case IRMRuntimePackage.VALUE_SET_TYPE:
				return createValueSetTypeFromString(eDataType, initialValue);
			case IRMRuntimePackage.IRM_DESIGN_MODEL_TYPE:
				return createIRMDesignModelTypeFromString(eDataType, initialValue);
			case IRMRuntimePackage.TRACE_MODEL_TYPE:
				return createTraceModelTypeFromString(eDataType, initialValue);
			case IRMRuntimePackage.COMPONENT_PROCESS_TYPE:
				return createComponentProcessTypeFromString(eDataType, initialValue);
			case IRMRuntimePackage.ENSEMBLE_DEFINITION_TYPE:
				return createEnsembleDefinitionTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case IRMRuntimePackage.INVARIANT_TYPE:
				return convertInvariantTypeToString(eDataType, instanceValue);
			case IRMRuntimePackage.IRM_COMPONENT_TYPE:
				return convertIRMComponentTypeToString(eDataType, instanceValue);
			case IRMRuntimePackage.COMPONENT_INSTANCE_TYPE:
				return convertComponentInstanceTypeToString(eDataType, instanceValue);
			case IRMRuntimePackage.VALUE_SET_TYPE:
				return convertValueSetTypeToString(eDataType, instanceValue);
			case IRMRuntimePackage.IRM_DESIGN_MODEL_TYPE:
				return convertIRMDesignModelTypeToString(eDataType, instanceValue);
			case IRMRuntimePackage.TRACE_MODEL_TYPE:
				return convertTraceModelTypeToString(eDataType, instanceValue);
			case IRMRuntimePackage.COMPONENT_PROCESS_TYPE:
				return convertComponentProcessTypeToString(eDataType, instanceValue);
			case IRMRuntimePackage.ENSEMBLE_DEFINITION_TYPE:
				return convertEnsembleDefinitionTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IRMInstance createIRMInstance() {
		IRMInstanceImpl irmInstance = new IRMInstanceImpl();
		return irmInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IRMComponentInstance createIRMComponentInstance() {
		IRMComponentInstanceImpl irmComponentInstance = new IRMComponentInstanceImpl();
		return irmComponentInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Alternative createAlternative() {
		AlternativeImpl alternative = new AlternativeImpl();
		return alternative;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PresentInvariantInstance createPresentInvariantInstance() {
		PresentInvariantInstanceImpl presentInvariantInstance = new PresentInvariantInstanceImpl();
		return presentInvariantInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShadowInvariantInstance createShadowInvariantInstance() {
		ShadowInvariantInstanceImpl shadowInvariantInstance = new ShadowInvariantInstanceImpl();
		return shadowInvariantInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcessInvariantInstance createProcessInvariantInstance() {
		ProcessInvariantInstanceImpl processInvariantInstance = new ProcessInvariantInstanceImpl();
		return processInvariantInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExchangeInvariantInstance createExchangeInvariantInstance() {
		ExchangeInvariantInstanceImpl exchangeInvariantInstance = new ExchangeInvariantInstanceImpl();
		return exchangeInvariantInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssumptionInstance createAssumptionInstance() {
		AssumptionInstanceImpl assumptionInstance = new AssumptionInstanceImpl();
		return assumptionInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Invariant createInvariantTypeFromString(EDataType eDataType, String initialValue) {
		return (Invariant)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertInvariantTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Component createIRMComponentTypeFromString(EDataType eDataType, String initialValue) {
		return (Component)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIRMComponentTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentInstance createComponentInstanceTypeFromString(EDataType eDataType, String initialValue) {
		return (ComponentInstance)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertComponentInstanceTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValueSet createValueSetTypeFromString(EDataType eDataType, String initialValue) {
		return (ValueSet)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertValueSetTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IRM createIRMDesignModelTypeFromString(EDataType eDataType, String initialValue) {
		return (IRM)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIRMDesignModelTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TraceModel createTraceModelTypeFromString(EDataType eDataType, String initialValue) {
		return (TraceModel)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTraceModelTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess createComponentProcessTypeFromString(EDataType eDataType, String initialValue) {
		return (ComponentProcess)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertComponentProcessTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnsembleDefinition createEnsembleDefinitionTypeFromString(EDataType eDataType, String initialValue) {
		return (EnsembleDefinition)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEnsembleDefinitionTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IRMRuntimePackage getIRMRuntimePackage() {
		return (IRMRuntimePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static IRMRuntimePackage getPackage() {
		return IRMRuntimePackage.eINSTANCE;
	}

} //IRMRuntimeFactoryImpl
