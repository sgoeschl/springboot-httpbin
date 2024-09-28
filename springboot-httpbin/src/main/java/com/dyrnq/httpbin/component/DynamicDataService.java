package com.dyrnq.httpbin.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.gaul.httpbin.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class DynamicDataService extends BaseService {

    private static final int MAX_DELAY_MS = 10 * 1000;

    public ResponseEntity<Void> delay(Integer delay) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();


        int delayMs = 1000 * delay;
        try {
            Thread.sleep(Math.min(delayMs, MAX_DELAY_MS));
        } catch (InterruptedException ie) {
            // ignore
        }

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


    public ResponseEntity<Void> drip() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        long durationMs = (long) (1000 * Utils.getDoubleParameter(servletRequest, "duration", 0.0));
        int numBytes = Utils.getIntParameter(servletRequest, "numbytes", 10);
        if (numBytes <= 0) {
            servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        int code = Utils.getIntParameter(servletRequest, "code", 200);
        int delay = Utils.getIntParameter(servletRequest, "delay", 0);

        servletResponse.setStatus(code);
        servletResponse.setContentType("application/octet-stream");
        Utils.sleepUninterruptibly(delay, TimeUnit.SECONDS);

        for (int i = 0; i < numBytes; ++i) {
            Utils.sleepUninterruptibly(durationMs / numBytes, TimeUnit.MILLISECONDS);
            os.write('*');
        }

        return null;

    }


    public ResponseEntity<Void> stream(int n) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);


        servletResponse.setContentType("application/json");
        servletResponse.setStatus(HttpServletResponse.SC_OK);

        for (int i = 0; i < n; ++i) {
            Utils.sleepUninterruptibly(1, TimeUnit.SECONDS);

            JSONObject response = new JSONObject();
            response.put("args", mapParametersToJSON());
            response.put("headers", mapHeadersToJSON());
            response.put("origin", getOrigin());
            response.put("url", getFullURL());
            response.put("id", i);

            byte[] body = response.toString().getBytes(StandardCharsets.UTF_8);
            os.write(body);
            os.write('\n');
            os.flush();
        }
        return null;
    }

    public ResponseEntity<Void> streamByte(int numBytes) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);


        int seed = Utils.getIntParameter(servletRequest, "seed", -1);
        int chunkSize = Utils.getIntParameter(servletRequest, "chunkSize", 200);
        byte[] buf = new byte[chunkSize];
        Random random = seed == -1 ? new Random() : new Random(seed);

        servletResponse.setStatus(HttpServletResponse.SC_OK);
        servletResponse.setContentType("application/octet-stream");
        for (long i = 0; i < numBytes; i += chunkSize) {
            random.nextBytes(buf);
            os.write(buf, 0, i + chunkSize > numBytes ? (int) (numBytes - i) : chunkSize);
        }

        return null;
    }

    public ResponseEntity<Void> uuid() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);


        JSONObject response = new JSONObject();
        response.put("uuid", UUID.randomUUID());
        rsOk(os, response);

        return null;
    }

    public ResponseEntity<Void> base64(String value) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);


        byte[] body = "Incorrect Base64 data try: SFRUUEJJTiBpcyBhd2Vzb21l".getBytes(StandardCharsets.UTF_8);

        try {
            if (Helpers.isBase64Encoded(value)) {
                body = Base64.decodeBase64(value);
            }
        } catch (Throwable t) {
        } finally {
            servletResponse.setStatus(HttpServletResponse.SC_OK);
            os.write(body);
            os.flush();
        }


        return null;
    }

    public ResponseEntity<Void> bytes(int numBytes) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        int seed = Utils.getIntParameter(servletRequest, "seed", -1);
        Random random = seed != -1 ? new Random(seed) : new Random();

        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        servletResponse.setContentType("application/octet-stream");
        servletResponse.setStatus(HttpServletResponse.SC_OK);
        servletResponse.setContentLengthLong(numBytes);
        byte[] buffer = new byte[4096];
        for (long i = 0; i < numBytes; ) {
            int count = (int) Math.min(buffer.length, numBytes - i);
            random.nextBytes(buffer);
            os.write(buffer, 0, count);
            i += count;
        }

        return null;
    }


    public ResponseEntity<Void> range(int numBytes) throws Exception {

        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();


        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        long size = numBytes;
        long start;
        long end;
        String range = servletRequest.getHeader("Range");
        if (range != null && range.startsWith("bytes=")) {
            range = range.substring("bytes=".length());
            String[] ranges = range.split("-", 2);
            if (ranges[0].isEmpty()) {
                start = size - Long.parseLong(ranges[1]);
                end = size - 1;
            } else if (ranges[1].isEmpty()) {
                start = Long.parseLong(ranges[0]);
                end = size - 1;
            } else {
                start = Long.parseLong(ranges[0]);
                end = Long.parseLong(ranges[1]);
            }
            if (end + 1 > size || start > end) {
                servletResponse.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                servletResponse.addHeader("ETag", "range" + size);
                servletResponse.addHeader("Content-Range", "bytes */" + size);
                return null;
            }
            servletResponse.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        } else {
            start = 0;
            end = size - 1;
            servletResponse.setStatus(HttpServletResponse.SC_OK);
        }

        servletResponse.addHeader("ETag", "range" + size);
        servletResponse.addHeader("Content-Length", String.valueOf(end - start + 1));
        servletResponse.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + size);
        servletResponse.addHeader("Accept-ranges", "bytes");

        for (long i = start; i <= end; ++i) {
            os.write((char) ('a' + (i % 26)));
        }
        os.flush();

        return null;
    }

    public ResponseEntity<Void> links(Integer n, Integer offset) throws Exception {

        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);


        StringBuffer body = new StringBuffer();
        body.append("<html><head><title>Links</title></head><body>");

        for (int i = 0; i < n; i++) {
            if (i == offset) {
                body.append(String.format("%d ", i));
            } else {
                body.append(String.format("<a href=\"%s/links/%d/%d\">%d</a> ", "", n, i, i));
            }

        }
        body.append("</body></html>");
        byte[] bodyByte = body.toString().getBytes(StandardCharsets.UTF_8);
        rsByte(os, bodyByte, HttpServletResponse.SC_OK, "text/html; charset=utf-8");
        return null;
    }

}
