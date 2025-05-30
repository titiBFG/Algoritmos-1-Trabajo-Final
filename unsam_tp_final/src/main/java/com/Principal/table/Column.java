// Column.java - clase o interfaz de core
package Principal.table;

import Utils.enums.DataType;

public class Column {

    private String label;
    private DataType type;

    public Column(String label, DataType type) {
        this.label = label;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public DataType getType() {
        return type;
    }
}