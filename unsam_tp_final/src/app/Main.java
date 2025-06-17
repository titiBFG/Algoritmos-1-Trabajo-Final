package app;

import io.interfaces.TableReader;
import utils.enums.DataType;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
            System.out.println("\n");
            System.out.println("\nSe agrega una columna llamada 'nueva_col':");
            System.out.println("\n");
            new TableView(conColNueva).printSummary();
            System.out.println("\n");
            new TableView(conColNueva).printAsGrid(List.of("nivel_plantera", "diametro_altura_pecho", "nueva_col"));
            System.out.println("\n");
            DataTable sinCol = conColNueva.dropColumn("nueva_col");
            System.out.println("\nSe elimina la columna 'nueva_col':");
            System.out.println("\n");
            new TableView(sinCol).printSummary();
            System.out.println("\n");
            new TableView(sinCol).printAsGrid(List.of("nivel_plantera", "diametro_altura_pecho"));
            System.out.println("\n");
            try {
                // Buscar el primer índice realmente presente en la tabla para eliminar esa fila
                Integer primerIndice = sinCol.getRows().keySet().stream().findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("La tabla está vacía"));
                DataTable sinFila = sinCol.dropRow(primerIndice);
                System.out.println("Se elimina la fila con índice " + primerIndice + ":");
                System.out.println("\n");
                new TableView(sinFila).printSummary();
                System.out.println("\n");
                new TableView(sinFila).printAsGrid();
            } catch (IllegalArgumentException ex) {
                System.out.println("No se pudo eliminar la fila: " + ex.getMessage());
            }
            // ==========================================================
            // NUEVOS CASOS DE USO PARA addColumnFromExisting
            // ==========================================================
            System.out.println("\nOperaciones con addColumnFromExisting:");

            // 1. Copia simple de columna
            DataTable copiaColumna = dataTable.addColumnFromExisting(
                    "altura_arbol",
                    "altura_copia",
                    null // Sin transformación
            );
            System.out.println("\n1) Copia simple de columna 'altura_arbol' a 'altura_copia':");
            new TableView(copiaColumna.sample(3)).printSummary();
            System.out.println("\n");
            new TableView(copiaColumna.sample(3)).printAsGrid(List.of("altura_arbol", "altura_copia"));

            // 2. Copia con transformación (cm a metros)
            DataTable conversionColumna = dataTable.addColumnFromExisting(
                    "altura_arbol",
                    "altura_metros",
                    valor -> valor instanceof Number ? ((Number)valor).doubleValue() / 100 : valor
            );
            System.out.println("\n2) Conversión de 'altura_arbol' (cm) a 'altura_metros' (m):");
            new TableView(conversionColumna.sample(3)).printSummary();
            System.out.println("\n");
            new TableView(conversionColumna.sample(3)).printAsGrid(List.of("altura_arbol", "altura_metros"));

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
            ((DataTable)tablaOrdenada).head(5);
            timer.stop();
            System.out.println("Tiempo de ordenamiento: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 10) IMPUTACIÓN DE VALORES
            // ==========================================================
            timer.start();
            System.out.println("\n" + "=".repeat(80));
            System.out.println("10) IMPUTACIÓN DE VALORES");
            System.out.println("=".repeat(80));
            new TableView(dataTable).printAsGrid(List.of("ancho_acera", "altura_arbol", "nombre_cientifico"));
            System.out.println("\n");
            DataTable imputada = dataTable.impute("ancho_acera", 1.5);
            System.out.println("Tabla con imputación de NA en 'ancho_acera' con 1.5 (primeras 5 filas):");
            System.out.println("\n");
            new TableView(imputada).printAsGrid(List.of("ancho_acera", "altura_arbol", "nombre_cientifico"));
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
            // 13) MODIFICACIÓN DE CELDAS (setAt)
            // ==========================================================
            timer.start();
            System.out.println("\n" + "=".repeat(80));
            System.out.println("13) MODIFICACIÓN DE CELDAS (setAt)");
            System.out.println("=".repeat(80));

            // Seleccionamos una fila y columna para modificar
            int filaAModificar = 0;
            String columnaAModificar = dataTable.getColumnLabels().get(0);
            Object valorOriginal = dataTable.getValue(columnaAModificar, filaAModificar);

            // Mostramos la fila completa antes de la modificación
            System.out.println("\nFila ORIGINAL (antes de modificar):");
            Row filaOriginal = dataTable.getRow(filaAModificar);
            System.out.println(filaOriginal);

            // Modificamos el valor
            Object nuevoValor = "255.44";
            System.out.println("\nModificando celda [" + filaAModificar + ", '" +
                    columnaAModificar + "'] de '" + valorOriginal +
                    "' a '" + nuevoValor + "'");
            dataTable.setAt(filaAModificar, columnaAModificar, nuevoValor);

            // Mostramos la fila completa después de la modificación
            System.out.println("\nFila MODIFICADA:");
            Row filaModificada = dataTable.getRow(filaAModificar);
            System.out.println(filaModificada);

            // Volvemos a poner el valor original
            System.out.println("\nRestaurando valor original...");
            dataTable.setAt(filaAModificar, columnaAModificar, valorOriginal);

            // Mostramos la fila completa después de restaurar
            System.out.println("\nFila RESTAURADA (debe ser igual a la original):");
            Row filaRestaurada = dataTable.getRow(filaAModificar);
            System.out.println(filaRestaurada);

            // Verificación
            if (filaOriginal.equals(filaRestaurada)) {
                System.out.println("\nLa fila restaurada es idéntica a la original.");
            } else {
                System.out.println("\nLa fila restaurada no coincide con la original.");
            }

            timer.stop();
            System.out.println("\nTiempo de modificación y restauración de celda: " + timer.getFormattedElapsedTime());

            // ==========================================================
            // 14) Prueba de Fuente de datos from2D y fromIterable
            // ========================================================== 
            System.out.println("\n" + "=".repeat(80));
            System.out.println("14) PRUEBA DE FUENTES DE DATOS (from2D y fromIterable)");
            System.out.println("=".repeat(80));

            timer.start();
            // Creamos un DataTable "helper" vacío solo para invocar los métodos
            DataTable helper = new DataTable(
            new LinkedHashMap<>(), 
            new ArrayList<>(), 
            new LinkedHashMap<>()
            );

            // ---- Prueba from2D ----
            Object[][] datos2D = {
                { "Pedrito", 30,  true  },
                { "Pablito",   25,  false },
                { "Rosa", 28,  true  }
            };
            String[] labels2D = { "nombre", "edad", "activo" };
            DataType[] types2D = {
                DataType.STRING,
                DataType.INTEGER,
                DataType.BOOLEAN
            };

            DataTable tabla2D = helper.from2D(datos2D, labels2D, types2D);
            DataTable tabla2Dfromexisting = tabla2D.addColumnFromExisting("edad", "edad + 1",
                valor -> {
                    if (valor instanceof Number) {
                        return ((Number) valor).intValue() + 1;
                    }
                    return valor; // Si no es un número, devolver el valor original
                }
            );
            System.out.println("\n=== Tabla construida desde matriz 2D ===");
            new TableView(tabla2D).printProlijo();
            System.out.println("\n=== Tabla construida desde matriz 2D con columna nueva ===");
            new TableView(tabla2Dfromexisting).printAsGrid();

            // ---- Prueba fromIterable ----
            List<List<Object>> datosIter = Arrays.asList(
                Arrays.asList("X", 1, false),
                Arrays.asList("Y", 2, true ),
                Arrays.asList("Z", 3, false)
            );
            List<String> labelsIter = Arrays.asList("col1", "col2", "col3");
            List<DataType> typesIter = Arrays.asList(
                DataType.STRING,
                DataType.INTEGER,
                DataType.BOOLEAN
            );

            // Podemos reutilizar 'helper' o 'tabla2D' ya creado; el método no usa estado anterior.
            DataTable tablaIter = helper.fromIterable(datosIter, labelsIter, typesIter);
            System.out.println("\n=== Tabla construida desde Iterable ===");
            new TableView(tablaIter).printProlijo();
            timer.stop();
            System.out.println("Tiempo de creación de tablas desde 2D e Iterable: " + timer.getFormattedElapsedTime());
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

