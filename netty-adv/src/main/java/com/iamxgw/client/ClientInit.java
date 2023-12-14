package com.iamxgw.client;

import com.iamxgw.kryocodec.KryoDecoder;
import com.iamxgw.kryocodec.KryoEncoder;
import com.iamxgw.server.HeartBeatRespHandler;
import com.iamxgw.server.LoginAuthRespHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @description: 客户端 handler 初始化
 * @author: IamXGW
 * @create: 2023-12-12 22:10
 */
public class ClientInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));

        /* 连接写空闲检测 */
        ch.pipeline().addLast(new CheckWriteIdleHandler());

        /* 粘包半包 */
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
        ch.pipeline().addLast(new LengthFieldPrepender(2));

        /* 序列化相关 */
        ch.pipeline().addLast(new KryoDecoder());
        ch.pipeline().addLast(new KryoEncoder());

        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
        ch.pipeline().addLast(new LoginAuthRespHandler());

        /* 连接读空闲检测 */
        ch.pipeline().addLast(new ReadTimeoutHandler(15));

        /* 向服务器发送心跳请求 */
        ch.pipeline().addLast(new HeartBeatReqHandler());

    }
}