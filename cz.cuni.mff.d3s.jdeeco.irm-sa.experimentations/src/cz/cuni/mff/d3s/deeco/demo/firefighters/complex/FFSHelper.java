package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.util.HashMap;
import java.util.Map;

public class FFSHelper {

	public static long DANGER_SITUATION_TIME = 6000; // in milliseconds
	
	private static FFSHelper instance;
	
	public static FFSHelper getInstance() {
		if (instance == null) {
			instance = new FFSHelper();
		}
		return instance;
	}
	
	
	
	private DangerSituation situation = null;
	private Map<String, Long> searchAndRescueMissions = new HashMap<>();
	
	private FFSHelper() {}
	
	public void registerFF(String id) {
		searchAndRescueMissions.put(id, null);
	}
	
	public boolean executeDangerSituation(String id, long time) {
		if (situation == null && time > DANGER_SITUATION_TIME) {
			situation = new DangerSituation(id, time);
			return true;
		}
		return false;
	}
	
	public boolean initiateSearchAndRescue(String id, long time) {
		if (situation != null && searchAndRescueMissions.get(id) == null) {
			searchAndRescueMissions.put(id, time);
			return true;
		}
		return false;
	}
	
	public void reset() {
		situation = null;
		searchAndRescueMissions.clear();
	}
	
	public String print() {
		if (situation == null) {
			 return "There was no dangerous situation during the simulation.";
		} else {
			long min = Long.MAX_VALUE;
			for (Long time : searchAndRescueMissions.values()) {
				if (time == null) {
					return "Something is wrong as not all FF issued the rescue misssion.";
				} else if (time < min) {
					min = time;
				}
			}
			return "Situation " + situation.ffId + " at " + situation.time + ", min time for SAR mission: " + min;
		}
	}
	
	private class DangerSituation {
		public final String ffId;
		public final long time;
		
		public DangerSituation(String id, long time) {
			this.ffId = id;
			this.time = time;
		}
		
	}
}
