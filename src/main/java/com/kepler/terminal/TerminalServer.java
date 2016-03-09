package com.kepler.terminal;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kepler.config.Config;
import com.kepler.config.ConfigSync;
import com.kepler.config.PropertiesUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author longyaokun 2016-03-07
 *
 */
public class TerminalServer {

	private static final Log LOGGER = LogFactory.getLog(TerminalServer.class);

	private static final String LOOP = "127.0.0.1";

	/**
	 * Terminal 服务开关
	 */
	private static final boolean ENABLED = PropertiesUtils.get(TerminalServer.class.getName().toLowerCase() + ".enabled", true);
	/**
	 * 本地默认端口
	 */
	private static final int PORT = PropertiesUtils.get(TerminalServer.class.getName().toLowerCase() + ".port", 8888);
	
	/**
	 * 默认綁定的IP
	 */
	private static final String IP = PropertiesUtils.get(TerminalServer.class.getName().toLowerCase() + ".ip", TerminalServer.LOOP);

	/**
	 * 退出命令
	 */
	private static final String CMD_QUIT = "quit";

	/**
	 * 命令最大长度
	 */
	private static final int CMD_MAX_LENGTH = PropertiesUtils.get(TerminalServer.class.getName().toLowerCase() + ".cmd_max_length", 1024);

	/**
	 * 命令格式
	 */
	private static final Pattern CMD_PATTERN = Pattern.compile("(set)[ ]+([^=,^ ]+)=([^=,^ ]+)");

	private static final String USAGE = "Please input the command: set key=value\ntype 'quit' to exit\n";

	private final ServerBootstrap bootstrap = new ServerBootstrap();

	private final ConfigSync configSync;

	private final Config config;

	public TerminalServer(ConfigSync configSync, Config config) {
		this.configSync = configSync;
		this.config = config;
	}

	public void init() throws Exception {
		if (!TerminalServer.ENABLED) {
			TerminalServer.LOGGER.warn("TerminalServer is not enabled!");
			return;
		}
		this.bootstrap
			.group(new NioEventLoopGroup(1), new NioEventLoopGroup(1))
			.channel(NioServerSocketChannel.class)
	        .childHandler(new ChannelInitializer<SocketChannel>() {
		        @Override
		        public void initChannel(SocketChannel ch) throws Exception {
			        ch.pipeline()
			        	//	Inbound handler
			        	.addLast(new LineBasedFrameDecoder(TerminalServer.CMD_MAX_LENGTH, true, true))
			        	.addLast(new StringDecoder())
			        	.addLast(new ConfigHandler())
			        	//	Oubtount Handler
			        	.addLast(new StringEncoder());
		        }
	        }).option(ChannelOption.SO_REUSEADDR, true).bind(TerminalServer.IP, TerminalServer.PORT).sync();

	}

	public void destroy() throws Exception {
		if (!TerminalServer.ENABLED) {
			return;
		}
		this.bootstrap.group().shutdownGracefully().sync();
		TerminalServer.LOGGER.warn("Terminal Server shutdown ... ");
	}

	private class ConfigHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
			String cmdLine = String.class.cast(msg);
			TerminalServer.LOGGER.info("Receive the command:" + cmdLine);
			if (TerminalServer.CMD_QUIT.equalsIgnoreCase(cmdLine)) {
				ctx.pipeline().writeAndFlush("Exit...");
				ctx.close();
				return;
			}
			Command command = new Command(cmdLine);
			if (!command.valid()) {
				ctx.pipeline().writeAndFlush(TerminalServer.USAGE);
			} else {
				String response = command.execute() ? "command executed!\n" : "command failed!\n";
				ctx.pipeline().writeAndFlush(response);
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			TerminalServer.LOGGER.error(cause.getMessage(), cause);
			ctx.close();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			String local = ctx.channel().localAddress().toString();
			String target = ctx.channel().remoteAddress().toString();
			TerminalServer.LOGGER.info("ConfigHandler:Connect active (" + local + " to " + target + ") ...");
			ctx.pipeline().writeAndFlush(TerminalServer.USAGE);
			ctx.fireChannelActive();
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			String local = ctx.channel().localAddress().toString();
			String target = ctx.channel().remoteAddress().toString();
			TerminalServer.LOGGER.info("ConfigHandler:Connect inactive (" + local + " to " + target + ") ...");
			ctx.fireChannelInactive();
		}
	}

	private class Command {

		@SuppressWarnings("unused")
		private String cmd;

		private String key;

		private String value;

		private boolean valid;

		private final Matcher matcher;

		public Command(String cmdLine) {
			this.matcher = TerminalServer.CMD_PATTERN.matcher(cmdLine);
			if (this.matcher.matches()) {
				this.valid = true;
				this.cmd = this.matcher.group(1);
				this.key = this.matcher.group(2);
				this.value = this.matcher.group(3);
			}
		}

		public boolean execute() {
			Map<String, String> configs = PropertiesUtils.properties();
			configs.put(this.key, this.value);
			TerminalServer.this.config.config(configs);
			try {
				TerminalServer.this.configSync.sync();
            } catch (Exception e) {
            	TerminalServer.LOGGER.error("Command execute failed:" , e);
	            return false;
            }
			return true;
		}

		public boolean valid() {
			return this.valid;
		}

	}
}
