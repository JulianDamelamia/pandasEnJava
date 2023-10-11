package dataframe;

import java.util.List;

import dataframe.cells.Cell;

public abstract class Column {
    private int size;
    private List<Cell> content;

    public Column(int size) {
        this.size = size;
    }
    public Column(int size, List<Cell> content) {
        this.size = size;
        this.content = content;
    }

    public abstract void setAs(dType dtype);


}
