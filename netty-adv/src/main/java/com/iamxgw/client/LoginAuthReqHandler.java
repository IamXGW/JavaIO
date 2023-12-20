package com.iamxgw.client;

import com.iamxgw.vo.MessageType;
import com.iamxgw.vo.MsgHeader;
import com.iamxgw.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 发起登陆请求
 * @author: IamXGW
 * @create: 2023-12-15 14:52
 */
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(LoginAuthReqHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /* 发出认证请求 */
        MyMessage loginMsg = buildLoginReq();
        LOG.info("请求服务认证：" + loginMsg);
        ctx.writeAndFlush(loginMsg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /* 是不是握手成功的应答 */
        MyMessage message = (MyMessage) msg;
        if (message.getMsgHeader() != null && message.getMsgHeader().getMsgType() == MessageType.LOGIN_RESP.value()) {
            LOG.info("收到认证应答报文，服务器认证是否通过？");
            byte loginResult = (byte) message.getBody();
            if (loginResult != (byte) 0) {
                /* 握手失败，关闭连接 */
                LOG.warn("未通过认证，关闭连接。" + message);
                ctx.close();
            } else {
                LOG.info("认证通过，移除本处理器，进入业务通信：" + message);
                ctx.pipeline().remove(this);
                ReferenceCountUtil.release(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private static MyMessage buildLoginReq() {
        MyMessage message = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgType(MessageType.LOGIN_REQ.value());
        message.setMsgHeader(msgHeader);
        return message;
    }
}