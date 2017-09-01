package com.insping.libra.sock.net.codec;

import java.util.Arrays;
import java.util.List;

import com.google.protobuf.MessageLite;
import com.insping.Instances;
import com.insping.libra.proto.ResGeneral.GeneralData;
import com.insping.libra.sock.net.codec.data.LibraHead;
import com.insping.libra.sock.net.codec.data.LibraMessage;
import com.insping.libra.sock.net.codec.data.LibraMessageType;
import com.insping.libra.sock.net.codec.data.UserInfo;
import com.insping.log.LibraLog;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class LibraDecoder extends ByteToMessageDecoder implements Instances {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            // 空的buf
            if (in instanceof EmptyByteBuf || in.readableBytes() < 0) {
                return;
            }
            short length = in.readShort();
            if (length != in.readableBytes()) {
                return;
            }
            // 反序列化
            LibraMessage message = new LibraMessage();
            // 解析Head
            LibraHead head = message.getHead();
            head.setDestServerID(in.readInt());
            head.setSrcServerID(in.readInt());
            head.setType(in.readByte());
            head.setProtocolID(in.readInt());
            head.setMessageID(in.readInt());
            head.setRequestID(in.readLong());
            UserInfo userInfo = head.getUserInfo();
            userInfo.setUid(in.readLong());
            userInfo.setCid(in.readInt());
            userInfo.setEid(in.readInt());
            message.setHead(head);
            short bodyLength = in.readShort();// 消息体的长度
            if (bodyLength != in.readableBytes()) {
                LibraLog.error("LibraMessage.decode is error! params:bodyLength:" + bodyLength + "\treadableLength:"
                        + in.readableBytes());
                return;
            }
            ByteBuf bodyByteBuf = in.readBytes(in.readableBytes());
            byte[] array;
            // 反序列化数据的起始点
            int offset;
            // 可读的数据字节长度
            int readableLen = bodyByteBuf.readableBytes();
            // 分为包含数组数据和不包含数组数据两种形式
            if (bodyByteBuf.hasArray()) {
                array = bodyByteBuf.array();
                offset = bodyByteBuf.arrayOffset() + bodyByteBuf.readerIndex();
            } else {
                array = new byte[readableLen];
                bodyByteBuf.getBytes(bodyByteBuf.readerIndex(), array, 0, readableLen);
                offset = 0;
            }
            byte[] result = Arrays.copyOfRange(array, offset, readableLen);
            LibraMessageType messageType = LibraMessageType.search(head.getType());
            if (messageType == null){
                LibraLog.error("LibraDecoder-decode : type is null , type value = " + head.getType());
                return;
            }
            switch (messageType) {
                case SERVER_REGISTER_RESP:
                case SERVER_HEARTBEAT_RESP: {
                    message.setBody(GeneralData.parseFrom(result));
                    break;
                }
                case MESSAGE_REQ: {
                    // 解析body
                    message.setBody(decoderBody(head.getProtocolID(), result));
                    break;
                }
                default:
                    LibraLog.info("LibraDecoder-decode : libraMessage Type is default. type = " + head.getType());
                    break;
            }
            out.add(message);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private Object decoderBody(int protocolId, byte[] array) throws Exception {
        MessageLite messageLite = handlerMgr.searchMessage(protocolId);
        if (messageLite == null) {
            LibraLog.error("LibraDecoder-decoderBody : messagLite is null.protocolId = " + protocolId);
            return null;
        }
        return messageLite.getDefaultInstanceForType().getParserForType().parseFrom(array);
    }

}
