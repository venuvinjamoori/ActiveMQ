package com.eidikointernal;

import java.io.OutputStream;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

public class MyLogInterceptor extends LoggingOutInterceptor {

    public MyLogInterceptor() {
        super(Phase.PRE_STREAM);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        OutputStream out = message.getContent(OutputStream.class);
        final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(out);
        message.setContent(OutputStream.class, newOut);
        newOut.registerCallback(new LoggingCallback());
    }

    public class LoggingCallback implements CachedOutputStreamCallback {
        public void onFlush(CachedOutputStream cos) {
        }

        public void onClose(CachedOutputStream cos) {
            try {
                StringBuilder builder = new StringBuilder();
                cos.writeCacheTo(builder, limit);
                // here comes my xml:
                String soapXml = builder.toString();
                System.out.println("********************************");
                System.out.println(soapXml);
                SendMessageAMQ sendMessage = new SendMessageAMQ();
                sendMessage.sendMessage("RequestQueue", soapXml, "admin", "admin");
                
                System.out.println("********************************");
            } catch (Exception e) {
            }
        }
    }
}
