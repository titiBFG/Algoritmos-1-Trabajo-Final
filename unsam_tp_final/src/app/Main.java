package app;

import io.interfaces.TableReader;
import utils.enums.LogicalOperator;
import utils.enums.Operator;
import io.CsvReader;
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
            // 1) Leemos CSV
            TableReader lector = new CsvReader();
            String filePath= "C:\\Users\\Julian\\OneDrive\\Desktop\\Algoritmos-1-Trabajo-Final-1\\unsam_tp_final\\src\\csvPrueva\\arbolado-publico-lineal-2017-2018.csv";
            DataTable dataTable = lector.read(filePath, ",");

            // 2) Creamos el TableView a partir de ese DataTable
            TableView view = new TableView(dataTable);

            // 3) Mostramos resumen + todas las filas + grid
            view.printSummary();
            System.out.println();
            view.printAllRows();
            System.out.println();
            view.printAsGrid();


                // 2) Filtramos: por ejemplo, altura_arbol > 10
            Filter filtroAltura = new SimpleFilter("altura_arbol", Operator.GT, 10);
            DataTable filtrada = (DataTable) dataTable.filter(filtroAltura);

            // 3) Mostramos las filas filtradas
            TableView view_filtro_simple = new TableView(filtrada);
            System.out.println("RESUMEN DE ÁRBOLES CON ALTURA > 10:");
            view_filtro_simple.printSummary();
            System.out.println();
            view_filtro_simple.printAllRows();
            System.out.println();
            view_filtro_simple.printAsGrid();

            // 2) Creamos dos filtros simples
        Filter filtroEspecie = new SimpleFilter("nombre_cientifico", Operator.EQ, "Fraxinus pennsylvanica");

        // 3) Combinamos ambos en un CompFilter con AND
        Filter compFilter = new CompFilter(
            java.util.Arrays.asList(filtroAltura, filtroEspecie),
            LogicalOperator.AND
        );

        // 4) Aplicamos el filtro compuesto
        DataTable filtrada_combinada = (DataTable) dataTable.filter(compFilter);

        // 5) Mostramos los resultados
        TableView viewComp = new TableView(filtrada_combinada);
        System.out.println("RESUMEN DE ÁRBOLES CON ALTURA > 10 Y ESPECIE 'Fraxinus pennsylvanica':");
        viewComp.printSummary();
        viewComp.printAllRows();
        viewComp.printAsGrid();

        // 1) Crear el sorter para altura_arbol ascendente
        Sorter sorter = new SimpleSorter();
        List<String> columnas = java.util.Collections.singletonList("altura_arbol");
        Table tablaOrdenada = sorter.sort(dataTable, columnas, true);

        // 2) Visualizar la tabla ordenada
        TableView viewOrdenada = new TableView(tablaOrdenada);
        System.out.println("TABLA ORDENADA POR altura_arbol ASCENDENTE:");
        viewOrdenada.printSummary();
        viewOrdenada.printAllRows();
        viewOrdenada.printAsGrid();
        
        // Ahora, ordenamos por dos columnas: comuna y altura_arbol
        // 1) Columnas a ordenar (en orden de prioridad)

        List<String> columnas_2 = Arrays.asList("calle_altura", "calle_chapa");

        // 2) Orden ascendente (true) o descendente (false)
        boolean ascending = false; // o false si querés todo descendente

        // 3) Crear el sorter y ordenar
        Sorter sorter_2 = new SimpleSorter();
        Table tablaOrdenada_2 = sorter_2.sort(dataTable, columnas_2, ascending);

        // 4) Mostrar la tabla ordenada
        System.out.println("TABLA ORDENADA POR comuna Y altura_arbol ASCENDENTE:");
        TableView viewOrdenada_2 = new TableView(tablaOrdenada_2);
        viewOrdenada_2.printSummary();
        viewOrdenada_2.printAsGrid();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
