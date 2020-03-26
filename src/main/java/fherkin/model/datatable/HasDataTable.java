package fherkin.model.datatable;

/**
 * Interface to identify a class that has a reference to a data table.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public interface HasDataTable {
	
	DataTable getDataTable();
	void setDataTable(DataTable dataTable);

}