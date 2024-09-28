package com.dyrnq.httpbin.component;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.google.common.base.Strings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.commons.lang3.StringUtils;
import org.gaul.httpbin.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class BaseService {

    Logger logger = LoggerFactory.getLogger(BaseService.class);
    @Autowired
    private NativeWebRequest request;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;
    @Value("${openapi.springboot-httpbin.base-path:}")
    private String openapiControllerBasePath;

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    protected String cookie(String name, String defaultValue) {
        Cookie cookie = JakartaServletUtil.getCookie(httpServletRequest, name);
        return cookie != null ? cookie.getValue() : defaultValue;
    }

    protected String getFullURL() {
        StringBuilder requestURL = new StringBuilder(httpServletRequest.getRequestURL().toString());
        String queryString = httpServletRequest.getQueryString();
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    protected JSONObject mapParametersToJSON() throws JSONException {
        JSONObject headers = new JSONObject();

        for (String name : Collections.list(httpServletRequest.getParameterNames())) {
            String[] values = request.getParameterValues(name);
            if (values.length == 1) {
                headers.put(name, values[0]);
            } else {
                headers.put(name, new JSONArray(values));
            }
        }

        return headers;
    }

    protected Map<String, List<String>> queryParamMap() throws JSONException, UnsupportedEncodingException {
        Map<String, List<String>> q = new HashMap<>();
        String queryString = httpServletRequest.getQueryString(); // 获取完整的查询字符串
        if (queryString != null) {
            String[] params = queryString.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "";

                if (q.containsKey(key)) {
                    q.get(key).add(value);
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(value);
                    q.put(key, list);
                }
            }
        }
        return q;
    }

    // 获取普通表单字段的值
    protected JSONObject getFormMapJsonObject() throws IOException, ServletException {
        JSONObject j = new JSONObject();
        Map<String, List<String>> q = new HashMap<>();

        return j;
    }

    protected JSONObject jsonObject(Map<String, List<String>> map) {
        JSONObject j = new JSONObject();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> val = entry.getValue();
            if (val.size() > 1) {
                j.put(key, new JSONArray(val));
            } else {
                j.put(key, val.get(0));
            }
        }
        return j;
    }


    protected Map<String, List<String>> getMultipartForm() throws IOException, ServletException {
        Map<String, List<String>> q = new HashMap<>();

        Collection<Part> parts = getHttpServletRequest().getParts();
        for (Part part : parts) {
            if (part.getSubmittedFileName() != null) {
                //uploadedFiles.put(part.getSubmittedFileName(), part.getInputStream().toString()); // 这里可以根据需要处理文件流
            } else {
                String key = part.getName();
                part.getInputStream();
                String value = IoUtil.read(part.getInputStream(), Charset.defaultCharset());
                if (q.containsKey(key)) {
                    q.get(key).add(value);
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(value);
                    q.put(key, list);
                }
            }
        }

        return q;
    }

    protected Map<String, List<String>> getFormUrlencoded(String dataBody) throws IOException, ServletException {
        Map<String, List<String>> q = new HashMap<>();
        if (dataBody != null) {
            String[] params = dataBody.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
//                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
//                String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "";
                String key = keyValue[0];
                String value = keyValue.length > 1 ? keyValue[1] : "";

                if (q.containsKey(key)) {
                    q.get(key).add(value);
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(value);
                    q.put(key, list);
                }
            }
        }
        return q;
    }


    protected void redirectTo(String location) {
        redirectTo(location, HttpServletResponse.SC_MOVED_TEMPORARILY, false);
    }

    protected void redirectTo(String location, int statusCode) {
        redirectTo(location, statusCode, false);
    }


    protected void redirectTo(String location, int statusCode, boolean absolute) {
        String fullLocation = null;
        if (StringUtils.toRootLowerCase(location).startsWith("http")) {
            fullLocation = location;
        } else {
            if (!Strings.isNullOrEmpty(openapiControllerBasePath)) {
                location = StrUtil.removeSuffix(openapiControllerBasePath, "/") + location;
            }
            if (absolute) {
                fullLocation = ServletUriComponentsBuilder.fromCurrentServletMapping().path(location).scheme(getScheme()).build().toUriString();
            } else {
                fullLocation = ServletUriComponentsBuilder.fromCurrentServletMapping().path(location).build().getPath();
            }
        }
        logger.debug("location={},fullLocation={}", location, fullLocation);
        httpServletResponse.setHeader("Location", fullLocation);
        httpServletResponse.setStatus(statusCode);
    }


    protected JSONObject mapHeadersToJSON() throws JSONException {
        JSONObject headers = new JSONObject();

        for (String name : Collections.list(httpServletRequest.getHeaderNames())) {
            List<String> values = Collections.list(httpServletRequest.getHeaders(name));
            if (values.size() == 1) {
                headers.put(name, values.get(0));
            } else {
                headers.put(name, new JSONArray(values));
            }
        }

        return headers;
    }


    protected void copyResource(String rs) throws IOException {
        Resource resource = new ClassPathResource(rs); // 指定图片路径
        httpServletResponse.setContentLength((int) resource.contentLength());
        Utils.copy(resource.getInputStream(), httpServletResponse.getOutputStream());
    }

    protected void redirectTo(String location, boolean absolute) {
        redirectTo(location, HttpServletResponse.SC_MOVED_TEMPORARILY, absolute);
    }

    protected void rsOk(OutputStream os, JSONObject obj) throws IOException, JSONException {
        rsJson(os, obj, HttpServletResponse.SC_OK);
    }

    protected void rsOk(OutputStream os, byte[] body) throws IOException, JSONException {
        rsByte(os, body, HttpServletResponse.SC_OK, "application/json");
    }

    protected void rsJson(OutputStream os, JSONObject obj, int code) throws IOException, JSONException {
        byte[] body = obj.toString(/*indent=*/ 2).getBytes(StandardCharsets.UTF_8);
        rsByte(os, body, code, "application/json");
    }

    protected void rsByte(OutputStream os, byte[] body, int code, String contentType) throws IOException {
        httpServletResponse.setContentLength(body.length);
        httpServletResponse.setStatus(code);
        if (contentType != null) {
            httpServletResponse.setContentType(contentType);
        }
        os.write(body);
        os.flush();
    }

    protected Map<String, String> getFormParameters() {
        Map<String, String> formParams = new HashMap<>();
        for (String paramName : request.getParameterMap().keySet()) {
            formParams.put(paramName, request.getParameter(paramName));
        }
        return formParams;
    }

    protected Map<String, List<String>> getMultipartFormFiles() throws IOException, ServletException {
        Map<String, List<String>> q = new HashMap<>();

        Collection<Part> parts = getHttpServletRequest().getParts();
        for (Part part : parts) {
            if (part.getSubmittedFileName() != null) {
                String key = part.getName();
                part.getInputStream();
                String value = IoUtil.read(part.getInputStream(), Charset.defaultCharset());
                if (q.containsKey(key)) {
                    q.get(key).add(value);
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(value);
                    q.put(key, list);
                }
            }
        }

        return q;
    }

    protected Map<String, String[]> getQueryParameters() {
        return request.getParameterMap();
    }

    protected ResponseEntity<Void> anything(String anything) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        JSONObject response = new JSONObject();

        String contentType = servletRequest.getContentType() != null ? StringUtils.lowerCase(servletRequest.getContentType()) : null;

        response.put("json", JSONObject.NULL);
        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            response.put("data", "");
            response.put("form", jsonObject(getMultipartForm()));
            response.put("files", jsonObject(getMultipartFormFiles()));
        } else if (contentType != null && contentType.startsWith("application/x-www-form-urlencoded")) {
            response.put("files", new JSONObject());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Utils.copy(is, baos);
            String string = baos.toString(StandardCharsets.UTF_8);
            response.put("form", jsonObject(getFormUrlencoded(string)));
            response.put("data", "");
        } else {
            response.put("form", new JSONObject());
            response.put("files", new JSONObject());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Utils.copy(is, baos);
            String string = baos.toString(StandardCharsets.UTF_8);
            response.put("data", string);
            try {
                response.put("json", new JSONObject(string));
            } catch (JSONException e) {
                // client can provide non-JSON data
            }
        }
        response.put("method", getHttpServletRequest().getMethod());
        response.put("args", jsonObject(queryParamMap()));
        response.put("headers", mapHeadersToJSON());
        response.put("origin", getOrigin());
        response.put("url", getFullURL());
        rsOk(os, response);
        return null;
    }

    protected String getOrigin() {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = getHttpServletRequest().getRemoteAddr();
        }
        return ipAddress;
    }

    protected String getScheme(){
        String scheme = request.getHeader("X-Forwarded-Proto");
        if (scheme == null || scheme.length() == 0 || "unknown".equalsIgnoreCase(scheme)) {
            scheme = getHttpServletRequest().getScheme();
        }
        return scheme;
    }
}
