package com.demo.pushgateway.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.logging.Logger;

@SpringBootApplication
@ComponentScan(basePackages = "com.demo.pushgateway.example.*")
@RestController
public class ExampleApplication {

    private static Logger log = Logger.getLogger(ExampleApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @RequestMapping("/metrics")
    public String home(HttpServletRequest request, HttpServletResponse response) {
        // ServiceInstance instance = lbClient.choose("ss1");
        log.info("Handling SS1:/...");
        return String.format("ss1[%s]@%s; serverType:%s", "Test", "Test", "Test");
    }
}
