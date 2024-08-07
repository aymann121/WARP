package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class GetNodeNamesOrderedAlphabeticallyTest {

	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testAlphabeticalOrderWithLettersAndIntegers() {
		String file = "SeeSpray.txt";
		WorkLoad workLoad = new WorkLoad(0.0, 0.0, file);
		String[] actual = workLoad.getNodeNamesOrderedAlphabetically();
		var nodes = workLoad.getNodes();
		Set<String> names = nodes.keySet();
		String[] namesToAlphabetize = names.toArray(new String[names.size()]);
		Arrays.sort(namesToAlphabetize);
		String[] expected = namesToAlphabetize;
		for(int i=0; i<actual.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}
	@Test
	@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
	public void testAlphabeticalOrderWithAllIntegerNames() {
		String file = "AllIntNames.txt";
		WorkLoad workLoad = new WorkLoad(0.0, 0.0, file);
		String[] actual = workLoad.getNodeNamesOrderedAlphabetically();
		
		var nodes = workLoad.getNodes();
		Set<String> names = nodes.keySet();
		String[] namesToOrderNumerically = names.toArray(new String[names.size()]);
		Arrays.sort(namesToOrderNumerically);
		String[] expected = namesToOrderNumerically;
		for(int i=0; i<actual.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}

}
