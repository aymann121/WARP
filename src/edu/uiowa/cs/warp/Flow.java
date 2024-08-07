package edu.uiowa.cs.warp;

import java.util.ArrayList;

/** Flow is a constructor that creates setters and getters for the objects that will 
 * later be called in Warp to determine the flow path of data. In addition to creating the 
 * arrays to store the nodes and edges, this class also created variables to determine if
 * the current flow met its specified end to end cost.
 * 
 * @author sgoddard
 * 
 *
 */
public class Flow extends SchedulableObject implements Comparable<Flow>{

	private static final Integer UNDEFINED = -1;
	private static final Integer DEFAULT_FAULTS_TOLERATED = 0; 
	private static final Integer DEFAULT_INDEX = 0;
	private static final Integer DEFAULT_PERIOD = 100; 
	private static final Integer DEFAULT_DEADLINE = 100;
	private static final Integer DEFAULT_PHASE = 0;
	

    Integer initialPriority = UNDEFINED;
    Integer index;  // order in which the node was read from the Graph file
    Integer numTxPerLink; //  determined by fault model
    ArrayList<Node> nodes; // Flow src is 1st element and flow snk is last element in array
    /*
     *  nTx needed for each link to reach E2E reliability target. Indexed by src node of the link. 
     *  Last entry is total worst-case E2E Tx cost for schedulability analysis
     */
    ArrayList<Integer> linkTxAndTotalCost; 
    ArrayList<Edge> edges; //used in Partition and scheduling
    Node nodePredecessor;
    Edge edgePredecessor;
    
    /*
     * Constructor that sets name, priority, and index for the flow.
     * @param name The file name
     * @param priority The specified priority type for the flow.
     * @param index The position of the node in the flow.
     */
    Flow (String name, Integer priority, Integer index){
    	super(name, priority, DEFAULT_PERIOD, DEFAULT_DEADLINE, DEFAULT_PHASE);
    	this.index = index;
        /*
         *  Default numTxPerLink is 1 transmission per link. Will be updated based
         *  on flow updated based on flow length and reliability parameters
         */
        this.numTxPerLink = DEFAULT_FAULTS_TOLERATED + 1; 
        this.nodes = new ArrayList<>();
        this.edges  = new ArrayList<>();
        this.linkTxAndTotalCost = new ArrayList<>();
        this.edges = new ArrayList<>();	
        this.nodePredecessor = null;
        this.edgePredecessor = null;
    }
    
    /*
     * Constructor that creates the flow based on based on the number of nodes and
     * the selected parameters. 
     */
    Flow () {
    	super();
    	this.index = DEFAULT_INDEX;
    	/*
    	 *  Default numTxPerLink is 1 transmission per link. Will be updated based
    	 *  on flow updated based on flow length and reliability parameters
    	 */
    	this.numTxPerLink = DEFAULT_FAULTS_TOLERATED + 1; 
    	this.nodes = new ArrayList<>();
    	this.linkTxAndTotalCost = new ArrayList<>();
    	this.edges = new ArrayList<>();
    	this.nodePredecessor = null;
        this.edgePredecessor = null;
    }

	/**
	 * Gets the initial Priority
	 * @return the initial Priority
	 */
	public Integer getInitialPriority() {
		return initialPriority;
	}

	/**
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @return the numTxPerLink
	 */
	public Integer getNumTxPerLink() {
		return numTxPerLink;
	}

	/**
	 * @return the nodes
	 */
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	/**
	 * @return the nodes
	 */
	public ArrayList<Edge> getEdges() {
		return edges;
	}

	/**
	 * Add an edge to the flow and sets the previous edge. Then updates the new edge to be the
	 * previous edge for the next edge that will be created.
	 * @param edge The selected edge.
	 */
	public void addEdge(Edge edge) {
		edge.setPredecessor(edgePredecessor);
		edges.add(edge);
		edgePredecessor = edge;
	}
	
	/**
	 * Adds a node to the flow and sets the previous node. Then updates the new node to be the
	 * previous node for the next node that will be created.
	 * @param node The current node.
	 */
	public void addNode(Node node) {
		/* set predecessor and add edge to flow */
		node.setPredecessor(nodePredecessor);
		nodes.add(node);
		/* update predecessor for next edge added */
		nodePredecessor = node;
	}
	/**
	 * @return the linkTxAndTotalCost
	 */
	public ArrayList<Integer> getLinkTxAndTotalCost() {
		return linkTxAndTotalCost;
	}

	/**
	 * @param initialPriority the initialPriority to set
	 */
	public void setInitialPriority(Integer initialPriority) {
		this.initialPriority = initialPriority;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * @param numTxPerLink the numTxPerLink to set
	 */
	public void setNumTxPerLink(Integer numTxPerLink) {
		this.numTxPerLink = numTxPerLink;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @param linkTxAndTotalCost the linkTxAndTotalCost to set
	 */
	public void setLinkTxAndTotalCost(ArrayList<Integer> linkTxAndTotalCost) {
		this.linkTxAndTotalCost = linkTxAndTotalCost;
	}

	@Override
    public int compareTo(Flow flow) {
    	// ascending order (0 is highest priority)
        return flow.getPriority() > this.getPriority() ? -1 : 1;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
}
