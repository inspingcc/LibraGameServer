package com.insping.libra.sock.net.handler.impl;

import com.insping.libra.proto.ReqTest;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.sock.net.handler.ServerHandler;
import com.insping.libra.sock.net.response.GeneralResponse;

public class TestHandler extends ServerHandler {

    @Override
    public void doLogic(LibraMessage message, GeneralResponse resp) throws Exception {
        resp.setDesc(((ReqTest.TestData) message.getBody()).getStr());
    }
}
