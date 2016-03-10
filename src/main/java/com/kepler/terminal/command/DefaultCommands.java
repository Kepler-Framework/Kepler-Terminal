package com.kepler.terminal.command;

import java.util.HashMap;
import java.util.Map;

import com.kepler.terminal.Command;
import com.kepler.terminal.CommandWriter;
import com.kepler.terminal.Commands;

/**
 * @author kim
 *
 * 2016年3月9日
 */
public class DefaultCommands implements Commands {

	private static final String WARNING = "Invalid operation";

	private final Map<String, Command> commands = new HashMap<String, Command>();

	private final Command warning = new WarningCommand();

	public DefaultCommands(Command... commands) {
		for (Command each : commands) {
			this.commands.put(each.prefix(), each);
		}
	}

	@Override
	public Command get(String prefix) {
		Command command = this.commands.get(prefix);
		return command != null ? command : this.warning;
	}

	private class WarningCommand implements Command {

		@Override
		public void command(CommandWriter writer, String[] args) throws Exception {
			writer.write(DefaultCommands.WARNING + "\r\n\r\n");
		}

		@Override
		public String prefix() {
			return null;
		}

		@Override
        public String usage() {
	        return null;
        }
	}
}
