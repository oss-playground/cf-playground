package com.demo.pushgateway.example;

import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @Autowired
    private RestTemplate restClient;

    private static Logger log = Logger.getLogger(ExampleApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @RequestMapping(value = "/call", method = RequestMethod.GET)
    public String home(HttpServletRequest request, HttpServletResponse response) {
        // ServiceInstance instance = lbClient.choose("ss1");
        // RestTemplate rest = new RestTemplate();
        log.info("Calling SS1@" + System.getenv("SS1_LINK"));
        return restClient.getForObject("http://" + System.getenv("SS1_LINK") + "/metrics", String.class);
    }
}
