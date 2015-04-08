package deeco.metadata;

import java.io.Serializable;

public class MetadataWrapper<T> implements Serializable{
	
	private static final long serialVersionUID = -1190064726886324861L;
	private T value;
	private long timestamp;
	private boolean operational;
	
	public MetadataWrapper(T value){
		this.value = value;
		timestamp = 0;
		operational = true;
	}
	
	public T getValue(){
		return value;
	}
	
	public long getTimestamp(){
		return timestamp;
	}
	
	public void setValue(T value, long timestamp){
		this.value = value;
		this.timestamp = timestamp;
	}
	
	public boolean isOperational(){
		return operational;
	}
	
	public void malfunction(){
		operational = false;	
	}

}
