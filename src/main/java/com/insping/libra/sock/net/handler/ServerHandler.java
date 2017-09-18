package com.insping.libra.sock.net.handler;

import java.util.Map;

import com.insping.Instances;
import com.insping.libra.proto.ResGeneral;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.sock.net.response.GeneralResponse;
import com.insping.libra.sock.net.response.ReturnObject;

/**
 * @author houshanping
 * @since 2017-07-12
 */
public abstract class ServerHandler implements Instances {
    public abstract void doLogic(LibraMessage message, GeneralResponse resp) throws Exception;
}
