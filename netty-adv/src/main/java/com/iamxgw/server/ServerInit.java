package com.iamxgw.server;

import com.iamxgw.kryocodec.KryoDecoder;
import com.iamxgw.kryocodec.KryoEncoder;
import com.iamxgw.server.asyncpro.DefaultTaskProcessor;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @description: ServerInit
 * @author: IamXGW
 * @create: 2023-12-09 15:18
 */
public class ServerInit extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        /**
         * 粘包半包问题
         */
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
        ch.pipeline().addLast(new LengthFieldPrepender(2));

        /**
         * 序列化相关
         */
        ch.pipeline().addLast(new KryoDecoder());
        ch.pipeline().addLast(new KryoEncoder());

        /**
         * 处理心跳超时
         */
        ch.pipeline().addLast(new ReadTimeoutHandler(15));

        ch.pipeline().addLast(new LoginAuthRespHandler());
        ch.pipeline().addLast(new HeartBeatRespHandler());
        ch.pipeline().addLast(new ServerBusiHandler(new DefaultTaskProcessor()));
    }
}