package com.retail.services.soap;

import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SoapClient {
	
	@Autowired
	private Environment env;
	
    public final static String SERVICE_URL = "https://localhost:9443/webtools/control/xmlrpc";
    public final static String DEV_SERVICE_URL = "https://192.168.0.52:6443/webtools/control/xmlrpc";
	/*
	 * public final static String SERVICE_URL =
	 * "https://localhost:8443/webtools/control/xmlrpc";
	 */

    private Map<String, Object> requestBody = new HashMap<>();
    private Map<String, Object> credentials = new HashMap<>();

    public void addAuthentication(Map<String, Object> credentials) {
        this.credentials = credentials;
    }

    public void addRequestParameter(String parameterName, Object parameterValue) {
        requestBody.put(parameterName, parameterValue);
    }

    public void setRequestBody(Map<String, Object> requestParameters) {
        this.requestBody = requestParameters;
    }

    public Object callOfbizService(Map<Object, Object> map) throws Exception {
        this.credentials = (Map<String, Object>) map.get("credentials");        
        this.requestBody = (Map<String, Object>) map.get("requestBody");        

        Map<String, Object> response = callOfbizService((String)map.get("service"));
        return response;
    }

    public Map<String, Object> callOfbizService(String serviceName) throws Exception {

        byPassSSLCertificate();

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(SERVICE_URL));
        config.setEnabledForExceptions(true);
        config.setEnabledForExtensions(true);

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        requestBody.putAll(credentials);

        Map<String, Object> response = (Map)client.execute(serviceName, new Object[]{requestBody});
        requestBody = new HashMap<>();

        return response;
    }
    
    public Map<String, Object> callDevOfbizService(String serviceName) throws Exception {

        byPassSSLCertificate();
        Map<String, Object> dev_credentials = new HashMap<String, Object>();
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        final String dev_service_url = env.getProperty("dev.service.url");
        //URL dev_url = new URL(dev_service_url);
        config.setServerURL(new URL(DEV_SERVICE_URL));
        config.setEnabledForExceptions(true);
        config.setEnabledForExtensions(true);

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
        
        dev_credentials.put("login.username", "admin");
        dev_credentials.put("login.password", "ofbiz");
        
        requestBody.putAll(dev_credentials);

        Map<String, Object> response = (Map)client.execute(serviceName, new Object[]{requestBody});
        requestBody = new HashMap<>();

        return response;
    }

    private void byPassSSLCertificate() throws Exception {
        TrustManager[] trustManagers = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManagers, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        HostnameVerifier hostnameVerifier = (String hostname, SSLSession session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    }
}
