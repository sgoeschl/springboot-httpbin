package com.dyrnq.httpbin.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.gaul.httpbin.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class ImagesService extends BaseService {

    private final Map<String, String[]> images = new HashMap<>() {{
        put("image/jpeg", new String[]{"image/jpeg", "images/jackal.jpg"});
        put("image/jpg", new String[]{"image/jpeg", "images/jackal.jpg"});
        put("image/webp", new String[]{"image/webp", "images/wolf_1.webp"});
        put("image/svg+xml", new String[]{"image/svg+xml", "images/svg_logo.svg"});
        put("image/png", new String[]{"image/png", "images/pig_icon.png"});
        put("*/*", new String[]{"image/webp", "images/wolf_1.webp"});
        put("image/*", new String[]{"image/webp", "images/wolf_1.webp"});
    }};


    public ResponseEntity<Void> images(String imageType) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        String acceptHeader = servletRequest.getHeader("accept");
        String accept = StringUtils.isNotBlank(imageType) ? imageType : (acceptHeader != null ? acceptHeader : "*/*");
        accept = StringUtils.lowerCase(accept);


        if (accept != null && images.containsKey(accept)) {
            Utils.copy(is, Utils.NULL_OUTPUT_STREAM);
            servletResponse.setStatus(HttpServletResponse.SC_OK);
            servletResponse.addHeader("Content-Type", images.get(accept)[0]);
            copyResource(images.get(accept)[1]);
        } else {
            getHttpServletResponse().setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        return null;

    }
}
