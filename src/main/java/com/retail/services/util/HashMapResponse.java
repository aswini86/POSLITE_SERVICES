package com.retail.services.util;

import java.util.HashMap;
import java.util.Map;

public class HashMapResponse {

    Map<String, Object> response = new HashMap<>();

    public Map<String, Object> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Object> response) {
        this.response = response;
    }
}
