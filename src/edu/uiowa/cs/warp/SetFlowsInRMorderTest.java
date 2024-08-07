package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class SetFlowsInRMorderTest {

	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void test() {
		String file = "StressTest4.txt";
		WorkLoad workLoad = new WorkLoad(0.0, 0.0, file);
		workLoad.setFlowsInRMorder();    						//should return the flows in priority order
		Collection<Flow> actual = workLoad.getFlows().values();
		workLoad.setFlowNamesInPriorityOrder(workLoad.getFlowNamesInOriginalOrder());
		Collection<Flow> expected = workLoad.getFlows().values();
		assertEquals(expected, actual);
	}

}
