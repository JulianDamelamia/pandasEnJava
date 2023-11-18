package dataframe;

import java.util.ArrayList;
import dataframe.cells.Cell;

// Esta es una clase auxiliar que será consumida únicamente cuando sea necesario ordenar o filtrar
public class Row implements Comparable<Row>{
    protected String label;
    protected ArrayList<Cell> cells;

    protected Row() {
        this.label = "";
        this.cells = new ArrayList<Cell>();
    }
    protected Row(String label, ArrayList<Cell> cells) {
        this.label = label;
        this.cells = cells;
    }
    protected void addCell(Cell cell) {
        cells.add(cell);
    }
    protected String getLabel() {
        return label;
    }
    protected ArrayList<Cell> getCells() {
        return cells;
    }

    protected Cell getCell(int index) {
        return cells.get(index);
    }
    protected void setLabel(String label) {
        this.label = label;
    }
    protected void setCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }
    @Override
    public int compareTo(Row otro){
        int i = 0;
        while (this.getCell(i).compareTo(otro.getCell(i)) == 0 && i < this.cells.size() - 1) {
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
