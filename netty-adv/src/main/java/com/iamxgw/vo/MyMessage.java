package com.iamxgw.vo;

/**
 * @description: MySessage
 * @author: IamXGW
 * @create: 2023-12-09 15:27
 */
public class MyMessage {
    /**
     * 消息头
     */
    private MsgHeader msgHeader;
    /**
     * 消息体
     */
    private Object body;

    public final MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public final void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public final Object getBody() {
        return body;
    }

    public final void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "MyMessage{" +
                "msgHeader=" + msgHeader +
                ", body=" + body +
                '}';
    }
}