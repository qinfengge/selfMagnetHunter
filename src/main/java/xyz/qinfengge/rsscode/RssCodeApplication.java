package xyz.qinfengge.rsscode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "xyz.qinfengge.rsscode.mapper")
@ComponentScan(basePackages = "xyz.qinfengge.rsscode.*")
@EnableScheduling
public class RssCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RssCodeApplication.class, args);
    }

}
