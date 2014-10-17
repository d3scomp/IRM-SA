package firefighters;


public class InDangerTimeHelper {
	
	private static InDangerTimeHelper instance;
	
	private long inDangerTime;
	
	public static InDangerTimeHelper getInstance() {
		if (instance == null) {
			instance = new InDangerTimeHelper();
		}
		return instance;
	}
	
	private InDangerTimeHelper() {
		reset();
	}
	
	public void setInDangerTime(long time) {
		if (inDangerTime < 0)
			inDangerTime = time;
	}
	
	public long getInDangerTime() {
		return inDangerTime;
	}
	
	public boolean isSetInDangerTime() {
		return inDangerTime > 0;
	}	
	
	public void reset() {
		inDangerTime = -1;
	}
}
