package com.retail.services.rest;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.services.soap.SoapClient;
import com.retail.services.util.HashMapResponse;

@RestController
@RequestMapping("/bo")
public class RateAdjustment {

    @Autowired
    ApplicationContext context;

    @PostMapping(path="/getRateAdjustmentList")
	public Object getRateAdjustmentList(@RequestBody Hashtable<String, Object> params) {		
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
    
    @PostMapping(path="/getRateAdjustmentTrxList")
	public Object getRateAdjustmentTrxList(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("findProductPriceRuleTxnList");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	/*
	 * @PostMapping(path="/performFind") public Object performFind(@RequestBody
	 * Hashtable<String, Object> params) { HashMap<String, Object> errorMsg = new
	 * HashMap<String, Object>(); SoapClient soapClient = (SoapClient)
	 * context.getBean(SoapClient.class);
	 * 
	 * for(String key : params.keySet()) { soapClient.addRequestParameter(key,
	 * params.get(key)); } HashMapResponse res = new HashMapResponse();
	 * 
	 * try {
	 * 
	 * @SuppressWarnings("unchecked") HashMap<String, Object> response =
	 * (HashMap<String, Object>)soapClient.callOfbizService("performFindList");
	 * 
	 * res.setResponse(response);
	 * 
	 * return res;
	 * 
	 * } catch (Exception e) { errorMsg.put("errormsg", e.getMessage());
	 * res.setResponse(errorMsg); return res; } }
	 */
    
    @PostMapping(path="/findRateProductPrice")
	public Object findRateProductPrice(@RequestBody Hashtable<String, Object> params) {
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("getProductPriceInfo");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/createProductPriceRule")
	public Object createProductPriceRule(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createProductPriceRule");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/createProductPriceCond")
	public Object createProductPriceCond(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createProductPriceCond");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/createProductPriceAction")
	public Object createProductPriceAction(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createProductPriceAction");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/findStoreCategory")
	public Object findStoreCategory(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("getFacilityCategoryInfo");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/createProductPriceTrx")
	public Object createProductPriceTrx(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createProductPriceTrx");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/updateProductPriceTrx")
	public Object updateProductPriceTrx(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("updateProductPriceTrx");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/createProductPriceTrxRule")
	public Object createProductPriceTrxRule(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createProductPriceTrxRule");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/updateProductPriceTrxRule")
	public Object updateProductPriceTrxRule(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("updateProductPriceRuleTrx");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/createProductPriceTrxCond")
	public Object createProductPriceTrxCond(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createProductPriceTrxCond");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/updateProductPriceTrxCond")
	public Object updateProductPriceTrxCond(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("updateProductPriceCondTrx");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/createProductPriceTrxAction")
	public Object createProductPriceTrxAction(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createProductPriceTrxAction");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/updateProductPriceTrxAction")
	public Object updateProductPriceTrxAction(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("updateProductPriceActionTrx");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    @PostMapping(path="/getRateAdjustmentTxnId")
	public Object getRateAdjustmentTxnId(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("getRateAdjustmentTxnId");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/approveProductPriceTxn")
	public Object approveProductPriceTxn(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("approveProductPriceTxn");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/saveUpdateProductPriceTxn")
	public Object saveUpdateProductPriceTxn(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("saveUpdateProductPriceTxn");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    @PostMapping(path="/createRateAdjustmentTrx")
	public Object createRateAdjustmentTrx(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createRateAdjustmentTrx");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    
    @PostMapping(path="/findInventoryArticles")
	public Object findInventoryArticles(@RequestBody Hashtable<String, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("getProductInventoryInfo");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
}
