package com.dyrnq.httpbin.controller;


import com.dyrnq.httpbin.api.ResponseInspectionApi;
import com.dyrnq.httpbin.component.ResponseInspectionService;
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
public class ResponseInspectionApiController implements ResponseInspectionApi {

    private final NativeWebRequest request;
    @Autowired
    ResponseInspectionService responseInspectionService;

    @Autowired
    public ResponseInspectionApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> cacheGet(String ifModifiedSince, String ifNoneMatch) throws Exception {
        return responseInspectionService.cache();
    }

    @Override
    public ResponseEntity<Void> cacheValueGet(Integer value) throws Exception {
        return responseInspectionService.cacheValue(value);
    }

    @Override
    public ResponseEntity<Void> etagEtagGet(String etag, String ifNoneMatch, String ifMatch) throws Exception {
        return responseInspectionService.etag(etag);
    }

    @Override
    public ResponseEntity<Void> responseHeadersGet(String freeform) throws Exception {
        return responseInspectionService.responseHeaders();
    }

    @Override
    public ResponseEntity<Void> responseHeadersPost(String freeform) throws Exception {
        return responseInspectionService.responseHeaders();
    }
}
