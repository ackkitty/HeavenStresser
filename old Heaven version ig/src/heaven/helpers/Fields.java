package heaven.helpers;

public class Fields {
    final String input;

    public Fields(String input) {
        this.input = input;
    }

    public Fields(String[] inputs) {
        this(parse(inputs));
    }

    static String parse(String[] arguments) {
        StringBuilder output = new StringBuilder();
        for (String argument : arguments)
            output.append(argument).append(" ");
        return output.toString();
    }

    public String getValue(String field) {
        return input.split("-" + field + " ")[1].split(" ")[0];
    }
}
