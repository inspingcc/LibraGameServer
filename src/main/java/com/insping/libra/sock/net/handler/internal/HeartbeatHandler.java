package com.insping.libra.sock.net.handler.internal;

import java.util.concurrent.TimeUnit;

import com.insping.Instances;
import com.insping.common.utils.TimeUtils;
import com.insping.libra.proto.ReqServerHeartbeat.HeartbeatData;
import com.insping.libra.sock.net.codec.data.LibraHead;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.sock.net.codec.data.LibraMessageType;
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
        // 握手成功，主动发送心跳消息
        if (message.getHead() != null && message.getHead().getType() == LibraMessageType.SERVER_REGISTER_RESP.getValue()) {
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 30 * 1000, TimeUnit.MILLISECONDS);
        } else if (message.getHead() != null && message.getHead().getType() == LibraMessageType.SERVER_HEARTBEAT_RESP.getValue()) {
            LibraLog.info("HeartbeatHandler-channelRead : receive GatewayServer heartbeat message ! ");
        } else {
            ctx.fireChannelRead(msg);
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
            ctx.writeAndFlush(heatBeat);
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
            head.setSrcServerID(0);
            head.setType(LibraMessageType.SERVER_HEARTBEAT_REQ.getValue());
            head.setProtocolID(0);
            head.setMessageID(0);
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
