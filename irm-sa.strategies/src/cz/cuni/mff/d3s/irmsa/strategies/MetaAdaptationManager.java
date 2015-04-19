package cz.cuni.mff.d3s.irmsa.strategies;

import static cz.cuni.mff.d3s.irmsa.strategies.ComponentHelper.retrieveFromInternalData;
import static cz.cuni.mff.d3s.irmsa.strategies.ComponentHelper.storeInInternalData;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
@SystemComponent
public class MetaAdaptationManager {

	/** Managing processs period. */
	static final long MANAGING_PERIOD = 2000L;

	/** Run flag stored in internal data under this key. */
	static public final String RUN_FLAG = "runFlag";

	/** Managed managers stored in internal data under this key. */
	static public final String MANAGED_MANAGERS = "managedManagers";

	/** Flag whether IRM adaptation can rum stored in internal data under this key. */
	static public final String IRM_CAN_RUN = "irmCanRun";

	/** Mandatory knowledge. */
	public String id = "MetaAdaptationManager";

	/** Managers have been already started. */
	public Boolean alreadyStarted = Boolean.FALSE;

	@Process
	@PeriodicScheduling(period = MANAGING_PERIOD, order = 10)
	static public void adapt(@In("id") String id,
			@InOut("alreadyStarted") ParamHolder<Boolean> alreadyStarted) {
		final Boolean run = retrieveFromInternalData(RUN_FLAG, Boolean.FALSE);
		final List<AdaptationManager> managers = retrieveFromInternalData(MANAGED_MANAGERS);
		if (!run) {
			for (AdaptationManager manager : managers) {
				manager.stop();
			}
			alreadyStarted.value = false;
			return;
		}
		//TODO implement better meta managing
		if (!alreadyStarted.value) {
			for (AdaptationManager manager : managers) {
				manager.run();
			}
			alreadyStarted.value = true;
		} else {
			boolean done = true;
			for (AdaptationManager manager : managers) {
				done &= manager.isDone();
			}
			if (done) {
				System.out.println("MetaAdaptation done!");
				storeInInternalData(IRM_CAN_RUN, true);
				alreadyStarted.value = false;
			}
		}
	}
}
