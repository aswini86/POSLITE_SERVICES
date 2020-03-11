package com.retail.services.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.services.soap.SoapClient;
import com.retail.services.util.CollectionUtil;
import com.retail.services.util.CommonUtil;
import com.retail.services.util.HashMapResponse;

@RestController
public class Customer {

    @Autowired
    ApplicationContext context;

    @SuppressWarnings("unchecked")
    @PostMapping(path = "/createCustomer")
    public Object quickCreateCustomer(@RequestBody Map<Object, Object> map) {
        Map<Object, Object> response = new HashMap<Object, Object>();
        try {
        	
        	 //Map<String, Object> customer = (Map<String, Object>)findCustomer(map);
           /*  if(customer.get("message").equals("success") ) {
             	response.put("message", "contact_number_already_exist");
                 return response;
             }*/
             
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addRequestParameter("firstName", map.get("firstName"));
            soapClient.addRequestParameter("lastName", map.get("lastName"));
            soapClient.addRequestParameter("emailAddress", map.get("emailAddress"));
            soapClient.addRequestParameter("address1", map.get("address1"));
            soapClient.addRequestParameter("city", map.get("city"));
            soapClient.addRequestParameter("postalCode", map.get("postalCode"));
            soapClient.addRequestParameter("stateProvinceGeoId", map.get("stateProvinceGeoId"));
            soapClient.addRequestParameter("contactNumber", map.get("contactNumber"));
            soapClient.addRequestParameter("roleTypeId", "CUSTOMER");
            soapClient.addRequestParameter("productStoreId", map.get("productStoreId"));
            //Map<String, Object> quickCreateCustomerResponse = soapClient.callOfbizService("quickCreateCustomer");
            Map<String, Object> quickCreateCustomerResponse = soapClient.callOfbizService("createUpdateCustomer");
            
            /*if (quickCreateCustomerResponse.get("partyId") != null) {
                try {
                    soapClient.setRequestBody(new HashMap<>());
                    soapClient.addRequestParameter("login.username", map.get("login.username"));
                    soapClient.addRequestParameter("login.password", map.get("login.password"));
                    soapClient.addRequestParameter("contactMechId", CommonUtil.getRandomString(5));
                    
                   
                    soapClient.addRequestParameter("contactNumber", map.get("contactNumber"));
                    soapClient.addRequestParameter("partyId", quickCreateCustomerResponse.get("partyId"));
                    soapClient.addRequestParameter("roleTypeId", "CUSTOMER");
                    soapClient.callOfbizService("createPartyTelecomNumber");

                } catch (Exception e) {
                    response.put("message", "error");
                    response.put("error_message", e.getMessage());

                    return response;
                }

            }*/
            response.put("message", "success");
            response.put("partyId", quickCreateCustomerResponse.get("partyId"));

            return response;
        } catch (Exception e) {
            response.put("message", "error");
            response.put("error_message", e.getMessage());
            return response;
        }
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findCustomer")
    public Object findCustomer(@RequestBody Map<Object, Object> map) {
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("phoneNumber", map.get("contactNumber"));
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("findCustomersByPhoneNumber");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        res.setResponse(response);
        response.put("message", "success");
        return res;
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findCustomerBO")
    public Object findCustomerBO(@RequestBody Map<Object, Object> map) {
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("phoneNumber", map.get("contactNumber"));
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        List<Object> finalCustomerList = new ArrayList<>();
        try {
            response = soapClient.callOfbizService("findCustomersByPhoneNumber");
            List<?> customerList = CollectionUtil.convertObjectToList(response.get("customerList"));
            HashMap<Object, Object> hashMap = null;
            
            for(int i = 0; i < customerList.size(); i++) {
            	hashMap = new HashMap<>();
            	hashMap = (HashMap<Object, Object>) customerList.get(i);
            	finalCustomerList.add(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        res.setResponse(response);
        response.put("message", "success");
        return finalCustomerList;
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findPosCustomers")
    public Object findPosCustomers(@RequestBody Map<Object, Object> map) {
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("phoneNumber", map.get("contactNumber"));
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        List<Object> finalCustomerList = new ArrayList<>();
        try {
            response = soapClient.callOfbizService("findPosCustomers");
            List<?> customerList = CollectionUtil.convertObjectToList(response.get("customerList"));
            HashMap<Object, Object> hashMap = null;
            
            for(int i = 0; i < customerList.size(); i++) {
            	hashMap = new HashMap<>();
            	hashMap = (HashMap<Object, Object>) customerList.get(i);
            	finalCustomerList.add(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        res.setResponse(response);
        response.put("message", "success");
        return finalCustomerList;
    }
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value = "/findCustomerBO") public Object
	 * findCustomerBO(@RequestBody Map<Object, Object> map) { SoapClient soapClient
	 * = (SoapClient) context.getBean(SoapClient.class);
	 * soapClient.addRequestParameter("phoneNumber", map.get("contactNumber"));
	 * Map<String, Object> response = new HashMap<>(); HashMapResponse res = new
	 * HashMapResponse(); try { response =
	 * soapClient.callOfbizService("findCustomerByContactNumber"); } catch
	 * (Exception e) { e.printStackTrace(); response.put("message", "error"); return
	 * response; } res.setResponse(response); response.put("message", "success");
	 * return res; }
	 */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findBarCode")
    public Object findBarCode(@RequestBody Map<Object, Object> map) {
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("productStoreId", map.get("productStoreId"));
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        List<Object> finalBarcodeList = new ArrayList<>();
        try {
            response = soapClient.callOfbizService("findProductNameAndBarcode");
            List<?> barcodeList = CollectionUtil.convertObjectToList(response.get("barcodeList"));
            HashMap<Object, Object> hashMap = null;
            
            for(int i = 0; i < barcodeList.size(); i++) {
            	hashMap = new HashMap<>();
            	hashMap = (HashMap<Object, Object>) barcodeList.get(i);
            	finalBarcodeList.add(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        res.setResponse(response);
        response.put("message", "success");
        return finalBarcodeList;
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/createGlobalProduct")
    public Object createGlobalProduct(@RequestBody Map<Object, Object> map) {
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("barcode", map.get("barcode"));
        soapClient.addRequestParameter("productName", map.get("productName"));
        soapClient.addRequestParameter("mrp", map.get("mrp"));
        soapClient.addRequestParameter("sp", map.get("sp"));
        Map<String, Object> response = new HashMap<>();
        String productId = "";
        try {
            response = soapClient.callOfbizService("createGlobalProduct");
            productId = (String) response.get("productId");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        response.put("message", "success");
        response.put("productId", productId);
        
        return response;
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/addGlobalProduct")
    public Object addGlobalProduct(@RequestBody Map<Object, Object> map) {
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("barcode", map.get("barcode"));
        soapClient.addRequestParameter("productName", map.get("productName"));
        soapClient.addRequestParameter("mrp", map.get("mrp"));
        soapClient.addRequestParameter("sp", map.get("sp"));
        Map<String, Object> response = new HashMap<>();
        String productId = "";
        try {
            response = soapClient.callOfbizService("addGlobalProduct");
            productId = (String) response.get("productId");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        response.put("message", "success");
        response.put("productId", productId);
        
        return response;
    }
    @SuppressWarnings("unchecked")
    @PostMapping(path = "/syncCustomerToServer")
    public Object syncCustomerToServer(@RequestBody Map<String, Object> map) {
        Map<Object, Object> response = new HashMap<Object, Object>();
        try {
        	
        	SoapClient syncSoapClient = (SoapClient) context.getBean(SoapClient.class);
        	Map<String, Object> getSyncCustomerResponse = syncSoapClient.callOfbizService("getCustomerSyncData");
        	if(getSyncCustomerResponse != null) {
        		System.out.println("getSyncCustomerResponse---"+getSyncCustomerResponse);
        	}
            /*SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addRequestParameter("firstName", map.get("firstName"));
            soapClient.addRequestParameter("lastName", map.get("lastName"));
            soapClient.addRequestParameter("emailAddress", map.get("emailAddress"));
            soapClient.addRequestParameter("address1", map.get("address1"));
            soapClient.addRequestParameter("city", map.get("city"));
            soapClient.addRequestParameter("postalCode", map.get("postalCode"));
            soapClient.addRequestParameter("stateProvinceGeoId", map.get("stateProvinceGeoId"));
            Map<String, Object> quickCreateCustomerResponse = soapClient.callOfbizService("quickCreateCustomer");*/

            /*if (quickCreateCustomerResponse.get("partyId") != null) {
                try {
                    soapClient.setRequestBody(new HashMap<>());
                    soapClient.addRequestParameter("login.username", map.get("login.username"));
                    soapClient.addRequestParameter("login.password", map.get("login.password"));
                    soapClient.addRequestParameter("contactMechId", CommonUtil.getRandomString(5));
                    
                   
                    soapClient.addRequestParameter("contactNumber", map.get("contactNumber"));
                    soapClient.addRequestParameter("partyId", quickCreateCustomerResponse.get("partyId"));
                    soapClient.addRequestParameter("roleTypeId", "CUSTOMER");
                    soapClient.callOfbizService("createPartyTelecomNumber");

                } catch (Exception e) {
                    response.put("message", "error");
                    response.put("error_message", e.getMessage());

                    return response;
                }

            }*/
            response.put("message", "success");
            //response.put("partyId", quickCreateCustomerResponse.get("partyId"));

            return response;
        } catch (Exception e) {
            response.put("message", "error");
            response.put("error_message", e.getMessage());
            return response;
        }
    }
}
