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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kilim.Pausable;
import kilim.http.IntList;
import kilim.http.Utils;
import kilim.nio.EndPoint;
import static com.archermind.httpclient.Utils.*;

/**
 * 
 * @author tiger Created on 2010-11-9 15:34:13
 */
public class HttpResponse extends HttpMsg {

	public int statusRange;
	public int versionRange;
	private String[] keys;
	private int[] valueRanges;
	ByteBuffer buffer;
	public int nFields;

	public int contentOffset;
	public int contentLength;

	/**
	 * The read cursor, used in the read* methods.
	 */
	public int iread;

	public HttpResponse() {
		keys = new String[5];
		valueRanges = new int[5];
	}

	public void readHead(EndPoint ep) throws IOException, Pausable {
		buffer = ByteBuffer.allocate(1024);
		int n;
		int headerLength = 0;
		do {
			n = readLine(ep); // includes 2 bytes for CRLF
			headerLength += n;
		} while (n > 2); // until blank line (CRLF)
		HttpResponseHeaderParser.initHeader(this, headerLength);
		contentOffset = headerLength; // doesn't mean there's necessarily any
										// content.
		String cl = getHeader("Content-Length");
		if (cl.length() > 0) {
			try {
				contentLength = Integer.parseInt(cl);
			} catch (NumberFormatException nfe) {
				throw new IOException("Malformed Content-Length hdr");
			}
		} else if ((getHeader("Transfer-Encoding").indexOf("chunked") >= 0)
				|| (getHeader("TE").indexOf("chunked") >= 0)) {
			contentLength = -1;
		} else {
			contentLength = 0;
		}

	}

	public void readBody(EndPoint ep) throws IOException, Pausable {
		iread = contentOffset;
		if (contentLength > 0) {
			fill(ep, contentOffset, contentLength);
			iread = contentOffset + contentLength;
		} else if (contentLength == -1) {
			// CHUNKED
			readAllChunks(ep);
		}
		readTrailers(ep);

	}
	
	public InputStream content(){
		return new ByteArrayInputStream(buffer.array(), contentOffset, contentLength);
	}
	
	public String status(){
		return extractRange(statusRange);
	}

	public String version(){
		return extractRange(versionRange);
	}
	
	public String contentType(){
		return getHeader(CONTENT_TYPE);
	}
	
	public String contentEncoding(){
		return getHeader(CONTENT_ENCODING);
	}
	
	private final Pattern charsetRegex = Pattern.compile("charset=([0-9A-Za-z-]+)");
	
	public String charset(){
		String contentType = contentType();
		String charset = "utf-8";
		Matcher matcher = this.charsetRegex.matcher(contentType);
        if (matcher.find()) {
            charset = matcher.group(1);
        }
        return charset;
	}
	
	public String getContentToString() throws Exception{
		String charset = charset();
		if(!"".equals(contentEncoding()))throw new Exception("content had been encoding, you should decode it by yourself");
		return new String(buffer.array(),contentOffset,contentLength,charset);
	}
	
    private void readTrailers(EndPoint endpoint) {
    	
    }

	private int readLine(EndPoint ep) throws IOException, Pausable {
		int ireadSave = iread;
		int i = ireadSave;
		while (true) {
			int end = buffer.position();
			byte[] bufa = buffer.array();
			for (; i < end; i++) {
				if (bufa[i] == CR) {
					++i;
					if (i >= end) {
						buffer = ep.fill(buffer, 1);
						bufa = buffer.array(); // fill could have changed the
												// buffer.
						end = buffer.position();
					}
					if (bufa[i] != LF) {
						throw new IOException("Expected LF at " + i);
					}
					++i;
					int lineLength = i - ireadSave;
					iread = i;
					return lineLength;
				}
			}
			buffer = ep.fill(buffer, 1); // no CRLF found. fill a bit more
											// and start over.
		}

	}

	// topup if request's buffer doesn't have all the bytes yet.
	public void fill(EndPoint endpoint, int offset, int size)
			throws IOException, Pausable {
		int total = offset + size;
		int currentPos = buffer.position();
		if (total > buffer.position()) {
			buffer = endpoint.fill(buffer, (total - currentPos));
		}
	}

	private void readAllChunks(EndPoint ep) throws IOException, Pausable {

		IntList chunkRanges = new IntList(); // alternate numbers in this list
												// refer to the start and end
												// offsets of chunks.
		do {
			int n = readLine(ep); // read chunk size text into buffer
			int beg = iread;
			int size = parseChunkSize(buffer, iread - n, iread); // Parse size
																	// in hex,
																	// ignore
																	// extension
			if (size == 0)
				break;
			// If the chunk has not already been read in, do so
			fill(ep, iread, size + 2 /* chunksize + CRLF */);
			// record chunk start and end
			chunkRanges.add(beg);
			chunkRanges.add(beg + size); // without the CRLF
			iread += size + 2; // for the next round.
		} while (true);

		// / consolidate all chunkRanges
		if (chunkRanges.numElements == 0) {
			contentLength = 0;
			return;
		}
		contentOffset = chunkRanges.get(0); // first chunk's beginning
		int endOfLastChunk = chunkRanges.get(1); // first chunk's end

		byte[] bufa = buffer.array();
		for (int i = 2; i < chunkRanges.numElements; i += 2) {
			int beg = chunkRanges.get(i);
			int chunkSize = chunkRanges.get(i + 1) - beg;
			System.arraycopy(bufa, beg, bufa, endOfLastChunk, chunkSize);
			endOfLastChunk += chunkSize;
		}
		// TODO move all trailer stuff up
		contentLength = endOfLastChunk - contentOffset;

		// At this point, the contentOffset and contentLen give the entire
		// content
	}

	public void addField(String key, int valRange) {
		if (keys.length == nFields) {
			keys = (String[]) Utils.growArray(keys, 5);
			valueRanges = Utils.growArray(valueRanges, 5);
		}
		keys[nFields] = key;
		valueRanges[nFields] = valRange;
		nFields++;
	}

	/**
	 * Get the value for a given key
	 * 
	 * @param key
	 * @return null if the key is not present in the header.
	 */
	public String getHeader(String key) {
		for (int i = 0; i < nFields; i++) {
			if (key.equalsIgnoreCase(keys[i])) {
				return extractRange(valueRanges[i]);
			}
		}
		return ""; // no point returning null
	}

	// complement of HttpRequestParser.encodeRange
	public String extractRange(int range) {
		int beg = range >> 16;
		int end = range & 0xFFFF;
		return extractRange(beg, end);
	}

	public String extractRange(int beg, int end) {
		return new String(buffer.array(), beg, (end - beg));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(500);
		for (int i = 0; i < nFields; i++) {
			sb.append(keys[i]).append(": ")
					.append(extractRange(valueRanges[i])).append('\n');
		}
		return sb.toString();
	}

}
