package fherkin.io.impl;

import fherkin.io.GherkinSyntaxException;
import fherkin.model.datatable.DataTable;
import fherkin.model.datatable.DataTableCell;
import fherkin.model.datatable.DataTableRow;
import fherkin.model.entry.Examples;
import fherkin.model.entry.Feature;
import fherkin.model.entry.ScenarioOutline;
import fherkin.model.entry.Tag;
import fherkin.model.location.Location;
import java.util.ArrayList;
import java.util.SortedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for:  AbstractGherkinParser data tables
 * 
 * @author Mike Clemens
 */
public class AbstractGherkinParserDataTableTest extends BaseAbstractGherkinParserTestCase {

	private Log log = LogFactory.getLog(getClass());
	
	@Before
	public void before() {
		stack.clear();
		entries.clear();
	}
	
	@Test
	public void testDataTableNoParent() {
		try {
			processDataTable("|", "    ", "|", " ", "2", " ", "|", " ", "3", " ", "|", " ", "5", " ", "|", "    ");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testDataTableParentCannotHaveDataTable() {
		stack.push(new Feature());
		stack.push(new ScenarioOutline());
		try {
			processDataTable("|", "    ", "|", " ", "2", " ", "|", " ", "3", " ", "|", " ", "5", " ", "|", "    ");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}	
	}
	
	@Test
	public void testDataTableWithTrailingText() {
		stack.push(new Feature());
		stack.push(new ScenarioOutline());
		stack.push(new Examples());
		try {
			processDataTable("|", "    ", "|", " ", "2", " ", "|", " ", "3", " ", "|", " ", "5", " ", "|", "    ", "fail");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	@Test
	public void testNewDataTable() {
		Examples examples = new Examples();
		stack.push(new Feature());
		stack.push(new ScenarioOutline());
		stack.push(examples);
		processDataTable("|", "    ", "|", " ", "2", " ", "|", " ", "3", " ", "|", " ", "5", " ", "|", "    ");
		
		DataTable dataTable = (DataTable) stack.peek();
		Assert.assertEquals("data table", dataTable.getKeyword());
		Assert.assertEquals(1, dataTable.getRows().size());
		Assert.assertSame(examples, dataTable.getOwner());
		Assert.assertSame(dataTable, examples.getDataTable());
		
		DataTableRow row = dataTable.getRows().get(0);
		Assert.assertEquals("data table row", row.getKeyword());
		Assert.assertSame(dataTable.getLocation(), row.getLocation());
		Assert.assertSame(dataTable, row.getDataTable());
		Assert.assertEquals(3, row.getCells().size());
		
		DataTableCell cell;
		for(int i = 0; i < row.getCells().size(); i++) {
			cell = row.getCells().get(i);
			Assert.assertEquals("data table cell", cell.getKeyword());
			Assert.assertSame(row, cell.getRow());
			
			switch(i) {
				case 0:
					Assert.assertEquals("2", cell.getValue());
					break;
				case 1:
					Assert.assertEquals("3", cell.getValue());
					break;
				default:
					Assert.assertEquals("5", cell.getValue());
					break;
			}
		}
	}
	
	@Test
	public void testExistingDataTable() {
		Examples examples = new Examples();
		stack.push(new Feature());
		stack.push(new ScenarioOutline());
		stack.push(examples);
		processDataTable("|", "    ", "|", " ", "2", " ", "|", " ", "3", " ", "|", " ", "5", " ", "|", "    ");
		processDataTable("|", "    ", "|", " ", "3", " ", "|", " ", "5", " ", "|", " ", "8", " ", "|", "    ");
		
		DataTable dataTable = (DataTable) stack.peek();
		Assert.assertEquals("data table", dataTable.getKeyword());
		Assert.assertEquals(2, dataTable.getRows().size());
		Assert.assertSame(examples, dataTable.getOwner());
		Assert.assertSame(dataTable, examples.getDataTable());
		
		DataTableRow row;
		DataTableCell cell;
		for(int i = 0; i < dataTable.getRows().size(); i++) {
			row = dataTable.getRows().get(i);
			Assert.assertEquals("data table row", row.getKeyword());
			
			if(i == 0)
				Assert.assertSame(dataTable.getLocation(), row.getLocation());
			else
				Assert.assertNotSame(dataTable.getLocation(), row.getLocation());
			
			Assert.assertSame(dataTable, row.getDataTable());
			Assert.assertEquals(3, row.getCells().size());
			
			for(int j = 0; j < row.getCells().size(); j++) {
				cell = row.getCells().get(j);
				Assert.assertEquals("data table cell", cell.getKeyword());
				Assert.assertSame(row, cell.getRow());
				
				switch(i) {
					case 0:
						switch(j) {
							case 0:
								Assert.assertEquals("2", cell.getValue());
								break;
							case 1:
								Assert.assertEquals("3", cell.getValue());
								break;
							case 2:
								Assert.assertEquals("5", cell.getValue());
								break;
						}
						break;
					default:
						switch(j) {
							case 0:
								Assert.assertEquals("3", cell.getValue());
								break;
							case 1:
								Assert.assertEquals("5", cell.getValue());
								break;
							case 2:
								Assert.assertEquals("8", cell.getValue());
								break;
						}
				}
			}
		}
	}
	
	@Test
	public void testDataTableTags() {
		Tag tag = new Tag("oops");
		tag.setLocation(new Location() {
			@Override
			public String getLocation() {
				return "?: Line 41, column 1";
			}
		});
		
		tags = new ArrayList<Tag>();
		tags.add(tag);
		
		Examples examples = new Examples();
		stack.push(new Feature());
		stack.push(new ScenarioOutline());
		stack.push(examples);
		
		try {
			processDataTable("|", "    ", "|", " ", "2", " ", "|", " ", "3", " ", "|", " ", "5", " ", "|", "    ");
			Assert.fail("Expected GherkinSyntaxException");
		}
		catch(GherkinSyntaxException e) {
			log.debug(e.getMessage());
		}
	}
	
	protected void processDataTable(String keyword, String... tokens) {
		SortedMap<Location, String> map = createTextTokens(tokens);
		
		StringBuffer buffer = new StringBuffer();
		for(String token : tokens)
			buffer.append(token);
		
		processDataTable(map, buffer.toString().trim(), new Location() {
			@Override
			public String getLocation() {
				return "FakeFile.feature: Line 42, column 42";
			}
		}, null);
	}

}