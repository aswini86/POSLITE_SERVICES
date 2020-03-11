package com.retail.services.rest;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.services.soap.SoapClient;
import com.retail.services.util.CommonUtil;
import com.retail.services.util.HashMapResponse;

@RestController
@RequestMapping("/bo")
public class Stock {

	@Autowired
	ApplicationContext context;
	
	@PostMapping(path="/getStockAdjustmentList")
	public Object getStockAdjustmentList(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("findInventoryItem");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	@PostMapping(path="/createInventoryItemVariance")
	public Object createInventoryItemVariance(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createInventoryVarianceTxn");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/approveInventoryItemVariance")
	public Object approveInventoryItemVariance(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("approveInventoryVarianceTxn");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	@PostMapping(path="/updateInventoryItemVariance")
	public Object updateInventoryItemVariance(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createUpdateInventoryVarianceTxn");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	@PostMapping(path="/getStockAdjustmentTrxList")
	public Object getStockAdjustmentTrxList(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("findStockAdjustmentTxnList");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
}
