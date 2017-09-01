package com.insping.libra.sock.net.handler;

import java.util.Map;

import com.google.protobuf.MessageLite;
import com.insping.Instances;
import com.insping.common.i18.I18nGreeting;
import com.insping.common.utils.JsonUtil;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.sock.net.codec.data.LibraMessageType;
import com.insping.libra.sock.net.response.ReturnObject;
import com.insping.log.LibraLog;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author houshanping
 */
public class SockLogicHandler extends ChannelInboundHandlerAdapter implements Instances {

    private ChannelHandlerContext ctx;

    private LibraMessage request;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.ctx = ctx;
        LibraMessage message = (LibraMessage) msg;
        if (message.getBody() == null || message.getHead() == null) {
            return;
        }
        if (message.getHead().getType() != LibraMessageType.MESSAGE_REQ.getValue()) {
            ctx.fireChannelRead(msg);
            return;
        }
        //TODO 逻辑处理分发

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
