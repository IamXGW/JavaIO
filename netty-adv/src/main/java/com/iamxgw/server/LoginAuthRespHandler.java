package com.iamxgw.server;

import com.iamxgw.vo.MessageType;
import com.iamxgw.vo.MsgHeader;
import com.iamxgw.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @description: 登陆检查，该处理器在用户验证通过后，可以移除
 * @author: IamXGW
 * @create: 2023-12-09 22:09
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(LoginAuthRespHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyMessage message = (MyMessage) msg;
        /*  是不是握手认证请求 */
        if (message.getMsgHeader() != null
                && message.getMsgHeader().getMsgType() == MessageType.LOGIN_REQ.value()) {
            LOG.info("Receive client login request: " + message);
            String nodeIndex = ctx.channel().remoteAddress().toString();
            MyMessage loginResp = null;
            boolean checkAuthPass = false;
            /* 拒绝重复登陆，使用客户端的地址代替实际的用户信息 */
            if (SecurityCenter.isDupLog(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
                LOG.warn("Reject dul login, response msg: " + loginResp);
                ctx.writeAndFlush(loginResp);
                ctx.close();
            } else {
                /* If user in the whiteList, then allow login, and put the user into cache */
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                if (SecurityCenter.isWhiteIP(ip)) {
                    SecurityCenter.addLoginUser(nodeIndex);
                    loginResp = buildResponse((byte) 0);
                    LOG.info("Auth success, response msg: " + loginResp);
                    ctx.writeAndFlush(loginResp);
                } else {
                    loginResp = buildResponse((byte) -1);
                    LOG.warn("Auth fail, response msg: " + loginResp);
                    ctx.writeAndFlush(loginResp);
                    ctx.close();
                }
            }
            ReferenceCountUtil.release(msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private MyMessage buildResponse(byte result) {
        MyMessage message = new MyMessage();
        MsgHeader header = new MsgHeader();
        header.setMsgType(MessageType.LOGIN_RESP.value());
        message.setMsgHeader(header);
        message.setBody(result);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SecurityCenter.removeLoginUser(ctx.channel().remoteAddress().toString());
        ctx.close();
    }
}