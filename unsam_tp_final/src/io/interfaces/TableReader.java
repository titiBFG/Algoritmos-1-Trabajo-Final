// TableReader.java - clase o interfaz de io
package io.interfaces;
import Principal.table.DataTable;
import java.io.IOException;

public interface TableReader {
    public DataTable read(String filePath, String delimiter) throws IOException;
}