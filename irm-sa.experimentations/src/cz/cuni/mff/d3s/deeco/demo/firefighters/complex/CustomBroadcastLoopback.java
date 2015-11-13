package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import cz.cuni.mff.d3s.jdeeco.network.device.SimpleBroadcastDevice;

public class CustomBroadcastLoopback extends SimpleBroadcastDevice {

	public CustomBroadcastLoopback() {
		super();
	}
	
	public CustomBroadcastLoopback(long delayMean, int delayDeviation, int range, int mtu) {
		super(delayMean, delayDeviation, range, mtu);
	}

	@Override
	public void sendToAll(PacketWrapper packet) {
		for (LoopDevice loop : loops) {
			if (!FFSHelper.getInstance().dropPacket(Integer.parseInt(packet.source.getId()), loop.getScheduler().getTimer().getCurrentMilliseconds())) {
				super.sendToAll(packet);
			}
		}
	}
}
