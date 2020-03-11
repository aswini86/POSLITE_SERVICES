package com.retail.services.services;

import com.retail.services.soap.SoapClient;
import com.retail.services.util.CollectionUtil;
import com.retail.services.util.CommonUtil;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class PartyService {

    public Map<String, Object> login(SoapClient soapClient) throws Exception {
        Map<String, Object> party = new HashMap<>();

        Map<String, Object> response = soapClient.callOfbizService("userLogin");
        
        ArrayList facilityArray = new ArrayList<String>();
        Map<String, Object> facilityTerminalMap = new HashMap<>();
        ArrayList facilityTerminalArray = new ArrayList<String>();
        if (response.containsKey("userLogin")) {
            Map<String, Object> userLogin = (Map) response.get("userLogin");
            String partyId = (String) userLogin.get("partyId");
            party.put("partyId", partyId);
            party.put("productStores", getProductStores(soapClient, partyId));
            //get Facilities
            HashMap<String, Object> facilityList = (HashMap<String, Object>) getPartyFacility(soapClient, partyId);
            facilityArray = (ArrayList<String>) facilityList.get("facilityArray");
            party.put("partyFacilities", facilityArray);
            //get terminals
            facilityTerminalMap = (Map<String, Object>) facilityList.get("facilityTerminals");
            //facilityTerminalArray = (ArrayList<String>) facilityList.get("facilityTerminals");
            List<Map<String, Object>> output = (List<Map<String, Object>>) facilityTerminalMap.get("facilityTerminalMap");
            
            party.put("productStoreTerminals", output);
        }

        return party;
    }
    
    private List<Map<String, Object>> getProductStores(SoapClient soapClient, String partyId) throws Exception {
        List<Map<String, Object>> productStores = new ArrayList<>();
        
        Object[] storeRoles = getProductStoreRole(soapClient, partyId);        
        for(int i=0;i<storeRoles.length;i++){
            Map<String, Object> storeRole = (Map)storeRoles[i];
            soapClient.addRequestParameter("searchByProductStoreId", storeRole.get("productStoreId").toString());
            Map<String, Object> productStore = (Map)((Object[])soapClient.callOfbizService("findProductStore").get("productStoreList"))[0];
            productStore.put("roleType", storeRole.get("roleType"));
            
            productStores.add(productStore);
        }
        
        return productStores;
    }

    private Object[] getProductStoreRole(SoapClient soapClient, String partyId) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("entityName", "ProductStoreRole");
        Map<String, Object> inputFields = new HashMap<>();
        requestBody.put("inputFields", inputFields);
        inputFields.put("partyId", partyId);
        
        soapClient.setRequestBody(requestBody);
        Object[] storesRoles = (Object[])soapClient.callOfbizService("performFindList").get("list");
        return storesRoles;
    }
    
    private HashMap<String, Object> getPartyFacility(SoapClient soapClient, String partyId) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        HashMap<String, Object> result = new HashMap<>();
        
        ArrayList<String> facilityArray = new ArrayList<String>();
        ArrayList<List<Map<String, Object>>> facilityTerminalArray = new ArrayList<List<Map<String, Object>>>();
        HashMap<String, Object> facilityTerminals = new HashMap<String, Object>();
        requestBody.put("entityName", "FacilityParty");
        Map<String, Object> inputFields = new HashMap<>();
        requestBody.put("inputFields", inputFields);
        inputFields.put("partyId", partyId);
        
        soapClient.setRequestBody(requestBody);
        Object[] facilityParties = (Object[]) soapClient.callOfbizService("performFindList").get("list");
        if(facilityParties != null) {
        	for(int i=0;i<facilityParties.length;i++){
        		Map<String, Object> facilityParty = (Map)facilityParties[i];
            	facilityArray.add((String) facilityParty.get("facilityId"));
            	facilityTerminals = getFacilityTerminals(soapClient,(String) facilityParty.get("facilityId"));
            	//facilityTerminalArray.addAll((Collection<? extends List<Map<String, Object>>>) facilityTerminals);
        	}
        }
        result.put("facilityArray", facilityArray);
        //result.put("facilityTerminalArray", facilityTerminalArray);
        result.put("facilityTerminals", facilityTerminals);
        return result;
    }
    
    private HashMap<String, Object> getFacilityTerminals(SoapClient soapClient, String facilityId) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        HashMap<String, Object> result = new HashMap<>();
        String posTerminalId = "";
        List<Map<String, Object>> termDayDetails = new ArrayList<Map<String,Object>>();
        ArrayList<String> facilityTerminalArray = new ArrayList<String>();
        ArrayList<Map<String, Object>> facilityTerminalDetailsArray = new ArrayList<Map<String, Object>>();
        requestBody.put("entityName", "PosTerminal");
        Map<String, Object> inputFields = new HashMap<>();
        requestBody.put("inputFields", inputFields);
        inputFields.put("facilityId", facilityId);
        
        soapClient.setRequestBody(requestBody);
        Object[] facilityParties = (Object[]) soapClient.callOfbizService("performFindList").get("list");
        if(facilityParties != null) {
        	for(int i=0;i<facilityParties.length;i++){
        		Map<String, Object> facilityParty = (Map)facilityParties[i];
        		facilityTerminalArray.add((String) facilityParty.get("posTerminalId"));
        		posTerminalId = (String) facilityParty.get("posTerminalId");
        		//Get Terminal Day Details
        		//result.put(posTerminalId, posTerminalId);
        		//result.put("facilityTerminalMap", (List<Map<String, Object>>) getTerminalDayDetails(soapClient, posTerminalId));
        		termDayDetails = (List<Map<String, Object>>) getTerminalDayDetails(soapClient, posTerminalId);
        		facilityTerminalDetailsArray.add(termDayDetails.get(0));
        	}
        }
        result.put("facilityTerminalMap", facilityTerminalDetailsArray);
        return result;
    }
    
    private List<Map<String, Object>> getTerminalDayDetails(SoapClient soapClient, String terminalId) throws Exception {
        List<Map<String, Object>> terminalDayDetails = new ArrayList<>();
        Map<String, Object> dayDetailsMap = new HashMap<String, Object>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String day = formatter.format(date).toString();
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("entityName", "DayDetail");
        Map<String, Object> inputFields = new HashMap<>();
        
        requestBody.put("inputFields", inputFields);
        inputFields.put("terminalId", terminalId);
        inputFields.put("day", day);
        
        soapClient.setRequestBody(requestBody);
        Object[] daydetails = (Object[])soapClient.callOfbizService("performFindList").get("list");
        if(daydetails != null && daydetails.length >=1) {
        	for(int i=0;i<daydetails.length;i++){
        		dayDetailsMap = (Map)daydetails[i];
        		terminalDayDetails.add(dayDetailsMap);
        	}
        } else {
        	dayDetailsMap.put("terminalId", terminalId);
        	dayDetailsMap.put("adminOpeningDayStatus", "NEW");
        	dayDetailsMap.put("adminClosingDayStatus", "NEW");
        	dayDetailsMap.put("userId", "");
        	terminalDayDetails.add(dayDetailsMap);
        }
        
        return terminalDayDetails;
    }
    public HashMap<String, Object> syncCustomerToServer(SoapClient soapClient) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        
    	Map<String, Object> getSyncCustomerResponse = soapClient.callOfbizService("getCustomerSyncData");
    	List<String> partyIds = new ArrayList<String>();
    	if(getSyncCustomerResponse != null) {
    		List<?> customerList = CollectionUtil.convertObjectToList(getSyncCustomerResponse.get("customerList"));
    		HashMap<Object, Object> hashMap = null;
    		List<Object> finalCustomerList = new ArrayList<>();
    		
    		for(int i = 0; i < customerList.size(); i++) {
            	hashMap = new HashMap<>();
            	hashMap = (HashMap<Object, Object>) customerList.get(i);
            	finalCustomerList.add(hashMap);
            	System.out.println("finalCustomerList.city-----"+hashMap.get("city"));
            	soapClient.addRequestParameter("firstName", hashMap.get("firstName"));
                soapClient.addRequestParameter("lastName", hashMap.get("lastName"));
                soapClient.addRequestParameter("emailAddress", "rsa@gmail.com");
                soapClient.addRequestParameter("address1", hashMap.get("address1"));
                soapClient.addRequestParameter("city", hashMap.get("city"));
                soapClient.addRequestParameter("postalCode", hashMap.get("postalCode"));
                soapClient.addRequestParameter("stateProvinceGeoId", hashMap.get("stateProvinceGeoId"));
                Map<String, Object> quickCreateCustomerResponse = soapClient.callDevOfbizService("quickCreateCustomer");
                if (quickCreateCustomerResponse.get("partyId") != null) {
                    try {
                    	partyIds.add((String) hashMap.get("partyId"));
                    	System.out.println("finalCustomerList.id-----"+hashMap.get("partyId"));
                        soapClient.setRequestBody(new HashMap<>());
                        soapClient.addRequestParameter("login.username", hashMap.get("login.username"));
                        soapClient.addRequestParameter("login.password", hashMap.get("login.password"));
                        soapClient.addRequestParameter("contactMechId", CommonUtil.getRandomString(5));
                        
                       
                        soapClient.addRequestParameter("contactNumber", hashMap.get("contactNumber"));
                        soapClient.addRequestParameter("partyId", quickCreateCustomerResponse.get("partyId"));
                        soapClient.addRequestParameter("roleTypeId", "CUSTOMER");
                        soapClient.callDevOfbizService("createPartyTelecomNumber");

                    } catch (Exception e) {
                    	getSyncCustomerResponse.put("message", "error");
                    	getSyncCustomerResponse.put("error_message", e.getMessage());
                    	result.put("getSyncCustomerResponse", getSyncCustomerResponse);
                    	
                        return result;
                    }
                }
            	//System.out.println("finalCustomerList.name-----"+hashMap.get("partyId"));
            }
    		//Code to update Sync Customer table
    		soapClient.addRequestParameter("partyIds", partyIds.toString());
    		soapClient.callOfbizService("updateSyncCustomer");
    	}
    	
        result.put("getSyncCustomerResponse", getSyncCustomerResponse);
        return result;
    }
    
    public HashMap<String, Object> syncProductToServer(SoapClient soapClient) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        
    	Map<String, Object> getSyncProductResponse = soapClient.callOfbizService("getProductSyncData");
    	List<String> productIds = new ArrayList<String>();
    	if(getSyncProductResponse != null) {
    		List<?> productList = CollectionUtil.convertObjectToList(getSyncProductResponse.get("productList"));
    		HashMap<Object, Object> hashMap = null;
    		List<Object> finalCustomerList = new ArrayList<>();
    		
    		for(int i = 0; i < productList.size(); i++) {
            	hashMap = new HashMap<>();
            	hashMap = (HashMap<Object, Object>) productList.get(i);
            	productIds.add((String) hashMap.get("productId"));
            	finalCustomerList.add(hashMap);
            	soapClient.addRequestParameter("productName", hashMap.get("productName"));
                soapClient.addRequestParameter("mrp", hashMap.get("mrp").toString());
                soapClient.addRequestParameter("sp", hashMap.get("sp").toString());
                soapClient.addRequestParameter("barcode", hashMap.get("barcode"));
                Map<String, Object> pushProductResponse = soapClient.callDevOfbizService("pushProductServer");
                
            }
    		//Code to update Sync Customer table
    		soapClient.addRequestParameter("productIds", productIds.toString());
    		soapClient.callOfbizService("updateSyncProduct");
    	}
    	
        result.put("getSyncProductResponse", getSyncProductResponse);
        return result;
    }
    public HashMap<String, Object> syncPosMetadataToServer(SoapClient soapClient) throws Exception {
    	HashMap<String, Object> result = new HashMap<>();
        
    	Map<String, Object> getPosFacilityResponse = soapClient.callOfbizService("getPosFacilitySyncData");
    	if(getPosFacilityResponse != null) {
    		List<?> posFacilityList = CollectionUtil.convertObjectToList(getPosFacilityResponse.get("posFacilityList"));
    		HashMap<Object, Object> hashMap = null;
    		
    		for(int i = 0; i < posFacilityList.size(); i++) {
    			hashMap = new HashMap<>();
            	hashMap = (HashMap<Object, Object>) posFacilityList.get(i);
            	
    			soapClient.addRequestParameter("facilityTypeId", hashMap.get("facilityTypeId"));
    			soapClient.addRequestParameter("facilityName", hashMap.get("facilityName"));
    			soapClient.addRequestParameter("defaultInventoryItemTypeId", hashMap.get("defaultInventoryItemTypeId"));
    			soapClient.addRequestParameter("ownerPartyId", hashMap.get("ownerPartyId"));
    		}
    		
    		Map<String, Object> getPrdStoreResponse = soapClient.callOfbizService("getProductStoreSyncData");
    		List<?> productStoreList = CollectionUtil.convertObjectToList(getPrdStoreResponse.get("productStoreList"));
    		HashMap<Object, Object> prdStoreHashMap = null;
    		
    		for(int i = 0; i < productStoreList.size(); i++) {
    			prdStoreHashMap = new HashMap<>();
    			prdStoreHashMap = (HashMap<Object, Object>) productStoreList.get(i);
            	
    			soapClient.addRequestParameter("storeName", prdStoreHashMap.get("storeName"));
    			soapClient.addRequestParameter("companyName", prdStoreHashMap.get("companyName"));
    		}
    		
    		Map<String, Object> getPSGResponse = soapClient.callOfbizService("getPSGSyncData");
    		List<?> prdStoreGrpList = CollectionUtil.convertObjectToList(getPSGResponse.get("prdStoreGrpList"));
    		HashMap<Object, Object> psgMap = null;
    		
    		for(int i = 0; i < productStoreList.size(); i++) {
    			psgMap = new HashMap<>();
    			psgMap = (HashMap<Object, Object>) prdStoreGrpList.get(i);
            	
    			soapClient.addRequestParameter("description", psgMap.get("description"));
    		}
    		
    		Map<String, Object> getPosTerminalResponse = soapClient.callOfbizService("getPosTerminalSyncData");
    		
    		for(int i = 0; i < productStoreList.size(); i++) {
            	
    			soapClient.addRequestParameter("facilityTypeId", hashMap.get("facilityTypeId"));
    			soapClient.addRequestParameter("facilityName", hashMap.get("facilityName"));
    			soapClient.addRequestParameter("defaultInventoryItemTypeId", hashMap.get("defaultInventoryItemTypeId"));
    			soapClient.addRequestParameter("ownerPartyId", hashMap.get("ownerPartyId"));
    			soapClient.addRequestParameter("storeName", prdStoreHashMap.get("storeName"));
    			soapClient.addRequestParameter("companyName", prdStoreHashMap.get("companyName"));
    			soapClient.addRequestParameter("description", psgMap.get("description"));
    		}
    		Map<String, Object> pushProductResponse = soapClient.callDevOfbizService("syncPosMetaData");
    		String facilityId = "";
    		List<?> posTerminalList = CollectionUtil.convertObjectToList(getPosTerminalResponse.get("posTerminalList"));
    		HashMap<Object, Object> posTerminalMap = null;
    		for(int i = 0; i < posTerminalList.size(); i++) {
    			posTerminalMap = new HashMap<>();
    			posTerminalMap = (HashMap<Object, Object>) posTerminalList.get(i);
    			facilityId = (String) pushProductResponse.get("facilityId");
    			soapClient.addRequestParameter("facilityName", hashMap.get("facilityName"));
    			soapClient.addRequestParameter("facilityId", facilityId);
    			soapClient.addRequestParameter("posTerminalId", posTerminalMap.get("posTerminalId"));
    			Map<String, Object> pushPosTerminalResponse = soapClient.callDevOfbizService("syncPosTerminalData");
    		}
    		
    	}
    	//result.put("getSyncProductResponse", getSyncProductResponse);
        return result;
    }
    
    public HashMap<String, Object> syncCartTransactionToServer(SoapClient soapClient) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        
    	Map<String, Object> getSyncCartTxnResponse = soapClient.callOfbizService("getCartSyncData");
    	List<String> receiptIds = new ArrayList<String>();
    	List<String> productIds = new ArrayList<String>();
    	if(getSyncCartTxnResponse != null) {
    		List<?> cartTxnList = CollectionUtil.convertObjectToList(getSyncCartTxnResponse.get("cartTxnList"));
    		HashMap<Object, Object> cartTxnMap = null;
    		
    		for(int i = 0; i < cartTxnList.size(); i++) {
    			
    			cartTxnMap = new HashMap<>();
    			cartTxnMap = (HashMap<Object, Object>) cartTxnList.get(i);
    			receiptIds.add((String) cartTxnMap.get("posReceiptId"));
            	soapClient.addRequestParameter("posReceiptId", cartTxnMap.get("posReceiptId"));
                soapClient.addRequestParameter("dayId", cartTxnMap.get("dayId"));
                soapClient.addRequestParameter("terminalId", cartTxnMap.get("terminalId"));
                soapClient.addRequestParameter("posStatus", cartTxnMap.get("posStatus"));
                soapClient.addRequestParameter("customerMobileNo", cartTxnMap.get("customerMobileNo"));
                Map<String, Object> pushProductResponse = soapClient.callDevOfbizService("pushCartTxnServer");
                
            }
    		//Code to update Sync Customer table
    		//soapClient.callOfbizService("updateSyncProduct");
    		List<?> cartTxnItemList = CollectionUtil.convertObjectToList(getSyncCartTxnResponse.get("cartTxnItemList"));
    		HashMap<Object, Object> cartTxnItemMap = null;
    		
    		for(int i = 0; i < cartTxnItemList.size(); i++) {
    			cartTxnItemMap = new HashMap<>();
    			cartTxnItemMap = (HashMap<Object, Object>) cartTxnItemList.get(i);
    			productIds.add((String) cartTxnItemMap.get("productId"));
    			//code to get new Receiptid
            	soapClient.addRequestParameter("receiptId", cartTxnItemMap.get("receiptId"));
            	soapClient.addRequestParameter("productId", cartTxnItemMap.get("productId"));
            	soapClient.addRequestParameter("productName", cartTxnItemMap.get("productName"));
                soapClient.addRequestParameter("dayId", cartTxnItemMap.get("dayId"));
                soapClient.addRequestParameter("quantity", cartTxnItemMap.get("quantity"));
                soapClient.addRequestParameter("productPrice", cartTxnItemMap.get("productPrice"));
                Map<String, Object> pushProductResponse = soapClient.callDevOfbizService("pushCartItemServer");
                
            }
    		soapClient.addRequestParameter("productIds", productIds.toString());
    		soapClient.callOfbizService("updateSyncProduct");
    		
    		List<?> cartTxnPaymentList = CollectionUtil.convertObjectToList(getSyncCartTxnResponse.get("cartTxnPaymentList"));
    		HashMap<Object, Object> cartTxnPaymentMap = null;
    		
    		for(int i = 0; i < cartTxnPaymentList.size(); i++) {
    			cartTxnPaymentMap = new HashMap<>();
    			cartTxnPaymentMap = (HashMap<Object, Object>) cartTxnPaymentList.get(i);
            	soapClient.addRequestParameter("posCartPaymentId", cartTxnPaymentMap.get("posCartPaymentId"));
                soapClient.addRequestParameter("receiptId", cartTxnPaymentMap.get("receiptId"));
                soapClient.addRequestParameter("dayId", cartTxnPaymentMap.get("dayId"));
                soapClient.addRequestParameter("terminalId", cartTxnPaymentMap.get("terminalId"));
                soapClient.addRequestParameter("paymentType", cartTxnPaymentMap.get("paymentType"));
                soapClient.addRequestParameter("receivedPayment", cartTxnPaymentMap.get("receivedPayment"));
                
                Map<String, Object> pushProductResponse = soapClient.callDevOfbizService("pushCartPaymentServer");
            }
    		List<?> posOrderHeaderList = CollectionUtil.convertObjectToList(getSyncCartTxnResponse.get("posOrderHeaderList"));
    		HashMap<Object, Object> orderHeaderMap = null;
    		
    		for(int i = 0; i < posOrderHeaderList.size(); i++) {
    			orderHeaderMap = new HashMap<>();
    			orderHeaderMap = (HashMap<Object, Object>) posOrderHeaderList.get(i);
            	soapClient.addRequestParameter("orderId", orderHeaderMap.get("orderId"));
                soapClient.addRequestParameter("orderTypeId", orderHeaderMap.get("orderTypeId"));
                soapClient.addRequestParameter("terminalId", orderHeaderMap.get("terminalId"));
                soapClient.addRequestParameter("grandTotal", orderHeaderMap.get("grandTotal"));
                soapClient.addRequestParameter("remainingSubTotal", cartTxnPaymentMap.get("remainingSubTotal"));
                
                Map<String, Object> pushOrderHeaderResponse = soapClient.callDevOfbizService("pushCartOrderHeaderServer");
            }
    		
    		List<?> posOrderItemList = CollectionUtil.convertObjectToList(getSyncCartTxnResponse.get("posOrderItemList"));
    		HashMap<Object, Object> orderItemMap = null;
    		
    		for(int i = 0; i < posOrderItemList.size(); i++) {
    			orderItemMap = new HashMap<>();
    			orderItemMap = (HashMap<Object, Object>) posOrderItemList.get(i);
            	soapClient.addRequestParameter("orderId", orderItemMap.get("orderId"));
                soapClient.addRequestParameter("orderItemSeqId", orderItemMap.get("orderItemSeqId"));
                soapClient.addRequestParameter("productName", orderItemMap.get("productName"));
                soapClient.addRequestParameter("quantity", orderItemMap.get("quantity"));
                soapClient.addRequestParameter("unitPrice", orderItemMap.get("unitPrice"));
                soapClient.addRequestParameter("unitListPrice", orderItemMap.get("unitListPrice"));
                
                Map<String, Object> pushOrderItemResponse = soapClient.callDevOfbizService("pushCartOrderItemServer");
            }
    		soapClient.addRequestParameter("receiptIds", receiptIds.toString());
    		soapClient.callOfbizService("updateSyncCartTxn");
    	}
    	
        return result;
    }
}