package com.iamxgw.vo;

import com.iamxgw.kryocodec.KryoSerializer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description: 加解密类
 * @author: IamXGW
 * @create: 2023-12-09 16:28
 */
public class EncryptUtils {
    private static String EntryptStr(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        if (encName == null || encName == "") {
            encName = "MD5";
        }
        try {
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid Entrypt algorithm.");
            throw new RuntimeException(e);
        }
        return strDes;
    }

    private static String EncryptByMD5(String str) {
        return EntryptStr(str, "MD5");
    }

    /**
     * @Description: 字节转 16 进制，结果以字符串形式表示
     * @Param: [digest]
     * @return: java.lang.String
     * @Author: IamXGW
     * @Date: 2023/12/9
     */
    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; ++i) {
            tmp = Integer.toHexString(bts[i] & 0XFF);
            if (tmp.length() == 1) {
                des += '0';
            }
            des += tmp;
        }
        return des;
    }

    public static String encrypt(String str) {
        String encryptStr = EncryptByMD5(str);
        if (encryptStr != null) {
            encryptStr = encryptStr + encryptStr.charAt(0) + encryptStr.charAt(2) + encryptStr.charAt(4);
            encryptStr = EncryptByMD5(encryptStr);
        }
        return encryptStr;
    }

    public static String encryptObj(Object o) {
        return encrypt(bytes2Hex(KryoSerializer.obj2Bytes(o)));
    }
}