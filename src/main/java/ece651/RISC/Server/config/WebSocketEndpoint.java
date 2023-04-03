package ece651.RISC.Server.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotate Handler for automatically register
 *
 * @author  reference cloudgyb
 * @since 2022/4/4 19:11
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketEndpoint {
    /**
     * WebSocket 端点路径
     */
    String value();
}
