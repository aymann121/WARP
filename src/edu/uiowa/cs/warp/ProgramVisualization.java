/**
 * 
 */
package edu.uiowa.cs.warp;

/**
 * ProgamVisualization extends the VisualizationObject and displays the information in this program.
 * It includes a header, a footer, a table for column headers, a title, and a Title.
 * This class displays its data using a GUI (Graphical User interface). To do this it used the GuiVisualization Class.
 * 
 * @author sgoddard
 * @version 1.5
 * 
 */
public class ProgramVisualization extends VisualizationObject {
	
	/**
	 * The file type of the source will be a .dsl
	 */
  private static final String SOURCE_SUFFIX = ".dsl";
  /**
   * The source code with the data used for visualization.
   */
  private ProgramSchedule sourceCode;
  /**
   * The program that will run for the GUI. We analyze this program to identify aspects of the visualization.
   */
  private Program program;
  /**
   * Boolean that displays whether or not all flows meet their deadlines.
   */
  private Boolean deadlinesMet;


  
  /**
   * This constructor initializes the program, sourceCode, and deadlinesMet Instance variables
   * It also calls the constructor of the parent class which initializes its name extension, the file manager and the visualization data to null.
   * 
   * @param warp this takes in a warp object to provide the data for the GUI to display.
  */
  ProgramVisualization(WarpInterface warp) {
    super(new FileManager(), warp, SOURCE_SUFFIX);
    this.program = warp.toProgram();
    this.sourceCode = program.getSchedule();
    this.deadlinesMet = warp.deadlinesMet();
  }
  
  /**
   * @return GuiVisualization this creates and returns a guiVisualization to represent the data using the createTitle, createColumnHeader, and createVisualizationData methods.
   */
  @Override
  public GuiVisualization displayVisualization() {
    return new GuiVisualization(createTitle(), createColumnHeader(), createVisualizationData());
  }

  /**
   * This method creates and returns a Header for the GUI of this application
   * Multiple items are added to the Header:
   * 	The Scheduler Name
   * 	The numFaults (if any exists in the program).
   * 	M (Which is the minimum packet reception rate of the program)
   * 	The E2E
   * 	The nChannels (The number of channels.)
   * 
   * @return header this is the Description object with embedded data about the program for the header of the GUI.
   */
  @Override
  protected Description createHeader() {
    Description header = new Description();

    header.add(createTitle());
    header.add(String.format("Scheduler Name: %s\n", program.getSchedulerName()));

    /* The following parameters are output based on a special schedule or the fault model */
    if (program.getNumFaults() > 0) { // only specify when deterministic fault model is assumed
      header.add(String.format("numFaults: %d\n", program.getNumFaults()));
    }
    header.add(String.format("M: %s\n", String.valueOf(program.getMinPacketReceptionRate())));
    header.add(String.format("E2E: %s\n", String.valueOf(program.getE2e())));
    header.add(String.format("nChannels: %d\n", program.getNumChannels()));
    return header;
  }

  /**
   * This method creates and returns a Footer for the GUI of this application.
   * The method checks if every flow in the program meets their respective deadline. 
   * If so, then a message about all flows meeting their deadline gets added to the footer.
   * If not, the footer contains a warning about flows not meeting their deadline.
   * 
   * @return footer this is a Description object embedded with data about the program for the footer of the GUI.
   */
  @Override
  protected Description createFooter() {
    Description footer = new Description();
    String deadlineMsg = null;

    if (deadlinesMet) {
      deadlineMsg = "All flows meet their deadlines\n";
    } else {
      deadlineMsg = "WARNING: NOT all flows meet their deadlines. See deadline analysis report.\n";
    }
    footer.add(String.format("// %s", deadlineMsg));
    return footer;
  }

  /**
   * This is a method that takes the ordered nodes in the program WorkLoad and adds them all into an array of Strings.
   * This helps to make sense of the Table of Data in createVisualizationData by grouping up information under columns by time slots.
   * 
   * @return columnNames This is a list of strings starting with "Time Slot" with every Node Name in the WorkLoad ordered Alphabetically
   */
  @Override
  protected String[] createColumnHeader() {
    var orderedNodes = program.toWorkLoad().getNodeNamesOrderedAlphabetically();
    String[] columnNames = new String[orderedNodes.length + 1];
    columnNames[0] = "Time Slot"; // add the Time Slot column header first
    /* loop through the node names, adding each to the header */
    for (int i = 0; i < orderedNodes.length; i++) {
      columnNames[i + 1] = orderedNodes[i];
    }
    return columnNames;
  }

  /**
   * This method initializes the visualization data if it has not already been initialized using the sourceCode Object.
   * 
   * @return visualizationData String[][] The visualization data is a 2D array of Strings. The data is copied over information from the source code using a nested for Loop.
   */
  @Override
  protected String[][] createVisualizationData() {
    if (visualizationData == null) {
      int numRows = sourceCode.getNumRows();
      int numColumns = sourceCode.getNumColumns();
      visualizationData = new String[numRows][numColumns + 1];

      for (int row = 0; row < numRows; row++) {
        visualizationData[row][0] = String.format("%s", row);
        for (int column = 0; column < numColumns; column++) {
          visualizationData[row][column + 1] = sourceCode.get(row, column);
        }
      }
    }
    return visualizationData;
  }

  /**
   * @return title this method returns "Warp program for graph " and the name of the program.
   */
  private String createTitle() {
    return String.format("WARP program for graph %s\n", program.getName());
  }
}
