// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
// The algorithm is based on repetitive addition.

//psuedo code for this project
// i = 0 
// multipler = R0
// n = R1
// product = R2
// for (i = 0, i < n, i++) {
//      product += multipler 
//}

@0
D = A
@i 
M = D //setting counter

@R2
M = 0 //resetting R2

(LOOP)
    @i 
    D = M 
    @R1 //checking if i >= R1
    D = D - M
    @END 
    D; JEQ //if (i == n), breaks condition of for loop

    @i //i++ at the beginning of loop
    M = M + 1
    @R0
    D = M //setting value in R0 to D Register to add to value in R2
    @R2
    M = M + D //adding value in D Register to value in R2

    @LOOP 
    0; JMP //unconditional jump to continue loop

(END)
    @END
    0; JMP //unconditional jump, end program on infinite loop


