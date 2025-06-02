package Principal.table;

import java.util.List;

public class RowView {
    private final int index;
    private final List<Object> values;
    private final List<String> columnLabels;

    public RowView(int index, List<Object> values, List<String> columnLabels) {
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

    public String toString() {
        StringBuilder sb = new StringBuilder("{ ");
        for (int i = 0; i < columnLabels.size(); i++) {
            sb.append(columnLabels.get(i)).append("=").append(values.get(i));
            if (i < columnLabels.size() - 1) sb.append(", ");
        }
        sb.append(" }");
        return sb.toString();
    }
}
