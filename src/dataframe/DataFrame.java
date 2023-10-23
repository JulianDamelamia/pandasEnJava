package dataframe;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import dataframe.Matriz;
import dataframe.cells.Cell;

public class Dataframe {
    
    private List<Column> columns; // lista de columnas -> [hash1, hash2 , ..., hashN]
    private Map<Column, String> labelsMap; // labels de columnas-> {'nombre' : hash}
    private Map<Integer, Column> ordenMap; //  orden de columnas -> { hash : order}
    private int numRows; // numero de filas
    private int numCols; // numero de columnas

    public Dataframe(){
        this.columns = new ArrayList<Column>(1);
        this.labelsMap = new HashMap<Column, String>();
        this.ordenMap = new HashMap<Integer, Column>();
        this.numRows = this.ordenMap.size();
        this.numCols = this.labelsMap.size();
    }
    
    private String[] listLabels(){
        String[] labels = new String[this.labelsMap.size()];
        for (Integer key : this.ordenMap.keySet()){
            Column column = this.ordenMap.get(key);
            String columnName = this.labelsMap.get(column);
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



    // sobrecarga de metodo printRow con y sin separador, idk si es necesario
    public void printRow(int row){
        for (Integer key : this.ordenMap.keySet()) {
            Column column = this.ordenMap.get(key);
            System.out.print(column.getContent().get(row) + "\t");
        }
    }
    public void printRow(int row, String separatorString){
        for (Integer key : this.ordenMap.keySet()) {
            Column column = this.ordenMap.get(key);
            System.out.print(column.getContent().get(row) + separatorString);
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
                String cellValue = this.ordenMap.get(i).getContent().get(j).toString();
                colWidths[i] = Math.max(colWidths[i], cellValue.length());
            }
        }
        for (int i = 0; i < labels.length; i++) {
            out += String.format("%-" + colWidths[i] + "s", labels[i]) + sep;
        }
        out += "\n";
        for(int row = 0; row < this.numRows; row++) {
            for (int i = 0; i < labels.length; i++) {
                String cellValue = this.ordenMap.get(i).getContent().get(row).toString();
                int padding = colWidths[i] - cellValue.length();
                int leftPadding = padding / 2;
                int rightPadding = padding - leftPadding;
                out += String.format("%" + leftPadding + "s%s%" + rightPadding + "s", "", cellValue, "") + sep;
            }
            out += "\n";
        }
        return out;
    }
    public void addColumn(Column column){
        this.columns.add(column);
        this.labelsMap.put(column, "Columna " + this.labelsMap.size());
        this.ordenMap.put(this.ordenMap.size(), column);
        this.numCols = this.labelsMap.size();
        this.numRows = this.ordenMap.get(0).size();
    }
    public void addColumn(Column column, String label){
        this.columns.add(column);
        this.labelsMap.put(column, label);
        this.ordenMap.put(this.ordenMap.size(), column);
        this.numCols = this.labelsMap.size();
        this.numRows = this.ordenMap.get(0).size();
    }
}


