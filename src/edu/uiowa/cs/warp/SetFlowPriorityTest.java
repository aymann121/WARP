package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class SetFlowPriorityTest {
	String file = "StressTest4.txt";
	WorkLoad workLoad = new WorkLoad(0.0, 0.0, file);

	//Testing that you can accurately set the priority for a single flow.
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testSettingSingleFlow() {
		//String file = "StressTest4.txt";
		String flowToSet = "F1";
		Integer newPriority = 6 ;
		//WorkLoad workLoad = new WorkLoad(0.0, 0.0, file);
		workLoad.setFlowPriority(flowToSet, newPriority);
		assertEquals(newPriority, workLoad.getFlowPriority(flowToSet));
	}
	//A test to ensure that multiple flows can accurately be set.
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testSettingPriorityMultipleFlows() {
		String flow1ToSet = "F2";
		Integer newPriority1 = 1 ;
		String flow2ToSet = "F5";
		Integer newPriority2 = 5 ;
		workLoad.setFlowPriority(flow1ToSet, newPriority1);
		workLoad.setFlowPriority(flow2ToSet, newPriority2);
		assertEquals(newPriority1, workLoad.getFlowPriority(flow1ToSet));
		assertEquals(newPriority2, workLoad.getFlowPriority(flow2ToSet));
	}
	//A test to make sure if you set the same flow twice, the most recent priority is saved.
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testSettingSameFlowTwice() {
		String flowToSet = "F2";
		Integer firstPriority = 1 ;
		Integer finalPriority = 5 ;
		workLoad.setFlowPriority(flowToSet, firstPriority);
		workLoad.setFlowPriority(flowToSet, finalPriority);
		assertEquals(finalPriority, workLoad.getFlowPriority(flowToSet));
	}
	//Test to ensure negative numbers and 
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testNegativePriority() {
		String flowToSet = "F2";
		Integer newNegPriority = -1 ;
		workLoad.setFlowPriority(flowToSet, newNegPriority);
		assertEquals(newNegPriority, workLoad.getFlowPriority(flowToSet));
	}

}
