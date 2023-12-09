package com.iamxgw.kryocodec;

import com.iamxgw.vo.EncryptUtils;
import com.iamxgw.vo.MakeMsgID;
import com.iamxgw.vo.MsgHeader;
import com.iamxgw.vo.MyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: Kryo 序列化的测试类
 * @author: IamXGW
 * @create: 2023-12-09 16:25
 */
public class TestKryoCodec {
    public static void main(String[] args) {
        TestKryoCodec testKryoCodec = new TestKryoCodec();
        for (int i = 0; i < 5; i++) {
            ByteBuf sendBuf = Unpooled.buffer();
            MyMessage myMsg = testKryoCodec.getMyMsg(i);
            System.out.println("Encode: " + myMsg);
            KryoSerializer.serialize(myMsg, sendBuf);
            MyMessage decodeMsg = (MyMessage) KryoSerializer.deserialize(sendBuf);
            System.out.println("Decode: " + decodeMsg);
            System.out.println("---------------------");
        }
    }

    public MyMessage getMyMsg(int j) {
        String content = "abcdefg--------AAAAAA:" + j;
        MyMessage myMessage = new MyMessage();
        MsgHeader header = new MsgHeader();
        header.setMd5(EncryptUtils.encrypt(content));
        header.setMsgID(MakeMsgID.getId());
        header.setMsgType((byte) 1);
        header.setPriority((byte) 7);
        Map<String, Object> attachment = new HashMap<>();
        for (int i = 0; i < 10; ++i) {
            attachment.put("city ---> " + i, "IamXGW " + i);
        }
        header.setAttachment(attachment);
        myMessage.setMsgHeader(header);
        myMessage.setBody(content);
        return myMessage;
    }
}