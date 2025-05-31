// Column.java - clase o interfaz de core
package Principal.table;

import Utils.enums.DataType;

public class Column {
    private DataType type;

    public Column(DataType type) {
        this.type = type;
    }

    public DataType getType() {
        return type;
    }
}