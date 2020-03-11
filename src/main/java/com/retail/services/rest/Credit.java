package com.retail.services.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.retail.services.soap.SoapClient;
import com.retail.services.util.HashMapResponse;

@RestController
public class Credit {
	@Autowired
    ApplicationContext context;
	
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/createPosRetailorCredit")
    public Object createCredit(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("customerMobileNum", map.get("customerMobileNum"));
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        soapClient.addRequestParameter("creditAmount", map.get("creditAmount"));
        soapClient.addRequestParameter("productStoreId", map.get("productStoreId"));
        soapClient.addRequestParameter("billId", map.get("billId"));
        soapClient.addRequestParameter("dayId", map.get("dayId"));
        soapClient.addRequestParameter("type", map.get("type"));
        soapClient.addRequestParameter("retailer", map.get("retailer"));
        soapClient.addRequestParameter("customer", map.get("customer"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("createPosRetailorCredit");
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
    @RequestMapping(value = "/createPosCustomerCredit")
    public Object createPosCustomerCredit(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("customerMobileNum", map.get("customerMobileNum"));
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        soapClient.addRequestParameter("paidAmount", map.get("paidAmount"));
        soapClient.addRequestParameter("productStoreId", map.get("productStoreId"));
        soapClient.addRequestParameter("billId", map.get("billId"));
        soapClient.addRequestParameter("dayId", map.get("dayId"));
        soapClient.addRequestParameter("type", map.get("type"));
        soapClient.addRequestParameter("retailer", map.get("retailer"));
        soapClient.addRequestParameter("customer", map.get("customer"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("createPosCustomerCredit");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	
	// Update Credit Limit Method Starts From Here
	@RequestMapping(value = "/updateCreditLimit")
    public Object updateCreditLimit(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("partyId", map.get("partyId"));
        soapClient.addRequestParameter("creditLimit", map.get("creditLimit"));	
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("updateCreditLimit");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	
	@RequestMapping(value = "/getCustomerCreditTxns")
    public Object getCustomerCreditTxns(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getCustomerCreditTxns");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	@RequestMapping(value = "/getCustomerTotalCreditAmt")
    public Object getCustomerTotalCreditAmt(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        soapClient.addRequestParameter("contactNumber", map.get("contactNumber"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getCustomerTotalCreditAmt");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	@RequestMapping(value = "/getCustomerTotalCreditDue")
    public Object getCustomerTotalCreditDue(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        soapClient.addRequestParameter("contactNumber", map.get("contactNumber"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getCustomerTotalCreditDue");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	@RequestMapping(value = "/getCustomerCreditLimitAmt")
    public Object getCustomerCreditLimitAmt(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        soapClient.addRequestParameter("contactNumber", map.get("contactNumber"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getCustomerCreditLimit");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	@RequestMapping(value = "/getActiveCreditNoteList")
    public Object getActiveCreditNoteList(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("contactNumber", map.get("contactNumber"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getActiveCreditNoteList");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	@RequestMapping(value = "/getCreditNoteAmtByCreditId")
    public Object getCreditNoteAmtByCreditId(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("creditId", map.get("creditId"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getCreditNoteAmtByCreditId");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	@RequestMapping(value = "/getCustomerCreditTakenTxns")
    public Object getCustomerCreditTakenTxns(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getCustomerCreditTakenTxns");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
	@RequestMapping(value = "/getCustomerPrevBill")
    public Object getCustomerPrevBill(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("customerId", map.get("customerId"));
        soapClient.addRequestParameter("contactNumber", map.get("contactNumber"));
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getCustomerPrevBill");
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
