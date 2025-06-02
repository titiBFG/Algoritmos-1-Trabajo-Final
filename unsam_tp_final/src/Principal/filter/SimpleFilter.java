// SimpleFilter.java - clase o interfaz de core
package Principal.filter;

import Principal.table.RowView;
import utils.enums.Operator;
import java.util.Objects;

public class SimpleFilter implements Filter {
    private final String columnName;
    private final Operator operator;
    private final Object compareValue;

    public SimpleFilter(String columnName, Operator operator, Object compareValue){
        if (columnName == null) {
        throw new NullPointerException("columnName no puede ser null");
    }
    if (operator == null) {
        throw new NullPointerException("operator no puede ser null");
    }   
        this.columnName = columnName;
        this.operator = operator;
        this.compareValue = compareValue;
    }

    @Override

    public boolean apply(RowView row){
        Object value = row.getValue(columnName);

        switch (operator){
            case EQ:
                return Objects.equals(value, compareValue);
            case EN:
                return !Objects.equals(value, compareValue);
            case GT:
                if (value instanceof Comparable && compareValue instanceof Comparable){
                    Comparable<Object> c1 = (Comparable<Object>) value;
                    return c1.compareTo(compareValue) > 0;
                }
                throw new IllegalArgumentException("Valores no comparables: " + value + " y " + compareValue);
            case LT:
                if (value instanceof Comparable && compareValue instanceof Comparable){
                    Comparable<Object> c2 = (Comparable<Object>) value;
                    return c2.compareTo(compareValue) < 0;
                }
                throw new IllegalArgumentException("Valores no comparables: " + value + " y " + compareValue);
            case GE:
                if (value instanceof Comparable && compareValue instanceof Comparable){
                    Comparable<Object> c3 = (Comparable<Object>) value;
                    return c3.compareTo(compareValue) >= 0;
                }
                throw new IllegalArgumentException("Valores no comparables: " + value + " y " + compareValue);
            case LE:
                if (value instanceof Comparable && compareValue instanceof Comparable){
                    Comparable<Object> c4 = (Comparable<Object>) value;
                    return c4.compareTo(compareValue) <= 0;
                }
                throw new IllegalArgumentException("Valores no comparables: " + value + " y " + compareValue);
            default:
                throw new IllegalArgumentException("Operador no soportado: " + operator);
        }
    }
}



