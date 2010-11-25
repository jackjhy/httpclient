package com.archermind.httpclient;

public class ErrorMsg extends HttpMsg {
	
	public final Exception getException() {
		return exception;
	}

	private Exception exception;
	
	public ErrorMsg(Exception e) {
		this.exception = e;
	}
	
	

}
