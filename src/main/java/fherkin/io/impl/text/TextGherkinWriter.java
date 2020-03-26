package fherkin.io.impl.text;

import fherkin.io.impl.AbstractGherkinWriter;
import fherkin.model.GherkinEntry;
import fherkin.model.datatable.DataTable;
import fherkin.model.datatable.DataTable.CellType;
import fherkin.model.datatable.DataTableCell;
import fherkin.model.datatable.DataTableRow;
import fherkin.model.datatable.HasDataTable;
import fherkin.model.entry.Comment;
import fherkin.model.entry.Examples;
import fherkin.model.entry.Feature;
import fherkin.model.entry.Scenario;
import fherkin.model.entry.Step;
import fherkin.model.entry.Tag;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

/**
 * Gherkin writer that writes to a text feature file.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class TextGherkinWriter extends AbstractGherkinWriter {
	
	protected PrintWriter out;
	
	public TextGherkinWriter(File file) throws IOException {
		out = new PrintWriter(file);
	}
	
	public TextGherkinWriter(File file, Charset charset) throws IOException {
		out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
	}

	public TextGherkinWriter(OutputStream outputStream) throws IOException {
		out = new PrintWriter(new OutputStreamWriter(outputStream));
	}
	
	public TextGherkinWriter(OutputStream outputStream, Charset charset) throws IOException {
		out = new PrintWriter(new OutputStreamWriter(outputStream, charset));
	}
	
	protected TextGherkinWriter() {
		// for unit testing only
	}

	@Override
	public void write(List<GherkinEntry> entries) {
		for(GherkinEntry entry : entries) {
			if(entry instanceof Feature)
				writeFeature((Feature) entry);
			else
			if(entry instanceof Scenario)
				writeScenario((Scenario) entry);
			else
			if(entry instanceof Step)
				writeStep((Step) entry);
			else
			if(entry instanceof Examples)
				writeExamples((Examples) entry);
			else
			if(entry instanceof DataTableRow)
				writeDataTableRow((DataTableRow) entry);
			else
			if(entry instanceof Comment)
				writeComment((Comment) entry);
			
			out.println();
		}
		
		out.close();
	}
	
	protected void writeFeature(Feature feature) {
		// if the feature has any tags, write them prior to the feature line
		if(feature.getTags() != null && feature.getTags().size() > 0)
			for(Tag tag : feature.getTags())
				writeTag(tag, 0);
		
		// write the first feature line
		out.print(feature.getKeyword());
		out.print(": ");
		out.print(feature.getText().trim());
		
		if(feature.getComment() != null) {
			out.print("  #");
			out.print(feature.getComment().getText());
		}
		
		// write the additional text lines
		if(feature.getAdditionalText() != null && feature.getAdditionalText().size() > 0) {
			int indent = feature.getKeyword().length() + ": ".length();
			for(String text : feature.getAdditionalText()) {
				out.println();
				writeIndent(indent);
				out.print(text);
			}
		}
	}
	
	protected void writeScenario(Scenario scenario) {
		out.println();
		
		// if the scenario has any tags, write them prior to the feature line
		if(scenario.getTags() != null && scenario.getTags().size() > 0)
			for(Tag tag : scenario.getTags())
				writeTag(tag, 2);
		
		// write the scenario line
		writeIndent(2);
		out.print(scenario.getKeyword());
		out.print(": ");
		out.print(scenario.getText().trim());
		
		if(scenario.getComment() != null) {
			out.print("  #");
			out.print(scenario.getComment().getText());
		}
	}
	
	protected void writeStep(Step step) {
		writeIndent(4 + step.getScenario().getMaxKeywordLength() - step.getKeyword().length());
		out.print(step.getKeyword());
		out.print(" ");
		out.print(step.getText().trim());
		
		if(step.getComment() != null) {
			out.print("  #");
			out.print(step.getComment().getText());
		}
	}
	
	protected void writeExamples(Examples examples) {
		writeIndent(4);
		out.print(examples.getKeyword());
		out.print(":");
		
		if(examples.getComment() != null) {
			out.print("  #");
			out.print(examples.getComment().getText());
		}
	}
	
	protected void writeDataTableRow(DataTableRow row) {
		DataTable dataTable = row.getDataTable();
		HasDataTable owner = dataTable.getOwner();
		
		// determine the indent based on the parent
		int indent;
		if(owner instanceof Step)
			indent = 5 + ((Step) owner).getScenario().getMaxKeywordLength();
		else
			indent = 6;
		
		for(int i = 0; i < indent; i++)
			out.print(" ");
		
		// print the data table row
		Iterator<DataTableCell> iterator = row.getCells().iterator();
		DataTableCell cell;
		String value;
		CellType cellType;
		int columnLength;
		int j;
		int tableWidth = dataTable.getWidth();
		for(int i = 0; i < tableWidth; i++) {
			cell = iterator.hasNext() ? iterator.next() : null;
			value = cell == null || cell.getValue() == null ? "" : cell.getValue().trim();
			cellType = dataTable.getColumnType(i);
			columnLength = dataTable.getColumnLength(i);
			
			out.print("| ");
			if(cellType == CellType.INTEGER || cellType == CellType.FLOAT)
				for(j = value.length(); j < columnLength; j++)
					out.print(" ");
			out.print(value);
			if(cellType != CellType.INTEGER && cellType != CellType.FLOAT)
				for(j = value.length(); j < columnLength; j++)
					out.print(" ");
			out.print(" ");
		}
		
		out.print("|");
		
		if(row.getComment() != null) {
			out.print("  #");
			out.print(row.getComment().getText());
		}
	}
	
	protected void writeComment(Comment comment) {
		out.print("#");
		out.print(comment.getText());
	}

	protected void writeTag(Tag tag, int indent) {
		writeIndent(indent);
		out.print("@");
		out.print(tag.getTag());
		
		if(tag.getComment() != null) {
			out.print("  # ");
			out.print(tag.getComment().getText());
		}
		
		out.println();
	}
	
	protected void writeIndent(int indent) {
		for(int i = 0; i < indent; i++)
			out.print(" ");
	}
	
}