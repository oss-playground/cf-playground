package com.playground.demo.springsleuth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class DemoController {

    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private RestTemplate restClient;

    @RequestMapping(value = "/metrics", method = RequestMethod.GET)
    public String getMetricsEndpoint(HttpServletRequest request, HttpServletResponse response) {
        logger.info("in the /metrics API");
        return "/metrics API response";
    }

    @RequestMapping(value = "/call", method = RequestMethod.GET)
    public String callApi(HttpServletRequest request, HttpServletResponse response) {
        logger.info("in the /call API");

        String resp = restClient.getForObject("http://localhost:8080/metrics", String.class);
        logger.info("The response is" + resp);

        return "/call API response";
    }

}
