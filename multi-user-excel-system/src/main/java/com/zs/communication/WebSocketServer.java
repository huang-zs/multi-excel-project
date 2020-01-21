package com.zs.communication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import io.netty.util.internal.logging.Log4J2LoggerFactory;

@ServerEndpoint(value = "/ws/asset/{id}")
@Component
public class WebSocketServer {
	private static Logger logger = LoggerFactory.logger(WebSocketServer.class);

	@PostConstruct
	public void init() {
		logger.debug("websocket 初始化");
	}
	// 群组 里面有很多个群，一个群操作同一个excel
	private static HashMap<String, CopyOnWriteArraySet<Session>> groupMap = new HashMap<>();

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(@PathParam(value = "id") String id, Session session) {
		// 如果这个群已经存在
		if (groupMap.containsKey(id)) {
			CopyOnWriteArraySet<Session> copyOnWriteArraySet = groupMap.get(id);
			copyOnWriteArraySet.add(session);
			groupMap.put(id, copyOnWriteArraySet);
		} else {
			CopyOnWriteArraySet<Session> copyOnWriteArraySet = new CopyOnWriteArraySet<Session>();
			copyOnWriteArraySet.add(session);
			groupMap.put(id, copyOnWriteArraySet);
		}
		//打印连接的群组
		Set<String> connectingSet = groupMap.keySet();
		Iterator<String> iterator = connectingSet.iterator();
		logger.debug("用户["+session+"]加入群组["+id+"]");
		while (iterator.hasNext()) {
			String excelId = iterator.next();
			CopyOnWriteArraySet<Session> copyOnWriteArraySet = groupMap.get(excelId);
			logger.debug("{"+excelId+"}群成员:"+copyOnWriteArraySet);
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(@PathParam(value = "id") String id, Session session) {
		logger.debug("用户["+session+"]退出群组["+id+"]");
		// 获取id这个群
		CopyOnWriteArraySet<Session> set = groupMap.get(id);
		// 移走 这个断开的群友
		set.remove(session);
		// 这个群没人了 移除这个群
		if (set.size() < 1) {
			logger.debug("群内最后一人断开连接,删除群");
			groupMap.remove(id);
		} else {
			groupMap.put(id, set);
			//打印连接的群组
			Set<String> connectingSet = groupMap.keySet();
			Iterator<String> iterator = connectingSet.iterator();
			while (iterator.hasNext()) {
				String excelId = iterator.next();
				CopyOnWriteArraySet<Session> copyOnWriteArraySet = groupMap.get(excelId);
				logger.debug("{"+excelId+"}群成员:"+copyOnWriteArraySet);
			}
		}
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(@PathParam(value = "id") String id, String message, Session session) {
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
		logger.error(error);
		error.printStackTrace();
	}

	/**
	 * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
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
	 * 群发消息
	 * 
	 * @param message
	 * @throws IOException
	 */
	public static void BroadCastInfo(String id, String message, Session senderSession) {
		logger.debug(message);
		// 获取要广播的群
		CopyOnWriteArraySet<Session> set = groupMap.get(id);
		// 不发给自己
//		set.remove(senderSession);
		for (Session receiverSession : set) {
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
	 * 指定Session发送消息
	 * 
	 * @param sessionId
	 * @param message
	 * @throws IOException
	 */
	public static void SendMessage(String message, String sessionId) throws IOException {

	}

}