package com.gkyj.gmv.server.kafka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import com.alibaba.fastjson.JSON;
import com.gkyj.gmv.server.user.model.TestData;
import com.gkyj.gmv.server.websocket.WebSocket;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class Listener {
	@KafkaListener(topics = {"test"})
    public void listen(ConsumerRecord<?, ?> record) {
        /*log.info("kafka的key: " + record.key());
        log.info("kafka的value: " + record.value().toString());*/
        sendMessageToUser(record,WebSocket.wbSocketsMap.get("test"));
        
	}
	@KafkaListener(topics = {"topic"})
    public void topicListen(ConsumerRecord<?, ?> record) {
       /* log.info("kafka的key: " + record.key());
       TestData testData = (TestData)record.value();
       log.info("kafka的value: " + testData.getParameter());*/
        sendMessageToUser(record,WebSocket.wbSocketsMap.get("topic"));
	}
	
	@KafkaListener(topics = {"video"})
    public void videoListen(ConsumerRecord<?, ?> record) {
       /* log.info("kafka的key: " + record.key());
       TestData testData = (TestData)record.value();
       log.info("kafka的value: " + testData.getParameter());*/
        sendMessageToUser(record,WebSocket.wbSocketsMap.get("video"));
	}
	
	public void sendMessageToUser(ConsumerRecord<?, ?> record,CopyOnWriteArraySet<WebSocket> sets) {
		if(sets == null || sets.size() == 0) {
			return;
		}
		for (WebSocket webSocket :sets){
            try {
            	
            	if(record.topic() != null && record.topic().equals("video")) {
                    webSocket.sendMessage(record.value().toString());
            	}else {
            		List<TestData> testData =(ArrayList) record.value();
                    webSocket.sendMessage(JSON.toJSON(testData).toString());
            	}
            	
			} catch (IOException e) {
				e.printStackTrace();
				log.info("websocket"+ e.getMessage());
			}
        }
	}
        
}
