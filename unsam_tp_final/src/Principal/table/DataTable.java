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

    public DataTable sample(int n) {
        List<Integer> indices = new ArrayList<>(this.getRows().keySet());
        Collections.shuffle(indices);
        int sampleSize = Math.min(n, indices.size());
        LinkedHashMap<Integer, Row> sampledRows = new LinkedHashMap<>();
        for (int i = 0; i < sampleSize; i++) {
            Integer idx = indices.get(i);
            sampledRows.put(idx, this.getRows().get(idx));
        }
        return new DataTable(sampledRows, this.getColumns(), this.getColumnTypes());
    }

    public DataTable sample(double frac) {
        if (frac <= 0 || frac > 1) throw new IllegalArgumentException("frac debe estar entre 0 (exclusivo) y 1 (inclusivo)");
        int n = (int) Math.ceil(frac * this.getRowCount());
        return this.sample(n);
}
//creo tablas nuevas para head y tail, por que?
    public DataTable head(int n) {
        LinkedHashMap<Integer, Row> firstRows = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<Integer, Row> entry : rows.entrySet()) {
            if (count >= n) break;
            firstRows.put(entry.getKey(), entry.getValue());
            count++;
        }
        return new DataTable(firstRows, columns, columnTypes);
    }

    public DataTable tail(int n) {
        LinkedHashMap<Integer, Row> lastRows = new LinkedHashMap<>();
        List<Integer> keys = new ArrayList<>(rows.keySet());
        int start = Math.max(0, keys.size() - n);
        for (int i = start; i < keys.size(); i++) {
            lastRows.put(keys.get(i), rows.get(keys.get(i)));
        }
        return new DataTable(lastRows, columns, columnTypes);
    }

    public DataTable deepCopy() {
        // Copia profunda de filas
        LinkedHashMap<Integer, Row> newRows = new LinkedHashMap<>();
        for (Map.Entry<Integer, Row> entry : this.getRows().entrySet()) {
            newRows.put(entry.getKey(), new Row(entry.getValue()));
        }
        // Copia profunda de columnas
        List<Column> newColumns = new ArrayList<>();
        for (Column col : this.getColumns()) {
            newColumns.add(new Column(col));
        }
        // Copia de tipos de columna
        Map<String, DataType> newTypes = new LinkedHashMap<>(this.getColumnTypes());
        // Nueva instancia independiente
        return new DataTable(newRows, newColumns, newTypes);
    }

    public static DataTable concat(DataTable t1, DataTable t2) {
        // Validar columnas
        List<Column> cols1 = t1.getColumns();
        List<Column> cols2 = t2.getColumns();
        if (cols1.size() != cols2.size()) {
            throw new IllegalArgumentException("Cantidad de columnas distinta");
        }
        for (int i = 0; i < cols1.size(); i++) {
            if (!cols1.get(i).getLabel().equals(cols2.get(i).getLabel())) {
                throw new IllegalArgumentException("Las etiquetas de columna no coinciden en la posición " + i);
            }
        }
        // Validar tipos
        if (!t1.getColumnTypes().equals(t2.getColumnTypes())) {
            throw new IllegalArgumentException("Los tipos de columna no coinciden");
        }
        // Concatenar filas con nuevos índices
        LinkedHashMap<Integer, Row> newRows = new LinkedHashMap<>();
        int idx = 0;
        for (Row row : t1.getRows().values()) {
            newRows.put(idx, new Row(idx, row.getValues(), row.getColumnLabels()));
            idx++;
        }
        for (Row row : t2.getRows().values()) {
            newRows.put(idx, new Row(idx, row.getValues(), row.getColumnLabels()));
            idx++;
        }
        // Crear nueva DataTable
        return new DataTable(newRows, cols1, t1.getColumnTypes());
    }

    // Método para imputar valores en una columna específica
    public DataTable impute(String columnName, Object nuevoValor) {
        DataTable copia = this.deepCopy();
        for (Row row : copia.getRows().values()) {
            Object valor = row.getValue(columnName);
            if (valor != null && valor.toString().trim().equalsIgnoreCase("NA")) {
                row.setValue(columnName, nuevoValor);
            }
        }
        return copia;
    }


}
