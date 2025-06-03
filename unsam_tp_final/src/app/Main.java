package app;

import io.interfaces.TableReader;
import io.CsvReader;
import Principal.table.DataTable;
import Principal.table.TableView;

public class Main {
    public static void main(String[] args) {
        try {
            // 1) Leemos CSV
            TableReader lector = new CsvReader();
            String filePath= "G:\\Facultad\\Algoritmos 1\\Trabajo Práctico Integrador\\Algoritmos-1-Trabajo-Final\\unsam_tp_final\\src\\csvPrueva\\arbolado-publico-lineal-2017-2018.csv";
            DataTable dataTable = lector.read(filePath, ",");

            // 2) Creamos el TableView a partir de ese DataTable
            TableView view = new TableView(dataTable);

            // 3) Mostramos resumen + todas las filas + grid
            view.printSummary();
            System.out.println();
            view.printAllRows();
            System.out.println();
            view.printAsGrid();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
