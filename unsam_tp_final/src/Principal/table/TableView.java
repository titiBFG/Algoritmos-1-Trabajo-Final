package Principal.table;

import java.util.List;

public class TableView {

    private final Table table;

    public TableView(Table table) {
        if (table == null) throw new IllegalArgumentException("table no puede ser null");
        this.table = table;
    }

    public void printSummary() {
        System.out.println("Resumen de la tabla");
        System.out.printf("Filas: %d | Columnas: %d%n",
                          table.getRowCount(), table.getColumnCount());
        System.out.println("Columnas: " + String.join(", ", table.getColumnLabels()));
    }

    public void printAllRows() {
        System.out.println("Contenido completo de la tabla");
        for (int i = 0; i < table.getRowCount(); i++) {
            Row fila = table.getRow(i);
            System.out.printf("Fila %d: %s%n", fila.getIndex(), fila.toString());
        }
    }

    /*
     * ejemplo:
     * | id | nombre  | edad |
     * | 1  | “Ana”   | 23   |
     * | 2  | “Pedro” | 30   |
     *
     */

    public void printAsGrid() {
        List<String> labels = table.getColumnLabels();
        int nCols = labels.size();
        int nRows = table.getRowCount();

        // 1) Calculamos ancho máximo de cada columna basándonos en cabecera y valores
        int[] maxWidth = new int[nCols];
        // Inicialmente, ancho = longitud del nombre de la cabecera
        for (int c = 0; c < nCols; c++) {
            maxWidth[c] = labels.get(c).length();
        }
        // Recorremos valores para ampliar anchos
        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                Object val = table.getValue(labels.get(c), r);
                String text = (val == null) ? "null" : val.toString();
                maxWidth[c] = Math.max(maxWidth[c], text.length());
            }
        }

        // 2) Construimos formato de cada celda: "| %-5s " para ancho=5
        StringBuilder formatBuilder = new StringBuilder();
        for (int c = 0; c < nCols; c++) {
            formatBuilder.append("| %-" + maxWidth[c] + "s ");
        }
        formatBuilder.append("|\n");
        String rowFormat = formatBuilder.toString();

        // 3) Línea separadora: "+-----+--------+-----+"
        StringBuilder sepBuilder = new StringBuilder();
        for (int c = 0; c < nCols; c++) {
            sepBuilder.append("+");
            for (int j = 0; j < maxWidth[c] + 2; j++) {
                sepBuilder.append("-");
            }
        }
        sepBuilder.append("+\n");
        String separator = sepBuilder.toString();

        // 4) Imprimimos cabecera
        System.out.print(separator);
        Object[] headerVals = labels.toArray(new Object[0]);
        System.out.printf(rowFormat, headerVals);
        System.out.print(separator);

        // 5) Imprimimos cada fila
        for (int r = 0; r < nRows; r++) {
            Object[] rowVals = new Object[nCols];
            for (int c = 0; c < nCols; c++) {
                Object val = table.getValue(labels.get(c), r);
                rowVals[c] = (val == null) ? "null" : val.toString();
            }
            System.out.printf(rowFormat, rowVals);
        }
        System.out.print(separator);
    }
}
