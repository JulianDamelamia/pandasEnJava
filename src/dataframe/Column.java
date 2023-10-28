package dataframe;

import java.util.ArrayList;

import dataframe.cells.Cell;

public class Column {
    private int size;
    private ArrayList<Cell> content;

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

    protected void addCell(Cell cell){
        this.content.add(size, cell);
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