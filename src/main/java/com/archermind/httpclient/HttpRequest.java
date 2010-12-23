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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import static com.archermind.httpclient.Utils.*;

/**
 * 
 * @author tiger Created on 2010-11-9 11:21:39
 */
public class HttpRequest extends HttpMsg {

	private String method;
	private String path;
	private NVPair[] header;
	private byte[] body;
	private String version;
	private String host;

	public boolean keepAlive() {
		if(header==null) return true;
		for (NVPair nv : header) {
			if (nv.getName().equalsIgnoreCase(CONN_DIRECTIVE)
					&& nv.getValue().toLowerCase().indexOf("close") > -1)
				return false;
		}
		return true;
	}

	public String getHost() {
		return host;
	}

	public HttpRequest(URL url, String method, String version) {
		this(url.getPath(),url.getHost()
				+ ((url.getPort() != -1) ? (":" + url.getPort()) : "")
				, method, version);
	}

	public HttpRequest(URL url, String method) {
		this(url, method, HTTP_1_1);
	}

	private HttpRequest(String path, String host, String method, String version) {
		this.method = method;
		this.path = path;
		this.version = version;
		this.host = host;
		
		if(path==null||path.length()==0)this.path="/";
	}

	public void setHeader(NVPair[] header) {
		this.header = header;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public ByteBuffer toBuffer() throws IOException {
		ExposedBaos bufferStream = new ExposedBaos();
		writeTo(bufferStream);
		return bufferStream.toByteBuffer();
	}

	private void writeTo(OutputStream os) throws IOException {
		DataOutputStream dos = new DataOutputStream(os);
		dos.write(method.toUpperCase().getBytes());
		dos.write(" ".getBytes());
		dos.write(path.getBytes());
		dos.write(" ".getBytes());
		dos.write(version.getBytes());
		dos.write(CR);
		dos.write(LF);

		dos.write(TARGET_HOST.getBytes());
		dos.write(": ".getBytes());
		dos.write(host.getBytes());
		dos.write(CR);
		dos.write(LF);

		if (header != null) {
			for (NVPair e : header) {
				dos.write(e.getName().getBytes());
				dos.write(": ".getBytes());
				dos.write(e.getValue().getBytes());
				dos.write(CR);
				dos.write(LF);
			}
		}

		if (body != null) {
			dos.write(CONTENT_LEN.getBytes());
			dos.write(": ".getBytes());
			dos.write(body.length);
			dos.write(CR);
			dos.write(LF);
		}

		dos.write(CR);
		dos.write(LF);

		if (body != null)
			dos.write(body);

	}
}
