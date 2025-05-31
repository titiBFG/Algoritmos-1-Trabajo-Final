// TableReader.java - clase o interfaz de io
package interfaces;

import java.io.IOException;

import Principal.table.DataTable;

public interface TableReader {
    
    /**
     * Reads a table from a file and returns it as a DataTable object.
     *
     * @param filePath the path to the file to read
     * @param delimiter the delimiter used in the file
     * @return a DataTable object containing the data from the file
     * @throws IOException if an I/O error occurs
     */
    DataTable read(String filePath, String delimiter, boolean hasHeaders) throws IOException;
}