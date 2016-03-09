package com.kepler.terminal;


/**
 * @author kim
 *
 * 2016年3月8日
 */
public interface Commands {

	/**
	 * 获取指定前缀Command
	 * 
	 * @param prefix
	 * @return
	 */
	public Command get(String prefix);
}
