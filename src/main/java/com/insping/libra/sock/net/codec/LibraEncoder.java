package com.insping.libra.sock.net.codec;

import com.google.protobuf.MessageLite;
import com.insping.Instances;
import com.insping.libra.sock.net.codec.data.LibraHead;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.sock.net.codec.data.UserInfo;
import com.insping.log.LibraLog;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class LibraEncoder extends MessageToByteEncoder<LibraMessage> implements Instances {

    @Override
    protected void encode(ChannelHandlerContext ctx, LibraMessage msg, ByteBuf out) throws Exception {
        LibraHead libraHead = msg.getHead();
        if (libraHead == null) {
            LibraLog.info("LibraEncoder-encode : is error! by head is null!");
            return;
        }
        ByteBuf headBuff = UnpooledByteBufAllocator.DEFAULT.buffer();
        headBuff.writeInt(libraHead.getDestServerID());// 来源instance
        headBuff.writeInt(libraHead.getSrcServerID());// 目的instance
        headBuff.writeByte(libraHead.getType());// 标志
        headBuff.writeInt(libraHead.getProtocolID());// 协议ID
        headBuff.writeInt(libraHead.getMessageID());// 消息ID
        headBuff.writeLong(libraHead.getRequestID());//RequestID
        UserInfo userInfo = libraHead.getUserInfo();
        if (userInfo == null)
            userInfo = new UserInfo();
        headBuff.writeLong(userInfo.getUid());// uid
        headBuff.writeInt(userInfo.getCid());// cid
        headBuff.writeInt(userInfo.getEid());// eid

        byte[] head = new byte[headBuff.readableBytes()];
        headBuff.getBytes(0, head);
        //byte[] head = headBuff.readBytes(headBuff.readableBytes()).array();
        byte[] body = new byte[0];
        if (msg.getBody() != null && msg.getBody() instanceof MessageLite) {
            body = ((MessageLite) msg.getBody()).toByteArray();
        }
        out.writeShort((short) (head.length + body.length + 2)); // 消息的总长度
        out.writeBytes(head); // 协议头
        out.writeShort((short) body.length); // 消息体长度
        out.writeBytes(body); // 消息体
        return;
    }

}
