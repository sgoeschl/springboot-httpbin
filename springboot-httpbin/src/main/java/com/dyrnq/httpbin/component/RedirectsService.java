package com.dyrnq.httpbin.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gaul.httpbin.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class RedirectsService extends BaseService {


    public ResponseEntity<Void> redirectTo() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);
        int statusCode = Utils.getIntParameter(servletRequest, "status_code", HttpServletResponse.SC_MOVED_TEMPORARILY);
        redirectTo(servletRequest.getParameter("url"), statusCode);

        return null;
    }


    public ResponseEntity<Void> redirect(int numBytes) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        if (numBytes > 20) {
            servletResponse.setStatus(422);
            os.write("n > 20".getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        if (numBytes > 1) {
            redirectTo("/redirect/" + (numBytes - 1));
        }
        if (numBytes <= 1) {
            redirectTo("/get");
        }
        return null;
    }

    public ResponseEntity<Void> absoluteRedirect(int numBytes) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);
        StringBuffer path = servletRequest.getRequestURL();
        if (numBytes > 20) {
            servletResponse.setStatus(422);
            os.write("n > 20".getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        if (numBytes > 1) {
            redirectTo("/absolute-redirect/" + (numBytes - 1), true);
        }
        if (numBytes <= 1) {
            redirectTo("/get", true);
        }
        return null;
    }


}
