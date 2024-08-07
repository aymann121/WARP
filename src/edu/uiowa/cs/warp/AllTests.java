package edu.uiowa.cs.warp;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ AddFlowTest.class, AddNodeToFlowTest.class, GetFlowPriorityTest.class,
		GetFlowTxAttemptsPerLinkTest.class, GetNodeNamesOrderedAlphabeticallyTest.class,
		GetTotalTxAttemptsInFlowTest.class, SetFlowPriorityTest.class, SetFlowsInRMorderTest.class })
public class AllTests {

}
