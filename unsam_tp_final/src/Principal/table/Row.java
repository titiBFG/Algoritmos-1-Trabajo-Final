package Principal.table;

import java.util.ArrayList;
//import java.util.LinkedHashMap;
import java.util.List;

public class Row {
    private final int index;
    private final List<Object> values;
    private final List<String> columnLabels;

    public Row(int index, List<Object> values, List<String> columnLabels) {
        this.index = index;
        this.values = values;
        this.columnLabels = columnLabels;
    }

    public int getIndex() {
        return index;
    }

    public Object getValue(String columnName) {
        int columnIndex = columnLabels.indexOf(columnName);
        if (columnIndex < 0 || columnIndex >= values.size()) {
            throw new IllegalArgumentException("Columna no encontrada: " + columnName);
        }
        return values.get(columnIndex);
    }
    // Un constructor de copia que cree un nuevo Row a partir de otro.
    public Row(Row other) {
        this.index = other.index;
        this.values = new ArrayList<>(other.values); // Copia la lista de valores
        this.columnLabels = new ArrayList<>(other.columnLabels); // También copialo si es mutable
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{ ");
        for (int i = 0; i < columnLabels.size(); i++) {
            sb.append(columnLabels.get(i)).append("=").append(values.get(i));
            if (i < columnLabels.size() - 1) sb.append(", ");
        }
        sb.append(" }");
        return sb.toString();
    }
    // Método para establecer un nuevo valor en una columna específica
    public void setValue(String columnName, Object nuevoValor) {
        int colIdx = columnLabels.indexOf(columnName);
        if (colIdx == -1) {
            throw new IllegalArgumentException("Columna no encontrada: " + columnName);
        }
        values.set(colIdx, nuevoValor);
    }

    // Agregando métodos para obtener los valores y etiquetas de columna
    // de la fila, si es necesario.
    public List<Object> getValues() {
        return new ArrayList<>(values);
    }

    public List<String> getColumnLabels() {
        return new ArrayList<>(columnLabels);
    }
}
