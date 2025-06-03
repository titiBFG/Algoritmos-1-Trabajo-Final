package Principal.table;
public interface Table {
    
    int getRowCount();
    int getColumnCount();
    Row getRow(int index);
    Column getColumn(String label);
}
