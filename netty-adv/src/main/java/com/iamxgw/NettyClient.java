package com.iamxgw;

import com.iamxgw.client.ClientInit;
import com.iamxgw.vo.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description: Netty 客户端主入口
 * @author: IamXGW
 * @create: 2023-12-12 21:52
 */
public class NettyClient implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(NettyClient.class);

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private EventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

    /* 是否用户主动关闭连接的标志 */
    private volatile boolean userClose = false;

    /* 连接是否成功的标志 */
    private volatile boolean connected = false;

    // 测试 Netty
    public static void main(String[] args) throws InterruptedException {
        NettyClient nettyClient = new NettyClient();
        nettyClient.connect(NettyConstant.SERVER_PORT, NettyConstant.SERVER_IP);
        nettyClient.send("V");
        nettyClient.close();
    }

    /* 以下方法供业务方使用 */
    private void send(Object message) {
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException("和服务器还未建立连接，请稍后再试");
        }
        MyMessage msg = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgID(MakeMsgID.getId());
        msgHeader.setMsgType(MessageType.SERVICE_REQ.value());
        msgHeader.setMd5(EncryptUtils.encryptObj(message));
        msg.setMsgHeader(msgHeader);
        msg.setBody(message);
        LOG.info("给服务器发送信息");
        channel.writeAndFlush(msg);
    }

    private void connect(int port, String host) throws InterruptedException {
        try {
            // 客户端启动必备
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class) // 指定使用 NIO 的通信方式
                    .handler(new ClientInit());
            ChannelFuture future = future = b.connect(new InetSocketAddress(host, port)).sync();
            LOG.info("已连接服务器");
            channel = future.channel();
            synchronized (this) {
                this.connected = true;
                this.notifyAll();
            }
            channel.closeFuture().sync();
        } finally {
            if (!userClose) {
                /* 非正常关闭，有可能发生了网络问题，尝试重连 */
                LOG.warn("需要重新连接");
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            /* 给足系统足够时间去释放相关资源 */
                            TimeUnit.SECONDS.sleep(1);
                            connect(NettyConstant.SERVER_PORT, NettyConstant.SERVER_IP);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } else {
                /* 正常关闭 */
                channel = null;
                group.shutdownGracefully().sync();
                synchronized (this) {
                    this.connected = false;
                    this.notifyAll();
                }
                LOG.info("正常关闭");
            }
        }

    }

    @Override
    public void run() {
        try {
            connect(NettyConstant.SERVER_PORT, NettyConstant.SERVER_IP);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        userClose = false;
        channel.close();
    }
}