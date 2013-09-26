package jarvice.message;

import jarvice.message.Message;
import jarvice.message.MessageListener;

public interface MessageProducer {
	
	 public void addMessageListener(MessageListener listener);

	    /**
	     * Remove a listener from the listener list.
	     * 
	     */
	    public void removeMessageListener(MessageListener listener);

	    /**
	     * Notify listeners after setting the message.
	     * 
	     */
	    public void sendMessage(Message message);
	

}
