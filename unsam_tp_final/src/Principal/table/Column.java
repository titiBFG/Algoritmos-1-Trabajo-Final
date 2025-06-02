package Principal.table;

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
}