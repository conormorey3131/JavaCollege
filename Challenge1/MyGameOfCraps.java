/**
 * A class to represent the Game of Craps
 * 
 * Extended version of the class Craps in Dietel and Dietel JHTP 9e.
 * Read carefully, and explore each line of code using the Debugger.
 * 
 * The code demonstrates the concepts met to date and enumerated types:
 * 
 *   1.  printing using println/printf.
 *   2.  constants, variables, methods, object creation
 *   3.  arrays, including passing to and returning from methods
 *   4.  selection (if/else, switch) and iteration (while, for, do/while)
 *   5.  Random number generation using class Random
 *   6.  private and public access
 *   7.  enumerator for the game status 
 *   8.  enumerator for the name of the dice roll value
 *   9.  Javadoc documentation throughout: file header comment and methods
 *  10.  The ternary (conditional) operator ?:
 *  11.  boolean variable (can be true or false)
 *  
 * @author Conor Morey 20251076
 * @version 7 Feb 2021
 * 
 * Edit: Added the options to allow the game be ran a choosen number of time by the user
 *       Also added a function which shows the user how many games they won/loss
 */
import java.util.Random;
import java.util.*;
public class MyGameOfCraps {
    // create random number generator (once only) for use in method rollDice
    private static final Random rng = new Random(); 

    /*
     * constants for the strings for won/lost  
     * (include unicode characters for happy/sad faces see: https://unicode-table.com/en/)
     */ 
    private static final String WON_STR = "Congratulations 😀, you won this game";
    private static final String LOST_STR = "Hard luck 😳, you lost this game";

    // enumerated type with enumerators that represent the game status
    private enum Status { WON, LOST, CONTINUE };

    /* 
     * enumerated type for names of rolls with special names replacing basic values
     * 2 is SNAKE EYES, 3 is TREY, 11 is YO_LEVEN, 12 is BOX_CARS
     */ 
    private enum RollName { ZERO_NA,  
        ONE_NA, SNAKE_EYES, TREY, FOUR, FIVE,     SIX,
        SEVEN,  EIGHT,      NINE, TEN,  YO_LEVEN, BOX_CARS };

    /**
     * Play game of craps the required number of times
     * @param args   command line arguments
     * @param numberWon number of games won
     * @param numberLost number of games lost
     * @param numberOfGames number of games the user wants to play
     */
    public static void main( String[] args ) {        
        Scanner keyboard = new Scanner(System.in);
        int game;
        int numberWon = 0;
        int numberLost = 0;

        System.out.println("My Game of Craps, Author: Conor Morey 20251076");

        /*
         * pseudocode goes here.
         * 1.   Begin
         * 2.       Display My Game of Craps + author
         * 3.       Input how many games you want to play with keyboard
         * 4.       Display number of games
         * 5.       Play the game
         */

        System.out.println("How many games would you like to play? ");
        int numberOfGames = keyboard.nextInt();
        for ( game=1; game<=numberOfGames; ++game ) {
            System.out.printf("\n### Game %d ###\n", game);
            if (playGame()) {
                numberWon++;
            }

            else {
                numberLost++; }
        } --game; // Actual number of games so far is one less, hence --, why?
        // Sentinel controlled iteration, play while user inputs "Y", add pseudo code…

        /*
         * 6.       Display enter Y or N  - Do you want to play the game again?
         * 7.       User inputs Y or N with keyboard. Y = Yes,  N = No
         * 8.       If user enters Y - code repeats itself from line 4. in the sudocode
         * 9.       If user enters N - game is terminated and code displays  You played the number of d games. Bye!
         * 10.  End
         */

        boolean isPlayAgain;
        do {
            System.out.print("\nDo you wish to play another game [y/N]: ");
            isPlayAgain = keyboard.next().toUpperCase().equals("Y");
            if ( isPlayAgain) {
                System.out.printf("\n###Game %d ###\n", ++game);
                playGame();
            }
        } while ( isPlayAgain );
        System.out.printf("You played %d games, you won %d and lost %d. Thank you for playing!\n", game, numberWon, numberLost );

    } // end main method
    /**
     * Plying the game
     * @param point tracks if the game was a win/loss
     * @param rollDice rolls the dice which a key part of the game
     */
    public static boolean playGame() {
        Status gameStatus = Status.CONTINUE;   // game will CONTINUE until WON or LOST
        int point = 0;                         // point if not won or lost on first roll  

        int rollDice = rollAndDisplayDice(2);      // roll two dice
        /*
         * Based on roll value, change game status; if continue remember the point value
         * The switch expression converts two dice roll value to corresponding enumerator.
         *    RollValue.values() returns an array of the enumerators of RollValue.
         *    RollValue.values()[roll] maps roll to the corresponding RollValue enumerator.
         */ 
        RollName rollName = RollName.values()[rollDice]; // Map to RollName enumerator 
        switch ( rollName ) {        
            case SEVEN: 
            case YO_LEVEN:          
            gameStatus = Status.WON;    // win if 7 or 11 on first roll
            break;

            case SNAKE_EYES: 
            case TREY: 
            case BOX_CARS: 
            gameStatus = Status.LOST;   // lose with 2, 3 or 12 on first roll
            break;

            default:         
            point = rollDice;           // neither won or lost, so store the point
            System.out.printf( "The Point is %s (%d). Roll %d before 7 to win\n", 
                rollName.name(), point, point );
            break;                      // this break is optional
        } 

        /*
         * Continue playing until player either throws point (won) or seven (lost)
         */
        while ( gameStatus == Status.CONTINUE ) { 
            rollDice = rollAndDisplayDice(2);

            if ( rollDice == point )             // status becomes win by making point
                gameStatus = Status.WON;
            else if ( rollDice == 7 )            // status lose by rolling 7 before point
                gameStatus = Status.LOST;
        } 

        /*
         * Now if here, the player must have lost or won, 
         *   hence display won or lost details using the WON_STR or LOST_STR
         */
        System.out.println(gameStatus == Status.WON ? WON_STR : LOST_STR);

        return ( gameStatus == Status.WON ); // return true if won or false if lost
    }       // end of method playGame

    /**
     * Roll requested number of dice and store in an array with sum
     * @param numberDice    the number of dice to role
     * @return an array where [0] element is the sum followed by individual rolls
     */
    private static int[] rollDice(int numberDice) {
        int[] diceRoll = new int[numberDice+1];

        for (int i=1; i < diceRoll.length; i++) {
            diceRoll[i] = rng.nextInt( 6 ) + 1; 
            diceRoll[0] += diceRoll[i]; 
        }

        return diceRoll; // return sum of dice
    } // end method rollDice

    /**
     * Display sum of dice rolls and the dice values
     * @param diceArray  array with total in first element and then individual dice values
     */
    private static void displayDice(int[] diceArray) {
        System.out.printf("Rolled %2d  Dice (", diceArray[0]);
        for (int i=1; i < diceArray.length; i++) {
            System.out.printf("%s %d", (i>1) ? "," : "", diceArray[i]);
        }
        System.out.println( " )" );
    } // end method displayDice

    /**
     * Roll requested number of dice and display rolls
     * @param numberDice    the number of dice to role
     * @return sum of dice rolled
     */
    private static int rollAndDisplayDice(int numberDice) {
        int[] rolledDice = rollDice(numberDice);
        displayDice(rolledDice);
        return rolledDice[0];    
    } // end method rollAndDisplayDice
} // end class MyGameOfCraps
