// DataTable.java - clase o interfaz de core

package Principal.table;

import java.util.Map;

import Utils.enums.DataType;

public class DataTable {

    private Map<Integer, RowView> rows;
    private Map<String, Column> columns;
    private Map<String, DataType> columnTypes;

    public DataTable(Map<Integer, RowView> rows, Map<String, Column> columns, Map<String, DataType> columnTypes) {
        this.rows = rows;
        this.columns = columns;
        this.columnTypes = columnTypes;
    }

    public void showRowCount() {
        System.out.println("Row count: " + rows.size());
    }

    public void showColumnCount() {
        System.out.println("Column count: " + columns.size());
    }

    public void showColumnTypes() {
        columnTypes.forEach((nombre, type) -> {
                System.out.println(nombre + ": " + type);
            });
    }

    public Object getValue(int row, String col) {
        // Agarro el objeto RowView de indice "row" y le extraigo el objeto de key "col"
        Map<String, Object>  r = rows.get(row).getValues();
        return r.get(col);
    }
}