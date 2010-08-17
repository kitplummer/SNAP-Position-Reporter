package com.dozersoftware.snap;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.StoreMessageToFile;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.ScheduledEventMessageComposer;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;
import org.jboss.soa.esb.schedule.SchedulingException;

public class PositionMessageComposer implements ScheduledEventMessageComposer {

	private String handle;
	public void initialize(ConfigTree config) throws ConfigurationException {
		System.out.println("** initialize: " + config);
		this.handle = config.getAttribute("handle");
	}

	public void uninitialize() {
		System.out.println("uninitialize **");
	}

	public Message composeMessage() throws SchedulingException {
		System.out.println("compose a message");
    	Message myMessage = MessageFactory.getInstance().getMessage(MessageType.JBOSS_XML);
    	myMessage.getBody().add("<MESSAGE type=\"text\" sender=\"" + handle + "\"><BODY>POSREP</BODY></MESSAGE>");
    	myMessage.getProperties().setProperty(StoreMessageToFile.PROPERTY_JBESB_FILENAME, "ScheduledServices.log");
    	return myMessage;
	}

	public Message onProcessingComplete(Message message)
			throws SchedulingException {
		System.out.println("onProcessingComplete");
		return message;
	}

}
