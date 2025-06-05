package Principal.table;

import java.util.*;
import java.util.stream.Collectors;
import Principal.filter.Filter;
import utils.enums.DataType;

public class DataTable implements Table {
    private final Map<Integer, Row> rows;
    private final List<Column> columns;
    private final Map<String, DataType> columnTypes;

    public DataTable(Map<Integer, Row> rows,
                     List<Column> columns,
                     Map<String, DataType> columnTypes) {
        Objects.requireNonNull(rows, "La fila no puede ser null");
        Objects.requireNonNull(columns, "La columna no puede ser null");
        Objects.requireNonNull(columnTypes, "columnTypes no puede ser null");
        for (Column col : columns) {
            if (!columnTypes.containsKey(col.getLabel())) {
                throw new IllegalArgumentException(
                    "columnTypes debe contener tipo para: " + col.getLabel());
            }
        }
        this.rows = new LinkedHashMap<>(rows);
        this.columns = List.copyOf(columns);
        this.columnTypes = Map.copyOf(columnTypes);
    }

    @Override
    public List<String> getColumnLabels() {
        return columns.stream()
                      .map(Column::getLabel)
                      .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValue(String columnName, int rowIndex) {
        validateColumnName(columnName);
        Row row = rows.get(rowIndex);
        if (row == null) {
            throw new IllegalArgumentException("Fila no encontrada: " + rowIndex);
        }
        return row.getValue(columnName);
    }

    @Override
    public Row getRow(int rowIndex) {
        Row row = rows.get(rowIndex);
        if (row == null) {
            throw new IllegalArgumentException("Fila no encontrada: " + rowIndex);
        }
        return row;
    }

    @Override
    public List<Column> getColumns() {
        return List.copyOf(columns);
    }

    @Override
    public Table filter(Filter filter) {
        Objects.requireNonNull(filter, "filter no puede ser null");
        Map<Integer, Row> newRows = new LinkedHashMap<>();
        for (var entry : this.rows.entrySet()) {
            Integer rowId = entry.getKey();
            Row fila = entry.getValue();
            if (filter.apply(fila)) {
                newRows.put(rowId, fila);
            }
        }
        return new DataTable(newRows, this.columns, this.columnTypes);
    }

    private void validateColumnName(String columnName) {
        boolean existe = columns.stream()
                                .anyMatch(col -> col.getLabel().equals(columnName));
        if (!existe) {
            throw new IllegalArgumentException(
                "La columna '" + columnName + "' no existe.");
        }
    }

    public Map<Integer, Row> getRows() {
    return rows;
}

    public java.util.Map<String, utils.enums.DataType> getColumnTypes() {
    return columnTypes;
}
}
