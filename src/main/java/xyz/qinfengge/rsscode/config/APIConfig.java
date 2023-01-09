package xyz.qinfengge.rsscode.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author lza
 * @Date 2022/12/16/17/13
 **/
@Component
@Data
public class APIConfig {

    @Value("${rsshub.url}")
    private String rsshubUrl;

    @Value("${javbusapi.url}")
    private String javbusapiUrl;
}
