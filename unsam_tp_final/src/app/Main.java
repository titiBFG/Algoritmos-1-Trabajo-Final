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


public class Main {
    public static void main(String[] args) {
        try {
            // ==========================================================
            // 1) LECTURA DE CSV: cargamos el archivo en una DataTable
            // ==========================================================
            TableReader lector = new CsvReader();
            String filePath = "src\\csvPrueva\\arbolado-publico-lineal-2017-2018.csv";
            DataTable dataTable = lector.read(filePath, ",");

            // ==========================================================
            // 2) VISUALIZACIÓN: mostramos resumen, filas y grid de la tabla original
            // ==========================================================
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

            // ==========================================================
            // 3) FILTRO SIMPLE: árboles con altura_arbol > 10
            // ==========================================================
            Filter filtroAltura = new SimpleFilter("altura_arbol", Operator.GT, 10);
            DataTable filtrada = (DataTable) dataTable.filter(filtroAltura);

            // Mostrar resultados del filtro simple
            TableView view_filtro_simple = new TableView(filtrada);
            System.out.println("RESUMEN DE ÁRBOLES CON ALTURA > 10:");
            view_filtro_simple.printProlijo();

            // ==========================================================
            // 4) FILTRO COMPUESTO: altura > 10 Y especie igual a 'Fraxinus pennsylvanica'
            // ==========================================================
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

            // ==========================================================
            // 5) ORDENAMIENTO: por una columna (altura_arbol ascendente)
            // ==========================================================
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
            List<String> columnas_2 = Arrays.asList("calle_altura", "calle_chapa");
            boolean ascending = false; // Orden descendente
            Sorter sorter_2 = new SimpleSorter();
            Table tablaOrdenada_2 = sorter_2.sort(dataTable, columnas_2, ascending);

            // Visualizar la tabla ordenada por dos columnas
            System.out.println("TABLA ORDENADA POR calle_altura Y calle_chapa DESCENDENTE:");
            TableView viewOrdenada_2 = new TableView(tablaOrdenada_2);
            viewOrdenada_2.printProlijo();

            // ==========================================================
            // 7) EXPORTAR A CSV el resultado ordenado
            // ==========================================================
            CsvWriter writer = new CsvWriter();
            writer.write(tablaOrdenada_2,
                "src\\csvPrueva\\resultado_3.csv",
                ",", false); // 'false' para no incluir encabezado, 'true' para incluirlo

            // ==========================================================
            // 8) MUESTREO ALEATORIO: obtener 5 filas aleatorias
            // ==========================================================
            DataTable muestraCinco = dataTable.sample(5);

            // Mostrar la muestra de 5 filas
            System.out.println("MUESTRA DE 5 FILAS ALEATORIAS:");
            TableView viewMuestra = new TableView(muestraCinco);
            viewMuestra.printProlijo();

            // ==========================================================
            // 9) MUESTREO ALEATORIO: obtener el 20% de las filas al azar
            // ==========================================================
            DataTable muestraVeintePorciento = dataTable.sample(0.2);

            System.out.println("MUESTRA DEL 20% DE LAS FILAS:");
            TableView viewMuestraPorciento = new TableView(muestraVeintePorciento);
            viewMuestraPorciento.printProlijo();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

