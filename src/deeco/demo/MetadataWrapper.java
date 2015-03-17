package deeco.demo;

import java.io.Serializable;

public class MetadataWrapper<T> implements Serializable{
	
	private T value;
	private long timestamp;
	
	public MetadataWrapper(T value){
		this.value = value;
		timestamp = 0;
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

}
