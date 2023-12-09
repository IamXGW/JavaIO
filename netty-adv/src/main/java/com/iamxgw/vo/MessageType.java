package com.iamxgw.vo;

public enum MessageType {
    SERVICE_REQ((byte) 0), /* 服务请求消息 */
    SERVICE_RESP((byte) 1), /* 服务相应消息*/
    ONE_WAY((byte) 2), /* 无需应答的业务请求消息 */
    LOGIN_REQ((byte) 3), /* 登陆请求消息 */
    LOGIN_RESP((byte) 4), /* 登陆相应消息 */
    HEARTBEAT_REQ((byte) 5), /* 心跳请求消息 */
    HEARTBEAT_RESP((byte) 6); /* 心跳相应消息 */

    private byte value;

    private MessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return this.value;
    }
}
