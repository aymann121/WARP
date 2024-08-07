package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;


/**
 * These are the tests for methods i through p. Tests for methods a-h are in their own files for their respective methods done by Kennedy Bombei.
 * @author Ayman
 *
 */
class WorkLoadTest {

	
	/**
	 * tests the SetFlowDeadline function using the StressTest File with the F1 flow.
	 */
	@Test
	void testSetFlowDeadline() {
		WorkLoad testObj = new WorkLoad(.9,.99,"StressTest.txt");
		Integer newFlowDeadline = 44;
		String flow = "F1";
		testObj.setFlowDeadline(flow,newFlowDeadline);
		Integer actual = testObj.getFlowDeadline(flow);
		assertSame(newFlowDeadline,actual);
	}

	/**
	 * tests the GetFlowDeadline function using the StressTest File with every Flow in that file using a for loop.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetFlowDeadline() {
		WorkLoad testObj = new WorkLoad(.9,.99,"StressTest.txt");
		String[] flowNames = {"F10", "F1", "F2", "AF1", "F3", "AF2", "F4", "F5", "AF4", "F6", "AF5", "F7", "F8", "F9", "AF10"};
		Integer[] flowDeadlines = {100, 20, 50, 20, 50, 50, 75, 75, 75, 75, 75, 100, 100, 100, 100, 75};
		for(int i = 0; i<flowNames.length; i++) {
			if(testObj.getFlowDeadline(flowNames[i]) != flowDeadlines[i]) {
				fail("Failed with the "+flowNames[i]+" flow. Expected: "+flowDeadlines[i]+" Actual: "+testObj.getFlowDeadline(flowNames[i]));
			}
		}
	}
	
	/**
	 * tests the getFlowDeadline using a nonExistentFlow. It compares the actual deadline to the default deadline.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetFlowDeadlineNonExistentFlow() {
		//initialize object and flow
		WorkLoad testObj = new WorkLoad(.9,.99,"StressTest.txt");
		String flow = "nonExistentFlow";
		//expected default deadline
		int expected = 100;
		assertEquals(expected,testObj.getFlowDeadline(flow));
	}


	/**
	 * test GetFlowNames using the Example file with an expected and actual list of Names.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetFlowNamesExample() {
			WorkLoad testObj = new WorkLoad(.9,.99,"Example.txt");
			String[] expectedNames = {"F0","F1"};
			//use the Arrays.toString method to easily Compare these two pieces of data.
			String expected = Arrays.toString(expectedNames);
			String actual = Arrays.toString(testObj.getFlowNames());
			assertEquals(expected,actual);
			
	}
	/**
	 * test GetFlowNames using the StressTest file with an expected and actual listOfNames.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetFlowNamesStressTest(){
		WorkLoad testObj = new WorkLoad(.9,.99,"StressTest.txt");
		String[] expectedNames = {"F1","F5","F2","F4","F3","F6","F7","F8","F9","F10","AF1","AF5","AF2","AF4","AF10"}; 
		//use the Arrays.toString method to easily Compare these two pieces of data.
		String expected = Arrays.toString(expectedNames);	
		String actual = Arrays.toString(testObj.getFlowNames());
		
		assertEquals(expected,actual);
	}
	/**
	 * test GetNodeIndex method using the StressTest file with every node available in that file.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetNodeIndex() {
		WorkLoad testObj = new WorkLoad(.9,.99,"StressTest.txt");
		String[] nodeNames = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U","V","W","Y"};
		int[] nodeIndexes = {3,0,1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 15, 12, 13, 14, 16, 17, 18, 19, 20, 21, 22, 23};

		for( Node e: testObj.getNodes().values()){
			for(int i = 0; i< nodeNames.length;i++) {
				if(nodeNames[i].equals(e.getName()) && !e.getIndex().equals(nodeIndexes[i])) {
					//if any of the Nodes don't have the right Indexes, this test fails and returns an error message.
					fail("not the right index Expected: "+nodeNames[i]+":"+nodeIndexes[i]+" Actual: "+e.getName()+":"+e.getIndex());
				}
			}
		}
	}
	/**
	 * test the GetNodesInFlow method using the StressTest file using the F2 flow and an array of an expected list of Nodes.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetNodesInFlowF2Flow() {
		WorkLoad testObj = new WorkLoad(.9,.99,"StressTest.txt");
		String flowName = "F2";
		String[] listOfNodes = {"C", "D", "E", "F", "G", "H", "I"};
		String expected = Arrays.toString(listOfNodes);
		String actual = Arrays.toString(testObj.getNodesInFlow(flowName));
		assertEquals(expected,actual);
	}
	/**
	 * test the GetNodesInFlow method using the StressTest file with a non existent flow. Expects an empty array.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetNodesInFlowNonExistentFlow() {
		WorkLoad testObj = new WorkLoad(.9,.99,"StressTest.txt");
		String flowName = "NonExistentFlow";
		String expected = "[]";
		String actual = Arrays.toString(testObj.getNodesInFlow(flowName));
		assertEquals(expected,actual);
	}

	/**
	 * Get hyperPeriod of WorkFlow using the StressTest File.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetHyperPeriodStressTest() {
		WorkLoad testObj = new WorkLoad(.9,.99,"StressTest.txt");
		Integer expected = 300;
		assertEquals(expected, testObj.getHyperPeriod());
	}
	/**
	 * Get hyperPeriod of Workflow using the Example File.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetHyperPeriodExample() {
		WorkLoad testObj = new WorkLoad(.9,.99,"Example.txt");
		Integer expected = 100;
		assertEquals(expected, testObj.getHyperPeriod());
	}

	/**
	 * Get number of transmissions attempts per link using the F1 flow in the StressTest File.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetNumTxAttemptsPerLinkF1Flow() {
		WorkLoad testObj = new WorkLoad(.9,.99,"StressTest.txt");
		String flow = "F1";
		Integer[] flowTx = {3,3,0};
		String expected = Arrays.toString(flowTx);
		String actual = Arrays.toString(testObj.getNumTxAttemptsPerLink(flow));
		assertEquals(expected,actual);
	}
	/**
	 * Get number of transmissions attempts per link using the F3 flow in the Example3 File.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetNumTxAttemptsPerLinkF3Flow() {
		WorkLoad testObj = new WorkLoad(.9,.99,"Example2.txt");
		String flow = "F5";
		Integer[] flowTx = {3,4,4,4,0};
		String expected = Arrays.toString(flowTx);
		String actual = Arrays.toString(testObj.getNumTxAttemptsPerLink(flow));
		assertEquals(expected,actual);
	}
	/**
	 * Tests the error message of the GetNumTxAttemptsPerLink using a non existent flow.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetNumTxAttemptsPerLinkNonExistentFlow() {
		//initialize object and flow
		WorkLoad testObj = new WorkLoad(.9,.99,"StressTest.txt");
		String flow = "nonExistentFlow";
		
		Exception thrown = assertThrows(IndexOutOfBoundsException.class, () -> testObj.getNumTxAttemptsPerLink(flow),
				"Expected IndexOutOfBounds excption was not thrown");
		String expectedErrorMessage = "Index -1 out of bounds for length 0";
		String actualErrorMessage = thrown.getMessage();
		//compares the error message to the one  we expect
		assertEquals(expectedErrorMessage,actualErrorMessage);
	}
	/**
	 * testMaxFlowLength function using the StressTest file which with an expected value of 8.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testMaxFlowLengthStressTest() {
		WorkLoad testObj = new WorkLoad(.9,.99,"StressTest.txt");
		Integer expected = 8;
		Integer actual = testObj.maxFlowLength();
		assertEquals(expected,actual);
	}
	/**
	 * testMaxFlowLength function using the Example file which with an expected value of 3.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testMaxFlowLengthExample() {
		WorkLoad testObj = new WorkLoad(.9,.99,"Example.txt");
		Integer expected = 3;
		Integer actual = testObj.maxFlowLength();
		assertEquals(expected,actual);
	}
	/**
	 * testMaxFlowLength function using the Example2 file which with an expected value of 6.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testMaxFlowLengthExample2() {
		WorkLoad testObj = new WorkLoad(.9,.99,"Example2.txt");
		Integer expected = 6;
		Integer actual = testObj.maxFlowLength();
		assertEquals(expected,actual);
	}


}
