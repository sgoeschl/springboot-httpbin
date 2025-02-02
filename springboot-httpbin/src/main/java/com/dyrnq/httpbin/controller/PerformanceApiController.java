package com.dyrnq.httpbin.controller;

import com.dyrnq.httpbin.api.PerformanceApi;


import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import jakarta.annotation.Generated;
import com.dyrnq.httpbin.component.PerformanceService;


@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0-SNAPSHOT")
@Controller
@RequestMapping("${openapi.springboot-httpbin.base-path:}")
public class PerformanceApiController implements PerformanceApi {

    private final NativeWebRequest request;

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    public PerformanceApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> performanceGcPauseGet(Long responseTime, Long intervalRepetition, Long intervalDuration)
        throws Exception {
        boolean isBlocked = performanceService.simulateInvocationWithGCPause(
            responseTime != null ? responseTime : 0,
            intervalRepetition != null ? intervalRepetition : 0,
            intervalDuration != null ? intervalDuration :0);
        return isBlocked ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.ok().build();
    }
}
