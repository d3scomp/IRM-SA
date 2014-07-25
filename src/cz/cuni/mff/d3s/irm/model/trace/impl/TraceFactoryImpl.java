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

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

import cz.cuni.mff.d3s.irm.model.design.Component;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.Invariant;

import cz.cuni.mff.d3s.irm.model.trace.api.*;

import cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory;
import cz.cuni.mff.d3s.irm.model.trace.meta.TracePackage;

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
public class TraceFactoryImpl extends EFactoryImpl implements TraceFactory {
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
	public static TraceFactory init() {
		try {
			TraceFactory theTraceFactory = (TraceFactory)EPackage.Registry.INSTANCE.getEFactory(TracePackage.eNS_URI);
			if (theTraceFactory != null) {
				return theTraceFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TraceFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TraceFactoryImpl() {
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
			case TracePackage.TRACE_MODEL: return createTraceModel();
			case TracePackage.COMPONENT_TRACE: return createComponentTrace();
			case TracePackage.PROCESS_TRACE: return createProcessTrace();
			case TracePackage.ENSEMBLE_DEFINITION_TRACE: return createEnsembleDefinitionTrace();
			case TracePackage.INVARIANT_MONITOR: return createInvariantMonitor();
			case TracePackage.MONITOR_PARAMETER: return createMonitorParameter();
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
			case TracePackage.COMPONENT_INSTANCE_TYPE:
				return createComponentInstanceTypeFromString(eDataType, initialValue);
			case TracePackage.COMPONENT_TYPE:
				return createComponentTypeFromString(eDataType, initialValue);
			case TracePackage.PROCESS_TYPE:
				return createProcessTypeFromString(eDataType, initialValue);
			case TracePackage.ENSEMBLE_DEFINITION_TYPE:
				return createEnsembleDefinitionTypeFromString(eDataType, initialValue);
			case TracePackage.INVARIANT_TYPE:
				return createInvariantTypeFromString(eDataType, initialValue);
			case TracePackage.ARCHITECTURE_COMPONENT_INSTANCE_TYPE:
				return createArchitectureComponentInstanceTypeFromString(eDataType, initialValue);
			case TracePackage.DESIGN_MODEL_TYPE:
				return createDesignModelTypeFromString(eDataType, initialValue);
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
			case TracePackage.COMPONENT_INSTANCE_TYPE:
				return convertComponentInstanceTypeToString(eDataType, instanceValue);
			case TracePackage.COMPONENT_TYPE:
				return convertComponentTypeToString(eDataType, instanceValue);
			case TracePackage.PROCESS_TYPE:
				return convertProcessTypeToString(eDataType, instanceValue);
			case TracePackage.ENSEMBLE_DEFINITION_TYPE:
				return convertEnsembleDefinitionTypeToString(eDataType, instanceValue);
			case TracePackage.INVARIANT_TYPE:
				return convertInvariantTypeToString(eDataType, instanceValue);
			case TracePackage.ARCHITECTURE_COMPONENT_INSTANCE_TYPE:
				return convertArchitectureComponentInstanceTypeToString(eDataType, instanceValue);
			case TracePackage.DESIGN_MODEL_TYPE:
				return convertDesignModelTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TraceModel createTraceModel() {
		TraceModelImpl traceModel = new TraceModelImpl();
		return traceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentTrace createComponentTrace() {
		ComponentTraceImpl componentTrace = new ComponentTraceImpl();
		return componentTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcessTrace createProcessTrace() {
		ProcessTraceImpl processTrace = new ProcessTraceImpl();
		return processTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnsembleDefinitionTrace createEnsembleDefinitionTrace() {
		EnsembleDefinitionTraceImpl ensembleDefinitionTrace = new EnsembleDefinitionTraceImpl();
		return ensembleDefinitionTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InvariantMonitor createInvariantMonitor() {
		InvariantMonitorImpl invariantMonitor = new InvariantMonitorImpl();
		return invariantMonitor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MonitorParameter createMonitorParameter() {
		MonitorParameterImpl monitorParameter = new MonitorParameterImpl();
		return monitorParameter;
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
	public Component createComponentTypeFromString(EDataType eDataType, String initialValue) {
		return (Component)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertComponentTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess createProcessTypeFromString(EDataType eDataType, String initialValue) {
		return (ComponentProcess)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertProcessTypeToString(EDataType eDataType, Object instanceValue) {
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
	public cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance createArchitectureComponentInstanceTypeFromString(EDataType eDataType, String initialValue) {
		return (cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertArchitectureComponentInstanceTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IRM createDesignModelTypeFromString(EDataType eDataType, String initialValue) {
		return (IRM)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDesignModelTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TracePackage getTracePackage() {
		return (TracePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TracePackage getPackage() {
		return TracePackage.eINSTANCE;
	}

} //TraceFactoryImpl
