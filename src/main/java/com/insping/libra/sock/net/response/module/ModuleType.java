package com.insping.libra.sock.net.response.module;

public interface ModuleType {
    int GENERAL_RESPONSE = 0x77777777;// 普通模块消息

    int SERVER_REGISTER = 0x70000001;//服务器注册模块消息
    int SERVER_HEARTBEAT = 0x70000002;//服务器心跳模块消息
}
