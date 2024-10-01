package heaven.helpers;

public enum HttpVersion {
    v1_0("1.0", "HTTP/1.0"),
    v1_1("1.1", "HTTP/1.1"),
    EXCEPTION("exception", "HTTP/null");
    public final String identifier;
    public final String protocolIdentifier;

    HttpVersion(String identifier, String protocolIdentifier) {
        this.identifier = identifier;
        this.protocolIdentifier = protocolIdentifier;
    }

    public static HttpVersion getBy(String identifier) {
        for (HttpVersion version : values())
            if (identifier.equalsIgnoreCase(version.identifier)) return version;
        throw new NullPointerException("No MinecraftVersion with the name \"" + identifier + "\" was found.");
    }
}
