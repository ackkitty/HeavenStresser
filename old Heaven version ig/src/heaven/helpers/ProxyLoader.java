package heaven.helpers;

import heaven.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxyLoader {
    public ArrayList<Proxy> proxies;
    AtomicInteger index;

    public ProxyLoader(String file) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        Object[] loader = fileReader.lines().distinct().toArray();
        proxies = new ArrayList<>();
        for (Object input : loader) {
            String line = (String) input;
            if (line.contains(":"))
                switch (line.split(":").length) {
                    case 3 ->
                            proxies.add(new Proxy(Proxy.Type.getBy(line.split("://")[0]), new InetSocketAddress(line.split("://")[1].split(":")[0], Integer.parseInt(line.split(":")[2].split(":")[0]))));
                    case 5 ->
                            proxies.add(new Proxy(Proxy.Type.getBy(line.split("://")[0]), new InetSocketAddress(line.split("://")[1].split(":")[0], Integer.parseInt(line.split(":")[2].split(":")[0])), line.split(":")[3]));
                }
        }
        if (proxies.isEmpty()) Instance.exceptionCaught();
        fileReader.close();
        index = new AtomicInteger(0);
    }

    public Proxy getNext() {
        return proxies.get(index.getAndUpdate(index -> (index + 1) % proxies.size()));
    }
}
