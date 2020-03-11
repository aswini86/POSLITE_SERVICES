package com.retail.services.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
public class Vendor {

	@Autowired
	ApplicationContext context;

	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/findParty")
	public Object findParty(@RequestBody Map<Object, Object> map) {
		try {
			List<Object> finalPartyList = new ArrayList<>();
			SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
			soapClient.addRequestParameter("roleTypeId", map.get("roleTypeId"));
			soapClient.addRequestParameter("lookupFlag", map.get("lookupFlag"));
			soapClient.addRequestParameter("VIEW_SIZE", map.get("VIEW_SIZE"));
			soapClient.addRequestParameter("firstName", map.get("firstName"));

			HashMap<String, Object> response = (HashMap<String, Object>) soapClient.callOfbizService("findParty");
			HashMap<Object, Object> partyAttrmap = (HashMap<Object, Object>) response.get("partyAttrmap");
			List<?> partyList = CollectionUtil.convertObjectToList(response.get("partyList"));
			HashMap<Object, Object> hashMap = null;

			for (int i = 0; i < partyList.size(); i++) {
				hashMap = new HashMap<>();
				hashMap = (HashMap<Object, Object>) partyList.get(i);
				String partyId = (String) hashMap.get("partyId");
				for (Map.Entry<Object, Object> entry : partyAttrmap.entrySet()) {
					if (partyId.equals(entry.getKey())) {
						List<?> attributeList = CollectionUtil.convertObjectToList(entry.getValue());
						for (int j = 0; j < attributeList.size(); j++) {
							HashMap<Object, Object> m1 = (HashMap<Object, Object>) attributeList.get(j);
							if (m1.get("attrName").toString().equalsIgnoreCase("VENDOR_NAME")) {
								hashMap.put("vendorName", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("ADDRESS_1")) {
								hashMap.put("address_1", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("ADDRESS_2")) {
								hashMap.put("address_2", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("ADDRESS_3")) {
								hashMap.put("address_3", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("COUNTRY")) {
								hashMap.put("country", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("STATES")) {
								hashMap.put("states", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("CITY")) {
								hashMap.put("city", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("PinCode")) {
								hashMap.put("pinCode", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("ContactPerson")) {
								hashMap.put("contactPerson", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("Landline")) {
								hashMap.put("landline", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("Mobile")) {
								hashMap.put("mobile", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("EmailAddress")) {
								hashMap.put("email", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("PAN")) {
								hashMap.put("panNumber", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("GSTINType")) {
								hashMap.put("GSTINType", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("GSTIN")) {
								hashMap.put("GSTIN", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("FSSAINumber")) {
								hashMap.put("FSSAINumber", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("FSSAIExpiryDate")) {
								hashMap.put("FSSAIExpiryDate", m1.get("attrValue"));
							}
							if (m1.get("attrName").equals("OrderRequired")) {
								hashMap.put("orderRequired", m1.get("attrValue"));
							}
						}

					}
				}
				finalPartyList.add(hashMap);
				System.out.println(hashMap);
			}

			return finalPartyList;

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@SuppressWarnings("unchecked")
	
	@RequestMapping(value = "/partyMapping")
	public Object partyMapping(@RequestBody Map<Object, Object> map) {
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		try {
			Map<Object, Object> request = (Map<Object, Object>) map.get("requestBody");
			soapClient.addRequestParameter("partyIdFrom", request.get("partyIdFrom"));
			soapClient.addRequestParameter("partyIdTo",request.get("partyIdTo"));
			soapClient.addAuthentication((Map<String, Object>) map.get("credentials"));

			HashMap<String, Object> response = (HashMap<String, Object>) soapClient.callOfbizService("PartyMapping");
			
			return response;

		} catch (Exception e) {
			HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
			try {
				HashMap<String, Object> response = (HashMap<String, Object>) soapClient
						.callOfbizService("findPartyMapping");
				if (response.get("partyMappingList") != null) {
					hashMap.put("message", "Mapping Already Exist");
					return hashMap;
				} else {
					hashMap.put("message", "Unknown problem found!");
					return hashMap;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				hashMap.put("message", "Error found");
				return hashMap;
			}
		}

	}

	@SuppressWarnings("unchecked")
	@PostMapping("/getSupplierProducts")
	public Object getSupplierProducts(@RequestBody Map<Object, Object> map) {
		try {
			List<Object> productList = new ArrayList<>();
			SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
			soapClient.addRequestParameter("partyId", map.get("vendorId"));

			HashMap<String, Object> response = (HashMap<String, Object>) soapClient
					.callOfbizService("getSupplierProducts");

			return response.get("supplierProducts");

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getSupplierProductsAndProduct")
	public Object getSupplierProductsAndProduct(@RequestBody Map<Object, Object> map) {

		Map<Object,Object> vendorItemList = new HashMap<Object,Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		HashMap<Object, Object> hashMap = new HashMap<Object, Object>();

			hashMap.put("partyId",map.get("partyId"));
			soapClient.addRequestParameter("entityName", "SupplierProductAndProduct");
			hashMap.put("primaryProductCategoryId",map.get("primaryProductCategoryId"));
			hashMap.put("manufacturerPartyId",map.get("manufacturerPartyId"));
			hashMap.put("noConditionFind", "Y");
			soapClient.addRequestParameter("inputFields", hashMap);
			soapClient.addRequestParameter("login.username", map.get("login.username"));
			soapClient.addRequestParameter("login.password", map.get("login.password"));
			HashMap<String, Object> response = null;
			
			try {
				response = (HashMap<String, Object>) soapClient.callOfbizService("performFindList");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
		
		return response.get("list");
	}

	@SuppressWarnings("unchecked")
    @PostMapping(value = "/getPartyMapping")
	public Object getPartyMapping(@RequestBody Map<Object, Object> map) {

		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		soapClient.addRequestParameter("entityName", "PartyIdMapping");
		HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
		hashMap.put("partyIdFrom",map.get("partyId"));
		hashMap.put("noConditionFind", "Y");
		soapClient.addRequestParameter("inputFields", hashMap);
		soapClient.addRequestParameter("login.username", map.get("login.username"));
		soapClient.addRequestParameter("login.password", map.get("login.password"));
		HashMap<String, Object> response = null;
		try {
			response = (HashMap<String, Object>) soapClient.callOfbizService("performFindList");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.get("list");
	}
	
	 @SuppressWarnings("unchecked")
	    @PostMapping(value = "/getProductCategory")
		public Object getProductCategory(@RequestBody Map<Object, Object> map) {

			SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
			soapClient.addRequestParameter("entityName", "ProductCategory");
			HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
			hashMap.put("productCategoryId",map.get("productCategoryId"));
			hashMap.put("noConditionFind", "Y");
			soapClient.addRequestParameter("inputFields", hashMap);
			soapClient.addRequestParameter("login.username", map.get("login.username"));
			soapClient.addRequestParameter("login.password", map.get("login.password"));
			HashMap<String, Object> response = null;
			try {
				response = (HashMap<String, Object>) soapClient.callOfbizService("performFindList");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response.get("list");
		}
	    
	    @SuppressWarnings("unchecked")
	    @PostMapping(value = "/createUserSupplierProduct")
		public Object createUserSupplierProduct(@RequestBody Map<Object, Object> request) {

			SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
			HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
			
			
			List<Object> productList = (List<Object>) request.get("productList");
			soapClient.addRequestParameter("login.username", request.get("login.username"));
			soapClient.addRequestParameter("login.password", request.get("login.password"));

//			Map<Object, Object> credentials = (Map<Object, Object>) request.get("credentials");
//			soapClient.addAuthentication(credentials);
			HashMap<String, Object> response = null;
			try {
				for(Object product: productList) {
					HashMap<Object, Object> products = (HashMap<Object, Object>) product;
					soapClient.addRequestParameter("productId", products.get("productId"));
					soapClient.addRequestParameter("partyIdUser", request.get("partyId"));
					soapClient.addRequestParameter("partyIdSupplier", products.get("partyId"));
					response = (HashMap<String, Object>) soapClient.callOfbizService("userSupplierProductMapping");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}
	    
	    
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/getFilterSupplierProductsAndProduct")
		public Object getFilterSupplierProductsAndProduct(@RequestBody Map<Object, Object> map) {

			SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
			HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
			
				hashMap.put("partyId",map.get("partyId"));
				soapClient.addRequestParameter("entityName", "SupplierProductAndProduct");
				hashMap.put("primaryProductCategoryId",map.get("primaryProductCategoryId"));
				hashMap.put("manufacturerPartyId",map.get("manufacturerPartyId"));
				hashMap.put("noConditionFind", "Y");
				soapClient.addRequestParameter("inputFields", hashMap);
				soapClient.addRequestParameter("login.username", map.get("login.username"));
				soapClient.addRequestParameter("login.password", map.get("login.password"));
				HashMap<String, Object> response = null;
				
				try {
					response = (HashMap<String, Object>) soapClient.callOfbizService("performFindList");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
			
			return response.get("list");
		}
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/findVendors")
		public Object findVendors(@RequestBody Hashtable<String, Object> params) {

	    	SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
	    	for(String key : params.keySet()) {
				soapClient.addRequestParameter(key, params.get(key));
			}
	    	
			HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
			HashMapResponse suppliersResponse = new HashMapResponse();
			
			HashMap<String, Object> response = null;
			try {
				response = (HashMap<String, Object>) soapClient.callOfbizService("findSuppliers");
				
				suppliersResponse.setResponse(response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			return suppliersResponse;
		}
	    
	    @PostMapping(path="/findVendorProducts")
		public Object findRateProductPrice(@RequestBody Hashtable<String, Object> params) {
			HashMap<String, Object> errorMsg = new HashMap<String, Object>();
			SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
			
			for(String key : params.keySet()) {
				soapClient.addRequestParameter(key, params.get(key));
			}
			HashMapResponse res = new HashMapResponse();
			
			try {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("findVendorProducts");
				
				res.setResponse(response);
				
				return res;
				
			} catch (Exception e) {
				errorMsg.put("errormsg", e.getMessage());
				res.setResponse(errorMsg);
				return res;
			}
		}
	    
}
