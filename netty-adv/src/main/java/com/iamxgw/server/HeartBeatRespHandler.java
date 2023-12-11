package com.iamxgw.server;

import com.esotericsoftware.minlog.Log;
import com.iamxgw.vo.MessageType;
import com.iamxgw.vo.MsgHeader;
import com.iamxgw.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 心跳检测
 * @author: IamXGW
 * @create: 2023-12-11 21:02
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(HeartBeatRespHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyMessage message = (MyMessage) msg;
        if (message.getMsgHeader() != null && message.getMsgHeader().getMsgType() == MessageType.HEARTBEAT_REQ.value()) {
            /* 应答心跳报文 */
            MyMessage heartBeatResp = buildHeartBeat();
            LOG.debug("心跳应答：" + heartBeatResp);
            ctx.writeAndFlush(heartBeatResp);
            ReferenceCountUtil.release(msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private MyMessage buildHeartBeat() {
        MyMessage myMessage = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgType(MessageType.HEARTBEAT_RESP.value());
        myMessage.setMsgHeader(msgHeader);
        return myMessage;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOG.warn("客户端已关闭连接");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof RuntimeException) {
            LOG.warn("客户端长时间未通信，可能已经宕机，关闭链路");
            SecurityCenter.removeLoginUser(ctx.channel().remoteAddress().toString());
            ctx.close();
        }
        super.exceptionCaught(ctx, cause);
    }
}