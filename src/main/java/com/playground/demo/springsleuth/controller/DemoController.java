package com.playground.demo.springsleuth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class DemoController {

    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @RequestMapping(value = "/metrics", method = RequestMethod.GET)
    public String getMetricsEndpoint(HttpServletRequest request, HttpServletResponse response) {
        logger.info("This is the log in the endpoint");
        return "Hello World!";
    }

}
