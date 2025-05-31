// DataTable.java - clase o interfaz de core

package Principal.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.enums.DataType;

public class DataTable {

    private Map<Integer, RowView> rows;
    private Map<String, Column> columns;
    private Map<String, DataType> columnTypes;

    public DataTable() {
        this.rows = new HashMap<>();
        this.columns = new HashMap<>();
        this.columnTypes = new HashMap<>();
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

    public void addColumn(String trim, DataType dataType) {
        // Agrego una nueva columna al DataTable
        Column column = new Column(dataType);
        columns.put(trim, column);
        columnTypes.put(trim, dataType);
    }

    public void addRow(List<Object> fila) {
        // Agrego una nueva fila al DataTable
        Map<String, Object> rowValues = new HashMap<>();
        int rowIndex = rows.size();
        rowIndex++;
        columns.forEach((name, column) -> {
            int i = 0;
            rowValues.put(name, fila.get(i));
            i++;
        });
        RowView rowView = new RowView(rowValues);
        rows.put(rowIndex, rowView);
    }
}