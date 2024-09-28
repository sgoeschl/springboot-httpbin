package com.dyrnq.httpbin.controller;


import com.dyrnq.httpbin.api.CookiesApi;
import com.dyrnq.httpbin.component.CookiesService;
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
public class CookiesApiController implements CookiesApi {

    private final NativeWebRequest request;
    @Autowired
    CookiesService cookiesService;

    @Autowired
    public CookiesApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> cookiesDeleteGet(String freeform) throws Exception {
        return cookiesService.cookiesDelete();
    }

    @Override
    public ResponseEntity<Void> cookiesGet() throws Exception {
        return cookiesService.cookies();
    }

    @Override
    public ResponseEntity<Void> cookiesSetGet(String freeform) throws Exception {
        return cookiesService.cookiesSet();
    }

    @Override
    public ResponseEntity<Void> cookiesSetNameValueGet(String name, String value) throws Exception {
        return cookiesService.cookiesSet(name, value);
    }
}
