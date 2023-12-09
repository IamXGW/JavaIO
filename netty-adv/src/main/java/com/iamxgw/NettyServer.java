package com.iamxgw;

import com.iamxgw.server.ServerInit;
import com.iamxgw.vo.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: netty server main class
 * @author: IamXGW
 * @create: 2023-12-09 14:49
 */
public class NettyServer {
    private static final Logger LOG = LoggerFactory.getLogger(NettyServer.class);

    /**
     * @Description:
     * @Param:
     * @return:
     * @Author: IamXGW
     * @Date: 2023/12/9
     */
    public void bind() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors(), new DefaultThreadFactory("nt_worker"));
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ServerInit());

        b.bind(NettyConstant.SERVER_PORT).sync();
        LOG.info("Netty server start: " + NettyConstant.SERVER_IP + ":" + NettyConstant.SERVER_PORT);
    }

    /**
     * @Description:
     * @Param:
     * @return:
     * @Author: IamXGW
     * @Date: 2023/12/9
     */
    public static void main(String[] args) throws InterruptedException {
        new NettyServer().bind();
    }
}