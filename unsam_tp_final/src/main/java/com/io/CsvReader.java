// CsvReader.java - clase o interfaz de io

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import table.DataTable;
import table.RowView;
import table.Column;
import enums.DataType;

public class CsvReader {

    public DataTable read(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<String> headers = new ArrayList<>();
            Map<Integer, RowView> rows = new HashMap<>();
            List<Column> columns = new ArrayList<>();
            // 
            Map<String /*label*/, DataType> columnTypes = new HashMap<>();

            // Read the header
            if ((line = br.readLine()) != null) {
                String[] headerArray = line.split(",");
                for (String header : headerArray) {
                    headers.add(header);
                }
            }

            // Lectura de la informacion
            int rowIndex = 0;
            while ((line = br.readLine()) != null) {
                String[] dataArray = line.split(",");
                List<Object> values = new ArrayList<>();
                for (int i = 0; i < dataArray.length; i++) {
                    values.add(dataArray[i]);
                    /*
                    if (i >= columns.size()) {
                        columns.add(new Column(headers.get(i) ó "NotDefined", DataType.STRING)); // Default type
                    } 
                    */
                }
                rows.put(rowIndex++, new RowView(rowIndex, values));
            }

            return new DataTable(rows, columns, columnTypes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}