package VMTranslator;

import java.io.File;
import java.io.IOException;
import java.lang.String;


public class Main {

    private static String vmSuffix = ".vm";
    private static String asmSuffix = ".asm"; // suffixes just to make life easier
   

    //vm and hack file names
    private static String vmName = "/users/bray/nand2tetris/projects/7/StackArithmetic/StackTest/StackTest.vm";
    private static String asmName = vmName.substring(0, vmName.length() - vmSuffix.length()) + asmSuffix;

    public static void main(String[] args) throws IOException {

        File vmFile = new File(vmName);
        File asmFile = new File (asmName);

        if (asmFile.exists()) { //checks if it exists and delets if it does
            asmFile.delete();
        }

        asmFile.createNewFile(); //creates new file

        Parser vmParser = new Parser(vmFile);
        CodeWriter asmWriter = new CodeWriter(asmFile);

        while (vmParser.hasMoreCommands()) {
            String command = vmParser.commandType();

            if (command == null) {
                vmParser.advance();
                continue;
            } else {
                asmWriter.writeVmCommand(vmParser.readVmCommand());
                switch (command) {
                    case ("C_ARITHMETIC"):
                        asmWriter.writerArithmetic(vmParser.firstArg());
                        break;
                    case("C_POP"):
                        asmWriter.writePopPush(command, vmParser.firstArg(), vmParser.secondArg());
                        break;
                    case("C_PUSH"):
                        asmWriter.writePopPush(command, vmParser.firstArg(), vmParser.secondArg());
                        break;
                }
            }

            vmParser.advance();

        
        }

        asmWriter.close();
        
    }

}
