package com.dyrnq.httpbin.controller;


import com.dyrnq.httpbin.api.RequestInspectionApi;
import com.dyrnq.httpbin.component.RequestInspectionService;
import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.9.0-SNAPSHOT")
@Controller
@RequestMapping("${openapi.springboot-httpbin.base-path:}")
public class RequestInspectionApiController implements RequestInspectionApi {

    private final NativeWebRequest request;
    @Autowired
    RequestInspectionService requestInspectionService;

    @Autowired
    public RequestInspectionApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> headersGet() throws Exception {
        return requestInspectionService.headers();
    }

    @Override
    public ResponseEntity<Void> ipGet() throws Exception {
        return requestInspectionService.ip();
    }

    @Override
    public ResponseEntity<Void> userAgentGet() throws Exception {
        return requestInspectionService.userAgent();
    }
}
