package com.kepler.terminal.command.config;

import java.util.HashMap;
import java.util.Map;

import com.kepler.terminal.Command;
import com.kepler.terminal.command.AbstractNoneLeafCommand;

/**
 * set key value
 * 
 * @author kim
 *
 * 2016年3月8日
 */
public class ConfigCommand extends AbstractNoneLeafCommand {

	private static final String PREFIX = "config";

	private final Map<String, Command> commands = new HashMap<String, Command>();

	public ConfigCommand(Command... commands) {
		super();
		for (Command each : commands) {
			this.commands.put(each.prefix(), each);
		}
	}

	@Override
    protected Command getSubCommand(String[] args) {
	    return this.commands.get(args[0]);
    }

	@Override
    protected boolean valid(String[] args) {
	    return args.length != 0;
    }

	@Override
	public String prefix() {
		return ConfigCommand.PREFIX;
	}
	
	@Override
    public String usage() {
		return "config only support: " + this.commands.keySet().toString(); 
    }

}
