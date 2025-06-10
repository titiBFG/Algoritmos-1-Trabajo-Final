package Principal.table;

import java.util.List;
import java.util.Map;
import Principal.filter.Filter;
import utils.enums.DataType;

public interface Table {
    List<String> getColumnLabels();
    int getRowCount();
    int getColumnCount();
    Object getValue(String columnName, int rowIndex);
    Row getRow(int rowIndex);
    Map<Integer, Row> getRows();
    List<Column> getColumns();
    Map<String, DataType> getColumnTypes();
    DataTable filter(Filter filter);
    DataTable deepCopy(Table dataTable);
    DataTable from2D(Object[][] data, String[] labels, DataType[] types); 
    DataTable fromIterable(Iterable<List<Object>> iterable, List<String> labels, List<DataType> types);
    DataTable concat(Table tablaB);
    DataTable impute(String columnName, Object newValue);
}

