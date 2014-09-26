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
package cz.cuni.mff.d3s.irm.model.trace.meta;

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
 * @see cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory
 * @model kind="package"
 * @generated
 */
public interface TracePackage extends EPackage {
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
	String eNAME = "trace";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://cz.cuni.mff.d3s.irm.model.trace/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "cz.cuni.mff.d3s.irm.model.trace";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TracePackage eINSTANCE = cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl.init();

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.TraceModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TraceModelImpl
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getTraceModel()
	 * @generated
	 */
	int TRACE_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Component Traces</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL__COMPONENT_TRACES = 0;

	/**
	 * The feature id for the '<em><b>Process Traces</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL__PROCESS_TRACES = 1;

	/**
	 * The feature id for the '<em><b>Ensemble Definition Traces</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL__ENSEMBLE_DEFINITION_TRACES = 2;

	/**
	 * The feature id for the '<em><b>Invariant Monitors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL__INVARIANT_MONITORS = 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL_FEATURE_COUNT = 4;

	/**
	 * The operation id for the '<em>Get IRM Component For Architecture Component Instance</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL___GET_IRM_COMPONENT_FOR_ARCHITECTURE_COMPONENT_INSTANCE__COMPONENTINSTANCE_IRM = 0;

	/**
	 * The operation id for the '<em>Get Component Process From Component Instance And Invariant</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL___GET_COMPONENT_PROCESS_FROM_COMPONENT_INSTANCE_AND_INVARIANT__COMPONENTINSTANCE_INVARIANT = 1;

	/**
	 * The operation id for the '<em>Get Ensemble Definition From Invariant</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL___GET_ENSEMBLE_DEFINITION_FROM_INVARIANT__INVARIANT = 2;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_MODEL_OPERATION_COUNT = 3;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.ComponentTraceImpl <em>Component Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.ComponentTraceImpl
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getComponentTrace()
	 * @generated
	 */
	int COMPONENT_TRACE = 1;

	/**
	 * The feature id for the '<em><b>From</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TRACE__FROM = 0;

	/**
	 * The feature id for the '<em><b>To</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TRACE__TO = 1;

	/**
	 * The number of structural features of the '<em>Component Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TRACE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Component Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TRACE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.ProcessTraceImpl <em>Process Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.ProcessTraceImpl
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getProcessTrace()
	 * @generated
	 */
	int PROCESS_TRACE = 2;

	/**
	 * The feature id for the '<em><b>From</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_TRACE__FROM = 0;

	/**
	 * The feature id for the '<em><b>To</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_TRACE__TO = 1;

	/**
	 * The number of structural features of the '<em>Process Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_TRACE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Process Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_TRACE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.EnsembleDefinitionTraceImpl <em>Ensemble Definition Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.EnsembleDefinitionTraceImpl
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getEnsembleDefinitionTrace()
	 * @generated
	 */
	int ENSEMBLE_DEFINITION_TRACE = 3;

	/**
	 * The feature id for the '<em><b>To</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION_TRACE__TO = 0;

	/**
	 * The feature id for the '<em><b>From</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION_TRACE__FROM = 1;

	/**
	 * The number of structural features of the '<em>Ensemble Definition Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION_TRACE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Ensemble Definition Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION_TRACE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.InvariantMonitorImpl <em>Invariant Monitor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.InvariantMonitorImpl
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getInvariantMonitor()
	 * @generated
	 */
	int INVARIANT_MONITOR = 4;

	/**
	 * The feature id for the '<em><b>Invariant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_MONITOR__INVARIANT = 0;

	/**
	 * The feature id for the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_MONITOR__METHOD = 1;

	/**
	 * The feature id for the '<em><b>Monitor Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_MONITOR__MONITOR_PARAMETERS = 2;

	/**
	 * The number of structural features of the '<em>Invariant Monitor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_MONITOR_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Invariant Monitor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVARIANT_MONITOR_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.MonitorParameterImpl <em>Monitor Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.MonitorParameterImpl
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getMonitorParameter()
	 * @generated
	 */
	int MONITOR_PARAMETER = 5;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONITOR_PARAMETER__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Knowledge Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONITOR_PARAMETER__KNOWLEDGE_PATH = 1;

	/**
	 * The number of structural features of the '<em>Monitor Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONITOR_PARAMETER_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Monitor Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONITOR_PARAMETER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>Component Instance Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getComponentInstanceType()
	 * @generated
	 */
	int COMPONENT_INSTANCE_TYPE = 6;

	/**
	 * The meta object id for the '<em>Component Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.design.Component
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getComponentType()
	 * @generated
	 */
	int COMPONENT_TYPE = 7;

	/**
	 * The meta object id for the '<em>Process Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getProcessType()
	 * @generated
	 */
	int PROCESS_TYPE = 8;

	/**
	 * The meta object id for the '<em>Ensemble Definition Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getEnsembleDefinitionType()
	 * @generated
	 */
	int ENSEMBLE_DEFINITION_TYPE = 9;

	/**
	 * The meta object id for the '<em>Invariant Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.design.Invariant
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getInvariantType()
	 * @generated
	 */
	int INVARIANT_TYPE = 10;

	/**
	 * The meta object id for the '<em>Architecture Component Instance Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getArchitectureComponentInstanceType()
	 * @generated
	 */
	int ARCHITECTURE_COMPONENT_INSTANCE_TYPE = 11;

	/**
	 * The meta object id for the '<em>Design Model Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.irm.model.design.IRM
	 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getDesignModelType()
	 * @generated
	 */
	int DESIGN_MODEL_TYPE = 12;


	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.TraceModel
	 * @generated
	 */
	EClass getTraceModel();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getComponentTraces <em>Component Traces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Component Traces</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getComponentTraces()
	 * @see #getTraceModel()
	 * @generated
	 */
	EReference getTraceModel_ComponentTraces();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getProcessTraces <em>Process Traces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Process Traces</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getProcessTraces()
	 * @see #getTraceModel()
	 * @generated
	 */
	EReference getTraceModel_ProcessTraces();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getEnsembleDefinitionTraces <em>Ensemble Definition Traces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Ensemble Definition Traces</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getEnsembleDefinitionTraces()
	 * @see #getTraceModel()
	 * @generated
	 */
	EReference getTraceModel_EnsembleDefinitionTraces();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getInvariantMonitors <em>Invariant Monitors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Invariant Monitors</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getInvariantMonitors()
	 * @see #getTraceModel()
	 * @generated
	 */
	EReference getTraceModel_InvariantMonitors();

	/**
	 * Returns the meta object for the '{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getIRMComponentForArchitectureComponentInstance(cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance, cz.cuni.mff.d3s.irm.model.design.IRM) <em>Get IRM Component For Architecture Component Instance</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get IRM Component For Architecture Component Instance</em>' operation.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getIRMComponentForArchitectureComponentInstance(cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance, cz.cuni.mff.d3s.irm.model.design.IRM)
	 * @generated
	 */
	EOperation getTraceModel__GetIRMComponentForArchitectureComponentInstance__ComponentInstance_IRM();

	/**
	 * Returns the meta object for the '{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getComponentProcessFromComponentInstanceAndInvariant(cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance, cz.cuni.mff.d3s.irm.model.design.Invariant) <em>Get Component Process From Component Instance And Invariant</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Component Process From Component Instance And Invariant</em>' operation.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getComponentProcessFromComponentInstanceAndInvariant(cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance, cz.cuni.mff.d3s.irm.model.design.Invariant)
	 * @generated
	 */
	EOperation getTraceModel__GetComponentProcessFromComponentInstanceAndInvariant__ComponentInstance_Invariant();

	/**
	 * Returns the meta object for the '{@link cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getEnsembleDefinitionFromInvariant(cz.cuni.mff.d3s.irm.model.design.Invariant) <em>Get Ensemble Definition From Invariant</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Ensemble Definition From Invariant</em>' operation.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.TraceModel#getEnsembleDefinitionFromInvariant(cz.cuni.mff.d3s.irm.model.design.Invariant)
	 * @generated
	 */
	EOperation getTraceModel__GetEnsembleDefinitionFromInvariant__Invariant();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace <em>Component Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component Trace</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace
	 * @generated
	 */
	EClass getComponentTrace();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>From</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace#getFrom()
	 * @see #getComponentTrace()
	 * @generated
	 */
	EAttribute getComponentTrace_From();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>To</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace#getTo()
	 * @see #getComponentTrace()
	 * @generated
	 */
	EAttribute getComponentTrace_To();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace <em>Process Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Process Trace</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace
	 * @generated
	 */
	EClass getProcessTrace();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>From</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace#getFrom()
	 * @see #getProcessTrace()
	 * @generated
	 */
	EAttribute getProcessTrace_From();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>To</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace#getTo()
	 * @see #getProcessTrace()
	 * @generated
	 */
	EAttribute getProcessTrace_To();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace <em>Ensemble Definition Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ensemble Definition Trace</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace
	 * @generated
	 */
	EClass getEnsembleDefinitionTrace();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>To</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace#getTo()
	 * @see #getEnsembleDefinitionTrace()
	 * @generated
	 */
	EAttribute getEnsembleDefinitionTrace_To();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>From</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace#getFrom()
	 * @see #getEnsembleDefinitionTrace()
	 * @generated
	 */
	EAttribute getEnsembleDefinitionTrace_From();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor <em>Invariant Monitor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Invariant Monitor</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor
	 * @generated
	 */
	EClass getInvariantMonitor();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor#getInvariant <em>Invariant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Invariant</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor#getInvariant()
	 * @see #getInvariantMonitor()
	 * @generated
	 */
	EAttribute getInvariantMonitor_Invariant();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor#getMethod <em>Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Method</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor#getMethod()
	 * @see #getInvariantMonitor()
	 * @generated
	 */
	EAttribute getInvariantMonitor_Method();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor#getMonitorParameters <em>Monitor Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Monitor Parameters</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor#getMonitorParameters()
	 * @see #getInvariantMonitor()
	 * @generated
	 */
	EReference getInvariantMonitor_MonitorParameters();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter <em>Monitor Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Monitor Parameter</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter
	 * @generated
	 */
	EClass getMonitorParameter();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter#getType()
	 * @see #getMonitorParameter()
	 * @generated
	 */
	EAttribute getMonitorParameter_Type();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter#getKnowledgePath <em>Knowledge Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Knowledge Path</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter#getKnowledgePath()
	 * @see #getMonitorParameter()
	 * @generated
	 */
	EReference getMonitorParameter_KnowledgePath();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance <em>Component Instance Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Component Instance Type</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance"
	 * @generated
	 */
	EDataType getComponentInstanceType();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.irm.model.design.Component <em>Component Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Component Type</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.design.Component
	 * @model instanceClass="cz.cuni.mff.d3s.irm.model.design.Component"
	 * @generated
	 */
	EDataType getComponentType();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess <em>Process Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Process Type</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess"
	 * @generated
	 */
	EDataType getProcessType();

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
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance <em>Architecture Component Instance Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Architecture Component Instance Type</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance"
	 * @generated
	 */
	EDataType getArchitectureComponentInstanceType();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.irm.model.design.IRM <em>Design Model Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Design Model Type</em>'.
	 * @see cz.cuni.mff.d3s.irm.model.design.IRM
	 * @model instanceClass="cz.cuni.mff.d3s.irm.model.design.IRM"
	 * @generated
	 */
	EDataType getDesignModelType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TraceFactory getTraceFactory();

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
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.TraceModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TraceModelImpl
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getTraceModel()
		 * @generated
		 */
		EClass TRACE_MODEL = eINSTANCE.getTraceModel();

		/**
		 * The meta object literal for the '<em><b>Component Traces</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_MODEL__COMPONENT_TRACES = eINSTANCE.getTraceModel_ComponentTraces();

		/**
		 * The meta object literal for the '<em><b>Process Traces</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_MODEL__PROCESS_TRACES = eINSTANCE.getTraceModel_ProcessTraces();

		/**
		 * The meta object literal for the '<em><b>Ensemble Definition Traces</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_MODEL__ENSEMBLE_DEFINITION_TRACES = eINSTANCE.getTraceModel_EnsembleDefinitionTraces();

		/**
		 * The meta object literal for the '<em><b>Invariant Monitors</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_MODEL__INVARIANT_MONITORS = eINSTANCE.getTraceModel_InvariantMonitors();

		/**
		 * The meta object literal for the '<em><b>Get IRM Component For Architecture Component Instance</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRACE_MODEL___GET_IRM_COMPONENT_FOR_ARCHITECTURE_COMPONENT_INSTANCE__COMPONENTINSTANCE_IRM = eINSTANCE.getTraceModel__GetIRMComponentForArchitectureComponentInstance__ComponentInstance_IRM();

		/**
		 * The meta object literal for the '<em><b>Get Component Process From Component Instance And Invariant</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRACE_MODEL___GET_COMPONENT_PROCESS_FROM_COMPONENT_INSTANCE_AND_INVARIANT__COMPONENTINSTANCE_INVARIANT = eINSTANCE.getTraceModel__GetComponentProcessFromComponentInstanceAndInvariant__ComponentInstance_Invariant();

		/**
		 * The meta object literal for the '<em><b>Get Ensemble Definition From Invariant</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRACE_MODEL___GET_ENSEMBLE_DEFINITION_FROM_INVARIANT__INVARIANT = eINSTANCE.getTraceModel__GetEnsembleDefinitionFromInvariant__Invariant();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.ComponentTraceImpl <em>Component Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.ComponentTraceImpl
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getComponentTrace()
		 * @generated
		 */
		EClass COMPONENT_TRACE = eINSTANCE.getComponentTrace();

		/**
		 * The meta object literal for the '<em><b>From</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_TRACE__FROM = eINSTANCE.getComponentTrace_From();

		/**
		 * The meta object literal for the '<em><b>To</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_TRACE__TO = eINSTANCE.getComponentTrace_To();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.ProcessTraceImpl <em>Process Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.ProcessTraceImpl
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getProcessTrace()
		 * @generated
		 */
		EClass PROCESS_TRACE = eINSTANCE.getProcessTrace();

		/**
		 * The meta object literal for the '<em><b>From</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_TRACE__FROM = eINSTANCE.getProcessTrace_From();

		/**
		 * The meta object literal for the '<em><b>To</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_TRACE__TO = eINSTANCE.getProcessTrace_To();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.EnsembleDefinitionTraceImpl <em>Ensemble Definition Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.EnsembleDefinitionTraceImpl
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getEnsembleDefinitionTrace()
		 * @generated
		 */
		EClass ENSEMBLE_DEFINITION_TRACE = eINSTANCE.getEnsembleDefinitionTrace();

		/**
		 * The meta object literal for the '<em><b>To</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENSEMBLE_DEFINITION_TRACE__TO = eINSTANCE.getEnsembleDefinitionTrace_To();

		/**
		 * The meta object literal for the '<em><b>From</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENSEMBLE_DEFINITION_TRACE__FROM = eINSTANCE.getEnsembleDefinitionTrace_From();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.InvariantMonitorImpl <em>Invariant Monitor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.InvariantMonitorImpl
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getInvariantMonitor()
		 * @generated
		 */
		EClass INVARIANT_MONITOR = eINSTANCE.getInvariantMonitor();

		/**
		 * The meta object literal for the '<em><b>Invariant</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVARIANT_MONITOR__INVARIANT = eINSTANCE.getInvariantMonitor_Invariant();

		/**
		 * The meta object literal for the '<em><b>Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVARIANT_MONITOR__METHOD = eINSTANCE.getInvariantMonitor_Method();

		/**
		 * The meta object literal for the '<em><b>Monitor Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INVARIANT_MONITOR__MONITOR_PARAMETERS = eINSTANCE.getInvariantMonitor_MonitorParameters();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.irm.model.trace.impl.MonitorParameterImpl <em>Monitor Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.MonitorParameterImpl
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getMonitorParameter()
		 * @generated
		 */
		EClass MONITOR_PARAMETER = eINSTANCE.getMonitorParameter();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONITOR_PARAMETER__TYPE = eINSTANCE.getMonitorParameter_Type();

		/**
		 * The meta object literal for the '<em><b>Knowledge Path</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MONITOR_PARAMETER__KNOWLEDGE_PATH = eINSTANCE.getMonitorParameter_KnowledgePath();

		/**
		 * The meta object literal for the '<em>Component Instance Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getComponentInstanceType()
		 * @generated
		 */
		EDataType COMPONENT_INSTANCE_TYPE = eINSTANCE.getComponentInstanceType();

		/**
		 * The meta object literal for the '<em>Component Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.design.Component
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getComponentType()
		 * @generated
		 */
		EDataType COMPONENT_TYPE = eINSTANCE.getComponentType();

		/**
		 * The meta object literal for the '<em>Process Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getProcessType()
		 * @generated
		 */
		EDataType PROCESS_TYPE = eINSTANCE.getProcessType();

		/**
		 * The meta object literal for the '<em>Ensemble Definition Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getEnsembleDefinitionType()
		 * @generated
		 */
		EDataType ENSEMBLE_DEFINITION_TYPE = eINSTANCE.getEnsembleDefinitionType();

		/**
		 * The meta object literal for the '<em>Invariant Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.design.Invariant
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getInvariantType()
		 * @generated
		 */
		EDataType INVARIANT_TYPE = eINSTANCE.getInvariantType();

		/**
		 * The meta object literal for the '<em>Architecture Component Instance Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getArchitectureComponentInstanceType()
		 * @generated
		 */
		EDataType ARCHITECTURE_COMPONENT_INSTANCE_TYPE = eINSTANCE.getArchitectureComponentInstanceType();

		/**
		 * The meta object literal for the '<em>Design Model Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.irm.model.design.IRM
		 * @see cz.cuni.mff.d3s.irm.model.trace.impl.TracePackageImpl#getDesignModelType()
		 * @generated
		 */
		EDataType DESIGN_MODEL_TYPE = eINSTANCE.getDesignModelType();

	}

} //TracePackage
