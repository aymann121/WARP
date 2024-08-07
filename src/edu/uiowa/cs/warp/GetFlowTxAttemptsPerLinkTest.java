package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.Math;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class GetFlowTxAttemptsPerLinkTest {

	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testF1StressTest4() {
		double m = .9;
		double e2e = .99;
		String flowName = "F1";
		String file = "StressTest4.txt";
		WorkLoad workLoad = new WorkLoad(m, e2e, file);
		int hops = workLoad.getNodesInFlow(flowName).length;
		Integer actual = workLoad.getFlowTxAttemptsPerLink(flowName);
		Integer expected = 3;
		assertEquals (expected, actual);
	}

	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testF9StressTest4() {
		String flowName = "F9";
		String file = "StressTest4.txt";
		WorkLoad workLoad = new WorkLoad(0.9, 0.99, file);
		Integer actual = workLoad.getFlowTxAttemptsPerLink(flowName);
		Integer expected = 3;
		assertEquals (expected, actual);
	}
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testF20ASeeSpray() {
		String flowName = "F10A";
		String file = "SeeSpray.txt";
		WorkLoad workLoad = new WorkLoad(0.9, 0.99, file);
		Integer actual = workLoad.getFlowTxAttemptsPerLink(flowName);
		Integer expected = 3;
		assertEquals (expected, actual);
	}


}
