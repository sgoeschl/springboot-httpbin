package com.dyrnq.httpbin.controller;


import com.dyrnq.httpbin.api.AnythingApi;
import com.dyrnq.httpbin.component.AnythingService;
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
public class AnythingApiController implements AnythingApi {

    private final NativeWebRequest request;
    @Autowired
    AnythingService anythingService;

    @Autowired
    public AnythingApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> anythingAnythingDelete(String anything) throws Exception {
        return anythingService.anything(anything);
    }

    @Override
    public ResponseEntity<Void> anythingAnythingGet(String anything) throws Exception {
        return anythingService.anything(anything);
    }

    @Override
    public ResponseEntity<Void> anythingAnythingPatch(String anything) throws Exception {
        return anythingService.anything(anything);
    }

    @Override
    public ResponseEntity<Void> anythingAnythingPost(String anything) throws Exception {
        return anythingService.anything(anything);
    }

    @Override
    public ResponseEntity<Void> anythingAnythingPut(String anything) throws Exception {
        return anythingService.anything(anything);
    }

    @Override
    public ResponseEntity<Void> anythingDelete() throws Exception {
        return anythingService.anything();
    }

    @Override
    public ResponseEntity<Void> anythingGet() throws Exception {
        return anythingService.anything();
    }

    @Override
    public ResponseEntity<Void> anythingPatch() throws Exception {
        return anythingService.anything();
    }

    @Override
    public ResponseEntity<Void> anythingPost() throws Exception {
        return anythingService.anything();
    }

    @Override
    public ResponseEntity<Void> anythingPut() throws Exception {
        return anythingService.anything();
    }


}
