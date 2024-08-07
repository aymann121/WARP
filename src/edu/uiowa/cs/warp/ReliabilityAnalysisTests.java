package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ReliabilityAnalysisTests {

	/**
	 * Initializes reliabilityAnalysis with the e2e and m variables
	 * @return ReliabiltyAnalysis
	 */
	ReliabilityAnalysis mE2eReliabilityAnalysisInitialization() {
		Double e2e = .99;
		Double m = .99;
		// end to end and minimum packet reception rate.
		ReliabilityAnalysis rObj = new ReliabilityAnalysis(e2e,m);
		return rObj;
	}
	/**
	 * initializeReliabilityAnalsysis with workload,nChannels, and the priority ScheduleChoices.
	 * @param fileName chooses which file will be used.
	 * @return ReliabilityAnalysis
	 */
	ReliabilityAnalysis programReliabilityAlaysisInitialization(String fileName) {
		double M = .9;
		double E2E = .99;
		Integer nChannels = 16;
		WorkLoad workLoad = new WorkLoad(1,M,E2E,fileName);
		Program program = new Program(workLoad, nChannels, SystemAttributes.ScheduleChoices.PRIORITY);
		return new ReliabilityAnalysis(program);
	}
	/**
	 * an overloaded version of initializeReliabilityAnalsysis with the variables M and E2E
	 * @param fileName chooses which file will be used.
	 * @return ReliabilityAnalysis
	 */
	ReliabilityAnalysis programReliabilityAlaysisInitialization(String fileName,double M, double E2E) {
		Integer nChannels = 16;
		WorkLoad workLoad = new WorkLoad(1,M,E2E,fileName);
		Program program = new Program(workLoad, nChannels, SystemAttributes.ScheduleChoices.PRIORITY);
		return new ReliabilityAnalysis(program);
	}
	
	/**
	 * Tests the NumTxPerLinkAndTotalTxCost method in reliabilityAnalysis using the Example File and an empty flow
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testNumTxPerLinkAndTotalTxCostExample() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("Example.txt");
		Flow flow = new Flow();
		ArrayList<Integer> actual = rObj.numTxPerLinkAndTotalTxCost(flow);
		ArrayList<Integer> expected = new ArrayList<Integer>();
		expected.add(-2);
		assertEquals(actual, expected);
	}
	/**
	 * Tests the NumTxPerLinkAndTotalTxCost method in reliabilityAnalysis using the Example File and an empty flow
	 */
	
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testNumTxPerLinkAndTotalTxCostStressTest() {  
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("StressTest4.txt");
		Flow flow = new Flow();
		ArrayList<Integer> actual = rObj.numTxPerLinkAndTotalTxCost(flow);
		ArrayList<Integer> expected = new ArrayList<Integer>();
		expected.add(-2);
		assertEquals(actual, expected);
	}
	/**
	 * Tests the VerifyReliabilities method in reliabilityAnalysis using the Example File using default configurations
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testVerifyReliabilitiesExample() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("Example.txt");
		boolean expected = true;
		boolean actual = rObj.verifyReliabilities();
		assertEquals( expected,actual);
	}
	/**
	 * Tests the VerifyReliabilities method in reliabilityAnalysis using the Example File using and E2E of 1
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testVerifyReliabilitiesExampleE2E1() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("Example.txt", .9, 1);
		boolean expected = false;
		boolean actual = rObj.verifyReliabilities();
		assertEquals( expected,actual);
	}
	/**
	 * tests the getFinalReliabilityRow method using the Example.txt file
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetFinalReliabilityRowExample() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("Example.txt");
		double[] doubleList = {1.0, 0.999, 0.9963, 1.0, 0.999, 0.9963};
		ReliabilityRow expected = new ReliabilityRow();
		for(double e : doubleList) {
			expected.add(e);
		}
		ReliabilityRow actual = rObj.getFinalReliabilityRow();
		assertEquals(expected,actual);
	}
	/** 
	 * tests the getFinalReliabilityRow method using the StressTest4 file
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetFinalReliabilityRowStressTest() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("StressTest4.txt");
		double[] doubleList = {1.0, 0.999, 0.9963, 1.0, 0.999, 0.9963, 0.99144, 0.98415, 0.9743084999999999, 0.9619082099999999, 1.0, 0.999, 0.8991, 0.8091900000000001, 0.7282710000000001, 0.0, 1.0, 0.999, 0.9963, 0.99144, 0.98415, 0.9743084999999999, 0.9619082099999999, 0.9470278619999999, 1.0, 0.999, 0.8991, 0.8091900000000001, 0.7282710000000001, 1.0, 0.99, 0.81, 1.0, 0.999, 0.0, 0.0, 0.0, 1.0, 0.99, 0.81, 0.7290000000000001, 0.6561000000000001, 0.5904900000000002, 0.5314410000000002, 1.0, 0.99, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.999, 0.9963, 0.99144, 0.98415, 0.9743084999999999};
		ReliabilityRow expected = new ReliabilityRow();
		for(double e : doubleList) {
			expected.add(e);
		}
		ReliabilityRow actual = rObj.getFinalReliabilityRow();
		assertEquals(expected,actual);
	}
	/**
	 * test the GetReliabilities method using the Example file.	
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetReliabilitiesExample() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("Example.txt");

		ReliabilityTable actual = rObj.getReliabilities();
		ReliabilityTable expected = new ReliabilityTable();
		double[][] expectedValues = new double[100][6];
		
		expectedValues[0] =new double[]{1.0,0.9,0.0,1.0,0.0,0.0};
		expectedValues[1] =new double[]{1.0,0.99,0.81,1.0,0.0,0.0};
		expectedValues[2] =new double[]{1.0,0.999,0.972,1.0,0.0,0.0};
		expectedValues[3] =new double[]{1.0,0.999,0.9963,1.0,0.0,0.0};
		expectedValues[4] =new double[]{1.0,0.999,0.9963,1.0,0.9,0.0};
		expectedValues[5] =new double[]{1.0,0.999,0.9963,1.0,0.99,0.81};
		expectedValues[6] =new double[]{1.0,0.999,0.9963,1.0,0.999,0.972};
		
		for(int i = 7; i<100; i++) {
			expectedValues[i] = new double[]{1.0,0.999,0.9963,1.0,0.999,0.9963};
		}
		for(double[] e: expectedValues) {
			ReliabilityRow ex = new ReliabilityRow();
			for(double i: e){
				ex.add(i);
			}
			expected.add(ex);
		}
		assertEquals(expected,actual);
	}
	/**
	 * test the GetReliabilities method using the Example2 file.	
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetReliabilitiesExample2() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("Example1a.txt");

		ReliabilityTable actual = rObj.getReliabilities();
		ReliabilityTable expected = new ReliabilityTable();
		double[][] expectedValues = new double[20][6];
		
		expectedValues[0] =new double[]{1.0,0.9,0.0,1.0,0.0,0.0};
		expectedValues[1] =new double[]{1.0,0.99,0.81,1.0,0.0,0.0};
		expectedValues[2] =new double[]{1.0,0.999,0.972,1.0,0.0,0.0};
		expectedValues[3] =new double[]{1.0,0.999,0.9963,1.0,0.0,0.0};
		expectedValues[4] =new double[]{1.0,0.999,0.9963,1.0,0.9,0.0};
		expectedValues[5] =new double[]{1.0,0.999,0.9963,1.0,0.99,0.81};
		expectedValues[6] =new double[]{1.0,0.999,0.9963,1.0,0.999,0.972};
		expectedValues[7] = new double[]{1.0,0.999,0.9963,1.0,0.999,0.9963};
		expectedValues[8] = new double[]{1.0,0.999,0.9963,1.0,0.999,0.9963};
		expectedValues[9] = new double[]{1.0,0.999,0.9963,1.0,0.999,0.9963};
		expectedValues[10] = new double[]{1.0,0.9,0.0,1.0,0.999,0.9963};
		expectedValues[11] = new double[]{1.0,0.99,0.81,1.0,0.999,0.9963};
		expectedValues[12] = new double[]{1.0,0.999,0.972,1.0,0.999,0.9963};

		
		for(int i =13; i<20; i++) {
			expectedValues[i] = new double[]{1.0,0.999,0.9963,1.0,0.999,0.9963};
		}
		for(double[] e: expectedValues) {
			ReliabilityRow ex = new ReliabilityRow();
			for(double i: e){
				ex.add(i);
			}
			expected.add(ex);
		}
		assertEquals(expected,actual);
	}
	/**
	 * tests the getFixedTxPerLinkAndTotalTxCost method using the Example.txt file
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testGetFixedTxPerLinkAndTotalTxCostExample() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("Example.txt");
		Flow flow = new Flow();
		ArrayList<Integer> actual = rObj.getFixedTxPerLinkAndTotalTxCost(flow);
		ArrayList<Integer> expected = new ArrayList<Integer>();
		expected.add(-2);
		assertEquals(expected,actual);
	}
	
	/**
	 * tests the getReliabilityHeaderRow using the Example.txt file
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void getReliabilityHeaderRowExample() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("Example.txt");
		ArrayList<String> actual = rObj.getReliabilityHeaderRow();
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("F0:A");
		expected.add("F0:B");
		expected.add("F0:C");
		expected.add("F1:C");
		expected.add("F1:B");
		expected.add("F1:A");
		assertEquals(expected,actual);
	}
	/**
	 * test the getReliabilityHeaderRow method using the StressTest4 file
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void getReliabilityHeaderRowStressTest() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("StressTest4.txt");
		ArrayList<String> actual = rObj.getReliabilityHeaderRow();
		ArrayList<String> expected = new ArrayList<String>();
		String[] values = {"F1:B", "F1:C", "F1:D", "F2:C", "F2:D", "F2:E", "F2:F", "F2:G", "F2:H", "F2:I", "F3:C", "F3:D", "F3:E", "F3:J", "F3:K", "F3:L", "F4:A", "F4:B", "F4:C", "F4:D", "F4:E", "F4:J", "F4:K", "F4:L", "F5:A", "F5:B", "F5:C", "F5:D", "F5:E", "F6:B", "F6:C", "F6:D", "F7:A", "F7:B", "F7:C", "F7:D", "F7:E", "F8:C", "F8:D", "F8:E", "F8:F", "F8:G", "F8:H", "F8:I", "F9:A", "F9:B", "F9:C", "F9:D", "F9:E", "F9:J", "F9:K", "F9:L", "F10:C", "F10:D", "F10:E", "F10:J", "F10:K", "F10:L"};
		for(String e:values) {
			expected.add(e);
		}
		assertEquals(expected,actual);
	}
	/**
	 * test the getColumnHeaderExample Method using the example.txt file
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void getColumnHeaderExample() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("Example.txt");
		ArrayList<String> actual = rObj.getColumnHeader();
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("F0:A");
		expected.add("F0:B");
		expected.add("F0:C");
		expected.add("F1:C");
		expected.add("F1:B");
		expected.add("F1:A");
		assertEquals(expected,actual);
	}
	/**
	 * test the getColumnHeaderExample Method using the example1a.txt file
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void getColumnHeaderExample1a() {
		ReliabilityAnalysis rObj = programReliabilityAlaysisInitialization("Example1a.txt");
		ArrayList<String> actual = rObj.getColumnHeader();
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("F0:A");
		expected.add("F0:B");
		expected.add("F0:C");
		expected.add("F1:C");
		expected.add("F1:B");
		expected.add("F1:A");
		assertEquals(expected,actual);
	}
	/**
	 * Tests the edge case of what happens when we call the wrong method
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testBuildReliabilityTableFailure() {
		ReliabilityAnalysis rObj = mE2eReliabilityAnalysisInitialization();
		Exception thrown = assertThrows(NullPointerException.class, () -> rObj.buildReliabilityTable(),
				"Expected NullPointer excption was not thrown");
		String expectedErrorMessage = "Cannot invoke \"edu.uiowa.cs.warp.WorkLoad.getFlowNamesInPriorityOrder()\" because \"this.workLoad\" is null";
		String actualErrorMessage = thrown.getMessage();
		//compares the error message to the one  we expect
		assertEquals(expectedErrorMessage,actualErrorMessage);
	}
	

}
