/**
 * Represent a single tile on the board, and manipulate its status. 
 * 
 * @author (Cohen, Hood) 
 * @version (0.1)
 */
public class Tile
{
    public static final int EMPTY = 0,
                            BLACK = 1,
                            WHITE = 2;
                            
    // instance variables
    private int state;
    
    public Tile()
    {
        this.setState(EMPTY);
    }
    // Setters
    public void setState(int s) { state = s; }
    // Getters
    public int getState()   { return state; }
    
    public void flip()
    {
        if(this.getState() == BLACK) { this.setState(WHITE); }
        if(this.getState() == WHITE) { this.setState(BLACK); }
    }
    
    public String toString()
    {
        String returnString;
        if(state == BLACK)
            returnString = "" + GameBoard.BLACK; // Cast char to a string
        else if(state == WHITE)
            returnString = "" + GameBoard.WHITE; // Cast char to a string
        else
            returnString = "" + GameBoard.EMPTY; // Cast char to a string
            
        return returnString;
    }
}
