package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class AddFlowTest {

	@Test
	public void testAddFlow() {
		String testFlow = new String("StressTest.txt"); 
		WorkLoad workLoad = new WorkLoad(0.0, 0.0, testFlow);
		workLoad.addFlow("testerFlow!");  // adding a new flow to the FlowNames array
		String [] compareTo = workLoad.getFlowNames();
		String expected = "testerFlow!";
		assertEquals(expected,compareTo[compareTo.length -1] ); 
	}

}
