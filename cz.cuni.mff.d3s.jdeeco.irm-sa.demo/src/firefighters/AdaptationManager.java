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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.runtime.api.Alternative;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ShadowInvariantInstance;
import cz.cuni.mff.d3s.irm.model.trace.api.ComponentTrace;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irmsa.ArchitectureReconfigurator;
import cz.cuni.mff.d3s.irmsa.EMFHelper;
import cz.cuni.mff.d3s.irmsa.IRMInstanceGenerator;
import cz.cuni.mff.d3s.irmsa.satsolver.SATSolver;
import cz.cuni.mff.d3s.irmsa.satsolver.SATSolverPreProcessor;

@Component
@SystemComponent
public class AdaptationManager {
	
	public static final String TRACE_MODEL = "trace";
	public static final String DESIGN_MODEL = "design";  
	
	@Process
	@PeriodicScheduling(period=5000)
	public static void reason(@In("id") String id) {
		// get runtime, architecture, design, and trace models from the process context
		RuntimeMetadata runtime = (RuntimeMetadata) ProcessContext.getCurrentProcess().getComponentInstance().eContainer();
//		System.out.println("*** Runtime is: "+ runtime + " ***");
		Architecture architecture = ProcessContext.getArchitecture();
//		printArchitectureModel(architecture);
		IRM design = (IRM) ProcessContext.getCurrentProcess().getComponentInstance().getInternalData().get(DESIGN_MODEL);
		TraceModel trace = (TraceModel) ProcessContext.getCurrentProcess().getComponentInstance().getInternalData().get(TRACE_MODEL);
		// generate the IRM runtime model instances
		IRMInstanceGenerator generator = new IRMInstanceGenerator(architecture, design, trace);
		List<IRMInstance> IRMInstances = generator.generateIRMInstances();
		// preprocess the generated instances
		for (IRMInstance i : IRMInstances) {
			SATSolverPreProcessor preProcessor = new SATSolverPreProcessor(i);
			preProcessor.convertDAGToForest();
		}
		// clean up the files from previous run (if any)
		deleteXMIFilesFromPreviousRun(CentralizedRun.MODELS_BASE_PATH, CentralizedRun.XMIFILE_PREFIX);
		// print the generated IRM runtime instances to the console and to XMI files (for manual checks)
		System.out.println("Number of IRMInstances: " + IRMInstances.size());
		for (int i = 0; i< IRMInstances.size(); i++) {
//			System.out.println(EMFHelper.getXMIStringFromModel(IRMInstances.get(i)));
			EMFHelper.saveModelInXMI(IRMInstances.get(i),CentralizedRun.MODELS_BASE_PATH + CentralizedRun.XMIFILE_PREFIX + i +".xmi");
		}
		// create a reconfigurator of the current runtime
		ArchitectureReconfigurator reconfigurator = new ArchitectureReconfigurator(runtime);
		for (IRMInstance i: IRMInstances) {
//			printIRMInstance(i); // for debugging...
			SATSolver solver = new SATSolver(i);
			
			if (solver.solve()) {
				System.out.println("SOLUTION exists");
				printSelectedIRMInstance(i); // for debugging...
				reconfigurator.addInstance(i);
			} else {
				System.out.println("NO SOLUTION exists");
			}
		}
		// enact changes to the runtime be starting/stopping processes to be run
//		reconfigurator.toggleProcessesAndEnsembles();
	}

	private static void printArchitectureModel(Architecture model) {
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		System.out.println("> ComponentInstances");
		for (ComponentInstance ci : model.getComponentInstances()) {
			if (ci instanceof LocalComponentInstance) {
				System.out.println(">> LocalComponentInstance with id: " + ci.getId());
			} else {
				System.out.println(">> RemoteComponentInstance with id: " + ci.getId());
			}
		}
		System.out.println("> EnsembleInstances");
		for (EnsembleInstance ei : model.getEnsembleInstances()) {
			System.out.println(">> EnsembleInstance "
					+ ei.getEnsembleDefinition().getName() + " with coord: "
					+ ei.getCoordinator().getId() + " and member: "
					+ ei.getMembers().get(0).getId());
		}
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^");
	}

	private static void printIRMInstance(IRMInstance i) {
		System.out.println("###########");
		System.out.println("IRMInstance: "+i);
		System.out.println("Printing IRMComponentInstances...");
		for (IRMComponentInstance ci : i.getIRMcomponentInstances()) {
			System.out.println("IRMComponentInstance: " + ci);
		}
		System.out.println("Printing InvariantInstances' IDs...");
		for (InvariantInstance ii : i.getInvariantInstances()) {
			if (ii instanceof PresentInvariantInstance) {
				System.out.println("InvariantInstance's ID: " + ((PresentInvariantInstance) ii).getInvariant().getRefID() + ", satisfied: " + ii.isSatisfied() + ", selected: " + ii.isSelected());
			} else {
				System.out.println("parent ShadowInvariantInstance, satisfied: " + ii.isSatisfied() + ", selected: " + ii.isSelected());
			}
			List<Alternative> aList = ii.getAlternatives();
			System.out.println("...with children: ");
			for (Alternative a : aList) {
				for (InvariantInstance child: a.getChildren()) {
					if (child instanceof PresentInvariantInstance) {
						System.out.println(((PresentInvariantInstance) child).getInvariant().getRefID());	
					} else {
						System.out.println("child ShadowInvariantInstance");
					}
					
				}
			}
		}
		System.out.println("###########");
	}
	
	private static void printSelectedIRMInstance(IRMInstance i) {
		System.out.println("***********");
		System.out.println("IRMInstance: "+i);
		System.out.println("Printing IRMComponentInstances...");
		for (IRMComponentInstance ci : i.getIRMcomponentInstances()) {
			System.out.println("IRMComponentInstance: " + ci);
		}
		System.out.println("Printing InvariantInstances' IDs...");
		for (InvariantInstance ii : i.getInvariantInstances()) {
			if ((ii instanceof PresentInvariantInstance)&& (ii.isSelected())) {
				System.out.println("Selected InvariantInstance's ID: " + ((PresentInvariantInstance) ii).getInvariant().getRefID() + " and name: " + ((PresentInvariantInstance) ii).getInvariant().getDescription());
			}
		}
		System.out.println("***********");
	}

	private static void deleteXMIFilesFromPreviousRun(String path, String prefix) {
		File[] filesList = new File (path).listFiles();
		for (File f : filesList) {
			String name = f.getName();
			if (name.startsWith(prefix)) {
				System.out.println("Deleting file '" + name + "'");
				try {
					Files.delete(Paths.get(path + name));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}