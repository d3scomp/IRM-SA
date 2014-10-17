package firefighters;


public class Results {
	
	private static Results instance;
	
	private long reactionTime;
	
	public static Results getInstance() {
		if (instance == null) {
			instance = new Results();
		}
		return instance;
	}
	
	private Results() {
		reset();
	}
	
	public void setReactionTime(long time) {
		if (reactionTime < 0)
			reactionTime = time;
	}
	
	public long getReactionTime() {
		return reactionTime;
	}	
	
	public void reset() {
		reactionTime = -1;
	}
}
