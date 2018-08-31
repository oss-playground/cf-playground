package com.playground.demo.microservice01.controller;

import com.playground.demo.microservice01.model.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<ServiceResponse> getMetricsEndpoint(HttpServletRequest request, HttpServletResponse response) {
        logger.info("/metrics API");
        return ResponseEntity.status(HttpStatus.OK).body(
                new ServiceResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }

    @RequestMapping(value = "/call", method = RequestMethod.GET)
    public ResponseEntity<ServiceResponse> callApi(HttpServletRequest request, HttpServletResponse response,
                                                   @RequestParam(value = "local", required = false) boolean isLocal) {
        logger.info("/call API");
        String apiResp = "";
        try {
            if (!isLocal) {
                apiResp = restClient.getForObject(System.getenv("DEMO_SERVICE_ONE") + "/metrics", String.class);
                logger.info("LOCAL API RESPONSE: " + apiResp);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ServiceResponse(HttpStatus.OK.value(), apiResp));
            }

            apiResp = restClient.getForObject("http://localhost:8080/metrics", String.class);
            logger.info("LOCAL API RESPONSE: " + apiResp);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ServiceResponse(HttpStatus.OK.value(), apiResp));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        }
    }

}
