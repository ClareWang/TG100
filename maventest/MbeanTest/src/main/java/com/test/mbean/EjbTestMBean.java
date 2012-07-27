package com.test.mbean;

/**
 *
 */
public interface EjbTestMBean {
    
    /**
     * Get current value of message.
     * @return
     */
    public String getMessage();

    /**
     * Set the value of message.
     * @param Message
     */
    public void setMessage(String message);

    /**
     * Send the message to server.
     * @return
     */
    public String sendMessage() throws Exception;
    
    /**
     * Send the input message to server.
     */
    public String sendMessage(String message) throws Exception;
    
    /**
     * Delete messages from server.
     * @param message
     */
    public String deleteMessage(String id);
    
    public String updateMessage(String message);
}
