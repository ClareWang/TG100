package com.test.server;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.server.beans.TestService;
import com.test.packet.Packet;

/**
 * Message-Driven Bean implementation class for: TestMdb
 * 
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/TestQueue") })
public class TestMdb implements MessageListener {

    /**
     * Logger for TestMdb
     */
    private final Logger LOGGER = LoggerFactory.getLogger(TestMdb.class);

    @EJB
    TestService tService;

    /**
     * Default constructor.
     */
    public TestMdb() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        if(!(message instanceof ObjectMessage))
        {
            LOGGER.error("Invalid packet received from queue.");
            return;
        }
        
        try {
        	ObjectMessage omsg = (ObjectMessage) message;
        	Packet packet = (Packet) omsg.getObject();
            if (packet == null) {
                LOGGER.error("No packet found!");
                return;
            }
            LOGGER.info("Packet received ! ");
            tService.handleMessage(packet);
        } catch (JMSException e) {
            LOGGER.error("Error happen when handling message in queue.", e);
        }
    }

}
