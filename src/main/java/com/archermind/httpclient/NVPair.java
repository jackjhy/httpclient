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

/**
 * 
 * @author tiger Created on 2010-11-16 14:49:31
 */
public class NVPair {

	private String name;
	private String value;

	/**
	 * default constructor
	 * @param n
	 * @param v
	 */
	public NVPair(String n, String v) {
		this.name = n;
		this.value = v;
	}

	public final String getName() {
		return name;
	}

	public final String getValue() {
		return value;
	}

	/**
	 * Produces a string containing the name and value of this instance.
	 * 
	 * @return a string containing the class name and the name and value
	 */
	public String toString() {
		return getClass().getName() + "[name=" + name + ",value=" + value + "]";
	}

}
