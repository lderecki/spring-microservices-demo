package pl.lderecki.aiapiconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AiApiConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiApiConsumerApplication.class, args);
    }

}
