package dataframe;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import dataframe.Matriz;
import dataframe.cells.Cell;
import dataframe.cells.NACell;

public class Dataframe {
    
    private List<Column> columns; // lista de columnas -> [hash1, hash2 , ..., hashN]
    private Map<Column, String> columnLabelsMap; // labels de columnas-> {'nombre' : hash}
    private Map<Integer, Column> columnOrderMap; //  orden de columnas -> { hash : order}
    private int numRows; // numero de filas
    private int numCols; // numero de columnas

    public Dataframe(){
        this.columns = new ArrayList<Column>(1);
        this.columnLabelsMap = new HashMap<Column, String>();
        this.columnOrderMap = new HashMap<Integer, Column>();
        this.numRows = this.columnOrderMap.size();
        this.numCols = this.columnLabelsMap.size();
    }
    
    private String[] listLabels(){
        String[] labels = new String[this.columnLabelsMap.size()];
        for (Integer key : this.columnOrderMap.keySet()){
            Column column = this.columnOrderMap.get(key);
            String columnName = this.columnLabelsMap.get(column);
            labels[key] = columnName;
        }
        return labels;
    }
    public String getLabels(){
        String[] labels = this.listLabels();
        String out = "";
        for (String label : labels){
            out += label + " | ";
        }
        out += "\n";
        return out;
    }



    // // sobrecarga de metodo printRow con y sin separador, idk si es necesario
    // public void printRow(int row){
    //     for (Integer key : this.columnOrderMap.keySet()) {
    //         Column column = this.columnOrderMap.get(key);
    //         System.out.print(column.getContent().get(row) + "\t");
    //     }
    // }
    // public void printRow(int row, String separatorString){
    //     for (Integer key : this.columnOrderMap.keySet()) {
    //         Column column = this.columnOrderMap.get(key);
    //         System.out.print(column.getContent().get(row) + separatorString);
    //     }
    // }
    

    public void addColumn(Column column){
        this.columns.add(column);
        this.columnLabelsMap.put(column, "Columna " + this.columnLabelsMap.size());
        this.columnOrderMap.put(this.columnOrderMap.size(), column);
        this.numCols = this.columnLabelsMap.size();
        this.numRows = this.columnOrderMap.get(0).size();
    }
    public void addColumn(Column column, String label){
        this.columns.add(column);
        this.columnLabelsMap.put(column, label);
        this.columnOrderMap.put(this.columnOrderMap.size(), column);
        this.numCols = this.columnLabelsMap.size();
        this.numRows = this.columnOrderMap.get(0).size();
    }


    public void addEmptyRow() {
        for (Column column: this.columns) {
            NACell cell = NACell.getInstance();
            column.addCell(cell);
        }
    }

    @Override
    public String toString() {
        String out = "";
        String sep = " | ";
        String[] labels = this.listLabels();
        int[] colWidths = new int[labels.length];
        for (int i = 0; i < labels.length; i++) {
            colWidths[i] = labels[i].length();
            for (int j = 0; j < this.numRows; j++) {
                String cellValue = this.columnOrderMap.get(i).getContent().get(j).toString();
                colWidths[i] = Math.max(colWidths[i], cellValue.length());
            }
        }
        for (int i = 0; i < labels.length; i++) {
            out += String.format("%-" + colWidths[i] + "s", labels[i]) + sep;
        }
        out += "\n";
        for(int row = 0; row < this.numRows; row++) {
            for (int i = 0; i < labels.length; i++) {
                String cellValue = this.columnOrderMap.get(i).getContent().get(row).toString();
                int padding = colWidths[i] - cellValue.length();
                int leftPadding = padding / 2;
                int rightPadding = padding - leftPadding;
                out += String.format("%" + leftPadding + "s%s%" + rightPadding + "s", "", cellValue, "") + sep;
            }
            out += "\n";
        }
        return out;
    }
}


