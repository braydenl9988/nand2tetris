package AssemblerTest;
import java.util.Hashtable;
import java.lang.StringBuilder;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SymbolTable{

    private Hashtable<String, String> symTable;
    private BufferedReader reader;
    private File asmFile;
    private int nextAvailableValue;

    public SymbolTable(File asmFile) throws IOException { //links asmFile to symbolTable
        //initialization
        this.asmFile = asmFile; 
        this.reader = new BufferedReader(new FileReader(this.asmFile));
        nextAvailableValue = 16;
        symTable = new Hashtable<String, String>();


        for (int i = 0; i < 16; i++) { //loop for registers
            StringBuilder tempString = new StringBuilder();
            tempString.append("R");
            tempString.append(i);
            String regSym = tempString.toString();

            
            symTable.put(regSym, String.valueOf(i));
        }

        symTable.put("SCREEN", "16384"); //default symbols in symbol tables
        symTable.put("KBD","24576");
        symTable.put("SP", "0");
        symTable.put("LCL", "1");
        symTable.put("ARG", "2");
        symTable.put("THIS", "3");
        symTable.put("THAT", "4");

    }

    public void setSymbol(String name, String address) {
        symTable.put(name, address);
    }

    public String getSymbol(String name) {
        return symTable.get(name);
    }

    public void increaseNextAvailable(){
        nextAvailableValue++;
    }

    public int getNextAvailableValue() {
        return nextAvailableValue;
    }

    public void firstRead() throws IOException {
        String currLine = reader.readLine();
        int lineCounter = 0;

        while (currLine != null) {
            currLine = LineParser.removeWhitespace(currLine); // removing whitespace from instruction

            if (currLine.length() == 0) { // checking if it is not a blank line
                currLine = reader.readLine();
                continue;
            }
            String instructionType = LineParser.returnInstructionType(currLine); // gettin instruction type

            if (instructionType.equals("L_INSTRUCTION")) {
                String labelName = currLine.replaceAll("[\\(\\)]","");
                symTable.put(labelName, String.valueOf(lineCounter));
                lineCounter--;
            }

            lineCounter++;
            currLine = reader.readLine();
   
        }
    }

   

}