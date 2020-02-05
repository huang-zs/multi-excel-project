package com.zs.communication;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zs.util.RedisUtil;

@ServerEndpoint(value = "/ws/asset/{id}/{uid}")
@Component
public class WebSocketServer {
	

private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

	
	@Value("${webSocket.timeOut}")
	private long timeOut;
	
	@PostConstruct
	public void init() {
		logger.debug("websocket 初始化");
	}
	// 群组 里面有很多个群，一个群操作同一个excel
	private static ConcurrentHashMap<String,ConcurrentHashMap<String,Session>> groupMap=new ConcurrentHashMap<String,ConcurrentHashMap<String,Session>>();
	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(@PathParam(value = "id") String id,@PathParam("uid")String uid, Session session) {
		//设置超时时间
		session.setMaxIdleTimeout(timeOut);
		ConcurrentHashMap<String, Session> excelUsersMap=null;
		// 如果这个群已经存在
		if (groupMap.containsKey(id)) {
			//获取excel群
			excelUsersMap = groupMap.get(id);
			
		} else {
			//新建excel群
			excelUsersMap = new ConcurrentHashMap<String, Session>();
		}
		//把当前连接的session放进对应的excel群
		excelUsersMap.put(uid, session);
		//把excel群放进excel总群
		groupMap.put(id, excelUsersMap);
		
		//打印连接的群组
		Enumeration<String> keys = groupMap.keys();
		logger.debug("用户["+uid+"]加入群组["+id+"]");
		while(keys.hasMoreElements()) {
			ConcurrentHashMap<String,Session> concurrentHashMap = groupMap.get(id);
			System.out.println("["+keys.nextElement()+"]群成员:"+concurrentHashMap.keySet());
			
		}
		//告诉所有人用户变化
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "users");
		KeySetView<String,Session> keySet = excelUsersMap.keySet();
		jsonObject.put("data", keySet);
		String message=jsonObject.toJSONString();
		BroadCastInfoAll(id, message, session);
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(@PathParam(value = "id") String id,@PathParam("uid")String uid, Session session) {
		logger.debug("用户["+uid+"]退出群组["+id+"]");
		// 获取id这个群
		ConcurrentHashMap<String, Session> excelUsersMap = groupMap.get(id);
		// 移走 这个断开的群友
		excelUsersMap.remove(uid);
		// 这个群没人了 移除这个群
		if (excelUsersMap.size() < 1) {
			logger.debug("群内最后一人断开连接,删除群,删redis");
			groupMap.remove(id);
			//删除存在redis的excel
			RedisUtil.del(id);
		} else {
			//打印连接的群组
			Enumeration<String> keys = groupMap.keys();
			logger.debug("用户["+uid+"]退出群组["+id+"]");
			while(keys.hasMoreElements()) {
				ConcurrentHashMap<String,Session> concurrentHashMap = groupMap.get(id);
				System.out.println("["+keys.nextElement()+"]群成员:"+concurrentHashMap.keySet());
				
			}
			//告诉所有人用户变化
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("type", "users");
			KeySetView<String,Session> keySet = excelUsersMap.keySet();
			jsonObject.put("data", keySet);
			String message=jsonObject.toJSONString();
			BroadCastInfo(id, message, session);
		}
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(@PathParam(value = "id") String id, String message, Session session) {
		logger.debug("收到消息:"+message);
		BroadCastInfo(id, message, session);

	}

	/**
	 * 出现错误
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		logger.error("websocket error",error);
		error.printStackTrace();
	}

	/**
	 * 发送消息
	 * 
	 * @param session
	 * @param message
	 */
	public static void SendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 群发消息不发自己
	 * 
	 * @param message
	 * @throws IOException
	 */
	public static void BroadCastInfo(String id, String message, Session senderSession) {
		logger.debug("群发消息不发自己:"+message);
		// 获取要广播的群
		ConcurrentHashMap<String, Session> excelUsersMap = groupMap.get(id);
		
		Collection<Session> sessions = excelUsersMap.values();
		for (Session receiverSession : sessions) {
			if(senderSession==receiverSession)//不发给自己
				continue;
			if (receiverSession.isOpen()) {
				logger.debug("发送给:"+receiverSession);
				SendMessage(receiverSession, message);
			}else {
				logger.debug(receiverSession+"不是打开状态");
			}
		}
	}
	/**
	 * 群发消息发自己
	 * 
	 * @param message
	 * @throws IOException
	 */
	public static void BroadCastInfoAll(String id, String message, Session senderSession) {
		logger.debug("群发消息发自己:"+message);
		// 获取要广播的群
		ConcurrentHashMap<String, Session> excelUsersMap = groupMap.get(id);
		
		Collection<Session> sessions = excelUsersMap.values();
		for (Session receiverSession : sessions) {
			if (receiverSession.isOpen()) {
				SendMessage(receiverSession, message);
			}else {
				logger.debug(receiverSession+"不是打开状态");
			}
		}
	}


}