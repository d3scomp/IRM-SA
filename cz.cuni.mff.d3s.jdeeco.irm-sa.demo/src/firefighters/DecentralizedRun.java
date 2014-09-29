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

import java.util.Arrays;

import org.matsim.api.core.v01.Id;
import org.matsim.core.basic.v01.IdImpl;

import tutorial.environment.MATSimDataProviderReceiver;
import tutorial.matsim.PopulationAgentSource;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.IrmAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.simulation.DirectSimulationHost;
import cz.cuni.mff.d3s.deeco.simulation.SimulationRuntimeBuilder;
import cz.cuni.mff.d3s.deeco.simulation.matsim.JDEECoAgent;
import cz.cuni.mff.d3s.deeco.simulation.matsim.JDEECoAgentSource;
import cz.cuni.mff.d3s.deeco.simulation.matsim.MATSimSimulation;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.design.IRMDesignPackage;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irm.model.trace.meta.TraceFactory;
import cz.cuni.mff.d3s.irmsa.EMFHelper;

public class DecentralizedRun {

	static final String MODELS_BASE_PATH = "designModels/";
	static final String XMIFILE_PREFIX = "firefighters_";
	static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH + "firefighters.irmdesign";
	
	private static final String MATSIM_CONFIG_CUSTOM = "input/config.xml";
	
	private static IRM design;

	private static JDEECoAgentSource jdeecoAgentSource;
	private static MATSimSimulation simulation;
	private static MATSimDataProviderReceiver matSimProviderReceiver;

	private static SimulationRuntimeBuilder builder;

	public static void main(String args[]) throws AnnotationProcessorException, InterruptedException {
		Log.i("Preparing simulation");

		@SuppressWarnings("unused")
		IRMDesignPackage p = IRMDesignPackage.eINSTANCE; 
		design = (IRM) EMFHelper.loadModelFromXMI(DESIGN_MODEL_PATH);
		
		jdeecoAgentSource = new JDEECoAgentSource();
		PopulationAgentSource populationAgentSource = new PopulationAgentSource();
		
		matSimProviderReceiver = new MATSimDataProviderReceiver();
		simulation = new MATSimSimulation(matSimProviderReceiver, matSimProviderReceiver, null, null, Arrays.asList(jdeecoAgentSource, populationAgentSource), MATSIM_CONFIG_CUSTOM);
		
		builder = new SimulationRuntimeBuilder();

		createAndDeployGroupMember(42,"1_1");
		createAndDeployGroupMember(43,"11_11");
		createAndDeployGroupLeader(1,"5_5");
		
		simulation.run();
		Log.i("Simulation Finished");
	}
	
	private static void createAndDeployGroupMember(int idx, String sourceLinkIdString) throws AnnotationProcessorException {
		String compIdString = "M" + idx;
		Id compId = new IdImpl(compIdString);
		Id sourceLinkId = new IdImpl(sourceLinkIdString);

		jdeecoAgentSource.addAgent(new JDEECoAgent(compId, sourceLinkId));

		GroupMember component = new GroupMember(compIdString,"L1");
		
		RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		TraceModel trace = TraceFactory.eINSTANCE.createTraceModel();
		AnnotationProcessorExtensionPoint extension = new IrmAwareAnnotationProcessorExtension(design,trace);
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, extension);
		processor.process(component);//, new AdaptationManager());
		
		// pass design and trace models to the AdaptationManager
		for (ComponentInstance c : model.getComponentInstances()) {
			if (c.getName().equals(AdaptationManager.class.getName())) {
				c.getInternalData().put(AdaptationManager.DESIGN_MODEL, design);
				c.getInternalData().put(AdaptationManager.TRACE_MODEL, trace);
			}
		}
		
		DirectSimulationHost host = simulation.getHost(compIdString);
		RuntimeFramework runtime = builder.build(host, simulation, model, null, null);
		
		runtime.start();		
	}
	
	private static void createAndDeployGroupLeader(int idx, String sourceLinkIdString) throws AnnotationProcessorException {
		String compIdString = "L" + idx;
		Id compId = new IdImpl(compIdString);
		Id sourceLinkId = new IdImpl(sourceLinkIdString);

		jdeecoAgentSource.addAgent(new JDEECoAgent(compId, sourceLinkId));

		GroupLeader component = new GroupLeader(compIdString);
		
		RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		TraceModel trace = TraceFactory.eINSTANCE.createTraceModel();
		AnnotationProcessorExtensionPoint extension = new IrmAwareAnnotationProcessorExtension(design,trace);
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE, model, extension);
		processor.process(component);//, new AdaptationManager());
		
		// pass design and trace models to the AdaptationManager
		for (ComponentInstance c : model.getComponentInstances()) {
			if (c.getName().equals(AdaptationManager.class.getName())) {
				c.getInternalData().put(AdaptationManager.DESIGN_MODEL, design);
				c.getInternalData().put(AdaptationManager.TRACE_MODEL, trace);
			}
		}
		
		DirectSimulationHost host = simulation.getHost(compIdString);
		RuntimeFramework runtime = builder.build(host, simulation, model, null, null);
		
		runtime.start();		
	}
}
