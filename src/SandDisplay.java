import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class SandDisplay extends JComponent implements MouseListener,
  MouseMotionListener, ActionListener, ChangeListener
{
  private Image image;
  private int cellSize;
  private JFrame frame;
  private int tool;
  private int numRows;
  private int numCols;
  private int[] mouseLoc;
  private JButton[] buttons;
  private JSlider slider;
  private int speed;
  private String[] buttonNames;
  private JPanel buttonPanel;
  
  public SandDisplay(String title, int numRows, int numCols, String[] names)
  {
    this.numRows = numRows;
    this.numCols = numCols;
    tool = 1;
    mouseLoc = null;
    speed = computeSpeed(5);
    
    //determine cell size
    cellSize = Math.max(1, 600 / Math.max(numRows, numCols));
    image = new BufferedImage(numCols * cellSize, numRows * cellSize, BufferedImage.TYPE_INT_RGB);
    
    frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
    frame.setResizable(false);
    
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
    frame.getContentPane().add(topPanel);
    
    setPreferredSize(new Dimension(numCols * cellSize, numRows * cellSize));
    addMouseListener(this);
    addMouseMotionListener(this);
    topPanel.add(this);
    
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
    topPanel.add(buttonPanel);
    
    buttonNames = names;
    buttons = new JButton[buttonNames.length];
    
    for (int i = 0; i < buttons.length; i++)
    {
      buttons[i] = new JButton(buttonNames[i]);
      buttons[i].setActionCommand("" + i);
      buttons[i].addActionListener(this);
      buttonPanel.add(buttons[i]);
    }
    
    buttons[tool].setSelected(true);
   
    slider = new JSlider(JSlider.HORIZONTAL, 0, 25, 5);
    slider.addChangeListener(this);
    slider.setMajorTickSpacing(5);
    slider.setPaintTicks(true);
    Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
    labelTable.put(0, new JLabel("SLOW"));
    labelTable.put(25, new JLabel("FAST"));
    slider.setLabelTable(labelTable);
    slider.setPaintLabels(true);

    frame.getContentPane().add(slider);

    frame.pack();
    frame.setVisible(true);
  }
  
  public void paintComponent(Graphics g)
  {
    g.drawImage(image, 0, 0, null);
  }
  
  public void pause(int milliseconds)
  {
    try
    {
      Thread.sleep(milliseconds);
    }
    catch(InterruptedException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public int[] getMouseLocation()
  {
    return mouseLoc;
  }
  
  public int getTool()
  {
    return tool;
  }
  
  public void setColor(int row, int col, Color color)
  {
    Graphics g = image.getGraphics();
    g.setColor(color);
    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
  }
 
  
  public void mouseClicked(MouseEvent e)
  {
  }
  
  public void mousePressed(MouseEvent e)
  {
    mouseLoc = toLocation(e);
  }
  
  public void mouseReleased(MouseEvent e)
  {
    mouseLoc = null;
  }
  
  public void mouseEntered(MouseEvent e)
  {
  }
  
  public void mouseExited(MouseEvent e)
  {
  }
  
  public void mouseMoved(MouseEvent e)
  {
  }
  
  public void mouseDragged(MouseEvent e)
  {
    mouseLoc = toLocation(e);
  }
  
  private int[] toLocation(MouseEvent e)
  {
    int row = e.getY() / cellSize;
    int col = e.getX() / cellSize;
    if (row < 0 || row >= numRows || col < 0 || col >= numCols)
      return null;
    int[] loc = new int[2];
    loc[0] = row;
    loc[1] = col;
    return loc;
  }
  
  public void actionPerformed(ActionEvent e)
  {
    tool = Integer.parseInt(e.getActionCommand());
    for (JButton button : buttons)
      button.setSelected(false);
    ((JButton)e.getSource()).setSelected(true);
  }
  
  public void stateChanged(ChangeEvent e)
  {
    speed = computeSpeed(slider.getValue());
  }
  
  //returns number of times to step between repainting and processing mouse input 
  public int getSpeed()
  {
    return speed;
  }
  
  //returns speed based on sliderValue
  //speed of 0 returns 10^3
  //speed of 100 returns 10^6
  private int computeSpeed(int sliderValue)
  {
	if(sliderValue == 0)
	{
		return 0;
	}
    return (int)Math.pow(10, 0.05 * sliderValue + 3);
  }
  public void addButton(int element, String name)
  {
	  buttonNames[element] = name;
	  for(int i = 0; i < buttons.length; i++)
	  {
		  buttonPanel.remove(buttons[i]);
	  }
	    
	  for (int i = 0; i < buttons.length; i++)
	  {
		  buttons[i] = new JButton(buttonNames[i]);
		  buttons[i].setActionCommand("" + i);
		  buttons[i].addActionListener(this);
		  buttonPanel.add(buttons[i]);
	  }
	  buttonPanel.validate();
  }
}