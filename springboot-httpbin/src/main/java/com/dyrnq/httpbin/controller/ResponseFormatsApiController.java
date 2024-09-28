package com.dyrnq.httpbin.controller;


import com.dyrnq.httpbin.api.ResponseFormatsApi;
import com.dyrnq.httpbin.component.ResponseFormatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.9.0-SNAPSHOT")
@Controller
@RequestMapping("${openapi.springboot-httpbin.base-path:}")
public class ResponseFormatsApiController implements ResponseFormatsApi {

    private final NativeWebRequest request;
    @Autowired
    ResponseFormatsService responseFormatsService;

    @Autowired
    public ResponseFormatsApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> brotliGet() throws Exception {
        return responseFormatsService.brotli();
    }

    @Override
    public ResponseEntity<Void> deflateGet() throws Exception {
        return responseFormatsService.deflate();
    }

    @Override
    public ResponseEntity<Void> denyGet() throws Exception {
        return responseFormatsService.deny();
    }

    @Override
    public ResponseEntity<Void> encodingUtf8Get() throws Exception {
        return responseFormatsService.encodingUtf8();
    }

    @Override
    public ResponseEntity<Void> gzipGet() throws Exception {
        return responseFormatsService.gzip();
    }

    @Override
    public ResponseEntity<Void> htmlGet() throws Exception {
        return responseFormatsService.html();
    }

    @Override
    public ResponseEntity<Void> jsonGet() throws Exception {
        return responseFormatsService.json();
    }

    @Override
    public ResponseEntity<Void> robotsTxtGet() throws Exception {
        return responseFormatsService.robots();
    }

    @Override
    public ResponseEntity<Void> xmlGet() throws Exception {
        return responseFormatsService.xml();
    }


    @Operation(
            operationId = "zstdGet",
            summary = "Returns zstd-encoded data.",
            tags = {"Response formats"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "zstd-encoded data.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/zstd"
    )
    public ResponseEntity<Void> zstdGet() throws Exception {
        return responseFormatsService.zstd();
    }
}
