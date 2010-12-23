package com.archermind.httpclient;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import static com.archermind.httpclient.Utils.*;

public class HttpRequestHelper {
	
	static final String GET = "get";
	static final String POST = "post";
	static final String DEFAULT_ACCEPT_VALUE = "*/*";
	static final String DEFAULT_USER_AGENT_VALUE="Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.63 Safari/534.3";	
	static final String ACCEPT_ENCODING_NAME = "Accept-Encoding";
	static final String DEFAULT_ACCEPT_ENCODING_VALUE = "gzip,deflate";
	
	private static NVPair[] DefaultHeader;
	
	static{
		Properties props=System.getProperties();
		String language = props.getProperty("user.language");
		language = language==null?"zh":language;
		language = language.trim().length()==0?"zh":language.trim();
		language = language+"-"+props.getProperty("user.country")+","+language+";q=0.8";

		ArrayList<NVPair> list = new ArrayList<NVPair>();
		list.add(new NVPair(ACCEPT_NAME, DEFAULT_ACCEPT_VALUE));
		list.add(new NVPair(USER_AGENT, DEFAULT_USER_AGENT_VALUE));
		list.add(new NVPair(ACCEPT_ENCODING_NAME, DEFAULT_ACCEPT_ENCODING_VALUE));
		list.add(new NVPair("Accept-Language", language));
		
		DefaultHeader = list.toArray(new NVPair[]{});
	}
	
	public static HttpRequest simpleGetRequest(URL url){
		HttpRequest req = new HttpRequest(url, GET);
		req.setHeader(DefaultHeader);
		return req;
	}

	public static HttpRequest simplePostRequest(URL url,NVPair[] params){
		HttpRequest req = new HttpRequest(url, POST);
		req.setHeader(mergeNvPairs(new NVPair[]{new NVPair(CONTENT_TYPE, "application/x-www-form-urlencoded")},DefaultHeader));
		req.setBody(simplePostBody(params));
		return req;
	}
	
	public static HttpRequest getRequest(URL url,NVPair[] nvs){
		HttpRequest req = new HttpRequest(url, GET);
		req.setHeader(mergeNvPairs(nvs,DefaultHeader));
		return req;
	}

	public static HttpRequest postRequest(URL url,NVPair[] nvs,NVPair[] params){
		HttpRequest req = new HttpRequest(url, POST);
		req.setHeader(mergeNvPairs(nvs, mergeNvPairs(new NVPair[]{new NVPair(CONTENT_TYPE, "application/x-www-form-urlencoded")},DefaultHeader)));
		req.setBody(simplePostBody(params));
		return req;
	}
	
	@SuppressWarnings("deprecation")
	public static byte[] simplePostBody(NVPair[] nvs){
		StringBuffer sb = new StringBuffer();
		for(NVPair n:nvs){
			sb.append(n.getName());
			sb.append('=');
			sb.append(n.getValue());
			sb.append('&');
		}
		String resultString = sb.toString();
		if(resultString.trim().length()>0)resultString = resultString.substring(0, resultString.length()-1);
		try {
			return URLEncoder.encode(resultString,"utf-8").getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			return URLEncoder.encode(resultString).getBytes();
		}
	}
	
	public static NVPair[] defaultHeader(){
		return DefaultHeader;
	}
	
	private static NVPair[] mergeNvPairs(NVPair[] n1,NVPair[] n2){
		ArrayList<NVPair> list = new ArrayList<NVPair>();
		list.addAll(Arrays.asList(n1));
		for(NVPair n:n2){
			boolean flag = false;
			for(NVPair temp:n1){
				if(n.getName().equalsIgnoreCase(temp.getName())) {
					flag = true;
					break;
				}
			}
			if(!flag) list.add(n);
		}
		return list.toArray(new NVPair[]{});
	}
}
