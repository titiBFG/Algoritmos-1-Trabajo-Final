package io.interfaces;

import Principal.table.Table;

public interface TableWriter {
    void write(Table table, String filePath, String delimiter, boolean withHeader) throws Exception;
}
// TableWriter.java - clase o interfaz de io
