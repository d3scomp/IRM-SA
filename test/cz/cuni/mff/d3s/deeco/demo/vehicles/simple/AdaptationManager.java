package cz.cuni.mff.d3s.deeco.demo.vehicles.simple;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMComponentInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.PresentInvariantInstance;
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
	@PeriodicScheduling(2000)
	public static void reason(@In("id") String id) {
		// get runtime, architecture, design, and trace models from the process context
		RuntimeMetadata runtime = (RuntimeMetadata) ProcessContext.getCurrentProcess().getComponentInstance().eContainer();
		Architecture architecture = ProcessContext.getArchitecture();
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
		deleteXMIFilesFromPreviousRun(AcceptanceTest.MODELS_BASE_PATH, AcceptanceTest.XMIFILE_PREFIX);
		// print the generated IRM runtime instances to the console and to XMI files (for manual checks)
		System.out.println("Number of IRMInstances: " + IRMInstances.size());
		for (int i = 0; i< IRMInstances.size(); i++) {
			System.out.println(EMFHelper.getXMIStringFromModel(IRMInstances.get(i)));
			EMFHelper.saveModelInXMI(IRMInstances.get(i),AcceptanceTest.MODELS_BASE_PATH + AcceptanceTest.XMIFILE_PREFIX + i +".xmi");
		}
		// create a reconfigurator of the current runtime
		ArchitectureReconfigurator reconfigurator = new ArchitectureReconfigurator(runtime);
		for (IRMInstance i: IRMInstances) {
			SATSolver solver = new SATSolver(i);
			if (solver.solve()) {
				printSelectedIRMInstance(i); // for debugging...
				reconfigurator.addInstance(i);
			}
		}
		// enact changes to the runtime be starting/stopping processes to be run
		reconfigurator.toggleProcessesAndEnsembles();
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
			if (ii.isSelected()) {
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