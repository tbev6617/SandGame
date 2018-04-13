import java.awt.*;
import java.util.*;

public class SandLab
{
  //Step 4,6
  //add constants for particle types here
  public static final int EMPTY = 0;
  public static final int METAL = 1;
  public static final int SAND = 2;
  public static final int WATER = 3;
  public static final int MUD = 4;
  public static final int GRASS = 5;
  public static final int STEAM = 6;
  public static final int CLEAR = 7;
  
  //do not add any more fields below
  private int[][] grid;
  private SandDisplay display;
  
  
  /**
   * Constructor for SandLab
   * @param numRows The number of rows to start with
   * @param numCols The number of columns to start with;
   */
  public SandLab(int numRows, int numCols)
  {
    String[] names;
    // Change this value to add more buttons
    //Step 4,6
    names = new String[8];
    //Each value needs a name for the button
    names[EMPTY] = "Erase";
    names[METAL] = "Metal";
    names[SAND] = "Sand";
    names[WATER] = "Water";
    names[MUD] = "Mud";
    names[GRASS] = "Grass";
    names[STEAM] = "Steam";
    names[CLEAR] = "CLEAR ALL";
    
    
    //1. Add code to initialize the data member grid with same dimensions
    grid = new int[numRows][numCols];
    
    display = new SandDisplay("Sand Game", numRows, numCols, names);
  }
  
  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool)
  {
    //2. Assign the values associated with the parameters to the grid
	  if (tool == CLEAR)
	  {
		  grid = new int[grid.length][grid[0].length];
	  }
	  else
	  {
		  grid[row][col] = tool;
	  }
  }

  //copies each element of grid into the display
  public void updateDisplay()
  {
      //Step 3
	  //Hint - use a nested for loop
	  for(int row = 0; row < grid.length; row++)
	  {
		  for(int col = 0; col < grid[0].length; col++)
		  {
			  int element = grid[row][col];
			  if (element == METAL)
			  {
				  display.setColor(row, col, Color.GRAY);
			  }
			  else if (element == SAND)
			  {
				  display.setColor(row, col, new Color(220, 160, 90));
			  }
			  else if (element == WATER)
			  {
				  display.setColor(row, col, new Color(0, 100, 200));
			  }
			  else if (element == MUD)
			  {
				  display.setColor(row, col, new Color(100, 70, 35));
			  }
			  else if (element == GRASS)
			  {
				  display.setColor(row, col, new Color(180, 225, 0));
			  }
			  else if (element == STEAM)
			  {
				  display.setColor(row, col, Color.LIGHT_GRAY);
			  }
			  else
			  {
				  display.setColor(row, col, Color.WHITE);
			  }
		  }
	  }
    
  }

  //Step 5,7
  //called repeatedly.
  //causes one random particle in grid to maybe do something.
  public void step()
  {

	int randRow = (int) (Math.random() * (grid.length - 1));
	int randCol = (int) (Math.random() * grid[0].length);
	int element = grid[randRow][randCol];
	//SAND TURNING TO MUD
	if (element == SAND || element == MUD)
	{
		if(grid[randRow + 1][randCol] == WATER)
		{
			grid[randRow][randCol] = WATER;
			grid[randRow + 1][randCol] = MUD;
		}
	}
	//GRASS FALLING THROUGH WATER
	if (element == GRASS)
	{
		if(grid[randRow + 1][randCol] == WATER)
		{
			grid[randRow][randCol] = WATER;
			grid[randRow + 1][randCol] = GRASS;
		}
	}
	//GRASS GROWING
	if (element == GRASS && randRow != 0 && (grid[randRow - 1][randCol] == EMPTY || grid[randRow - 1][randCol] == WATER))
	{
		if ((int)(Math.random() * 100) == 1)
		{
			if(mudUnderMe(randRow, randCol))
			{
				grid[randRow - 1][randCol] = GRASS;
			}
		}
	}
	//MUD/SAND/GRASS FALLING
	if(element == MUD || element == GRASS || element == SAND)
	{
		if(grid[randRow + 1][randCol] == EMPTY)
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow + 1][randCol] = element;
		}
	}
	
    //WATER FALLING
	if (element == WATER)
	{
		
		int randDirection = (int)(Math.random() * 3);
		
		//left
		if (randDirection == 0 && randCol != 0 && grid[randRow][randCol -1] == EMPTY)
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow][randCol -1] = WATER;
		}
		//right
		else if(randDirection == 1 && randCol + 1 < grid[0].length && grid[randRow][randCol + 1] == EMPTY)
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow][randCol + 1] = WATER;
		}
		//down
		else if(randDirection == 2 && grid[randRow + 1][randCol] == EMPTY)
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow + 1][randCol] = WATER;
		}
		
	}
	//STEAM FLOATING
	if (element == STEAM)
	{
		
		int randDirection = (int)(Math.random() * 3);
		
		//left
		if (randDirection == 0 && randCol != 0 && grid[randRow][randCol -1] == EMPTY)
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow][randCol -1] = STEAM;
		}
		//right
		else if(randDirection == 1 && randCol + 1 < grid[0].length && grid[randRow][randCol + 1] == EMPTY)
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow][randCol + 1] = STEAM;
		}
		//up
		else if(randDirection == 2 && randRow != 0 && grid[randRow - 1][randCol] == EMPTY)
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow - 1][randCol] = STEAM;
		}
		
	}
	//MUD SPREADING
	if(grid[randRow][randCol] == MUD || grid[randRow][randCol] == WATER)
	{
		if(grid[randRow + 1][randCol] == SAND)
		{
			grid[randRow + 1][randCol] = MUD;
		}
	}
    
	
  }
  public boolean mudUnderMe(int startRow, int col)
  {
	  for(int row = startRow; row < grid.length; row++)
	  {
		  if (grid[row][col] == MUD)
		  {
			  grid[row][col] = SAND;
			  return true;
		  }
		  if (grid[row][col] == EMPTY || grid[row][col] == METAL)
		  {
			  return false;
		  }
	  }
	  return false;
  }
  //do not modify this method!
  public void run()
  {
    while (true) // infinite loop
    {
      for (int i = 0; i < display.getSpeed(); i++)
      {
        step();
      }
      updateDisplay();
      display.repaint();
      display.pause(1);  //wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null)  //test if mouse clicked
      {
        locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
      }
    }
  }
}
