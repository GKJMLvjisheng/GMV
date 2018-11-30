package com.gkyj.gmv.server.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;


@ServerEndpoint("/wbSocket/{topicName}")
@Component
public class WebSocket {
	private Session session;
    public static CopyOnWriteArraySet<WebSocket> wbSockets = new CopyOnWriteArraySet<WebSocket>(); //此处定义静态变量，以在其他方法中获取到所有连接
    public static ConcurrentHashMap<String,CopyOnWriteArraySet<WebSocket>> wbSocketsMap = new ConcurrentHashMap<>();
    /**
     * 建立连接。
     * 建立连接时入参为session
     */
    @OnOpen
    public void onOpen(Session session,@PathParam(value = "topicName") String topicName){
        this.session = session;
        if(topicName == null || topicName.isEmpty()) {
        	wbSockets.add(this); //将此对象存入集合中以在之后广播用
        }else {
        	 //实现一对一订阅
        	boolean flag = wbSocketsMap.containsKey(topicName);
        	if(!flag) {
        		CopyOnWriteArraySet<WebSocket> newSet = new CopyOnWriteArraySet<>();
        		newSet.add(this);
        		wbSocketsMap.put(topicName, newSet);
        	}else {
        		CopyOnWriteArraySet<WebSocket> oldSet = wbSocketsMap.get(topicName);
        		oldSet.add(this);
        		wbSocketsMap.put(topicName,oldSet);
        	}
        }
        System.out.println("New session insert,sessionId is "+ session.getId() + " topicName:" + topicName);
    }
    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(@PathParam(value = "topicName") String topicName){
    	if(topicName == null || topicName.isEmpty()) {
    		wbSockets.remove(this);//将socket对象从集合中移除，以便广播时不发送次连接。如果不移除会报错(需要测试)
    	}else {
    		CopyOnWriteArraySet<WebSocket> oldSet = wbSocketsMap.get(topicName);
    		oldSet.remove(this);
    		wbSocketsMap.put(topicName, oldSet);
    	} 
        System.out.println("A session closed,sessionId is "+ session.getId());
    }
    /**
     * 接收前端传过来的数据。
     * 虽然在实现推送逻辑中并不需要接收前端数据，但是作为一个webSocket的教程或叫备忘，还是将接收数据的逻辑加上了。
     */
    @OnMessage
    public void onMessage(String message ,Session session){
        System.out.println(message + "from " + session.getId());
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}