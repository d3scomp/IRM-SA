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
package cz.cuni.mff.d3s.irm.model.runtime.meta;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.irm.model.runtime.meta.IRMRuntimeFactory
 * @model kind="package"
 * @generated
 */
public interface IRMRuntimePackage extends EPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright 2014 Charles University in Prague\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\nhttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";

	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "runtime";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://cz.cuni.mff.d3s.irm.model.runtime/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "cz.cuni.mff.d3s.irm.model.runtime";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	IRMRuntimePackage eINSTANCE = cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl.init();

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.IRMInstanceImpl <em>IRM Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMInstanceImpl
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getIRMInstance()
	 * @generated
	 */
	int IRM_INSTANCE = 0;

	/**
	 * The feature id for the '<em><b>Invariant Instances</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_INSTANCE__INVARIANT_INSTANCES = 0;

	/**
	 * The feature id for the '<em><b>IR Mcomponent Instances</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_INSTANCE__IR_MCOMPONENT_INSTANCES = 1;

	/**
	 * The feature id for the '<em><b>Design Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_INSTANCE__DESIGN_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Trace Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_INSTANCE__TRACE_MODEL = 3;

	/**
	 * The number of structural features of the '<em>IRM Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_INSTANCE_FEATURE_COUNT = 4;

	/**
	 * The operation id for the '<em>Get Roots</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_INSTANCE___GET_ROOTS = 0;

	/**
	 * The number of operations of the '<em>IRM Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_INSTANCE_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.InvariantInstanceImpl <em>Invariant Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.InvariantInstanceImpl
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getInvariantInstance()
	 * @generated
	 */
	int INVARIANT_INSTANCE = 1;

	/**
	 * The feature id for the '<em><b>Diagram Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_INSTANCE__DIAGRAM_INSTANCE = 0;

	/**
	 * The feature id for the '<em><b>Satisfied</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_INSTANCE__SATISFIED = 1;

	/**
	 * The feature id for the '<em><b>Selected</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_INSTANCE__SELECTED = 2;

	/**
	 * The feature id for the '<em><b>Alternatives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_INSTANCE__ALTERNATIVES = 3;

	/**
	 * The number of structural features of the '<em>Invariant Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_INSTANCE_FEATURE_COUNT = 4;

	/**
	 * The operation id for the '<em>Get Parent</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_INSTANCE___GET_PARENT = 0;

	/**
	 * The number of operations of the '<em>Invariant Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_INSTANCE_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.IRMComponentInstanceImpl <em>IRM Component Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMComponentInstanceImpl
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getIRMComponentInstance()
	 * @generated
	 */
	int IRM_COMPONENT_INSTANCE = 2;

	/**
	 * The feature id for the '<em><b>IR Mcomponent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_COMPONENT_INSTANCE__IR_MCOMPONENT = 0;

	/**
	 * The feature id for the '<em><b>Architecture Instance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_COMPONENT_INSTANCE__ARCHITECTURE_INSTANCE = 1;

	/**
	 * The feature id for the '<em><b>Knowledge Snapshot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_COMPONENT_INSTANCE__KNOWLEDGE_SNAPSHOT = 2;

	/**
	 * The number of structural features of the '<em>IRM Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_COMPONENT_INSTANCE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>IRM Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IRM_COMPONENT_INSTANCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.AlternativeImpl <em>Alternative</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.AlternativeImpl
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getAlternative()
	 * @generated
	 */
	int ALTERNATIVE = 3;

	/**
	 * The feature id for the '<em><b>Children</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALTERNATIVE__CHILDREN = 0;

	/**
	 * The number of structural features of the '<em>Alternative</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALTERNATIVE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Alternative</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALTERNATIVE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.PresentInvariantInstanceImpl <em>Present Invariant Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.PresentInvariantInstanceImpl
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getPresentInvariantInstance()
	 * @generated
	 */
	int PRESENT_INVARIANT_INSTANCE = 4;

	/**
	 * The feature id for the '<em><b>Diagram Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRESENT_INVARIANT_INSTANCE__DIAGRAM_INSTANCE = INVARIANT_INSTANCE__DIAGRAM_INSTANCE;

	/**
	 * The feature id for the '<em><b>Satisfied</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRESENT_INVARIANT_INSTANCE__SATISFIED = INVARIANT_INSTANCE__SATISFIED;

	/**
	 * The feature id for the '<em><b>Selected</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRESENT_INVARIANT_INSTANCE__SELECTED = INVARIANT_INSTANCE__SELECTED;

	/**
	 * The feature id for the '<em><b>Alternatives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRESENT_INVARIANT_INSTANCE__ALTERNATIVES = INVARIANT_INSTANCE__ALTERNATIVES;

	/**
	 * The feature id for the '<em><b>Invariant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRESENT_INVARIANT_INSTANCE__INVARIANT = INVARIANT_INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Present Invariant Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRESENT_INVARIANT_INSTANCE_FEATURE_COUNT = INVARIANT_INSTANCE_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Parent</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRESENT_INVARIANT_INSTANCE___GET_PARENT = INVARIANT_INSTANCE___GET_PARENT;

	/**
	 * The number of operations of the '<em>Present Invariant Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRESENT_INVARIANT_INSTANCE_OPERATION_COUNT = INVARIANT_INSTANCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.ShadowInvariantInstanceImpl <em>Shadow Invariant Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.ShadowInvariantInstanceImpl
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getShadowInvariantInstance()
	 * @generated
	 */
	int SHADOW_INVARIANT_INSTANCE = 5;

	/**
	 * The feature id for the '<em><b>Diagram Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHADOW_INVARIANT_INSTANCE__DIAGRAM_INSTANCE = INVARIANT_INSTANCE__DIAGRAM_INSTANCE;

	/**
	 * The feature id for the '<em><b>Satisfied</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHADOW_INVARIANT_INSTANCE__SATISFIED = INVARIANT_INSTANCE__SATISFIED;

	/**
	 * The feature id for the '<em><b>Selected</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHADOW_INVARIANT_INSTANCE__SELECTED = INVARIANT_INSTANCE__SELECTED;

	/**
	 * The feature id for the '<em><b>Alternatives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHADOW_INVARIANT_INSTANCE__ALTERNATIVES = INVARIANT_INSTANCE__ALTERNATIVES;

	/**
	 * The number of structural features of the '<em>Shadow Invariant Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHADOW_INVARIANT_INSTANCE_FEATURE_COUNT = INVARIANT_INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Parent</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHADOW_INVARIANT_INSTANCE___GET_PARENT = INVARIANT_INSTANCE___GET_PARENT;

	/**
	 * The number of operations of the '<em>Shadow Invariant Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHADOW_INVARIANT_INSTANCE_OPERATION_COUNT = INVARIANT_INSTANCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.ProcessInvariantInstanceImpl <em>Process Invariant Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.ProcessInvariantInstanceImpl
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getProcessInvariantInstance()
	 * @generated
	 */
	int PROCESS_INVARIANT_INSTANCE = 6;

	/**
	 * The feature id for the '<em><b>Diagram Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_INVARIANT_INSTANCE__DIAGRAM_INSTANCE = PRESENT_INVARIANT_INSTANCE__DIAGRAM_INSTANCE;

	/**
	 * The feature id for the '<em><b>Satisfied</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_INVARIANT_INSTANCE__SATISFIED = PRESENT_INVARIANT_INSTANCE__SATISFIED;

	/**
	 * The feature id for the '<em><b>Selected</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_INVARIANT_INSTANCE__SELECTED = PRESENT_INVARIANT_INSTANCE__SELECTED;

	/**
	 * The feature id for the '<em><b>Alternatives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_INVARIANT_INSTANCE__ALTERNATIVES = PRESENT_INVARIANT_INSTANCE__ALTERNATIVES;

	/**
	 * The feature id for the '<em><b>Invariant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_INVARIANT_INSTANCE__INVARIANT = PRESENT_INVARIANT_INSTANCE__INVARIANT;

	/**
	 * The feature id for the '<em><b>Component Process</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_INVARIANT_INSTANCE__COMPONENT_PROCESS = PRESENT_INVARIANT_INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Process Invariant Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_INVARIANT_INSTANCE_FEATURE_COUNT = PRESENT_INVARIANT_INSTANCE_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Parent</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_INVARIANT_INSTANCE___GET_PARENT = PRESENT_INVARIANT_INSTANCE___GET_PARENT;

	/**
	 * The number of operations of the '<em>Process Invariant Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_INVARIANT_INSTANCE_OPERATION_COUNT = PRESENT_INVARIANT_INSTANCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.ExchangeInvariantInstanceImpl <em>Exchange Invariant Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.ExchangeInvariantInstanceImpl
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getExchangeInvariantInstance()
	 * @generated
	 */
	int EXCHANGE_INVARIANT_INSTANCE = 7;

	/**
	 * The feature id for the '<em><b>Diagram Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE_INVARIANT_INSTANCE__DIAGRAM_INSTANCE = PRESENT_INVARIANT_INSTANCE__DIAGRAM_INSTANCE;

	/**
	 * The feature id for the '<em><b>Satisfied</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE_INVARIANT_INSTANCE__SATISFIED = PRESENT_INVARIANT_INSTANCE__SATISFIED;

	/**
	 * The feature id for the '<em><b>Selected</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE_INVARIANT_INSTANCE__SELECTED = PRESENT_INVARIANT_INSTANCE__SELECTED;

	/**
	 * The feature id for the '<em><b>Alternatives</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE_INVARIANT_INSTANCE__ALTERNATIVES = PRESENT_INVARIANT_INSTANCE__ALTERNATIVES;

	/**
	 * The feature id for the '<em><b>Invariant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE_INVARIANT_INSTANCE__INVARIANT = PRESENT_INVARIANT_INSTANCE__INVARIANT;

	/**
	 * The feature id for the '<em><b>Ensemble Definition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE_INVARIANT_INSTANCE__ENSEMBLE_DEFINITION = PRESENT_INVARIANT_INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Exchange Invariant Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE_INVARIANT_INSTANCE_FEATURE_COUNT = PRESENT_INVARIANT_INSTANCE_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Parent</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE_INVARIANT_INSTANCE___GET_PARENT = PRESENT_INVARIANT_INSTANCE___GET_PARENT;

	/**
	 * The number of operations of the '<em>Exchange Invariant Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE_INVARIANT_INSTANCE_OPERATION_COUNT = PRESENT_INVARIANT_INSTANCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '<em>Invariant Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.design.Invariant
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getInvariantType()
	 * @generated
	 */
	int INVARIANT_TYPE = 8;

	/**
	 * The meta object id for the '<em>IRM Component Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.design.Component
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getIRMComponentType()
	 * @generated
	 */
	int IRM_COMPONENT_TYPE = 9;

	/**
	 * The meta object id for the '<em>Component Instance Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getComponentInstanceType()
	 * @generated
	 */
	int COMPONENT_INSTANCE_TYPE = 10;

	/**
	 * The meta object id for the '<em>Value Set Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ValueSet
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getValueSetType()
	 * @generated
	 */
	int VALUE_SET_TYPE = 11;

	/**
	 * The meta object id for the '<em>IRM Design Model Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.design.IRM
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getIRMDesignModelType()
	 * @generated
	 */
	int IRM_DESIGN_MODEL_TYPE = 12;

	/**
	 * The meta object id for the '<em>Trace Model Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.TraceModel
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getTraceModelType()
	 * @generated
	 */
	int TRACE_MODEL_TYPE = 13;

	/**
	 * The meta object id for the '<em>Component Process Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getComponentProcessType()
	 * @generated
	 */
	int COMPONENT_PROCESS_TYPE = 14;

	/**
	 * The meta object id for the '<em>Ensemble Definition Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition
	 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getEnsembleDefinitionType()
	 * @generated
	 */
	int ENSEMBLE_DEFINITION_TYPE = 15;


	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance <em>IRM Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IRM Instance</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance
	 * @generated
	 */
	EClass getIRMInstance();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getInvariantInstances <em>Invariant Instances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Invariant Instances</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getInvariantInstances()
	 * @see #getIRMInstance()
	 * @generated
	 */
	EReference getIRMInstance_InvariantInstances();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getIRMcomponentInstances <em>IR Mcomponent Instances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>IR Mcomponent Instances</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getIRMcomponentInstances()
	 * @see #getIRMInstance()
	 * @generated
	 */
	EReference getIRMInstance_IRMcomponentInstances();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getDesignModel <em>Design Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Design Model</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getDesignModel()
	 * @see #getIRMInstance()
	 * @generated
	 */
	EAttribute getIRMInstance_DesignModel();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getTraceModel <em>Trace Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Trace Model</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getTraceModel()
	 * @see #getIRMInstance()
	 * @generated
	 */
	EAttribute getIRMInstance_TraceModel();

	/**
	 * Returns the meta object for the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getRoots() <em>Get Roots</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Roots</em>' operation.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance#getRoots()
	 * @generated
	 */
	EOperation getIRMInstance__GetRoots();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance <em>Invariant Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Invariant Instance</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance
	 * @generated
	 */
	EClass getInvariantInstance();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getDiagramInstance <em>Diagram Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Diagram Instance</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getDiagramInstance()
	 * @see #getInvariantInstance()
	 * @generated
	 */
	EReference getInvariantInstance_DiagramInstance();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#isSatisfied <em>Satisfied</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Satisfied</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#isSatisfied()
	 * @see #getInvariantInstance()
	 * @generated
	 */
	EAttribute getInvariantInstance_Satisfied();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#isSelected <em>Selected</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Selected</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#isSelected()
	 * @see #getInvariantInstance()
	 * @generated
	 */
	EAttribute getInvariantInstance_Selected();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getAlternatives <em>Alternatives</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Alternatives</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getAlternatives()
	 * @see #getInvariantInstance()
	 * @generated
	 */
	EReference getInvariantInstance_Alternatives();

	/**
	 * Returns the meta object for the '{@link cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getParent() <em>Get Parent</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Parent</em>' operation.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance#getParent()
	 * @generated
	 */
	EOperation getInvariantInstance__GetParent();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance <em>IRM Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IRM Component Instance</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance
	 * @generated
	 */
	EClass getIRMComponentInstance();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getIRMcomponent <em>IR Mcomponent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>IR Mcomponent</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getIRMcomponent()
	 * @see #getIRMComponentInstance()
	 * @generated
	 */
	EAttribute getIRMComponentInstance_IRMcomponent();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getArchitectureInstance <em>Architecture Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Architecture Instance</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getArchitectureInstance()
	 * @see #getIRMComponentInstance()
	 * @generated
	 */
	EAttribute getIRMComponentInstance_ArchitectureInstance();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getKnowledgeSnapshot <em>Knowledge Snapshot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Knowledge Snapshot</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance#getKnowledgeSnapshot()
	 * @see #getIRMComponentInstance()
	 * @generated
	 */
	EAttribute getIRMComponentInstance_KnowledgeSnapshot();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.Alternative <em>Alternative</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Alternative</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.Alternative
	 * @generated
	 */
	EClass getAlternative();

	/**
	 * Returns the meta object for the reference list '{@link cz.cuni.mff.d3s.irm.model.runtime.api.Alternative#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Children</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.Alternative#getChildren()
	 * @see #getAlternative()
	 * @generated
	 */
	EReference getAlternative_Children();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance <em>Present Invariant Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Present Invariant Instance</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance
	 * @generated
	 */
	EClass getPresentInvariantInstance();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance#getInvariant <em>Invariant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Invariant</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance#getInvariant()
	 * @see #getPresentInvariantInstance()
	 * @generated
	 */
	EAttribute getPresentInvariantInstance_Invariant();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.ShadowInvariantInstance <em>Shadow Invariant Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Shadow Invariant Instance</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.ShadowInvariantInstance
	 * @generated
	 */
	EClass getShadowInvariantInstance();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance <em>Process Invariant Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Process Invariant Instance</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance
	 * @generated
	 */
	EClass getProcessInvariantInstance();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance#getComponentProcess <em>Component Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Component Process</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance#getComponentProcess()
	 * @see #getProcessInvariantInstance()
	 * @generated
	 */
	EAttribute getProcessInvariantInstance_ComponentProcess();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance <em>Exchange Invariant Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exchange Invariant Instance</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance
	 * @generated
	 */
	EClass getExchangeInvariantInstance();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance#getEnsembleDefinition <em>Ensemble Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ensemble Definition</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance#getEnsembleDefinition()
	 * @see #getExchangeInvariantInstance()
	 * @generated
	 */
	EAttribute getExchangeInvariantInstance_EnsembleDefinition();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.irm.model.design.Invariant <em>Invariant Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Invariant Type</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.design.Invariant
	 * @model instanceClass="cz.cuni.mff.d3s.irm.model.design.Invariant"
	 * @generated
	 */
	EDataType getInvariantType();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.irm.model.design.Component <em>IRM Component Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IRM Component Type</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.design.Component
	 * @model instanceClass="cz.cuni.mff.d3s.irm.model.design.Component"
	 * @generated
	 */
	EDataType getIRMComponentType();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance <em>Component Instance Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Component Instance Type</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance"
	 * @generated
	 */
	EDataType getComponentInstanceType();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.knowledge.ValueSet <em>Value Set Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Value Set Type</em>'.
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ValueSet
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.knowledge.ValueSet"
	 * @generated
	 */
	EDataType getValueSetType();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.irm.model.design.IRM <em>IRM Design Model Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IRM Design Model Type</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.design.IRM
	 * @model instanceClass="cz.cuni.mff.d3s.irm.model.design.IRM"
	 * @generated
	 */
	EDataType getIRMDesignModelType();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel <em>Trace Model Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Trace Model Type</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.TraceModel
	 * @model instanceClass="cz.cuni.mff.d3s.irm.model.trace.api.TraceModel"
	 * @generated
	 */
	EDataType getTraceModelType();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess <em>Component Process Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Component Process Type</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess"
	 * @generated
	 */
	EDataType getComponentProcessType();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition <em>Ensemble Definition Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Ensemble Definition Type</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition"
	 * @generated
	 */
	EDataType getEnsembleDefinitionType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	IRMRuntimeFactory getIRMRuntimeFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.IRMInstanceImpl <em>IRM Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMInstanceImpl
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getIRMInstance()
		 * @generated
		 */
		EClass IRM_INSTANCE = eINSTANCE.getIRMInstance();

		/**
		 * The meta object literal for the '<em><b>Invariant Instances</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IRM_INSTANCE__INVARIANT_INSTANCES = eINSTANCE.getIRMInstance_InvariantInstances();

		/**
		 * The meta object literal for the '<em><b>IR Mcomponent Instances</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IRM_INSTANCE__IR_MCOMPONENT_INSTANCES = eINSTANCE.getIRMInstance_IRMcomponentInstances();

		/**
		 * The meta object literal for the '<em><b>Design Model</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IRM_INSTANCE__DESIGN_MODEL = eINSTANCE.getIRMInstance_DesignModel();

		/**
		 * The meta object literal for the '<em><b>Trace Model</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IRM_INSTANCE__TRACE_MODEL = eINSTANCE.getIRMInstance_TraceModel();

		/**
		 * The meta object literal for the '<em><b>Get Roots</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation IRM_INSTANCE___GET_ROOTS = eINSTANCE.getIRMInstance__GetRoots();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.InvariantInstanceImpl <em>Invariant Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.InvariantInstanceImpl
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getInvariantInstance()
		 * @generated
		 */
		EClass INVARIANT_INSTANCE = eINSTANCE.getInvariantInstance();

		/**
		 * The meta object literal for the '<em><b>Diagram Instance</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INVARIANT_INSTANCE__DIAGRAM_INSTANCE = eINSTANCE.getInvariantInstance_DiagramInstance();

		/**
		 * The meta object literal for the '<em><b>Satisfied</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVARIANT_INSTANCE__SATISFIED = eINSTANCE.getInvariantInstance_Satisfied();

		/**
		 * The meta object literal for the '<em><b>Selected</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVARIANT_INSTANCE__SELECTED = eINSTANCE.getInvariantInstance_Selected();

		/**
		 * The meta object literal for the '<em><b>Alternatives</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INVARIANT_INSTANCE__ALTERNATIVES = eINSTANCE.getInvariantInstance_Alternatives();

		/**
		 * The meta object literal for the '<em><b>Get Parent</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation INVARIANT_INSTANCE___GET_PARENT = eINSTANCE.getInvariantInstance__GetParent();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.IRMComponentInstanceImpl <em>IRM Component Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMComponentInstanceImpl
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getIRMComponentInstance()
		 * @generated
		 */
		EClass IRM_COMPONENT_INSTANCE = eINSTANCE.getIRMComponentInstance();

		/**
		 * The meta object literal for the '<em><b>IR Mcomponent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IRM_COMPONENT_INSTANCE__IR_MCOMPONENT = eINSTANCE.getIRMComponentInstance_IRMcomponent();

		/**
		 * The meta object literal for the '<em><b>Architecture Instance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IRM_COMPONENT_INSTANCE__ARCHITECTURE_INSTANCE = eINSTANCE.getIRMComponentInstance_ArchitectureInstance();

		/**
		 * The meta object literal for the '<em><b>Knowledge Snapshot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IRM_COMPONENT_INSTANCE__KNOWLEDGE_SNAPSHOT = eINSTANCE.getIRMComponentInstance_KnowledgeSnapshot();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.AlternativeImpl <em>Alternative</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.AlternativeImpl
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getAlternative()
		 * @generated
		 */
		EClass ALTERNATIVE = eINSTANCE.getAlternative();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ALTERNATIVE__CHILDREN = eINSTANCE.getAlternative_Children();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.PresentInvariantInstanceImpl <em>Present Invariant Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.PresentInvariantInstanceImpl
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getPresentInvariantInstance()
		 * @generated
		 */
		EClass PRESENT_INVARIANT_INSTANCE = eINSTANCE.getPresentInvariantInstance();

		/**
		 * The meta object literal for the '<em><b>Invariant</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRESENT_INVARIANT_INSTANCE__INVARIANT = eINSTANCE.getPresentInvariantInstance_Invariant();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.ShadowInvariantInstanceImpl <em>Shadow Invariant Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.ShadowInvariantInstanceImpl
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getShadowInvariantInstance()
		 * @generated
		 */
		EClass SHADOW_INVARIANT_INSTANCE = eINSTANCE.getShadowInvariantInstance();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.ProcessInvariantInstanceImpl <em>Process Invariant Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.ProcessInvariantInstanceImpl
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getProcessInvariantInstance()
		 * @generated
		 */
		EClass PROCESS_INVARIANT_INSTANCE = eINSTANCE.getProcessInvariantInstance();

		/**
		 * The meta object literal for the '<em><b>Component Process</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_INVARIANT_INSTANCE__COMPONENT_PROCESS = eINSTANCE.getProcessInvariantInstance_ComponentProcess();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.runtime.impl.ExchangeInvariantInstanceImpl <em>Exchange Invariant Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.ExchangeInvariantInstanceImpl
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getExchangeInvariantInstance()
		 * @generated
		 */
		EClass EXCHANGE_INVARIANT_INSTANCE = eINSTANCE.getExchangeInvariantInstance();

		/**
		 * The meta object literal for the '<em><b>Ensemble Definition</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXCHANGE_INVARIANT_INSTANCE__ENSEMBLE_DEFINITION = eINSTANCE.getExchangeInvariantInstance_EnsembleDefinition();

		/**
		 * The meta object literal for the '<em>Invariant Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.design.Invariant
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getInvariantType()
		 * @generated
		 */
		EDataType INVARIANT_TYPE = eINSTANCE.getInvariantType();

		/**
		 * The meta object literal for the '<em>IRM Component Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.design.Component
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getIRMComponentType()
		 * @generated
		 */
		EDataType IRM_COMPONENT_TYPE = eINSTANCE.getIRMComponentType();

		/**
		 * The meta object literal for the '<em>Component Instance Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getComponentInstanceType()
		 * @generated
		 */
		EDataType COMPONENT_INSTANCE_TYPE = eINSTANCE.getComponentInstanceType();

		/**
		 * The meta object literal for the '<em>Value Set Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.knowledge.ValueSet
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getValueSetType()
		 * @generated
		 */
		EDataType VALUE_SET_TYPE = eINSTANCE.getValueSetType();

		/**
		 * The meta object literal for the '<em>IRM Design Model Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.design.IRM
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getIRMDesignModelType()
		 * @generated
		 */
		EDataType IRM_DESIGN_MODEL_TYPE = eINSTANCE.getIRMDesignModelType();

		/**
		 * The meta object literal for the '<em>Trace Model Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.trace.api.TraceModel
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getTraceModelType()
		 * @generated
		 */
		EDataType TRACE_MODEL_TYPE = eINSTANCE.getTraceModelType();

		/**
		 * The meta object literal for the '<em>Component Process Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getComponentProcessType()
		 * @generated
		 */
		EDataType COMPONENT_PROCESS_TYPE = eINSTANCE.getComponentProcessType();

		/**
		 * The meta object literal for the '<em>Ensemble Definition Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition
		 * @see cz.cuni.mff.d3s.irm.model.runtime.impl.IRMRuntimePackageImpl#getEnsembleDefinitionType()
		 * @generated
		 */
		EDataType ENSEMBLE_DEFINITION_TYPE = eINSTANCE.getEnsembleDefinitionType();

	}

} //IRMRuntimePackage
