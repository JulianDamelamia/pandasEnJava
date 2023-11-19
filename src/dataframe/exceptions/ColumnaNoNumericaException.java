package dataframe.exceptions;

public class ColumnaNoNumericaException extends IllegalArgumentException {
    public ColumnaNoNumericaException() {
        super("La columna no contiene valores numéricos.");
    }
}

