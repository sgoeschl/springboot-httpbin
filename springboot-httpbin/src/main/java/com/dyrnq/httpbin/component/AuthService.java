package com.dyrnq.httpbin.component;

import cn.hutool.core.util.HexUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONUtil;
import com.dyrnq.httpbin.api.ApiUtil;
import com.dyrnq.httpbin.api.entity.AuthInfo;
import com.dyrnq.httpbin.api.entity.Digest;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.gaul.httpbin.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;

/**
 * <a href="https://github.com/SakigamiYang/httpbin4j/blob/master/src/main/java/me/sakigamiyang/httpbin4j/controllers/auth/DigestHandler.java">...</a>
 * <a href="https://gist.github.com/usamadar/2912088">...</a>
 */
@Component
public class AuthService extends BaseService {
    private static final String DIGEST_AUTH_DEFAULT_STALE_AFTER = "never";
    private static final List<String> DIGEST_AUTH_REQUIRE_COOKIE_HANDLING_FLAGS = Arrays.asList("1", "t", "true");
    private static final List<String> DIGEST_AUTH_ALGORITHM_LIST = Arrays.asList("md5", "sha-256", "sha-512");
    private static final List<String> DIGEST_AUTH_QOP_LIST = Arrays.asList("auth", "auth-int", "auth,auth-int");
    private static final Random rand = new Random(Calendar.getInstance().getTimeInMillis());
    private static final String DIGEST_AUTH_DEFAULT_ALGORITHM = "md5";
    static Logger logger = LoggerFactory.getLogger(ApiUtil.class);

    public ResponseEntity<Void> basicAuth(String user, String passwd) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        String authorization = servletRequest.getHeader("Authorization");

        if (authorization == null) {
            servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            servletResponse.setHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
        } else {
            handleBasicAuth(servletRequest, servletResponse, os, user, passwd, HttpServletResponse.SC_UNAUTHORIZED);
        }
        return null;
    }

    public ResponseEntity<Void> hiddenBasicAuth(String user, String passwd) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        String authorization = servletRequest.getHeader("Authorization");

        if (authorization == null) {
            servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            servletResponse.setHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
        } else {
            handleBasicAuth(servletRequest, servletResponse, os, user, passwd, HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
    }


    public ResponseEntity<Void> bearer(String authorization, String token) throws Exception {
        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

        String header = servletRequest.getHeader("Authorization");


        String headerToken = null;
        if (header != null) {
            String[] tokenHolder = StringUtils.splitByWholeSeparator(header, " ");
            if (tokenHolder.length >= 2) {
                if (StringUtils.equalsIgnoreCase(tokenHolder[0], "Bearer")) {
                    headerToken = tokenHolder[1];
                }
            }
        }

        if (headerToken == null) {
            servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        JSONObject response = new JSONObject();
        response.put("authenticated", true);
        response.put("token", headerToken);
        rsOk(os, response);

        return null;
    }

    public ResponseEntity<Void> digestAuth(String qop, String user, String passwd, String algorithm, String staleAfter) throws Exception {

        HttpServletRequest servletRequest = getHttpServletRequest();
        HttpServletResponse servletResponse = getHttpServletResponse();
        //InputStream is = servletRequest.getInputStream();
        OutputStream os = servletResponse.getOutputStream();
        //Utils.copy(is, Utils.NULL_OUTPUT_STREAM);

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Utils.copy(is, baos);

        boolean requireCookieHandling = DIGEST_AUTH_REQUIRE_COOKIE_HANDLING_FLAGS.contains(Optional.ofNullable(servletRequest.getParameter("require-cookie")).orElse(""));

        if (!DIGEST_AUTH_QOP_LIST.contains(qop)) {
            qop = "auth,auth-int";
        }
        if (Strings.isNullOrEmpty(algorithm) || !DIGEST_AUTH_ALGORITHM_LIST.contains(algorithm)) {
            algorithm = DIGEST_AUTH_DEFAULT_ALGORITHM;
        }
        if (Strings.isNullOrEmpty(staleAfter)) {
            staleAfter = DIGEST_AUTH_DEFAULT_STALE_AFTER;
        }
        String authHeader = servletRequest.getHeader("Authorization");
        logger.debug("authHeader={}", authHeader);
        AuthInfo authInfo = AuthInfo.create(authHeader);
        Digest digest = Digest.create(authInfo);

        if (digest == null || (requireCookieHandling && Strings.isNullOrEmpty(servletRequest.getHeader("Cookie")))) {
            JakartaServletUtil.addCookie(servletResponse, "stale_after", staleAfter);
            JakartaServletUtil.addCookie(servletResponse, "fake", "fake_value");
            digestUnauthorizedResponse(servletRequest, servletResponse, qop, algorithm, false);
            return null;
        }

        String fakeCookie = cookie("fake", null);
        if (Strings.isNullOrEmpty(fakeCookie)) {
            fakeCookie = "fake_value";
        }
        if (requireCookieHandling && !"fake_value".equals(fakeCookie)) {
            JSONObject body = new JSONObject() {{
                put("errors", new ArrayList<>() {{
                    add("missing cookie set on challenge");
                }});
            }};

            rsJson(os, body, HttpServletResponse.SC_FORBIDDEN);

            return null;
        }

        String currentNonce = digest.getNonce();
        String staleAfterCookie = cookie("stale_after", null);
        String lastNonceValueCookie = cookie("last_nonce", null);
//        logger.info(" currentNonce={} , lastNonceValueCookie={}",currentNonce,lastNonceValueCookie);
        if (currentNonce.equals(lastNonceValueCookie) || "0".equals(staleAfterCookie)) {
            JakartaServletUtil.addCookie(servletResponse, "stale_after", staleAfter);
            JakartaServletUtil.addCookie(servletResponse, "fake", "fake_value");
            JakartaServletUtil.addCookie(servletResponse, "last_nonce", currentNonce);
            digestUnauthorizedResponse(servletRequest, servletResponse, qop, algorithm, true);
            return null;
        }
        //服务器接收到 Authorization 头后，使用相同的算法来计算 HA1 和 HA2

        boolean check = checkDigestAuth(servletRequest, servletResponse, digest, user, passwd, algorithm);
        logger.debug(" check={}", check);
        if (!check) {
            digestUnauthorizedResponse(servletRequest, servletResponse, qop, algorithm, false);
            return null;
        }

        if (!Strings.isNullOrEmpty(staleAfterCookie)) {
            JakartaServletUtil.addCookie(servletResponse, "stale_after", nextStaleAfterValue(staleAfterCookie));
        }

        JakartaServletUtil.addCookie(servletResponse, "fake", "fake_value");
        JakartaServletUtil.addCookie(servletResponse, "last_nonce", currentNonce);
        JSONObject body = new JSONObject();
        body.put("authenticated", true);
        body.put("user", user);
        rsOk(os, body);

        return null;
    }

    private String nextStaleAfterValue(String staleAfterValue) {
        try {
            return String.valueOf(Integer.parseInt(staleAfterValue) - 1);
        } catch (Throwable t) {
            return DIGEST_AUTH_DEFAULT_STALE_AFTER;
        }
    }

    private boolean checkDigestAuth(@NotNull HttpServletRequest httpServletRequest,
                                    @NotNull HttpServletResponse httpServletResponse,
                                    Digest digest,
                                    String user,
                                    String passwd,
                                    String algorithm) {
        String body = JakartaServletUtil.getBody(httpServletRequest);
        Map<String, String> requestInfo = new TreeMap<>() {{
            put("uri", httpServletRequest.getRequestURI());
            put("body", body);
            put("method", httpServletRequest.getMethod());
        }};

        String serverResponse = makeResponseHash(digest, user, passwd, algorithm, requestInfo);
        logger.debug("digest={}", JSONUtil.toJsonStr(digest));

        logger.debug("serverResponse={},digest.getResponse()={},uri={},algorithm={}", serverResponse, digest.getResponse(), httpServletRequest.getRequestURI(), algorithm);
        return StringUtils.equalsIgnoreCase(serverResponse, digest.getResponse());
        //return Strings.isNullOrEmpty(serverResponse) && digest.getResponse().equals(serverResponse);
    }

    private String makeResponseHash(Digest digest,
                                    String user,
                                    String passwd,
                                    String algorithm,
                                    Map<String, String> requestInfo) {
        String hash;
        String ha1 = HA1(digest.getRealm(), user, passwd, algorithm);
        String ha2 = HA2(digest, requestInfo, algorithm);

        String qop = digest.getQop();
        if (Strings.isNullOrEmpty(qop)) {
            hash = H(Helpers.joinByteArrays(
                            ":".getBytes(StandardCharsets.UTF_8),
                            ha1.getBytes(StandardCharsets.UTF_8),
                            digest.getNonce().getBytes(StandardCharsets.UTF_8),
                            ha2.getBytes(StandardCharsets.UTF_8)),
                    algorithm);
        } else if ("auth".equalsIgnoreCase(qop) || "auth-int".equalsIgnoreCase(qop)) {
            String nonce = digest.getNonce();
            String nc = digest.getNc();
            String cnonce = digest.getCnonce();
            if (Strings.isNullOrEmpty(nonce)
                    || Strings.isNullOrEmpty(nc)
                    || Strings.isNullOrEmpty(cnonce)) {
                throw new RuntimeException("'nonce, nc, cnonce' required for response H");
            }
            hash = H(Helpers.joinByteArrays(
                            ":".getBytes(StandardCharsets.UTF_8),
                            ha1.getBytes(StandardCharsets.UTF_8),
                            nonce.getBytes(StandardCharsets.UTF_8),
                            nc.getBytes(StandardCharsets.UTF_8),
                            cnonce.getBytes(StandardCharsets.UTF_8),
                            qop.getBytes(StandardCharsets.UTF_8),
                            ha2.getBytes(StandardCharsets.UTF_8)),
                    algorithm);
        } else {
            throw new RuntimeException("qop value are wrong");
        }

        return hash;
    }

    private String HA1(String realm, String username, String passwd, String algorithm) {
        if (Strings.isNullOrEmpty(realm)) {
            realm = "";
        }
        return H(Helpers.joinByteArrays(
                ":".getBytes(StandardCharsets.UTF_8),
                username.getBytes(StandardCharsets.UTF_8),
                realm.getBytes(StandardCharsets.UTF_8),
                passwd.getBytes(StandardCharsets.UTF_8)), algorithm);
    }

    private String HA2(Digest digest, Map<String, String> requestInfo, String algorithm) {
        if (Strings.isNullOrEmpty(digest.getQop()) || "auth".equalsIgnoreCase(digest.getQop())) {
            return H(Helpers.joinByteArrays(
                    ":".getBytes(StandardCharsets.UTF_8),
                    requestInfo.get("method").getBytes(StandardCharsets.UTF_8),
                    requestInfo.get("uri").getBytes(StandardCharsets.UTF_8)), algorithm);
        } else if ("auth-int".equalsIgnoreCase(digest.getQop())) {
            return H(Helpers.joinByteArrays(
                            ":".getBytes(StandardCharsets.UTF_8),
                            requestInfo.get("method").getBytes(StandardCharsets.UTF_8),
                            requestInfo.get("uri").getBytes(StandardCharsets.UTF_8),
                            H(requestInfo.get("body").getBytes(StandardCharsets.UTF_8), algorithm).getBytes(StandardCharsets.UTF_8)),
                    algorithm);
        }
        return "";
    }

    private String H(byte[] inputs, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.toUpperCase());
            md.update(inputs);
            byte[] digest = md.digest();
            return HexUtil.encodeHexStr(digest);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * hash functions
     *
     * @param data      data
     * @param algorithm algorithm
     * @return hash result string
     */
    @SuppressWarnings({"deprecation", "UnstableApiUsage"})
    public String hash(String data, String algorithm) {
        return switch (algorithm) {
            case "sha-256" -> Hashing.sha256().hashBytes(data.getBytes()).toString();
            case "sha-512" -> Hashing.sha512().hashBytes(data.getBytes()).toString();
            default -> Hashing.md5().hashBytes(data.getBytes()).toString();
        };
    }

    private void digestUnauthorizedResponse(@NotNull HttpServletRequest httpServletRequest,
                                            @NotNull HttpServletResponse httpServletResponse,
                                            String qop,
                                            String algorithm,
                                            boolean stale) {
        String nonce = hash(String.join(":", httpServletRequest.getRemoteAddr(), String.valueOf(Instant.now().toEpochMilli()), String.valueOf(rand.nextInt())), algorithm);
        String opaque = hash(String.valueOf(rand.nextInt()), algorithm);
        String value = String.join(",",
                String.format("realm=\"%s\"", "me@kennethreitz.com"),
                String.format("nonce=\"%s\"", nonce),
                String.format("opaque=\"%s\"", opaque),
                String.format("qop=%s", qop),
                String.format("algorithm=\"%s\"", algorithm),
                String.format("stale=%s", stale));
        httpServletResponse.setHeader("WWW-Authenticate", "Digest " + value);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
    // Digest realm="me@kennethreitz.com", nonce="3987db62282c1b31146e6b1fecc22c5b", qop="auth", opaque="90a60b5d3d12a8736b208e20fc373db6", algorithm=MD5, stale=FALSE


    private void handleBasicAuth(HttpServletRequest request, HttpServletResponse servletResponse, OutputStream os, String user, String passwd, int failureStatus) throws IOException, JSONException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Basic ")) {
            servletResponse.setStatus(failureStatus);
            return;
        }

        byte[] bytes = Base64.getDecoder().decode(header.substring("Basic ".length()));
        String[] parts = new String(bytes, StandardCharsets.UTF_8).split(":", 2);
        String[] auth = new String[]{user, passwd};
        if (auth.length != 2 || !Arrays.equals(auth, parts)) {
            servletResponse.setStatus(failureStatus);
            return;
        }

        JSONObject response = new JSONObject();
        response.put("authenticated", true);
        response.put("user", parts[0]);
        rsOk(os, response);
    }

}
