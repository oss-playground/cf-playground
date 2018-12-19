package com.demo.pushgateway.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.logging.Logger;

@SpringBootApplication
// @ComponentScan(basePackages = "com.demo.pushgateway.*")
@RestController
public class ExampleApplication {

    private static Logger log = Logger.getLogger(ExampleApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @RequestMapping("/call")
    public String home() {
        // ServiceInstance instance = lbClient.choose("ss1");
        RestTemplate rest = new RestTemplate();
        URI uri = URI.create("http://" + System.getenv("SS1_LINK") + "/metrics");
        log.info("Calling SS1@" + uri.toString());
        return rest.getForObject("http://" + System.getenv("SS1_LINK") + "/metrics", String.class);
    }
}
