package com.iamxgw;

import com.iamxgw.client.ClientInit;
import com.iamxgw.vo.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: Netty 客户端主入口
 * @author: IamXGW
 * @create: 2023-12-12 21:52
 */
public class NettyClient {
    private static final Logger LOG = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup group = new NioEventLoopGroup();

    // 测试 Netty
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.connect(NettyConstant.SERVER_PORT, NettyConstant.SERVER_IP);
//        nettyClient.send("V");
//        nettyClient.close();
    }

    private void connect(int port, String ip) {
        // 客户端启动必备
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class) // 指定使用 NIO 的通信方式
                .handler(new ClientInit());
    }
}