package dataframe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataframe.cells.BooleanCell;
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

    private Column(Column original) {
        this(original.size(), original.getContent());       
    }
    public Column copy() {
        Column copyColumn = new Column(this);
        copyColumn.content = new ArrayList<Cell>(size);
        for (Cell cell : this.content) {
            Cell copyCell = cell.copy();
            copyColumn.content.add(copyCell);
        }
        return copyColumn;
    }

    public void setCell(int index, Object value){
            this.content.get(index).setValue(value);
        }

    //probablemente quiera esto protected para no agregar arrays de largo inválido una vez creado el df
    public void setContent(ArrayList<Cell> content){
        this.content =content;
        this.size = content.size();
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
