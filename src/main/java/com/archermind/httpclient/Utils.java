package com.archermind.httpclient;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Utils {
	public static byte CR = (byte) '\r';
	public static byte LF = (byte) '\n';
	static final byte b0 = (byte) '0', b9 = (byte) '9';
	static final byte ba = (byte) 'a', bf = (byte) 'f';
	static final byte bA = (byte) 'A', bF = (byte) 'F';
	static final byte SEMI = (byte) ';';

	/** HTTP header definitions */
	public static final String TRANSFER_ENCODING = "Transfer-Encoding";
	public static final String CONTENT_LEN = "Content-Length";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String EXPECT_DIRECTIVE = "Expect";
	public static final String CONN_DIRECTIVE = "Connection";
	public static final String TARGET_HOST = "Host";
	public static final String USER_AGENT = "User-Agent";
	public static final String DATE_HEADER = "Date";
	public static final String SERVER_HEADER = "Server";
	public static final String HTTP_1_1 = "HTTP/1.1";
	public static final String HTTP_1_0 = "HTTP/1.0";

	public static int parseChunkSize(ByteBuffer buffer, int start, int end)
			throws IOException {
		byte[] bufa = buffer.array();
		int size = 0;
		for (int i = start; i < end; i++) {
			byte b = bufa[i];
			if (b >= b0 && b <= b9) {
				size = size * 16 + (b - b0);
			} else if (b >= ba && b <= bf) {
				size = size * 16 + ((b - ba) + 10);
			} else if (b >= bA && b <= bF) {
				size = size * 16 + ((b - bA) + 10);
			} else if (b == CR || b == SEMI) {
				// SEMI-colon starts a chunk extension. We ignore extensions
				// currently.
				break;
			} else {
				throw new IOException(
						"Error parsing chunk size; unexpected char " + b
								+ " at offset " + i);
			}
		}
		return size;
	}

}
