package Principal.filter;

import java.util.*;
import Principal.table.Row;
import utils.enums.LogicalOperator;

public class CompFilter implements Filter {
    private final List<Filter> filters;
    private final LogicalOperator logicalOp;

    public CompFilter(List<Filter> filters, LogicalOperator logicalOp) {
        if (logicalOp == LogicalOperator.NOT && filters.size() != 1) {
            throw new IllegalArgumentException("NOT debe recibir exactamente un filtro");
        }
        if ((logicalOp == LogicalOperator.AND || logicalOp == LogicalOperator.OR) && filters.size() < 2) {
            throw new IllegalArgumentException("AND/OR requieren al menos dos filtros");
        }
        this.filters = filters;
        this.logicalOp = logicalOp;
    }

    @Override
    public boolean apply(Row row) {
        switch (logicalOp) {
            case AND:
                for (Filter f : filters) {
                    if (!f.apply(row)) return false;
                }
                return true;
            case OR:
                for (Filter f : filters) {
                    if (f.apply(row)) return true;
                }
                return false;
            case NOT:
                return !filters.get(0).apply(row);
            default:
                throw new UnsupportedOperationException("Operador lógico no soportado");
        }
    }
}
