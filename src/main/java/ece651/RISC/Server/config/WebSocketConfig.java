package ece651.RISC.Server.config;
import ece651.RISC.Server.Controller.ChatWebSocketHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Configuration class for starting WebSocket and registering Handler.
 * The WebSocketConfig class implements WebSocketConfigurer interface
 * to configure WebSocket handlers and endpoints.
 * It enables WebSocket functionality in the Spring application.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    /**
     * An ObjectProvider is used to provide instances of TextWebSocketHandler.
     * It is injected through the constructor.
     */
    private final ObjectProvider<TextWebSocketHandler> webSocketHandlers;

    /**
     * Constructor for WebSocketConfig class.
     *
     * @param webSocketHandlers an ObjectProvider instance providing instances of TextWebSocketHandler
     */
    public WebSocketConfig(ObjectProvider<TextWebSocketHandler> webSocketHandlers) {
        this.webSocketHandlers = webSocketHandlers;
    }

    /**
     * Overrides registerWebSocketHandlers method of WebSocketConfigurer interface.
     * This method registers WebSocket handlers and their endpoints.
     *
     * @param registry a WebSocketHandlerRegistry instance used for registering WebSocket handlers and endpoints
     */
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
