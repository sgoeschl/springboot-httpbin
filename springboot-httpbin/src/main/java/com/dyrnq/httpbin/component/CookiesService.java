package com.dyrnq.httpbin.component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gaul.httpbin.Utils;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Component
public class CookiesService extends BaseService {
    public ResponseEntity<Void> cookies() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        JSONObject cookies = new JSONObject();

        if (servletRequest.getCookies() != null) {
            for (Cookie cookie : servletRequest.getCookies()) {
                cookies.put(cookie.getName(), cookie.getValue());
            }
        }

        JSONObject response = new JSONObject();
        response.put("cookies", cookies);

        rsOk(os, response);
        return null;
    }


    public ResponseEntity<Void> cookiesSet() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();


        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        for (Map.Entry<String, String[]> entry : servletRequest.getParameterMap().entrySet()) {
            for (String value : entry.getValue()) {
                servletResponse.addHeader("Set-Cookie", String.format("%s=%s; Path=/", entry.getKey(), value));
            }
        }
        redirectTo("/cookies", HttpServletResponse.SC_MOVED_TEMPORARILY);
        return null;
    }

    public ResponseEntity<Void> cookiesSet(String name, String value) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();


        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        servletResponse.addHeader("Set-Cookie", String.format("%s=%s; Path=/", name, value));
        redirectTo("/cookies", HttpServletResponse.SC_MOVED_TEMPORARILY);
        return null;
    }

    public ResponseEntity<Void> cookiesDelete() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();


        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        for (Map.Entry<String, String[]> entry : servletRequest.getParameterMap().entrySet()) {
            servletResponse.addHeader("Set-Cookie", String.format("%s=; Path=/", entry.getKey()));
        }

        redirectTo("/cookies", HttpServletResponse.SC_MOVED_TEMPORARILY);

        return null;
    }
}
