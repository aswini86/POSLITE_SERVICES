package com.retail.services.services;

import com.retail.services.soap.SoapClient;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public Map<String, Object> getProductByEAN(SoapClient soapClient, String ean, String productStoreGroupId, String currencyUomId) throws Exception {
        Map<String, Object> product = new HashMap<>();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("entityName", "GoodIdentification");
        Map<String, Object> inputFields = new HashMap<>();
        requestBody.put("inputFields", inputFields);
        inputFields.put("goodIdentificationTypeId", "EAN");
        inputFields.put("idValue", ean);

        soapClient.setRequestBody(requestBody);
        Map<String, Object> map = soapClient.callOfbizService("performFindList");

        if (((int) map.get("listSize")) > 0) {
            Object[] listObjects = (Object[]) map.get("list");
            Map<String, Object> goodIdentification = (Map) listObjects[0];

            String productId = (String) goodIdentification.get("productId");

            soapClient.addRequestParameter("idToFind", productId);
            product = (Map) soapClient.callOfbizService("findProductById").get("product");
            
            product.put("productPrice", getProductPrice(soapClient, productId, productStoreGroupId, currencyUomId));
        }

        return product;
    }

    public Object[] getProducts(SoapClient soapClient, String searchBy, String searchValue) throws Exception {

        soapClient.addRequestParameter("searchBy", searchBy);
        soapClient.addRequestParameter("searchValue", searchValue);
        Object[] products = (Object[])soapClient.callOfbizService("findPOSProducts").get("products");
        for(int i=0;i<products.length;i++){
            Map<String, Object> product = (Map)products[i];
            product.put("ean", getProductEAN(soapClient, product.get("productId").toString()));
        }
        return products;
    }
    
    public Object[] getStockProducts(SoapClient soapClient, String searchBy, String searchValue) throws Exception {

        soapClient.addRequestParameter("searchBy", searchBy);
        soapClient.addRequestParameter("searchValue", searchValue);
        Object[] products = (Object[])soapClient.callOfbizService("getStockProducts").get("products");
        /*for(int i=0;i<products.length;i++){
            Map<String, Object> product = (Map)products[i];
            product.put("ean", getProductEAN(soapClient, product.get("productId").toString()));
        }*/
        return products;
    }

    public Object[] getProductAttributes(SoapClient soapClient, String productId) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("entityName", "ProductAttribute");
        Map<String, Object> inputFields = new HashMap<>();
        requestBody.put("inputFields", inputFields);
        inputFields.put("productId", productId);
        soapClient.setRequestBody(requestBody);
        Object[] productAttributes = (Object[]) soapClient.callOfbizService("performFindList").get("list");
        return productAttributes;
    }
    
    public String getProductEAN(SoapClient soapClient, String productId) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("entityName", "GoodIdentification");
        Map<String, Object> inputFields = new HashMap<>();
        requestBody.put("inputFields", inputFields);
        inputFields.put("productId", productId);
        inputFields.put("goodIdentificationTypeId", "EAN");
        soapClient.setRequestBody(requestBody);
        Object[] goodIdentifications = (Object[]) soapClient.callOfbizService("performFindList").get("list");
        if(goodIdentifications.length>0){
            return ((Map<String, Object>)goodIdentifications[0]).get("idValue").toString();
        }else{
            return "";
        }
    }
    
    public void createProductPrice(SoapClient soapClient, String productId, String productStoreGroupId, String productPriceTypeId, String productPricePurposeId, double price, String currencyUomId) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", productId);
        requestBody.put("productStoreGroupId", productStoreGroupId);
        requestBody.put("productPriceTypeId", productPriceTypeId);
        requestBody.put("productPricePurposeId", productPricePurposeId);
        requestBody.put("currencyUomId", currencyUomId);
        requestBody.put("price", new BigDecimal(price));
        
        soapClient.setRequestBody(requestBody);
        soapClient.callOfbizService("createProductPrice");
    }
    
    public Map<String, Object> getProductPrice(SoapClient soapClient, String productId, String productStoreGroupId, String currencyUomId) throws Exception {
        soapClient.addRequestParameter("productId", productId);
        soapClient.addRequestParameter("productStoreGroupId", productStoreGroupId);
        soapClient.addRequestParameter("currencyUomId", currencyUomId);
        
        Map<String, Object> itemPrice = (Map) soapClient.callOfbizService("findPOSProductPrice");
        return itemPrice;
    }
    
    public Map<String, Object> getProductByProductId(SoapClient soapClient, String productId, String productStoreGroupId, String currencyUomId) throws Exception {
        Map<String, Object> product = new HashMap<>();

            soapClient.addRequestParameter("idToFind", productId);
            product = (Map) soapClient.callOfbizService("findProductById").get("product");
            
            product.put("productPrice", getProductPrice(soapClient, productId, productStoreGroupId, currencyUomId));

        return product;
    }
}
