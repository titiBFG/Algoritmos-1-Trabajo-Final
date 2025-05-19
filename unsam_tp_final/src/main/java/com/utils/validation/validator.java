package validation;

import javax.print.attribute.standard.MediaSize.NA;
import enums.DataType;

public class validator {
    
    public boolean validate(Object value, DataType type) {
    // 1) Si es NA, consideralo siempre válido
    if (value instanceof NA) {
        return true;
    }
    // 2) Si no es NA, validá contra el tipo esperado
    switch (type) {
        case NUMERIC:
            return value instanceof Number;
        case STRING:
            return value instanceof String;
        case BOOLEAN:
            return value instanceof Boolean;
        // etc.
        default:
            return false;
    }
}

}
