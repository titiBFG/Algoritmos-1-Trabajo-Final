// RowView.java - clase o interfaz de core
package Principal.table;

import java.util.Map; 

public class RowView {

    private int index;
    private Map<String, Object> values;

    public RowView(int index, Map<String, Object> values) {
        this.index = index;
        this.values = values;
    }

    public int getIndex() {
        return index;
    }

    public Map<String, Object> getValues() {
        return values;
    }
}