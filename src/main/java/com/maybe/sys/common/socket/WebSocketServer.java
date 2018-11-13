package com.maybe.sys.common.socket;

import com.auth0.jwt.interfaces.Claim;
import com.maybe.sys.common.config.SocketEnum;
import com.maybe.sys.common.dto.SocketData;
import com.maybe.sys.common.util.JWToken;
import com.maybe.sys.common.util.SpringContextHolder;
import com.maybe.sys.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/web/socket/{token}")
public class WebSocketServer {
    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static ConcurrentHashMap<Integer, WebSocketServer> webSocketSet = new ConcurrentHashMap<>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private Integer id;
    //你要注入的service或者dao
    private WebSocketService socketService;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(@PathParam(value = "token") String token, Session session) {
        addOnlineCount(); // 在线数加1
        try {
            Map<String, Claim> map = JWToken.verifyToken(token);
            this.session = session;
            this.id = map.get("id").asInt(); // 接收到发送消息的人员编号
            webSocketSet.put(this.id, this);   // 加入set中
            //此处是解决无法注入的关键
            socketService = SpringContextHolder.getBean(WebSocketService.class);
            SysUser user = socketService.findUserById(this.id);
            log.info("用户" + user.getName() + "上线！当前在线人数为" + getOnlineCount());
            sendToOther(SocketData.send(SocketEnum.ONLINE_NOTICE, "用户 " + user.getName() + " 上线了！"));
            try {
                sendMessage(SocketData.send(SocketEnum.CONNECT_SUCCESS, ""));
            } catch (IOException e) {
                log.error("WebSocket IO异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("非法用户，断开连接");
            try {
                session.close();
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        subOnlineCount();                // 在线数减1
        webSocketSet.remove(this.id);  // 从set中删除
        SysUser user = socketService.findUserById(this.id);
        log.info("用户" + user.getName() + "离线！当前在线人数为" + getOnlineCount());
        sendToOther(SocketData.send(SocketEnum.OFFLINE_NOTICE, "用户 " + user.getName() + " 离线了！"));
    }

    /**
     * 发生异常的调用方法
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:" + message);
        //可以自己约定字符串内容，比如 内容|0 表示信息群发，内容|X 表示信息发给id为X的用户
        String sendMessage = message.split("[|]")[0];
        String sendUserId = message.split("[|]")[1];
        try {
            if(sendUserId.equals("0"))
                sendToAll(sendMessage);
            else
                sendToUser(sendMessage, sendUserId);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送信息给指定ID用户，如果用户不在线则返回不在线信息给自己
     * @param message
     * @param sendUserId
     * @throws IOException
     */
    public void sendToUser(String message,String sendUserId) throws IOException {
        if (webSocketSet.get(sendUserId) != null) {
            if(!id.equals(sendUserId))
                webSocketSet.get(sendUserId).sendMessage( "用户" + id + "发来消息：" + " <br/> " + message);
            else
                webSocketSet.get(sendUserId).sendMessage(message);
        } else {
            // 如果用户不在线则返回不在线信息给自己
            // sendToUser("当前用户不在线", id);
        }
    }

    /**
     * 发送信息给所有人
     * @param message
     * @throws IOException
     */
    public void sendToAll(String message) {
        for (Integer key : webSocketSet.keySet()) {
            try {
                webSocketSet.get(key).sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送信息给其他人
     */
    public void sendToOther(String message) {
        for (Integer key : webSocketSet.keySet()) {
            if (!key.equals(this.id)) {
                try {
                    webSocketSet.get(key).sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
