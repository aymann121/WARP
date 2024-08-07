package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ReliabilityVisualizationTest {
	
	/**
	 * Initializes the ReliabilityVisualization object by initializing the warpInterface object using the SystemFactory class.
	 * 
	 * @param fileName the exact txt file that we want to use for testing
	 * @return returns the ReliabilityVisualization Object that we can use for testing.
	 */
	private ReliabilityVisualization initializeReliabilityVisualization(String fileName){
		double M = .9;
		double E2E = .99;
		Integer nChannels = 16;
		WorkLoad workLoad = new WorkLoad(1,M,E2E,fileName);
		WarpInterface warp = SystemFactory.create(workLoad, nChannels, SystemAttributes.ScheduleChoices.PRIORITY);
		ReliabilityVisualization rv = new ReliabilityVisualization(warp);
		return rv;
	}
	/**
	 * Initializes the ReliabilityVisualization object by initializing the warpInterface object using the SystemFactory class.
	 * An overloaded method with the added parameters of M and E2E when they aren't default
	 * 
	 * @param fileName the exact txt file that we want to use for testing
	 * @return returns the ReliabilityVisualization Object that we can use for testing.
	 */
	private ReliabilityVisualization initializeReliabilityVisualization(String fileName,double M,double E2E){

		Integer nChannels = 16;
		WorkLoad workLoad = new WorkLoad(1,M,E2E,fileName);
		WarpInterface warp = SystemFactory.create(workLoad, nChannels, SystemAttributes.ScheduleChoices.PRIORITY);
		ReliabilityVisualization rv = new ReliabilityVisualization(warp);
		return rv;
	}
	
	/**
	 * Tests the specific output of the ReliabilityVisualization Header when initialized with the Example.txt file.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testExampleCreateHeader() {
		Description exampleHeader = new Description();
		exampleHeader.add("Reliability Analysis for graph Example created with the following parameters: \n");
		exampleHeader.add("Scheduler Name: Priority\n");
		exampleHeader.add("numFaults: 1\n");
		exampleHeader.add("M: 0.9\n");
		exampleHeader.add("E2E: 0.99\n");
		exampleHeader.add("nChannels: 16\n");
		
		assertEquals(exampleHeader, initializeReliabilityVisualization("Example.txt").createHeader());
	}
	/**
	 * Tests the specific output of the ReliabilityVisualization Header when initialized with the StressTest4.txt file.
	 * M is .9 and E2E is .99 (default)
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testStressTest4CreateHeader() {
		Description exampleHeader = new Description();
		exampleHeader.add("Reliability Analysis for graph StressTest4 created with the following parameters: \n");
		exampleHeader.add("Scheduler Name: Priority\n");
		exampleHeader.add("numFaults: 1\n");
		exampleHeader.add("M: 0.9\n");
		exampleHeader.add("E2E: 0.99\n");
		exampleHeader.add("nChannels: 16\n");
		
		assertEquals(exampleHeader, initializeReliabilityVisualization("StressTest4.txt").createHeader());
	}

	/**
	 * Tests the specific output of the ReliabilityVisualization data Column Header when initialized with the Example.txt file.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testExampleCreateColumnHeader() {
		String[] actualArr = initializeReliabilityVisualization("Example.txt").createColumnHeader();
		String[] expectedArr = {"F0:A", "F0:B", "F0:C", "F1:C", "F1:B", "F1:A"};

		String expected = Arrays.toString(expectedArr);
		String actual = Arrays.toString(actualArr);
		assertEquals(expected,actual);
	}
	/**
	 * Tests the specific output of the ReliabilityVisualization data Column Header when initialized with the Example.txt file
	 * This one uses a custom 0.8 E2E and 0.5 M
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testExampleCreateColumnHeader08E2E() {
		String[] actualArr = initializeReliabilityVisualization("Example.txt",0.8,0.5).createColumnHeader();
		String[] expectedArr = {"F0:A", "F0:B", "F0:C", "F1:C", "F1:B", "F1:A"};

		String expected = Arrays.toString(expectedArr);
		String actual = Arrays.toString(actualArr);
		assertEquals(expected,actual);
	}
	/**
	 * Tests the specific output of the ReliabilityVisualization data Column Header when initialized with the Example.txt file
	 * This one uses a custom 1 E2E and 1 M
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testExampleCreateColumnHeader1E2E() {
		String[] actualArr = initializeReliabilityVisualization("Example.txt",1,1).createColumnHeader();
		String[] expectedArr = {"F0:A", "F0:B", "F0:C", "F1:C", "F1:B", "F1:A"};

		String expected = Arrays.toString(expectedArr);
		String actual = Arrays.toString(actualArr);
		assertEquals(expected,actual);
	}
	/**
	 * Tests the specific output of the ReliabilityVisualization data Column Header when initialized with the StressTest4.txt file.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testStressTestCreateColumnHeader() {
		String[] actualArr = initializeReliabilityVisualization("StressTest4.txt").createColumnHeader();
		String[] expectedArr = {"F1:B","F1:C","F1:D","F2:C","F2:D","F2:E","F2:F","F2:G","F2:H","F2:I","F3:C","F3:D","F3:E","F3:J","F3:K","F3:L","F4:A","F4:B","F4:C","F4:D","F4:E","F4:J","F4:K","F4:L","F5:A","F5:B","F5:C","F5:D","F5:E","F6:B","F6:C","F6:D","F7:A","F7:B","F7:C","F7:D","F7:E","F8:C","F8:D","F8:E","F8:F","F8:G","F8:H","F8:I","F9:A","F9:B","F9:C","F9:D","F9:E","F9:J","F9:K","F9:L","F10:C","F10:D","F10:E","F10:J","F10:K","F10:L"};

		String expected = Arrays.toString(expectedArr);
		String actual = Arrays.toString(actualArr);
		assertEquals(expected,actual);
	}
	
	/**
	 * Tests the specific output of the ReliabilityVisualization data when initialized with the Example.txt file.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testExampleCreateVisualizationData() {
		String[][] actualArr = initializeReliabilityVisualization("Example.txt").createVisualizationData();
		String[][] expectedArr = new String[100][6];
		
		expectedArr[0] = new String[]{"1.0","0.9","0.0","1.0","0.0","0.0"};
		expectedArr[1] =new String[]{"1.0","0.99","0.81","1.0","0.0","0.0"};
		expectedArr[2] =new String[]{"1.0","0.999","0.972","1.0","0.0","0.0"};
		expectedArr[3] =new String[]{"1.0","0.999","0.9963","1.0","0.0","0.0"};
		expectedArr[4] =new String[]{"1.0","0.999","0.9963","1.0","0.9","0.0"};
		expectedArr[5] =new String[]{"1.0","0.999","0.9963","1.0","0.99","0.81"};
		expectedArr[6] =new String[]{"1.0","0.999","0.9963","1.0","0.999","0.972"};
		
		for(int i = 7; i<100; i++) {
			expectedArr[i] = new String[]{"1.0","0.999","0.9963","1.0","0.999","0.9963"};
		}
		for(int i = 0; i<100; i++) {
			assertEquals(Arrays.asList(expectedArr[i]),Arrays.asList(actualArr[i]));
		}
	}
	/**
	 * Tests the specific output of the ReliabilityVisualization data when initialized with the Example1a.txt file using 0.8M.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testExample1a08MCreateVisualizationData() {
		String[][] actualArr = initializeReliabilityVisualization("Example1a.txt",.8,.99).createVisualizationData();
		String[][] expectedArr = new String[20][6];
		
		expectedArr[0] = new String[]{"1.0","0.8","0.0","1.0","0.0","0.0"};
		expectedArr[1] =new String[]{"1.0","0.96","0.6400000000000001","1.0","0.0","0.0"};
		expectedArr[2] =new String[]{"1.0","0.992","0.896","1.0","0.0","0.0"};
		expectedArr[3] =new String[]{"1.0","0.9984","0.9728000000000001","1.0","0.0","0.0"};
		expectedArr[4] =new String[]{"1.0","0.9984","0.9932799999999999","1.0","0.0","0.0"};
		expectedArr[5] =new String[]{"1.0","0.9984","0.9932799999999999","1.0","0.8","0.0"};
		expectedArr[6] =new String[]{"1.0","0.9984","0.9932799999999999","1.0","0.96","0.6400000000000001"};
		expectedArr[7] =new String[]{"1.0","0.9984","0.9932799999999999","1.0","0.992","0.896"};
		expectedArr[8] =new String[]{"1.0","0.9984","0.9932799999999999","1.0","0.9984","0.9728000000000001"};
		expectedArr[9] =new String[]{"1.0","0.9984","0.9932799999999999","1.0","0.9984","0.9932799999999999"};
		expectedArr[10] =new String[]{"1.0","0.8","0.0","1.0","0.9984","0.9932799999999999"};
		expectedArr[11] =new String[]{"1.0","0.96","0.6400000000000001","1.0","0.9984","0.9932799999999999"};
		expectedArr[12] =new String[]{"1.0","0.992","0.896","1.0","0.9984","0.9932799999999999"};
		expectedArr[13] =new String[]{"1.0","0.9984","0.9728000000000001","1.0","0.9984","0.9932799999999999"};
		
		for(int i = 14; i<20; i++) {
			expectedArr[i] =new String[]{"1.0","0.9984","0.9932799999999999","1.0","0.9984","0.9932799999999999"};
		}
		for(int i = 0; i<20; i++) {
			assertEquals(Arrays.asList(expectedArr[i]),Arrays.asList(actualArr[i]));
		}

	}
	
	/**
	 * Tests the specific output of the ReliabilityVisualization data when initialized with the Example1a.txt file using .9 M .99 E2E.
	 */
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	void testExample1a09MCreateVisualizationData() {
		String[][] actualArr = initializeReliabilityVisualization("Example1a.txt").createVisualizationData();
		String[][] expectedArr = new String[20][6];
		
		expectedArr[0] = new String[]{"1.0","0.9","0.0","1.0","0.0","0.0"};
		expectedArr[1] =new String[]{"1.0","0.99","0.81","1.0","0.0","0.0"};
		expectedArr[2] =new String[]{"1.0","0.999","0.972","1.0","0.0","0.0"};
		expectedArr[3] =new String[]{"1.0","0.999","0.9963","1.0","0.0","0.0"};
		expectedArr[4] =new String[]{"1.0","0.999","0.9963","1.0","0.9","0.0"};
		expectedArr[5] =new String[]{"1.0","0.999","0.9963","1.0","0.99","0.81"};
		expectedArr[6] =new String[]{"1.0","0.999","0.9963","1.0","0.999","0.972"};
		for(int i = 7; i<10; i++) {
			expectedArr[i] = new String[]{"1.0","0.999","0.9963","1.0","0.999","0.9963"};
		}
		expectedArr[10] = new String[]{"1.0","0.9","0.0","1.0","0.999","0.9963"};
		expectedArr[11] = new String[]{"1.0","0.99","0.81","1.0","0.999","0.9963"};
		expectedArr[12] = new String[]{"1.0","0.999","0.972","1.0","0.999","0.9963"};
		for(int i = 13; i<20; i++) {
			expectedArr[i] = new String[]{"1.0","0.999","0.9963","1.0","0.999","0.9963"};
		}
		for(int i = 0; i<20; i++) {
			assertEquals(Arrays.asList(expectedArr[i]),Arrays.asList(actualArr[i]));
		}
	}

	

}
