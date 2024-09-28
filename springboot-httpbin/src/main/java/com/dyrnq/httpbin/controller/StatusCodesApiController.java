package com.dyrnq.httpbin.controller;


import com.dyrnq.httpbin.api.StatusCodesApi;
import com.dyrnq.httpbin.component.StatusCodesService;
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
public class StatusCodesApiController implements StatusCodesApi {

    private final NativeWebRequest request;
    @Autowired
    StatusCodesService statusCodesService;

    @Autowired
    public StatusCodesApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> statusCodesDelete(String codes) throws Exception {
        return statusCodesService.status(codes);
    }

    @Override
    public ResponseEntity<Void> statusCodesGet(String codes) throws Exception {
        return statusCodesService.status(codes);
    }

    @Override
    public ResponseEntity<Void> statusCodesPatch(String codes) throws Exception {
        return statusCodesService.status(codes);
    }

    @Override
    public ResponseEntity<Void> statusCodesPost(String codes) throws Exception {
        return statusCodesService.status(codes);
    }

    @Override
    public ResponseEntity<Void> statusCodesPut(String codes) throws Exception {
        return statusCodesService.status(codes);
    }
}
