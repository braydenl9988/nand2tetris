// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

//psuedo code for this project
// while (keyPressed) {
//      SCREEN = -1
//      SCREEN + 1 = -1
//      .
//      .
//      SCREEN + 511 = -1
//} else {
//      SCREEN = 0
//      SCREEN + 1 = 0
//      .
//      .
//      SCREEN + 511 = 0
//}

(WHILE)
    @0
    D = A
    @i 
    M = D //setting counter

    @8192
    D = A 
    @n 
    M = D 

    @KBD
    D = M //stores keyboard value
    @KEYPRESSED 
    D; JGT //jumps if keyboard value > 0 (basically if key is pressed)
    @KEYRELEASED
    D; JEQ //jumps if keyboard value == 0 (basically if no key is pressed)

    @WHILE
    0; JMP


(KEYPRESSED)
    @i 
    D = M 
    @n //checking if i >= n
    D = D - M
    @WHILE
    D; JEQ //if (i == n), returns back to the original loop

    @i //checking which RAM register we should be writing -1 too
    D = M 
    @SCREEN
    D = D + A 
    @currentScreen
    A = D //sets currentScreen address to the one we want to set to -1
    M = -1

    @i //increase i for loop conditional
    M = M + 1

    @KEYPRESSED
    0; JMP

(KEYRELEASED)
    @i 
    D = M 
    @n //checking if i >= n
    D = D - M
    @WHILE
    D; JEQ //if (i == n), returns back to the original loop

    @i //checking which RAM register we should be writing 0 too
    D = M 
    @SCREEN
    D = D + A 
    @currentScreen
    A = D //sets currentScreen address to the one we want to set to 0
    M = 0

    @i //increase i for loop conditional
    M = M + 1

    @KEYRELEASED
    0; JMP


