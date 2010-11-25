/*
 *  你看，你看，我的程序
 *  http://www.@!#!&.com
 *  jackjhy@gmail.com
 * 
 *  听说牛粪离钻石只有一步之遥，听说稻草离金条只有一步之遥，
 *  听说色情离艺术只有一步之遥，听说裸体离衣服只有一步之遥，
 *  听说龙芯离AMD只有一步之遥，听说神舟离月球只有一步之遥，
 *  听说现实离乌邦只有一步之遥，听说社会离共产只有一步之遥，
 *  听说台湾离独立只有一步之遥，听说日本离玩完只有一步之遥，
 */

package com.archermind.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import kilim.Mailbox;
import kilim.Pausable;
import kilim.nio.EndPoint;

/**
 * 
 * @author tiger Created on 2010-11-5 14:49:31
 */
public class HttpClient {

	// 保持到同一服务器的tcp连接可复用
	private ConcurrentHashMap<String, ArrayList<EndPoint>> endpointsPool = null;

	private BackendScheduler sc = null;

	public HttpClient(ConcurrentHashMap<String, ArrayList<EndPoint>> eps,
			BackendScheduler bsc) {
		this.sc = bsc;
		this.endpointsPool = eps;
	}

	public HttpResponse doRequest(HttpRequest req) throws Pausable, Exception {

		Mailbox<HttpMsg> mbx = new Mailbox<HttpMsg>(1);
		EndPoint ep = getEndPoint(req.getHost());
		new RequestTask(mbx, ep,req).start();
		HttpMsg msg = mbx.get();
		
		if(msg instanceof ErrorMsg){
			throw ((ErrorMsg)msg).getException();
		}
		if (req.keepAlive())
			keepEndPoint(ep, req.getHost());
		else
			releaseEndPoint(ep);
		return (HttpResponse)msg;
	}

	
//	public HttpResponse doRequest(HttpRequest req) throws Pausable, Exception {
//
//		Mailbox<HttpMsg> mbx = new Mailbox<HttpMsg>(1);
//		EndPoint ep = getEndPoint(req.getHost());
//		new RequestTask(mbx, ep,req).start();
//		HttpResponse resp = (HttpResponse) mbx.get();
//		if (req.keepAlive())
//			keepEndPoint(ep, req.getHost());
//		else
//			releaseEndPoint(ep);
//		return resp;
//	}
	
//	class RequestTask extends Task{
//		
//		@Override
//		public void execute() throws Pausable, Exception {
//			// TODO Auto-generated method stub
//			super.execute();
//		}
//		
//	}

	public HttpResponse doRequestnb(HttpRequest req) throws Pausable, IOException {

		Mailbox<HttpMsg> mbx = new Mailbox<HttpMsg>(1);
		EndPoint ep = getEndPoint(req.getHost());
		System.out.println("get ep");
		new RequestTask(mbx, ep,req).start();
		HttpResponse resp = (HttpResponse) mbx.getnb();
		if(resp==null) return null;
		
		if (req.keepAlive())
			keepEndPoint(ep, req.getHost());
		else
			releaseEndPoint(ep);
		return resp;
	}
	
	private synchronized void keepEndPoint(EndPoint ep, String host) {
		ArrayList<EndPoint> eps = endpointsPool.get(host);
		if (eps == null)
			eps = new ArrayList<EndPoint>();
		eps.add(ep);
		endpointsPool.put(host, eps);
	}

	private void releaseEndPoint(EndPoint ep) {
		ep.close();
	}

	private EndPoint getEndPoint(String host) throws Pausable {
		ArrayList<EndPoint> eps = endpointsPool.get(host);
		EndPoint result = null;
		if (eps != null) {
			while (eps.size() > 0) {
				result = eps.remove(eps.size() - 1);
				if (checkEndPoint(result))
					return result;
				else
					continue;
			}
		}

		Mailbox<EndPoint> mbx = new Mailbox<EndPoint>(1);
		new ConnectionTask(host, mbx, sc).start();
		return mbx.get();
	}

	private boolean checkEndPoint(EndPoint ep) {
		return true;
	}

}
