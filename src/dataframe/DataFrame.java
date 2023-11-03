package dataframe;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import dataframe.Matriz;
import dataframe.cells.BooleanCell;
import dataframe.cells.Cell;
import dataframe.cells.NACell;
import dataframe.cells.NumericCell;
import dataframe.cells.StringCell;
import src.Identificador;
public class DataFrame {
    
    private List<Column> columns; // lista de columnas -> [col1, col2 , ..., colN]
    private Map<Column, String> columnLabelsMap; // labels de columnas-> { col : 'nombre'}
    private Map<Integer, Column> columnOrderMap; //  orden de columnas -> { int : col}
    private Map<String, Integer> rowLabelsMap; // labels de columnas-> {'nombre' : int posicional}
    // private Map<String, Integer> rowOrderMap; //  orden de columnas -> { hash : int orden}
    private int numRows; // numero de filas
    private int numCols; // numero de columnas


    // Constructores 
    //-TODO: sobrecarga
    public DataFrame(){
        this.columns = new ArrayList<Column>(1);
        this.columnLabelsMap = new HashMap<Column, String>();
        this.rowLabelsMap = new HashMap<String, Integer>();
        this.columnOrderMap = new HashMap<Integer, Column>();
        this.numRows = this.columnOrderMap.size();
        this.numCols = this.columnLabelsMap.size();
    }
    
    // Agregar columnas sobrecargado
    public void addColumn(List<Cell> cells){
        Column column = new Column(cells);
        this.columns.add(column);
        this.columnLabelsMap.put(column, "Columna " + this.columnLabelsMap.size());
        this.columnOrderMap.put(this.columnOrderMap.size(), column);
        this.numCols = this.columnLabelsMap.size();
        this.numRows = this.columnOrderMap.get(0).size();
    }

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

    
    // Getter Celda
    public Cell getCell(int col, int row){
        Column column = this.columnOrderMap.get(col);
        Cell cell = column.getContent().get(row);
        return cell;
    }

    // Setters getters labels filas
    public void setEtiquetasFilas(){
        rowLabelsMap.clear();
        for(Integer i=0; i < columns.get(0).size(); i++) {
            rowLabelsMap.put(i.toString(), i);
        }
    }

     public String getRowLabels(){
        if(this.rowLabelsMap.size() == 0){
            setEtiquetasFilas();
        }
        String[] rowLabels = this.listRowLabels();
        String out = "";
        for (String label : rowLabels){
            out += label + " | ";
        }
        out += "\n";
        return out;
    }
    private String[] listRowLabels(){
        String[] labels = new String[this.rowLabelsMap.size()];
        int i = 0;
        for (String key : this.rowLabelsMap.keySet()){
            labels[i] = key;
            i++;
        }
        return labels;
    }
    
    // Setters getters labels columnas

    public void setColumLabels(String[] labels) throws IndexOutOfBoundsException{
        columnLabelsMap.clear();
        for (Integer key : this.columnOrderMap.keySet()){
            Column column = this.columnOrderMap.get(key);
            String columnName = labels[key];
            columnLabelsMap.put(column, columnName);
        }
    }
    
    public String getColumnLabels(){
        String[] colLabels = this.listColumnLabels();
        String out = "";
        for (String label : colLabels){
            out += label + " | ";
        }
        out += "\n";
        return out;
    }
    
    private String[] listColumnLabels(){
        String[] labels = new String[this.columnLabelsMap.size()];
        for (Integer key : this.columnOrderMap.keySet()){
            Column column = this.columnOrderMap.get(key);
            String columnName = this.columnLabelsMap.get(column);
            labels[key] = columnName;
        }
        return labels;
    }
   

    // Getters dimensiones y tipos
    public int getCantColumnas(){
        return this.columns.size();
    }

    public int getCantFilas(){
        int numRows =  this.columns.get(0).size();
        return numRows;
    }

    public Integer[] getDimensions() {
        int numCols = getCantColumnas();
        int numRows = getCantFilas();
        return new Integer[]{numRows, numCols};
    }
    


    public String getColumnType(int colNumber){
        Identificador identificador = null;
        String[] labels = this.listColumnLabels();
        
        for (int i = 0; i < labels.length; i++) {
            if(i == colNumber){
                    String celda = this.columnOrderMap.get(i).getContent().get(1).toString();
                    identificador = new Identificador(celda);
            }
        }
        return identificador.getType();
    }



    // To String
    public String toString(String separador) {
        String out = "";
        String sep = " " + separador + " ";
        String[] labels = this.listColumnLabels();
        int[] colWidths = new int[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            colWidths[i] = ("["+labels[i]+"]").length();
            for (int j = 0; j < this.numRows; j++) {
                String cellValue = this.columnOrderMap.get(i).getContent().get(j).toString();
                colWidths[i] = Math.max(colWidths[i], cellValue.length());
            }
        }
        
        for (int i = 0; i < labels.length; i++) {
            if(i == 0){
                out += String.format("%-" + colWidths[i] + "s", "") + sep;    
            }
            out += String.format("%-" + colWidths[i] + "s", "[" + labels[i] + "]")  + sep;
        }
        out += "\n";
        
        for (Integer row = 0; row < this.numRows; row++) {
                this.rowLabelsMap.put(row.toString(), row);
                int rowPadding = colWidths[0] - row.toString().length();
                int leftRowPadding = rowPadding / 2;
                int rightRowPadding = rowPadding - leftRowPadding;
            out += String.format("%-" + (leftRowPadding + row.toString().length() + rightRowPadding) + "s", "[Fila: " + row + "]") + sep;
            for (int i = 0; i < labels.length; i++) {
                String cellValue = this.columnOrderMap.get(i).getContent().get(row).toString();
                int padding = colWidths[i] - cellValue.length();
                int leftPadding = padding / 2;
                int rightPadding = padding - leftPadding;
                
                out += String.format("%-" + (leftPadding + cellValue.length() + rightPadding) + "s", cellValue) + sep;
            }
            out += "\n";
        }
        
        return out;
    }
}


