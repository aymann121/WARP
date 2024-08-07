/**
 * WARP: On-the-fly Program Synthesis for Agile, Real-time, and Reliable Wireless Networks. This
 * system generates node communication programs WARP uses programs to specify a network’s behavior
 * and includes a synthesis procedure to automatically generate such programs from a high-level
 * specification of the system’s workload and topology. WARP has three unique features: <br>
 * (1) WARP uses a domain-specific language to specify stateful programs that include conditional
 * statements to control when a flow’s packets are transmitted. The execution paths of programs
 * depend on the pattern of packet losses observed at run-time, thereby enabling WARP to readily
 * adapt to packet losses due to short-term variations in link quality. <br>
 * (2) Our synthesis technique uses heuristics to improve network performance by considering
 * multiple packet loss patterns and associated execution paths when determining the transmissions
 * performed by nodes. Furthermore, the generated programs ensure that the likelihood of a flow
 * delivering its packets by its deadline exceeds a user-specified threshold. <br>
 * (3) WARP can adapt to workload and topology changes without explicitly reconstructing a network’s
 * program based on the observation that nodes can independently synthesize the same program when
 * they share the same workload and topology information. Simulations show that WARP improves
 * network throughput for data collection, dissemination, and mixed workloads on two realistic
 * topologies. Testbed experiments show that WARP reduces the time to add new flows by 5 times over
 * a state-of-the-art centralized control plane and guarantees the real-time and reliability of all
 * flows.
 */

package edu.uiowa.cs.warp;

import argparser.ArgParser;
import argparser.BooleanHolder;
import argparser.DoubleHolder;
import argparser.IntHolder;
import argparser.StringHolder;
import edu.uiowa.cs.warp.SystemAttributes.ScheduleChoices;
import edu.uiowa.cs.warp.Visualization.SystemChoices;
import edu.uiowa.cs.warp.Visualization.WorkLoadChoices;



/**
 * * WARP summarizes communication between nodes to show the flow path of information, the cost,
 * reliability, and efficiency of this communication.
 * Will output the specific configurations of the current file and a visual representation
 * of the path (flow) which the information is being passed through the nodes. This program
 * will determine if the information is received before the stated time and check for any 
 * conflicts between node channels. 
 * @author sgoddard
 * @version 1.5
 *
 */
public class Warp {
	/**
	 * Number of channels available to schedule for the flow.
	 */
  private static final Integer NUM_CHANNELS = 16; 
  /**
   * The lowest link transmission quality acceptable. 
   */
  private static final Double MIN_LQ = 0.9; 
  																						
  /**
   * If not specified, this is the default end to end reliability for all flows.                                    
   */
  private static final Double E2E = 0.99; 
                                          
  /**
   * Sub-directory for the default outputs.
   */
  private static final String DEFAULT_OUTPUT_SUB_DIRECTORY = "OutputFiles/";
  /**
   * Default scheduler of information passed between the flows is Priority.
   */
  private static final ScheduleChoices DEFAULT_SCHEDULER = ScheduleChoices.PRIORITY;
  /**
   * The default number of faults to be tolerated per transmission.
   */
  private static final Integer DEFAULT_FAULTS_TOLERATED = 0;
  
  /**
   * The total number of wireless channels available for scheduling.
   */
  private static Integer nChannels; 
  /**
   * The allowable number of faults between two nodes.
   */
  private static Integer numFaults; 
  /**
   * A variable for the minimum link quality for the system. 
   */
  private static Double minLQ; 
  /**
   * A variable for the end to end reliability for the system.
   */
                               
  private static Double e2e; 
                            
  /**
   * Creates a sub-director for the output files to be stored. 
   */
  private static String outputSubDirectory; 
                                            
  /**
   * A value to denote if the Gui Visualization was requested.
   */
  private static Boolean guiRequested; 
  /**
   * A value to denote if the GraphVis file was requested.
   */
  private static Boolean gvRequested; 
  /**
   * A value to denote if the warp file was requested.
   */
  private static Boolean wfRequested; 
  /**
   * A value to denote if a Reliability Analysis was requested.
   */
  private static Boolean raRequested; 
  /**
   *  A value to denote if a Latency Analysis was requested.
   */
  private static Boolean laRequested; 
  /**
   * A value to denote if a Channel Analysis was requested.
   */
  private static Boolean caRequested; 
  /**
   * A value to denote if a Simulation file was requested.
   */
  private static Boolean simRequested; 
  /**
   * A value to denote if all additional files were requested.
   */
  private static Boolean allRequested; 
  /**
   * A value to denote if a latency report was requested.
   */
  private static Boolean latencyRequested; 
  /**
   * A value to denote if a scheduler was requested, defaults to false. 
   */
  private static Boolean schedulerRequested = false;
  /**
   * A value to denote if verbose mode was selected.
   */
  private static Boolean verboseMode; 
  /**
   * The input file to pass in the graph workload.
   */
  private static String inputFile; 
  /**
   * The type of scheduler selected.
   */
  private static ScheduleChoices schedulerSelected; 

  /**
   * Identify which parameters to be used based on the command-line options selected. If verbose
   * mode is selected these options will be printed.
   * @param args The chosen WARP parameters selected to be included for this file. 
   */
  public static void main(String[] args) {
    
    setWarpParameters(args);

    
    if (verboseMode) {
      printWarpParameters();
    }

    /**
     * Creates the workload based on the specifications and visualize the system. Will create table/graph
     * visualizations for the selected parameters. 
     * @param numFaults The number of faults to be allowed per edge during the flow path. 
     * @param minLQ The chosen link quality.
     * @param e2e The end-to-end reliability, set by default to 99% (.99).
     * @param inputFile The chosen file name as a string.
     */

    WorkLoad workLoad = new WorkLoad(numFaults, minLQ, e2e, inputFile);
    if (allRequested) {
      for (WorkLoadChoices choice : WorkLoadChoices.values()) {
        visualize(workLoad, choice); // visualize all Program choices
      }
      // Create and visualize the Warp System
      if (schedulerRequested) {
        WarpInterface warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
        verifyPerformanceRequirements(warp);
        for (SystemChoices choice : SystemChoices.values()) {
          visualize(warp, choice); // visualize all System choices
        }
      } else { // create a system for all scheduler choices
        for (ScheduleChoices sch : ScheduleChoices.values()) {
          schedulerSelected = sch;
          WarpInterface warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
          verifyPerformanceRequirements(warp);
          for (SystemChoices choice : SystemChoices.values()) {
            visualize(warp, choice); // visualize all System choices
          }
        }
      }
    } else { // visualize warp workload, source program and other requested items
      visualize(workLoad, WorkLoadChoices.INPUT_GRAPH);
      if (wfRequested) {
        visualize(workLoad, WorkLoadChoices.COMUNICATION_GRAPH);
      }
      if (gvRequested) {
        visualize(workLoad, WorkLoadChoices.GRAPHVIZ);
      }
      WarpInterface warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
      verifyPerformanceRequirements(warp);
      visualize(warp, SystemChoices.SOURCE);
      if (caRequested) {
        visualize(warp, SystemChoices.CHANNEL);
      }
      if (laRequested) {
        visualize(warp, SystemChoices.LATENCY);
      }
      if (latencyRequested || laRequested) {
        visualize(warp, SystemChoices.LATENCY_REPORT);
      }
      if (raRequested) {
        visualize(warp, SystemChoices.RELIABILITIES);
      }
    }

  }
  
  /**
   * Creates a visualization of the input file data, the flow pattern, and the communication costs
   * based on the options chosen. 
   * @param workLoad Determined from the input file based on the specified number of faults, 
   * end to end reliability, and link quality. 
   * @param choice The chosen options for the visualization of the input graph, the communication 
   * graph, and the graph of visited nodes. 
   */

  private static void visualize(WorkLoad workLoad, WorkLoadChoices choice) {
    var viz =
        VisualizationFactory.createWorkLoadVisualization(workLoad, outputSubDirectory, choice);
    if (viz != null) {
      if (verboseMode) {
        System.out.println(viz.toString());
      }
      viz.toFile();
      if (guiRequested) {
        viz.toDisplay();
      }
    }
  }

  /**
   * Creates a visual spreadsheet of the flow path data based on the choices of reports and analyses
   * to be included.
   * @param warp The current warp data, created based on the chosen parameters.
   * @param choice The information to be included on the warp visualization graph based on the 
   * chosen parameters for the reports and analyses desired to be displayed. 
   */

  private static void visualize(WarpInterface warp, SystemChoices choice) {
    var viz = VisualizationFactory.createProgramVisualization(warp, outputSubDirectory, choice);
    if (viz != null) {
      viz.toFile();
      if (guiRequested && schedulerRequested) {
        /* Only display window when a specific scheduler has been requested */
        viz.toDisplay();
      }
    }
  }

  /**
   * Checks if the specified deadlines were met, if the reliability met the default .99 and reports if 
   * any conflicts were detected between channels. 
   * @param warp The current warp system, created based on the chosen parameters.
   */

  private static void verifyPerformanceRequirements(WarpInterface warp) {
    verifyDeadlines(warp);
    verifyReliabilities(warp);
    verifyNoChannelConflicts(warp);
  }

  /**
   * Checks to see if the reliability requirement was met after the flow has been completed. 
   * Will output a message stating whether or not the flows met the end-to-end reliability desired
   * under the type of scheduling specified.
   * @param warp The current warp system, created based on the chosen parameters.
   */

  private static void verifyReliabilities(WarpInterface warp) {
    if (schedulerSelected != ScheduleChoices.RTHART) {
      /* RealTime HART doesn't adhere to reliability targets */
      if (!warp.reliabilitiesMet()) {
        System.err.printf(
            "\n\tERROR: Not all flows meet the end-to-end "
                + "reliability of %s under %s scheduling.\n",
            String.valueOf(e2e), schedulerSelected.toString());
      } else if (verboseMode) {
        System.out.printf(
            "\n\tAll flows meet the end-to-end reliability " + "of %s under %s scheduling.\n",
            String.valueOf(e2e), schedulerSelected.toString());
      }
    }
  }
  /**
   * Checks if all flows met their deadlines on the the specified type of scheduling. 
   * Will output a message stating whether or not the deadlines were met and what chosen type of
   *  scheduling this was checked against.
   * @param warp The current warp system, created based on the chosen parameters.
   */


  private static void verifyDeadlines(WarpInterface warp) {
    if (!warp.deadlinesMet()) {
      System.err.printf("\n\tERROR: Not all flows meet their deadlines under %s scheduling.\n",
          schedulerSelected.toString());
      visualize(warp, SystemChoices.DEADLINE_REPORT);
    } else if (verboseMode) {
      System.out.printf("\n\tAll flows meet their deadlines under %s scheduling.\n",
          schedulerSelected.toString());
    }
  }
  /**
   * Checks if there were any conflicts between the flows as the communication traveled through.
   * If conflicts occured, and error message is displayed and (if not previously created based on
   * selected parameters) a visualization will be created to display details of where this conflict
   * occurred. 
   * @param warp The current warp system, created based on the chosen parameters.
   */


  private static void verifyNoChannelConflicts(WarpInterface warp) {
    if (warp.toChannelAnalysis().isChannelConflict()) {
      System.err
          .printf("\n\tERROR: Channel conficts exists. See Channel Visualization for details.\n");
      if (!caRequested) { // only need to create the visualization if not already requested
        visualize(warp, SystemChoices.CHANNEL);
      }
    } else if (verboseMode) {
      System.out.printf("\n\tNo channel conflicts detected.\n");
    }
  }

  /**
   * Creates objects to store all of the parameters needed to determine what will be inspected 
   * and displayed in this warp system. 
   * @param args The specified parameters for the warp configuration.
   */
  


  private static void setWarpParameters(String[] args) { // move command line parsing into this
                                                         // function--need to set up globals?

    // create holder objects for storing results ...
    // BooleanHolder debug = new BooleanHolder();
    StringHolder schedulerSelected = new StringHolder();
    IntHolder channels = new IntHolder();
    IntHolder faults = new IntHolder();
    DoubleHolder m = new DoubleHolder();
    DoubleHolder end2end = new DoubleHolder();
    BooleanHolder gui = new BooleanHolder();
    BooleanHolder gv = new BooleanHolder();
    BooleanHolder wf = new BooleanHolder();
    BooleanHolder ra = new BooleanHolder();
    BooleanHolder la = new BooleanHolder();
    BooleanHolder ca = new BooleanHolder();
    BooleanHolder s = new BooleanHolder();
    BooleanHolder all = new BooleanHolder();
    BooleanHolder latency = new BooleanHolder();
    BooleanHolder verbose = new BooleanHolder();
    StringHolder input = new StringHolder();
    StringHolder output = new StringHolder();
    
    /**
     * Creates a parser to communicate the range/specifications for each attribute so the user
     * gives valid input that the code can use.
     */

    ArgParser parser = new ArgParser("java -jar warp.jar");
    parser.addOption("-sch, --schedule %s {priority,rm,dm,rtHart,poset} #scheduler options",
        schedulerSelected);
    parser.addOption("-c, --channels %d {[1,16]} #number of wireless channels", channels);
    parser.addOption("-m %f {[0.5,1.0]} #minimum link quality in the system", m);
    parser.addOption(
        "-e, --e2e %f {[0.5,1.0]} #global end-to-end communcation reliability for all flows",
        end2end);
    parser.addOption("-f, --faults %d {[1,10]} #number of faults per edge in a flow (per period)",
        faults);
    parser.addOption("-gui %v #create a gui visualizations", gui);
    parser.addOption("-gv %v #create a graph visualization (.gv) file for GraphViz", gv);
    parser.addOption(
        "-wf  %v #create a WARP (.wf) file that shows the maximum number of transmissions on each segment of the flow needed to meet the end-to-end reliability",
        wf);
    parser.addOption(
        "-ra  %v #create a reliability analysis file (tab delimited .csv) for the warp program",
        ra);
    parser.addOption(
        "-la  %v #create a latency analysis file (tab delimited .csv) for the warp program", la);
    parser.addOption(
        "-ca  %v #create a channel analysis file (tab delimited .csv) for the warp program", ca);
    parser.addOption("-s  %v #create a simulator input file (.txt) for the warp program", s);
    parser.addOption("-a, --all  %v #create all output files (activates -gv, -wf, -ra, -s)", all);
    parser.addOption("-l, --latency  %v #generates end-to-end latency report file (.txt)", latency);
    parser.addOption("-i, --input %s #<InputFile> of graph flows (workload)", input);
    parser.addOption("-o, --output %s #<OutputDIRECTORY> where output files will be placed",
        output);
    parser.addOption(
        "-v, --verbose %v #Echo input file name and parsed contents. Then for each flow instance: show maximum E2E latency and min/max communication cost for that instance of the flow",
        verbose);
    // parser.addOption ("-d, -debug, --debug %v #Debug mode: base directory =
    // $HOME/Documents/WARP/", debug);


    /**
     * Checks to verify that all of the specified values are valid inputs. If the input is valid, 
     * then this is assigned to the attribute. If the input is not valid, then the attribute will 
     * be set to the specified default value to ensure that the program will run. 
     */

    parser.matchAllArgs(args);

    // Set WARP system configuration options
    if (channels.value > 0) {
      nChannels = channels.value; // set option specified
    } else {
      nChannels = NUM_CHANNELS; // set to default
    }
    if (faults.value > 0) { // global variable for # of Faults tolerated per edge
      numFaults = faults.value; // set option specified
    } else {
      numFaults = DEFAULT_FAULTS_TOLERATED; // set to default
    }
    if (m.value > 0.0) { // global variable for minimum Link Quality in system
      minLQ = m.value; // set option specified
    } else {
      minLQ = MIN_LQ; // set to default
    }
    if (end2end.value > 0.0) { // global variable for minimum Link Quality in system
      e2e = end2end.value; // set option specified
    } else {
      e2e = E2E; // set to default
    }
    if (output.value != null) { // default output subdirectory (from working directory)
      outputSubDirectory = output.value; // set option specified
    } else {
      outputSubDirectory = DEFAULT_OUTPUT_SUB_DIRECTORY; // set to default
    }

    /**
     * Assigns the requested parameters to a variable to be used when creating the warp code 
     * requirements and visualizations.
     */

    guiRequested = gui.value; // GraphVis file requested flag
    gvRequested = gv.value; // GraphVis file requested flag
    wfRequested = wf.value; // WARP file requested flag
    raRequested = ra.value; // Reliability Analysis file requested flag
    laRequested = la.value; // Latency Analysis file requested flag
    caRequested = ca.value; // Latency Analysis file requested flag
    simRequested = s.value; // Simulation file requested flag
    allRequested = all.value; // all out files requested flag
    latencyRequested = latency.value; // latency report requested flag
    verboseMode = verbose.value; // verbose mode flag (mainly for running in IDE)
    // debugMode = debug.value; // debug mode flag (mainly for running in IDE)
    inputFile = input.value; // input file specified
    if (schedulerSelected.value != null) { // can't switch on a null value so check then switch
      schedulerRequested = true;
      switch (schedulerSelected.value) {
        case "priority":
          Warp.schedulerSelected = ScheduleChoices.PRIORITY;
          break;

        case "rm":
          Warp.schedulerSelected = ScheduleChoices.RM;
          break;

        case "dm":
          Warp.schedulerSelected = ScheduleChoices.DM;
          break;

        case "rtHart":
          Warp.schedulerSelected = ScheduleChoices.RTHART;
          break;

        case "poset":
          Warp.schedulerSelected = ScheduleChoices.POSET_PRIORITY;
          break;

        default:
          Warp.schedulerSelected = ScheduleChoices.PRIORITY;
          break;
      }
    } else { // null value when no scheduler specified; so use default
      Warp.schedulerSelected = DEFAULT_SCHEDULER;
    }
  }
  
  /**
   * Prints out the parameters for current configuration to display what values of the parameters
   * were requested (or if these requests were not valid inputs, it will show what the parameter 
   * defaulted to).
   */

  private static void printWarpParameters() { // print all system configuration parameters
    // Print out each of the system configuration values
    System.out.println("WARP system configuration values:");
    System.out.println("\tScheduler=" + schedulerSelected);
    System.out.println("\tnChanels=" + nChannels);
    System.out.println("\tnumFaults=" + numFaults);
    System.out.println("\tminLQ=" + minLQ);
    System.out.println("\tE2E=" + e2e);
    System.out.println("\tguiRequest flag=" + guiRequested);
    System.out.println("\tgvRequest flag=" + gvRequested);
    System.out.println("\twfRequest flag=" + wfRequested);
    System.out.println("\traRequest flag=" + raRequested);
    System.out.println("\tlaRequest flag=" + laRequested);
    System.out.println("\tcaRequest flag=" + caRequested);
    System.out.println("\tsimRequest flag=" + simRequested);
    System.out.println("\tallOutFilesRequest flag=" + allRequested);
    System.out.println("\tlatency flag=" + latencyRequested);
    if (inputFile != null) {
      System.out.println("\tinput file=" + inputFile);
    } else {
      System.out.println("\tNo input file specified; will be requested when needed.");
    }
    System.out.println("\toutputSubDirectory=" + outputSubDirectory);
    System.out.println("\tverbose flag=" + verboseMode);
    // System.out.println ("\tdebug flag=" + debugMode);
  }

}
