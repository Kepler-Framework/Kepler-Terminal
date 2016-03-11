package com.kepler.terminal.command;

import com.kepler.terminal.Command;
import com.kepler.terminal.CommandWriter;

/**
 * @author longyaokun
 *
 */
public abstract class AbstractLeafCommand implements Command{

	@Override
    public void command(CommandWriter writer, String[] args) throws Exception {
	    if (!this.valid(args)) {
	    	writer.write(this.usage());
        }else{
        	this.execute(writer, args);;
        }
	    
    }

	
	/**
	 * 验证参数合法性
	 * @param args
	 * @return
	 */
	protected abstract boolean valid(String[] args);
	
	/**
	 * 执行命令
	 * @throws Exception
	 */
	protected abstract void execute(CommandWriter writer, String[] args) throws Exception;
	
	@Override
    public abstract String prefix();

	@Override
    public abstract String usage();

}
