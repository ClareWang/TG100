package com.test.mbean;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.beans.MessageNotifyer;
import com.test.packet.Packet;
import com.test.packet.Type;


public class EjbTest implements EjbTestMBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(EjbTest.class);
    
    private String msg;
    
    MessageNotifyer notifyer;
    
    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public void setMessage(String message) {
        this.msg = message;
    }

    @Override
    public String sendMessage() throws Exception {
        if(msg == null || msg.isEmpty())
        {
            return "Message input cannot be null";
        }
        initNotifyer();
        Packet packet=new Packet();
        packet.SetType(Type.SEND);
        packet.SetMessage(msg);       
        notifyer.notifyMessage(packet);
        return packet.toString() + " is sent to server succesfully.";
    }

    @Override
    public String sendMessage(String message) throws Exception{
        if(message == null || message.isEmpty())
        {
            return "Message input cannot be null";
        }
        initNotifyer();

        Packet packet=new Packet();
        packet.SetType(Type.SEND);
        packet.SetMessage(message); 
        notifyer.notifyMessage(packet);
        return packet.toString() + " is sent to server succesfully.";
    }
    
    private void initNotifyer()
    {
        try {
            if(notifyer==null)
            {
                InitialContext ctx = new InitialContext();
                notifyer = (MessageNotifyer)ctx.lookup("java:global/MbeanTest/MessageNotifyer!com.test.beans.MessageNotifyer");
            }
        } catch (NamingException e) {
            LOGGER.error("Unable to find MessageNotifyer.", e);
        }
    }

    @Override
    public String deleteMessage(String id) {
    	if(id == null || id.isEmpty())
        {
            return "Id input cannot be null";
        }
    	    	
        initNotifyer();

        Packet packet=new Packet();
        packet.SetType(Type.DELETE);
        packet.SetId(Long.parseLong(id)); 
        notifyer.notifyMessage(packet);
        return packet.toString() + " is sent to server succesfully.";
    }  
    
    @Override
    public String updateMessage(String message) {
    	return "test";
    } 
        
}
