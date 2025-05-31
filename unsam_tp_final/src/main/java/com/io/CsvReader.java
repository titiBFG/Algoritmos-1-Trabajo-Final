package IO;

// CsvReader.java - clase o interfaz de io

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import Principal.table.DataTable;
import Utils.enums.DataType;
import interfaces.TableReader;

public class CsvReader implements TableReader {

    @Override
    public DataTable read(String filePath, String delimiter, boolean hasHeaders) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("El path no puede ser nulo o vacio");
        }
        DataTable table = new DataTable();
        List<String[]> rawRows = new ArrayList<>();
        String[] headers = null;
        List<DataType> columnTypes = new ArrayList<>();

        // Lectura del archivo y fase de parseo

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line = reader.readLine();
            if (hasHeaders) {
                headers = line.split(Pattern.quote(delimiter), -1);
                // Arranco asumiendo que todas las columnas son de tipo String
                for (int i = 0; i < headers.length; i++){
                    columnTypes.add(DataType.STRING);
                }
            } else {
                // Si no hay headers, agrego el tipo de cada columna por indice de columna
                int columnCount = line.split(Pattern.quote(delimiter), -1).length;
                for (int i = 0; i < columnCount; i++) {
                    columnTypes.add(DataType.STRING);
                }
                // Contruccion del DataTable con tipos definitivos
                for (int i = 0; i < headers.length; i++){
                    table.addColumn(headers[i].trim(), columnTypes.get(i));
                    }
            while (line != null) {
                String[] values = line.split(Pattern.quote(delimiter), -1);
                rawRows.add(values);
                for (int i = 0; i < values.length; i++){
                    columnTypes.set(i, inferType(values[i], columnTypes.get(i)));
                }

            }
        }

        // Parseo final de los datos
        for (String[]row : rawRows){
            List<Object> parsed = new ArrayList<>();
            for (int i = 0; i < row.length; i++){
                parsed.add(inferType(row[i], columnTypes.get(i)));
            }
            table.addRow(parsed);
        }

        return table;
    } catch (IOException e) {
            throw new IOException("Error al leer el archivo: " + filePath, e);
        }
    }

    public DataTable read(String filePath) throws IOException {
        return read(filePath, ",", true);
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
