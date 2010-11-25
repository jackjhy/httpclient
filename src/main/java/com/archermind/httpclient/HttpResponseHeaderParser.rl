package com.archermind.httpclient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class HttpResponseHeaderParser {
public static final Charset UTF8 = Charset.forName("UTF-8");

	%%{
		machine http_header_parser;
		
		action mark {mark = fpc; }
		
		action extract_field_name { 
      		field_name = kw_lookup(data, mark, fpc);
      		if (field_name == null) {// not a known keyword
        		field_name = resp.extractRange(mark, fpc);
      		}
    	}
    	
    	action status{
    		resp.statusRange = encodeRange(mark, fpc);
    	}
    	
    	action extract_value {
      		int value = encodeRange(mark, fpc);
      		resp.addField(field_name, value);
    	}
    	
    	action version {
      		resp.versionRange = encodeRange(mark, fpc);
    	}
    	
	    CRLF = "\r\n";

	    # character types
    	CTL = (cntrl | 127);
    	safe = ("$" | "-" | "_" | ".");
    	extra = ("!" | "*" | "'" | "(" | ")" | ",");
    	reserved = (";" | "/" | "?" | ":" | "@" | "&" | "=" | "+");
    	unsafe = (CTL | " " | "\"" | "#" | "%" | "<" | ">");
    	national = any -- (alpha | digit | reserved | extra | safe | unsafe);
    	unreserved = (alpha | digit | safe | extra | national);
    	escape = ("%" xdigit xdigit);
    	uchar = (unreserved | escape);
    	pchar = (uchar | ":" | "@" | "&" | "=" | "+");
    	tspecials = ("(" | ")" | "<" | ">" | "@" | "," | ";" | ":" | "\\" | "\"" | "/" | "[" | "]" | "?" | "=" | "{" | "}" | " " | "\t");
    	
	    # elements
    	token = (ascii -- (CTL | tspecials));

	    field_name = ( any -- ":" )+ >mark %extract_field_name;
    	field_value = any* >mark %extract_value;
    	fields = field_name ":" " "* field_value :> CRLF;
    	
	    version = "HTTP/"  ( digit+ "." digit+ )  >mark %version;
    	status = digit+ >mark %status;
    	desc = (alpha | digit)*;

    	start_line = ( version " "+ status " "+ desc CRLF ) ;
    	header = start_line ( fields )* CRLF ;
    	main := header %err{err("Malformed Header. Error at " + p + "\n" + new String(data, 0, pe, UTF8));};
    	
	}%%
	
	%% write data;
	
	public static void err(String msg) throws IOException{
    	throw new IOException(msg);
  	}
  	
  	public static void initHeader(HttpResponse resp, int headerLength) throws IOException {
    ByteBuffer bb = resp.buffer;
    /* required variables */
    byte[] data = bb.array();
    int p = 0;
    int pe = headerLength;
    int cs = 0;

    // variables used by actions in http_resp_parser machine above.
    int query_start = 0;
    int mark = 0;
    String field_name = "";

    %% write init;
    %% write exec;
    
    if (cs == http_header_parser_error) {
      throw new IOException("Malformed HTTP Header. p = " + p +", cs = " + cs);
    }
  }
  	
  	/**
	 * encode the start pos and length as ints;
 	*/
  	public static int encodeRange(int start, int end) {
    	return (start << 16) + end ;
  	}
  	
  %%{
    machine http_keywords;

    main := |*
    'Accept-Ranges'i => { kw = "Accept-Ranges";};
    'Age'i => { kw = "Age";};
    'Allow'i => { kw = "Allow";};
    'Cache-Control'i => { kw = "Cache-Control";};
    'Connection'i => { kw = "Connection";};
    'Content-Encoding'i => { kw = "Content-Encoding";};
    'Content-Language'i => { kw = "Content-Language";};
    'Content-Length'i => { kw = "Content-Length";};
    'Content-Location'i => { kw = "Content-Location";};
    'Content-MD5'i => { kw = "Content-MD5";};
    'Content-Range'i => { kw = "Content-Range";};
    'Content-Type'i => { kw = "Content-Type";};
    'Date'i => { kw = "Date";};
    'ETag'i => { kw = "ETag";};
    'Expires'i => { kw = "Expires";};
    'Last-Modified'i => { kw = "Last-Modified";};
    'Location'i => { kw = "Location";};
    'Pragma'i => { kw = "Pragma";};
    'Proxy-Authenticate'i => { kw = "Proxy-Authenticate";};
    'Retry-After'i => { kw = "Retry-After";};
    'Server'i => { kw = "Server";};
    'Trailer'i => { kw = "Trailer";};
    'Transfer-Encoding'i => { kw = "Transfer-Encoding";};
    'Upgrade'i => { kw = "Upgrade";};
    'Vary'i => { kw = "Vary";};
    'Via'i => { kw = "Via";};
    'Warning'i => { kw = "Warning";};
    'WWW-Authenticate'i => { kw = "WWW-Authenticate";};
    *|;

    write data;
  }%%

  @SuppressWarnings("unused")
  public static String kw_lookup(byte[] data, int start, int len) {
    int ts, te, act;

    int p = start;
    int pe = start + len;
    int eof = pe;
    int cs;
    String kw = null;
    %% write init;
    %% write exec;

    return kw;
  }
  	
  public static String crlf = "\r\n";
  public static void main(String args[]) throws Exception {
    /// Testing
    String s = 
      "HTTP/1.1 200 OK\r\n" +
      "Date: Mon, 22 Nov 2010 06:50:42 GMT\r\n" +
      "Content-Encoding: gzip\r\n" +
      "Content-Type: text/javascript;charset=UTF-8\r\n" +
      "Keep-Alive: 300\r\n" +
      "Connection: keep-alive\r\n\r\n";
    System.out.println("Input Resp: (" + s.length() + " bytes)");System.out.println(s);
    byte[] data = s.getBytes();
    int len = data.length;
    
    System.out.println("=============================================================");
    HttpResponse resp = new HttpResponse();
    resp.buffer = ByteBuffer.allocate(2048);
    resp.buffer.put(data);
    initHeader(resp, len);
    System.out.println(resp);
  }  	
  	
}