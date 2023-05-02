package ece651.RISC.Server.config;
import ece651.RISC.Server.Controller.ChatWebSocketHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Start WebSocket and register Handler
 *
 * @author reference cloudgyb
 * @since 2022/4/4 19:00
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ObjectProvider<TextWebSocketHandler> webSocketHandlers;

    public WebSocketConfig(ObjectProvider<TextWebSocketHandler> webSocketHandlers) {
        this.webSocketHandlers = webSocketHandlers;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        webSocketHandlers.forEach(textWebSocketHandler -> {
            final WebSocketEndpoint annotation = textWebSocketHandler.getClass()
                    .getAnnotation(WebSocketEndpoint.class);
            if (annotation != null) {
                final String endpoint = annotation.value();
                registry.addHandler(textWebSocketHandler, endpoint);
            }
        });
        // Register the chatWebSocketHandler() handler which will receive WebSocket
        // connections from
        // the /chat endpoint and allow requests from any origin.
        registry.addHandler(chatWebSocketHandler(), "/chat").setAllowedOrigins("*");
    }

    //evo3: chat room
    //Create and return a new instance of the ChatWebSocketHandler handler.
    private TextWebSocketHandler chatWebSocketHandler() {
        return new ChatWebSocketHandler();
    }

}
