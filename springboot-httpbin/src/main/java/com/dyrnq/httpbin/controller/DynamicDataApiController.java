package com.dyrnq.httpbin.controller;

import com.dyrnq.httpbin.api.DynamicDataApi;
import com.dyrnq.httpbin.component.DynamicDataService;
import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.math.BigDecimal;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.9.0-SNAPSHOT")
@Controller
@RequestMapping("${openapi.springboot-httpbin.base-path:}")
public class DynamicDataApiController implements DynamicDataApi {

    private final NativeWebRequest request;
    @Autowired
    DynamicDataService dynamicDataService;

    @Autowired
    public DynamicDataApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> base64ValueGet(String value) throws Exception {
        return dynamicDataService.base64(value);
    }

    @Override
    public ResponseEntity<Void> bytesNGet(Integer n) throws Exception {
        return dynamicDataService.bytes(n);
    }

    @Override
    public ResponseEntity<Void> delayDelayDelete(Integer delay) throws Exception {
        return dynamicDataService.delay(delay);
    }

    @Override
    public ResponseEntity<Void> delayDelayGet(Integer delay) throws Exception {
        return dynamicDataService.delay(delay);
    }

    @Override
    public ResponseEntity<Void> delayDelayPatch(Integer delay) throws Exception {
        return dynamicDataService.delay(delay);
    }

    @Override
    public ResponseEntity<Void> delayDelayPost(Integer delay) throws Exception {
        return dynamicDataService.delay(delay);
    }

    @Override
    public ResponseEntity<Void> delayDelayPut(Integer delay) throws Exception {
        return dynamicDataService.delay(delay);
    }

    @Override
    public ResponseEntity<Void> dripGet(BigDecimal duration, Integer numbytes, Integer code, BigDecimal delay) throws Exception {
        return dynamicDataService.drip();
    }

    @Override
    public ResponseEntity<Void> linksNOffsetGet(Integer n, Integer offset) throws Exception {
        return dynamicDataService.links(n, offset);
    }

    @Override
    public ResponseEntity<Void> rangeNumbytesGet(Integer numbytes) throws Exception {
        return dynamicDataService.range(numbytes);
    }

    @Override
    public ResponseEntity<Void> streamBytesNGet(Integer n) throws Exception {
        return dynamicDataService.streamByte(n);
    }

    @Override
    public ResponseEntity<Void> streamNGet(Integer n) throws Exception {
        return dynamicDataService.stream(n);
    }

    @Override
    public ResponseEntity<Void> uuidGet() throws Exception {
        return dynamicDataService.uuid();
    }
}
