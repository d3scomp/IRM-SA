package cz.cuni.mff.d3s.irmsa.strategies;

import static cz.cuni.mff.d3s.irmsa.strategies.ComponentHelper.retrieveFromInternalData;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.SystemComponent;

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
	public String id;

	@Process
	@PeriodicScheduling(period = MANAGING_PERIOD, order = 10)
	static public void adapt(@In("id") String id) {
		final Boolean run = retrieveFromInternalData(RUN_FLAG);
		final List<AdaptationManager> managers = retrieveFromInternalData(MANAGED_MANAGERS);
		if (run == null || !run) {
//			for (AdaptationManager manager : managers) {
//				manager.stop();
//			}
			return;
		}
		//TODO implement meta managing
		for (AdaptationManager manager : managers) {
			manager.run();;
		}
	}
}
