package combined;

import static cz.cuni.mff.d3s.irmsa.strategies.ComponentHelper.retrieveFromInternalData;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.irm.model.design.IRM;
import cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irm.model.trace.api.TraceModel;
import cz.cuni.mff.d3s.irmsa.IRMInstanceGenerator;
import cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata.MetadataWrapper;

@Component
@SystemComponent
public class Monitor {

	/** Trace model stored in internal data under this key. */
	static final String TRACE_MODEL = "trace";

	/** Design model stored in internal data under this key. */
	static final String DESIGN_MODEL = "design";

	/** Period for monitoring. */
	static final long MONITORING_PERIOD = 100;

	/** Mandatory field. */
	public String id;

	/** FF1 Environment temperature from the perspective of FF1. */
	public MetadataWrapper<Double> thoughtTemperature;

	/** FF1 position from the perspective of FF1. */
	public MetadataWrapper<Position> thoughtPosition;

	public static final PrintWriter writer;

	static {
		PrintWriter w = null;
		try {
			w = new PrintWriter("fitnessData.txt", "UTF-8");

			StringBuilder builder = new StringBuilder();
			builder.append(String.format("%s %s %s %s %s\n", "time", "actual_temperature",  "belief_temperature", "actual_position", "belief_position"));
			w.write(builder.toString());

		} catch (IOException e) {
			Log.e("Can't open fitnessData.txt file", e);
		}
		writer = w;
	}

	@Process
	@PeriodicScheduling(period = MONITORING_PERIOD, order = 20)
	static public void monitorSelectedInvariantFitness(
			@In("id") String id,
			@In("thoughtTemperature") MetadataWrapper<Double> thoughtTemperature,
			@In("thoughtPosition") MetadataWrapper<Position> thoughtPosition) {
		final long time = ProcessContext.getTimeProvider().getCurrentMilliseconds();

		// get architecture, design, trace models and plug-ins from the process context
		final Architecture architecture = ProcessContext.getArchitecture();
		final IRM design = retrieveFromInternalData(DESIGN_MODEL);
		final TraceModel trace = retrieveFromInternalData(TRACE_MODEL);

		// generate the IRM runtime model instances
		final IRMInstanceGenerator generator =
				new IRMInstanceGenerator(architecture, design, trace);
		final List<IRMInstance> IRMInstances = generator.generateIRMInstances();

		StringBuilder builder = new StringBuilder();
		builder.append(time).append(" ");

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
		/*if (temperatureInvariant != null) {
			System.out.println("FF1 temperature fitness: " + temperatureInvariant.getFitness());
			builder.append(String.format(Locale.ENGLISH, "%.3f ", temperatureInvariant.getFitness()));
		} else {
			builder.append(" ");
		}
		if (inaccuracyInvariant != null) {
			System.out.println("FF1 inaccuracy fitness: " + inaccuracyInvariant.getFitness());
			builder.append(String.format(Locale.ENGLISH, "%.3f ", inaccuracyInvariant.getFitness()));
		} else {
			builder.append(" ");
		}

		final double temperatureReal = Environment.getTemperature(Environment.FF_LEADER_ID);
		System.out.println("FF1 real temperature: " + temperatureReal);
		builder.append(String.format(Locale.ENGLISH, "%.3f ", temperatureReal));

		if (thoughtTemperature != null && thoughtTemperature.getValue() != null) {
			final double temperatureBelief = thoughtTemperature.getValue();
			System.out.println("FF1 temperature belief: " + temperatureBelief);
			builder.append(String.format(Locale.ENGLISH, "%.3f ", temperatureBelief));

			final double temperatureQuality = 1 - 1.0 * Math.abs(temperatureReal - thoughtTemperature.getValue()) / temperatureReal;
			System.out.println("FF1 temperature quality: " + temperatureQuality);
			builder.append(String.format(Locale.ENGLISH, "%.3f ", temperatureQuality));
		} else {
			builder.append("  ");
		}

		final Position positionReal = Environment.getPositionInternal(Environment.FF_LEADER_ID);
		System.out.println("FF1 real position: " + positionReal);
		builder.append(String.format(Locale.ENGLISH, "%s ", positionReal));

		if (thoughtPosition != null && thoughtPosition.getValue() != null) {
			final Position positionBelief = thoughtPosition.getValue();
			System.out.println("FF1 position belief: " + positionBelief);
			builder.append(String.format(Locale.ENGLISH, "%s ", positionBelief));
		} else {
			builder.append(" ");
		}


		final double positionQuality = 1 - Environment.getInaccuracy(Environment.FF_LEADER_ID) / 30.0;
		System.out.println("FF1 position quality: " + positionQuality);
		builder.append(String.format(Locale.ENGLISH, "%.3f\n", positionQuality));*/

		// actual temperature
		builder.append(Environment.getRealTemperature(Environment.FF_LEADER_ID)).append(" ");

		// belief temperature
		builder.append(thoughtTemperature.getValue()).append(" ");

		// actual position
		builder.append(Environment.getRealPosition(Environment.FF_LEADER_ID)).append(" ");

		// belief position
		builder.append(thoughtPosition.getValue()).append("\n");


		writer.write(builder.toString());
	}
}
