package Principal.table;

import java.util.*;
import java.util.stream.Collectors;
import Principal.filter.Filter;
import utils.enums.DataType;
import utils.validation.NA;

/**
 * Implementación de la interfaz Table que maneja datos tabulares con tipado fuerte.
 * Esta clase proporciona funcionalidades para manipular, filtrar y transformar datos tabulares.
 */
public class DataTable implements Table {
    private final Map<Integer, Row> rows;
    private final List<Column> columns;
    private final Map<String, DataType> columnTypes;

    /**
     * Constructor de DataTable.
     * @param rows Mapa de filas indexadas por su ID
     * @param columns Lista de columnas
     * @param columnTypes Mapa de tipos de datos por nombre de columna
     * @throws NullPointerException si algún parámetro es null
     * @throws IllegalArgumentException si columnTypes no contiene todos los tipos de columnas
     */
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
    public DataTable filter(Filter filter) {
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
    @Override
    public Map<Integer, Row> getRows() {
        return rows;
    }
    @Override
    public Map<String, DataType> getColumnTypes() {
        return columnTypes;
    }

    /**
     * Obtiene una muestra aleatoria de n filas de la tabla.
     * @param n Número de filas a muestrear
     * @return Nueva DataTable con n filas aleatorias
     * @throws IllegalArgumentException si n es negativo o mayor que el número total de filas
     */
    public DataTable sample(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El tamaño de la muestra no puede ser negativo");
        }
        if (n > this.getRowCount()) {
            throw new IllegalArgumentException("El tamaño de la muestra no puede ser mayor que el número total de filas");
        }
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

    /**
     * Obtiene una muestra aleatoria de un porcentaje de filas de la tabla.
     * @param frac Fracción de filas a muestrear (entre 0 y 1)
     * @return Nueva DataTable con el porcentaje especificado de filas aleatorias
     * @throws IllegalArgumentException si frac no está entre 0 (exclusivo) y 1 (inclusivo)
     */
    public DataTable sample(double frac) {
        if (frac <= 0 || frac > 1) throw new IllegalArgumentException("frac debe estar entre 0 (exclusivo) y 1 (inclusivo)");
        int n = (int) Math.ceil(frac * this.getRowCount());
        return this.sample(n);
    }
    /**
     * === OPERACIONES BÁSICAS DE ACCESO ===
     */
    public void setAt(int rowIndex, String columnName, Object value) {
        validateColumnName(columnName);
        Row row = rows.get(rowIndex);
        if (row == null) {
            throw new IllegalArgumentException("Fila no encontrada: " + rowIndex);
        }

        DataType expectedType = columnTypes.get(columnName);
        Object safeValue;

        if (value.equals(NA.INSTANCE)) {
            safeValue = NA.INSTANCE;
        } else {
            // Usamos el parser de DataType para convertir el valor
            try {
                safeValue = expectedType.parse(value.toString());
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "El valor '" + value + "' no es válido para el tipo " + expectedType);
            }
        }

        row.setValue(columnName, safeValue);
    }
    /**
     * Añade una nueva columna.
     * @param columnName Nombre de la nueva columna
     * @param type Tipo de datos de la nueva columna
     * @param values Lista de valores (debe coincidir con el número de filas)
     * @return Nueva DataTable con la columna añadida
     * @throws IllegalArgumentException si el tamaño no coincide o el nombre ya existe
     */
    public DataTable addColumn(String columnName, DataType type, List<Object> values) {
        // Validaciones
        if (columnName == null || type == null || values == null) {
            throw new NullPointerException("Ningún parámetro puede ser null");
        }
        if (values.size() != rows.size()) {
            throw new IllegalArgumentException(
                    "El número de valores (" + values.size() +
                            ") no coincide con el número de filas (" + rows.size() + ")");
        }
        if (columnTypes.containsKey(columnName)) {
            throw new IllegalArgumentException("La columna " + columnName + " ya existe");
        }

        // Crear nueva estructura de columnas
        List<Column> newColumns = new ArrayList<>(this.columns);
        newColumns.add(new Column(columnName, type));

        // Crear nuevos tipos de columna
        Map<String, DataType> newColumnTypes = new LinkedHashMap<>(this.columnTypes);
        newColumnTypes.put(columnName, type);

        Map<Integer, Row> newRows = new LinkedHashMap<>();
        int valueIndex = 0;

        for (Map.Entry<Integer, Row> entry : rows.entrySet()) {
            int rowIndex = entry.getKey();
            Row oldRow = entry.getValue();

            List<Object> newValues = new ArrayList<>(oldRow.getValues());
            Object rawValue = values.get(valueIndex++);
            Object value;

            if (rawValue.equals(NA.INSTANCE)) {
                value = NA.INSTANCE;
            } else {
                try {
                    value = type.parse(rawValue.toString());
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "Valor inválido en posición " + (valueIndex-1) +
                                    " para tipo " + type + ": " + rawValue);
                }
            }

            newValues.add(value);
            List<String> newLabels = new ArrayList<>(oldRow.getColumnLabels());
            newLabels.add(columnName);

            newRows.put(rowIndex, new Row(rowIndex, newValues, newLabels));
        }

        return new DataTable(newRows, newColumns, newColumnTypes);
    }
    /**
     * Elimina una fila por su índice .
     * @param rowIndex Índice de la fila a eliminar
     * @return Nueva DataTable sin la fila especificada
     * @throws IllegalArgumentException si la fila no existe
     */
    public DataTable dropRow(int rowIndex) {
        if (!rows.containsKey(rowIndex)) {
            throw new IllegalArgumentException("Fila no encontrada: " + rowIndex);
        }

        // Crear nuevo mapa de filas sin la especificada
        Map<Integer, Row> newRows = new LinkedHashMap<>();
        for (Map.Entry<Integer, Row> entry : rows.entrySet()) {
            if (entry.getKey() != rowIndex) {
                newRows.put(entry.getKey(), new Row(entry.getValue()));
            }
        }

        return new DataTable(newRows, this.columns, this.columnTypes);
    }
    /**
     * Elimina una columna de la tabla, manteniendo todas las filas pero sin los valores de la columna especificada.
     * @param columnName Nombre de la columna a eliminar
     * @return Nueva DataTable sin la columna especificada
     * @throws IllegalArgumentException si la columna no existe
     * @throws NullPointerException si columnName es null
     */
    public DataTable dropColumn(String columnName) {
        Objects.requireNonNull(columnName, "La columna tiene que tener un nombre");
        validateColumnName(columnName);

        // 1. Crear nueva lista de columnas (excluyendo la que se elimina)
        List<Column> newColumns = this.columns.stream()
                .filter(col -> !col.getLabel().equals(columnName))
                .map(Column::new) // Usar constructor de copia
                .collect(Collectors.toList());

        // 2. Crear nuevos tipos de columna
        Map<String, DataType> newColumnTypes = new LinkedHashMap<>(this.columnTypes);
        newColumnTypes.remove(columnName);

        // 3. Reconstruir filas excluyendo la columna eliminada
        Map<Integer, Row> newRows = new LinkedHashMap<>();

        for (Map.Entry<Integer, Row> entry : this.rows.entrySet()) {
            int rowIndex = entry.getKey();
            Row oldRow = entry.getValue();

            List<Object> newValues = new ArrayList<>();
            List<String> newLabels = new ArrayList<>();

            // Filtrar valores y etiquetas
            for (int i = 0; i < oldRow.getColumnLabels().size(); i++) {
                String currentLabel = oldRow.getColumnLabels().get(i);
                if (!currentLabel.equals(columnName)) {
                    newLabels.add(currentLabel);
                    Object value = oldRow.getValues().get(i);
                    // Preservar NA.INSTANCE si existe
                    newValues.add(value instanceof NA ? NA.INSTANCE : value);
                }
            }

            newRows.put(rowIndex, new Row(rowIndex, newValues, newLabels));
        }

        return new DataTable(newRows, newColumns, newColumnTypes);
    }
    /**
     * Obtiene las primeras n filas de la tabla.
     * @param n Número de filas a obtener
     * @return Nueva DataTable con las primeras n filas
     * @throws IllegalArgumentException si n es negativo
     */
    public DataTable head(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El número de filas no puede ser negativo");
        }
        LinkedHashMap<Integer, Row> firstRows = new LinkedHashMap<>();
        int count = 0;
        for (var entry : rows.entrySet()) {
            if (count++ >= n) break;
            firstRows.put(entry.getKey(), entry.getValue());
        }
        return new DataTable(firstRows, columns, columnTypes);
    }

    /**
     * Obtiene las últimas n filas de la tabla.
     * @param n Número de filas a obtener
     * @return Nueva DataTable con las últimas n filas
     * @throws IllegalArgumentException si n es negativo
     */
    public DataTable tail(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El número de filas no puede ser negativo");
        }
        List<Integer> keys = new ArrayList<>(rows.keySet());
        int start = Math.max(0, keys.size() - n);
        LinkedHashMap<Integer, Row> lastRows = new LinkedHashMap<>();
        for (int i = start; i < keys.size(); i++) {
            Integer idx = keys.get(i);
            lastRows.put(idx, rows.get(idx));
        }
        return new DataTable(lastRows, columns, columnTypes);
    }
    /**
     * Muestra las filas desde un indice hasta otro
     * Si los índices son inválidos, lanza excepción.
     */
    public void slice(int indice1, int indice2) {
        if (indice1 < 0 || indice2 > rows.size() || indice1 > indice2) {
            throw new IllegalArgumentException("Índices fuera de rango o mal ordenados");
        }

        LinkedHashMap<Integer, Row> slicedRows = new LinkedHashMap<>();
        int count = 0;

        for (Map.Entry<Integer, Row> entry : rows.entrySet()) {
            if (count >= indice1 && count < indice2) {
                slicedRows.put(entry.getKey(), entry.getValue());
            }
            count++;
            if (count >= indice2) break;
        }

        DataTable t = new DataTable(slicedRows, columns, columnTypes);
        TableView vista = new TableView(t);
        vista.printAllRows();  // o printProlijo()
    }

    /**
     * Crea una copia profunda de la tabla original.
     * @param original Tabla a copiar
     * @return Nueva DataTable con una copia profunda de todos los datos
     * @throws NullPointerException si original es null
     */
    @Override
    public DataTable deepCopy(Table original) {
        Objects.requireNonNull(original, "original no puede ser null");

        // Clonamos esquema (Column y tipos) 
        List<Column> cols = original.getColumns().stream()
            .map(Column::new)  // usa tu constructor de copia completo
            .collect(Collectors.toList());
        Map<String, DataType> colTypes = new LinkedHashMap<>(original.getColumnTypes());

        // Clonamos filas
        Map<Integer, Row> rowsCopy = new LinkedHashMap<>();
        for (Map.Entry<Integer, Row> e : original.getRows().entrySet()) {
            Row cloned = new Row(e.getValue());  // construtor copia de Row
            rowsCopy.put(e.getKey(), cloned);
        }

        return new DataTable(rowsCopy, cols, colTypes);
    }

    /**
     * Crea una nueva DataTable a partir de datos en formato 2D.
     * @param data Matriz de datos
     * @param labels Nombres de las columnas
     * @param types Tipos de datos de las columnas
     * @return Nueva DataTable con los datos proporcionados
     * @throws NullPointerException si algún parámetro es null
     * @throws IllegalArgumentException si las dimensiones no coinciden
     */
    @Override
    public DataTable from2D(
            Object[][] data,
            String[] labels,
            DataType[] types) {

        // Validaciones básicas
        if (data == null || labels == null || types == null)
            throw new NullPointerException("data, labels y types no pueden ser null");
        if (labels.length != types.length)
            throw new IllegalArgumentException("labels y types deben tener misma longitud");

        // Construcción del esquema
        List<Column> cols = new ArrayList<>();
        Map<String, DataType> colTypes = new LinkedHashMap<>();
        for (int i = 0; i < labels.length; i++) {
            cols.add(new Column(labels[i], types[i]));
            colTypes.put(labels[i], types[i]);
        }

        // Ensamblaje de filas
        Map<Integer, Row> rows = new LinkedHashMap<>();
        for (int i = 0; i < data.length; i++) {
            if (data[i].length != labels.length)
                throw new IllegalArgumentException(
                    "La fila " + i + " no coincide en tamaño con labels");
            List<Object> vals = Arrays.asList(data[i]);
            Row row = new Row(i, vals, Arrays.asList(labels));
            rows.put(i, row);
        }

        return new DataTable(rows, cols, colTypes);
    }

    /**
     * Crea una nueva DataTable a partir de un iterable de listas.
     * @param iterable Iterable de listas de valores
     * @param labels Nombres de las columnas
     * @param types Tipos de datos de las columnas
     * @return Nueva DataTable con los datos proporcionados
     * @throws NullPointerException si algún parámetro es null
     * @throws IllegalArgumentException si las dimensiones no coinciden
     */
    @Override
    public DataTable fromIterable(
            Iterable<List<Object>> iterable,
            List<String> labels,
            List<DataType> types) {

        if (iterable == null || labels == null || types == null)
            throw new NullPointerException("Ningún parámetro puede ser null");
        if (labels.size() != types.size())
            throw new IllegalArgumentException("labels y types deben coincidir");

        // Esquema
        List<Column> cols = new ArrayList<>();
        Map<String, DataType> colTypes = new LinkedHashMap<>();
        for (int i = 0; i < labels.size(); i++) {
            cols.add(new Column(labels.get(i), types.get(i)));
            colTypes.put(labels.get(i), types.get(i));
        }

        // Filas
        Map<Integer, Row> rows = new LinkedHashMap<>();
        int idx = 0;
        for (List<Object> vals : iterable) {
            if (vals.size() != labels.size())
                throw new IllegalArgumentException(
                    "La fila en posición " + idx + " tiene longitud incorrecta");
            Row row = new Row(idx, vals, labels);
            rows.put(idx++, row);
        }

        return new DataTable(rows, cols, colTypes);
    }

    /**
     * Concatena esta tabla con otra tabla.
     * @param otra Tabla a concatenar
     * @return Nueva DataTable con las filas de ambas tablas
     * @throws NullPointerException si otra es null
     * @throws IllegalArgumentException si las tablas tienen diferentes esquemas
     */
    @Override
    public DataTable concat(Table otra) {
        // 1) Validaciones
        Objects.requireNonNull(otra, "otra no puede ser null");
        if (!(otra instanceof DataTable)) {
            throw new IllegalArgumentException(
                "Sólo puedo concatenar con otra instancia de DataTable");
        }
        DataTable tablaB = (DataTable) otra;   // casteo seguro
        DataTable tablaA = this;               // la tabla sobre la que invocamos

        // 2) Verificar mismo esquema
        List<Column> colsA = tablaA.getColumns();
        List<Column> colsB = tablaB.getColumns();
        if (colsA.size() != colsB.size()) {
            throw new IllegalArgumentException(
                "Diferente número de columnas: " 
                + colsA.size() + " vs " + colsB.size());
        }
        Map<String, DataType> typesA = tablaA.getColumnTypes();
        Map<String, DataType> typesB = tablaB.getColumnTypes();
        for (int i = 0; i < colsA.size(); i++) {
            String labelA = colsA.get(i).getLabel();
            String labelB = colsB.get(i).getLabel();
            if (!labelA.equals(labelB) 
                || !typesA.get(labelA).equals(typesB.get(labelB))) {
                throw new IllegalArgumentException(
                    "Esquema distinto en columna " + i + ": "
                    + labelA + "/" + typesA.get(labelA)
                    + " vs " + labelB + "/" + typesB.get(labelB));
            }
        }

        // 3) Clonar esquema
        List<Column> newCols = colsA.stream()
                                    .map(Column::new)
                                    .collect(Collectors.toList());
        Map<String, DataType> newTypes = new LinkedHashMap<>(typesA);

        // 4) Concatenar filas con reindexado
        Map<Integer, Row> newRows = new LinkedHashMap<>();
        int idx = 0;
        for (Row row : tablaA.getRows().values()) {
            newRows.put(idx, new Row(idx, row.getValues(), row.getColumnLabels()));
            idx++;
        }
        for (Row row : tablaB.getRows().values()) {
            newRows.put(idx, new Row(idx, row.getValues(), row.getColumnLabels()));
            idx++;
        }

        return new DataTable(newRows, newCols, newTypes);
    }

    @Override
    public DataTable impute(String columnName, Object newValue) {
        // 1) Validaciones
        validateColumnName(columnName);
        Objects.requireNonNull(newValue, "El valor de imputación no puede ser null");

        DataType expectedType = columnTypes.get(columnName);
        Object safeValue = castToType(newValue, expectedType);

        // 2) Índice de la columna a imputar
        int colIdx = -1;
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getLabel().equals(columnName)) {
                colIdx = i;
                break;
            }
        }

        // 3) Reconstruir filas, reemplazando solo las celdas NA.INSTANCE
        Map<Integer, Row> newRows = new LinkedHashMap<>();
        for (var entry : this.rows.entrySet()) {
            int idx = entry.getKey();
            Row oldRow = entry.getValue();
            Object cell = oldRow.getValue(columnName);

            if (cell == NA.INSTANCE) {
                // Clonamos valores y reemplazamos NA
                List<Object> vals = oldRow.getValues();  // copia defensiva
                vals.set(colIdx, safeValue);
                newRows.put(idx, new Row(idx, vals, oldRow.getColumnLabels()));
            } else {
                // Copia profunda de la fila original
                newRows.put(idx, new Row(oldRow));
            }
        }

        // 4) Devolver nueva tabla (preserva índices originales)
        return new DataTable(newRows, this.columns, this.columnTypes);
    }

    /** 
     * Intenta convertir `value` al tipo `dt`, o lanza IllegalArgumentException.
     */
    private Object castToType(Object value, DataType dt) {
        switch (dt) {
            case INTEGER:
                if (value instanceof Number) return ((Number) value).intValue();
                return Integer.parseInt(value.toString());
            case FLOAT:
                if (value instanceof Number) return ((Number) value).floatValue();
                return Float.parseFloat(value.toString());
            case DOUBLE:
                if (value instanceof Number) return ((Number) value).doubleValue();
                return Double.parseDouble(value.toString());
            case STRING:
                return value.toString();
            case BOOLEAN:
                if (value instanceof Boolean) return value;
                return Boolean.parseBoolean(value.toString());
            default:
                throw new IllegalArgumentException("Tipo desconocido: " + dt);
        }
    }
}
