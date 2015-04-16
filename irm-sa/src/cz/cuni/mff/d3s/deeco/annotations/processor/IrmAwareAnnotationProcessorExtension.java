/*******************************************************************************
 * Copyright 2014 Charles University in Prague
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import cz.cuni.mff.d3s.deeco.annotations.AssumptionParameter;
import cz.cuni.mff.d3s.deeco.annotations.IRMComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Invariant;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitor;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;
import cz.cuni.mff.d3s.irm.model.design.Component;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.EnsembleDefinitionTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.MonitorParameter;
import cz.cuni.mff.d3s.irm.model.trace.api.ProcessTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory;

/**
 * <p>
 * Processes the annotations related to the IRM-SA and creates the trace model
 * the holds the connections between the jDEECo runtime metadata and the IRM
 * design model.
 * </p>
 * <p>
 * The <code>AnnotationProcessor</code> provides necessary hooks where this
 * processor is plugged-in.
 * </p>
 *
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @see AnnotationProcessor
 */
public class IrmAwareAnnotationProcessorExtension extends AnnotationProcessorExtensionPoint {

	public static final String COMPONENT_ROLE = "role";

	IRM design;
	TraceModel trace;
	TraceFactory tFactory;

	public IrmAwareAnnotationProcessorExtension(IRM design, TraceModel trace) {
		this.design = design;
		this.trace = trace;
		tFactory = TraceFactory.eINSTANCE;
	}

	/**
	 * Creates invariant monitors by parsing the appropriately annotated Java methods.
	 */
	@Override
	public void onUnknownMethodAnnotation(AnnotationProcessor caller, boolean inComponent, Method method, Annotation unknownAnnotation) throws AnnotationProcessorException {
		if (!(unknownAnnotation instanceof InvariantMonitor)) return;

		String invariantRefID = ((InvariantMonitor) unknownAnnotation).value();
		cz.cuni.mff.d3s.irm.model.design.Invariant invariantInDesignModel;
		if (inComponent) {
			invariantInDesignModel = getProcessInvariantFromDesignModel(invariantRefID);
		} else {
			invariantInDesignModel = getExchangeInvariantFromDesignModel(invariantRefID);
		}

		if (invariantInDesignModel == null) {
			Log.w("No monitor created out of the method " + method.getName());
			return;
		} 
		
		cz.cuni.mff.d3s.irm.model.trace.api.InvariantMonitor monitor = tFactory.createInvariantMonitor();
		monitor.setInvariant(invariantInDesignModel);
		monitor.setMethod(method);

		loop:
		for (Parameter parameter : method.getParameters()) {
			final Annotation[] annotations = parameter.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof AssumptionParameter) {
					continue loop; //skip assumption parameters completely
				}
			}
			MonitorParameter mParameter = tFactory.createMonitorParameter();
			mParameter.setType(parameter.getClass());
			try {
				Annotation directionAnnotation = caller.getKindAnnotation(annotations);
				if (!(directionAnnotation instanceof In)) {
					throw new AnnotationProcessorException("The only direction allowed in monitor parameters is @" + In.class.getSimpleName());
				}
				String path = caller.getKindAnnotationValue(directionAnnotation);
				mParameter.setKnowledgePath(KnowledgePathHelper.createKnowledgePath(path, inComponent ? PathOrigin.COMPONENT : PathOrigin.ENSEMBLE));
			} catch (AnnotationProcessorException | ParseException e) {
				throw new AnnotationProcessorException("Method: " + method.getName() + "->Parameter: "+ parameter.getName() + "->" + e.getMessage(), e);
			}
			monitor.getMonitorParameters().add(mParameter);
		}
		Class<?> returnType = method.getReturnType();
		//TODO maybe create new special annotation for fitness monitors? instead of switching on return type?
		if (returnType == Boolean.class || returnType == Boolean.TYPE) {
			trace.getInvariantSatisfactionMonitors().add(monitor);
		} else if (returnType == Double.class || returnType == Double.TYPE) {
			trace.getInvariantFitnessMonitors().add(monitor);
		} else {
			throw new AnnotationProcessorException("Return type of method " + method.getName() + " does not match supported InvariantMonitors (double/boolean).");
		}
	}

	/**
	 * Creates component traces using the appropriate class-level annotations.
	 */
	@Override
	public void onComponentInstanceCreation(ComponentInstance componentInstance, Annotation unknownAnnotation) {
		if (unknownAnnotation instanceof IRMComponent) {
			String IRMComponentValue = ((IRMComponent) unknownAnnotation).value();

			try {
				componentInstance.getKnowledgeManager().update(getChangeSetWithIRMComponent(IRMComponentValue));
			} catch (KnowledgeUpdateException e) {
				Log.e("Error while writing component role to knowledge manager of component "+ componentInstance.getName(), e);
			}

			Component componentInDesignModel = getIRMComponentFromDesignModel(IRMComponentValue);
			if (componentInDesignModel == null) {
				Log.w("No design trace is created for component " + componentInstance.getName());
			} else {
				ComponentTrace cTrace = tFactory.createComponentTrace();
				cTrace.setFrom(componentInstance);
				cTrace.setTo(componentInDesignModel);
				trace.getComponentTraces().add(cTrace);
			}
		}
		if (unknownAnnotation instanceof SystemComponent) {
			componentInstance.setSystemComponent(true);
		}
	}

	/**
	 * Creates process traces using the appropriate method-level annotations.
	 */
	@Override
	public void onComponentProcessCreation(ComponentProcess componentProcess, Annotation unknownAnnotation) {
		if (unknownAnnotation instanceof Invariant) {
			String invariantRefID = ((Invariant) unknownAnnotation).value();

			cz.cuni.mff.d3s.irm.model.design.Invariant invariantInDesignModel = getProcessInvariantFromDesignModel(invariantRefID);
			if (invariantInDesignModel == null) {
				Log.w("No design trace is created for process " + componentProcess.getName());
			} else {
				ProcessTrace pTrace = tFactory.createProcessTrace();
				pTrace.setFrom(componentProcess);
				pTrace.setTo(invariantInDesignModel);
				trace.getProcessTraces().add(pTrace);
			}
		}
	}

	/**
	 * Creates ensemble traces using the appropriate method-level annotations.
	 */
	@Override
	public void onEnsembleDefinitionCreation(EnsembleDefinition ensembleDefinition, Annotation unknownAnnotation) {
		if (unknownAnnotation instanceof Invariant) {
			String invariantRefId = ((Invariant) unknownAnnotation).value();

			cz.cuni.mff.d3s.irm.model.design.Invariant invariantInDesignModel = getExchangeInvariantFromDesignModel(invariantRefId);
			if (invariantInDesignModel == null) {
				Log.w("No design trace is created for ensemble " + ensembleDefinition.getName());
			} else {
				EnsembleDefinitionTrace eTrace = tFactory.createEnsembleDefinitionTrace();
				eTrace.setFrom(ensembleDefinition);
				eTrace.setTo(invariantInDesignModel);
				trace.getEnsembleDefinitionTraces().add(eTrace);
			}
		}
	}

	private ChangeSet getChangeSetWithIRMComponent(String IRMComponent) {
		ChangeSet changeSet = new ChangeSet();
		KnowledgePath path = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
		PathNodeField pNode = RuntimeMetadataFactoryExt.eINSTANCE.createPathNodeField();
		pNode.setName(COMPONENT_ROLE);
		path.getNodes().add(pNode);
		changeSet.setValue(path, IRMComponent);
		return changeSet;
	}

	private Component getIRMComponentFromDesignModel(String IRMcomponent) {
		Log.d("Looking for component: "+IRMcomponent);
		for (Component c : design.getComponents()) {
			if (c.getName().equals(IRMcomponent)) {
				return c;
			}
		}
		Log.w("Component role " + IRMcomponent + " was not found in the design model.");
		return null;
	}

	private cz.cuni.mff.d3s.irm.model.design.Invariant getProcessInvariantFromDesignModel(String invariantRefId) {
		Log.d("Looking for process invariant: "+invariantRefId);
		for (cz.cuni.mff.d3s.irm.model.design.Contributes c : design.getContributes()) {
			cz.cuni.mff.d3s.irm.model.design.Invariant i = c.getEnd();
			if (i.getRefID().equals(invariantRefId)) {
				return i;
			}
		}
		//TODO for now search for invariant (not process, but assumption) in assumed too
		for (cz.cuni.mff.d3s.irm.model.design.Assumed a : design.getAssumed()) {
			cz.cuni.mff.d3s.irm.model.design.Invariant i = a.getEnd();
			if (i.getRefID().equals(invariantRefId)) {
				return i;
			}
		}
		Log.w("Process invariant " + invariantRefId + " was not found in the design model.");
		return null;
	}

	private cz.cuni.mff.d3s.irm.model.design.Invariant getExchangeInvariantFromDesignModel(String invariantRefId) {
		Log.d("Looking for exchange invariant: "+invariantRefId);
		for (cz.cuni.mff.d3s.irm.model.design.Coordinator c : design.getCoordinators()) {
			cz.cuni.mff.d3s.irm.model.design.Invariant i = c.getEnd();
			if (i.getRefID().equals(invariantRefId)) {
				return i;
			}
		}
		Log.w("Exchange invariant " + invariantRefId + " was not found in the design model.");
		return null;
	}

}
