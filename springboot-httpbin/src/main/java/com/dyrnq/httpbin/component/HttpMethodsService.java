package com.dyrnq.httpbin.component;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HttpMethodsService extends BaseService {
    public ResponseEntity<Void> httpMethods() throws Exception {
        return super.anything(null);
    }
}
