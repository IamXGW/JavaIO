package com.iamxgw.server;

import com.iamxgw.server.asyncpro.AsyncBusiProcess;
import com.iamxgw.server.asyncpro.ITaskProcessor;
import com.iamxgw.vo.EncryptUtils;
import com.iamxgw.vo.MessageType;
import com.iamxgw.vo.MsgHeader;
import com.iamxgw.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 业务处理
 * @author: IamXGW
 * @create: 2023-12-11 21:40
 */
public class ServerBusiHandler extends SimpleChannelInboundHandler<MyMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(ServerBusiHandler.class);

    private ITaskProcessor taskProcessor;

    public ServerBusiHandler(ITaskProcessor taskProcessor) {
        super();
        this.taskProcessor = taskProcessor;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) throws Exception {
        /* 检查 MD5 */
        String headMd5 = msg.getMsgHeader().getMd5();
        String calMd5 = EncryptUtils.encryptObj(msg.getBody());
        if (!headMd5.equals(calMd5)) {
            LOG.error("报文 MD5 检查不通过：" + headMd5 + " vs " + calMd5);
            ctx.writeAndFlush(buildBusResp("报文 MD5 检查不通过，关闭连接"));
            ctx.close();
        }

        LOG.info(msg.toString());
        if (msg.getMsgHeader().getMsgType() == MessageType.ONE_WAY.value()) {
            LOG.debug("ONE_WAY 消息类型，异步处理");
            AsyncBusiProcess.submitTask(taskProcessor.execAsyncTask(msg));
        } else {
            LOG.debug("TWO_WAY 消息类型，应答");
            ctx.writeAndFlush(buildBusResp("OK"));
        }
    }

    private MyMessage buildBusResp(String result) {
        MyMessage message = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgType(MessageType.SERVICE_RESP.value());
        message.setMsgHeader(msgHeader);
        message.setBody(result);
        return message;
    }
}