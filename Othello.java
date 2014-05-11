
/**
 * Othello is the driver for the model GameBoard, it instantiates GameBoard 
 * and cleanes and provides data for GameBoard
 * 
 * @author (Cohen, Hood) 
 * @version (0.1)
 */
import java.util.Scanner;
import java.util.InputMismatchException;


public class Othello
{
    public static void main(String[] args)
    {
        GameBoard game1 = new GameBoard(8);
        Scanner input = new Scanner(System.in);
        int player1x, player1y, player2x, player2y;
        boolean isValidInput;
        
        while(game1.hasValidMove(Tile.WHITE)|| game1.hasValidMove(Tile.BLACK))
        {
            System.out.println("\f");
            isValidInput = false;
            if(game1.hasValidMove(Tile.BLACK)) // If black has a valid move on the board, otherwise whites turn.
            {
                System.out.println(game1);
                while(!isValidInput) // Keep asking for input, until valid input is recieved. 
                {
                    try
                    {
                        System.out.print(GameBoard.BLACK + " Row = ");
                        player1x = input.nextInt();
                        System.out.print(GameBoard.BLACK + " Column = ");
                        player1y = input.nextInt();
                        
                        if(game1.isEqualToValidMoves(player1x, player1y)) // Is the x/y input a valid move?
                        {
                            game1.move(player1x,player1y,Tile.BLACK);
                            isValidInput = true;
                        // The input was sanitary but that was not a valid move on the board. 
                        } else {
                            System.err.println("You can't move there.");
                        }
                    } catch(InputMismatchException ex) {
                        System.err.println("Only integer input is allowed.");
                        input.nextLine();
                    } catch(ArrayIndexOutOfBoundsException ex) {
                        System.err.println("That integer exceeds the bounds of this board.");
                        input.nextLine();
                    } catch(Exception ex) {
                        System.err.println("An unexpected exception was thrown.");
                        input.nextLine();
                    }
                }
            } else {
                System.out.println("Black had no moves.");
                try{
                    Thread.sleep(3000);
                } catch(Exception ex) {
                    System.err.println("Error sleeping the thread.");
                }
            }
            
            System.out.println("\f");           // Flush the screen
            isValidInput = false;               // Reset to false to check for valid input
            
            if(game1.hasValidMove(Tile.WHITE)) // If white has a valid move on the board, otherwise blacks turn
            {
                System.out.println(game1);          // Print the gameBoard
                while(!isValidInput) // Keep asking for input, until valid input is recieved. 
                {
                    try
                    {
                        System.out.print(GameBoard.WHITE + " Row = ");
                        player2x = input.nextInt();
                        System.out.print(GameBoard.WHITE + " Column = ");
                        player2y = input.nextInt();
                        
                        if(game1.isEqualToValidMoves(player2x, player2y)) // Is the x/y input a valid move?
                        {
                            game1.move(player2x,player2y,Tile.WHITE);
                            isValidInput = true;
                        // The input was sanitary but that was not a valid move on the board. 
                        } else {
                            System.err.println("You can't move there.");
                        }
                    } catch(InputMismatchException ex) {
                        System.err.println("Only integer input is allowed.");
                        input.nextLine();
                    } catch(ArrayIndexOutOfBoundsException ex) {
                        System.err.println("That integer exceeds the bounds of this board.");
                        input.nextLine();
                    } catch(Exception ex) {
                        System.err.println("An unexpected exception was thrown.");
                        input.nextLine();
                    }
                }
            } else {
                System.out.println("White had no moves.");
                try{
                    Thread.sleep(3000);
                } catch(Exception ex) {
                    System.err.println("Error sleeping the thread.");
                }
            }
        } // End of the main game loop. Neither color has a move left
        
        System.out.println("\f");
        System.out.println(game1);
        if(game1.getScore(Tile.BLACK) > game1.getScore(Tile.WHITE)) {
            System.out.println("Black won the game!");
        } else if(game1.getScore(Tile.BLACK) < game1.getScore(Tile.WHITE)) {
            System.out.println("White won the game!");
        } else {
            System.out.println("It was a tie!");
        }
    }
}
