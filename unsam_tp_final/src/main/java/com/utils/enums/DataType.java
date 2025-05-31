package Utils.enums;

// DataType.java - clase o interfaz de utils
import java.util.function.Function;

public enum DataType {
    INTEGER(Integer::parseInt),
    FLOAT(Float::parseFloat),
    DOUBLE(Double::parseDouble),
    STRING(s -> s),
    BOOLEAN(Boolean::parseBoolean);

    private final Function<String, ?> parser;

    DataType(Function<String, ?> parser) {
        this.parser = parser;
    }

    public Object parse(String value) {
        return parser.apply(value);
    }
}