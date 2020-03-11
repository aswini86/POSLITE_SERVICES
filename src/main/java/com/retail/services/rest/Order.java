package com.retail.services.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.services.soap.SoapClient;
import com.retail.services.util.CollectionUtil;
import com.retail.services.util.HashMapResponse;

@RestController
@RequestMapping("/bo")
public class Order {
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
    private Environment env;
	
	@PostMapping(path="/createPurchaseOrder")
	public Object createPurchaseOrder(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createPurchaseOrderTxn");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	@PostMapping(path="/editPurchaseOrder")
	public Object editPurchaseOrder(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("editPurchaseOrderTxn");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	@PostMapping(path="/findPurchaseOrder")
	public Object findPurchaseOrder(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("findPurchaseOrder");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	@PostMapping(path="/findPOItems")
	public Object findPOItems(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("findPurchaseOrderItems");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
}