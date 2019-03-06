package com.eidikointernal;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class SendMessageAMQ {
	
	// private final static String JMSCF_JNDI_NAME = "java:comp/env/jms/TheConnectionFactory";
	//    private final static String JMSQ_JNDI_NAME = "java:comp/env/jms/PackageReceivedQueue";
	//    private final static String messageText = "Package Received - 24595023";

	public  void sendMessage(String queueName ,String message , String userName , String pwd) throws Exception {

		ConnectionFactory factory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_BROKER_URL);

       

        // Create JMS Connection
        Connection connection = factory.createConnection(userName,pwd);

        // Create JMS Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        Destination q = session.createQueue(queueName);
        
        	Session session1 = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        Destination replyDestination = session1.createQueue("ResponseQueue");

        // Create MessageProducer and TextMessage
        MessageProducer queueSender = session.createProducer(q);
        TextMessage outMessage = session.createTextMessage();
        outMessage.setText(message);

        // Set type and destination and send
        outMessage.setJMSType("package_received");
        outMessage.setJMSDestination(q);
        
        outMessage.setJMSReplyTo(replyDestination);
        queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        
        queueSender.send(outMessage);
        
        connection.close();    
        System.out.println("Get completed");
	}


}
