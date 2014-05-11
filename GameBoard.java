
/**
 * GameBoard contains the methods tht keep track of the progress in the game
 * and manipulate it. It checks to see if a move is valid places the piece and flips the opponents pieces.
 * 
 * @author (Cohen, Hood) 
 * @version (0.1)
 */
public class GameBoard
{
    // Constants
    public static final int IN_PROGRESS = 0,
                            BLACK_WINS  = 1,
                            WHITE_WINS  = 2,
                            TIE = 3;
                            
                            
    public static final char EMPTY = 32,        // Empty Space
                             BLACK = 9822,      // Black Knight
                             WHITE = 9816,      // White Knight
                             VALID_MOVE = 9735; // 90 deg arrow
                            
    // private instance variables                       
    private Tile[][] gameBoard;     // 2D array of tile objects
    private int gameBoardSize;      // Store the x/y length of the board
    private int stateOfTheGame;     // Used for the main game loop, stores the constants above
    private boolean[][] validMoves; // 2D array of true/false
    private int blackScore;         
    private int whiteScore;
    
    
    // Constructor
    public GameBoard(int gameBoardSize)
    {
        setStateOfTheGame(IN_PROGRESS);     // Set the game to be in progress
        setGameBoardSize(gameBoardSize);    // Set gameBoardSize to what was provided in the arguements.
        setGameBoard();                     // Initialize all the tiles on the board, and starting tiles.                
        setValidMoves();                    // Initialize the validMoves array and populate it with valid moves for BLACK
        setScore(Tile.BLACK);
        setScore(Tile.WHITE);
    }
    
    // Initialization methods, used at the startup of the game. 
    public void setGameBoardSize(int gameBoardSize)
    {
        // if gameBoardSize is divisible by 2 && larger than 4 and less than 20
        if(gameBoardSize%2 == 0 && gameBoardSize > 4 && gameBoardSize < 20) {
            this.gameBoardSize = gameBoardSize;
        // if not then set size to 8
        } else {
            this.gameBoardSize = 8;
        }
    }
    public void setGameBoard()
    {
        gameBoard = new Tile[getGameBoardSize()][getGameBoardSize()];
        for(int i = 0; i < gameBoard.length; i++)
        {
            for(int j = 0; j < gameBoard[i].length; j++)
            {
                gameBoard[i][j] = new Tile();
            }
        }
        
        gameBoard[gameBoard.length/2-1][gameBoard.length/2-1].setState(Tile.BLACK);
        gameBoard[gameBoard.length/2-1][gameBoard.length/2].setState(Tile.WHITE);
        gameBoard[gameBoard.length/2][gameBoard.length/2-1].setState(Tile.WHITE);
        gameBoard[gameBoard.length/2][gameBoard.length/2].setState(Tile.BLACK);
    }
    public void setValidMoves()
    {
        validMoves = new boolean[getGameBoardSize()][getGameBoardSize()];   // Initialize the array
    }
    
    public void setScore(int offensiveColor)
    {
        if(offensiveColor == Tile.BLACK)
        {
            blackScore = score(Tile.BLACK);
            if(blackScore == 0)
                setStateOfTheGame(WHITE_WINS);
        } else if(offensiveColor == Tile.WHITE) {
            whiteScore = score(Tile.WHITE);
            if(whiteScore == 0)
                setStateOfTheGame(BLACK_WINS);
        }
    }
    
    // Setters
    public void setStateOfTheGame(int stateOfTheGame)
    {
        this.stateOfTheGame = stateOfTheGame;
    }
    
    
    // Getters
    public void getValidMoves(int offenseColor)
    {
        for(int i = 0; i < gameBoard.length; i++)
        {
            for(int j = 0; j < gameBoard[i].length; j++)
            {
                validMoves[i][j] = false;   // Reset moves
                // short circuit eval, don't check places that already have a tile
                // does row i, column j, have a valid move for the piece on the offensive?
                if(gameBoard[i][j].getState() < 1 && isValid(i,j,offenseColor)) 
                {
                    validMoves[i][j] = true;    // There is a move, set to true
                }
            }
        }
    }
    public int getGameBoardSize()
    {
        return gameBoardSize;
    }
    
    public int getStateOfTheGame()
    {
        return stateOfTheGame;
    }
    
    public Tile getTile(int x, int y)
    {
        return gameBoard[x][y];
    }
    
    public int getScore(int state)
    {
        if(state == Tile.BLACK) {
            return blackScore;
        } else if(state == Tile.WHITE) {
            return whiteScore;
        } else {
            System.err.println("Else inside public int getScore(int state)");
            System.exit(1);
            return 0;
        }
    }
    
    // Other Methods
    // Score cycles through gameBoard[][] and increments for every of the players tiles on the board
    public int score(int offensiveColor)
    {
        int score = 0;
        for(int i = 0; i < gameBoard.length; i++)
        {
            for(int j = 0; j < gameBoard[i].length; j++)
            {
                if(gameBoard[i][j].getState() == offensiveColor)
                    score++;
            }
        }
        return score;
    }
    
    // Attempts to place offensive color at x / y, if it is valid
    // flip the defensive color you out flank.
    public void move(int x, int y, int offensiveColor)
    {
        if(validMoves[x][y] == true)                    // is a valid move
        {
            flip(x,y,offensiveColor);                   // flip surrounding pieces
            gameBoard[x][y].setState(offensiveColor);   // place piece
            setScore(offensiveColor);                   // Keep track of the score
        }
    }
    
    public int opponentColor(int state)
    {
        if(state == Tile.BLACK) 
            return Tile.WHITE;
        else if(state == Tile.WHITE)
            return Tile.BLACK;
        else 
            return Tile.EMPTY;
    }
    
    // returns true if one valid move is found. 
    // Called by getValidMoves to populate the validMoves[][].
    private boolean isValid(int x, int y, int state)
    {
        // Is the move within the boundries of the board?
        if(x > getGameBoardSize() || y > getGameBoardSize())
        {
            return false;
        } 
        
        int opponentState = opponentColor(state); //initialize
        
            
        for(int i = -1; i <= 1; i++)
        {
            
            for(int j = -1; j <= 1; j++)
            {
               
                if((withinBounds(i+x) && withinBounds(j+y)) && !(i == 0 && j == 0))
                {
                   
                    // Opponent piece found in perimiter is my piece on the other side?
                    if(gameBoard[x+i][y+j].getState() == opponentState) 
                    {
                        int k = 2;
                        boolean tileEmpty = true;
                        //while k is not greater than the board.
                        //while the current tile being checked (location in the loop * k) is not empty
                        while ((k < getGameBoardSize() && tileEmpty)) {
                           if(withinBounds(k*i+x) && withinBounds(k*j+y))
                           {
                               if (gameBoard[k*i+x][k*j+y].getState() == Tile.EMPTY)
                                    tileEmpty = false;
                               else if (gameBoard[k*i+x][k*j+y].getState() == state)
                                    return true;
                           } 
                           k++;
                        }
                    }
                }
                
            }
        }
        
        return false;
    }
    
    
    private void flip(int x, int y, int state)
    {
        int opponentState = Tile.EMPTY;
        if(state == Tile.BLACK)
            opponentState = Tile.WHITE;
        if(state == Tile.WHITE)
            opponentState = Tile.BLACK;
            
        for(int i = -1; i <= 1; i++)
        {
            
            for(int j = -1; j <= 1; j++)
            {
                if((withinBounds(i+x) && withinBounds(j+y)) && !(i == 0 && j == 0))
                {
                    // Opponent piece found in perimiter is my piece on the other side?
                    if(gameBoard[x+i][y+j].getState() == opponentState) 
                    {
                        int k = 2;
                        //while k is not greater than the board.
                        //while the current tile being checked (location in the loop * k) is not empty
                        if(withinBounds(k*i+x) && withinBounds(k*j+y))
                        {                         
                            // prophosizing... if on the edge of the board and it checks a tile that does not exist it will continue until it is out of bounds. maybe a check that it is in bounds within the loop because k will increment
                            while (withinBounds(k*i+x) && withinBounds(k*j+y) && k < getGameBoardSize() && gameBoard[k*i+x][k*j+y].getState() != Tile.EMPTY) {
                               
                                   if(gameBoard[k*i+x][k*j+y].getState() == state) {
                                       int q = 1;
                                       while(gameBoard[q*i+x][q*j+y].getState() != state)
                                       {
                                           gameBoard[q*i+x][q*j+y].setState(state);
                                           q++;
                                       }
                                   }
                               k++;
                            }
                        }
                    }
                }
                
            }
        }
    }
    
    private boolean withinBounds(int index)
    {
        if(index >= 0 && index < getGameBoardSize() )
            return true;
        return false;
    }
    
    public boolean isEqualToValidMoves(int row, int col)
    {
        if(validMoves[row][col] == true)
            return true;
        return false;
    }
    
    public boolean hasValidMove(int state)
    {
        getValidMoves(state);
        for(int i = 0; i < this.getGameBoardSize(); i++)
        {
            for(int j = 0; j < this.getGameBoardSize(); j++)
            {
                if(validMoves[i][j] == true)
                    return true;
            }
        }
        return false;
    }
    
    public String toString()
    {
        StringBuilder gameBoardString = new StringBuilder("");
        gameBoardString.append("## OTHELLO ####$ ");
        for(int i = 0; i < score(Tile.BLACK); i++)
            gameBoardString.append(BLACK);
        gameBoardString.append(" " + BLACK + "  " + score(Tile.BLACK) + " squares\n");
        gameBoardString.append("###############$ ");
        for(int i = 0; i < score(Tile.WHITE); i++)
            gameBoardString.append(WHITE);
        gameBoardString.append(" " + WHITE + "  " + score(Tile.WHITE) + " squares\n\n");
        
        gameBoardString.append("[X]\t");
        for(int i = 0; i < gameBoard.length; i++)
        {
            gameBoardString.append("[" + i + "]\t");
        }
        gameBoardString.append("\n\n\n\n");
        
        for(int i = 0; i < gameBoard.length; i++)
        {
            for(int j = 0; j <= gameBoard[i].length; j++)
            {
                if(j == 0) { gameBoardString.append("[" + i + "]\t"); }
                else
                {
                    if(gameBoard[i][j-1].getState() == Tile.BLACK)
                        gameBoardString.append(BLACK);
                    else if(gameBoard[i][j-1].getState() == Tile.WHITE)
                        gameBoardString.append(WHITE);
                    else if(validMoves[i][j-1] == true)
                        gameBoardString.append(VALID_MOVE);
                    else
                        gameBoardString.append(EMPTY);
                    
                    gameBoardString.append("\t");
                }
            }
            gameBoardString.append("\n\n\n\n");
        }
       
        return gameBoardString.toString();
    }
}
