package VMTranslator;

import java.lang.String;
import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.HashSet;


public class Parser {

    private File vmFile;
    private BufferedReader fileReader;
    private String currentLine;
    private HashSet<String> arithmeticCommands;

    public Parser(File vmFile) throws IOException {
        this.vmFile = vmFile;

        fileReader = new BufferedReader(new FileReader(this.vmFile));

        advance();

        initializeArithmeticCommands();
    }

    private void initializeArithmeticCommands() { //initialize hashset for arithmetic/logical commands
        arithmeticCommands = new HashSet<String>();

        arithmeticCommands.add("add");
        arithmeticCommands.add("sub");
        arithmeticCommands.add("neg");
        arithmeticCommands.add("eq");
        arithmeticCommands.add("gt");
        arithmeticCommands.add("lt");
        arithmeticCommands.add("and");
        arithmeticCommands.add("or");
        arithmeticCommands.add("not");
    }

    public boolean hasMoreCommands() throws IOException { //checking if there are more commands
        if (currentLine != null) {
            return true;
        } else {
            return false;
        }
    }

    public void advance() throws IOException { //go to the next line
        currentLine = fileReader.readLine();
        if (currentLine != null) {
            removeComment();
        }
        
    }

    public String readVmCommand() {
        return currentLine;
    }

    public String commandType() {
        if (currentLine.length() == 0) {
            return null;
        }

        if (arithmeticCommands.contains(currentLine)) { //checking if it is just arithmetic/logical commands
            return "C_ARITHMETIC";
        } else if (currentLine.substring(0, 3).equals("pop")) { //checking if it is pop command
            return "C_POP";
        } else if (currentLine.substring(0, 4).equals("push")) { //checking if it is push command
            return "C_PUSH";
        } else {
            return null;
        }
    }

    public String firstArg() {
        if (currentLine.indexOf(" ") == -1) { //arithmetic commands should not have spaces
            return currentLine;
        } else if (currentLine.length() > 0) { //if not an arithmetic command, return push/pop
            String removedCommand = currentLine.substring(currentLine.indexOf(" ") + 1); //string after first space
            return removedCommand.substring(0, removedCommand.indexOf(" ")); //returns everything after the command and before second space
        } else {
            return null;
        }
    }

    public int secondArg() {
        if (currentLine.length() > 0) {
            String removedCommand = currentLine.substring(currentLine.indexOf(" ") + 1); //string after first space
            String removedFirstArg = removedCommand.substring(removedCommand.indexOf(" ") + 1); //string after second space
            return Integer.parseInt(removedFirstArg);
        } else {
            return 0;
        }
    }

    public void removeComment() {
        if (currentLine.indexOf("//") != -1) {
            currentLine = currentLine.substring(0, currentLine.indexOf("//")); //eliminates everything after double slash
        }
    }




}

