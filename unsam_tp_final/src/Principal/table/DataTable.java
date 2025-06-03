package Principal.table;
// DataTable.java - clase o interfaz de core
import java.util.Map;
import java.util.Objects;
import java.util.LinkedHashMap;
import java.util.List;
import utils.enums.DataType;
import Principal.filter.Filter;


public class DataTable{

    private Map<Integer, Row> rows;
    private List<Column> columns;
    private Map<String, DataType> columnTypes;

    public DataTable(Map<Integer, Row> rows, List<Column> columns, Map<String, DataType> columnTypes) {
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

    public Object getValue(String columnName, int rowIndex) {
        Row row = rows.get(rowIndex);
        if (row == null) {
            throw new IllegalArgumentException("Fila no Encontrada: " + rowIndex);
        }
        return row.getValue(columnName);
    }

    public Row getRow(int row) {
        return rows.get(row);
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<String> getColumnLabels(){
        return columns.stream()
                    .map(Column::getLabel)
                    .toList();
    }

    public DataTable filter(Filter filter) {
        Objects.requireNonNull(filter, "filter no puede ser null");

        // 1) Preparo newRows vacío
        Map<Integer, Row> newRows = new LinkedHashMap<>();

        // 2) Itero todas las filas actuales
        for (Map.Entry<Integer, Row> entry : this.rows.entrySet()) {
            Integer rowId = entry.getKey();
            Row row = entry.getValue();

            // 3) Si el filtro se cumple, agrego la misma instancia de RowView.
            if (filter.apply(row)) {
                newRows.put(rowId, row);
            }
        }

        // 4) Construyo un nuevo DataTable con las mismas columnas, mismos tipos, pero solo filas filtradas
        return new DataTable(newRows, this.columns, this.columnTypes);
    }
}