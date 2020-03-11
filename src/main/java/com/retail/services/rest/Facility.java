package com.retail.services.rest;

import com.retail.services.soap.SoapClient;
import com.retail.services.util.HashMapResponse;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bo")
public class Facility {
	
	@Autowired
    ApplicationContext context;
	
	
	@PostMapping(path="/findFacility")
	public Object findFacility(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("performFindList");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/performFind")
	public Object performFind(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("performFindList");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/createStockCheck")
	public Object createStockCheck(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createWorkEffortAndPartyAssign");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	@PostMapping(path="/findInventoryItem")
	public Object findInventoryItem(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("findInventoryItemAndlocation");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	@PostMapping(path="/getWorkEffort")
	public Object getWorkEffort(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("getWorkEffort");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/updateWorkEffort")
	public Object updateWorkEffort(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("updateWorkEffort");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/createInventoryItemAttr")
	public Object createInventoryItemAttr(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createInventoryItemAttribute");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/updateInventoryItem")
	public Object updateInventoryItem(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("updateInventoryItem");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/deleteInventoryItemAttribute")
	public Object deleteInventoryItemAttribute(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("deleteInventoryItemAttribute");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/createWorkEffortAttribute")
	public Object createWorkEffortAttribute(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createWorkEffortAttribute");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/saveStockCheck")
	public Object saveStockCheck(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createWorkEffort");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/assignStockUser")
	public Object assignStockUser(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("assignPartyToWorkEffort");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
}
