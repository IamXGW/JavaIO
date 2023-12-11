package com.iamxgw.kryocodec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @description: Kryo 的序列化器，负责 Kryo 的序列化和反序列化
 * @author: IamXGW
 * @create: 2023-12-09 15:47
 */
public class KryoSerializer {
    private static Kryo kryo = KryoFactory.createKryo();

    /**
     * @Description: Kryo 序列化
     * @Param: [object, out]
     * @return: void
     * @Author: IamXGW
     * @Date: 2023/12/9
     */
    public static void serialize(Object object, ByteBuf out) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, object);
        output.flush();
        output.close();

        byte[] b = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.writeBytes(b);
    }

    /**
     * @Description: Kryo 反序列化
     * @Param: [out]
     * @return: java.lang.Object
     * @Author: IamXGW
     * @Date: 2023/12/9
     */
    public static Object deserialize(ByteBuf out) {
        if (out == null) {
            return null;
        }
        Input input = new Input(new ByteBufInputStream(out));
        return kryo.readClassAndObject(input);
    }

    public static byte[] obj2Bytes(Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, object);
        output.flush();
        output.close();

        byte[] b = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return b;
    }
}