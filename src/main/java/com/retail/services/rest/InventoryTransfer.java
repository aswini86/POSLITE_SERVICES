package com.retail.services.rest;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.services.soap.SoapClient;
import com.retail.services.util.HashMapResponse;




@RestController
@RequestMapping("/bo")
public class InventoryTransfer {
	
	@Autowired
	ApplicationContext context;
	
	@PostMapping(path="/uploadInventory",consumes="application/json",produces="application/json")
	public Boolean uploadInventory()  {
		Hashtable<String, String> params = new Hashtable<String, String>();
		//delegator.userLogin(params); //TODO check login result
		return true;
	}

	
	@PostMapping(path="/transferInventory/create")
	public Object transferInventoryItem(@RequestBody Hashtable<Object, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		 SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for (Object key : params.keySet()) {
			soapClient.addRequestParameter((String)key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("createInventoryTransfer");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/transferInventory/check")
	public Object checkTransferInventoryItem(@RequestBody Hashtable<Object, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		 SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for (Object key : params.keySet()) {
			soapClient.addRequestParameter((String)key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("prepareInventoryTransfer");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	
	@PostMapping(path="/transferInventory/complete")
	public Object completeInventoryItemTransfer(@RequestBody Hashtable<Object, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for (Object key : params.keySet()) {
			soapClient.addRequestParameter((String)key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("completeInventoryTransfer");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/transferInventory/cancel")
	public Object cancelInventoryItemTransfer(@RequestBody Hashtable<Object, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for (Object key : params.keySet()) {
			soapClient.addRequestParameter((String)key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("cancelInventoryTransfer");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(path="/find/inventoryItem")
	public Object findInventoryItem(@RequestBody Hashtable<Object, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for (Object key : params.keySet()) {
			soapClient.addRequestParameter((String)key, params.get(key));
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
	
	
	
	@PostMapping(path="/find/inventoryTransfer")
	public Object findTransferInventory(@RequestBody Hashtable<Object, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = new OfbizService();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for (Object key : params.keySet()) {
			soapClient.addRequestParameter((String)key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("findInventoryTransfer");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	
	
	@PostMapping(path="/findFacilities")
	public Object findFacility(@RequestBody Hashtable<Object, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for (Object key : params.keySet()) {
			soapClient.addRequestParameter((String)key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("findFacilities");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	@PostMapping(path="/findStoreFacilities")
	public Object findStoreFacilities(@RequestBody Hashtable<Object, Object> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		//OfbizService ofbizService = (OfbizService) context.getBean(OfbizService.class);
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for (Object key : params.keySet()) {
			soapClient.addRequestParameter((String)key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("findProductStoreFacility");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
	
	
	@SuppressWarnings("unchecked")
    @PostMapping(value = "/getGoodIdentification")
	public Object getPartyMapping(@RequestBody Map<Object, Object> map) {

		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		soapClient.addRequestParameter("entityName", "GoodIdentification");
		HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
//		hashMap.put("productId",map.get("productId"));
		hashMap.put("noConditionFind", "Y");
		soapClient.addRequestParameter("inputFields", hashMap);
		
		HashMap<String, Object> response = null;
		try {
			response = (HashMap<String, Object>) soapClient.callOfbizService("performFindList");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.get("list");
	}
}
