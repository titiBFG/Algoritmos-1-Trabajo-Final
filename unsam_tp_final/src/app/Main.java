// Main.java - clase o interfaz de app
package app;

import io.CsvReader;
import Principal.table.DataTable;


public class Main {
    public static void main(String[] args) {
        CsvReader reader = new CsvReader();

        String filepath = "G:\\Facultad\\Algoritmos 1\\Trabajo Práctico Integrador\\Algoritmos-1-Trabajo-Final\\unsam_tp_final\\src\\csvPrueva\\arbolado-publico-lineal-2017-2018.csv";

        DataTable table = reader.read(filepath, ",");

        if (table != null){
            table.showColumnCount();
            table.showRowCount();
            table.showColumnTypes();

            // Ejemplo de acceso a un valor específico
            Object value = table.getValue(0, "long");
            System.out.println("Valor de la primera fila en la columna 'long': " + value);
        } else {
            System.out.println("Error al leer el archivo o tabla vacía.");

        }

    }
}
