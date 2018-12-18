package com.demo.pushgateway.controllers;

import com.demo.pushgateway.responses.ServiceResponse;
import io.prometheus.client.Histogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
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
public class ServiceController {

    public static final Histogram requestLatency = Histogram.build().name("requests_latency_seconds")
            .help("Request latency in seconds").register();

    @Autowired
    private RestTemplate restClient;

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

    @RequestMapping(value = "/call", method = RequestMethod.GET)
    public ResponseEntity<ServiceResponse> callApi(HttpServletRequest request, HttpServletResponse response,
                                                   @RequestParam(value = "local", required = false) boolean isLocal) {
        System.out.println("/call API");
        String apiResp = "";
        try {
            if (!isLocal) {
                apiResp = restClient.getForObject(System.getenv("DEMO_SERVICE_ONE") + "/metrics", String.class);
                System.out.println("LOCAL API RESPONSE: " + apiResp);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ServiceResponse(HttpStatus.OK.value(), apiResp));
            }

            apiResp = restClient.getForObject("http://localhost:8080/metrics", String.class);
            System.out.println("LOCAL API RESPONSE: " + apiResp);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ServiceResponse(HttpStatus.OK.value(), apiResp));

        } catch (Exception e) {
            // System.out.println(e.getMessage(), e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        }
    }

}
