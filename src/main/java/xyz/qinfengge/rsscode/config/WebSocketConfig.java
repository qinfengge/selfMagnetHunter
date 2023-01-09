package xyz.qinfengge.rsscode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * @author lizhiao
 * @version 1.0
 * @date 2022/3/7 16:48
 */

@Component
public class WebSocketConfig {


    @Bean
    public ServerEndpointExporter serverEndpointExporter(){

        return new ServerEndpointExporter();

    }

}