package heaven.helpers;

public enum McVersion {
    v1_7("1.7", 3),
    v1_7_2("1.7.2", 4),
    v1_7_6("1.7.6", 5),
    v1_8("1.8", 47),
    v1_9("1.9", 107),
    v1_9_1("1.9.1", 108),
    v1_9_2("1.9.2", 109),
    v1_9_3("1.9.3", 110),
    v1_10("1.10", 210),
    v1_11("1.11", 315),
    v1_11_1("1.11.1", 316),
    v1_12("1.12", 335),
    v1_12_1("1.12.1", 338),
    v1_12_2("1.12.2", 340),
    v1_13("1.13", 393),
    v1_13_1("1.13.1", 401),
    v1_13_2("1.13.2", 404),
    v1_14("1.14", 477),
    v1_14_1("1.14.1", 480),
    v1_14_2("1.14.2", 485),
    v1_14_3("1.14.3", 490),
    v1_14_4("1.14.4", 498),
    v1_15("1.15", 573),
    v1_15_1("1.15.1", 575),
    v1_15_2("1.15.2", 578),
    v1_16("1.16", 735),
    v1_16_1("1.16.1", 736),
    v1_16_2("1.16.2", 751),
    v1_16_3("1.16.3", 753),
    v1_16_4("1.16.4", 754),
    v1_17("1.17", 755),
    v1_17_1("1.17.1", 756),
    v1_18("1.18", 757),
    v1_18_2("1.18.2", 758),
    v1_19("1.19", 759),
    v1_19_1("1.19.1", 760),
    v1_19_3("1.19.3", 761),
    v1_19_4("1.19.4", 762),
    v1_20("1.20", 763),
    v1_20_2("1.20.2", 764),
    v1_20_3("1.20.3", 765),
    v1_20_5("1.20.5", 766),
    INVALID(null, -1);
    public final String identifier;
    public final int protocolIdentifier;

    McVersion(String identifier, int protocolIdentifier) {
        this.identifier = identifier;
        this.protocolIdentifier = protocolIdentifier;
    }

    public static McVersion getBy(String identifier) {
        for (McVersion version : values())
            if (identifier.equalsIgnoreCase(version.identifier)) return version;
        throw new NullPointerException("No MinecraftVersion with the name \"" + identifier + "\" was found.");
    }
}
