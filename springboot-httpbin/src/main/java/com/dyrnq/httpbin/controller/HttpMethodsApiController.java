package com.dyrnq.httpbin.controller;


import com.dyrnq.httpbin.api.HttpMethodsApi;
import com.dyrnq.httpbin.component.HttpMethodsService;
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
public class HttpMethodsApiController implements HttpMethodsApi {

    private final NativeWebRequest request;
    @Autowired
    HttpMethodsService httpMethodsService;

    @Autowired
    public HttpMethodsApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> deleteDelete() throws Exception {
        return httpMethodsService.httpMethods();
    }

    @Override
    public ResponseEntity<Void> getGet() throws Exception {
        return httpMethodsService.httpMethods();
    }

    @Override
    public ResponseEntity<Void> patchPatch() throws Exception {
        return httpMethodsService.httpMethods();
    }

    @Override
    public ResponseEntity<Void> postPost() throws Exception {
        return httpMethodsService.httpMethods();
    }

    @Override
    public ResponseEntity<Void> putPut() throws Exception {
        return httpMethodsService.httpMethods();
    }
}
