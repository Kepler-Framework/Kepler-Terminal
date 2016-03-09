package com.kepler.terminal.command;

import com.kepler.terminal.Command;
import com.kepler.terminal.CommandWriter;

/**
 * exit 关闭终端
 * 
 * @author kim
 *
 * 2016年3月8日
 */
public class ExitCommand implements Command {

	private static final String PREFIX = "exit";

	@Override
	public void command(CommandWriter writer, String[] args) throws Exception {
		writer.close();
	}

	@Override
	public String prefix() {
		return ExitCommand.PREFIX;
	}
}
