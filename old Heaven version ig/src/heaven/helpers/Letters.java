package heaven.helpers;

public class Letters {
    public static String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String getCasual(int length) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < length; index++)
            builder.append(alphabet.charAt((int) (Math.random() * (alphabet.length() - 1))));
        return builder.toString();
    }
}
