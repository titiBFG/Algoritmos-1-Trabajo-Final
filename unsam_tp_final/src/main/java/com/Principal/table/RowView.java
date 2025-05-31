// RowView.java - clase o interfaz de core
package Principal.table;

import java.util.Map; 

    public class RowView {
    private Map<String, Object> values;

    public RowView(Map<String, Object> values) {
        this.values = values;
    }
    
    public Map<String, Object> getValues() {
        return values;
    }
}