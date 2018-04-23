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
  public static final int CLOUD = 6;
  public static final int GRASS = 5;
  public static final int STOVE = 7;
  public static final int FIRE = 8;
  public static final int BOMB = 9;
  public static final int CLEAR = 10;
  
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
    names = new String[11];
    //Each value needs a name for the button
    names[EMPTY] = "Erase";
    names[METAL] = "Metal";
    names[SAND] = "Sand";
    names[WATER] = "Water";
    names[MUD] = "Mud";
    names[GRASS] = "Grass";
    names[CLOUD] = "Cloud";
    names[STOVE] = "Furnace";
    names[FIRE] = "Fire";
    names[BOMB] = "Bomb";
    names[CLEAR] = "CLEAR ALL";
    
    
    //1. Add code to initialize the data member grid with same dimensions
    grid = new int[numRows][numCols];
    for(int row = 0; row < numRows; row++)
    {
    	for(int col = 0; col < numCols; col++)
        {
        	grid[row][col] = EMPTY;
        }
    }
    
    display = new SandDisplay("Sand Game", numRows, numCols, names);
  }
  
  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool)
  {
    //2. Assign the values associated with the parameters to the grid
		  grid[row][col] = tool;
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
			  else if (element == CLOUD)
			  {
				  display.setColor(row, col, Color.LIGHT_GRAY);
			  }
			  else if(element == STOVE)
			  {
				  display.setColor(row, col, new Color(200, 15, 10));
			  }
			  else if(element == FIRE)
			  {
				  int randNumber = (int) (Math.random() * 2);
				  if(randNumber == 0)
				  {
					  display.setColor(row, col, Color.RED);
				  }
				  if(randNumber == 1)
				  {
					  display.setColor(row, col, Color.ORANGE);
				  }
			  }
			  else if(element == BOMB)
			  {
				  display.setColor(row, col, Color.BLACK);
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

	int randRow = (int) (Math.random() * (grid.length));
	int randCol = (int) (Math.random() * grid[0].length);
	int element = grid[randRow][randCol];
	//SAND TURNING TO MUD
	if (element == SAND || element == MUD)
	{
		if(randRow < grid.length - 1 && grid[randRow + 1][randCol] == WATER)
		{
			grid[randRow][randCol] = WATER;
			grid[randRow + 1][randCol] = MUD;
		}
	}
	//MUD SPREADING
	if(grid[randRow][randCol] == MUD || grid[randRow][randCol] == WATER)
	{
		if(randRow < grid.length - 1 && grid[randRow + 1][randCol] == SAND)
		{
			grid[randRow + 1][randCol] = MUD;
		}
	}
	//GRASS FALLING THROUGH WATER
	if (element == GRASS)
	{
		if(randRow < grid.length - 1 && grid[randRow + 1][randCol] == WATER)
		{
			grid[randRow][randCol] = WATER;
			grid[randRow + 1][randCol] = GRASS;
		}
	}
	//GRASS GROWING
	if (element == GRASS && randRow != 0 && (grid[randRow - 1][randCol] == EMPTY || grid[randRow - 1][randCol] == WATER))
	{
		//GROW GRASS
		if ((int)(Math.random() * 100) == 1)
		{
			if(mudUnderMe(randRow, randCol) && (int)(Math.random() * 2) == 1)
			{
				grid[randRow - 1][randCol] = GRASS;
			}
		}
		//HYDRATE SAND
		if(grid[randRow - 1][randCol] == WATER)
		{
			if (hydrateSand(randRow, randCol))
			{
				grid[randRow - 1][randCol] = EMPTY;
			}
		}
	}
	//MUD/SAND/GRASS FALLING
	if(element == MUD || element == GRASS || element == SAND)
	{
		if(randRow < grid.length - 1 && grid[randRow + 1][randCol] == EMPTY)
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow + 1][randCol] = element;
		}
	}
	
    //WATER FALLING OR QUENCHING FIRE
	if (element == WATER)
	{
		
		int randDirection = (int)(Math.random() * 3);
		
		//left
		if (randDirection == 0 && randCol != 0 && (grid[randRow][randCol -1] == EMPTY || grid[randRow][randCol -1] == FIRE))
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow][randCol -1] = WATER;
		}
		//right
		else if(randDirection == 1 && randCol + 1 < grid[0].length && (grid[randRow][randCol + 1] == EMPTY || grid[randRow][randCol + 1] == FIRE))
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow][randCol + 1] = WATER;
		}
		//down
		else if(randDirection == 2 && randRow < grid.length - 1 && (grid[randRow + 1][randCol] == EMPTY || grid[randRow + 1][randCol] == FIRE))
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow + 1][randCol] = WATER;
		}
		
	}
	//CLOUD FLOATING
	if (element == CLOUD)
	{
		
		int randDirection = (int)(Math.random() * 3);
		
		//left
		if (randDirection == 0 && randCol != 0 && grid[randRow][randCol -1] == EMPTY)
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow][randCol -1] = CLOUD;
		}
		//right
		else if(randDirection == 1 && randCol + 1 < grid[0].length && grid[randRow][randCol + 1] == EMPTY)
		{
			grid[randRow][randCol] = EMPTY;
			grid[randRow][randCol + 1] = CLOUD;
		}
		//up
		else if(randDirection == 2 && randRow != 0 && (grid[randRow - 1][randCol] == WATER || grid[randRow - 1][randCol] == EMPTY))
		{
			grid[randRow][randCol] = grid[randRow - 1][randCol];
			grid[randRow - 1][randCol] = CLOUD;
		}
		//CLOUD RAINING
		if(randRow < grid.length - 1 && grid[randRow + 1][randCol] != CLOUD && isPartCloud(randRow, randCol) && (int)(Math.random() * 500) == 1)
		{
			grid[randRow][randCol] = WATER;
		}
	}
	//STOVE STUFF
	if(element == STOVE)
	{
		//BOIL WATER
		if((int)(Math.random() * 250) == 1 && randRow != 0 && grid[randRow - 1][randCol] == WATER)
		{
			grid[randRow - 1][randCol] = CLOUD;
		}
		//BURN GRASS
		int randDirection = (int)(Math.random() * 4);
		
		//left
		if (randDirection == 0 && randCol != 0)
		{
			if(grid[randRow][randCol -1] == GRASS)
			{
				grid[randRow][randCol -1] = FIRE;
			}
		}
		
		//right
		else if(randDirection == 1 && randCol + 1 < grid[0].length)
		{
			if(grid[randRow][randCol + 1] == GRASS)
			{
				grid[randRow][randCol + 1] = FIRE;
			}
		}
		//up
		else if(randDirection == 2 && randRow > 0)
		{
			if(grid[randRow - 1][randCol] == GRASS)
			{
				grid[randRow - 1][randCol] = FIRE;
			}
		}
		//down
		else if(randDirection == 3 && randRow < grid.length - 1)
		{
			if(grid[randRow + 1][randCol] == GRASS)
			{
				grid[randRow + 1][randCol] = FIRE;
			}
		}
	}
	//FIRE STUFF
	if(element == FIRE)
	{
		//BURN GRASS
		boolean grassBurned = false;
		int randDirection = (int)(Math.random() * 4);
		
		//left
		if (randCol != 0)
		{
			if(grid[randRow][randCol - 1] == GRASS)
			{
				grid[randRow][randCol - 1] = FIRE;
				grassBurned = true;
			}
		}
		
		//right
		if(randCol + 1 < grid[0].length)
		{
			if(grid[randRow][randCol + 1] == GRASS)
			{
				grid[randRow][randCol + 1] = FIRE;
				grassBurned = true;
			}
		}
		//up
		if(randRow > 0)
		{
			if(grid[randRow - 1][randCol] == GRASS)
			{
				grid[randRow - 1][randCol] = FIRE;
				grassBurned = true;
			}
		}
		//down
		if(randRow < grid.length - 1)
		{
			if(grid[randRow + 1][randCol] == GRASS)
			{
				grid[randRow + 1][randCol] = FIRE;
				grassBurned = true;
			}
		}
		//DISAPPEAR
		if(grassBurned == false && (int)(Math.random() * 30) == 1)
		{
			grid[randRow][randCol] = EMPTY;
		}
		//MOVE LIKE A CLOUD
		else
		{
			randDirection = (int)(Math.random() * 3);
				
			//left
			if (randDirection == 0 && randCol != 0 && grid[randRow][randCol -1] == EMPTY)
			{
				grid[randRow][randCol] = EMPTY;
				grid[randRow][randCol -1] = FIRE;
			}
			//right
			else if(randDirection == 1 && randCol + 1 < grid[0].length && grid[randRow][randCol + 1] == EMPTY)
			{
				grid[randRow][randCol] = EMPTY;
				grid[randRow][randCol + 1] = FIRE;
			}
			//up
			else if(randDirection == 2 && randRow != 0 && grid[randRow - 1][randCol] == EMPTY)
			{
				grid[randRow][randCol] = grid[randRow - 1][randCol];
				grid[randRow - 1][randCol] = FIRE;
			}
		}
	}
	//BOMB EXPLODES
	if (element == BOMB)
	{
		//left
		if (randCol != 0)
		{
			if(grid[randRow][randCol -1] == FIRE || grid[randRow][randCol -1] == STOVE)
			{
				explode(randRow, randCol);
			}
		}
		//right
		if(randCol + 1 < grid[0].length)
		{
			if(grid[randRow][randCol + 1] == FIRE || grid[randRow][randCol + 1] == STOVE)
			{
				explode(randRow, randCol);
			}
		}
		//up
		if(randRow > 0)
		{
			if(grid[randRow - 1][randCol] == FIRE || grid[randRow - 1][randCol] == STOVE)
			{
				explode(randRow, randCol);
			}
		}
		//down
		if(randRow < grid.length - 1)
		{
			if(grid[randRow + 1][randCol] == FIRE || grid[randRow + 1][randCol] == STOVE)
			{
				explode(randRow, randCol);
			}
		}
	}
  }
  public void explode(int startRow, int startCol)
  {
	  grid[startRow][startCol] = FIRE;
	  for(int col = Math.max(startCol - 6, 0); col < Math.min(startCol + 6, grid[0].length); col++)
	  {
		  for(int row = Math.max(startRow - 6, 0); row < Math.min(startRow + 6, grid.length); row++)
		  {
			  if(grid[row][col] != WATER && grid[row][col] != BOMB)
			  {
				  grid[row][col] = FIRE;
			  }
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
		  if (grid[row][col] == METAL)
		  {
			  return false;
		  }
	  }
	  return false;
  }
  public boolean hydrateSand(int startRow, int col)
  {
	  for(int row = startRow; row < grid.length; row++)
	  {
		  if (grid[row][col] == SAND)
		  {
			  grid[row][col] = MUD;
			  return true;
		  }
		  if (grid[row][col] == METAL)
		  {
			  return false;
		  }
	  }
	  return false;
  }
  public boolean isPartCloud(int startRow, int col)
  {
	  for(int row = startRow; row > 0; row --)
	  {
		  if(grid[row][col] == METAL)
		  {
			  return true;
		  }
		  if(grid[row][col] == EMPTY)
		  {
			  return false;
		  }
	  }
	  return true;
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
      if(display.getTool() == CLEAR)
      {
    	  grid = new int[grid.length][grid[0].length];
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
