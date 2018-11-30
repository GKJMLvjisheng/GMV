package com.gkyj.gmv.server.kafka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

public class EncodeingKafka implements Serializer<Object> {
	@Override
	public void configure(Map configs, boolean isKey) {
		
	}
	@Override
	public byte[] serialize(String topic, Object data) {
		return toByteArray(data);
	}
	/*
	 * producer调用close()方法是调用
	 */
	@Override
	public void close() {
		System.out.println("EncodeingKafka is close");
	}
	/**
	 * 对象转数组
	 * @param obj
	 * @return
	 */
	public byte[] toByteArray (Object obj) {   
		byte[] bytes = null;   
		ByteArrayOutputStream bos = new ByteArrayOutputStream();   
		try {     
			ObjectOutputStream oos = new ObjectOutputStream(bos);      
			oos.writeObject(obj);     
			oos.flush();      
			bytes = bos.toByteArray ();   
			oos.close();      
			bos.close();     
		} catch (IOException ex) {     
			ex.printStackTrace();
		}   
		return bytes; 
	}

}