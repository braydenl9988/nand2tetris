package VMTranslator;

import java.lang.String;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;


public class CodeWriter{

    private File asmFile;
    private String fileName;
    private BufferedWriter fileWriter;
    private int labelCounter;

    public CodeWriter(File asmFile) throws IOException {
        this.asmFile = asmFile;

        fileName = asmFile.getName();

        fileWriter = new BufferedWriter(new FileWriter(this.asmFile));

        labelCounter = 0;

    }

    public void writerArithmetic(String command) throws IOException {

        switch (command) {
            case "add":
                callAdd();
                break;
            case "sub":
                callSub();
                break;
            case "neg":
                callNeg();
                break;
            case "gt":
                callGt();
                break;
            case "lt":
                callLt();
                break;
            case "eq":
                callEq();
                break;
            case "and":
                callAnd();
                break;
            case "or":
                callOr();
                break;
            case "not":
                callNot();
                break;

        }

    }

    public void writePopPush(String command, String segment, int index) throws IOException {
        switch (command) {
            case "C_POP":
                switch (segment) {
                    case ("local"):
                        writeSegmentAddress("LCL", index);
                        break;
                    case("argument"):
                        writeSegmentAddress("ARG", index);
                        break;
                    case("this"):
                        writeSegmentAddress("THIS", index);
                        break;
                    case("that"):
                        writeSegmentAddress("THAT", index);
                        break;
                    case("static"):
                        fileWriter.write("@" + fileName + "." + String.valueOf(index));
                        fileWriter.newLine();
                        break;
                    case("temp"):
                        fileWriter.write("@R" + String.valueOf(5 + index));
                        fileWriter.newLine();
                        break;
                    case("pointer"):
                        fileWriter.write("@R" + String.valueOf(3 + index));
                        fileWriter.newLine();
                        break;
                }

                fileWriter.write("D=A"); //takes address that we need to pop value to
                fileWriter.newLine();  
                fileWriter.write("@R13"); //stores it in R13 temporarily
                fileWriter.newLine();
                fileWriter.write("M=D");
                fileWriter.newLine();

                popStackD(); //pops top value and stores in D

                fileWriter.write("@R13"); //gets appropriate address in R13
                fileWriter.newLine();
                fileWriter.write("A=M"); //sets it to A reg
                fileWriter.newLine();
                fileWriter.write("M=D"); //writes D to RAM[A]
                fileWriter.newLine();

                break;
            
            case "C_PUSH":
                switch (segment) {
                    case ("local"):
                        writeSegmentAddress("LCL", index);
                        fileWriter.write("D=M");
                        fileWriter.newLine();
                        break;
                    case("argument"):
                        writeSegmentAddress("ARG", index);
                        fileWriter.write("D=M");
                        fileWriter.newLine();
                        break;
                    case("this"):
                        writeSegmentAddress("THIS", index);
                        fileWriter.write("D=M");
                        fileWriter.newLine();
                        break;
                    case("that"):
                        writeSegmentAddress("THAT", index);
                        fileWriter.write("D=M");
                        fileWriter.newLine();
                        break;
                    case("static"):
                        fileWriter.write("@" + fileName + "." + String.valueOf(index));
                        fileWriter.newLine();
                        fileWriter.write("D=M");
                        fileWriter.newLine();
                        break;
                    case("temp"):
                        fileWriter.write("@" + String.valueOf(5 + index));
                        fileWriter.newLine();
                        fileWriter.write("D=M");
                        fileWriter.newLine();
                        break;
                    case("pointer"):
                        fileWriter.write("@" + String.valueOf(3 + index));
                        fileWriter.newLine();
                        fileWriter.write("D=M");
                        fileWriter.newLine();
                        break;
                    case ("constant"):
                        fileWriter.write("@" + String.valueOf(index));
                        fileWriter.newLine();
                        fileWriter.write("D=A");
                        fileWriter.newLine();
                }

                
                pushStackD(); //push value from D reg to top of stack
                break;          
        }
    }

    public void close() throws IOException {
        fileWriter.close();
    }

    public void writeVmCommand(String vmCommand) throws IOException {
        fileWriter.write("//" + vmCommand);
        fileWriter.newLine();
    }

    //helper methods from here and below

    private void writeSegmentAddress(String segment, int index) throws IOException {
        fileWriter.write("@" + segment); //gets segment address
        fileWriter.newLine();
        fileWriter.write("D=M"); 
        fileWriter.newLine();

        fileWriter.write("@" + String.valueOf(index)); //gets the segement address plus the index
        fileWriter.newLine();

        fileWriter.write("A=D+A");
        fileWriter.newLine();
    }



    private void popStackD() throws IOException {
        decreaseSp();
        fileWriter.write("A=M"); //A = sp
        fileWriter.newLine();
        fileWriter.write("D=M"); //D = *sp
        fileWriter.newLine();
    }

    private void pushStackD() throws IOException {
        loadSpToA(); //get the stack pointer
        fileWriter.write("M=D"); //store value in D in RAM[SP]
        fileWriter.newLine();
        increaseSp(); //increase the SP

    }

    private void decreaseSp() throws IOException {
        fileWriter.write("@SP");
        fileWriter.newLine();
        fileWriter.write("M=M-1");
        fileWriter.newLine();
    }

    private void increaseSp() throws IOException {
        fileWriter.write("@SP");
        fileWriter.newLine();
        fileWriter.write("M=M+1");
        fileWriter.newLine();
    }

    private void loadSpToA() throws IOException {
        fileWriter.write("@SP");
        fileWriter.newLine();
        fileWriter.write("A=M");
        fileWriter.newLine();
    }

    private void jumpConCode (String jumpCon) throws IOException {

        //subtraction to check difference in values
        fileWriter.write("D=M-D");
        fileWriter.newLine();

        //creates label to jump to if condition is met
        fileWriter.write("@COMP" + String.valueOf(labelCounter));
        fileWriter.newLine();
        fileWriter.write("D; " + jumpCon);
        fileWriter.newLine();
        loadSpToA();

        fileWriter.write("M=0"); //setting value in memory to false to false
        fileWriter.newLine();

        //jump to increase if con is not met
        fileWriter.write("@INCREASE" + String.valueOf(labelCounter)); 
        fileWriter.newLine();
        fileWriter.write("0;JMP");
        fileWriter.newLine();

        //setting memory to true if jumped to (condition is met)
        fileWriter.write("(COMP" + String.valueOf(labelCounter) + ")");
        fileWriter.newLine();
        loadSpToA();
        fileWriter.write("M=-1");
        fileWriter.newLine();

        //label to jump to from/over comp label to increase stack pointer
        fileWriter.write("(INCREASE" + String.valueOf(labelCounter) + ")"); 
        fileWriter.newLine();
        increaseSp();

        labelCounter++; //increase labelCounter


    }

    private void callAdd() throws IOException {
        popStackD(); //pop top stack value to d reg
        decreaseSp(); //decreasing stack pointer
        loadSpToA(); //get next value's address
        
        fileWriter.write("M=D+M"); //operand
        fileWriter.newLine();

        increaseSp();
    }
    private void callSub() throws IOException {
        popStackD(); //pop top stack value to d reg
        decreaseSp(); //decreasing stack pointer
        loadSpToA(); //get next value's address
        
        fileWriter.write("M=M-D"); //operand
        fileWriter.newLine();

        increaseSp();
    }

    private void callAnd() throws IOException {
        popStackD(); //pop top stack value to d reg
        decreaseSp(); //decreasing stack pointer
        loadSpToA(); //get next value's address
        
        fileWriter.write("M=D&M"); //operand
        fileWriter.newLine();

        increaseSp();
    }

    private void callOr() throws IOException {
        popStackD(); //pop top stack value to d reg
        decreaseSp(); //decreasing stack pointer
        loadSpToA(); //get next value's address
        
        fileWriter.write("M=D|M"); //operand
        fileWriter.newLine();

        increaseSp();
    }

    private void callNeg() throws IOException {
        popStackD(); //pop top stack value to d reg
        loadSpToA(); //get the stack pointer address
        fileWriter.write("M=-D"); //load with operand
        fileWriter.newLine();
        increaseSp(); //increase stack pointer
    }

    private void callNot() throws IOException {
        popStackD(); //pop top stack value to d reg
        loadSpToA(); //get the stack pointer address
        fileWriter.write("M=!D"); //load with operand
        fileWriter.newLine();
        increaseSp(); //increase stack pointer
    }

    private void callGt() throws IOException {
        popStackD(); //pop top stack value to d reg
        decreaseSp(); //decrease stack pointer
        loadSpToA(); //get stack pointer address

        jumpConCode("JGT");

    }

    private void callLt() throws IOException {
        popStackD(); //pop top stack value to d reg
        decreaseSp(); //decrease stack pointer
        loadSpToA(); //get stack pointer address

        jumpConCode("JLT");

    }

    private void callEq() throws IOException {
        popStackD(); //pop top stack value to d reg
        decreaseSp(); //decrease stack pointer
        loadSpToA(); //get stack pointer address

        jumpConCode("JEQ");

    }

    






}



   