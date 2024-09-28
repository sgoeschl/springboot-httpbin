package com.dyrnq.httpbin.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gaul.httpbin.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;

@Component
public class StatusCodesService extends BaseService {
    public ResponseEntity<Void> status(String codes) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);
        int status = Integer.parseInt(codes);
        if (status >= 300 && status < 400) {
            redirectTo("/get", status);
        } else {
            servletResponse.setStatus(status);
        }
        return null;
    }
}
