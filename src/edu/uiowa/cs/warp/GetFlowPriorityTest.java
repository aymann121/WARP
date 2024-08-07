package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class GetFlowPriorityTest {

	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testFlowPriotityWithNewFlowOnlyFlowAsParameter() {
		String file = "StressTest4.txt";
		String flow = "F2";  // the flow for the comparison where we set a new flow priority
		String flow2 = "F1"; // the flow for the comparison to the pre-set priotity
		Integer newPriority = 27; // the new priority we are setting for flow
		WorkLoad workLoad = new WorkLoad(0.0, 0.0, file); //creating a new workload object
		Integer preSetPriority = 1;						//the value of the preset priority
														//is there a way to get this ??
		workLoad.setFlowPriority(flow, newPriority);
		Integer actual = workLoad.getFlowPriority(flow);
		assertEquals(27, actual);     	// Sets a new flow priority and compares the value we set it
										//to to what getFlowPriority returns
		
		assertEquals(preSetPriority, workLoad.getFlowPriority(flow2)); //Compares the priority value
																	   //automatically set for the
																	   // specified file and flow
																	   // to what the method returns
	}

}
