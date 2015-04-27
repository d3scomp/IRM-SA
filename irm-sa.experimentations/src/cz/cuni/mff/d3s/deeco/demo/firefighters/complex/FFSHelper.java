package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class FFSHelper {
	private static FFSHelper instance;
	
	public static FFSHelper getInstance() {
		if (instance == null) {
			instance = new FFSHelper();
		}
		return instance;
	}
	
	private Map<String, Team> teams = new HashMap<>();
	private Map<Integer, CustomL2Strategy> strategies = new HashMap<>();
	private Map<Integer, String> nodeIdToOfficerId = new HashMap<>();
	
	private FFSHelper() {}
	
	public void registerFF(Integer nodeId, String officerId) {
		nodeIdToOfficerId.put(nodeId, officerId);
	}
	public void registerOfficer(Integer nodeId, String officerId) {
		Team t = null;
		if (teams.containsKey(officerId)) {
			t = teams.get(officerId);
		} else {
			t = new Team(); 
			teams.put(officerId, t);
		}
		t.team = officerId;
		nodeIdToOfficerId.put(nodeId, officerId);
	}
	
	public void addStrategy(CustomL2Strategy strategy) {
		this.strategies.put(strategy.getNodeId(), strategy);
	}
	
	public boolean executeDangerSituation(String ffId, Integer nodeId, String leaderId, long time) {
		Team t = teams.get(leaderId);
		if (t.situationTime != -1) {
			return false;
		}
		boolean correctFF = false;
		for (int suffix: Settings.inDangerFFSuffix) {
			if (ffId.endsWith(Integer.toString(suffix))) {
				correctFF = true;
				break;
			}
		}
		if (!correctFF) {
			return false;
		}
		if (Settings.inDangerTimes > time) {
			return false;
		}
		t.situationTime = time;
		return true;
	}
	
	public boolean initiateSearchAndRescue(boolean nearbyGMInDanger, String ffId, String leaderId, Integer nodeId, long time) {
		Team t = teams.get(leaderId);
		if (t.situationTime == -1) {
			return false;
		}
		if (t.rescueTime != -1) {
			return false;
		}
		for (int suffix: Settings.inDangerFFSuffix) {
			if (ffId.endsWith(Integer.toString(suffix))) {
				return false;
			}
		}
		if (nearbyGMInDanger) {
			t.rescueTime = time;
			return true;
		}
		Long lastPacketTime = strategies.get(nodeId).getLastPacketTime();
		if (lastPacketTime == null) {
			return false;
		}
		if (time - lastPacketTime > Settings.INACCURACY) {
			t.rescueTime = time;
			t.becauseOfInaccuracy = true;
			return true;
		}
		return false;
	}
	
	public void setOfficerTime(String officerId, long time) {
		Team t = teams.get(officerId);
		if (t.officerTime != -1) {
			return;
		}
		System.out.println(officerId +" detects danger at: " + time);
		t.officerTime = time;
	}
	
	public boolean dropPacket(Integer nodeId, long currentTime) {
		String leaderId = nodeIdToOfficerId.get(nodeId);
		if (leaderId != null) {//FF of Officer
			Team t = teams.get(leaderId);
			if (t.officerTime == -1) {
				return false;
			}
			return t.officerTime + Settings.NETWORK_DELAY > currentTime;
		} else {
			//switch off the communication for anyone who is not ff or officer
			return currentTime > Settings.LEADER_AV_SWITCH_OFF_TIME;
		}
	}
	
	public void reset() {
		teams.clear();
		strategies.clear();
		nodeIdToOfficerId.clear();
	}
	
	public String print() {
		String result = "";
		long reaction;
		for (Team team: teams.values()) {
			reaction = team.rescueTime - team.situationTime;
			result += team.team  +  " - danger at: " + team.situationTime + " officer time: " + team.officerTime + " rescue time at: " + team.rescueTime +  ", rescue after ("+ ((team.becauseOfInaccuracy)? "i":"ni") +"): " + reaction;
			writeToFile(Long.toString(reaction));
		}
		return result;
	}
	
	public void writeToFile(String line) {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("results/"+Settings.BROADCAST_PERIOD+".txt", true)))) {
		    out.println(line);
		}catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	private class Team {
		public String team = null;
		public long officerTime = -1;
		public long situationTime = -1;
		public long rescueTime = -1;
		public boolean becauseOfInaccuracy = false;
	}
	
	
}
