package ece651.RISC.Server;
import com.alibaba.fastjson2.JSON;

import ece651.RISC.Server.config.WebSocketEndpoint;
import ece651.RISC.shared.Player;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.alibaba.fastjson2.JSONObject;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

/**
 * Simple login information Handler
 *
 * @author yang
 * @since 2023/4/3 20:44
 */
@Component
@WebSocketEndpoint("/name")
@Slf4j
public class NameWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOGGER.info("连接已建立，会话ID：" + session.getId() + "，客户端地址：" + session.getRemoteAddress());
        SocketManager.add(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String jsonString = message.getPayload();
        LOGGER.info("receive the message" + jsonString);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String MsgType = jsonObject.getString("msg_type");
        String playerName = "";
        // state1
        // get clients' name, adding to the storage, respond the territory
        if(MsgType.equals("player_name")) {
            playerName = jsonObject.getString("name");
            if(ClientManager.getSize() >= 3) {
                pushMsg(session, "The room is full, please join another game!");
                return;
            }
            ClientManager.addPlayer(session.getId(), new Player(1, playerName));
        }

        if(ClientManager.getSize() < 3) {
            final TextMessage textMessage = new TextMessage("Waiting for other " + (3 - ClientManager.getSize()) + " Players");
            session.sendMessage(textMessage);
            return;
        }

        // state2
        // get clients' unit allocation, respond the Territory
        if(MsgType.equals("player_units")) {
            playerName = jsonObject.getString("name");
            ClientManager.addPlayer(session.getId(), new Player(1, playerName));
        }
        // All the player has come, allocating territories and units
//        initTerritory(ClientManager.getValues());

        // get clients' name


        // begin game
        while (true) {

        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println("消息传输出错！");
        exception.printStackTrace();
    }

    /**
     * 连接关闭移除会话
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("连接被关闭，会话ID：" + session.getId() + "，客户端地址：" + session.getRemoteAddress());
        SocketManager.remove(session.getId());
    }

    /**
     * 向客户端推送消息
     *
     * @param msg 文本消息
     */
    public void pushMsg(WebSocketSession session, String msg) {
        final TextMessage textMessage = new TextMessage(msg);
        try {
            session.sendMessage(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
