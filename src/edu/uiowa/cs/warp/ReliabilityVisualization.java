package edu.uiowa.cs.warp;

/**
 * ReliabilityVisualization creates the visualizations for
 * the reliability analysis of the WARP program in the form
 * of a .ra file located in the OutputFiles folder.
 */
public class ReliabilityVisualization  extends VisualizationObject {

	/**
	 * Creating final variables that will be used to denote the file type that will be created (.ra) and the name of the table, 
	 * which will always be a Reliability Analysis when this class is called.
	 */
	private static final String SOURCE_SUFFIX = ".ra";
	private static final String OBJECT_NAME = "Reliability Analysis";
	/**
	 * Creating variables to access the reliabilityAnalysis class and warp 
	 */
	private WarpInterface warp;
	private ReliabilityAnalysis ra;
	
	ReliabilityVisualization(WarpInterface warp) {
		super(new FileManager(), warp, SOURCE_SUFFIX);
		this.warp = warp;
		this.ra = warp.toReliabilityAnalysis();
		ra = new ReliabilityAnalysis(warp.toProgram());
	}

	/**
	 * Creates a header for the .ra file to be created. This includes the schedule name, the minPacketRecptionRate, 
	 * the e2e reliability, and the number of channels for the chosen file. 
	 * @return The entire header containing all above information
	 */
	@Override
	protected Description createHeader() {
		Description header = new Description();
		
		header.add(createTitle());
		header.add(String.format("Scheduler Name: %s\n", warp.getSchedulerName()));
		if(warp.getNumFaults() > 0) {
			header.add(String.format("numFaults: %s\n", warp.getNumFaults()));
		}
		header.add(String.format("M: %s\n",warp.getMinPacketReceptionRate()));
		header.add(String.format("E2E: %s\n",warp.getE2e()));
		header.add(String.format("nChannels: %s\n",warp.getNumChannels()));
		
		return header;
	}
	
	/**
	 * Creates a header for each column, combining the name of the flow with the corresponding node. 
	 * @return columnNames a list of the names for every column in order for our .ra file.
	 */
	@Override
	protected String[] createColumnHeader() {
		String[] columnHeader = ra.getColumnHeader().toArray(new String[ra.getColumnHeader().size()]);
		return  columnHeader;
	}
	
	/**
	 * Creates the data to fill our .ra file using the given calculation :
	 * newSinkNodeState = (1-M)*prevSnkNodeState + M*prevSrcNodeState. This data is put into a ,matrix of size
	 * (number of rows) x (number of columns).
	 * Doing this way until we implement getReliabilities this will be changed before the final
	 * this was just to make sure the file was working correctly.
	 * @return visualizationData the data to fill our .ra file.
	 */

	protected String[][] createVisualizationData(){
		ReliabilityTable reliabilities = ra.getReliabilities();
		String[][] visualizationData = new String[reliabilities.size()][ra.getColumnHeader().size()];
		for(int i = 0; i < reliabilities.getNumRows(); i++) {
			for(int j = 0; j < reliabilities.getNumColumns(); j++) {
				visualizationData[i][j] = reliabilities.get(i, j).toString();
			}
		}
		return visualizationData;
	}
	/**
	 * Creates the title for our .ra file.
	 * @return title
	 */
	private String createTitle() {
		return String.format(OBJECT_NAME + " for graph %s created with the following parameters: \n", warp.getName());
	}
/* File Visualization for workload defined in Example.txt follows. Note
 * that your Authentication tag will be different from this example. The
 * rest of your output in the file ExamplePriority-0.9M-0.99E2E.ra
 * should match this output, where \tab characters are used a column
 * delimiters.
// Course CS2820 Authentication Tag: r3XWfL9ywZO36jnWMZcKC2KTB2hUCm3AQCGxREWbZRoSn4/XdrQ/QuNQvtzAxeSSw55bWTXwbI9VI0Om+mEhNd4JC2UzrBBrXnHmsbPxbZ8=
Reliability Analysis for graph Example created with the following parameters:
Scheduler Name:	Priority
M:	0.9
E2E:	0.99
nChannels:	16
F0:A	F0:B	F0:C	F1:C	F1:B	F1:A
1.0	0.9	0.0	1.0	0.0	0.0
1.0	0.99	0.81	1.0	0.0	0.0
1.0	0.999	0.972	1.0	0.0	0.0
1.0	0.999	0.9963	1.0	0.0	0.0
1.0	0.999	0.9963	1.0	0.9	0.0
1.0	0.999	0.9963	1.0	0.99	0.81
1.0	0.999	0.9963	1.0	0.999	0.972
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
1.0	0.999	0.9963	1.0	0.999	0.9963
*/
}
