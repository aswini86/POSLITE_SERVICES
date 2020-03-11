package com.retail.services.controllers;

import com.retail.services.soap.SoapClient;
import com.retail.services.util.HashMapResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.retail.services.services.ProductService;

@RestController
public class POSController {

    @Autowired
    ApplicationContext context;

    @Autowired
    private Environment environment;

    @SuppressWarnings({"unchecked"})
    @PostMapping("/posLogin")
    public Object posLogin(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> map = (Map<String, Object>) soapClient.callOfbizService("userLogin");
            if (map.containsKey("userLogin")) {

                Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");
                requestBody.put("userId", ((Map<String, Object>) request.get("credentials")).get("login.username"));

                soapClient.setRequestBody(requestBody);

                Object posTerminals[] = (Object[]) ((Map<String, Object>) soapClient.callOfbizService("findPOSTerminals")).get("posTerminals");

                if (posTerminals.length != 0) {
                    boolean TERMINAL_NOT_FOUND = true;
                    for (int i = 0; i < posTerminals.length; i++) {
                        Map<String, Object> posTerminal = (Map) posTerminals[i];
                        String terminalId = requestBody.get("terminalId").toString();
                        if (terminalId.equals(posTerminal.get("terminalId").toString())) {
                            TERMINAL_NOT_FOUND = false;
                            soapClient.addRequestParameter("terninalId", requestBody.get("terminalId"));
                            soapClient.addRequestParameter("day", requestBody.get("day"));

                            Object[] dayDetails = (Object[]) ((Map<String, Object>) soapClient.callOfbizService("findDays")).get("dayDetails");
                            if (dayDetails.length != 0) {
                                Map<String, Object> dayDetail = (Map) dayDetails[0];
                                String dayStatus = (String) dayDetail.get("dayStatus");
                                switch (dayStatus) {
                                    case "OPENED":
                                        responseHeader.put("status", "ALREADY_LOGGED_IN");
                                        responseHeader.put("message", "Day is open.");
                                        break;

                                    case "CLOSED":
                                        responseHeader.put("status", "DAY_CLOSED");
                                        responseHeader.put("message", "Day closed. Please wait for next day.");
                                        break;
                                }
                            } else {
                                soapClient.addRequestParameter("terminalId", terminalId);
                                soapClient.addRequestParameter("dayStatus", "OPENED");
                                dayDetails = (Object[]) ((Map<String, Object>) soapClient.callOfbizService("findDays")).get("dayDetails");
                                if (dayDetails.length != 0) {
                                    responseHeader.put("status", "DAY_CLOSE_PENDING");
                                    responseHeader.put("message", "Please close the previous day, before opening a new day.");
                                } else {
                                    responseHeader.put("status", "LOGGED_IN");
                                    responseHeader.put("message", "Ready to open new day.");

                                    soapClient.addRequestParameter("terminalId", terminalId);
                                    soapClient.addRequestParameter("dayStatus", "CLOSE");
                                    dayDetails = (Object[]) ((Map<String, Object>) soapClient.callOfbizService("findDays")).get("dayDetails");

                                    Map<String, Object> responseBody = new HashMap<>();
                                    response.put("responseBody", responseBody);
                                    if (dayDetails.length != 0) {
                                        Map<String, Object> dayDetail = (Map) dayDetails[0];
                                        responseBody.put("lastClosingDay", dayDetail.get("day"));
                                        responseBody.put("dayId", dayDetail.get("dayId"));
                                    } else {
                                        responseBody.put("lastClosingDay", null);
                                    }
                                }
                            }
                            break;
                        }
                    }
                    if (TERMINAL_NOT_FOUND) {
                        responseHeader.put("status", "INVALID_TERMINAL");
                        responseHeader.put("message", environment.getProperty("pos.message.INVALID_TERMINAL"));
                    }
                } else {
                    responseHeader.put("status", "NO_TERMINAL_ASSOCIATED");
                    responseHeader.put("message", environment.getProperty("pos.message.NO_TERMINAL_ASSOCIATED"));
                }
            } else {
                responseHeader.put("status", "INVALID_CREDENTIAL");
                responseHeader.put("message", environment.getProperty("pos.message.INVALID_CREDENTIAL"));
            }
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/openingDay")
    public Object openingDay(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");

            String terminalId = requestBody.get("terminalId").toString();
            String dayStatus = requestBody.get("dayStatus").toString();
            String receiptId = requestBody.get("receiptId").toString();
            
            soapClient.addRequestParameter("terminalId", terminalId);
            soapClient.addRequestParameter("receiptId", receiptId);
            soapClient.addRequestParameter("day", requestBody.get("day").toString());
            soapClient.addRequestParameter("dayStatus", dayStatus);
            Object[] dayDetails = (Object[]) ((Map<String, Object>) soapClient.callOfbizService("findDays")).get("dayDetails");
            System.out.println("dayDetails-------"+dayDetails.length);
            if (dayDetails.length != 0) {
                responseHeader.put("status", "INVALID_DAY");
                responseHeader.put("message", "You can not open this day.");
            } else {
                formatRequestForCashDenomination(request);
                //requestBody.put("dayStatus", "OPENED");
                requestBody.put("userId", ((Map<String, Object>) request.get("credentials")).get("login.username"));
                requestBody.put("adminOpeningDayStatus", "PENDING");
                requestBody.put("adminClosingDayStatus", "PENDING");
                soapClient.setRequestBody(requestBody);
                Map<String, Object> map = (HashMap<String, Object>) soapClient.callOfbizService("createDay");

                if (requestBody.containsKey("lastClosingDayId")) {
                    soapClient.addRequestParameter("dayId", requestBody.get("lastClosingDayId"));
                    soapClient.addRequestParameter("dayStatus", "LOCK");
                    soapClient.callOfbizService("updateDay");
                }

                responseHeader.put("status", "DAY_OPENED");
                responseHeader.put("message", "Day is open now.");
                response.put("responseBody", map);
            }

        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            response.put("message", "error");
            return response.get("responseHeader");
        }

        formatResponseForCashDenomination(response);
        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/closingDay")
    public Object closingDay(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);
        Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");
        String terminalId = requestBody.get("terminalId").toString();
        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));


            formatRequestForCashDenomination(request);
            //requestBody.put("dayStatus", "CLOSED");
            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (HashMap<String, Object>) soapClient.callOfbizService("updateDay");

            responseHeader.put("status", "DAY_CLOSED");
            responseHeader.put("message", "Day is closed now.");
            response.put("responseBody", map);

        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        formatResponseForCashDenomination(response);
        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/findDays")
    public Object findDays(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");

            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (Map) soapClient.callOfbizService("findDays");
            Object[] dayDetails = (Object[]) map.get("dayDetails");

            if (dayDetails.length != 0) {
                responseHeader.put("status", "DAYS_FOUND");
                responseHeader.put("message", "One or more are found.");
                response.put("responseBody", map);
            } else {
                responseHeader.put("status", "DAYS_NOT_FOUND");
                responseHeader.put("message", "No days found.");
            }
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        formatResponseForCashDenomination(response);
        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/findDay")
    public Object findDay(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");

            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (Map) soapClient.callOfbizService("findDay");

            if (map.containsKey("dayDetail")) {
                responseHeader.put("status", "DAY_FOUND");
                responseHeader.put("message", "Day found.");
                response.put("responseBody", map);
            } else {
                responseHeader.put("status", "DAY_NOT_FOUND");
                responseHeader.put("message", "Invalid dayId.");
            }
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        formatResponseForCashDenomination(response);
        return response;
    }
    
    @SuppressWarnings({"unchecked"})
    @PostMapping("/findWebDay")
    public Object findWebDay(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");

            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (Map) soapClient.callOfbizService("findDay");

            if (map.containsKey("dayDetail")) {
                responseHeader.put("status", "DAY_FOUND");
                responseHeader.put("message", "Day found.");
                response.put("responseBody", map);
            } else {
                responseHeader.put("status", "DAY_NOT_FOUND");
                responseHeader.put("message", "Invalid dayId.");
            }
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/updateDay")
    public Object updateDay(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");

            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (Map) soapClient.callOfbizService("updateDay");

            responseHeader.put("status", "DAY_UPDATED");
            responseHeader.put("message", "Day updated.");
            response.put("responseBody", map);

        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        formatResponseForCashDenomination(response);
        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/findProducts")
    public Object findProducts(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");

            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (Map) soapClient.callOfbizService("findPOSProducts");
            Object[] products = (Object[]) map.get("products");

            if (products.length != 0) {
                responseHeader.put("status", "PRODUCTS_FOUND");
                responseHeader.put("message", "One or more products are found.");
                response.put("responseBody", map);
            } else {
                responseHeader.put("status", "PRODUCTS_NOT_FOUND");
                responseHeader.put("message", "No products found.");
            }
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        return response;
    }

    @SuppressWarnings({"unchecked"})
    @PostMapping("/findProductPrice")
    public Object findProductPrice(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");

            soapClient.setRequestBody(requestBody);
            System.out.println(requestBody);
            Map<String, Object> map = (Map) soapClient.callOfbizService("findPOSProductPrice");

            responseHeader.put("status", "PRODUCT_PRICE_FOUND");
            responseHeader.put("message", "Product price found.");
            response.put("responseBody", map);
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        return response;
    }

    private void formatRequestForCashDenomination(Map<String, Object> request) {
        Map<String, Object> requestBody = (Map) request.get("requestBody");

        if (requestBody != null) {
            if (requestBody.containsKey("openingDayCashDenomination")) {
                Map<String, Object> openingDayCashDenomination = (Map) requestBody.get("openingDayCashDenomination");
                requestBody.put("openingDayCount2000", openingDayCashDenomination.get("2000"));
                requestBody.put("openingDayCount500", openingDayCashDenomination.get("500"));
                requestBody.put("openingDayCount200", openingDayCashDenomination.get("200"));
                requestBody.put("openingDayCount100", openingDayCashDenomination.get("100"));
                requestBody.put("openingDayCount50", openingDayCashDenomination.get("50"));
                requestBody.put("openingDayCount20", openingDayCashDenomination.get("20"));
                requestBody.put("openingDayCount10", openingDayCashDenomination.get("10"));
                requestBody.put("openingDayCount5", openingDayCashDenomination.get("5"));
                requestBody.put("openingDayCount2", openingDayCashDenomination.get("2"));
                requestBody.put("openingDayCount1", openingDayCashDenomination.get("1"));

                requestBody.remove("openingDayCashDenomination");
            }

            if (requestBody.containsKey("closingDayCashDenomination")) {
                Map<String, Object> closingDayCashDenomination = (Map) requestBody.get("closingDayCashDenomination");
                requestBody.put("closingDayCount2000", closingDayCashDenomination.get("2000"));
                requestBody.put("closingDayCount500", closingDayCashDenomination.get("500"));
                requestBody.put("closingDayCount200", closingDayCashDenomination.get("200"));
                requestBody.put("closingDayCount100", closingDayCashDenomination.get("100"));
                requestBody.put("closingDayCount50", closingDayCashDenomination.get("50"));
                requestBody.put("closingDayCount20", closingDayCashDenomination.get("20"));
                requestBody.put("closingDayCount10", closingDayCashDenomination.get("10"));
                requestBody.put("closingDayCount5", closingDayCashDenomination.get("5"));
                requestBody.put("closingDayCount2", closingDayCashDenomination.get("2"));
                requestBody.put("closingDayCount1", closingDayCashDenomination.get("1"));

                requestBody.remove("closingDayCashDenomination");
            }
        }
    }

    private void formatResponseForCashDenomination(Map<String, Object> response) {
        Map<String, Object> responseBody = (Map) response.get("responseBody");

        if (responseBody != null) {
            if (responseBody.containsKey("dayDetail")) {
                Map<String, Object> dayDetail = (Map) responseBody.get("dayDetail");
                changeDayDetail(dayDetail);
            }

            if (responseBody.containsKey("dayDetails")) {
                Object[] dayDetails = (Object[]) responseBody.get("dayDetails");
                for (int i = 0; i < dayDetails.length; i++) {
                    Map<String, Object> dayDetail = (Map) dayDetails[i];
                    changeDayDetail(dayDetail);
                }
            }
        }
    }

    private void changeDayDetail(Map<String, Object> dayDetail) {
        if (dayDetail.containsKey("openingDayCount2000")) {
            Map<String, Object> openingDayCashDenomination = new HashMap<>();

            openingDayCashDenomination.put("2000", dayDetail.get("openingDayCount2000"));
            dayDetail.remove("openingDayCount2000");

            openingDayCashDenomination.put("500", dayDetail.get("openingDayCount500"));
            dayDetail.remove("openingDayCount500");

            openingDayCashDenomination.put("200", dayDetail.get("openingDayCount200"));
            dayDetail.remove("openingDayCount200");

            openingDayCashDenomination.put("100", dayDetail.get("openingDayCount100"));
            dayDetail.remove("openingDayCount100");

            openingDayCashDenomination.put("50", dayDetail.get("openingDayCount50"));
            dayDetail.remove("openingDayCount50");

            openingDayCashDenomination.put("20", dayDetail.get("openingDayCount20"));
            dayDetail.remove("openingDayCount20");

            openingDayCashDenomination.put("10", dayDetail.get("openingDayCount10"));
            dayDetail.remove("openingDayCount10");

            openingDayCashDenomination.put("5", dayDetail.get("openingDayCount5"));
            dayDetail.remove("openingDayCount5");

            openingDayCashDenomination.put("2", dayDetail.get("openingDayCount2"));
            dayDetail.remove("openingDayCount2");

            openingDayCashDenomination.put("1", dayDetail.get("openingDayCount1"));
            dayDetail.remove("openingDayCount1");

            dayDetail.put("openingDayCashDenomination", openingDayCashDenomination);
        }

        if (dayDetail.containsKey("closingDayCount2000")) {
            Map<String, Object> closingDayCashDenomination = new HashMap<>();

            closingDayCashDenomination.put("2000", dayDetail.get("closingDayCount2000"));
            dayDetail.remove("closingDayCount2000");

            closingDayCashDenomination.put("500", dayDetail.get("closingDayCount500"));
            dayDetail.remove("closingDayCount500");

            closingDayCashDenomination.put("200", dayDetail.get("closingDayCount200"));
            dayDetail.remove("closingDayCount200");

            closingDayCashDenomination.put("100", dayDetail.get("closingDayCount100"));
            dayDetail.remove("closingDayCount100");

            closingDayCashDenomination.put("50", dayDetail.get("closingDayCount50"));
            dayDetail.remove("closingDayCount50");

            closingDayCashDenomination.put("20", dayDetail.get("closingDayCount20"));
            dayDetail.remove("closingDayCount20");

            closingDayCashDenomination.put("10", dayDetail.get("closingDayCount10"));
            dayDetail.remove("closingDayCount10");

            closingDayCashDenomination.put("5", dayDetail.get("closingDayCount5"));
            dayDetail.remove("closingDayCount5");

            closingDayCashDenomination.put("2", dayDetail.get("closingDayCount2"));
            dayDetail.remove("closingDayCount2");

            closingDayCashDenomination.put("1", dayDetail.get("closingDayCount1"));
            dayDetail.remove("closingDayCount1");

            dayDetail.put("closingDayCashDenomination", closingDayCashDenomination);
        }
    }
    
    @SuppressWarnings({"unchecked"})
    @PostMapping("/getDayDetails")
    public Object getDayDetails(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");

            String day = requestBody.get("day").toString();
            String dayStatus = "",adminOpeningDayStatus;
            
            if(!(day.isEmpty())) {
            	soapClient.addRequestParameter("day", day);
            }
            
            Object[] dayDetails = (Object[]) ((Map<String, Object>) soapClient.callOfbizService("findDayDetails")).get("DayDetails");
            System.out.println("dayDetails-------"+dayDetails.length);
            if (dayDetails.length == 0) {
                responseHeader.put("message", "Invalid Day details");
            } else {
            	response.put("dayDetails", dayDetails);
            }

        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        return response;
    }
    
    @PostMapping(path="/generateReceiptId")
	public Object generateReceiptId(@RequestBody Hashtable<String, String> params) {		
		HashMap<String, Object> errorMsg = new HashMap<String, Object>();
		SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
		
		for(String key : params.keySet()) {
			soapClient.addRequestParameter(key, params.get(key));
		}
		HashMapResponse res = new HashMapResponse();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> response = (HashMap<String, Object>)soapClient.callOfbizService("generateReceiptId");
			
			res.setResponse(response);
			
			return res;
			
		} catch (Exception e) {
			errorMsg.put("errormsg", e.getMessage());
			res.setResponse(errorMsg);
			return res;
		}
	}
    /*@SuppressWarnings({"unchecked"})
    @PostMapping("/getStoreAddress")
    public Object getStoreAddress(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);
        HashMapResponse res = new HashMapResponse();
        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");
            String posTerminalId = requestBody.get("posTerminalId").toString();
            
            if(!(posTerminalId.isEmpty())) {
            	soapClient.addRequestParameter("posTerminalId", posTerminalId);
            }
            
            response = (HashMap<String, Object>)soapClient.callOfbizService("getStoreAddress");
            res.setResponse(response);
            

        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        return res;
    }*/
    @SuppressWarnings({"unchecked"})
    @PostMapping("/getPOSStoreAddress")
    public Object getPOSStoreAddress(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);
        HashMapResponse res = new HashMapResponse();
        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");
            String posTerminalId = requestBody.get("posTerminalId").toString();
            
            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (Map) soapClient.callOfbizService("getStoreAddress");
            res.setResponse(map);
            
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        return res;
    }
    @SuppressWarnings({"unchecked"})
    @PostMapping("/findTestDay")
    public Object findTestDay(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");

            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (Map) soapClient.callOfbizService("findDay");

            if (map.containsKey("dayDetail")) {
                responseHeader.put("status", "DAY_FOUND");
                responseHeader.put("message", "Day found.");
                response.put("responseBody", map);
            } else {
                responseHeader.put("status", "DAY_NOT_FOUND");
                responseHeader.put("message", "Invalid dayId.");
            }
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        formatResponseForCashDenomination(response);
        return response;
    }
    @SuppressWarnings({"unchecked"})
    @PostMapping("/getDayId")
    public Object getDayId(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);

        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");

            soapClient.setRequestBody(requestBody);
            System.out.println(requestBody);
            Map<String, Object> map = (Map) soapClient.callOfbizService("getDayId");

            responseHeader.put("status", "Day Id Found");
            responseHeader.put("message", "Day Id Found.");
            response.put("responseBody", map);
        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
        }

        return response;
    }
    @SuppressWarnings({"unchecked"})
    @PostMapping("/posmartCashReport")
    public Object posmartCashReport(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);
        Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");
        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (HashMap<String, Object>) soapClient.callOfbizService("posmartCashReport");
            responseHeader.put("status", "Success.");
            response.put("responseBody", map);

        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
    @SuppressWarnings({"unchecked"})
    @PostMapping("/getPosmartSummaryCashReport")
    public Object getPosmartSummaryCashReport(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);
        Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");
        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (HashMap<String, Object>) soapClient.callOfbizService("getPosmartSummaryCashReport");
            responseHeader.put("status", "Success.");
            response.put("responseBody", map);

        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
    @SuppressWarnings({"unchecked"})
    @PostMapping("/billSummary")
    public Object billSummary(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);
        Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");
        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (HashMap<String, Object>) soapClient.callOfbizService("billSummary");
            responseHeader.put("status", "Success.");
            response.put("responseBody", map);

        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/enableTaxInvoice")
    public Object enableTaxInvoice(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("isChecked", map.get("isChecked"));
        soapClient.addRequestParameter("receiptId", map.get("receiptId"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("enableTaxInvoice");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getTaxInvoice")
    public Object getTaxInvoice(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("receiptId", map.get("receiptId"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getTaxInvoice");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/updateCartTxn")
    public Object updateCartTxn(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("receiptId", map.get("receiptId"));
        soapClient.addRequestParameter("addl_discount", map.get("addl_discount"));
        soapClient.addRequestParameter("isDiscountPer", map.get("isDiscountPer"));
        soapClient.addRequestParameter("charges", map.get("charges"));
        soapClient.addRequestParameter("isChargePer", map.get("isChargePer"));
        soapClient.addRequestParameter("billAmount", map.get("billAmount"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("updateCartTxn");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getAdditionalCartTxn")
    public Object getAdditionalCartTxn(@RequestBody Map<Object, Object> map) {
		
        SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
        soapClient.addRequestParameter("receiptId", map.get("receiptId"));
        soapClient.addRequestParameter("billAmount", map.get("billAmount"));
        
        Map<String, Object> response = new HashMap<>();
        HashMapResponse res = new HashMapResponse();
        try {
            response = soapClient.callOfbizService("getAdditionalCartTxn");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "error");
            return response;
        }
        
        response.put("message", "success");
        res.setResponse(response);
        return res;
    }
    @SuppressWarnings({"unchecked"})
    @PostMapping("/customerReport")
    public Object customerReport(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);
        Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");
        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            soapClient.setRequestBody(requestBody);
            Map<String, Object> map = (HashMap<String, Object>) soapClient.callOfbizService("customerReport");
            responseHeader.put("status", "Success.");
            response.put("responseBody", map);

        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
    @SuppressWarnings({"unchecked"})
    @PostMapping("/expiredProductReport")
    public Object expiredProductReport(@RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> responseHeader = new HashMap<>();
        response.put("responseHeader", responseHeader);
        Map<String, Object> requestBody = (Map<String, Object>) request.get("requestBody");
        String productStoreGroupId = (String) requestBody.get("productStoreGroupId");
        String currencyUomId = (String) requestBody.get("currencyUomId");
        String expireDate = "", manufacturingDate = "";
        try {
            SoapClient soapClient = (SoapClient) context.getBean(SoapClient.class);
            soapClient.addAuthentication((Map<String, Object>) request.get("credentials"));

            soapClient.setRequestBody(requestBody);
            Object[] inventoryItems = (Object[]) soapClient.callOfbizService("findInventoryItems").get("inventoryItemList");
           
            for (int i = 0; i < inventoryItems.length; i++) {
                Map<String, Object> inventoryItem = (Map) inventoryItems[i];

                String productId = (String) inventoryItem.get("productId");
                ProductService productService = context.getBean(ProductService.class);
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
            responseHeader.put("status", "Success.");
            response.put("responseBody", inventoryItems);

        } catch (Exception e) {
            responseHeader.put("status", "ERROR");
            responseHeader.put("message", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}
