package com.dyrnq.httpbin.component;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AnythingService extends BaseService {
    public ResponseEntity<Void> anything(String anything) throws Exception {
        return super.anything(anything);
    }

    public ResponseEntity<Void> anything() throws Exception {
        return anything(null);
    }
}
