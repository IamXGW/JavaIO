package com.iamxgw.client;

import com.iamxgw.vo.MessageType;
import com.iamxgw.vo.MsgHeader;
import com.iamxgw.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 客户端在长久未向服务器发送请求时，发出心跳请求报文
 * @author: IamXGW
 * @create: 2023-12-13 22:24
 */
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(HeartBeatReqHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT) {
            MyMessage heartBeat = buildHeartBeat();
            LOG.debug("写空闲，发出报文维持连接：" + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }
    }

    private MyMessage buildHeartBeat() {
        MyMessage message = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgType(MessageType.HEARTBEAT_REQ.value());
        message.setMsgHeader(msgHeader);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            LOG.warn("服务器长时间未应答，关闭连接");
        }
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyMessage message = (MyMessage) msg;
        /* 是不是心跳应答 */
        if (message.getMsgHeader() != null
                && message.getMsgHeader().getMsgType() == MessageType.HEARTBEAT_RESP.value()) {
            LOG.debug("收到服务器心跳应答报文，服务器正常");
            ReferenceCountUtil.release(msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}