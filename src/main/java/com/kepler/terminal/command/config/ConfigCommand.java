package com.kepler.terminal.command.config;

import java.util.HashMap;
import java.util.Map;

import com.kepler.terminal.Command;
import com.kepler.terminal.CommandWriter;

/**
 * set key value
 * 
 * @author kim
 *
 * 2016年3月8日
 */
public class ConfigCommand implements Command {

	private static final String WARNING = "Not supported operation: ";

	private static final String PREFIX = "config";

	private final Map<String, Command> commands = new HashMap<String, Command>();

	public ConfigCommand(Command... commands) {
		super();
		for (Command each : commands) {
			this.commands.put(each.prefix(), each);
		}
	}

	@Override
	public void command(CommandWriter writer, String[] args) throws Exception {		
		if(args.length <= 1){
			writer.write(this.subCommands() + "\r\n");
			return;
		}
		// 获取分支
		Command command = this.commands.get(args[1]);
		if (command != null) {
			command.command(writer, args);
		} else {
			// 提示不支持操作
			writer.write(ConfigCommand.WARNING + args[1] + "\r\n");
		}
	}

	@Override
	public String prefix() {
		return ConfigCommand.PREFIX;
	}
	
	private String subCommands(){
		return "config only support: " + this.commands.keySet().toString(); 
		
	}
}
