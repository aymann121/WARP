/**
 * 
 */
package edu.uiowa.cs.warp;

import java.io.File;

/**
 * VisualizationImplementation is a class that handles a lot of the processes of visualizing the data of the Warp application.
 * The class uses information through instance variables and choices to decide what kind of information and how that information will be presented to the GUI.
 * Broadly, it deals with Implementation details for the GUI visualization.
 * @author sgoddard
 * @version 1.5
 */
public class VisualizationImplementation implements Visualization {
	/**
	 * Description of the Visualization
	 */
  private Description visualization;
  	/**
  	 * Description of the content within the specified file. 
  	 */
  private Description fileContent;
  	/**
  	 * window used to see GUI visualization, application window.
  	 */
  private GuiVisualization window;
  /**
   * Name of the specified file that will be written to with with fileContent.
   */
  private String fileName;
  /**
   * The text input that takes in the name of the file that will be written to.
   */
  private String inputFileName;
  /**
   * template of the specified output file.
   */
  private String fileNameTemplate;
  /**
   * File manager used to handle file editing.
   */
  private FileManager fm = null;
  /**
   * Warp interface that contains central information for our program.
   */
  private WarpInterface warp = null;
  /**
   * The workload that organizes the operations of the program.
   */
  private WorkLoad workLoad = null;
  /**
   * The Visualization Object used to initialize the window to see the application.
   */
  private VisualizationObject visualizationObject;


  /**
   * Initializes the objects of this class using instance variables and local variables to change information.
   * This Constructor takes in the warp object, the outputDirectory, and the SystemChoices object
   * Initialized a new FileManager as fm, the warp object, and the String fileNameTemplate as instance variables.
   * 
   * @param warp WarpInterface type, used to initialize warp and create inputFileName variable.
   * @param outputDirectory String type, used to initialize fileNameTemplate using the createFileNameTemplate method.
   * @param choice WorkLoadChoices type, passed to the createVisualization function
   */
  public VisualizationImplementation(WarpInterface warp, String outputDirectory,
      SystemChoices choice) {
    this.fm = new FileManager();
    this.warp = warp;
    inputFileName = warp.toWorkload().getInputFileName();
    this.fileNameTemplate = createFileNameTemplate(outputDirectory);
    visualizationObject = null;
    createVisualization(choice);
  }

  /**
   *Initializes the objects of this class using instance variables and local variables to change information.
   *This constructor takes in the workLoad object and outputDirectory
   *Initializes a new FileManager as fm, the workLoad object and the string FileNameTemplate as instance variables.
   *
   * @param workLoad WorkLoad type, used to initialize workLoad and create inputFileName variable.
   * @param outputDirectory String type, used to initialize fileNameTemplate using the createFileNameTemplate method.
   * @param choice WorkLoadChoices type, passed to the createVisualization function
   */
  public VisualizationImplementation(WorkLoad workLoad, String outputDirectory,
      WorkLoadChoices choice) {
    this.fm = new FileManager();
    this.workLoad = workLoad;
    inputFileName = workLoad.getInputFileName();
    this.fileNameTemplate = createFileNameTemplate(outputDirectory);
    visualizationObject = null;
    createVisualization(choice);
  }

  /**
   * Makes program window visible using the VisualizationObject
   */
  @Override
  public void toDisplay() {
    // System.out.println(displayContent.toString());
    window = visualizationObject.displayVisualization();
    if (window != null) {
      window.setVisible();
    }
  }

  /**
   * Writes to the classes File Manager using the classes fileName and fileContent instance variables.
   */
  @Override
  public void toFile() {
    fm.writeFile(fileName, fileContent.toString());
  }

  /**
   * @return returns the String representation of the Visualization Object
   */
  @Override
  public String toString() {
    return visualization.toString();
  }

  /**
   * This method is a switch statement based on what is selected for the choice object.
   * The expected choices consist of Source, Reliabilities, Simulator_Input, Latency, Channel, Latency_Report, and Deadline_Report.
   * Every option runs the createVisualization method with the correct inputs.
   * 
   * @param choice Type of SystemChoices. The parameter that is looked at for the switch statement.
   */
  private void createVisualization(SystemChoices choice) {
    switch (choice) { // select the requested visualization
      case SOURCE:
        createVisualization(new ProgramVisualization(warp));
        break;

      case RELIABILITIES:
        // TODO Implement Reliability Analysis Visualization
        createVisualization(new ReliabilityVisualization(warp));
        break;

      case SIMULATOR_INPUT:
        // TODO Implement Simulator Input Visualization
        createVisualization(new NotImplentedVisualization("SimInputNotImplemented"));
        break;

      case LATENCY:
        // TODO Implement Latency Analysis Visualization
        createVisualization(new LatencyVisualization(warp));
        break;

      case CHANNEL:
        // TODO Implement Channel Analysis Visualization
        createVisualization(new ChannelVisualization(warp));
        break;

      case LATENCY_REPORT:
        createVisualization(new ReportVisualization(fm, warp,
            new LatencyAnalysis(warp).latencyReport(), "Latency"));
        break;

      case DEADLINE_REPORT:
        createVisualization(
            new ReportVisualization(fm, warp, warp.toProgram().deadlineMisses(), "DeadlineMisses"));
        break;

      default:
        createVisualization(new NotImplentedVisualization("UnexpectedChoice"));
        break;
    }
  }
  /**
   * Implementation of createVisualization that takes in a choice with a switch statement.
   * If the choice is COMUNCATION_GRAPH, GRAPHVIZ, OR INPUT_GRAPH, than the version of createVisualitation with a General Type gets called with their respective paramater type.
   * If choice is none of those, createVisualization gets called with a NotImplentedVisualization Type.
   * 
   * @param choice Type of WorkLoadChoices. The parameter that is looked at for the switch statement.
   */
  private void createVisualization(WorkLoadChoices choice) {
    switch (choice) { // select the requested visualization
      case COMUNICATION_GRAPH:
        // createWarpVisualization();
        createVisualization(new CommunicationGraph(fm, workLoad));
        break;

      case GRAPHVIZ:
        createVisualization(new GraphViz(fm, workLoad.toString()));
        break;

      case INPUT_GRAPH:
        createVisualization(workLoad);
        break;

      default:
        createVisualization(new NotImplentedVisualization("UnexpectedChoice"));
        break;
    }
  }
  /**
   * Changes instance variables to display the information of a new object
   * Changes the visualization, fileContent, fileName, and visualizationObject instance methods.
   * 
   * @param obj object of generic type <T extends VisualizationObject> with information to change the visualization displayed.
   */
  private <T extends VisualizationObject> void createVisualization(T obj) {
    visualization = obj.visualization();
    fileContent = obj.fileVisualization();
    /* display is file content printed to console */
    fileName = obj.createFile(fileNameTemplate); // in output directory
    visualizationObject = obj;
  }

  /**
   * Returns a variable called fileNameTemplate
   * fileNameTemplate consists of a newDirectory (which is a directory made from the file Manager base directory and the output directory),
   * the separator of the File instanceVariable, and the name of the inputFile.  
   * 
   * @param outputDirectory (String) used to create the newDirectory with the file manager.
   * @return String The new FileNameTemplate
   */
  private String createFileNameTemplate(String outputDirectory) {
    String fileNameTemplate;
    var workingDirectory = fm.getBaseDirectory();
    var newDirectory = fm.createDirectory(workingDirectory, outputDirectory);
    // Now create the fileNameTemplate using full output path and input filename
    if (inputFileName.contains("/")) {
      var index = inputFileName.lastIndexOf("/") + 1;
      fileNameTemplate = newDirectory + File.separator + inputFileName.substring(index);
    } else {
      fileNameTemplate = newDirectory + File.separator + inputFileName;
    }
    return fileNameTemplate;
  }

}
