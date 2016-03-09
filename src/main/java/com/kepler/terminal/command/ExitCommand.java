package com.kepler.terminal.command;

import com.kepler.terminal.CommandWriter;

/**
 * exit 关闭终端
 * 
 * @author kim
 *
 * 2016年3月8日
 */
public class ExitCommand extends AbstractLeafCommand {

	private static final String PREFIX = "exit";

	@Override
	public String prefix() {
		return ExitCommand.PREFIX;
	}

	@Override
    public String usage() {
	    return null;
    }

	@Override
    protected boolean valid(String[] args) {
	    return true;
    }

	@Override
    protected void execute(CommandWriter writer, String[] args) throws Exception {
		writer.close();	    
    }
}
