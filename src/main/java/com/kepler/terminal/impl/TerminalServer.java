package com.kepler.terminal.impl;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kepler.config.PropertiesUtils;
import com.kepler.connection.impl.ExceptionListener;
import com.kepler.org.apache.commons.lang.StringUtils;
import com.kepler.terminal.CommandWriter;
import com.kepler.terminal.Commands;

/**
 * @author longyaokun 2016-03-07
 *
 */
public class TerminalServer {

	/**
	 * 报文最大长度
	 */
	private static final int MAX_LENGTH = PropertiesUtils.get(TerminalServer.class.getName().toLowerCase() + ".max_length", 1024);

	/**
	 * 綁定IP, 默认本机回路
	 */
	private static final String IP = PropertiesUtils.get(TerminalServer.class.getName().toLowerCase() + ".ip", "127.0.0.1");

	/**
	 * 本地默认端口
	 */
	private static final int PORT = PropertiesUtils.get(TerminalServer.class.getName().toLowerCase() + ".port", 12345);

	private static final Log LOGGER = LogFactory.getLog(TerminalServer.class);

	/**
	 * 仅1个线程的EventLoop
	 */
	private final NioEventLoopGroup eventloop = new NioEventLoopGroup(1);

	private final ServerBootstrap bootstrap = new ServerBootstrap();

	private final StringDecoder decoder = new StringDecoder();

	private final StringEncoder encoder = new StringEncoder();

	private final Commands commands;

	public TerminalServer(Commands commands) {
		this.commands = commands;
	}

	public void init() throws Exception {
		// Parent-Child使用相同EventLoop
		this.bootstrap.group(this.eventloop, this.eventloop).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new LineBasedFrameDecoder(TerminalServer.MAX_LENGTH, true, true));
				ch.pipeline().addLast(TerminalServer.this.decoder);
				ch.pipeline().addLast(TerminalServer.this.encoder);
				ch.pipeline().addLast(new TerminalHandler());
			}
			// 绑定IP:PORT并同步等待连接
		}).option(ChannelOption.SO_REUSEADDR, true).bind(TerminalServer.IP, TerminalServer.PORT).sync();
		TerminalServer.LOGGER.info("Terminal started ...");
	}

	public void destroy() throws Exception {
		// 同步关闭关闭
		this.bootstrap.group().shutdownGracefully().sync();
		TerminalServer.LOGGER.warn("Terminal shutdown ... ");
	}

	private class TerminalHandler extends ChannelInboundHandlerAdapter implements CommandWriter {

		private ChannelHandlerContext context;

		@Override
		public void write(String data) {
			this.context.writeAndFlush(data);
		}

		@Override
		public void close() {
			this.context.close().addListener(ExceptionListener.TRACE);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			// 是否允许建立连接, 动态参数
			if (!PropertiesUtils.get(TerminalServer.class.getName().toLowerCase() + ".enabled", true)) {
				ctx.close().addListener(ExceptionListener.TRACE);
			}
			this.context = ctx;
			TerminalServer.LOGGER.info("Connect active (" + ctx.channel().localAddress() + " to " + ctx.channel().remoteAddress() + ") ...");
			ctx.fireChannelActive();
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			TerminalServer.LOGGER.info("Connect inactive (" + ctx.channel().localAddress() + " to " + ctx.channel().remoteAddress() + ") ...");
			ctx.fireChannelInactive();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			TerminalServer.LOGGER.error(cause.getMessage(), cause);
			ctx.close().addListener(ExceptionListener.TRACE);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			TerminalServer.LOGGER.info("Receive the command: " + msg + " ... ");
			// 切分请求
			String message = String.class.cast(msg);
			if (StringUtils.isEmpty(message.trim())) {
				return;
			}
			String[] args = message.split(" ");
			
			// 获取解析器并执行
			TerminalServer.this.commands.get(args[0]).command(this, args);
		}
	}
}
