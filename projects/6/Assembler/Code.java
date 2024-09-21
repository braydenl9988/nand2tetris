package Assembler;

import java.util.Arrays;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.Hashtable;

public class Code {

    private final static String EQUAL_STRING = "=";
    private final static String SEMICOLON_STRING = ";";
    private final static int BINARY_LENGTH = 16;

    // hash tables to store all the individual values
    private static Hashtable<String, String> compHash;
    private static Hashtable<String, String> destHash;
    private static Hashtable<String, String> jumpHash;

    

    public static void createCompHash() { // creates comp hash table with corresponding values
        compHash = new Hashtable<String, String>();
        compHash.put("0", "0101010");
        compHash.put("1", "0111111");
        compHash.put("-1", "0111010");
        compHash.put("D", "0001100");
        compHash.put("A", "0110000");
        compHash.put("M", "1110000");
        compHash.put("!D", "0001101");
        compHash.put("!A", "0110001");
        compHash.put("!M", "1110001");
        compHash.put("-D", "0001111");
        compHash.put("-A", "0110011");
        compHash.put("-M", "1110011");
        compHash.put("D+1", "0011111");
        compHash.put("A+1", "0110111");
        compHash.put("M+1", "1110111");
        compHash.put("D-1", "0001110");
        compHash.put("A-1", "0110010");
        compHash.put("M-1", "1110010");
        compHash.put("D+A", "0000010");
        compHash.put("A+D", "0000010");
        compHash.put("D+M", "1000010");
        compHash.put("M+D", "1000010");
        compHash.put("D-A", "0010011");
        compHash.put("D-M", "1010011");
        compHash.put("A-D", "0000111");
        compHash.put("M-D", "1000111");
        compHash.put("D&A", "0000000");
        compHash.put("D&M", "1000000");
        compHash.put("D|A", "0010101");
        compHash.put("D|M", "1010101");

    }

    public static void createDestHash() { // creates dest hash table with corresponding values
        destHash = new Hashtable<String, String>();
        destHash.put("", "000");
        destHash.put("M", "001");
        destHash.put("D", "010");
        destHash.put("DM", "011");
        destHash.put("MD", "011");
        destHash.put("A", "100");
        destHash.put("AM", "101");
        destHash.put("MA", "101");
        destHash.put("AD", "110");
        destHash.put("DA", "110");
        destHash.put("ADM", "111");
    }

    public static void createJumpHash() { // creates jump hash table with corresponding values
        jumpHash = new Hashtable<String, String>();
        jumpHash.put("", "000");
        jumpHash.put("JGT", "001");
        jumpHash.put("JEQ", "010");
        jumpHash.put("JGE", "011");
        jumpHash.put("JLT", "100");
        jumpHash.put("JNE", "101");
        jumpHash.put("JLE", "110");
        jumpHash.put("JMP", "111");
    }

    public static String translateA (String instruction) {
        int address = Integer.parseInt(instruction);
        int[] binaryArray = new int[BINARY_LENGTH]; // binary array to house numbers

        for (int i = 15; i >= 1; i--) { // binary conversion
            binaryArray[i] = address % 2;
            address = address / 2;
        }

        binaryArray[0] = 0;

        return Arrays.toString(binaryArray).replaceAll("[\\[\\],\\s]", "");// return binary as a string
    }

    //method overloading
    public static String translateA(String instruction, SymbolTable symbolTable) {

        if (symbolTable.getSymbol(instruction) == null) { //checks if symbol is not in table
            int nextAvail = symbolTable.getNextAvailableValue();
            String availString = String.valueOf(nextAvail);
            symbolTable.setSymbol(instruction, availString);
            symbolTable.increaseNextAvailable();
        }

        int address = Integer.parseInt(symbolTable.getSymbol(instruction)); //retrieves value associated with symbol

        int[] binaryArray = new int[BINARY_LENGTH]; // binary array to house numbers

        for (int i = 15; i >= 1; i--) { // binary conversion
            binaryArray[i] = address % 2;
            address = address / 2;
        }

        binaryArray[0] = 0;

        return Arrays.toString(binaryArray).replaceAll("[\\[\\],\\s]", "");// return binary as a string
    }


    public static String translateC(String instruction) {
        StringBuilder binaryBuilder = new StringBuilder();
        binaryBuilder.append("111"); // setting leftmost digits to 111

        String dest = ""; // initializing dest, comp, and jump strings
        String comp = "";
        String jump = "";

        if (instruction.indexOf(EQUAL_STRING) != -1) {
            // sets dest string to everything before equal sign
            dest = instruction.substring(0, instruction.indexOf(EQUAL_STRING));
            // sets instruction string to everything after equal sign
            instruction = instruction.substring(instruction.indexOf(EQUAL_STRING) + 1);
        }
        if (instruction.indexOf(SEMICOLON_STRING) != -1) {
            // set jump to everything after semicolon
            jump = instruction.substring(instruction.indexOf(SEMICOLON_STRING) + 1);
            // set instruction to everything before semicolon
            instruction = instruction.substring(0, instruction.indexOf(SEMICOLON_STRING));
        }
        comp = instruction; // set comp to instruction

        if (compHash == null) {
            createCompHash();
        }
        if (destHash == null) {
            createDestHash();
        }
        if (jumpHash == null) {
            createJumpHash();
        }

        String destBinary = handleDest(dest); // string to store binary values returned
        String compBinary = handleComp(comp);
        String jumpBinary = handleJump(jump);

        binaryBuilder.append(compBinary); // appending everything
        binaryBuilder.append(destBinary);
        binaryBuilder.append(jumpBinary);

        return binaryBuilder.toString(); // return full binary string
    }



    // returning binary codes for comp, dest, and jump based off key
    public static String handleComp(String comp) {
        return compHash.get(comp);
    }

    public static String handleDest(String dest) {
        return destHash.get(dest);
    }

    public static String handleJump(String jump) {
        return jumpHash.get(jump);
    }

}
