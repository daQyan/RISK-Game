package ece651.RISC.Server.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to annotate Handler for automatic registration.
 * This annotation is used to mark WebSocket handlers that should be automatically registered
 * with the Spring WebSocketHandlerRegistry.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketEndpoint {
    /**
     * Specifies the WebSocket endpoint path.
     */
    String value();
}
