// SimpleSorter.java - clase o interfaz de core
package Principal.sort;

import Principal.sort.interfaces.Sorter;
import Principal.table.Table;
import Principal.table.Row;
import Principal.table.DataTable;

import java.util.*;

public class SimpleSorter implements Sorter {
    @Override
    public Table sort(Table table, List<String> columns, boolean ascending) {
        // 1) Tomar las filas actuales en una lista junto a sus índices
        List<Map.Entry<Integer, Row>> filaEntrys = new ArrayList<>(((DataTable) table).getRows().entrySet());

        // 2) Ordenar la lista usando los valores de las columnas
        filaEntrys.sort((e1, e2) -> {
            Row row1 = e1.getValue();
            Row row2 = e2.getValue();
            for (String col : columns) {
                Object v1 = row1.getValue(col);
                Object v2 = row2.getValue(col);

                // Si ambos son numéricos, comparar como double
                if (v1 instanceof Number && v2 instanceof Number) {
                    double d1 = ((Number) v1).doubleValue();
                    double d2 = ((Number) v2).doubleValue();
                    int cmp = Double.compare(d1, d2);
                    if (cmp != 0) return ascending ? cmp : -cmp;
                } else if (v1 instanceof Comparable && v2 instanceof Comparable) {
                    @SuppressWarnings("unchecked")
                    int cmp = ((Comparable<Object>) v1).compareTo(v2);
                    if (cmp != 0) return ascending ? cmp : -cmp;
                } else if (v1 != null && v2 == null) {
                    return ascending ? 1 : -1;
                } else if (v1 == null && v2 != null) {
                    return ascending ? -1 : 1;
                }
                // Si son iguales, sigue con la siguiente columna
            }
            return 0;
        });

        // 3) Crear el nuevo map de filas, conservando columnas/types
        LinkedHashMap<Integer, Row> filasOrdenadas = new LinkedHashMap<>();
        for (Map.Entry<Integer, Row> entry : filaEntrys) {
            filasOrdenadas.put(entry.getKey(), entry.getValue());
        }

        // 4) Crear la tabla ordenada usando el mismo constructor que DataTable
        return new DataTable(filasOrdenadas, ((DataTable) table).getColumns(), ((DataTable) table).getColumnTypes());
    }
}
