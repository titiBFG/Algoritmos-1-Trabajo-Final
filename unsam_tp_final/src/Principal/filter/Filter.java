// Filter.java - clase o interfaz de core
package Principal.filter;

import Principal.table.RowView;

public interface Filter{

    boolean apply(RowView row);

}