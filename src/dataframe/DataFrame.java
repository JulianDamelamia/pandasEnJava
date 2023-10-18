package dataframe;
import java.util.List;
import java.util.Map;

import dataframe.Matriz;
import dataframe.cells.Cell;

public class Dataframe {
    
    private List<Integer> columns; // lista de columnas -> [hash1, hash2 , ..., hashN]
    private Map<String, Integer> cLabels; // labels de columnas-> {'nombre' : hash}
    private Map<Integer, Integer> cOrder; //  orden de columnas -> { hash : order}

    private List<Integer> rows;
    private Map<String, Integer> rLabels;
    private Map<Integer, Integer> rOrder;

    private Matriz<Cell> data;

    public Dataframe(){
        this.data = new Matriz<Cell>(1);
    }

}


