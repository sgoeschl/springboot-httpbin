package com.dyrnq.httpbin.controller;


import com.dyrnq.httpbin.api.ImagesApi;
import com.dyrnq.httpbin.component.ImagesService;
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
public class ImagesApiController implements ImagesApi {

    private final NativeWebRequest request;
    @Autowired
    ImagesService imagesService;

    @Autowired
    public ImagesApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> imageGet() throws Exception {
        return imagesService.images(null);
    }

    @Override
    public ResponseEntity<Void> imageJpegGet() throws Exception {
        return imagesService.images("image/jpeg");
    }

    @Override
    public ResponseEntity<Void> imagePngGet() throws Exception {
        return imagesService.images("image/png");
    }

    @Override
    public ResponseEntity<Void> imageSvgGet() throws Exception {
        return imagesService.images("image/svg+xml");
    }

    @Override
    public ResponseEntity<Void> imageWebpGet() throws Exception {
        return imagesService.images("image/webp");
    }
}
