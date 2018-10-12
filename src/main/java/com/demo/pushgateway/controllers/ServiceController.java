package com.demo.pushgateway.controllers;

import com.demo.pushgateway.responses.ServiceResponse;
import io.prometheus.client.Histogram;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    public static final Histogram requestLatency = Histogram.build().name("requests_latency_seconds")
            .help("Request latency in seconds").register();

    @RequestMapping(value = {"/metrics"}, method = RequestMethod.GET)
    public ResponseEntity<ServiceResponse> getMetrics() {
        try {
            System.out.println("Hello world!!");
            return ResponseEntity.ok(new ServiceResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ServiceResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()));
        }
    }

}
