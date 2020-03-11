package com.retail.services.controllers;

import com.retail.services.services.InventoryService;
import com.retail.services.services.ProductService;
import com.retail.services.soap.SoapClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    @Autowired
    ApplicationContext applicationContext;

    @SuppressWarnings({"unchecked"})
    @PostMapping("/getInventoryItems")
    public Map<String, Object> getInventoryItems(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) applicationContext.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            InventoryService inventoryService = applicationContext.getBean(InventoryService.class);
            Map<String, Object> requestBody = (Map) request.get("requestBody");
            Map<String, Object> criteria = (Map) requestBody.get("criteria");
            String productStoreGroupId = requestBody.get("productStoreGroupId").toString();
            String currencyUomId = requestBody.get("currencyUomId").toString();
            Object[] inventoryItems = inventoryService.getInventoryItems(soapClient, criteria, productStoreGroupId, currencyUomId);

            if (inventoryItems.length > 0) {
                responseHeader.put("status", "INVENTORY_ITEMS_FOUND");
                responseHeader.put("message", "Inventory items found.");
                response.put("responseBody", inventoryItems);
            } else {
                responseHeader.put("status", "INVENTORY_ITEMS_NOT_FOUND");
                responseHeader.put("message", "Inventory items not found.");
            }
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/getUnmappedItems")
    public Map<String, Object> getUnmappedItems(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) applicationContext.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            InventoryService inventoryService = applicationContext.getBean(InventoryService.class);
            Object[] unmappedItems = inventoryService.getUnmappedItems(soapClient, (Map<String, Object>) request.get("requestBody"));

            if (unmappedItems.length > 0) {
                responseHeader.put("status", "UNMAPPED_ITEMS_FOUND");
                responseHeader.put("message", "Unmapped items found.");
                response.put("responseBody", unmappedItems);
            } else {
                responseHeader.put("status", "UNMAPPED_ITEMS_NOT_FOUND");
                responseHeader.put("message", "Unmapped items not found.");
            }
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/getProducts")
    public Map<String, Object> getProducts(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) applicationContext.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<Object, Object> requestBody = (Map) request.get("requestBody");
            String searchBy = (String) requestBody.get("searchBy");
            String searchValue = (String) requestBody.get("searchValue");

            ProductService productService = applicationContext.getBean(ProductService.class);
            //Object[] products = (Object[]) productService.getProducts(soapClient, searchBy, searchValue);
            Object[] products = (Object[]) productService.getStockProducts(soapClient, searchBy, searchValue);

            if (products.length > 0) {
                responseHeader.put("status", "PRODUCTS_FOUND");
                responseHeader.put("message", "Products found.");

                response.put("responseBody", products);
            } else {
                responseHeader.put("status", "PRODUCTS_NOT_FOUND");
                responseHeader.put("message", "Products not found.");
            }

        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/getProductByEAN")
    public Map<String, Object> getProductByEAN(@RequestBody Map<Object, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<Object, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) applicationContext.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map) request.get("requestBody");
            String ean = requestBody.get("ean").toString();
            String productStoreGroupId = requestBody.get("productStoreGroupId").toString();
            String currencyUomId = requestBody.get("currencyUomId").toString();
            
            ProductService productService = applicationContext.getBean(ProductService.class);
            Map<String, Object> product = productService.getProductByEAN(soapClient, ean, productStoreGroupId, currencyUomId);

            if (!product.isEmpty()) {
                responseHeader.put("status", "PRODUCT_FOUND");
                responseHeader.put("message", "Product found.");

                response.put("responseBody", product);
            } else {
                responseHeader.put("status", "PRODUCT_NOT_FOUND");
                responseHeader.put("message", "No product found.");
            }
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/getInventoryItemsByEAN")
    public Map<String, Object> getInventoryItemsByEAN(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) applicationContext.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map) request.get("requestBody");
            String ean = requestBody.get("ean").toString();
            String facilityId = requestBody.get("facilityId").toString();
            String productStoreGroupId = requestBody.get("productStoreGroupId").toString();
            String currencyUomId = requestBody.get("currencyUomId").toString();

            InventoryService inventoryService = applicationContext.getBean(InventoryService.class);
            Object[] inventoryItems = inventoryService.getInventoryItemsByEAN(soapClient, ean, facilityId, productStoreGroupId, currencyUomId);

            if (inventoryItems.length > 0) {
                responseHeader.put("status", "INVENTORY_ITEMS_FOUND");
                responseHeader.put("message", "Inventory items found.");

                response.put("inventoryItems", inventoryItems);
            } else {
                responseHeader.put("status", "INVENTORY_ITEMS_NOT_FOUND");
                responseHeader.put("message", "Inventory items not found");
            }
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/addItemsToInventory")
    public Map<String, Object> addItemsToInventory(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);
System.out.println("came into addItemsToInventory----------");
        try {
            SoapClient soapClient = (SoapClient) applicationContext.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map) request.get("requestBody");
            List<Object> items = (List) requestBody.get("items");

            for (int i = 0; i < items.size(); i++) {
                Map item = (Map) items.get(i);

                String ean = (String) item.get("ean");
                String facilityId = requestBody.get("facilityId").toString();
                String productStoreGroupId = requestBody.get("productStoreGroupId").toString();
                String currencyUomId = requestBody.get("currencyUomId").toString();
                System.out.println("ean----------"+ean+"facilityId----"+facilityId+"productStoreGroupId----"+productStoreGroupId+"currencyUomId---"+currencyUomId);
                ProductService productService = applicationContext.getBean(ProductService.class);
                Map<String, Object> product = productService.getProductByEAN(soapClient, ean, productStoreGroupId, currencyUomId);
                if (!product.isEmpty()) {
                    addItemToMappedItems(soapClient, product, facilityId, productStoreGroupId, currencyUomId, item);
                } else {
                    addItemToUnmappedItems(soapClient, item, facilityId);
                }
            }
            responseHeader.put("status", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        return response;
    }
    
    @SuppressWarnings({"unchecked"})
    @PostMapping("/deleteUnmappedItem")
    public Map<String, Object> deleteUnmappedItem(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) applicationContext.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map) request.get("requestBody");
            String itemId = requestBody.get("itemId").toString();
            InventoryService inventoryService = applicationContext.getBean(InventoryService.class);
            inventoryService.deleteUnmappedItem(soapClient, itemId);
            responseHeader.put("status", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        return response;
    }

    private void addItemToMappedItems(SoapClient soapClient, Map<String, Object> product, String facilityId, String productStoreGroupId, String currencyUomId, Map<String, Object> item) throws Exception {
        String productId = product.get("productId").toString();
        InventoryService inventoryService = applicationContext.getBean(InventoryService.class);
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("facilityId", facilityId);
        criteria.put("productId", productId);
        String lotId = item.get("lotId").toString();
        criteria.put("lotId", lotId);
        Object[] inventoryItems = inventoryService.getInventoryItems(soapClient, criteria,  productStoreGroupId, currencyUomId);

        String inventoryItemId;
        if (inventoryItems.length != 0) {
            Map<Object, Object> inventoryItem = (Map<Object, Object>) inventoryItems[0];
            inventoryItemId = (String) inventoryItem.get("inventoryItemId");
            System.out.println("inventoryItemId---"+inventoryItemId);
        } else {
            String inventoryItemTypeId = "NON_SERIAL_INV_ITEM";//product.get("inventoryItemTypeId").toString();
            String expiryDate = item.get("expiryDate").toString();
            String manufacturingDate = item.get("manufacturingDate").toString();
            String batchNumber = item.get("batchNumber").toString();
            double cp = Double.parseDouble(item.get("sp").toString());
            double mrp = Double.parseDouble(item.get("mrp").toString());
            double sp = Double.parseDouble(item.get("sp").toString());
            inventoryItemId = inventoryService.createInventoryItem(soapClient, facilityId, productStoreGroupId, productId, inventoryItemTypeId, batchNumber, lotId, expiryDate, cp, mrp, sp, currencyUomId, manufacturingDate);
        }
        double quantityOnHandVar = Double.parseDouble(item.get("quantity").toString());
        inventoryService.createPhysicalInventoryItemAndVariance(soapClient, inventoryItemId, quantityOnHandVar);
    }

    private void addItemToUnmappedItems(SoapClient soapClient, Map<String, Object> item, Object facilityId) throws Exception {
        InventoryService inventoryService = applicationContext.getBean(InventoryService.class);
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("facilityId", facilityId);
        criteria.put("ean",item.get("ean"));
        Object[] unmappedItems = inventoryService.getUnmappedItems(soapClient, criteria);

        if (unmappedItems.length == 0) {
            soapClient.addRequestParameter("ean", item.get("ean"));
            soapClient.addRequestParameter("facilityId", facilityId);
            soapClient.addRequestParameter("productName", item.get("description"));
            soapClient.addRequestParameter("description", item.get("description"));
            soapClient.addRequestParameter("quantity", item.get("quantity"));
            soapClient.addRequestParameter("batchNumber", item.get("batchNumber"));
            soapClient.addRequestParameter("mrp", item.get("mrp"));
            soapClient.addRequestParameter("sp", item.get("sp"));
            soapClient.addRequestParameter("cp", item.get("cp"));
            soapClient.addRequestParameter("lotId", item.get("lotId"));
            soapClient.addRequestParameter("expiryDate", item.get("expiryDate"));
            soapClient.addRequestParameter("mfd", item.get("manufacturingDate"));
            
            soapClient.callOfbizService("insertUnmappedItem");
        }
    }
    
    @SuppressWarnings({"unchecked"})
    @PostMapping("/getProductByProductId")
    public Map<String, Object> getProductByProductId(@RequestBody Map<Object, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<Object, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) applicationContext.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map) request.get("requestBody");
            String productId = requestBody.get("productId").toString();
            String productStoreGroupId = requestBody.get("productStoreGroupId").toString();
            String currencyUomId = requestBody.get("currencyUomId").toString();
            
            ProductService productService = applicationContext.getBean(ProductService.class);
            Map<String, Object> product = productService.getProductByProductId(soapClient, productId, productStoreGroupId, currencyUomId);

            if (!product.isEmpty()) {
                responseHeader.put("status", "PRODUCT_FOUND");
                responseHeader.put("message", "Product found.");

                response.put("responseBody", product);
            } else {
                responseHeader.put("status", "PRODUCT_NOT_FOUND");
                responseHeader.put("message", "No product found.");
            }
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}
