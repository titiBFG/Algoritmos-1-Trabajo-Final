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
        // Usamos los índices realmente presentes
        if (table instanceof DataTable) {
            for (Integer rowIndex : ((DataTable) table).getRows().keySet()) {
                Row fila = table.getRow(rowIndex);
                System.out.printf("Fila %d: %s%n", fila.getIndex(), fila.toString());
            }
        } else {
            // fallback: recorrer por cantidad de filas
            for (int i = 0; i < table.getRowCount(); i++) {
                Row fila = table.getRow(i);
                System.out.printf("Fila %d: %s%n", fila.getIndex(), fila.toString());
            }
        }
    }

    public void printAsGrid() {
        final int MAX_COLS = 8;
        final int MAX_ROWS = 10;

        List<String> labels = table.getColumnLabels();
        int colCount = table.getColumnCount();
        int rowCount = table.getRowCount();

        // Determinar columnas a mostrar
        int shownCols = Math.min(colCount, MAX_COLS);
        boolean colEllipsis = colCount > MAX_COLS;

        // Determinar filas a mostrar
        int shownRows = Math.min(rowCount, MAX_ROWS);
        boolean rowEllipsis = rowCount > MAX_ROWS;

        // Calcular ancho de cada columna
        int[] colWidths = new int[shownCols];
        for (int c = 0; c < shownCols; c++) {
            colWidths[c] = labels.get(c).length();
        }
        for (int r = 0; r < shownRows; r++) {
            Row row = table.getRow(r);
            for (int c = 0; c < shownCols; c++) {
                String val = String.valueOf(row.getValue(labels.get(c)));
                colWidths[c] = Math.max(colWidths[c], val.length());
            }
        }

        // Imprimir header
        System.out.print("|");
        for (int c = 0; c < shownCols; c++) {
            System.out.printf(" %-" + colWidths[c] + "s |", labels.get(c));
        }
        if (colEllipsis) System.out.print(" ... |");
        System.out.println();

        // Imprimir separador
        System.out.print("|");
        for (int c = 0; c < shownCols; c++) {
            System.out.print(" " + "-".repeat(colWidths[c]) + " |");
        }
        if (colEllipsis) System.out.print("-----|");
        System.out.println();

        // Imprimir filas
        for (int r = 0; r < shownRows; r++) {
            Row row = table.getRow(r);
            System.out.print("|");
            for (int c = 0; c < shownCols; c++) {
                String val = String.valueOf(row.getValue(labels.get(c)));
                System.out.printf(" %-" + colWidths[c] + "s |", val);
            }
            if (colEllipsis) System.out.print(" ... |");
            System.out.println();
        }
        if (rowEllipsis) {
            // Imprimir puntos suspensivos para filas
            System.out.print("|");
            for (int c = 0; c < shownCols; c++) {
                System.out.printf(" %-" + colWidths[c] + "s |", "...");
            }
            if (colEllipsis) System.out.print(" ... |");
            System.out.println();
        }
    }


    public void printProlijo() {
        printSummary();
        System.out.println();
        printAsGrid();
    }
}
