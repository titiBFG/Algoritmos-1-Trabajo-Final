// DataTable.java - clase o interfaz de core

package table;

import java.util.Map;
import java.util.List;

import enums.DataType;

public class DataTable {

    private Map<Integer, RowView> rows;
    private List<Column> columns;
    private Map<String, DataType> columnTypes;

    public DataTable(Map<Integer, RowView> rows, List<Column> columns, Map<String, DataType> columnTypes) {
        this.rows = rows;
        this.columns = columns;
        this.columnTypes = columnTypes;
    }



}