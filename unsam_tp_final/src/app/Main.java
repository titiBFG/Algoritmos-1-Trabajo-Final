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

import java.util.Arrays;
import java.util.List;

import Principal.filter.CompFilter;
import Principal.filter.Filter;
import utils.OperationTimer;

public class Main {
    public static void main(String[] args) {
        try {
            // ==========================================================
            // 1) LECTURA DE CSV: cargamos el archivo en una DataTable
            // ==========================================================
            OperationTimer timer = new OperationTimer();
            timer.start();
            TableReader lector = new CsvReader();
            String filePath = "src/csvPrueva/arbolado-publico-lineal-2017-2018.csv";
            DataTable dataTable = lector.read(filePath, ",");
            timer.stop();
            System.out.println("Tiempo de lectura del CSV: " + timer.getFormattedElapsedTime());
            // ==========================================================
            // 2) VISUALIZACIÓN: mostramos resumen, filas y grid de la tabla original
            // ==========================================================
            timer.start();
            TableView view = new TableView(dataTable);
            view.printProlijo();
            // Muestra las primeras 5 filas
            TableView tableView = new TableView(dataTable.head(5));
            System.out.println("head 5");
            tableView.printAllRows();
            //muestra las ultimas 5 filas

            TableView tableView2 = new TableView(dataTable.tail(5));
            System.out.println("tail 5");
            tableView2.printAllRows();
            timer.stop();
            System.out.println("Tiempo de visualización: " + timer.getFormattedElapsedTime());
            // ==========================================================
            // 3) FILTRO SIMPLE: árboles con altura_arbol > 10
            // ==========================================================
            timer.start();
            Filter filtroAltura = new SimpleFilter("altura_arbol", Operator.GT, 10);
            DataTable filtrada = (DataTable) dataTable.filter(filtroAltura);

            // Mostrar resultados del filtro simple
            TableView view_filtro_simple = new TableView(filtrada);
            System.out.println("RESUMEN DE ÁRBOLES CON ALTURA > 10:");
            view_filtro_simple.printProlijo();
            timer.stop();
            System.out.println("Tiempo de filtrado simple: " + timer.getFormattedElapsedTime());
            // ==========================================================
            // 4) FILTRO COMPUESTO: altura > 10 Y especie igual a 'Fraxinus pennsylvanica'
            // ==========================================================
            timer.start();
            Filter filtroEspecie = new SimpleFilter("nombre_cientifico", Operator.EQ, "Fraxinus pennsylvanica");
            Filter compFilter = new CompFilter(
                java.util.Arrays.asList(filtroAltura, filtroEspecie),
                LogicalOperator.AND
            );
            DataTable filtrada_combinada = (DataTable) dataTable.filter(compFilter);

            // Mostrar resultados del filtro compuesto
            TableView viewComp = new TableView(filtrada_combinada);
            System.out.println("RESUMEN DE ÁRBOLES CON ALTURA > 10 Y ESPECIE 'Fraxinus pennsylvanica':");
            viewComp.printProlijo();

            timer.stop();
            System.out.println("Tiempo de filtrado compuesto: " + timer.getFormattedElapsedTime());
            // ==========================================================
            // 5) ORDENAMIENTO: por una columna (altura_arbol ascendente)
            // ==========================================================
            timer.start();
            Sorter sorter = new SimpleSorter();
            List<String> columnas = java.util.Collections.singletonList("altura_arbol");
            Table tablaOrdenada = sorter.sort(dataTable, columnas, true);

            // Visualizar la tabla ordenada por altura_arbol
            TableView viewOrdenada = new TableView(tablaOrdenada);
            System.out.println("TABLA ORDENADA POR altura_arbol ASCENDENTE:");
            viewOrdenada.printProlijo();
            
            // ==========================================================
            // 6) ORDENAMIENTO: por dos columnas (ej: calle_altura y calle_chapa descendente)
            // ==========================================================
            timer.start();
            List<String> columnas_2 = Arrays.asList("calle_altura", "calle_chapa");
            boolean ascending = false; // Orden descendente
            Sorter sorter_2 = new SimpleSorter();
            Table tablaOrdenada_2 = sorter_2.sort(dataTable, columnas_2, ascending);

            // Visualizar la tabla ordenada por dos columnas
            System.out.println("TABLA ORDENADA POR calle_altura Y calle_chapa DESCENDENTE:");
            TableView viewOrdenada_2 = new TableView(tablaOrdenada_2);
            viewOrdenada_2.printProlijo();
            timer.stop();
            System.out.println("Tiempo de ordenamiento por dos columnas: " + timer.getFormattedElapsedTime());
            // ==========================================================
            // 7) EXPORTAR A CSV el resultado ordenado
            // ==========================================================
            timer.start();
            CsvWriter writer = new CsvWriter();
            writer.write(tablaOrdenada_2,
                "src/csvPrueva/resultado_3.csv",
                ",", false); // 'false' para no incluir encabezado, 'true' para incluirlo
            timer.stop();
            System.out.println("Tiempo de exportación a CSV: " + timer.getFormattedElapsedTime());
            // ==========================================================
            // 8) MUESTREO ALEATORIO: obtener 5 filas aleatorias
            // ==========================================================
            timer.start();
            DataTable muestraCinco = dataTable.sample(5);

            // Mostrar la muestra de 5 filas
            System.out.println("MUESTRA DE 5 FILAS ALEATORIAS:");
            TableView viewMuestra = new TableView(muestraCinco);
            viewMuestra.printProlijo();
            timer.stop();
            System.out.println("Tiempo de muestreo aleatorio (5 filas): " + timer.getFormattedElapsedTime());
            // ==========================================================
            // 9) MUESTREO ALEATORIO: obtener el 20% de las filas al azar
            // ==========================================================
            timer.start();
            DataTable muestraVeintePorciento = dataTable.sample(0.2);

            System.out.println("MUESTRA DEL 20% DE LAS FILAS:");
            TableView viewMuestraPorciento = new TableView(muestraVeintePorciento);
            viewMuestraPorciento.printProlijo();
            timer.stop();
            System.out.println("Tiempo de muestreo aleatorio (20% de filas): " + timer.getFormattedElapsedTime());
            // ==========================================================
            // 10) COPIA PROFUNDA: crear una copia de la tabla original
            // ==========================================================
            timer.start();
            DataTable copia = dataTable.deepCopy(dataTable);
            // Cambiar un valor en la copia
            copia.getRows().get(0).setValue("altura_arbol", 100); 

            // Mostrar solo la fila modificada y la original para comparar
            System.out.println("Fila modificada de la copia:");
            System.out.println(copia.getRows().get(0));
            System.out.println("Fila correspondiente en la tabla original (debería ser distinta):");
            System.out.println(dataTable.getRows().get(0));
            timer.stop();
            System.out.println("Tiempo de copia profunda: " + timer.getFormattedElapsedTime());
            // ==========================================================
            // 11) CONCATENACIÓN: unir dos DataTables con las mismas columnas
            // ==========================================================
            timer.start();
            // 1) Tomar dos tablas originales o filtradas (puede ser la misma para probar)
            DataTable tablaA = dataTable.sample(5); // 5 filas al azar de la tabla original
            DataTable tablaB = dataTable.sample(5); // otras 5 filas al azar

            // 2) Concatenar ambas tablas
            DataTable concatenada = tablaA.concat(tablaB);

            // 3) Visualizar el resultado
            System.out.println("TABLA CONCATENADA:");
            TableView viewConcat = new TableView(concatenada);
            viewConcat.printSummary();
            viewConcat.printAllRows();
            viewConcat.printAsGrid();
            timer.stop();
            System.out.println("Tiempo de concatenación: " + timer.getFormattedElapsedTime());
            //===========================================================
            // 12) Imputación de valores: reemplazar "NA" por un valor específico
            //===========================================================
            timer.start();
            // Imputar solo NA de la columna "ancho_acera" con 1.5
            DataTable imputadaColumna = concatenada.impute("ancho_acera", 1.5);

            // Visualizá el resultado para ver el cambio
            TableView viewImputada = new TableView(imputadaColumna);
            System.out.println("TABLA CON IMPUTACIÓN DE 'NA' EN 'ancho_acera' CON 1.5:");
            viewImputada.printAllRows();
            timer.stop();
            System.out.println("Tiempo de imputación de valores: " + timer.getFormattedElapsedTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

