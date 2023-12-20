package com.iamxgw.client;

import com.iamxgw.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 接受业务应答并处理
 * @author: IamXGW
 * @create: 2023-12-15 14:49
 */
public class ClientBusiHandler extends SimpleChannelInboundHandler<MyMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(ClientBusiHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) throws Exception {
        LOG.info("业务应答消息：" + msg.toString());
    }
}