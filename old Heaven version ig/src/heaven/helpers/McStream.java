package heaven.helpers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class McStream {
    public static void write(int object, ByteBuf stream) {
        while ((object & 0xFFFFFF80) != 0) {
            stream.writeByte(object & 0x7F | 0x80);
            object >>>= 7;
        }
        stream.writeByte(object);
    }

    public static void write(String object, ByteBuf stream) {
        write(object.length(), stream);
        stream.writeBytes(object.getBytes());
    }

    public static ByteBuf createHandshake(McVersion version, String hostname, int port, int state) {
        ByteBuf packet = Unpooled.buffer();
        packet.writeByte(0);
        write(version.protocolIdentifier, packet);
        write(hostname, packet);
        packet.writeShort(port);
        write(state, packet);
        return packet;
    }

    public static ByteBuf createLoginStart(McVersion version, String username) {
        ByteBuf packet = Unpooled.buffer();
        packet.writeByte(0);
        write(username, packet);
        if (version.protocolIdentifier > McVersion.v1_18_2.protocolIdentifier) {
            packet.writeByte(0);
            packet.writeByte(0);
        }
        return packet;
    }

    public static void write(ByteBuf packet, ByteBuf stream) {
        write(packet.writerIndex(), stream);
        stream.writeBytes(packet);
    }
}
