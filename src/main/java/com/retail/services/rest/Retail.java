package com.retail.services.rest;

import com.retail.services.soap.SoapClient;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Retail {

    @Autowired
    ApplicationContext context;

    @Autowired
    private Environment environment;

    @SuppressWarnings("unchecked")
    @PostMapping(path = "/retailService")
    public Object retailService(@RequestBody Map<Object, Object> map) {
        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            Map<Object, Object> response = (HashMap<Object, Object>) soapClient.callOfbizService(map);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}