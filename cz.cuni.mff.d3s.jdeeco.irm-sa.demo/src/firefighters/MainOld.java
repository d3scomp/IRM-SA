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
package firefighters;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.IrmAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFrameworkBuilder;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Distribution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Execution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Scheduling;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory;
import cz.cuni.mff.d3s.irmsa.EMFHelper;

public class MainOld {

	static final String MODELS_BASE_PATH = "designModels/";
	static final String XMIFILE_PREFIX = "firefighters_";
	static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH + "firefighters.irmdesign";	

	public static void main(String args[]) throws AnnotationProcessorException, InterruptedException {
		RuntimeMetadata runtime = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		TraceModel trace = TraceFactory.eINSTANCE.createTraceModel();
		@SuppressWarnings("unused")
		IRMDesignPackage p = IRMDesignPackage.eINSTANCE; 
		IRM design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);
		
		AnnotationProcessorExtensionPoint extension = new IrmAwareAnnotationProcessorExtension(design,trace);
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, runtime, new CloningKnowledgeManagerFactory(), extension);
		
		processor.process(
			new GroupMember("M1","L1"), 
			new GroupMember("M2","L1"), 
			new GroupLeader("L1"),
			new AdaptationManager(), 
			SensorDataUpdate.class, 
			GMsInDangerUpdate.class 
		);

		// pass design and trace models to the AdaptationManager
		for (ComponentInstance c : runtime.getComponentInstances()) {
			if (c.getName().equals(AdaptationManager.class.getName())) {
				c.getInternalData().put(AdaptationManager.DESIGN_MODEL, design);
				c.getInternalData().put(AdaptationManager.TRACE_MODEL, trace);
			}
		}
		
		RuntimeFrameworkBuilder builder = new RuntimeFrameworkBuilder(
				new RuntimeConfiguration(
						Scheduling.WALL_TIME,
						Distribution.LOCAL, 
						Execution.SINGLE_THREADED), new CloningKnowledgeManagerFactory());
		RuntimeFramework runtimeFramework = builder.build(runtime);
		
		runtimeFramework.start();
		
	}
}
