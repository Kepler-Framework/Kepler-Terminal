package com.kepler.terminal;

/**
 * @author kim
 *
 * 2016年3月8日
 */
public interface Command {

	/**
	 * @param writer 回写客户端
	 * @param args 请求参数
	 * @throws Exception
	 */
	public void command(CommandWriter writer, String[] args) throws Exception;

	/**
	 * 前缀
	 * @return
	 */
	public String prefix();
}
