package heaven.helpers;

import java.net.InetSocketAddress;

public class Proxy {
    public Type type;
    public InetSocketAddress address;
    public String username, password;

    public Proxy(Type type, InetSocketAddress address) {
        this.type = type;
        this.address = address;
    }

    public Proxy(Type type, InetSocketAddress address, String userpass) {
        if (type == Type.SOCKS_4)
            throw new RuntimeException("The proxy kind \"socks4\" does not support username:password authentication.");
        this.address = address;
        this.username = userpass.split(":")[0];
        this.password = userpass.split(":")[1];
    }

    public enum Type {
        SOCKS_4("socks4"), SOCKS_5("socks5"), HTTP("http");
        final String identifier;

        Type(String identifier) {
            this.identifier = identifier;
        }

        public static Type getBy(String identifier) {
            for (Type type : values())
                if (identifier.equalsIgnoreCase(type.identifier)) return type;
            throw new NullPointerException("No ProxyType with the name \"" + identifier + "\" was found.");
        }
    }
}
