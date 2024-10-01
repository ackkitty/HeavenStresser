package heaven.attacks;

import io.netty.buffer.ByteBuf;

import java.net.InetSocketAddress;

public abstract class Method {
    public abstract ByteBuf createData(InetSocketAddress target);
}
