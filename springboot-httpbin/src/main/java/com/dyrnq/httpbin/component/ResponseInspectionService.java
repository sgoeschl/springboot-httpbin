package com.dyrnq.httpbin.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.gaul.httpbin.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ResponseInspectionService extends BaseService {
    Logger logger = LoggerFactory.getLogger(ResponseInspectionService.class);
    public ResponseEntity<Void> cache() throws Exception {

        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);


        String ifModifiedSince = servletRequest.getHeader("If-Modified-Since");
        String ifNoneMatch = servletRequest.getHeader("If-None-Match");

        if (StringUtils.isNotBlank(ifModifiedSince) || StringUtils.isNotBlank(ifNoneMatch)) {
            servletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return null;
        }

        JSONObject response = new JSONObject();
        response.put("args", mapParametersToJSON());
        response.put("headers", mapHeadersToJSON());
        response.put("origin", getOrigin());
        response.put("url", getFullURL());

        // 获取当前时间
        Date currentDate = new Date();

        // 定义输出格式
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        // 格式化时间并输出
        String formattedDate = formatter.format(currentDate);

        getHttpServletResponse().setHeader("Last-Modified", formattedDate);
        getHttpServletResponse().setHeader("ETag", UUID.randomUUID().toString().replace("-", ""));
        rsOk(os, response);

        return null;
    }

    public ResponseEntity<Void> cacheValue(Integer value) throws Exception {

        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        JSONObject response = new JSONObject();
        response.put("args", mapParametersToJSON());
        response.put("headers", mapHeadersToJSON());
        response.put("origin", getOrigin());
        response.put("url", getFullURL());
        servletResponse.setHeader("Cache-Control", "public, max-age=" + value);
        rsOk(os, response);

        return null;
    }

    public ResponseEntity<Void> etag(String eTag) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        List<String> ifNoneMatch = Collections.list(servletRequest.getHeaders("If-None-Match"));
        List<String> ifMatch = Collections.list(servletRequest.getHeaders("If-Match"));
        logger.info("ifNoneMatch = {}", ifNoneMatch);
        logger.info("ifMatch = {}", ifMatch);
        if (!ifNoneMatch.isEmpty()) {
            //if etag in if_none_match or "*" in if_none_match:
            if (ifNoneMatch.contains(eTag) || ifNoneMatch.contains("*")) {
                servletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                servletResponse.setHeader("ETag", eTag);
                return null;
            }
        } else if (!ifMatch.isEmpty()) {
            // if etag not in if_match and "*" not in if_match:
            if (!ifMatch.contains(eTag) && !ifMatch.contains("*")) {
                servletResponse.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
                return null;
            }
        }
        servletResponse.setHeader("ETag", eTag);
        JSONObject response = new JSONObject();
        response.put("args", jsonObject(queryParamMap()));
        response.put("headers", mapHeadersToJSON());
        response.put("origin", getOrigin());
        response.put("url", getFullURL());
        rsOk(os, response);


//        if (ifMatch == null) {
//            // nothing
//        } else if (ifMatch.equals("*") || ifMatch.equals(eTag)) {
//            servletResponse.setStatus(HttpServletResponse.SC_OK);
//            servletResponse.setHeader("ETag", eTag);
//            return null;
//        } else {
//            servletResponse.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
//            return null;
//        }
//
//        String ifNoneMatch = servletRequest.getHeader("If-None-Match");
//        if (ifNoneMatch == null) {
//            // nothing
//        } else if (ifNoneMatch.equals("*") || ifNoneMatch.equals(eTag)) {
//            servletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
//            servletResponse.setHeader("ETag", eTag);
//            return null;
//        }
//
//        servletResponse.setHeader("ETag", eTag);

        return null;
    }

    public ResponseEntity<Void> responseHeaders() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);
        for (String paramName : Collections.list(servletRequest.getParameterNames())) {
            String[] values = servletRequest.getParameterMap().get(paramName);
            for (String v : values) {
                servletResponse.addHeader(paramName, v);
            }
        }
        int len = 0;
        byte[] body = null;
        while (true) {
            JSONObject response = new JSONObject();
            response.put("Content-Type", "application/json");
            response.put("Content-Length", len + "");
            for (String paramName : Collections.list(servletRequest.getParameterNames())) {
                String[] values = servletRequest.getParameterMap().get(paramName);
                if (values.length == 1) {
                    response.put(paramName, values[0]);
                } else {
                    response.put(paramName, new JSONArray(Arrays.asList(values)));
                }
            }
            body = response.toString(/*indent=*/ 2).getBytes(StandardCharsets.UTF_8);
            if (len == body.length) {
                break;
            }
            len++;
        }


        getHttpServletResponse().setContentLength(body.length);
        getHttpServletResponse().setContentType("application/json");
        getHttpServletResponse().setStatus(HttpServletResponse.SC_OK);
        os.write(body);
        os.flush();


        //servletResponse.setStatus(HttpServletResponse.SC_OK);
        return null;
    }
}
