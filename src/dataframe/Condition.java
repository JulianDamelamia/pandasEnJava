package dataframe;

public class Condition {
    private String columna;
    private String operador;
    private Object valor;

    public Condition(String columna, String operador, Object valor) {
        this.columna = columna;
        this.operador = operador;
        this.valor = valor;
    }

    public String getColumna() {
        return columna;
    }

    public String getOperador() {
        return operador;
    }

    public Object getValor() {
        return valor;
    }
    // Opcional: Override de toString para una representación más clara
    @Override
    public String toString() {
        return columna + " " + operador + " " + valor.toString();
    }
}

