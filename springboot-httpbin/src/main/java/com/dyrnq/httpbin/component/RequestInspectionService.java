package com.dyrnq.httpbin.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;

@Component
public class RequestInspectionService extends BaseService {
    public ResponseEntity<Void> ip() throws Exception {

        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        JSONObject response = new JSONObject();
        response.put("origin", getOrigin());
        rsOk(os, response);

        return null;
    }

    public ResponseEntity<Void> userAgent() throws Exception {

        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        JSONObject response = new JSONObject();
        response.put("user-agent", servletRequest.getHeader("User-Agent"));
        rsOk(os, response);

        return null;
    }

    public ResponseEntity<Void> headers() throws Exception {

        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        JSONObject response = new JSONObject();
        response.put("headers", mapHeadersToJSON());
        rsOk(os, response);

        return null;
    }
}
