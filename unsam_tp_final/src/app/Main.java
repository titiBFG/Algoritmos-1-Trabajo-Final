package app;

import io.interfaces.TableReader;
import utils.enums.LogicalOperator;
import utils.enums.Operator;
import io.CsvReader;
import io.CsvWriter;
import Principal.filter.SimpleFilter;
import Principal.sort.SimpleSorter;
import Principal.sort.interfaces.Sorter;
import Principal.table.DataTable;
import Principal.table.Table;
import Principal.table.TableView;
import Principal.table.Row;
import Principal.table.Column;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Principal.filter.CompFilter;
import Principal.filter.Filter;
import utils.OperationTimer;

public class Main {
    public static void main(String[] args) {
        try {
            OperationTimer timer = new OperationTimer();

            // ==========================================================
            // 1) LECTURA DE CSV: cargamos el archivo en una DataTable
            // ==========================================================
            System.out.println("\n" + "=".repeat(80));
            System.out.println("1) LECTURA DE CSV");
            System.out.println("=".repeat(80));
            timer.start();
            TableReader lector = new CsvReader();
            String filePath = "src\\csvPrueva\\arbolado-publico-lineal-2017-2018.csv";
            DataTable dataTable = lector.read(filePath, ",");
            timer.stop();
            System.out.println("Tiempo de lectura del CSV: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 2) INFORMACIÓN BÁSICA DE LA TABLA
            // ==========================================================
            timer.start();
            System.out.println("\n" + "=".repeat(80));
            System.out.println("2) INFORMACIÓN BÁSICA DE LA TABLA");
            System.out.println("=".repeat(80));
            System.out.println("Cantidad de filas: " + dataTable.getRowCount());
            System.out.println("Cantidad de columnas: " + dataTable.getColumnCount());
            System.out.println("Etiquetas de columnas: " + dataTable.getColumnLabels());
            System.out.println("Tipos de datos de columnas:");
            for (Map.Entry<String, utils.enums.DataType> entry : dataTable.getColumnTypes().entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("Etiquetas de filas (primeros 5): " + dataTable.getRows().keySet().stream().limit(5).toList());
            timer.stop();
            System.out.println("Tiempo de información básica: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 3) ACCESO INDEXADO
            // ==========================================================
            timer.start();
            System.out.println("\n" + "=".repeat(80));
            System.out.println("3) ACCESO INDEXADO");
            System.out.println("=".repeat(80));
            int filaEjemplo = 0;
            Row fila = dataTable.getRow(filaEjemplo);
            System.out.println("Fila " + filaEjemplo + ": " + fila);

            String columnaEjemplo = dataTable.getColumnLabels().get(0);
            System.out.print("Columna '" + columnaEjemplo + "' (primeros 5 valores): ");
            dataTable.getRows().values().stream().limit(5).forEach(r -> System.out.print(r.getValue(columnaEjemplo) + ", "));
            System.out.println();

            System.out.println("Celda [fila 0, columna '" + columnaEjemplo + "']: " + dataTable.getValue(columnaEjemplo, 0));
            timer.stop();
            System.out.println("Tiempo de acceso indexado: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 4) FORMATO DE CARGA/DESCARGA (CSV)
            // ==========================================================
            timer.start();
            System.out.println("\n" + "=".repeat(80));
            System.out.println("4) EXPORTAR A CSV (primeras 5 filas)");
            System.out.println("=".repeat(80));
            CsvWriter writer = new CsvWriter();
            DataTable head5 = dataTable.sample(5);
            writer.write(head5, "src\\csvPrueva\\ejemplo_prueba.csv", ",", true);
            System.out.println("Archivo 'ejemplo_export.csv' generado con 5 filas.");
            timer.stop();
            System.out.println("Tiempo de exportación a CSV: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 5) VISUALIZACIÓN
            // ==========================================================
            timer.start();
            System.out.println("\n" + "=".repeat(80));
            System.out.println("5) VISUALIZACIÓN DE LA TABLA (RESUMEN Y GRID)");
            System.out.println("=".repeat(80));
            TableView view = new TableView(dataTable);
            view.printSummary();
            System.out.println();
            view.printAsGrid();
            timer.stop();
            System.out.println("Tiempo de visualización: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 6) GENERACIÓN Y MODIFICACIÓN
            // ==========================================================
            timer.start();
            System.out.println();
            System.out.println("=" .repeat(80));
            System.out.println("6) GENERACIÓN Y MODIFICACIÓN");
            System.out.println("=".repeat(80));
            System.out.println("Se muestra cómo crear una copia profunda de la tabla, modificar una celda, agregar una columna nueva, eliminar una columna y eliminar una fila.");
            DataTable copia = dataTable.deepCopy(dataTable);
            System.out.println("Copia profunda creada. ¿Es igual a la original? " + copia.equals(dataTable));
            copia.getRows().get(0).setValue(columnaEjemplo, "VALOR_MODIFICADO");
            System.out.println("Valor modificado en copia (fila 0, columna '" + columnaEjemplo + "'): " + copia.getRows().get(0).getValue(columnaEjemplo));
            List<Object> nuevaCol = Arrays.asList("A", "B", "C", "D", "E");
            DataTable conColNueva = head5.addColumn("nueva_col", utils.enums.DataType.STRING, nuevaCol);
            System.out.println("Se agrega una columna llamada 'nueva_col':");
            new TableView(conColNueva).printAsGrid();
            DataTable sinCol = conColNueva.dropColumn("nueva_col");
            System.out.println("Se elimina la columna 'nueva_col':");
            new TableView(sinCol).printAsGrid();
            try {
                // Buscar el primer índice realmente presente en la tabla para eliminar esa fila
                Integer primerIndice = sinCol.getRows().keySet().stream().findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("La tabla está vacía"));
                DataTable sinFila = sinCol.dropRow(primerIndice);
                System.out.println("Se elimina la fila con índice " + primerIndice + ":");
                new TableView(sinFila).printAsGrid();
            } catch (IllegalArgumentException ex) {
                System.out.println("No se pudo eliminar la fila: " + ex.getMessage());
            }
            timer.stop();
            System.out.println("Tiempo de generación y modificación: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 7) SELECCIÓN (HEAD, TAIL, SLICE)
            // ==========================================================
            timer.start();
            System.out.println();
            System.out.println("=" .repeat(80));
            System.out.println("7) SELECCIÓN (HEAD, TAIL, SLICE)");
            System.out.println("=".repeat(80));
            System.out.println("Se seleccionan y muestran las primeras filas (head), las últimas filas (tail) y un rango de filas (slice) de la tabla.");
            System.out.println("Primeras 3 filas (head):");
            dataTable.head(3);
            System.out.println();
            System.out.println("Últimas 3 filas (tail):");
            dataTable.tail(3);
            System.out.println();
            System.out.println("Filas desde la 2 hasta la 5 (slice):");
            dataTable.slice(2, 5);
            timer.stop();
            System.out.println("Tiempo de selección (head/tail/slice): " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 8) FILTRADO
            // ==========================================================
            timer.start();
            System.out.println();
            System.out.println("=" .repeat(80));
            System.out.println("8) FILTRADO");
            System.out.println("=".repeat(80));
            System.out.println("Se filtran filas según condiciones simples y compuestas.");
            Filter filtroAltura = new SimpleFilter("altura_arbol", Operator.GT, 10);
            DataTable filtrada = dataTable.filter(filtroAltura);
            System.out.println("\nFilas donde 'altura_arbol' > 10:");
            new TableView(filtrada).printSummary();

            Filter filtroEspecie = new SimpleFilter("nombre_cientifico", Operator.EQ, "Fraxinus pennsylvanica");
            Filter compFilter = new CompFilter(Arrays.asList(filtroAltura, filtroEspecie), LogicalOperator.AND);
            DataTable filtradaComp = dataTable.filter(compFilter);
            System.out.println("\nFilas donde 'altura_arbol' > 10 y 'nombre_cientifico' = 'Fraxinus pennsylvanica':");
            new TableView(filtradaComp).printSummary();

            Filter filtroComuna = new SimpleFilter("comuna", Operator.EQ, 1);
            Filter compFilter2 = new CompFilter(
                Arrays.asList(compFilter, filtroComuna),
                LogicalOperator.OR
            );
            DataTable filtradaCompuesta = dataTable.filter(compFilter2);
            System.out.println("\nFilas donde ('altura_arbol' > 10 y 'nombre_cientifico' = 'Fraxinus pennsylvanica') o 'comuna' = 1:");
            new TableView(filtradaCompuesta).printSummary();
            timer.stop();
            System.out.println("Tiempo de filtrado: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 9) ORDENAMIENTO
            // ==========================================================
            timer.start();
            System.out.println("\n" + "=".repeat(80));
            System.out.println("9) ORDENAMIENTO");
            System.out.println("=".repeat(80));
            Sorter sorter = new SimpleSorter();
            List<String> columnas = Arrays.asList("altura_arbol");
            Table tablaOrdenada = sorter.sort(dataTable, columnas, true);
            System.out.println("Tabla ordenada por 'altura_arbol' ascendente (primeras 5 filas):");
            new TableView(((DataTable)tablaOrdenada).sample(5)).printAsGrid();
            timer.stop();
            System.out.println("Tiempo de ordenamiento: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 10) IMPUTACIÓN DE VALORES
            // ==========================================================
            timer.start();
            System.out.println("\n" + "=".repeat(80));
            System.out.println("10) IMPUTACIÓN DE VALORES");
            System.out.println("=".repeat(80));
            DataTable imputada = dataTable.impute("ancho_acera", 1.5);
            System.out.println("Tabla con imputación de NA en 'ancho_acera' con 1.5 (primeras 5 filas):");
            new TableView(imputada.sample(5)).printAsGrid();
            timer.stop();
            System.out.println("Tiempo de imputación: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 11) MUESTREO ALEATORIO
            // ==========================================================
            timer.start();
            System.out.println("\n" + "=".repeat(80));
            System.out.println("11) MUESTREO ALEATORIO");
            System.out.println("=".repeat(80));
            DataTable muestraCinco = dataTable.sample(5);
            System.out.println("Muestra de 5 filas aleatorias:");
            new TableView(muestraCinco).printAsGrid();
            DataTable muestraVeintePorciento = dataTable.sample(0.2);
            System.out.println("Muestra del 20% de las filas:");
            new TableView(muestraVeintePorciento).printSummary();
            timer.stop();
            System.out.println("Tiempo de muestreo aleatorio: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 12) CONCATENACIÓN
            // ==========================================================
            timer.start();
            System.out.println("\n" + "=".repeat(80));
            System.out.println("12) CONCATENACIÓN");
            System.out.println("=".repeat(80));
            DataTable tablaA = dataTable.sample(3);
            DataTable tablaB = dataTable.sample(2);
            DataTable concatenada = tablaA.concat(tablaB);
            System.out.println("Tabla concatenada (3 filas + 2 filas):");
            new TableView(concatenada).printAsGrid();
            timer.stop();
            System.out.println("Tiempo de concatenación: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 13) NA (VALOR FALTANTE)
            // ==========================================================
            timer.start();
            System.out.println("\n" + "=".repeat(80));
            System.out.println("13) VALOR FALTANTE (NA)");
            System.out.println("=".repeat(80));
            System.out.println("Primer fila, columna 'ancho_acera': " + dataTable.getRows().get(0).getValue("ancho_acera"));
            timer.stop();
            System.out.println("Tiempo de acceso a NA: " + timer.getFormattedElapsedTime());

            System.out.println("\n" + "=".repeat(80));
            System.out.println("FIN DEL DEMO");
            System.out.println("=".repeat(80) + "\n");

        } catch (Exception e) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("¡ERROR EN LA EJECUCIÓN!");
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("Stacktrace:");
            e.printStackTrace(System.out);
            System.out.println("=".repeat(80));
        }
    }
}

