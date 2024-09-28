package com.dyrnq.httpbin.controller;


import com.dyrnq.httpbin.api.AuthApi;
import com.dyrnq.httpbin.component.AuthService;
import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.9.0-SNAPSHOT")
@Controller
@RequestMapping("${openapi.springboot-httpbin.base-path:}")
public class AuthApiController implements AuthApi {

    private final NativeWebRequest request;
    @Autowired
    AuthService authService;

    @Autowired
    public AuthApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> basicAuthUserPasswdGet(String user, String passwd) throws Exception {
        return authService.basicAuth(user, passwd);
    }

    @Override
    public ResponseEntity<Void> bearerGet(String authorization, String token) throws Exception {
        return authService.bearer(authorization, token);
    }

    @Override
    public ResponseEntity<Void> digestAuthQopUserPasswdAlgorithmGet(String qop, String user, String passwd, String algorithm) throws Exception {
        return authService.digestAuth(qop, user, passwd, algorithm, null);
    }

    @Override
    public ResponseEntity<Void> digestAuthQopUserPasswdAlgorithmStaleAfterGet(String qop, String user, String passwd, String algorithm, String staleAfter) throws Exception {
        return authService.digestAuth(qop, user, passwd, algorithm, staleAfter);
    }

    @Override
    public ResponseEntity<Void> digestAuthQopUserPasswdGet(String qop, String user, String passwd) throws Exception {
        return authService.digestAuth(qop, user, passwd, null, null);
    }

    @Override
    public ResponseEntity<Void> hiddenBasicAuthUserPasswdGet(String user, String passwd) throws Exception {
        return authService.hiddenBasicAuth(user, passwd);
    }
}
