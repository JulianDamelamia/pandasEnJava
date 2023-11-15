package dataframe;

import java.util.ArrayList;
import java.util.List;

import dataframe.cells.Cell;
import dataframe.cells.NumericCell;
import dataframe.cells.StringCell;

// Esta es una clase auxiliar que será consumida únicamente cuando sea necesario ordenar o filtrar
public class Row implements Comparable<Row>{
    public String label;
    public ArrayList<Cell> cells;

    public Row() {
        this.label = "";
        this.cells = new ArrayList<Cell>();
    }
    public Row(String label, ArrayList<Cell> cells) {
        this.label = label;
        this.cells = cells;
    }
    public void addCell(Cell cell) {
        cells.add(cell);
    }
    public String getLabel() {
        return label;
    }
    public ArrayList<Cell> getCells() {
        return cells;
    }

    public Cell getCell(int index) {
        return cells.get(index);
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }
    @Override
    public int compareTo(Row otro){
        int i = 0;
        while (this.getCell(i).compareTo(otro.getCell(i)) == 0) {
           i++;
        }
        return  this.getCell(i).compareTo(otro.getCell(i));
    }

    public int compareTo(Row otro, int index){
        return this.getCell(index).compareTo(otro.getCell(index));
    }

    @Override
    public String toString() {
        String result = "";
        for (Cell cell : this.cells) {
            result += cell.toString() + " ";
        }
        return result;
    }
    
}
