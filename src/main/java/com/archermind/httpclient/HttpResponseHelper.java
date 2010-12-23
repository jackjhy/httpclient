package com.archermind.httpclient;

import java.util.Properties;

public class HttpResponseHelper {

	static 	Properties props=System.getProperties();
	
	public static String getTmpDirPath(){
		return props.getProperty("java.io.tmpdir");
	}

	public static String getMaxContentLengthInMemory(){
		return props.getProperty("com.archermind.httpclient.maxcontentlengthinmemory");
	}
}
