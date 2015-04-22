package combined;

import static cz.cuni.mff.d3s.irmsa.strategies.ComponentHelper.retrieveFromInternalData;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irmsa.IRMInstanceGenerator;

@Component
@SystemComponent
public class Monitor {

	/** Trace model stored in internal data under this key. */
	static final String TRACE_MODEL = "trace";

	/** Design model stored in internal data under this key. */
	static final String DESIGN_MODEL = "design";

	/** Period for monitoring. */
	static final long MONITORING_PERIOD = 5000;

	/** Mandatory field. */
	public String id;

	@Process
	@PeriodicScheduling(period = MONITORING_PERIOD, order = 20)
	static public void monitorSelectedInvariantFitness(
			@In("id") String id) {
		final long time = ProcessContext.getTimeProvider().getCurrentMilliseconds();

		// get architecture, design, trace models and plug-ins from the process context
		final Architecture architecture = ProcessContext.getArchitecture();
		final IRM design = retrieveFromInternalData(DESIGN_MODEL);
		final TraceModel trace = retrieveFromInternalData(TRACE_MODEL);

		// generate the IRM runtime model instances
		final IRMInstanceGenerator generator =
				new IRMInstanceGenerator(architecture, design, trace);
		final List<IRMInstance> IRMInstances = generator.generateIRMInstances();

		System.out.println("=============");
		System.out.println("=GRAPH DATA=");
		System.out.println("=============");
		System.out.println("TIME: " + time);
		InvariantInstance batteryInvariant = null;
		InvariantInstance temperatureInvariant = null;
		InvariantInstance inaccuracyInvariant = null;
		for (IRMInstance irm: IRMInstances) {
			for (InvariantInstance invariant: irm.getInvariantInstances()) {
				if (invariant instanceof AssumptionInstance) {
					final AssumptionInstance ai =(AssumptionInstance) invariant;
					if (ai.getComponentInstance().getKnowledgeManager().getId().equals(Environment.FF_LEADER_ID)) {
						if (ai.getInvariant().getRefID().equals("A02")) {
							inaccuracyInvariant = ai;
						} else if (ai.getInvariant().getRefID().equals("A01")) {
							batteryInvariant = ai;
						}
					}
				} else if (invariant instanceof ProcessInvariantInstance) {
					final ProcessInvariantInstance pii =(ProcessInvariantInstance) invariant;
					if (pii.getComponentProcess().getComponentInstance().getKnowledgeManager().getId().equals(Environment.FF_LEADER_ID)) {
						if (pii.getInvariant().getRefID().equals("P03")) {
							temperatureInvariant = pii;
						}
					}
				}
			}
		}
		if (batteryInvariant != null) {
			System.out.println("FF1 battery fitness: " + batteryInvariant.getFitness());
		}
		if (temperatureInvariant != null) {
			System.out.println("FF1 temperature fitness: " + temperatureInvariant.getFitness());
		}
		if (inaccuracyInvariant != null) {
			System.out.println("FF1 inaccuracy fitness: " + inaccuracyInvariant.getFitness());
		}
	}
}
