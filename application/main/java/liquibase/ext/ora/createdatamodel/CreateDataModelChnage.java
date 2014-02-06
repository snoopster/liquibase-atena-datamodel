/**
 * 
 */
package liquibase.ext.ora.createdatamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.change.ChangeWithColumns;
import liquibase.change.ColumnConfig;
import liquibase.change.DatabaseChange;
import liquibase.change.DatabaseChangeProperty;
import liquibase.change.core.CreateTableChange;
import liquibase.database.Database;
import liquibase.ext.ora.addcheck.AddCheckChange;
import liquibase.statement.SqlStatement;

/**
 * @author Kamil Wysocki
 * 
 */
@DatabaseChange(name = "createModelTable", description = "Create data model", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateDataModelChnage extends AbstractChange implements
		ChangeWithColumns<ColumnConfig> {

	private String catalogName;
	private String schemaName;
	private String tableName;
	private List<ColumnConfig> columns;

	public CreateDataModelChnage() {
		columns = new ArrayList<ColumnConfig>();
		ColumnConfig colPm = new ColumnConfig();
		colPm.setName("pm");
		colPm.setRemarks("Klucz g³ówny");
		colPm.setType("number(12)");
		addColumn(colPm);
	}

	/**
	 * @return the catalogName
	 */
	public String getCatalogName() {
		return catalogName;
	}

	/**
	 * @param catalogName
	 *            the catalogName to set
	 */
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param schemaName
	 *            the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see liquibase.change.Change#getConfirmationMessage()
	 */
	@Override
	public String getConfirmationMessage() {
		return "Tabelka gotowa";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see liquibase.change.ChangeWithColumns#getColumns()
	 */
	@Override
	@DatabaseChangeProperty(requiredForDatabase = "all")
	public List<ColumnConfig> getColumns() {
		if (columns == null) {
			return new ArrayList<ColumnConfig>();
		}
		return columns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * liquibase.change.ChangeWithColumns#addColumn(liquibase.change.ColumnConfig
	 * )
	 */
	@Override
	public void addColumn(ColumnConfig column) {
		columns.add(column);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see liquibase.change.ChangeWithColumns#setColumns(java.util.List)
	 */
	@Override
	public void setColumns(List<ColumnConfig> columns) {
		this.columns = columns;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * liquibase.change.Change#generateStatements(liquibase.database.Database)
	 */
	@Override
	public SqlStatement[] generateStatements(Database database) {
		List<SqlStatement> dataModelStatement = new ArrayList<SqlStatement>();
		dataModelStatement.addAll(Arrays.asList(getTableDataModel(database)));
		dataModelStatement.addAll(Arrays.asList(getAddCheckStatement(database)));
		return dataModelStatement.toArray(new SqlStatement[dataModelStatement
				.size()]);
	}

	/**
	 * @param database
	 * @return
	 */
	private SqlStatement[] getTableDataModel(Database database) {
		CreateTableChange tableChange = new CreateTableChange();
		tableChange.setColumns(getColumns());
		tableChange.setTableName(getTableName());
		tableChange.setSchemaName(getSchemaName());
		SqlStatement[] statements = tableChange.generateStatements(database);
		return statements;
	}
	
	private SqlStatement[] getAddCheckStatement(Database database){
		AddCheckChange addCheck = new AddCheckChange();
		addCheck.setCondition("pm between 0 and 5");
		addCheck.setTableName(getTableName());
		return addCheck.generateStatements(database);
	}

}
