package com.dyrnq.httpbin.component;

import cn.hutool.core.util.ZipUtil;
import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.encoder.Encoder;
import com.github.luben.zstd.Zstd;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gaul.httpbin.Utils;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

@Component
public class ResponseFormatsService extends BaseService {


    public ResponseEntity<Void> gzip() throws Exception {
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
        response.put("gzipped", true);


        byte[] uncompressed = response.toString(/*indent=*/ 2).getBytes(StandardCharsets.UTF_8);
        byte[] compressed = ZipUtil.gzip(uncompressed);
        servletResponse.setContentLength(compressed.length);
        servletResponse.setHeader("Content-Encoding", "gzip");
        servletResponse.setContentType("application/json");
        servletResponse.setStatus(HttpServletResponse.SC_OK);
        os.write(compressed);
        os.flush();

        return null;
    }

    public ResponseEntity<Void> deflate() throws Exception {
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
        response.put("deflated", true);

        byte[] uncompressed = response.toString(/*indent=*/ 2).getBytes(
                StandardCharsets.UTF_8);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(
                uncompressed.length);
        try (DeflaterOutputStream dos = new DeflaterOutputStream(
                baos, new Deflater(Deflater.DEFAULT_COMPRESSION,
                /*nowrap=*/ true))) {
            dos.write(uncompressed);
        }
        byte[] compressed = baos.toByteArray();

        servletResponse.setContentLength(compressed.length);
        servletResponse.setHeader("Content-Encoding", "deflate");
        servletResponse.setContentType("application/json");
        servletResponse.setStatus(HttpServletResponse.SC_OK);
        os.write(compressed);
        os.flush();
        return null;
    }

    public ResponseEntity<Void> html() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        servletResponse.setStatus(HttpServletResponse.SC_OK);
        servletResponse.addHeader("Content-Type", "text/html; charset=utf-8");
        copyResource("moby.html");
        return null;
    }

    public ResponseEntity<Void> xml() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        servletResponse.setStatus(HttpServletResponse.SC_OK);
        servletResponse.addHeader("Content-Type", "application/xml");
        copyResource("sample.xml");
        return null;
    }


    public ResponseEntity<Void> encodingUtf8() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        servletResponse.setStatus(HttpServletResponse.SC_OK);
        servletResponse.addHeader("Content-Type", "text/html; charset=utf-8");
        copyResource("UTF-8-demo.txt");
        return null;
    }

    public ResponseEntity<Void> json() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        servletResponse.setStatus(HttpServletResponse.SC_OK);
        servletResponse.addHeader("Content-Type", "application/json");

        String JSON = """
                {
                  "slideshow": {
                    "author": "Yours Truly",
                    "date": "date of publication",
                    "slides": [
                      {
                        "title": "Wake up to WonderWidgets!",
                        "type": "all"
                      },
                      {
                        "items": [
                          "Why <em>WonderWidgets</em> are great",
                          "Who <em>buys</em> WonderWidgets"
                        ],
                        "title": "Overview",
                        "type": "all"
                      }
                    ],
                    "title": "Sample Slide Show"
                  }
                }
                """;

        byte[] output = (JSON).getBytes(StandardCharsets.UTF_8);

        servletResponse.setStatus(HttpServletResponse.SC_OK);
        os.write(output);
        return null;
    }


    public ResponseEntity<Void> robots() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        byte[] output = "User-agent: *\nDisallow: /deny\n".getBytes(StandardCharsets.UTF_8);
        servletResponse.setStatus(HttpServletResponse.SC_OK);
        servletResponse.setContentType("text/plain");
        os.write(output);
        return null;
    }

    public ResponseEntity<Void> deny() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();

        String ANGRY_ASCII = """
                     .-''''''-.
                   .' _      _ '.
                  /   O      O   \\
                 :                :
                 |                |
                 :       __       :
                  \\  .-"`  `"-.  /
                   '.          .'
                     '-......-'
                YOU SHOULDN'T BE HERE
                """;

        byte[] output = (ANGRY_ASCII).getBytes(StandardCharsets.UTF_8);

        servletResponse.setStatus(HttpServletResponse.SC_OK);
        servletResponse.setContentType("text/plain");
        os.write(output);
        return null;
    }

    public ResponseEntity<Void> brotli() throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        // Load the native library
        Brotli4jLoader.ensureAvailability();
        // 读取输入流并忽略内容（如果需要处理请求体，可以在这里实现）
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        // 创建响应 JSON 对象
        JSONObject response = new JSONObject();
        response.put("args", mapParametersToJSON());
        response.put("headers", mapHeadersToJSON());
        response.put("origin", getOrigin());
        response.put("url", getFullURL());
        response.put("brotli", true);

        // 将 JSON 转换为字节数组
        byte[] uncompressed = response.toString(/*indent=*/ 2).getBytes(StandardCharsets.UTF_8);
        byte[] compressed = Encoder.compress(uncompressed);

        // 设置响应头
        servletResponse.setContentLength(compressed.length);
        servletResponse.setHeader("Content-Encoding", "br"); // Brotli 编码
        servletResponse.setContentType("application/json");
        servletResponse.setStatus(HttpServletResponse.SC_OK);

        // 写入压缩后的数据到输出流
        os.write(compressed);
        os.flush();

        return null; // 返回 null，因为我们已经写入了响应
    }

    public ResponseEntity<Void> zstd() throws Exception {
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
        response.put("zstded", true);


        byte[] uncompressed = response.toString(/*indent=*/ 2).getBytes(StandardCharsets.UTF_8);
        byte[] compressed = Zstd.compress(uncompressed);
        servletResponse.setContentLength(compressed.length);
        servletResponse.setHeader("Content-Encoding", "zstd");
        servletResponse.setContentType("application/json");
        servletResponse.setStatus(HttpServletResponse.SC_OK);
        os.write(compressed);
        os.flush();

        return null;
    }
}
