package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;


public class GetTotalTxAttemptsInFlowTest {

	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testStressTest4() {
		String file = "StressTest4.txt";
		String flow = "F1";
		WorkLoad workLoad = new WorkLoad(0.9, 0.99, file);
		Integer actual = workLoad.getTotalTxAttemptsInFlow(flow);
		Integer expected  = 4;
		assertEquals(expected , actual);
	}
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testSeeSpray() {
		String file = "SeeSpray.txt";
		String flow = "F20A";
		WorkLoad workLoad = new WorkLoad(0.9, 0.99, file);
		Integer actual = workLoad.getTotalTxAttemptsInFlow(flow);
		Integer expected  = 2;
		assertEquals(expected , actual);
	}


}
