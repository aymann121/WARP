package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class AddNodeToFlowTest {
	

	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testAddingANodeToPrexistingFlow() {
		String file = "StressTest.txt";
		String flow = "F6";
		WorkLoad workLoad = new WorkLoad(0.0, 0.0, file);
		String nodeToBeAdded = "six";
		workLoad.addNodeToFlow(flow,nodeToBeAdded);
		assertEquals(true , workLoad.getNodes().containsKey(nodeToBeAdded));
		}
		
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testAddingANodeThatAlreadyExists() {
		String file = "StressTest4.txt";
		String flow = "F1";
		WorkLoad workLoad = new WorkLoad(0.0, 0.0, file);
		String nodeToBeAdded = "B";
		workLoad.addNodeToFlow(flow,nodeToBeAdded);
		assertEquals(true , workLoad.getNodes().containsKey(nodeToBeAdded));
		}

}
