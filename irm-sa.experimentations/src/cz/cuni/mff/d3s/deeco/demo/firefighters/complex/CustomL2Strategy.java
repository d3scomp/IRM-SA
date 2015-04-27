package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Strategy;

public class CustomL2Strategy implements DEECoPlugin, L2Strategy {

	private CurrentTimeProvider timeProvider;
	private int nodeId;
	private KnowledgePath kp;
	
	private Long lastPacketTime = -1L;
	private Long inDangerTime = -1L;
	
	@Override
	public void processL2Packet(L2Packet packet) {
		// TODO Auto-generated method stub
		KnowledgeData kd = (KnowledgeData) packet.getObject();
		if (inDangerTime == -1) {
			if (kd.getKnowledge().getKnowledgePaths().contains(kp) && (Integer) kd.getKnowledge().getValue(kp) > 0) {
				inDangerTime = timeProvider.getCurrentMilliseconds();
			} else {
				lastPacketTime = timeProvider.getCurrentMilliseconds();
			}
		}
	}
 	
	public Long getInaccuracy() {
		if (lastPacketTime == -1L || inDangerTime == -1L) {
			return null;
		}
		return inDangerTime - lastPacketTime;
	}
	
	public Long getInDangerTime() {
		return inDangerTime;
	}
	
	public Long getLastPacketTime() {
		return lastPacketTime;
	}
	
	public int getNodeId() {
		return nodeId;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		this.nodeId = container.getId();
		// Resolve dependencies
		Network network = container.getPluginInstance(Network.class);
		timeProvider = container.getRuntimeFramework().getScheduler().getTimer();
		// Register as network L2 strategy
		network.getL2().registerL2Strategy(this);
		try {
			kp = KnowledgePathHelper.createKnowledgePath("noOfGMsInDanger", PathOrigin.COMPONENT);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AnnotationProcessorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FFSHelper.getInstance().addStrategy(this);
	}

}
