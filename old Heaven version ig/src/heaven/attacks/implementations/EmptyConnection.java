package heaven.attacks.implementations;


import heaven.attacks.Method;
import io.netty.buffer.ByteBuf;

import java.net.InetSocketAddress;

public class EmptyConnection extends Method {
    @Override
    public ByteBuf createData(InetSocketAddress target) {
        return null;
    }
}
