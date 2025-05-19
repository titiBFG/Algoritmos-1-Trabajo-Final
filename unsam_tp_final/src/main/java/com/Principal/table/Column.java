// Column.java - clase o interfaz de core
package table;

import enums.DataType;

public class Column {

    private String label;
    private DataType type;

    public Column(String label, DataType type) {
        this.label = label;
        this.type = type;
    }

    public DataType getType() {
        return type;
    }
}