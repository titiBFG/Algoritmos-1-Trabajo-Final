package Principal.table;

import java.util.List;
import Principal.filter.Filter;

public interface Table {
    List<String> getColumnLabels();
    int getRowCount();
    int getColumnCount();
    Object getValue(String columnName, int rowIndex);
    Row getRow(int rowIndex);
    List<Column> getColumns();
    Table filter(Filter filter);
}
