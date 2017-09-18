package com.insping.libra.sock.net.handler.internal;

import java.util.concurrent.TimeUnit;

import com.insping.Instances;
import com.insping.common.utils.TimeUtils;
import com.insping.libra.proto.ReqServerHeartbeat.HeartbeatData;
import com.insping.libra.proto.ResGeneral;
import com.insping.libra.sock.net.module.ModuleType;
import com.insping.libra.sock.net.codec.data.LibraHead;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.world.LibraConfig;
import com.insping.log.LibraLog;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * @author houshanping
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter implements Instances {

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LibraMessage message = (LibraMessage) msg;
        if (message.getHead() == null || message.getHead().getSrcServerID() != LibraConfig.SERVER_ID || !(message.getBody() instanceof ResGeneral.GeneralData)) {
            ctx.fireChannelRead(msg);
            return;
        }
        // 握手成功，主动发送心跳消息
        if (((ResGeneral.GeneralData) message.getBody()).getProtocolID() == ModuleType.SERVER_REGISTER) {
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, LibraConfig.SERVER_HEARTBEAT_PERIOD, TimeUnit.MILLISECONDS);
        } else if (((ResGeneral.GeneralData) message.getBody()).getProtocolID() == ModuleType.SERVER_HEARTBEAT) {
            LibraLog.info("HeartbeatHandler-channelRead : receive GatewayServer heartbeat message ! ");
        }
    }

    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            LibraMessage heatBeat = buildHeatBeat();
            LibraLog.info("HeartBeatTask-run : send heartbeat to GatewayServer!");
            ctx.channel().writeAndFlush(heatBeat);
        }

        private LibraMessage buildHeatBeat() {
            HeartbeatData.Builder builder = HeartbeatData.newBuilder();
            builder.setServerID(LibraConfig.SERVER_ID);
            builder.setServerName(LibraConfig.SERVER_NAME);
            builder.setServerKey("serverkey");
            builder.setServerType(0);
            builder.setServerStatus(0);
            builder.setTime(TimeUtils.nowLong());
            builder.setServerDesc("desc");
            // 数据Head
            LibraHead head = new LibraHead();
            head.setDestServerID(LibraConfig.SERVER_ID);
            head.setSrcServerID(LibraConfig.GATEWAY_SERVER_ID);
            //head.setType(LibraMessageType.SERVER_HEARTBEAT_REQ.getValue());
            head.setProtocolID(ModuleType.SERVER_HEARTBEAT);
            //head.setMessageID(0);
            return new LibraMessage(head, builder.build());
        }
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
