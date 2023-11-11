package dataframe;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import dataframe.cells.Cell;
import src.Identificador;

public class DataFrame {
    
    public List<Column> columns; // lista de columnas -> [col1, col2 , ..., colN]
    public Map<Column, String> columnLabelsMap; // labels de columnas-> { col : 'nombre'}
    public Map<Integer, Column> columnOrderMap; //  orden de columnas -> { int : col}
    public Map<String, Integer> rowLabelsMap; // labels de columnas-> {'nombre' : int posicional}
    // private Map<String, Integer> rowOrderMap; //  orden de columnas -> { hash : int orden}
    public int numRows; // numero de filas
    public int numCols; // numero de columnas

    // Constructores 
    //-TODO: sobrecarga
    public DataFrame(){
        
        /*
         * Constructor de la clase DataFrame
         * 
         * @param columns: lista de columnas
         * @param columnLabelsMap: diccionario de etiquetas de columnas
         * @param columnOrderMap: diccionario de orden de columnas
         * @param rowLabelsMap: diccionario de etiquetas de filas
         * @param rowOrderMap: diccionario de orden de filas
         * @param numRows: numero de filas
         * @param numCols: numero de columnas
         * 
         * @return DataFrame
         * 
         */
        this.columns = new ArrayList<Column>(1);
        this.columnLabelsMap = new HashMap<Column, String>();
        this.columnOrderMap = new HashMap<Integer, Column>();
        this.rowLabelsMap = new HashMap<String, Integer>();
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

    
    // Getter Setter Celda
    public Cell getCell(int col, int row){
        Column column = this.columnOrderMap.get(col);
        Cell cell = column.getContent().get(row);
        return cell;
    }

    public void setCell(int col, int row, Cell cell){
        Column column = this.columnOrderMap.get(col);
        column.setCell(row, cell);
    }

    public void setCell(int col, int index, Object value){
        Column column = this.columnOrderMap.get(col);
        column.setCell(index, value);
    }

    public void setCell(String colLabel, int row, Cell cell){
        Column column = null;
        for (Map.Entry<Column, String> entry : this.columnLabelsMap.entrySet()) {
            if (entry.getValue().equals(colLabel)) {
                column = entry.getKey();
                break;
            }
            else{
                System.out.println("No se encontró la columna");
            }
        }
        if (column != null) {
            column.setCell(row, cell);
        }
    }

    // Setters getters labels filas
    public void setRowLabels(){
        rowLabelsMap.clear();
        for(Integer i=0; i < columns.get(0).size(); i++) {
            rowLabelsMap.put(i.toString(), i);
        }
    }

     public String getRowLabels(){
        if(this.rowLabelsMap.size() == 0){
            setRowLabels();
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
        if (labels.length != this.columns.size()){
            throw new IndexOutOfBoundsException("La cantidad de labels no coincide con la cantidad de columnas");
        }
        else{
            for (int i = 0; i < this.columns.size(); i++){
            String columnName = labels[i];
            columnLabelsMap.put(this.columns.get(i), columnName);
            }
        }
        
    }
    public void setColumLabels(Map<Column, String> labelsMap){
        for (Column column : labelsMap.keySet()){
            String columnName = labelsMap.get(column);
            columnLabelsMap.put(column, columnName);
        }
    }

    public void setColumLabels(Column column, String columnName){
        columnLabelsMap.put(column, columnName);
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
      
    //SHALLOW COPY
    private DataFrame(DataFrame dataframe) {
        this.columns = new ArrayList<Column>();
        this.columnLabelsMap = new HashMap<Column, String>(dataframe.columnLabelsMap);
        this.rowLabelsMap = new HashMap<String, Integer>(dataframe.rowLabelsMap);
        this.columnOrderMap = new HashMap<Integer, Column>(dataframe.columnOrderMap);
        this.numRows = this.columnOrderMap.size();
        this.numCols = this.columnLabelsMap.size();
    }
    // Deep Copy
    // public DataFrame copy() {
    //     DataFrame dfOrigen = this;  // solo para aclarar
    //     DataFrame copia = new DataFrame(dfOrigen);
    //     copia.columns = new ArrayList<Column>();
    //     // for(int i = 0; i< dfOrigen.columns.size(); i++){
    //     //     Column copiaColumn = dfOrigen.columns.get(i).copy();
    //     //     copia.columns.add(copiaColumn);
    //     //     copia.columnLabelsMap.put(copiaColumn, dfOrigen.columnLabelsMap.get(dfOrigen.columns.get(i)));
    //     //     copia.columnOrderMap.put(i, copiaColumn);
    //     // }

    //     return copia;
    // }
    public DataFrame copy() {
        DataFrame copia = new DataFrame();
        
        for (Column columnaOriginal : this.columns) {
            Column columnaCopia;
            List<Cell> contenidoCopia = new ArrayList<Cell>();
            for (Cell celdaOriginal : columnaOriginal.getContent()) {
                // Utiliza el método copy() de Cell para realizar una copia profunda de las celdas
                Cell celdaCopia = celdaOriginal.copy();
                contenidoCopia.add(celdaCopia);
            }
            columnaCopia = new Column(contenidoCopia);
            copia.addColumn(columnaCopia);
            
            // Copia los mapeos de etiquetas y órdenes de las columnas
            String label = this.columnLabelsMap.get(columnaOriginal);
            copia.columnLabelsMap.put(columnaCopia, label);
            
            Integer order = this.columnOrderMap.entrySet().stream()
                                                .filter(entry -> entry.getValue().equals(columnaOriginal))
                                                .map(Map.Entry::getKey)
                                                .findFirst()
                                                .orElse(null);
            if (order != null) {
                copia.columnOrderMap.put(order, columnaCopia);
            }
        }

        // Copia el mapeo de etiquetas de filas
        copia.rowLabelsMap.putAll(this.rowLabelsMap);

        // Copia el número de filas y columnas
        copia.numRows = this.numRows;
        copia.numCols = this.numCols;

        return copia;
    }

    // To String

    //     copia.columns = new ArrayList<Column>();
    //     // for(int i = 0; i< dfOrigen.columns.size(); i++){
    //     //     Column copiaColumn = dfOrigen.columns.get(i).copy();
    //     //     copia.columns.add(copiaColumn);
    //     //     copia.columnLabelsMap.put(copiaColumn, dfOrigen.columnLabelsMap.get(dfOrigen.columns.get(i)));
    //     //     copia.columnOrderMap.put(i, copiaColumn);
    //     // }

    //     return copia;
    // }
    public DataFrame copy() {
        DataFrame copia = new DataFrame();
        
        for (Column columnaOriginal : this.columns) {
            Column columnaCopia;
            List<Cell> contenidoCopia = new ArrayList<Cell>();
            for (Cell celdaOriginal : columnaOriginal.getContent()) {
                // Utiliza el método copy() de Cell para realizar una copia profunda de las celdas
                Cell celdaCopia = celdaOriginal.copy();
                contenidoCopia.add(celdaCopia);
            }
            columnaCopia = new Column(contenidoCopia);
            copia.addColumn(columnaCopia);
            
            // Copia los mapeos de etiquetas y órdenes de las columnas
            String label = this.columnLabelsMap.get(columnaOriginal);
            copia.columnLabelsMap.put(columnaCopia, label);
            
            Integer order = this.columnOrderMap.entrySet().stream()
                                                .filter(entry -> entry.getValue().equals(columnaOriginal))
                                                .map(Map.Entry::getKey)
                                                .findFirst()
                                                .orElse(null);
            if (order != null) {
                copia.columnOrderMap.put(order, columnaCopia);
            }
        }

        // Copia el mapeo de etiquetas de filas
        copia.rowLabelsMap.putAll(this.rowLabelsMap);

        // Copia el número de filas y columnas
        copia.numRows = this.numRows;
        copia.numCols = this.numCols;

        return copia;
    }

    public Column getColumn(int col){
        /*
         * Metodo para obtener la columna
         * 
         * @param col: numero de columna
         * 
         * @return column
         * 
         */

        Column column = this.columnOrderMap.get(col);
        return column;
    }
    
    public List<Column> getColumns(){
        /*
         * Metodo para obtener las columnas
         * 
         * @param columns: lista de columnas
         * 
         * @return columns
         * 
         */
        return this.columns;
    }

    public String toString(String separador) {
        if (separador == null) {
            separador = " | ";
        }
        
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


