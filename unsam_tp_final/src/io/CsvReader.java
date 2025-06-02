// CsvReader.java - clase o interfaz de io
package io;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import Principal.table.DataTable;
import Principal.table.RowView;
import Principal.table.Column;
import utils.enums.DataType;
import io.interfaces.TableReader;


public class CsvReader implements TableReader {

    public DataTable read(String filePath, String delimiter) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("El path no puede ser nulo o vacio");
        }
        
        List<String[]> rawRows = new ArrayList<>();
        String[] headers = null;
        List<DataType> columnTypes = new ArrayList<>();

        // Lectura del archivo y fase de parseo

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line = reader.readLine();
            headers = line.split(Pattern.quote(delimiter), -1);
            // Arranco asumiendo que todas las columnas son de tipo String
            for (int i = 0; i < headers.length; i++){
                columnTypes.add(DataType.STRING);

            }
            while (line != null) {
                String[] values = line.split(Pattern.quote(delimiter), -1);
                rawRows.add(values);
                for (int i = 0; i < values.length; i++){
                    columnTypes.set(i, inferType(values[i], columnTypes.get(i)));
                }

            }

        // Contruccion del DataTable con tipos definitivos
        DataTable table = new DataTable();
        for (int i = 0; i < headers.length; i++){
            table.addColumn(headers[i].trim(), columnTypes.get(i));
        }

        // Parseo final de los datos

        for (String[] row : rawRows){
            List<Object> parsed = new ArrayList<>();
            for (int i = 0; i < row.length; i++){
                parsed.add(parsedValue(row[i], columnTypes.get(i)));
            }
            table.addRow(parsed);
        }

        return table;
    }

    }
        
    private DataType inferType(String value, DataType currentType){
        if (value == null || value.isEmpty()) {
            return currentType; // TODO DataType NA?
        }
        
        try {
            Integer.parseInt(value);
            return DataType.INTEGER;
        } catch (NumberFormatException e) {
        }
        
        try {
            Double.parseDouble(value);
            return DataType.DOUBLE;
        } catch (NumberFormatException e) {
        }
        
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return DataType.BOOLEAN;
        }
        
        return DataType.STRING; // Si no se puede inferir, mantenemos el tipo actual
    }


}
