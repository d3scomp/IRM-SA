package firefighters;

import java.util.Collection;
import java.util.List;

import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.simulation.DelayedKnowledgeDataHandler;
import cz.cuni.mff.d3s.deeco.simulation.task.KnowledgeUpdateTask;

public class CustomDelayedNetworkKnowledgeDataHandler extends
		DelayedKnowledgeDataHandler {
	
	public CustomDelayedNetworkKnowledgeDataHandler(long delay) {
		super(delay);
	}
	
//	@Override
//	public void networkBroadcast(AbstractHost from, List<? extends KnowledgeData> knowledgeData, Collection<KnowledgeDataReceiver> receivers) {
//		super.networkBroadcast(from, knowledgeData, receivers);
//		if (from.getHostId().equals("L1")) {
//			long time = schedulers.get(from.getHostId()).getCurrentMilliseconds();
//			long creationTime = NearbyGMInDangerInaccuaracy.getInstance().getCreationTime();
//			if (creationTime > 0)
//				NearbyGMInDangerInaccuaracy.getInstance().setReceivingTime(time + delay);
//		}
//	}
	

	@Override
	public void at(long time, Object triger) {
		super.at(time, triger);
		KnowledgeUpdateTask task = (KnowledgeUpdateTask) triger;
		if (task.getFrom().equals("L1")) {
			long creationTime = NearbyGMInDangerInaccuaracy.getInstance().getCreationTime();
			if (creationTime > 0 && time - delay > creationTime)
				NearbyGMInDangerInaccuaracy.getInstance().setReceivingTime(time);
		}
	}

}
