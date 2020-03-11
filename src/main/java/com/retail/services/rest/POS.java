package com.retail.services.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.retail.services.soap.SoapClient;

@RestController
public class POS {

    public static final Logger logger = LoggerFactory.getLogger(POS.class);

    @Autowired
    ApplicationContext context;

    @Autowired
    private Environment environment;
    
    @SuppressWarnings("unchecked")
    @PostMapping(path = "/createInvoice")
    public Object createInvoice(@RequestBody HashMap<String, Object> request) {
        HashMap<String, Object> errorMsg = new HashMap<String, Object>();
        HashMap<Object, HashMap<String, Object>> response = new HashMap<>();
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        String partyId = (String) request.get("partyId");
        String partyIdFrom = (String) request.get("partyIdFrom");
        String invoiceTypeId = (String) request.get("invoiceTypeId");
        String statusId = (String) request.get("statusId");
        String invoiceDate = (String) request.get("invoiceDate");

        soapClient.addRequestParameter("partyId", partyId);
        soapClient.addRequestParameter("partyIdFrom", partyIdFrom);
        soapClient.addRequestParameter("invoiceTypeId", invoiceTypeId);
        soapClient.addRequestParameter("statusId", statusId);

        Map<String, Object> credentials = (Map<String, Object>) request.get("credentials");
        soapClient.addAuthentication(credentials);

        try {

            HashMap<String, Object> result = (HashMap<String, Object>) soapClient.callOfbizService("createInvoice");

            String invoiceId = (String) result.get("invoiceId");

            List<Object> invoiceItemList = (List<Object>) request.get("invoiceItemList");
            for (Object invoiceItem : invoiceItemList) {
                HashMap<String, Object> param = (HashMap<String, Object>) invoiceItem;
                param.put("credentials", credentials);
                createInvoiceItem(invoiceId, param);
            }
            response.put("responseBody", result);
            logger.info("Response : CreateInvoice {}", response);
            return response;

        } catch (Exception e) {
            errorMsg.put("errormsg", environment.getProperty("create.invoice.error"));
            response.put("error", errorMsg);
            //logger.error("Error in CreateInvoice {}", e.getStackTrace());
            return response;
        }
    }

    @SuppressWarnings("unchecked")
    public Object createInvoiceItem(String invoiceId, HashMap<String, Object> request) {
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        HashMap<String, Object> errorMsg = new HashMap<String, Object>();
        HashMap<String, Object> response = new HashMap<>();
        soapClient.addRequestParameter("invoiceId", invoiceId);

        for (String key : request.keySet()) {
            soapClient.addRequestParameter(key, request.get(key));
        }

        Map<String, Object> credentials = (Map<String, Object>) request.get("credentials");

        soapClient.addAuthentication(credentials);

        try {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> result = (HashMap<String, Object>) soapClient
                    .callOfbizService("createInvoiceItem");

            response.put("responseBody", result);
            logger.info("Response : CreateInvoiceItem {}", response);
            return response;
        } catch (Exception e) {
            errorMsg.put("errormsg", environment.getProperty("create.invoiceitem.error"));
            response.put("error", errorMsg);
            //logger.error("Error: In Create Invoice Item {}", e.getStackTrace());
            return response;
        }

    }

    @SuppressWarnings("unchecked")
    @PostMapping(path = "/checkout")
    public Object invoiceReadyForPayment(@RequestBody HashMap<String, Object> request) {
        HashMap<String, Object> errorMsg = new HashMap<String, Object>();
        HashMap<Object, HashMap<String, Object>> response = new HashMap<>();
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);

        for (String key : request.keySet()) {
            soapClient.addRequestParameter(key, request.get(key));
        }

        Map<String, Object> credentials = (Map<String, Object>) request.get("credentials");
        soapClient.addAuthentication(credentials);

        try {

            HashMap<String, Object> result = (HashMap<String, Object>) soapClient
                    .callOfbizService("setInvoiceStatus");

            response.put("responseBody", result);
            logger.info("Response : CheckOut {}", response);
            return response;

        } catch (Exception e) {
            errorMsg.put("errormsg", environment.getProperty("checkout.error"));
            response.put("error", errorMsg);
            //logger.error("Error: In CheckOut {}", e.getStackTrace());
            return response;
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping(path = "/createBillStatus")
    public Object createBillStatus(@RequestBody HashMap<String, Object> request) {
        HashMap<String, Object> errorMsg = new HashMap<String, Object>();
        HashMap<Object, HashMap<String, Object>> response = new HashMap<>();
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        HashMap<String, Object> response1 = (HashMap<String, Object>) findBillStatus(request);
        for (String key : request.keySet()) {
            soapClient.addRequestParameter(key, request.get(key));
        }
        Map<String, Object> credentials = (Map<String, Object>) request.get("credentials");
        soapClient.addAuthentication(credentials);

        try {
            Object invoiceId = (Object) response1.get("billStatus");

            if (invoiceId != null) {
                HashMap<String, Object> result = (HashMap<String, Object>) soapClient
                        .callOfbizService("updateBillingStatus");
                response.put("responseBody", result);

            } else {

                HashMap<String, Object> result = (HashMap<String, Object>) soapClient
                        .callOfbizService("createBillingStatus");
                String status = (String) request.get("statusId");
                response.put("responseBody", result);

            }
            logger.info("Response : CreateBillStatus {}", response);
            return response;

        } catch (Exception e) {
            errorMsg.put("errormsg", environment.getProperty("billstaus.error"));
            response.put("error", errorMsg);
            //logger.error("Error: In CreateBillStatus {}", e.getStackTrace());
            return response;
        }
    }

    @SuppressWarnings("unchecked")
    public Object findBillStatus(HashMap<String, Object> request) {
        HashMap<String, Object> errorMsg = new HashMap<String, Object>();
        HashMap<Object, HashMap<String, Object>> response = new HashMap<>();
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        String invoiceId = (String) request.get("invoiceId");
        soapClient.addRequestParameter("invoiceId", invoiceId);
        Map<String, Object> credentials = (Map<String, Object>) request.get("credentials");
        soapClient.addAuthentication(credentials);

        try {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> result = (HashMap<String, Object>) soapClient.callOfbizService("findBillStatus");
            response.put("responseBody", result);
            logger.info("Response : FindBillStatus {}", response);
            return response;
        } catch (Exception e) {
            errorMsg.put("errormsg", environment.getProperty("find.bill.status.error"));
            response.put("error", errorMsg);
            //logger.error("Error: In Find Bill Status {}", e.getStackTrace());
            return response;
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping(path = "/voidBill")
    public Object voidBill(@RequestBody HashMap<String, Object> request) {
        HashMap<String, Object> errorMsg = new HashMap<String, Object>();
        HashMap<Object, HashMap<String, Object>> response = new HashMap<>();
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        HashMap<String, Object> response1 = (HashMap<String, Object>) findBillStatus(request);
        for (String key : request.keySet()) {
            soapClient.addRequestParameter(key, request.get(key));
        }
        Map<String, Object> credentials = (Map<String, Object>) request.get("credentials");
        soapClient.addAuthentication(credentials);

        try {
            Object invoiceId = (Object) response1.get("billStatus");

            if (invoiceId != null) {
                HashMap<String, Object> result = (HashMap<String, Object>) soapClient
                        .callOfbizService("updateBillingStatus");
                response.put("responseBody", result);

            } else {

                HashMap<String, Object> result = (HashMap<String, Object>) soapClient
                        .callOfbizService("createBillingStatus");
                String status = (String) request.get("statusId");
                response.put("responseBody", result);

            }
            HashMap<String, Object> invoiceResponse = (HashMap<String, Object>) invoiceCancel(request);
            logger.info("Response : VoidBill {}", response);
            return response;

        } catch (Exception e) {
            errorMsg.put("errormsg", environment.getProperty("void.bill.error"));
            response.put("error", errorMsg);
            //logger.error("Error: In Void Bill  {}", e.getStackTrace());
            return response;
        }
    }

    @SuppressWarnings("unchecked")
    public Object invoiceCancel(HashMap<String, Object> request) {

        HashMap<String, Object> errorMsg = new HashMap<String, Object>();
        HashMap<Object, HashMap<String, Object>> response = new HashMap<>();
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("invoiceId", request.get("invoiceId"));
        soapClient.addRequestParameter("statusId", "INVOICE_CANCELLED");
        Map<String, Object> credentials = (Map<String, Object>) request.get("credentials");
        soapClient.addAuthentication(credentials);

        try {
            HashMap<String, Object> result = (HashMap<String, Object>) soapClient.callOfbizService("setInvoiceStatus");
            response.put("responseBody", result);
            logger.info("Response : InvoiceCancel {}", response);
            return response;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            errorMsg.put("errormsg", environment.getProperty("void.bill.error"));
            response.put("error", errorMsg);
            //logger.error("Error: In Invoice Cancel {}", e.getStackTrace());
            return response;
        }
    }
}
