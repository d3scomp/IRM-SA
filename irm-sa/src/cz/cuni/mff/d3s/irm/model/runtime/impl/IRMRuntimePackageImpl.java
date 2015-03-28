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
import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ShadowInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimeFactory;
import cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class IRMRuntimePackageImpl extends EPackageImpl implements IRMRuntimePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass irmInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass invariantInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass irmComponentInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass alternativeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass presentInvariantInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass shadowInvariantInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass processInvariantInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass exchangeInvariantInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass assumptionInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType invariantTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType irmComponentTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType componentInstanceTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType valueSetTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType irmDesignModelTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType traceModelTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType componentProcessTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType ensembleDefinitionTypeEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private IRMRuntimePackageImpl() {
		super(eNS_URI, IRMRuntimeFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link IRMRuntimePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static IRMRuntimePackage init() {
		if (isInited) return (IRMRuntimePackage)EPackage.Registry.INSTANCE.getEPackage(IRMRuntimePackage.eNS_URI);

		// Obtain or create and register package
		IRMRuntimePackageImpl theIRMRuntimePackage = (IRMRuntimePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof IRMRuntimePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new IRMRuntimePackageImpl());

		isInited = true;

		// Create package meta-data objects
		theIRMRuntimePackage.createPackageContents();

		// Initialize created meta-data
		theIRMRuntimePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theIRMRuntimePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(IRMRuntimePackage.eNS_URI, theIRMRuntimePackage);
		return theIRMRuntimePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIRMInstance() {
		return irmInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIRMInstance_InvariantInstances() {
		return (EReference)irmInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIRMInstance_IRMcomponentInstances() {
		return (EReference)irmInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIRMInstance_DesignModel() {
		return (EAttribute)irmInstanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIRMInstance_TraceModel() {
		return (EAttribute)irmInstanceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getIRMInstance__GetRoots() {
		return irmInstanceEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInvariantInstance() {
		return invariantInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInvariantInstance_DiagramInstance() {
		return (EReference)invariantInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInvariantInstance_Satisfied() {
		return (EAttribute)invariantInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInvariantInstance_Selected() {
		return (EAttribute)invariantInstanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInvariantInstance_Alternatives() {
		return (EReference)invariantInstanceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInvariantInstance_Fitness() {
		return (EAttribute)invariantInstanceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getInvariantInstance__GetParent() {
		return invariantInstanceEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIRMComponentInstance() {
		return irmComponentInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIRMComponentInstance_IRMcomponent() {
		return (EAttribute)irmComponentInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIRMComponentInstance_ArchitectureInstance() {
		return (EAttribute)irmComponentInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIRMComponentInstance_KnowledgeSnapshot() {
		return (EAttribute)irmComponentInstanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAlternative() {
		return alternativeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAlternative_Children() {
		return (EReference)alternativeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPresentInvariantInstance() {
		return presentInvariantInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPresentInvariantInstance_Invariant() {
		return (EAttribute)presentInvariantInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getShadowInvariantInstance() {
		return shadowInvariantInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProcessInvariantInstance() {
		return processInvariantInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcessInvariantInstance_ComponentProcess() {
		return (EAttribute)processInvariantInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExchangeInvariantInstance() {
		return exchangeInvariantInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExchangeInvariantInstance_EnsembleDefinition() {
		return (EAttribute)exchangeInvariantInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAssumptionInstance() {
		return assumptionInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssumptionInstance_ComponentInstance() {
		return (EAttribute)assumptionInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getInvariantType() {
		return invariantTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIRMComponentType() {
		return irmComponentTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getComponentInstanceType() {
		return componentInstanceTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getValueSetType() {
		return valueSetTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIRMDesignModelType() {
		return irmDesignModelTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getTraceModelType() {
		return traceModelTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getComponentProcessType() {
		return componentProcessTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getEnsembleDefinitionType() {
		return ensembleDefinitionTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IRMRuntimeFactory getIRMRuntimeFactory() {
		return (IRMRuntimeFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		irmInstanceEClass = createEClass(IRM_INSTANCE);
		createEReference(irmInstanceEClass, IRM_INSTANCE__INVARIANT_INSTANCES);
		createEReference(irmInstanceEClass, IRM_INSTANCE__IR_MCOMPONENT_INSTANCES);
		createEAttribute(irmInstanceEClass, IRM_INSTANCE__DESIGN_MODEL);
		createEAttribute(irmInstanceEClass, IRM_INSTANCE__TRACE_MODEL);
		createEOperation(irmInstanceEClass, IRM_INSTANCE___GET_ROOTS);

		invariantInstanceEClass = createEClass(INVARIANT_INSTANCE);
		createEReference(invariantInstanceEClass, INVARIANT_INSTANCE__DIAGRAM_INSTANCE);
		createEAttribute(invariantInstanceEClass, INVARIANT_INSTANCE__SATISFIED);
		createEAttribute(invariantInstanceEClass, INVARIANT_INSTANCE__SELECTED);
		createEReference(invariantInstanceEClass, INVARIANT_INSTANCE__ALTERNATIVES);
		createEAttribute(invariantInstanceEClass, INVARIANT_INSTANCE__FITNESS);
		createEOperation(invariantInstanceEClass, INVARIANT_INSTANCE___GET_PARENT);

		irmComponentInstanceEClass = createEClass(IRM_COMPONENT_INSTANCE);
		createEAttribute(irmComponentInstanceEClass, IRM_COMPONENT_INSTANCE__IR_MCOMPONENT);
		createEAttribute(irmComponentInstanceEClass, IRM_COMPONENT_INSTANCE__ARCHITECTURE_INSTANCE);
		createEAttribute(irmComponentInstanceEClass, IRM_COMPONENT_INSTANCE__KNOWLEDGE_SNAPSHOT);

		alternativeEClass = createEClass(ALTERNATIVE);
		createEReference(alternativeEClass, ALTERNATIVE__CHILDREN);

		presentInvariantInstanceEClass = createEClass(PRESENT_INVARIANT_INSTANCE);
		createEAttribute(presentInvariantInstanceEClass, PRESENT_INVARIANT_INSTANCE__INVARIANT);

		shadowInvariantInstanceEClass = createEClass(SHADOW_INVARIANT_INSTANCE);

		processInvariantInstanceEClass = createEClass(PROCESS_INVARIANT_INSTANCE);
		createEAttribute(processInvariantInstanceEClass, PROCESS_INVARIANT_INSTANCE__COMPONENT_PROCESS);

		exchangeInvariantInstanceEClass = createEClass(EXCHANGE_INVARIANT_INSTANCE);
		createEAttribute(exchangeInvariantInstanceEClass, EXCHANGE_INVARIANT_INSTANCE__ENSEMBLE_DEFINITION);

		assumptionInstanceEClass = createEClass(ASSUMPTION_INSTANCE);
		createEAttribute(assumptionInstanceEClass, ASSUMPTION_INSTANCE__COMPONENT_INSTANCE);

		// Create data types
		invariantTypeEDataType = createEDataType(INVARIANT_TYPE);
		irmComponentTypeEDataType = createEDataType(IRM_COMPONENT_TYPE);
		componentInstanceTypeEDataType = createEDataType(COMPONENT_INSTANCE_TYPE);
		valueSetTypeEDataType = createEDataType(VALUE_SET_TYPE);
		irmDesignModelTypeEDataType = createEDataType(IRM_DESIGN_MODEL_TYPE);
		traceModelTypeEDataType = createEDataType(TRACE_MODEL_TYPE);
		componentProcessTypeEDataType = createEDataType(COMPONENT_PROCESS_TYPE);
		ensembleDefinitionTypeEDataType = createEDataType(ENSEMBLE_DEFINITION_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		presentInvariantInstanceEClass.getESuperTypes().add(this.getInvariantInstance());
		shadowInvariantInstanceEClass.getESuperTypes().add(this.getInvariantInstance());
		processInvariantInstanceEClass.getESuperTypes().add(this.getPresentInvariantInstance());
		exchangeInvariantInstanceEClass.getESuperTypes().add(this.getPresentInvariantInstance());
		assumptionInstanceEClass.getESuperTypes().add(this.getPresentInvariantInstance());

		// Initialize classes, features, and operations; add parameters
		initEClass(irmInstanceEClass, IRMInstance.class, "IRMInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIRMInstance_InvariantInstances(), this.getInvariantInstance(), this.getInvariantInstance_DiagramInstance(), "invariantInstances", null, 0, -1, IRMInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIRMInstance_IRMcomponentInstances(), this.getIRMComponentInstance(), null, "IRMcomponentInstances", null, 0, -1, IRMInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIRMInstance_DesignModel(), this.getIRMDesignModelType(), "designModel", null, 1, 1, IRMInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIRMInstance_TraceModel(), this.getTraceModelType(), "traceModel", null, 1, 1, IRMInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getIRMInstance__GetRoots(), this.getInvariantInstance(), "getRoots", 1, -1, IS_UNIQUE, IS_ORDERED);

		initEClass(invariantInstanceEClass, InvariantInstance.class, "InvariantInstance", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInvariantInstance_DiagramInstance(), this.getIRMInstance(), this.getIRMInstance_InvariantInstances(), "diagramInstance", null, 1, 1, InvariantInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInvariantInstance_Satisfied(), ecorePackage.getEBoolean(), "satisfied", "true", 1, 1, InvariantInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInvariantInstance_Selected(), ecorePackage.getEBoolean(), "selected", null, 1, 1, InvariantInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInvariantInstance_Alternatives(), this.getAlternative(), null, "alternatives", null, 0, -1, InvariantInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getInvariantInstance_Fitness(), ecorePackage.getEDouble(), "fitness", null, 1, 1, InvariantInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getInvariantInstance__GetParent(), this.getInvariantInstance(), "getParent", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(irmComponentInstanceEClass, IRMComponentInstance.class, "IRMComponentInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIRMComponentInstance_IRMcomponent(), this.getIRMComponentType(), "IRMcomponent", null, 1, 1, IRMComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIRMComponentInstance_ArchitectureInstance(), this.getComponentInstanceType(), "architectureInstance", null, 1, 1, IRMComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIRMComponentInstance_KnowledgeSnapshot(), this.getValueSetType(), "knowledgeSnapshot", null, 1, 1, IRMComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(alternativeEClass, Alternative.class, "Alternative", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAlternative_Children(), this.getInvariantInstance(), null, "children", null, 0, -1, Alternative.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(presentInvariantInstanceEClass, PresentInvariantInstance.class, "PresentInvariantInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPresentInvariantInstance_Invariant(), this.getInvariantType(), "invariant", null, 1, 1, PresentInvariantInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(shadowInvariantInstanceEClass, ShadowInvariantInstance.class, "ShadowInvariantInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(processInvariantInstanceEClass, ProcessInvariantInstance.class, "ProcessInvariantInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProcessInvariantInstance_ComponentProcess(), this.getComponentProcessType(), "componentProcess", null, 0, 1, ProcessInvariantInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(exchangeInvariantInstanceEClass, ExchangeInvariantInstance.class, "ExchangeInvariantInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExchangeInvariantInstance_EnsembleDefinition(), this.getEnsembleDefinitionType(), "ensembleDefinition", null, 0, 1, ExchangeInvariantInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(assumptionInstanceEClass, AssumptionInstance.class, "AssumptionInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAssumptionInstance_ComponentInstance(), this.getComponentInstanceType(), "componentInstance", null, 0, 1, AssumptionInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(invariantTypeEDataType, Invariant.class, "InvariantType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(irmComponentTypeEDataType, Component.class, "IRMComponentType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(componentInstanceTypeEDataType, ComponentInstance.class, "ComponentInstanceType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(valueSetTypeEDataType, ValueSet.class, "ValueSetType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(irmDesignModelTypeEDataType, IRM.class, "IRMDesignModelType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(traceModelTypeEDataType, TraceModel.class, "TraceModelType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(componentProcessTypeEDataType, ComponentProcess.class, "ComponentProcessType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(ensembleDefinitionTypeEDataType, EnsembleDefinition.class, "EnsembleDefinitionType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //IRMRuntimePackageImpl
