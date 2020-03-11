package com.retail.services.services;

import com.retail.services.soap.SoapClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private ApplicationContext applicationContext;

    public Object[] getInventoryItems(SoapClient soapClient, Map<String, Object> criteria, String productStoreGroupId, String currencyUomId) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("entityName", "InventoryItemAndLocation");
        requestBody.put("inputFields", criteria);
        String expireDate = "",manufacturingDate = "";

        soapClient.setRequestBody(requestBody);
        Object[] inventoryItems = (Object[]) soapClient.callOfbizService("performFindList").get("list");

        for (int i = 0; i < inventoryItems.length; i++) {
            Map<String, Object> inventoryItem = (Map) inventoryItems[i];

            String productId = (String) inventoryItem.get("productId");
            ProductService productService = applicationContext.getBean(ProductService.class);
            inventoryItem.put("ean", productService.getProductEAN(soapClient, productId));
            inventoryItem.put("productPrice", productService.getProductPrice(soapClient, productId, productStoreGroupId, currencyUomId));
            try {
            	if(inventoryItem.get("expireDate") != null) {
            		expireDate = new SimpleDateFormat("dd/MM/yyyy").format((Date) inventoryItem.get("expireDate"));
            	}
            	if(inventoryItem.get("manufacturingDate") != null) {
            		manufacturingDate = new SimpleDateFormat("dd/MM/yyyy").format((Date) inventoryItem.get("manufacturingDate"));
            	}
                inventoryItem.put("expireDate", expireDate);
                inventoryItem.put("manufacturingDate", manufacturingDate);
            } catch (Exception e) {
                
            }
        }
        return inventoryItems;
    }

    public Object[] getUnmappedItems(SoapClient soapClient, Map<String, Object> criteria) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("entityName", "UnmappedItem");
        requestBody.put("inputFields", criteria);
        soapClient.setRequestBody(requestBody);
        Object[] unmappedItems = (Object[]) soapClient.callOfbizService("performFindList").get("list");
        return unmappedItems;
    }

    public void deleteUnmappedItem(SoapClient soapClient, String itemId) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("itemId", itemId);

        soapClient.setRequestBody(requestBody);
        soapClient.callOfbizService("deleteUnmappedItem");
    }

    public Object[] getInventoryItemsByEAN(SoapClient soapClient, String ean, String facilityId, String productStoreGroupId, String currencyUomId) throws Exception {
        Object[] inventoryItems = new Object[0];

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("entityName", "GoodIdentification");
        Map<String, Object> inputFields = new HashMap<>();
        requestBody.put("inputFields", inputFields);
        inputFields.put("goodIdentificationTypeId", "EAN");
        inputFields.put("idValue", ean);

        soapClient.setRequestBody(requestBody);
        Map<Object, Object> map = (Map) soapClient.callOfbizService("performFindList");

        if (((int) map.get("listSize")) > 0) {
            Object[] listObjects = (Object[]) map.get("list");
            Map<String, Object> goodIdentification = (Map) listObjects[0];

            String productId = (String) goodIdentification.get("productId");
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("facilityId", facilityId);
            criteria.put("productId", productId);
            inventoryItems = getInventoryItems(soapClient, criteria, productStoreGroupId, currencyUomId);
        }

        return inventoryItems;
    }

    public String createInventoryItem(SoapClient soapClient, String facilityId, String productStoreGroupId, String productId,
    		String inventoryItemTypeId, String batchNumber, String lotId, String expiryDate, double cp, double mrp, double sp,
    		String currencyUomId, String manufacturingDate) throws Exception {
        soapClient.addRequestParameter("facilityId", facilityId);
        soapClient.addRequestParameter("inventoryItemTypeId", inventoryItemTypeId);
        soapClient.addRequestParameter("lotId", lotId);
        soapClient.addRequestParameter("unitCost", cp);        
        soapClient.addRequestParameter("productId", productId);
        soapClient.addRequestParameter("batchNumber", batchNumber);
        StringTokenizer tokenizer = null;
        int expYear = 0, expMonth = 0, expDay = 0;
        int mnfYear = 0, mnfMonth = 0, mnfDay = 0;
        try {
            if(expiryDate != null) {
            	tokenizer = new StringTokenizer(expiryDate, "-");
            	expYear = Integer.parseInt(tokenizer.nextToken());
            	expMonth = Integer.parseInt(tokenizer.nextToken());
            	expDay = Integer.parseInt(tokenizer.nextToken());
            	soapClient.addRequestParameter("expireDate", new java.sql.Timestamp(new GregorianCalendar(expYear, expMonth, expDay).getTimeInMillis()));
            }
            if(manufacturingDate != null) {
            	tokenizer = new StringTokenizer(manufacturingDate, "-");
            	mnfYear = Integer.parseInt(tokenizer.nextToken());
            	mnfMonth = Integer.parseInt(tokenizer.nextToken());
            	mnfDay = Integer.parseInt(tokenizer.nextToken());
                
            	soapClient.addRequestParameter("manufacturingDate", new java.sql.Timestamp(new GregorianCalendar(mnfYear, mnfMonth, mnfDay).getTimeInMillis()));
            }
            
        } catch (Exception e) {

        }
        String inventoryItemId =  (String) soapClient.callOfbizService("createInventoryItem").get("inventoryItemId");
        
        ProductService productService = applicationContext.getBean(ProductService.class);
        productService.createProductPrice(soapClient, productId, productStoreGroupId, "DEFAULT_PRICE", "PURCHASE", sp, currencyUomId);
        productService.createProductPrice(soapClient, productId, productStoreGroupId, "LIST_PRICE", "PURCHASE", mrp, currencyUomId);
        
        return inventoryItemId;
    }

    public void createPhysicalInventoryItemAndVariance(SoapClient soapClient, String inventoryItemId, double quantityOnHandVar) throws Exception {
        soapClient.addRequestParameter("inventoryItemId", inventoryItemId);
        soapClient.addRequestParameter("quantityOnHandVar", quantityOnHandVar);
        soapClient.addRequestParameter("availableToPromiseVar", quantityOnHandVar);
        soapClient.callOfbizService("createPhysicalInventoryAndVariance");
    }
}
