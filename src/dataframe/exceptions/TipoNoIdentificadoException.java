package dataframe.exceptions;

public class TipoNoIdentificadoException extends IllegalArgumentException {
    public TipoNoIdentificadoException() {
        super("No se pudo identificar el tipo de la celda");
    }
}