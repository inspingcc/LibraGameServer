package com.insping.libra.sock.net.handler;

import com.insping.Instances;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.sock.net.response.GeneralResponse;
import com.insping.libra.world.LibraConfig;
import com.insping.log.LibraLog;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author houshanping
 */
public class SockLogicHandler extends ChannelInboundHandlerAdapter implements Instances {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LibraMessage message = (LibraMessage) msg;
        if (message == null || message.getHead() == null || message.getHead().getSrcServerID() != LibraConfig.SERVER_ID) {
            ctx.fireChannelRead(msg);
            return;
        }
        // 本地逻辑处理
        ServerHandler handler = handlerMgr.searchHandler(message.getHead().getProtocolID());
        if (handler == null) {
            LibraLog.info("SockLogicHandler-channelRead:have no handler protocolID = " + message.getHead().getProtocolID());
            ctx.fireChannelRead(msg);
            return;
        }
        GeneralResponse resp = new GeneralResponse();
        handler.doLogic(message, resp);
        LibraMessage libraMessage = resp.build(message);
        ctx.channel().writeAndFlush(libraMessage);
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
