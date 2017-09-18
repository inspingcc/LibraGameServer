package com.insping.libra.sock.net.module;

public interface ModuleType {
    // 内部或通用模块号
    int GENERAL_RESPONSE = 0x77777777;// 普通消息模块
    int SERVER_REGISTER = 0x70000001;// 服务器注册模块消息
    int SERVER_HEARTBEAT = 0x70000002;// 服务器心跳模块消息

}
