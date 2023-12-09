package com.iamxgw.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 消息头
 * @author: IamXGW
 * @create: 2023-12-09 15:28
 */
public class MsgHeader {
    /**
     * MD5 摘要
     */
    private String md5;

    /**
     * 消息 ID
     */
    private long msgID;
    /**
     * 消息类型
     */
    private byte msgType;
    /**
     * 消息优先级
     */
    private byte priority;
    /**
     * 消息头额外附件
     */
    private Map<String, Object> attachment = new HashMap<>();

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getMsgID() {
        return msgID;
    }

    public void setMsgID(long msgID) {
        this.msgID = msgID;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "MsgHeader{" +
                "md5='" + md5 + '\'' +
                ", msgID=" + msgID +
                ", msgType=" + msgType +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}