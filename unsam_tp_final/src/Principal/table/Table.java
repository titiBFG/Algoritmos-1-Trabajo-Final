package Principal.table;
public interface Table {
    
    int getRowCount();
    int getColumnCount();
    RowView getRowView(int index);
    Column getColumn(String label);
}
