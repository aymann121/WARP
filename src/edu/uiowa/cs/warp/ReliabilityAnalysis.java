package edu.uiowa.cs.warp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import edu.uiowa.cs.warp.WarpDSL.InstructionParameters;


/**
 * ReliabilityAnalysis analyzes the end-to-end reliability of messages transmitted in flows for the
 * WARP system.
 * <p>
 * 
 * Let M represent the Minimum Packet Reception Rate on an edge in a flow. The end-to-end
 * reliability for each flow, flow:src->sink, is computed iteratively as follows:<br>
 * (1)The flow:src node has an initial probability of 1.0 when it is released. All other initial
 * probabilities are 0.0. (That is, the reset of the nodes in the flow have an initial probability
 * value of 0.0.) <br>
 * (2) each src->sink pair probability is computed as NewSinkNodeState = (1-M)*PrevSnkNodeState +
 * M*PrevSrcNodeState <br>
 * This value represents the probability that the message as been received by the node SinkNode.
 * Thus, the NewSinkNodeState probability will increase each time a push or pull is executed with
 * SinkNode as a listener.
 * <p>
 * 
 * The last probability state value for any node is the reliability of the message reaching that
 * node, and the end-to-end reliability of a flow is the value of the last Flow:SinkNode
 * probability.
 * <p>
 * 
 * CS2820 Spring 2023 Project: Implement this class to compute the probabilities the comprise the
 * ReliablityMatrix, which is the core of the file visualization that is requested in Warp.
 * <p>
 * 
 * To do this, you will need to retrieve the program source, parse the instructions for each node,
 * in each time slot, to extract the src and snk nodes in the instruction and then apply the message
 * success probability equation defined above.
 * <p>
 * 
 * I recommend using the getInstructionParameters method of the WarpDSL class to extract the src and
 * snk nodes from the instruction string in a program schedule time slot.
 * 
 * @author sgoddard
 * @version 1.5
 *
 */
public class ReliabilityAnalysis {
	/**
	 * These are the default values for numTx,minimum packet reception rate (M), and end-to-end reliability.
	 */
  private static final Integer DEFAULT_TX_NUM = 0;
  private static final Double DEFAULT_M = 0.9;
  private static final Double DEFAULT_E2E = 0.99;
	/**
	 * This return true if there is a specified number of faults for the flow. We set as false automatically and change to
	 * true later if numFaults is specified.
	 */
  private Boolean numFaultModel = false; 
  /**
   * These are the variables we will use throughout the reliability analysis class, we set minPacketReceptionRate and e2e
   * to the previously specified final variables.
   */
  private Integer numFaults = 0;
  private Double minPacketReceptionRate = DEFAULT_M;
  private Double e2e = DEFAULT_E2E;
  /**
   * Creating variables to access the workLoad and program classes
   */
  private WorkLoad workLoad;
  private Program program;
  /**
   * Creating an empty reliability table and column header to fill with data later
   */
  private ReliabilityTable Reliabilities = new ReliabilityTable();
  private ArrayList<String> columnHeader = new ArrayList<>();
  

  /**
   * Constructor with program as input.
   *
   * @param program WARP program for analysis
   */
  public ReliabilityAnalysis(Program program) {
	  this.program = program;
	  this.workLoad = program.toWorkLoad();
	  this.numFaults = workLoad.getNumFaults();
	  if (numFaults > 0) {
		  numFaultModel = true;
	  } else {
		  numFaultModel = false;
	  }
	  this.minPacketReceptionRate = workLoad.getMinPacketReceptionRate();
	  this.e2e = workLoad.getE2e();
	  buildReliabilityTable();
  }

  /**
   * Constructor for e2e reliability fault model.
   *
   * @param e2e end-to-end reliability to be met by each flow
   * @param minPacketReceptionRate minimum packet reception rate in system
   */
  public ReliabilityAnalysis(Double e2e, Double minPacketReceptionRate) {
    numFaultModel = false;
    this.numFaults = DEFAULT_TX_NUM;
    this.minPacketReceptionRate = minPacketReceptionRate;
    this.e2e = e2e;
  }

  /**
   * Constructor for k-Fault model.
   *
   * @param numFaults number of faults to be tolerated per instance of the flow
   */
  public ReliabilityAnalysis(Integer numFaults) {
    numFaultModel = true;
    this.numFaults = numFaults;
    this.minPacketReceptionRate = DEFAULT_M;
    this.e2e = DEFAULT_E2E;
  }

  /**
   * Gets the number of transmissions required per link and the total transmissions required for the
   * flow, based on the fault model.
   *
   * @param flow information needed to capture the flow from src to snk
   * @return the number of transmissions per link and the total transmissions for the flow
   */
  public ArrayList<Integer> numTxPerLinkAndTotalTxCost(Flow flow) {
    if (numFaultModel) {
      return getFixedTxPerLinkAndTotalTxCost(flow);
    } else {
      return getTxPerLinkAndTotalTxCost(flow);
    }
  }


  /**
   * Verifies flows meet their end-to-end deadlines.
   *
   * @return true if the program meets its 2e2 reliability target, based on the fault model.
   */

  public Boolean verifyReliabilities() {
	  boolean metReliability = true;
	  ReliabilityRow finalRow = getFinalReliabilityRow();
	  for(int i = 0; i < finalRow.size(); i++) {
		  if(finalRow.get(i) < e2e) {
			  System.out.println("The flow did not meet e2e of " + e2e + " "+ columnHeader.get(i) + " with a"
			  		+ " probability of " + finalRow.get(i));
			  metReliability = false;
		  }
	  }
	  return metReliability;
  }
  //should be private but changed to public for testing purposes
  public ReliabilityRow getFinalReliabilityRow(){
	  Integer index = Reliabilities.getNumRows() -1;
	  return Reliabilities.get(index);
  }
  /**
   * This returns the reliability table which will hold
   * the majority of our file and the probability a package is received
   * @return Reliability Table which holds to percentages per node of receiving the message
   */
  public ReliabilityTable getReliabilities() {
		  
    return Reliabilities;
  }
  /**
   * Assigns cost of transmissions to each node and computes the maximum number of 
   * transmissions needed per edge, and puts this information into an arrayList. 
   * @param flow The current flow being evaluated
   * @return an arrrayList containing the maximum number of transmissions per edge. 
   */
//should be private but changed to public for testing purposes
  public ArrayList<Integer> getFixedTxPerLinkAndTotalTxCost(Flow flow) {
    ArrayList<Node> nodesInFlow = flow.nodes;
    int numNodesInFlow = nodesInFlow.size();
    ArrayList<Integer> txArrayList = new ArrayList<Integer>();
    /*
     * Each node will have at most numFaults+1 transmissions. Because we don't know which nodes will
     * send the message over an edge, we give the cost to each node.
     */
    for (int i = 0; i < numNodesInFlow; i++) {
      txArrayList.add(numFaults + 1);
    }
    /*
     * now compute the maximum # of TX, assuming at most numFaults occur on an edge per period, and
     * each edge requires at least one successful TX.
     */
    int numEdgesInFlow = numNodesInFlow - 1;
    int maxFaultsInFlow = numEdgesInFlow * numFaults;
    txArrayList.add(numEdgesInFlow + maxFaultsInFlow);
    return txArrayList;
  }

  /**
   * Computes the number of transmissions required for each link in a flow and the total transmission
   * cost. The transmission cost is calculated based on the number of transmissions and the link
   * capacity.
   * @param flow Flow object containing the nodes representing the path of the flow.
   * @return An ArrayList containing the number of transmissions for each link and the total
	*/
  private ArrayList<Integer> getTxPerLinkAndTotalTxCost(Flow flow) {
	    ArrayList<Node> nodesInFlow = flow.nodes;
	    /* The last entry will contain the worst-case cost of transmitting E2E in isolation */
    int numNodesInFlow = nodesInFlow.size();

    // Array to track numPushes for each node in this
    Integer[] numPushes = new Integer[numNodesInFlow + 1];
    // flow (same as numTx per link)
    Arrays.fill(numPushes, 0); // initialize to all 0 values
    int numHops = numNodesInFlow - 1;
    // minLinkReliablityNeded is the minimum reliability needed per link in a flow to hit E2E
    // reliability for the flow
    /* use max to handle rounding error when e2e == 1.0 */
    Double minLinkReliablityNeded = Math.max(e2e, Math.pow(e2e, (1.0 / (double) numHops)));
    // Now compute reliability of packet reaching each node in the given time slot
    // Start with a 2-D reliability window that is a 2-D matrix of no size
    // each row is a time slot, stating at time 0
    // each column represents the reliability of the packet reaching that node at the
    // current time slot (i.e., the row it is in)
    // will add rows as we compute reliabilities until the final reliability is reached
    // for all nodes.
    Vector<Vector<Double>> reliabilityWindow = new Vector<Vector<Double>>();
    Vector<Double> newReliabilityRow = new Vector<Double>();
    for (int i = 0; i < numNodesInFlow; i++) {
      newReliabilityRow.add(0.0); // create the the row initialized with 0.0 values
    }
    reliabilityWindow.add(newReliabilityRow); // now add row to the reliability window, Time 0
    Vector<Double> tmpVector = reliabilityWindow.get(0);
    Double[] currentReliabilityRow = tmpVector.toArray(new Double[tmpVector.size()]);
    // var currentReliabilityRow = (Double[]) reliabilityWindow.get(0).toArray();
    // Want reliabilityWindow[0][0] = 1.0 (i.e., P(packet@FlowSrc) = 1
    // but I din't want to mess with the newReliablityRow vector I use below
    // So, we initialize this first entry to 1.0, wich is reliabilityWindow[0][0]
    // We will then update this row with computed values for each node and put it
    // back in the matrix
    currentReliabilityRow[0] = 1.0; // initialize (i.e., P(packet@FlowSrc) = 1
    Double e2eReliabilityState = currentReliabilityRow[numNodesInFlow - 1]; // the analysis will end
    // when the e2e
    // reliability matrix is
    // met, initially the
    // state is not met and
    // will be 0 with this
    // statement
    int timeSlot = 0; // start time at 0
    /*
     * change to while and increment increment timeSlot because we don't know how long this schedule
     * window will last
     */
    while (e2eReliabilityState < e2e) {
      Double[] prevReliabilityRow = currentReliabilityRow;
      /* would be reliabilityWindow[timeSlot] if working through a schedule */
      currentReliabilityRow = newReliabilityRow.toArray(new Double[newReliabilityRow.size()]);
      // Now use each flow:src->sink to update reliability computations
      // this is the update formula for the state probabilities
      // nextState = (1 - M) * prevState + M*NextHighestFlowState
      // use MinLQ for M in above equation
      // NewSinkNodeState = (1-M)*PrevSnkNodeState + M*PrevSrcNodeState

      for (int nodeIndex = 0; nodeIndex < (numNodesInFlow - 1); nodeIndex++) { // loop through each
        // node in the flow and
        // update the sates for
        // each link (i.e.,
        // sink->src pair)
        int flowSrcNodeindex = nodeIndex;
        int flowSnkNodeindex = nodeIndex + 1;
        Double prevSrcNodeState = prevReliabilityRow[flowSrcNodeindex];
        Double prevSnkNodeState = prevReliabilityRow[flowSnkNodeindex];
        Double nextSnkState;
        /*
         * Do a push until PrevSnk state > e2e to ensure next node reaches target E2E BUT skip if
         * no chance of success (i.e., source doesn't have packet)
         */
        if ((prevSnkNodeState < minLinkReliablityNeded) && prevSrcNodeState > 0) {
          /* need to continue attempting toTx, so update current state */
          nextSnkState = ((1.0 - minPacketReceptionRate) * prevSnkNodeState)
              + (minPacketReceptionRate * prevSrcNodeState);
          numPushes[nodeIndex] += 1; // increment the number of pushes for for this node to snk node
        } else {
          /*
           * snkNode has met its reliability. Thus move on to the next node and record that the
           * reliability is met.
           */
          nextSnkState = prevSnkNodeState;
        }

        /*
         * probabilities are non-decreasing so update if we were higher by carrying old value
         * forward
         */

        if (currentReliabilityRow[flowSrcNodeindex] < prevReliabilityRow[flowSrcNodeindex]) {
          /*
           * carry forward the previous state for the src node, which may get over written later by
           * another instruction in this slot
           */
          currentReliabilityRow[flowSrcNodeindex] = prevReliabilityRow[flowSrcNodeindex];
        }
        currentReliabilityRow[flowSnkNodeindex] = nextSnkState;
      }

      e2eReliabilityState = currentReliabilityRow[numNodesInFlow - 1];
      Vector<Double> currentReliabilityVector = new Vector<Double>();
      // convert the row to a vector so we can add it to the reliability window
      Collections.addAll(currentReliabilityVector, currentReliabilityRow);
      if (timeSlot < reliabilityWindow.size()) {
        reliabilityWindow.set(timeSlot, (currentReliabilityVector));
      } else {
        reliabilityWindow.add(currentReliabilityVector);
      }
      timeSlot += 1; // increase to next time slot
    }
    int size = reliabilityWindow.size();
    /*
     * The total (worst-case) cost to transmit E2E in isolation with specified reliability target is
     * the number of rows in the reliabilityWindow
     */
    numPushes[numNodesInFlow] = size;
    // Now convert the array to the ArrayList needed to return
    ArrayList<Integer> numPushesArrayList = new ArrayList<Integer>();
    Collections.addAll(numPushesArrayList, numPushes);
    return numPushesArrayList;
    
  }

  /**
   * This method builds our ReliabilityTable. It creates a table with the correct number of 
   * rows and columns and inserts a 1.0 for every source node, and a 0.0 if the node is not
   * a source node. 
   */
  //should be private but public for testing
  public void buildReliabilityTable() {
	  ArrayList<String> flowNames = workLoad.getFlowNamesInPriorityOrder();
	  NodeMap nodeMap = new NodeMap();
	  Integer columnIndex = 0;
	  ArrayList<String> colHeader = new ArrayList<>();
	  ProgramSchedule schedule = program.getSchedule();
	  
	  for(int i = 0; i < flowNames.size(); i++) {
		  String flowName = flowNames.get(i);
		  String[] nodeNames = workLoad.getNodesInFlow(flowName);
		  
		  Integer phase = workLoad.getFlowPhase(flowName);
		  
		  for(int j = 0; j < nodeNames.length; j++) {
			  boolean isSource = false;
			  String header = flowName + ":" + nodeNames[j];
			  colHeader.add(header);
			  if(j == 0) {
				  isSource = true;
			  }
			  
			  nodeMap.put(header, new ReliabilityNode(phase, isSource, columnIndex));
			  columnIndex++;
		  }
	  }
	  setReliabilityHeaderRow(colHeader);
	  ReliabilityTable reliabilities = new ReliabilityTable(schedule.size(), columnIndex);
	  setInitialStateForReleasedFlows(nodeMap, reliabilities);
	  
	  ArrayList<Double> rowArrayList = reliabilities.get(0);
	  Double[] currentReliabilityRow = rowArrayList.toArray(new Double[rowArrayList.size()]);
	  WarpDSL dsl = new WarpDSL();
	  for(int timeSlot = 0; timeSlot < schedule.size(); timeSlot++) {
		  ArrayList<String> rowList = schedule.get(timeSlot);
		  String[] row = rowList.toArray(new String[rowList.size()]);
		  Double[] prevReliabilityRow = currentReliabilityRow;
		  ArrayList<Double> currentRowList = reliabilities.get(timeSlot);
		  currentReliabilityRow = currentRowList.toArray(new Double[currentRowList.size()]);
		  for(String instruction : row) {
			  if(instruction == null) {
				  setReliabilities(reliabilities);
				  return;
			  }
			  ArrayList<InstructionParameters> instructionParamArray = dsl.getInstructionParameters(instruction);
			  
			  for(InstructionParameters entry: instructionParamArray) {
				  String flowName = entry.getFlow();
				  if(!flowName.equals(entry.unused())) {
					  String flowSrcNodeName = flowName + ":" + entry.getSrc();
					  String flowSnkNodeName = flowName + ":" + entry.getSnk();
					  Integer flowSnkNodeindex = nodeMap.get(flowSnkNodeName).columnIndex;
					  Integer flowSrcNodeindex = nodeMap.get(flowSrcNodeName).columnIndex;
					  Double prevSrcNodeState;
					  Integer phase = workLoad.getFlowPhase(flowName);
					  Integer period = workLoad.getFlowPeriod(flowName);
					  
					  phase = phase + ((timeSlot / period) * period);
					  
					  if(timeSlot == phase && nodeMap.get(flowSrcNodeName).isSource) {
						  prevSrcNodeState = 1.0;
						  prevReliabilityRow[flowSrcNodeindex] = 1.0;
						  String[] nodes = workLoad.getNodesInFlow(flowName);
						  for(int i = 1; i < nodes.length; i++) {
							  prevReliabilityRow[flowSrcNodeindex + i] = 0.0;
						  }
					  } else if (timeSlot == phase) {
						  prevSrcNodeState = 0.0;
						  
						  String[] nodes = workLoad.getNodesInFlow(flowName);
						  String flowSrc = flowName + ":" + nodes[0];
						  Integer flowSrcindex = nodeMap.get(flowSrc).columnIndex;
						  prevReliabilityRow[flowSrcindex] = 1.0;
						  String[] nodesInFlow = workLoad.getNodesInFlow(flowName);
						  for(int i = 1; i < nodesInFlow.length; i++) {
							  prevReliabilityRow[flowSrcindex +i] = 0.0;
						  }
					  } else {
						  prevSrcNodeState = prevReliabilityRow[flowSrcNodeindex];
					  }
					  Double prevSnkNodeState = prevReliabilityRow[flowSnkNodeindex];
					  Double nextSnkState = (1-minPacketReceptionRate) * prevSnkNodeState + (minPacketReceptionRate * prevSrcNodeState);
					  currentReliabilityRow[flowSnkNodeindex] = nextSnkState;
				  }
			  }
		  }
		  ReliabilityRow timeSlotArrayList = new ReliabilityRow(currentReliabilityRow);
		  reliabilities.set(timeSlot, timeSlotArrayList);
		  carryForwardReliabilities(timeSlot, nodeMap, reliabilities);
	  }
	  
	  
  }
  /**
   * This allows our table to carry forward the reliability (data for our cell in the table) from
   * the last push, ensuring that the reliability does not decrease from what it previously was. 
   * @param timeSlot the time at the current push (same as the row)
   * @param nodeMap a Map of all the nodes for this flow
   * @param reliabilities, a Reliability table with the reliabilities for each node in the flow
   */
  private void carryForwardReliabilities(Integer timeSlot, NodeMap nodeMap, ReliabilityTable reliabilities) {
	  ArrayList<String> flows = workLoad.getFlowNamesInPriorityOrder();
	  for(String flow : flows) {
		  Integer period = workLoad.getFlowPeriod(flow);
		  Integer phase = workLoad.getFlowPhase(flow);
		  if(timeSlot % period != phase) {
			  String[] nodesInFlow = workLoad.getNodesInFlow(flow);
			  String srcNodeName = flow + ":" + nodesInFlow[0];
			  Integer flowSrcNodeindex = nodeMap.get(srcNodeName).columnIndex;
			  Integer counter = 0;
			  for(String node: nodesInFlow) {
				  Double val1 = reliabilities.get(timeSlot - 1, flowSrcNodeindex + counter);
				  Double val2 = reliabilities.get(timeSlot, flowSrcNodeindex + counter);
				  reliabilities.set(timeSlot, flowSrcNodeindex+ counter, Math.max(val1, val2));
				  counter++;
			  }
		  }
	  }
	  setReliabilities(reliabilities);
  }
  
  /**
   * setInitialStateFOrReleasedFlows takes in our ReliabilityTable which has all values set
   * to 0 and sets the value to 1 if the node is a source node. This initializes our table to produce the
   * correct output. 
   * @param nodeMap the map of all the nodes in each flow. Each has a distinct kay
   * @param reliabilities our ReliabilityTable with all values set to 0
   */
   private void setInitialStateForReleasedFlows(NodeMap nodeMap, ReliabilityTable reliabilities) {
 	  for(Entry<String, ReliabilityNode> entry : nodeMap.entrySet()) {
 		  ReliabilityNode currentNode = entry.getValue();
 		  for(int i = 0; i < reliabilities.getNumRows(); i++) {
 			  if(currentNode.isSource) {
 				  reliabilities.set(i, currentNode.columnIndex, 1.0);;
 			  } 
 		  }
 	  }
   }
  /**
   * This function prints the reliability table in the console with the header and all the reliability table
   * (This function is not called in this class)
   * @param reliabilities the reliability data to populate our table
   * 
   */
  private void printRaTable(ReliabilityTable reliabilities) {
	  System.out.println(columnHeader);
	  int i = Reliabilities.getNumRows();
	  for (int j = 0; j< i; j++) {
		  System.out.println(Reliabilities.get(j));
	  }
	  
	  
  }
  /**
   * The following methods set instances of the specified variables 
   *
   */
  private void setReliabilities(ReliabilityTable rm) {
	  this.Reliabilities = rm;
  }
  
  private void setReliabilityHeaderRow(ArrayList<String> header) {
	  this.columnHeader = header;
  }
  public ArrayList<String> getReliabilityHeaderRow() {
	  return this.columnHeader;
  }
  public ArrayList<String> getColumnHeader(){
	  return this.columnHeader;
  }


  class NodeMap extends HashMap<String,ReliabilityNode>{
	  
  }
  /**
   * Constructor class to generate the reliabilityNode
   */
  private class ReliabilityNode {
	  private boolean isSource;
	  private Integer columnIndex;
	  private Integer phase;
	  
	  /**
	   * Sets the following parameters to the corresponding instance variables.
	   * @param phase the phase for the specified node
	   * @param isSource True if the node is a source node and False otherwise
	   * @param columnIndex The column to place the node in.
	   */
	  
	  ReliabilityNode(Integer phase, boolean isSource, Integer columnIndex){
		  this.isSource = isSource;
		  this.columnIndex = columnIndex;
		  this.phase = phase;
	  }
  }


}

