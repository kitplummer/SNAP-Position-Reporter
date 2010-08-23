package com.dozersoftware.snap;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

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
	
	// 32.377062, -110.999336
	private double curLat;
	private double curLng;

	private int count = 1;
	
	public void initialize(ConfigTree config) throws ConfigurationException {
		System.out.println("** initialize: " + config);
		String sysFile = "snap.properties";
		InputStream sysIn = PositionMessageComposer.class.getClassLoader().getResourceAsStream(sysFile);
		try {
			
			if (sysIn == null) {
				System.out.println("SNAP: Can't Read System Props File");
			}
			Properties sysProps = new java.util.Properties();
			sysProps.load(sysIn);
			
			this.handle = sysProps.getProperty("handle");
			
		} catch (Exception e) {

			System.out.println("NORM ERROR: Processing Properties...");
			this.handle = "UNK";
			
			
		}
		this.curLat = 32.30;
		this.curLng = -110.90;
	}

	public void uninitialize() {
		System.out.println("uninitialize **");
	}

	public Message composeMessage() throws SchedulingException {
    	Message myMessage = MessageFactory.getInstance().getMessage(MessageType.JBOSS_XML);
    	myMessage.getBody().add("<MESSAGE type=\"text\" sender=\"" + handle + "\">" + genBody() + "</MESSAGE>");
    	myMessage.getProperties().setProperty(StoreMessageToFile.PROPERTY_JBESB_FILENAME, "ScheduledServices.log");
    	return myMessage;
	}

	public Message onProcessingComplete(Message message)
			throws SchedulingException {
		return message;
	}
	
	private String genBody() {
		String body = null;
		
		if(count == 0) {
			//reset coords - cheesy.
			this.curLat = 32.30;
			this.curLng = -110.90;
		}
		if(count <= 10) {
			// Southbound
			curLat = curLat - (count * 0.01);
			body = format();
			count++;
		} else if (count <= 20 && count > 10) {
			curLng = curLng - ((count - 10) * 0.01 );
			body = format();
			count++;
		} else if (count <= 30 && count > 20) {
			curLat = curLat + ((count - 20) * 0.01 );
			body = format();
			count++;
		} else if (count <= 39 && count > 30) {
			curLng = curLng + ((count - 30) * 0.01);
			body = format();
			count++;
		} else if (count == 40){
			curLng = curLng + ((count - 30) * 0.01);
			body = format();
			count = 0;
		}
		return body;	

	}
	
	private String format(){
		//{"POSREP": [16,"Aug 17, 2010 3:11:00 AM","31.74","-111.11"]}
		Date date = new Date();
		String body = "<BODY>{\"POSREP\":[" + count + ",";
		body = body + "\"" + DateFormat.getDateTimeInstance().format(date) + "\",";
		body = body + Double.toString(curLat) + ",";
		body = body +  Double.toString((curLng)) + "]}";
		body = body + "</BODY>";
		
		return body;
	}

}
