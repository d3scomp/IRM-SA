package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.jdeeco.network.device.SimpleBroadcastDevice;

public class CustomBroadcastLoopback extends SimpleBroadcastDevice {

	public CustomBroadcastLoopback() {
		super();
	}
	
	public CustomBroadcastLoopback(long delayMean, int delayDeviation, int range) {
		super(delayMean, delayDeviation, range);
	}

	@Override
	public void sendToAll(PacketWrapper packet) {
		if (!FFSHelper.getInstance().dropPacket(Integer.parseInt(packet.source.getId()), scheduler.getTimer().getCurrentMilliseconds())) {
			super.sendToAll(packet);
		}
	}
}
