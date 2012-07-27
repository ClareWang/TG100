package com.test.beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.packet.Packet;



/**
 * Session Bean implementation class MessageNotifyer
 */
@Stateless
@LocalBean
public class MessageNotifyer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageNotifyer.class);
    // private static String destinationName = "quque/TestQueue";

    @Resource(mappedName = "ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "queue/TestQueue")
    private Queue queue;

    private QueueConnection connection = null;
    private QueueSession session = null;

        
    
    public void notifyMessage(Packet packet) {
        if (packet != null)
            sendMessage(packet);
    }

    @PostConstruct
    private void initQueue() {
        try {
            closeConnection();
            connection = ((QueueConnectionFactory) connectionFactory).createQueueConnection();
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
        } catch (Exception e) {
            LOGGER.error("Failed to init JMS queue.", e);
            closeConnection();
        }
    }

    /**
     * 
     */
    @PreDestroy
    private void closeConnection() {
        try {
            if (session != null) {
                session.close();
            }
        } catch (Exception e) {
            LOGGER.warn("Closing queue session error.", e);
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            LOGGER.warn("Closing queue connection error.", e);
        }
        session = null;
        connection = null;
    }

    private void sendMessage(Packet packet) {
        try {
            LOGGER.debug("Send message: {}", packet.GetMessage());
            if (connection == null || session == null) {
                initQueue();
            }
            if (connection != null && session != null) {
              	MessageProducer producer = session.createProducer(queue);
              	producer.send(session.createObjectMessage(packet));
            } else {
                LOGGER.error("No available message queue.");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to send message to queue.", e);
        }
    }
}
