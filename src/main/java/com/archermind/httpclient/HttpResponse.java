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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	ByteBuffer buffertmp;
	public int nFields;

	private int tmpIRead;
	private int swapSize;

	public int contentOffset;
	public int contentLength;

	public boolean isSavedAsTempFile = false;
	private static boolean haveTmpPath = false;
	static {
		if (HttpResponseHelper.getTmpDirPath() != null
				&& HttpResponseHelper.getTmpDirPath().trim().length() > 0)
			haveTmpPath = true;
	}
	private static int MAX_CONTENT_LENGTH_IN_MEMORY = 1024 * 1024 * 10;

	static {
		if (HttpResponseHelper.getMaxContentLengthInMemory() != null
				&& HttpResponseHelper.getMaxContentLengthInMemory().trim()
						.length() > 0)
			try {
				MAX_CONTENT_LENGTH_IN_MEMORY = Integer
						.parseInt(HttpResponseHelper
								.getMaxContentLengthInMemory().trim());
			} catch (NumberFormatException e) {
			}
	}

	public File tempFile;
	private OutputStream tmpfilewrite;

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
			if (contentLength > MAX_CONTENT_LENGTH_IN_MEMORY && haveTmpPath) {
				isSavedAsTempFile = true;
				buffertmp = ByteBuffer.allocate(MAX_CONTENT_LENGTH_IN_MEMORY);
				tempFile = new File(HttpResponseHelper.getTmpDirPath()
						+ File.separator + "htmp_" + Math.random());
				tmpfilewrite = new FileOutputStream(tempFile);
				tmpfilewrite.write(buffer.array(), contentOffset,
						buffer.position() - contentOffset);
			}
			if (!isSavedAsTempFile) {
				fill(ep, contentOffset, contentLength);
				iread = contentOffset + contentLength;
			} else {
				fill(ep, 0, contentLength +contentOffset- buffer.position());
				tmpfilewrite.flush();
				tmpfilewrite.close();
				buffertmp.clear();
			}
		} else if (contentLength == -1) {
			// CHUNKED
			readAllChunks(ep);
		}
		readTrailers(ep);

	}

	public InputStream content() throws FileNotFoundException {
		if (isSavedAsTempFile)
			return new FileInputStream(tempFile);
		return new ByteArrayInputStream(buffer.array(), contentOffset,
				contentLength);
	}

	public File getTempFile() throws Exception {
		if (isSavedAsTempFile)
			return tempFile;
		throw new Exception("no temp file");
	}

	public String status() {
		return extractRange(statusRange);
	}

	public String version() {
		return extractRange(versionRange);
	}

	public String contentType() {
		return getHeader(CONTENT_TYPE);
	}

	public String contentEncoding() {
		return getHeader(CONTENT_ENCODING);
	}

	private final Pattern charsetRegex = Pattern
			.compile("charset=([0-9A-Za-z-]+)");

	public String charset() {
		String contentType = contentType();
		String charset = "utf-8";
		Matcher matcher = this.charsetRegex.matcher(contentType);
		if (matcher.find()) {
			charset = matcher.group(1);
		}
		return charset;
	}

	public String getContentToString() throws Exception {
		String charset = charset();
		if (!"".equals(contentEncoding()))
			throw new Exception(
					"content had been encoding, you should decode it by yourself");
		if (isSavedAsTempFile)
			throw new Exception(
					"content had been saved as temp file,plz use content() to get inputstream");
		return new String(buffer.array(), contentOffset, contentLength, charset);
	}

	private void readTrailers(EndPoint endpoint) {

	}

	private int readLine(EndPoint ep) throws IOException, Pausable {
		if (isSavedAsTempFile) {
			int ireadSave = tmpIRead;
			int i = ireadSave;
			while (true) {
				int end = buffertmp.position();
				byte[] bufa = buffertmp.array();
				for (; i < end; i++) {
					if (bufa[i] == CR) {
						++i;
						if (i >= end) {
							buffertmp = ep.fill(buffertmp, 1);
							bufa = buffertmp.array(); // fill could have changed
														// the
														// buffer.
							end = buffertmp.position();
						}
						if (bufa[i] != LF) {
							throw new IOException("Expected LF at " + i);
						}
						++i;
						int lineLength = i - ireadSave;
						tmpIRead = i;
						swapSize = buffertmp.position() - tmpIRead;
						return lineLength;
					}
				}
				buffertmp = ep.fill(buffertmp, 1); // no CRLF found. fill a bit
													// more
													// and start over.
			}
		} else {
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
							bufa = buffer.array(); // fill could have changed
													// the
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
	}

	// topup if request's buffer doesn't have all the bytes yet.
	public void fill(EndPoint endpoint, int offset, int size)
			throws IOException, Pausable {
		int currentPos = isSavedAsTempFile ? buffertmp.position() : buffer
				.position();
		int total = offset + size;

		if (isSavedAsTempFile) {
			int remnant = total - currentPos;
			if (remnant <= 0)
				return;
			while (remnant > MAX_CONTENT_LENGTH_IN_MEMORY) {
				buffertmp.clear();
				buffertmp = endpoint.fill(buffertmp,
						MAX_CONTENT_LENGTH_IN_MEMORY);
				remnant = remnant - buffertmp.position();
				if (remnant <= 0) {
					tmpfilewrite.write(buffertmp.array(), 0, remnant
							+ buffertmp.position());
					tmpIRead = remnant + buffertmp.position();
					return;
				} else
					tmpfilewrite.write(buffertmp.array(), 0,
							buffertmp.position());
			}
			buffertmp.clear();
			buffertmp = endpoint.fill(buffertmp, remnant);
			tmpfilewrite.write(buffertmp.array(), 0, remnant);
			tmpIRead = remnant;
		} else {
			if (total > buffer.position()) {
				buffer = endpoint.fill(buffer, (total - currentPos));
			}
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
			if (size == 0) {
				break;
			}
			if (isSavedAsTempFile) {
				if (swapSize >= size + 2) {
					tmpfilewrite.write(buffertmp.array(), tmpIRead, size);
					tmpIRead += size + 2;
					continue;
				} else {
					tmpfilewrite.write(buffertmp.array(), tmpIRead, swapSize);
				}
			}

			// If the chunk has not already been read in, do so
			if (!isSavedAsTempFile && haveTmpPath
					&& size + 2 + iread > buffer.position()
					&& iread + size + 2 > MAX_CONTENT_LENGTH_IN_MEMORY) {
				isSavedAsTempFile = true;
				tempFile = new File(HttpResponseHelper.getTmpDirPath()
						+ File.separator + "htmp_" + Math.random());
				tmpfilewrite = new FileOutputStream(tempFile);
				buffertmp = ByteBuffer.allocate(MAX_CONTENT_LENGTH_IN_MEMORY);
				// move readed bytes to tmp file
				if (chunkRanges.numElements != 0) {
					byte[] bufa = buffer.array();
					for (int i = 0; i < chunkRanges.numElements; i += 2) {
						int begi = chunkRanges.get(i);
						int chunkSize = chunkRanges.get(i + 1) - begi;
						tmpfilewrite.write(bufa, begi, chunkSize);
					}
				}
				tmpfilewrite.write(buffer.array(), iread, buffer.position()-iread);
				swapSize = buffer.position() - iread;
			}
			if (isSavedAsTempFile) {
				buffertmp.clear();
				fill(ep, 0, size + 2 - swapSize /* chunksize + CRLF */);
			} else {
				fill(ep, iread, size + 2 /* chunksize + CRLF */);
				// record chunk start and end
				chunkRanges.add(beg);
				chunkRanges.add(beg + size); // without the CRLF
				iread += size + 2; // for the next round.
			}
		} while (true);

		// / consolidate all chunkRanges
		if (!isSavedAsTempFile) {
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
		} else {
			tmpfilewrite.flush();
			tmpfilewrite.close();
			buffertmp.clear();
			contentLength = (int) tempFile.length();
		}

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
