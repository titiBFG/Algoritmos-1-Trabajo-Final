package io;

import io.interfaces.TableWriter;
import Principal.table.Table;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter implements TableWriter {

    @Override
    public void write(Table table, String filePath, String delimiter, boolean withHeader) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            List<String> columns = table.getColumnLabels();

            // Escribo encabezado si corresponde
            if (withHeader) {
                writer.write(String.join(delimiter, columns));
                writer.newLine();
            }

            // Escribo todas las filas existentes
            if (table instanceof Principal.table.DataTable) {
                for (Integer rowIndex : ((Principal.table.DataTable) table).getRows().keySet()) {
                    StringBuilder line = new StringBuilder();
                    for (int c = 0; c < columns.size(); c++) {
                        Object val = table.getValue(columns.get(c), rowIndex);
                        line.append(val == null ? "" : val.toString());
                        if (c < columns.size() - 1) line.append(delimiter);
                    }
                    writer.write(line.toString());
                    writer.newLine();
                }
            } else {
                // fallback: por cantidad de filas
                for (int r = 0; r < table.getRowCount(); r++) {
                    StringBuilder line = new StringBuilder();
                    for (int c = 0; c < columns.size(); c++) {
                        Object val = table.getValue(columns.get(c), r);
                        line.append(val == null ? "" : val.toString());
                        if (c < columns.size() - 1) line.append(delimiter);
                    }
                    writer.write(line.toString());
                    writer.newLine();
                }
            }
        }
    }
}
// CsvWriter.java - clase o interfaz de io
