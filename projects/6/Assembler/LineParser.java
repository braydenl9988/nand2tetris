package Assembler;
public class LineParser {

    private final static String AT_STRING = "@";
    private final static String COMMENT_STRING = "//";
    private final static String LABEL_BEGIN = "(";

    public static String returnInstructionType(String instruction) {
        if (instruction.substring(0, 1).equals(AT_STRING)) { // checks if string starts with @ symbol
            return "A_INSTRUCTION";
        } else if (instruction.substring(0, 1).equals(LABEL_BEGIN)) { // checks if string starts with parentheses
            return "L_INSTRUCTION";
        } else { // else returns that it is a c instruction
            return "C_INSTRUCTION";
        }
    }

    public static String removeWhitespace(String fileLine) {
        fileLine = fileLine.replaceAll("\\s+", ""); // removes all whitespace

        if (fileLine.indexOf(COMMENT_STRING) != -1) { // removes comments
            fileLine = fileLine.substring(0, fileLine.indexOf(COMMENT_STRING));
        }

        return fileLine;
    }

}
