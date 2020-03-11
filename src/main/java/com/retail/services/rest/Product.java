package com.retail.services.rest;

import java.util.HashMap;
import java.util.Hashtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.services.soap.SoapClient;
import com.retail.services.util.HashMapResponse;

@RestController
@RequestMapping("/bo")
public class Product {

    @Autowired
    ApplicationContext context;

    @PostMapping(path = "/findProducts")
    public Object findProducts(@RequestBody Hashtable<String, String> params) {
        HashMap<String, Object> errorMsg = new HashMap<String, Object>();
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);

        for (String key : params.keySet()) {
            soapClient.addRequestParameter(key, params.get(key));
        }
        HashMapResponse res = new HashMapResponse();

        try {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> response = (HashMap<String, Object>) soapClient.callOfbizService("findProducts");

            res.setResponse(response);

            return res;

        } catch (Exception e) {
            errorMsg.put("errormsg", e.getMessage());
            res.setResponse(errorMsg);
            return res;
        }
    }

    @PostMapping(path = "/findProductStore")
    public Object findProductStore(@RequestBody Hashtable<String, String> params) {
        HashMap<String, Object> errorMsg = new HashMap<String, Object>();
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);

        for (String key : params.keySet()) {
            soapClient.addRequestParameter(key, params.get(key));
        }
        HashMapResponse res = new HashMapResponse();

        try {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> response = (HashMap<String, Object>) soapClient.callOfbizService("findProductStore");

            res.setResponse(response);

            return res;

        } catch (Exception e) {
            errorMsg.put("errormsg", e.getMessage());
            res.setResponse(errorMsg);
            return res;
        }
    }
}
