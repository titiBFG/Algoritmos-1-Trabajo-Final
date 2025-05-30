// Main.java - clase o interfaz de app
package app;

import IO.CsvReader;
import Principal.table.DataTable;

public class Main {
    public static void main(String[] args) {
        new CsvReader();
        DataTable tabla = CsvReader.read("src/main/resources/prueba.csv");

        tabla.showColumnCount();
        tabla.showRowCount();
        tabla.showColumnTypes();
    }
}