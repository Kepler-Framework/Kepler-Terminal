package com.kepler.terminal;

/**
 * @author kim
 *
 * 2016年3月8日
 */
public interface CommandWriter {

	/**
	 * 回写客户端
	 * 
	 * @param data
	 */
	public void write(String data);

	/**
	 * 关闭终端
	 */
	public void close();
}
