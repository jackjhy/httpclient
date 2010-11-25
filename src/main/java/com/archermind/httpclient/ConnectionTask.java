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

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import kilim.Mailbox;
import kilim.Pausable;
import kilim.nio.EndPoint;
import kilim.nio.SessionTask;

/**
 * 
 * @author tiger
 * Created on 2010-11-8 14:29:45
 */
public class ConnectionTask extends SessionTask{

    Mailbox<EndPoint> mbx;
    private String host;
    private int port = 80;
    private BackendScheduler sc = null;

    public ConnectionTask(String host,Mailbox<EndPoint> mbx,BackendScheduler sc){
    	int n = host.indexOf(":");
    	if(n>0){
    		this.host = host.substring(0,n);
    		this.port = Integer.parseInt(host.substring(n+1, host.length()));
    	}else this.host = host;
        this.mbx = mbx;
        this.sc = sc;
    }

    @Override
    public void execute() throws Pausable, Exception {
        SocketChannel sch = SocketChannel.open();
        sch.connect(new InetSocketAddress(host,port));
        sch.configureBlocking(false);
        this.setEndPoint(new EndPoint(sc.registrationMbx, sch));
        mbx.put(getEndPoint());
    }
    
    public static void main(String[] args){
    	String ss = "abc:80";
    	int n = ss.indexOf(":");
    	System.out.println(ss.substring(0,n));
    	System.out.println(ss.substring(n+1,ss.length()));
    }

}
