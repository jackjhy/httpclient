package com.archermind.httpclient;

import java.io.IOException;

import kilim.Mailbox;
import kilim.Pausable;
import kilim.Task;
import kilim.nio.EndPoint;

public class RequestTask extends Task {
	
	private EndPoint point;
	private Mailbox<HttpMsg> mailbox;
	private HttpRequest req;
	
	public RequestTask(Mailbox<HttpMsg> mb,EndPoint ep,HttpRequest req) {
		this.point = ep;
		this.mailbox = mb;
		this.req = req;
	}
	
//	@Override
//	public void execute() throws Pausable, Exception {
//	    	point.write(req.toBuffer());
//	    	HttpResponse resp = new HttpResponse();
//	    	resp.readHead(point);
//	    	resp.readBody(point);
//			mailbox.put(resp);
//	}
	
	@Override
	public void execute() throws Pausable{
	    	HttpResponse resp = null;
			try {
				point.write(req.toBuffer());
				resp = new HttpResponse();
				resp.readHead(point);
				resp.readBody(point);
				mailbox.put(resp);
			} catch (IOException e) {
				System.out.println("IO Exception");
				mailbox.put(new ErrorMsg(e));
			}
	}


}
