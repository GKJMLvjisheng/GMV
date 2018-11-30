package com.gkyj.gmv.server.kafka;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

public class DecodeingKafka implements Deserializer<Object> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}
 
	@Override
	public Object deserialize(String topic, byte[] data) {
		return toObject(data);
	}
 
	public Object toObject (byte[] bytes) {   
		Object obj = null;   
		try {     
			ByteArrayInputStream bis = new ByteArrayInputStream (bytes);     
			ObjectInputStream ois = new ObjectInputStream (bis);     
			obj = ois.readObject();   
			ois.close();
			bis.close();
		} catch (IOException ex) {     
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {     
			ex.printStackTrace();
		}   
		return obj; 
	}

	@Override
	public void close() {
		
	}

}
