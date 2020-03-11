package com.retail.services.controllers;

import com.retail.services.services.PartyService;
import com.retail.services.soap.SoapClient;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.retail.services.rest.Customer;

@RestController
public class PartyController {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private Environment environment;
    
    @SuppressWarnings({"unchecked"})
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);
        
        SoapClient soapClient = (SoapClient) applicationContext.getBean(SoapClient.class);
        soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));
        PartyService partyService = applicationContext.getBean(PartyService.class);
        
        try {
        	//code for syncing Customer
            //Map<String, Object> syncCustomerDetails= partyService.syncCustomerToServer(soapClient);
            
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }
        try {
        	//code for syncing Product
            //Map<String, Object> syncProductDetails= partyService.syncProductToServer(soapClient);
            
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }
        try {
        	//code for syncing Pos metadata to server
            //Map<String, Object> syncPosMetadataDetails= partyService.syncPosMetadataToServer(soapClient);
            
            //code for syncing Cart transaction
            //Map<String, Object> syncCartTxnDetails= partyService.syncCartTransactionToServer(soapClient);
            
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }
        try {
            
            Map<String, Object> party= partyService.login(soapClient);

            if (party.isEmpty()) {
                responseHeader.put("status", "INVALID_CREDENTIALS");
                responseHeader.put("message", "Invalid id or password");
            } else {
                responseHeader.put("status", "LOGGED_IN");
                responseHeader.put("message", "Party found.");
                response.put("responseBody", party);
            }
            //Map<String, Object> syncCustomerDetails= partyService.syncCustomerToServer(soapClient);
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}
