package com.kepler.terminal.command.config;

import java.util.Map;

import com.kepler.config.PropertiesUtils;
import com.kepler.terminal.CommandWriter;
import com.kepler.terminal.command.AbstractLeafCommand;

/**
 * 获取参数集合
 * 
 * @author kim
 *
 * 2016年3月9日
 */
public class GetCommand extends AbstractLeafCommand {

	private static final String PREFIX = "get";
	
	private static final String USAGE = "Usage:config get [key1] [key2] [key3]...";
	
	@Override
    protected boolean valid(String[] args) {
	    return true;
    }
	
	@Override
    protected void execute(CommandWriter writer, String[] args) throws Exception {
		Map<String, String> configs = PropertiesUtils.memory();
		writer.write(args.length > 0 ? this.some(configs, args) : this.all(configs));
    }

	private String some(Map<String, String> configs, String[] args){
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < args.length; i++){
			buffer.append(args[i]).append("\t\t").append(configs.get(args[i].toLowerCase())).append("\r\n\r\n");
		}
		return buffer.toString();
	}
	
	private String all(Map<String, String> configs){
		StringBuffer buffer = new StringBuffer();
		for (String key : configs.keySet()) {
			buffer.append(key).append("\t\t").append(configs.get(key)).append("\r\n\r\n");
		}
		return buffer.toString();
	}
	
	@Override
	public String prefix() {
		return GetCommand.PREFIX;
	}

	@Override
    public String usage() {
	    return GetCommand.USAGE;
    }

}
