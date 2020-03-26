package fherkin.model.entry;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for:  Scenario
 * 
 * @author Mike Clemens
 */
public class ScenarioTest extends Scenario {
	
	@Test
	public void testGetMaxKeywordLength() {
		Step step;
		StringBuffer buffer;
		int j;
		for(int i = 0; i < 10; i++) {
			buffer = new StringBuffer();
			for(j = 0; j < (i % 2 == 0 ? 1 : i); j++)
				buffer.append("X");
			
			step = new Step();
			step.setKeyword(buffer.toString());
			step.setScenario(this);
			addStep(step);
		}
		
		Assert.assertEquals(9, getMaxKeywordLength());
	}

}