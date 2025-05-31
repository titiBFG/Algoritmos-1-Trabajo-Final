// Main.java - clase o interfaz de app
package app;

import java.io.IOException;

import IO.CsvReader;
import Principal.table.DataTable;

public class Main {
    public static void main(String[] args) throws IOException {
        CsvReader csvReader = new CsvReader();
        DataTable tabla = csvReader.read("src/main/resources/prueba.csv", ",", true);

        tabla.showColumnCount();
        tabla.showRowCount();
        tabla.showColumnTypes();
    }
}