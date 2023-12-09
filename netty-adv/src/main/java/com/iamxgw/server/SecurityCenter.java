package com.iamxgw.server;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @description: 安全中心
 * @author: IamXGW
 * @create: 2023-12-09 22:19
 */
public class SecurityCenter {
    /* 用以检查用户是否重复登陆的缓存 */
    private static Map<String, Boolean> nodeChk = new ConcurrentHashMap<>();

    /* user login white list */
    private static Set<String> whiteList = new CopyOnWriteArraySet<>();

    static {
        whiteList.add("127.0.0.1");
    }

    public static boolean isWhiteIP(String ip) {
        return whiteList.contains(ip);
    }

    public static boolean isDupLog(String userInfo) {
        return nodeChk.containsKey(userInfo);
    }

    public static void addLoginUser(String userInfo) {
        nodeChk.put(userInfo, true);
    }

    public static void removeLoginUser(String userInfo) {
        nodeChk.remove(userInfo, true);
    }
}