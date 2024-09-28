package com.dyrnq.httpbin.controller;


import com.dyrnq.httpbin.api.RedirectsApi;
import com.dyrnq.httpbin.component.RedirectsService;
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
public class RedirectsApiController implements RedirectsApi {

    private final NativeWebRequest request;
    @Autowired
    RedirectsService redirectsService;

    @Autowired
    public RedirectsApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> absoluteRedirectNGet(Integer n) throws Exception {
        return redirectsService.absoluteRedirect(n);
    }

    @Override
    public ResponseEntity<Void> redirectNGet(Integer n) throws Exception {
        return redirectsService.redirect(n);
    }

    @Override
    public ResponseEntity<Void> redirectToDelete() throws Exception {
        return redirectsService.redirectTo();
    }

    @Override
    public ResponseEntity<Void> redirectToGet(String url, Integer statusCode) throws Exception {
        return redirectsService.redirectTo();
    }

    @Override
    public ResponseEntity<Void> redirectToPatch() throws Exception {
        return redirectsService.redirectTo();
    }

    @Override
    public ResponseEntity<Void> redirectToPost(String url, Integer statusCode) throws Exception {
        return redirectsService.redirectTo();
    }

    @Override
    public ResponseEntity<Void> redirectToPut(String url, Integer statusCode) throws Exception {
        return redirectsService.redirectTo();
    }

    @Override
    public ResponseEntity<Void> relativeRedirectNGet(Integer n) throws Exception {
        return redirectsService.redirect(n);
    }
}
