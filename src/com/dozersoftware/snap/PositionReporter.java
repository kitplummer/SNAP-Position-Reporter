/**
* Copyright (c) 2010 Dozer Software LLC.

* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
**/

package com.dozersoftware.snap;

import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;

public class PositionReporter extends AbstractActionLifecycle {

	public PositionReporter(ConfigTree config) {
	}

	public Message process(Message message) {
		
		try {
			new ServiceInvoker("NormOut", "SNAPNormProcessor").deliverAsync(message);
		} catch (MessageDeliverException e) {
			e.printStackTrace();
		}
		//System.out.println("Kicking a PositionReport!");
		return message;
	}
	
	public void exceptionHandler(Message message, Throwable exception) {
		   System.out.println("!ERROR!");
		   System.out.println(exception.getMessage());
		   System.out.println("For Message: ");
		   System.out.println(message.getBody().get());
	}

}
