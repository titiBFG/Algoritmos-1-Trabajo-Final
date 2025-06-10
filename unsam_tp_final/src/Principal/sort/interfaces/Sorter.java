// Sorter.java - clase o interfaz de core
package Principal.sort.interfaces;

import java.util.List;

import Principal.table.Table;

public interface Sorter {
    Table sort(Table table, List<String> columns, boolean ascending);

}
