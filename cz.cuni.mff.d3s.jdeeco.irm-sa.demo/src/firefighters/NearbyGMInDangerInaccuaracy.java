package firefighters;

import java.util.LinkedList;
import java.util.List;

public class NearbyGMInDangerInaccuaracy {

	private static long MAX_INACCURACY = Long.MAX_VALUE; // in milliseconds

	private long creationTime;
	private long receivingTime;

	private List<Long> inaccuracies;

	private static NearbyGMInDangerInaccuaracy instance;

	public static void disableInaccuracyChecking() {
		MAX_INACCURACY = Long.MAX_VALUE;
	}

	public static void enableInaccuarcyChecking(long maxInaccuracy) {
		getInstance().reset();
		MAX_INACCURACY = maxInaccuracy;
	}

	public static long getMaxInaccuracy() {
		return MAX_INACCURACY;
	}

	public static NearbyGMInDangerInaccuaracy getInstance() {
		if (instance == null) {
			instance = new NearbyGMInDangerInaccuaracy();
		}
		return instance;
	}

	private NearbyGMInDangerInaccuaracy() {
		reset();
		inaccuracies = new LinkedList<Long>();
	}

	public void setCreationTime(long time) {
		if (creationTime < 0)
			this.creationTime = time;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setReceivingTime(long time) {
		if (receivingTime < 0) {
			this.receivingTime = time;
			if (MAX_INACCURACY < Long.MAX_VALUE) {
				inaccuracies.add(receivingTime - creationTime);
			}
		}
	}

	public List<Long> getInaccuracies() {
		return inaccuracies;
	}

	public boolean isInaccurate() {
		return creationTime >= 0 && receivingTime >= 0
				&& MAX_INACCURACY < receivingTime - creationTime;
	}

	private void reset() {
		this.creationTime = -1;
		this.receivingTime = -1;
	}
}
