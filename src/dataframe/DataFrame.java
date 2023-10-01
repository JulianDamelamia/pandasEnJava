package dataframe;
import java.util.List;
import dataframe.Column;

// Realmente no sé si esto debería funcionar como lista de columnas o será más eficiente tener un array 2D de objetos
// Por un lado, al tener las columnas como objetos, se puede acceder a sus atributos y métodos de forma más sencilla
// Por otro lado, es más tedioso de implementar y no sé si es más eficiente que un array 2D de objetos

class DataFrame {
    
    private List<Column> columns;
    private String[] cLabels;
    private String[] rLabels;
    private DType[] dtypes;
    private int length;
    private int width;
}


