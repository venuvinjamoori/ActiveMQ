package com.eidikointernal;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

public class TestClient {

	public static void main(String[] args) throws Exception{
		
		//GreetingsProxy proxy = new GreetingsProxy();
		//String s = proxy.sayHello("Surender");
	//	System.out.println(s);
		
		 Map<String, Object> outProps = new HashMap();
         outProps.put("action", "UsernameToken Timestamp");
	//	  outProps.put("action", "UsernameToken");

         outProps.put("passwordType", "PasswordDigest");
         outProps.put("user", "abcd");
         outProps.put("passwordCallbackClass", "com.eidikointernal.UTPasswordCallback");
        
         //GreetingsServiceLocator service = new GreetingsServiceLocator();
         GreetingsService service = new GreetingsService();
         Greetings port = service.getGreetingsPort();
         Client client = ClientProxy.getClient(port);  
        // org.apache.cxf.endpoint.Client client = ClientProxy.getClient(port);
       //  org.apache.cxf.endpoint.Client client = ClientProxy.getClient(proxy);
         Endpoint cxfEndpoint = client.getEndpoint();  
       //  client.getInInterceptors().add(new WSS4JInInterceptor(outProps));
         cxfEndpoint.getOutInterceptors().add((Interceptor<? extends Message>) new WSS4JOutInterceptor(outProps));
         cxfEndpoint.getOutInterceptors().add((Interceptor<? extends Message>) new MyLogInterceptor());
         String s1 =port.sayHello("Surender");
         System.out.println(s1);

	}

}
