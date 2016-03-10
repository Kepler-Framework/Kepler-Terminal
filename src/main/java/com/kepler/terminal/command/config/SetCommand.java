package com.kepler.terminal.command.config;

import java.util.Arrays;
import java.util.Map;

import com.kepler.config.Config;
import com.kepler.config.ConfigSync;
import com.kepler.config.PropertiesUtils;
import com.kepler.org.apache.commons.lang.StringUtils;
import com.kepler.terminal.CommandWriter;
import com.kepler.terminal.command.AbstractLeafCommand;

/**
 * 修改指定参数
 * 
 * @author longyaokun
 *
 *         2016年3月9日
 */
public class SetCommand extends AbstractLeafCommand {

	private static final String PREFIX = "set";

	private static final String USAGE = "Usage:config set key value";

	private final ConfigSync sync;

	private final Config config;

	public SetCommand(ConfigSync sync, Config config) {
		super();
		this.sync = sync;
		this.config = config;
	}

	@Override
	protected boolean valid(String[] args) {
		return args.length >= 2;
	}

	private String value(String[] args) {
		return StringUtils.join(args, " ");
	}

	@Override
	protected void execute(CommandWriter writer, String[] args) throws Exception {
		// 获取内存快照
		Map<String, String> configs = PropertiesUtils.memory();
		// 修改内存快照
		configs.put(args[0], this.value(Arrays.copyOfRange(args, 1, args.length)));
		// 通知新配置
		this.config.config(configs);
		// 同步ZK
		this.sync.sync();
		writer.write("\r\n\r\n");

	}

	@Override
	public String prefix() {
		return SetCommand.PREFIX;
	}

	@Override
	public String usage() {
		return SetCommand.USAGE;
	}

}
