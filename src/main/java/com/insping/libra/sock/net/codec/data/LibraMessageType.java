package com.insping.libra.sock.net.codec.data;

public enum LibraMessageType {

    MESSAGE_REQ((byte) 0),  // 下行数据
    MESSAGE_RESP((byte) 1), //上行数据
    CLIENT_AUTH((byte) 2),  //客户端验证
    CLIENT_SELECT_SERVER((byte) 3),   //登录服务器
    SERVER_REGISTER_REQ((byte) 4),  //服务器认证
    SERVER_REGISTER_RESP((byte) 5), //服务器认证回复
    SERVER_HEARTBEAT_REQ((byte) 6), //服务器心跳
    SERVER_HEARTBEAT_RESP((byte) 7);    //服务器心跳回复

    private byte value;

    private LibraMessageType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static LibraMessageType search(byte value) {
        for (LibraMessageType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
