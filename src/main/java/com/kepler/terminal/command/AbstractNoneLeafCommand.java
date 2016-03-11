package com.kepler.terminal.command;

import java.util.Arrays;

import com.kepler.terminal.Command;
import com.kepler.terminal.CommandWriter;

/**
 * @author longyaokun
 *
 */
public abstract class AbstractNoneLeafCommand implements Command {

	@Override
	public void command(CommandWriter writer, String[] args) throws Exception {
		if (!this.valid(args)) {
			writer.write(this.usage());
		} else {
			Command command = this.getSubCommand(args);
			if (command != null) {
				command.command(writer, Arrays.copyOfRange(args, 1, args.length));
			} else {
				writer.write(this.usage());
			}
		}

	}

	/**
	 * 获取command
	 * 
	 * @param args
	 * @return
	 */
	protected abstract Command getSubCommand(String[] args);

	/**
	 * 验证参数合法性
	 * 
	 * @param args
	 * @return
	 */
	protected abstract boolean valid(String[] args);

	@Override
	public abstract String prefix();

	@Override
	public abstract String usage();

}
