package com.retail.services.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.services.soap.SoapClient;
import com.retail.services.util.HashMapResponse;

@RestController
public class CreditNote {
	
	@Autowired
    ApplicationContext context;
	
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/createPosCustomerCreditNote")
    public Object createPosCustomerCreditNote(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("creditId", map.get("creditId"));
        soapClient.addRequestParameter("customerMobileNum", map.get("customerMobileNum"));
        soapClient.addRequestParameter("productStoreId", map.get("productStoreId"));
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        soapClient.addRequestParameter("billId", map.get("billId"));
        soapClient.addRequestParameter("paidAmount", map.get("paidAmount"));
        //soapClient.addRequestParameter("balanceAmount", map.get("balanceAmount"));
        soapClient.addRequestParameter("dayId", map.get("dayId"));
        soapClient.addRequestParameter("customer", map.get("customer"));
        soapClient.addRequestParameter("retailer", map.get("retailer"));
        soapClient.addRequestParameter("type", map.get("type"));
        
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("createPosCustomerCreditNote");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/getBillItems")
    public Object getBillItems(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("receiptId", map.get("receiptId"));
        soapClient.addRequestParameter("productStoreGroupId", map.get("productStoreGroupId"));
        soapClient.addRequestParameter("currencyUomId", map.get("currencyUomId"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getBillItems");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/createIssueCreditNote")
    public Object createIssueCreditNote(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        
        soapClient.addRequestParameter("login.username", map.get("login.username"));
        soapClient.addRequestParameter("login.password", map.get("login.password"));
        soapClient.addRequestParameter("facilityId", map.get("facilityId"));
        soapClient.addRequestParameter("productId", map.get("productId"));
        soapClient.addRequestParameter("billId", map.get("billId"));
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        soapClient.addRequestParameter("returnQuantity", map.get("returnQuantity"));
        soapClient.addRequestParameter("productPrice", map.get("productPrice"));
        soapClient.addRequestParameter("productStoreId", map.get("productStoreId"));
        soapClient.addRequestParameter("creditNoteAmount", map.get("creditNoteAmount"));
        soapClient.addRequestParameter("dayId", map.get("dayId"));
        soapClient.addRequestParameter("customer", map.get("customer"));
        soapClient.addRequestParameter("retailer", map.get("retailer"));
        soapClient.addRequestParameter("type", map.get("type"));
        
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("createIssueCreditNote");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	@RequestMapping(value = "/getCustomerCreditNoteAmt")
    public Object getCustomerCreditNoteAmt(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        soapClient.addRequestParameter("contactNumber", map.get("contactNumber"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getCustomerCreditNoteAmt");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	@RequestMapping(value = "/getCustomerCreditNoteTxns")
    public Object getCustomerCreditNoteTxns(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getCustomerCreditNoteTxns");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
}
