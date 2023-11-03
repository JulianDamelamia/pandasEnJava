package dataframe;

import java.util.ArrayList;
import java.util.List;

import dataframe.cells.Cell;

public class Column {
    private int size;
    private ArrayList<Cell> content;
    //Constructor vacio
    public Column() {
        this.size = 0;
        this.content = new ArrayList<Cell>();
    }
    public Column(int size) {
        this.size = size;
        this.content = new ArrayList<Cell>();
    }

    public Column (ArrayList<Cell> content) {
        this.size = content.size();
        this.content = content;
    }
    public Column(int size, ArrayList<Cell> content) {
        this.size = size;
        this.content = content;
    }
    protected Column(Column column) {
        this.size = column.size;
        this.content = new ArrayList<Cell>(column.content);
    }
    //probablemente quiera esto protected para no agregar arrays de largo inválido una vez creado el df
    public void setContent(ArrayList<Cell> content){
        this.content =content;
        this.size = content.size();
    }

    public void setCell(int index, Cell cell){
        this.content.set(index, cell);
    }

    public Cell getCell(int index){
        return this.content.get(index);
    }

    public int size(){
        return this.size;
    }
    
    public Column(List<Cell> cells) {
        this.size = cells.size();
        this.content = new ArrayList<>(cells);
    }


    public ArrayList<Cell> getContent(){
        return this.content;
    }
    @Override
    public String toString(){
        String result = "";
        for (Cell cell : this.content) {
            result += cell.toString() + " ";
        }
        return result;
    }


}
