package Assembler;
import java.lang.String;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class Assembler {

    private static String asmSuffix = ".asm"; // suffixes just to make life easier
    private static String hackSuffix = ".hack";

    // asm and hack file names
    private static String asmName = "/users/bray/nand2tetris/projects/6/pong/PongL.asm";
    private static String hackName = asmName.substring(0, asmName.length() - asmSuffix.length()) + hackSuffix;

    private final static String AT_STRING = "@";

    public static void main(String[] args) throws IOException {
        File asmFile = new File(asmName); // creating files to access them
        File hackFile = new File(hackName);

        if (hackFile.exists()) { // checking if hackFile exists in the first place
            hackFile.delete();
        }
        hackFile.createNewFile(); // create the hackFile

        SymbolTable symbolTable = new SymbolTable(asmFile);
        symbolTable.firstRead();

        BufferedReader reader = new BufferedReader(new FileReader(asmFile)); // creating writers and readers for files
        BufferedWriter writer = new BufferedWriter(new FileWriter(hackFile));

        String fileLine = reader.readLine();

        while (fileLine != null) {
            fileLine = LineParser.removeWhitespace(fileLine); // removing whitespace from instruction

            if (fileLine.length() == 0) { // checking if it is not a blank line
                fileLine = reader.readLine();
                continue;
            }
            String instructionType = LineParser.returnInstructionType(fileLine); // gettin instruction type

            if (instructionType.equals("A_INSTRUCTION")) { // handles a isntruction

                fileLine = fileLine.substring(fileLine.indexOf(AT_STRING) + 1); // removes @ symbol from string
                String finalBinary;

                if (fileLine.matches("[0-9]+")) { //checks if instruction is an integer or variable
                    finalBinary = Code.translateA(fileLine);
                } else {
                    finalBinary = Code.translateA(fileLine, symbolTable);
                }
                writer.write(finalBinary); // writes binary to file
                writer.newLine();

            } else if (instructionType.equals("C_INSTRUCTION")) { // handles c instruction
                String finalBinary = Code.translateC(fileLine);
                writer.write(finalBinary);
                writer.newLine();
            } 
                

            fileLine = reader.readLine();
        }

        writer.close();

        reader.close();

    }
}