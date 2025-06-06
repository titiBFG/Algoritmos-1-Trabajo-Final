package Principal.table;

import java.util.ArrayList;

import utils.enums.DataType;

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

    public String getLabel(){
        return label;
    }
    // Un constructor de copia que cree una nueva columna a partir de otra.
    public Column(Column other) {
        this.label = other.label;
    }

}