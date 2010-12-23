package com.archermind.httpclient;

import java.net.URL;

import kilim.Pausable;
import kilim.Task;

import static com.archermind.httpclient.HttpRequestHelper.*;

public class HttpClientHelper {
	
	public static void get(final URL url, final HttpClient client,final ResponseCallable callable){
		Task task = new Task() {
			@Override
			public void execute() throws Pausable, Exception {
				HttpResponse resp = client.doRequest(simpleGetRequest(url));
				callable.call(resp);
			}
		};
		task.start();
	}

	public static void get(final URL url,final NVPair[] headers, final HttpClient client,final ResponseCallable callable){
		Task task = new Task() {
			@Override
			public void execute() throws Pausable, Exception {
				HttpResponse resp = client.doRequest(getRequest(url,headers));
				callable.call(resp);
			}
		};
		task.start();
	}

	public static void get(final URL url, final HttpClient client,final long timeoutMills,final ResponseCallable callable){
		Task task = new Task() {
			@Override
			public void execute() throws Pausable, Exception {
				HttpResponse resp = client.doRequest(simpleGetRequest(url),timeoutMills);
				callable.call(resp);
			}
		};
		task.start();
	}
	
	public static void get(final URL url,final NVPair[] headers, final HttpClient client,final long timeoutMills,final ResponseCallable callable){
		Task task = new Task() {
			@Override
			public void execute() throws Pausable, Exception {
				HttpResponse resp = client.doRequest(getRequest(url,headers),timeoutMills);
				callable.call(resp);
			}
		};
		task.start();
	}

	public static void post(final URL url,final NVPair[] params, final HttpClient client,final ResponseCallable callable){
		Task task = new Task() {
			@Override
			public void execute() throws Pausable, Exception {
				HttpResponse resp = client.doRequest(simplePostRequest(url, params));
				callable.call(resp);
			}
		};
		task.start();
	}

	public static void post(final URL url,final NVPair[] headers,final NVPair[] params, final HttpClient client,final ResponseCallable callable){
		Task task = new Task() {
			@Override
			public void execute() throws Pausable, Exception {
				HttpResponse resp = client.doRequest(postRequest(url,headers,params));
				callable.call(resp);
			}
		};
		task.start();
	}

	public static void post(final URL url, final NVPair[] params,final HttpClient client,final long timeoutMills,final ResponseCallable callable){
		Task task = new Task() {
			@Override
			public void execute() throws Pausable, Exception {
				HttpResponse resp = client.doRequest(simplePostRequest(url, params),timeoutMills);
				callable.call(resp);
			}
		};
		task.start();
	}
	
	public static void post(final URL url,final NVPair[] headers, final NVPair[] params,final HttpClient client,final long timeoutMills,final ResponseCallable callable){
		Task task = new Task() {
			@Override
			public void execute() throws Pausable, Exception {
				HttpResponse resp = client.doRequest(postRequest(url,headers,params),timeoutMills);
				callable.call(resp);
			}
		};
		task.start();
	}

}
