package com.kepler.terminal.command.config;

import java.util.Map;

import com.kepler.config.Config;
import com.kepler.config.ConfigSync;
import com.kepler.config.PropertiesUtils;
import com.kepler.terminal.Command;
import com.kepler.terminal.CommandWriter;

/**
 * 修改指定参数
 * 
 * @author longyaokun
 *
 * 2016年3月9日
 */
public class SetCommand implements Command {

	private static final String PREFIX = "set";

	private static final String USAGE = "Usage:config set key value";
	private final ConfigSync sync;

	private final Config config;

	public SetCommand(ConfigSync sync, Config config) {
		super();
		this.sync = sync;
		this.config = config;
	}

	public void command(CommandWriter writer, String[] args) throws Exception {
		if(args.length < 4){
			writer.write(SetCommand.USAGE + "\r\n");
			return;
		}
		// 获取内存快照
		Map<String, String> configs = PropertiesUtils.memory();
		// 修改内存快照
		configs.put(args[2], args[3]);
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
}