// CsvReader.java
package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import Principal.table.Column;
import Principal.table.DataTable;
import Principal.table.Row;
import utils.validation.NA;
import utils.enums.DataType;
import io.interfaces.TableReader;

/**
 * CsvReader que implementa TableReader y devuelve un DataTable completo.
 */
public class CsvReader implements TableReader {

    @Override
    public DataTable read(String filePath, String delimiter) throws IOException {
        // 1) Validar parámetros de entrada
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("El path no puede ser nulo o vacío");
        }
        if (delimiter == null || delimiter.isEmpty()) {
            throw new IllegalArgumentException("El delimitador no puede ser nulo o vacío");
        }

        List<String[]> rawRows = new ArrayList<>();  // Almacena filas crudas (sin procesar)
        String[] headers;                            // Contendrá los nombres de columna

        // 2) Primera pasada: leer encabezado e inferir tipos de columna
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line == null) {
                // CSV vacío o sin encabezado
                throw new IOException("El CSV no puede estar vacío o no tener encabezado");
            }
            // Separar nombres de columna usando el delimitador (escapeado con Pattern.quote)
            headers = line.split(Pattern.quote(delimiter), -1);

            // Inicializar lista de DataType (todos en null, para inferir más adelante)
            List<DataType> inferredTypes = new ArrayList<>(headers.length);
            for (int i = 0; i < headers.length; i++) {
                inferredTypes.add(null);
            }

            // Recorrer cada línea restante para inferir tipos de columna
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(Pattern.quote(delimiter), -1);
                rawRows.add(values);  // Guardar fila cruda para procesar después

                for (int i = 0; i < headers.length; i++) {
                    if (i >= values.length) {
                        // Esta fila es más corta que el encabezado; no hay valor en esta columna
                        continue;
                    }
                    String celda = values[i].trim();
                    if (celda.isEmpty()) {
                        // Celda vacía ("" o "   ") → NA, no influye en la inferencia de tipo
                        continue;
                    }

                    // Determinar tipo de la celda: INTEGER, DOUBLE, BOOLEAN o STRING
                    DataType celdaTipo;
                    try {
                        Integer.parseInt(celda);
                        celdaTipo = DataType.INTEGER;
                    } catch (NumberFormatException e1) {
                        try {
                            Double.parseDouble(celda);
                            celdaTipo = DataType.DOUBLE;
                        } catch (NumberFormatException e2) {
                            if (celda.equalsIgnoreCase("true") || celda.equalsIgnoreCase("false")) {
                                celdaTipo = DataType.BOOLEAN;
                            } else {
                                celdaTipo = DataType.STRING;
                            }
                        }
                    }

                    // Obtener el tipo previamente inferido para esta columna (puede ser null)
                    DataType celdatipoprev = inferredTypes.get(i);
                    if (celdatipoprev == null) {
                        // Si aún no se había asignado, usar el tipo recién detectado
                        inferredTypes.set(i, celdaTipo);
                    } else if (celdatipoprev == celdaTipo) {
                        // Si coincide con el tipo anterior, no hacemos nada
                    } else if (celdatipoprev == DataType.STRING || celdaTipo == DataType.STRING) {
                        // Si alguno es STRING, la columna definitiva es STRING
                        inferredTypes.set(i, DataType.STRING);
                    } else if ((celdatipoprev == DataType.DOUBLE && celdaTipo == DataType.INTEGER)
                            || (celdatipoprev == DataType.INTEGER && celdaTipo == DataType.DOUBLE)) {
                        // Mezcla INTEGER y DOUBLE → promover a DOUBLE
                        inferredTypes.set(i, DataType.DOUBLE);
                    } else {
                        // Cualquier otra combinación → forzar STRING
                        inferredTypes.set(i, DataType.STRING);
                    }
                }
            }

            // 3) Rellenar con STRING donde el tipo siga siendo null (columnas completamente vacías)
            for (int i = 0; i < inferredTypes.size(); i++) {
                if (inferredTypes.get(i) == null) {
                    inferredTypes.set(i, DataType.STRING);
                }
            }

            // 4) Construir List<Column> y Map<String,DataType>
            List<Column> columnList = new ArrayList<>(headers.length);
            Map<String, DataType> columnTypesMap = new LinkedHashMap<>(headers.length);
            for (int i = 0; i < headers.length; i++) {
                String label = headers[i].trim();
                DataType dt = inferredTypes.get(i);
                columnList.add(new Column(label, dt));
                columnTypesMap.put(label, dt);
            }

            // 5) Construir lista de etiquetas (columnLabels) para pasar a cada Row
            List<String> columnLabels = new ArrayList<>(headers.length);
            for (Column col : columnList) {
                columnLabels.add(col.getLabel());
            }

            // 6) Crear Map<Integer, Row> con cada rawRow ya parseada
            Map<Integer, Row> rowMap = new LinkedHashMap<>();
            for (int rowIdx = 0; rowIdx < rawRows.size(); rowIdx++) {
                String[] raw = rawRows.get(rowIdx);
                List<Object> parsedRow = new ArrayList<>(headers.length);

                for (int i = 0; i < headers.length; i++) {
                    if (i < raw.length) {
                        // parseValue devuelve NA.INSTANCE si raw[i] es null, vacío o solo espacios
                        parsedRow.add(parseValue(raw[i], inferredTypes.get(i)));
                    } else {
                        // Si la fila no tenía tantas columnas, guardar NA
                        parsedRow.add(NA.INSTANCE);
                    }
                }

                // Construir el objeto Row: index = rowIdx (0-based), lista de valores y lista de etiquetas
                Row r = new Row(rowIdx, parsedRow, columnLabels);
                rowMap.put(rowIdx, r);
            }

            // 7) Construir y retornar el DataTable con:
            //    - rowMap: Mapa filaID → Row
            //    - columnList: Lista de Column (label + tipo)
            //    - columnTypesMap: Mapa nombreColumna → DataType
            return new DataTable(rowMap, columnList, columnTypesMap);
        }
    }

    /**
     * parseValue: convierte la cadena raw al objeto correspondiente según DataType.
     *   - Si raw es null, vacío ("") o "   ", devuelve NA.INSTANCE.
     *   - Si falla el parseo a INTEGER, DOUBLE o BOOLEAN, también devuelve NA.INSTANCE.
     */
    private Object parseValue(String raw, DataType type) {
        // 1) Si es null o, tras hacer trim, queda vacío → NA.INSTANCE
        if (raw == null || raw.trim().isEmpty()) {
            return NA.INSTANCE;
        }

        String v = raw.trim();
        switch (type) {
            case INTEGER:
                try {
                    return Integer.parseInt(v);
                } catch (NumberFormatException e) {
                    System.err.println("Warning: no se pudo parsear '" + v + "' a INTEGER, devuelvo NA");
                    return NA.INSTANCE;
                }
            case DOUBLE:
                try {
                    return Double.parseDouble(v);
                } catch (NumberFormatException e) {
                    System.err.println("Warning: no se pudo parsear '" + v + "' a DOUBLE, devuelvo NA");
                    return NA.INSTANCE;
                }
            case BOOLEAN:
                if (v.equalsIgnoreCase("true")) {
                    return true;
                } else if (v.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    System.err.println("Warning: no se pudo parsear '" + v + "' a BOOLEAN, devuelvo NA");
                    return NA.INSTANCE;
                }
            case STRING:
            default:
                // Para STRING, devolver la cadena misma
                return v;
        }
    }
}
