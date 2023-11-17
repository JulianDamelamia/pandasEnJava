package dataframe;

import dataframe.cells.BooleanCell;
import dataframe.cells.Cell;
import dataframe.cells.NACell;
import dataframe.cells.NumericCell;
import dataframe.cells.StringCell;
import utils.Archivos;
import utils_df.Criterios;
import utils_df.Identificador;
import utils_df.RandomSample;
import utils_df.Selection;
import utils_df.Summarise;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
public class DataFrame {

  public List<Column> columns; // lista de columnas -> [col1, col2 , ..., colN]
  public Map<String, Column> columnLabelsMap; // labels de columnas-> { 'nombre': col }
  public Map<Integer, Column> columnOrderMap; //  orden de columnas -> { int : col}
  public Map<String, Integer> rowLabelsMap; // labels de filas -> {'nombre' : int posicional}
  public Map<Integer, String> rowOrderMap; // orden de filas -> {int posicional : 'nombre'}
  public int numRows; // numero de filas
  public int numCols; // numero de columnas

  public DataFrame filter(String colLabel, String operador, Object valor) throws IllegalArgumentException {
    String[] filasSalida;
  
    if (valor instanceof Number) {
        filasSalida = filteredArray(colLabel, operador, (Number) valor);
    } else if (valor instanceof String) {
        filasSalida = filteredArray(colLabel, operador, (String) valor);
    } else if (valor instanceof Boolean) {
        filasSalida = filteredArray(colLabel, operador, (Boolean) valor);
    } else {
        throw new IllegalArgumentException("El valor debe ser un número, un string o un booleano");
    }
    String[] colLabels = this.columnLabelsMap.keySet().toArray(new String[0]);
    DataFrame dfSalida = this.select(filasSalida, colLabels);
    return dfSalida;
  }

  public String[] filteredArray(String colLabel, String operador, Number valor) throws IllegalArgumentException{
    Cell celdaAuxiliar = new NumericCell(valor);
    return filterSetup(colLabel, operador, celdaAuxiliar).toArray(String[]::new);
  }

  public String[] filteredArray(String colLabel, String operador, String valor) throws IllegalArgumentException{
    Cell celdaAuxiliar = new StringCell(valor);
    return filterSetup(colLabel, operador, celdaAuxiliar).toArray(String[]::new);
  }

  public String[] filteredArray(String colLabel, String operador, Boolean valor) throws IllegalArgumentException{
    Cell celdaAuxiliar = new BooleanCell(valor);
    return filterSetup(colLabel, operador, celdaAuxiliar).toArray(String[]::new);
  }

  private List<String> filterSetup(String colLabel, String operador, Cell celdaAuxiliar) throws IllegalArgumentException{
    Criterios criterios = new Criterios(celdaAuxiliar);
    Predicate<Cell> condicion = criterios.operadores.get(operador);
    List<String> filasSalida = new ArrayList<>();
    if (condicion != null){
        for( String rowLabel : rowLabelsMap.keySet()){
              Cell valorAcomparar = this.getColumn(colLabel).getCell(this.rowLabelsMap.get(rowLabel));
            if ( condicion.test(valorAcomparar) ){
                filasSalida.add(rowLabel);
            }
        }
      }else{
          throw new IllegalArgumentException();
      }
    return filasSalida;
  }

  public Row getRow(int index){
    Row row = new Row();
    row.setLabel(this.rowOrderMap.get(index));
    for (int j = 0; j < this.numCols; j++) {
      row.addCell(this.getCell(j, index));
    }
    return row;
  }

  public Row getRow(String label){
    Row row = new Row();
    row.setLabel(label);
    for (int j = 0; j < this.numCols; j++) {
      row.addCell(this.getCell(j, this.rowLabelsMap.get(label)));
    }
    return row;
  }

  public void setRowLabel(String vieja, String nueva){
    Integer index = this.rowLabelsMap.get(vieja); // {"0" : 0, "1" : 1, "2" : 2} --> vieja = "0" --> index = 0
    this.rowLabelsMap.remove(vieja); // {"1" : 1, "2" : 2}
    this.rowLabelsMap.put(nueva, index); // {"1" : 1, "2" : 2, "nueva" : 0}
    this.rowOrderMap.put(index, nueva);// rowOrderMap = {0 : "0", 1 : "1", 2 : "2"} --> rowOrderMap = {0 : "nueva", 1 : "1", 2 : "2"}
  }

  private ArrayList<Row> getRows(){
    ArrayList<Row> rows = new ArrayList<Row>();
    for (String label: this.rowLabelsMap.keySet()) {
      Row row = new Row();
      row.setLabel(label);
      for (int j = 0; j < this.numCols; j++) {
        row.addCell(this.getCell(j, rowLabelsMap.get(label)));
      }
      rows.add(row);
    }
    return rows;
  }

  public DataFrame sort() {
    DataFrame sortedDf = this.shallowCopy();
    HashMap<Integer, String> sortedMap = quickSort(sortedDf.getRows(), 0, sortedDf.numRows - 1);
    sortedDf.rowOrderMap = sortedMap;
    return sortedDf;
  }
  public DataFrame sort(String colLabel) {
    DataFrame sortedDf = this.shallowCopy();
    //{"label" : Columna} colLabel
    // {int : Columna} columnOrderMap
    Column column = sortedDf.columnLabelsMap.get(colLabel);
    
    if(sortedDf.columnOrderMap.get(0) != column){
      HashMap<Integer, Column> columnOrderAuxiliar = new HashMap<Integer, Column>();
      columnOrderAuxiliar.putAll(this.columnOrderMap);
      for (Map.Entry<Integer, Column> parKV : this.columnOrderMap.entrySet()) {
        if(parKV.getValue().equals(column)){
          System.out.println("Encontrado! entrada nro " + parKV.getKey());
          columnOrderAuxiliar.put(parKV.getKey(), this.columnOrderMap.get(0));
          columnOrderAuxiliar.put(0,column );
          break;
        }
      }
      sortedDf.columnOrderMap = columnOrderAuxiliar;
    }


    HashMap<Integer, String> sortedMap = quickSort(sortedDf.getRows(), 0, sortedDf.numRows - 1);
    sortedDf.rowOrderMap = sortedMap;
    sortedDf.columnOrderMap = this.columnOrderMap;
    return sortedDf;
  }

  private HashMap<Integer, String> quickSort(ArrayList<Row> rows, int low, int high) {
    if (low < high) {
      int pi = particion(rows, low, high);
      quickSort(rows, low, pi - 1);
      quickSort(rows, pi + 1, high);
      };

    HashMap<Integer, String> sortedMap = new HashMap<>();
      
    for(int i=0; i<rows.size(); i++){
      sortedMap.put((Integer) i, (String)(rows.get(i).label));
    }
    return sortedMap;
  }

  public DataFrame select(String[] rowLabels, String[] colLabels) throws IllegalArgumentException{
    DataFrame seleccion = this.shallowCopy();
    System.out.println("Cuidado! esta es una copia superficial. Las referencias a las columnas hacen referencia al mismo objeto");
    seleccion.columnOrderMap = new HashMap<>();
    int i = 0;
    for(String col: colLabels){
      if(!this.columnLabelsMap.containsKey(col)){
        throw new IllegalArgumentException("La columna " + col + " no existe");
      }
      seleccion.columnOrderMap.put( i, this.columnLabelsMap.get(col));
      i++;
    }

    seleccion.rowOrderMap = new HashMap<>();
    int j = 0;
    for(String row: rowLabels){
      if(!this.rowLabelsMap.containsKey(row)){
        throw new IllegalArgumentException("La fila " + row + " no existe");
      }
      seleccion.rowOrderMap.put(j, row);
      j++;
      seleccion.numRows = j;
      seleccion.numCols = i;
    }
    return seleccion;
  }

  private int particion (ArrayList<Row> rows, int low, int high) {
    Row pivot = rows.get(high);
    int i = low - 1;
    for (int j = low; j < high; j++) {
      if (rows.get(j).compareTo(pivot) < 0) {
        i++;
        Row temp = rows.get(i);
        rows.set(i, rows.get(j));
        rows.set(j, temp);
      }
    }
    Row temp = rows.get(i + 1);
    rows.set(i + 1, rows.get(high));
    rows.set(high, temp);
    return i + 1;
  }

  public DataFrame shallowCopy() {
    DataFrame copy = new DataFrame();
    copy.columns = this.columns;
    copy.columnLabelsMap = this.columnLabelsMap;
    copy.columnOrderMap = this.columnOrderMap;
    copy.rowLabelsMap = this.rowLabelsMap;
    copy.numRows = this.numRows;
    copy.numCols = this.numCols;
    copy.setRowLabels();
    return copy;
  }

  // Constructores
  //-TODO: sobrecarga
  public DataFrame() {
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
    this.columnLabelsMap = new HashMap<String, Column>();
    this.columnOrderMap = new HashMap<Integer, Column>();
    this.rowLabelsMap = new HashMap<String, Integer>();
    this.rowOrderMap = new HashMap<Integer, String>();
    this.numRows = this.columnOrderMap.size();
    this.numCols = this.columnLabelsMap.size();
  }

  public DataFrame(Object[][] matriz) {
    this.columns = new ArrayList<>();
    this.columnLabelsMap = new HashMap<>();
    this.columnOrderMap = new HashMap<>();
    this.rowLabelsMap = new HashMap<>();
    this.rowOrderMap = new HashMap<>();
    this.numRows = 0;
    this.numCols = 0;

    if (matriz.length > 0) {
        this.numRows = matriz.length;
        this.numCols = matriz[0].length;

        // Inicialización de las columnas
        for (int j = 0; j < this.numCols; j++) {
            Column newColumn = new Column();
            ArrayList<Cell> cells = new ArrayList<>();
            Identificador identif;
            for (int i = 0; i < this.numRows; i++) {
              identif = new Identificador(matriz[i][j]);
              Object obj = matriz[i][j];
              switch (identif.getType()) {
                case "STRING":
                    StringCell strCell = new StringCell(obj.toString());
                    cells.add(strCell);
                    break;
                case "FLOAT":
                    if (obj instanceof Float) {
                        NumericCell fltCell = new NumericCell((Float) obj);
                        cells.add(fltCell);
                    }
                    break;
                case "INTEGER":
                    if (obj instanceof Integer) {
                        NumericCell intCell = new NumericCell((Integer) obj);
                        cells.add(intCell);
                    }
                    break;
                case "NA":
                    NACell naCell = new NACell();
                    cells.add(naCell);
                    break;
                case "BOOLEAN":
                    if (obj instanceof Boolean) {
                        BooleanCell blnCell = new BooleanCell((Boolean) obj);
                        cells.add(blnCell);
                    }
                    break;
                default:
                    StringCell defaultCell = new StringCell(obj.toString());
                    cells.add(defaultCell);
                    break;
            }
            }
            newColumn.setContent(cells);
            this.columns.add(newColumn);
            this.columnLabelsMap.put("Columna " + j,newColumn); // Puedes cambiar esto por nombres de columnas deseados
            this.columnOrderMap.put(j, newColumn);
        }

        // Inicialización de etiquetas de filas
        setRowLabels();
    }
}

 

  /**
   * Agrega una nueva columna al DataFrame con las celdas especificadas.
   * 
   * @param cells la lista de celdas que conforman la nueva columna
   */
  public void addColumn(List<Cell> cells) {
    Column column = new Column(cells);
    this.columns.add(column);
    this.columnLabelsMap.put("Columna " + this.columnLabelsMap.size(), column);
    this.columnOrderMap.put(this.columnOrderMap.size(), column);
    this.numCols = this.columnLabelsMap.size();
    this.numRows = this.columnOrderMap.get(0).size();
    if(this.rowLabelsMap.size() == 0){
      setRowLabels();
    } 
  }

  /**
   * Agrega una columna al DataFrame.
   * 
   * @param column la columna a agregar
   */
  public void addColumn(Column column) {
    this.columns.add(column);
    this.columnLabelsMap.put("Columna " + this.columnLabelsMap.size(), column);
    this.columnOrderMap.put(this.columnOrderMap.size(), column);
    this.numCols = this.columnLabelsMap.size();
    this.numRows = this.columnOrderMap.get(0).size();
    if(this.rowLabelsMap.size() == 0){
      setRowLabels();
    }
  }

  /**
   * Agrega una columna al DataFrame.
   * 
   * @param column la columna a agregar
   * @param label la etiqueta de la columna
   */
  public void addColumn(Column column, String label) {
    this.columns.add(column);
    this.columnLabelsMap.put(label, column);
    this.columnOrderMap.put(this.columnOrderMap.size(), column);
    this.numCols = this.columnLabelsMap.size();
    this.numRows = this.columnOrderMap.get(0).size();
    if(this.rowLabelsMap.size() == 0){
      setRowLabels();
    }
  }

  // Getters y Setters

  /**
   * Retorna la celda en la posición especificada.
   *
   * @param col la columna de la celda.
   * @param row la fila de la celda.
   * @return la celda en la posición especificada.
   */
  public Cell getCell(int col, int row) {
    Column column = this.columnOrderMap.get(col);
    Cell cell = column.getContent().get(row);
    return cell;
  }

  // GetCell sobrecargado que recibe dos etiqeutas con formato string
  public Cell getCell(String colLabel, String rowLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    Integer row = this.rowLabelsMap.get(rowLabel);
    Cell cell = column.getContent().get(row);
    return cell;
  }

  /**
   * Establece el valor de una celda en la columna y fila especificadas.
   * @Autor: Julian

   * @param col el índice de la columna en la que se encuentra la celda.
   * @param index el índice de la fila en la que se encuentra la celda.
   * @param value el valor que se establecerá en la celda.
   */
  public void setCell(int col, int index, Object value) {
    Column column = this.columnOrderMap.get(col);
    column.setCell(index, value);
  }

  /**
   * Establece el valor de una celda en la fila especificada de la columna con la etiqueta dada.
   * 
   * @param colLabel la etiqueta de la columna en la que se establecerá el valor de la celda
   * @param row el índice de la fila en la que se establecerá el valor de la celda
   * @param cell el objeto Cell que se establecerá como valor de la celda
   */
  public void setCell(String colLabel, int row, Cell cell) {
    // Column column = null;
    // for (Map.Entry<String, Column> entry : this.columnLabelsMap.entrySet()) {
    //   if (entry.getValue().equals(colLabel)) {
    //     column = entry.getKey();
    //     break;
    //   } else {
    //     System.out.println("No se encontró la columna");
    //   }
    // }
    // if (column != null) {
    //   column.setCell(row, cell);
    // }
    Column column = this.columnLabelsMap.get(colLabel);
    
    if (column != null) {
        column.setCell(row, cell);
    } else {
        System.out.println("No se encontró la columna");
    }
  }

  /**
   * Limpia el mapa de etiquetas de fila y luego lo llena con las etiquetas de fila correspondientes a cada índice de fila.
   */
  public void setRowLabels() {
    rowLabelsMap.clear();
    rowOrderMap.clear();
    for (Integer i = 0; i < this.numRows; i++) {
      rowLabelsMap.put(i.toString(), i);
      rowOrderMap.put(rowLabelsMap.get(i.toString()), i.toString());
    }
  }

  /**
   * Devuelve una cadena que representa las etiquetas de fila del DataFrame.
   * Si no se han establecido las etiquetas de fila, se establecen llamando al método setRowLabels().
   * @return una cadena que representa las etiquetas de fila del DataFrame.
   */
  public String getRowLabels() {
    if (this.rowLabelsMap.size() == 0) {
      setRowLabels();
    }
    String[] rowLabels = this.listRowLabels();
    String out = "";
    for (String label : rowLabels) {
      out += label + " | ";
    }
    return out;
  }

  /** //TODO: definir si es un getter....
   * Devuelve un arreglo de Strings con las etiquetas de las filas del DataFrame.
   * @return arreglo de Strings con las etiquetas de las filas del DataFrame.
   */
  private String[] listRowLabels() {
    String[] labels = new String[this.rowLabelsMap.size()];
    int i = 0;
    for (String key : this.rowLabelsMap.keySet()) {
      labels[i] = key;
      i++;
    }
    return labels;
  }

  /**
   * Establece las etiquetas de columna para el DataFrame.
   * 
   * @param labels un arreglo de Strings con las etiquetas de columna
   * @throws IndexOutOfBoundsException si la cantidad de etiquetas no coincide con la cantidad de columnas
   */
  public void setColumLabels(String[] labels) throws IndexOutOfBoundsException {
    columnLabelsMap.clear();
    if (labels.length != this.columns.size()) {
      throw new IndexOutOfBoundsException(
        "La cantidad de labels no coincide con la cantidad de columnas"
      );
    } else {
      for (int i = 0; i < this.columns.size(); i++) {
        String columnName = labels[i];
        columnLabelsMap.put(columnName, this.columns.get(i));
      }
    }
  }

  /**
   * Establece las etiquetas de columna para el DataFrame.
   * 
   * @param labelsMap un mapa que asocia cada columna con su etiqueta correspondiente.
   */
  public void setColumLabels(Map<Column, String> labelsMap) {
    for (Column column : labelsMap.keySet()) {
      String columnName = labelsMap.get(column);
      columnLabelsMap.put(columnName, column);
    }
  }

  /**
   * Establece el nombre de la columna especificada en el mapa de etiquetas de columna.
   * @param column la columna a la que se le asignará el nombre de la etiqueta.
   * @param columnName el nombre de la etiqueta de la columna.
   */
  public void setColumLabels(Column column, String columnName) {
    columnLabelsMap.put(columnName, column);
  }

  /**
   * Retorna una cadena de caracteres que representa las etiquetas de las columnas del DataFrame.
   * 
   * @return una cadena de caracteres con las etiquetas de las columnas separadas por "|".
   */
  public String getColumnLabels() {
    String[] colLabels = this.listColumnLabels();
    String out = "";
    for (String label : colLabels) {
      out += label + " | ";
    }
    return out;
  }

  /** //TODO definir si es un getter...
   * Retorna un arreglo de Strings con los nombres de las columnas del DataFrame.
   * 
   * @return un arreglo de Strings con los nombres de las columnas del DataFrame.
   */
  private String[] listColumnLabels() {
    String[] labels = new String[this.columnOrderMap.size()];
    for (Integer key : this.columnOrderMap.keySet()) {
        Column column = this.columnOrderMap.get(key);
        String columnName = null;
        for (Map.Entry<String, Column> entry : this.columnLabelsMap.entrySet()) {
            if (entry.getValue().equals(column)) {
                columnName = entry.getKey();
                break;
            }
        }
        
        if (columnName != null) {
            labels[key] = columnName;
        }
    }
    return labels;
  }

  /**
   * Devuelve la cantidad de columnas del DataFrame.
   * @return la cantidad de columnas del DataFrame.
   */
  public int getCantColumnas() {
    return this.columns.size();
  }

  /**
   * Devuelve la cantidad de filas del DataFrame.
   * @return la cantidad de filas del DataFrame.
   */
  public int getCantFilas() {
    int numRows = this.columns.get(0).size();
    return numRows;
  }

  /**
   * Retorna un arreglo con las dimensiones del DataFrame.
   * El primer elemento del arreglo es la cantidad de filas y el segundo elemento es la cantidad de columnas.
   *
   * @return un arreglo de enteros con las dimensiones del DataFrame.
   */
  public Integer[] getDimensions() {
    int numCols = getCantColumnas();
    int numRows = getCantFilas();
    return new Integer[] { numRows, numCols };
  }

  /**
   * Devuelve el tipo de dato de la columna especificada.
   *
   * @param colNumber el número de la columna cuyo tipo de dato se desea obtener.
   * @return el tipo de dato de la columna especificada.
   */
  public String getColumnType(int colNumber) {
    Identificador identificador = null;
    String[] labels = this.listColumnLabels();

    for (int i = 0; i < labels.length; i++) {
      if (i == colNumber) {
        String celda =
          this.columnOrderMap.get(i).getContent().get(1).toString();
        identificador = new Identificador(celda);
      }
    }
    return identificador.getType();
  }

  //Metodo que devuelve una lista de los tipos de datos de las columnas
  public String[] getColumnTypes() {
    String[] labels = this.listColumnLabels();
    String[] types = new String[labels.length];
    Identificador identificador = null;

    for (int i = 0; i < labels.length; i++) {
      String celda = this.columnOrderMap.get(i).getContent().get(1).toString();
      identificador = new Identificador(celda);
      types[i] = identificador.getType();
    }
    return types;
  }
  // FIX ISSUE //TODO
  public void deleteRow(int rowIndex) {
    // Elimina la fila específica en cada columna
    for (Column column : columns) {
        column.removeCell(rowIndex);
    }

    
    // Actualiza el mapa de etiquetas de fila y la cantidad de filas
    rowLabelsMap.remove(String.valueOf(rowIndex));
    rowOrderMap.remove(rowIndex);
    numRows--;
    
}
  public void deleteColumn(int columnIndex) {
      // Elimina la columna específica
      Column removedColumn = columns.remove(columnIndex);

      // Elimina la referencia de la columna del mapa de etiquetas de columna
      String removedLabel = null;
      for (Map.Entry<String, Column> entry : columnLabelsMap.entrySet()) {
          if (entry.getValue().equals(removedColumn)) {
              removedLabel = entry.getKey();
              break;
          }
      }

      if (removedLabel != null) {
          columnLabelsMap.remove(removedLabel);
      }

      // Actualiza los mapas y la cantidad de columnas
      columnOrderMap.remove(columnIndex);
      numCols--;

      // Recrea el mapa de orden de columnas
      Map<Integer, Column> newColumnOrderMap = new HashMap<>();
      for (int i = 0; i < columns.size(); i++) {
          newColumnOrderMap.put(i, columns.get(i));
      }
      columnOrderMap = newColumnOrderMap;      
  }

  public void deleteCell(int rowIndex, int columnIndex) {
      // Elimina la celda específica en la columna y fila indicadas
      columns.get(columnIndex).removeCellbyNA(rowIndex);
  }
  public DataFrame randomSample(){
      DataFrame df = RandomSample.sample(this);
      return df;
    }

  public DataFrame randomSample(double p) {
  if (p < 0 || p > 1) {
      System.err.println("El porcentaje debe estar en el rango [0, 1]");
      return null;
  }
  else{
      DataFrame df = RandomSample.sample(this, p);
      return df;
    }

}

  public DataFrame head() {
  DataFrame df = Selection.head(this);
  return df;
}

  public DataFrame tail() {
    DataFrame df = Selection.tail(this);
    return df;
  }

  //SHALLOW COPY
  private DataFrame(DataFrame dataframe) {
    this.columns = new ArrayList<Column>();
    this.columnLabelsMap =
      new HashMap<String, Column>(dataframe.columnLabelsMap);
    this.rowLabelsMap = new HashMap<String, Integer>(dataframe.rowLabelsMap);
    this.columnOrderMap =
      new HashMap<Integer, Column>(dataframe.columnOrderMap);
    this.numRows = this.columnOrderMap.size();
    this.numCols = this.columnLabelsMap.size();
  }

  /**
   * Representa un conjunto de datos tabulares con etiquetas de fila y columna.
   * Cada columna es un objeto Column y cada fila es un objeto Row.
   * Proporciona métodos para manipular y analizar los datos.
   *
   * @return DataFrame
   */
  public DataFrame copy() {
      DataFrame copyDataFrame = new DataFrame();

      for (int i = 0; i < this.numCols; i++) {
        Column originalColumn = this.columnOrderMap.get(i);
        String label = getKeyFromValue(this.columnLabelsMap, originalColumn);

        Column copiedColumn = new Column();

        ArrayList<Cell> copiedContent = new ArrayList<>();
        for (Cell originalCell : originalColumn.getContent()) {
            Cell copiedCell = originalCell.copy();
            copiedContent.add(copiedCell);
        }

        copiedColumn.setContent(copiedContent);

        copyDataFrame.addColumn(copiedColumn, label);
      }

      copyDataFrame.rowLabelsMap.putAll(this.rowLabelsMap);
      copyDataFrame.rowOrderMap.putAll(this.rowOrderMap);
      copyDataFrame.numRows = this.numRows;
      copyDataFrame.numCols = this.numCols;

      return copyDataFrame;
  }

  // Método para obtener la clave de un valor en un mapa
  private <T, E> T getKeyFromValue(Map<T, E> map, E value) {
      for (Map.Entry<T, E> entry : map.entrySet()) {
          if (Objects.equals(value, entry.getValue())) {
              return entry.getKey();
          }
      }
      return null;
  }

  /**
  * Metodo para obtener la columna
  *
  * @param col: numero de columna
  *
  * @return column
  *
  **/
  public Column getColumn(int col) {
    Column column = this.columnOrderMap.get(col);
    return column;
  }

  public Column getColumn(String label) {
  Column column = this.columnLabelsMap.get(label);
  return column;
  }

  /**
  * Metodo para obtener las columnas
  *
  * @param columns: lista de columnas
  *
  * @return columns
  *
  **/
  public List<Column> getColumns() {
    
    return this.columns;
  }

  public float sum(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.sum(cells);
  }

  public float sum(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.sum(cells);
  }

  public float max(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.max(cells);
  }

  public float max(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.max(cells);
  }

  public float min(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.min(cells);
  }

  public float min(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.min(cells);
  }

  public float mean(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.mean(cells);
  }

  public float mean(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.mean(cells);
  }

  public float variance(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.variance(cells);
  }

  public float variance(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.variance(cells);
  }

  public float standardDeviation(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.standardDeviation(cells);
  }

  public float standardDeviation(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.standardDeviation(cells);
  }

  public void show() {
    System.out.println(this.toString("|", false, false));
  }
  public void showAllColumns() {
    System.out.println(this.toString("|", false, true));
  }

  public void showAllRows(){
        System.out.println(this.toString("|", true, false));
  }
  
  public void showAll() {
    System.out.println(this.toString("|", true, true));
  }
 
  /**
   * Returns a string representation of the DataFrame, using the specified separator between columns.
   * If the separator is null, the default separator " | " is used.
   * The string representation includes column labels and row labels.
   * Each cell value is padded to fit the width of its column.
   * 
   * @param separador the separator to use between columns
   * @return a string representation of the DataFrame
   */
  public String toString(String separador, Boolean showAllRows, Boolean showAllColumns) {
    String out = "";
    String sep = " " + separador + " ";
    String[] labels = this.listColumnLabels();
    int[] colWidths = new int[labels.length];
    int numRowsToShow = Math.min(this.numRows, 10); // Mostrar solo las primeras 10 filas
    int numColumnsToShow = Math.min(this.numCols, 5); // Mostrar solo las primeras 5 columnas

    if (separador == null) {
      separador = " | ";
    }
    if (showAllRows == null) {
        showAllRows = false;
    }
    if (showAllColumns == null) {
        showAllColumns = false;
    }
    if (showAllRows) {
        numRowsToShow = this.numRows; // Mostrar todas las filas
    } else {
        numRowsToShow = Math.min(this.numRows, 10); // Mostrar solo las primeras 10 filas
    }

    if (showAllColumns) {
        numColumnsToShow = this.numCols; // Mostrar todas las columnas
    } else {
        numColumnsToShow = Math.min(this.numCols, 5); // Mostrar solo las primeras 5 columnas
    }


    for (int i = 0; i < numColumnsToShow; i++) {
        colWidths[i] = ("[" + labels[i] + "]").length();
        for (int j = 0; j < numRowsToShow; j++) {
            String cellValue = this.columnOrderMap.get(i).getContent().get(j).toString();
            colWidths[i] = Math.max(colWidths[i], cellValue.length());
        }
    }

    for (int i = 0; i < numColumnsToShow; i++) {
        if (i == 0) {
            out += String.format("%-" + colWidths[i] + "s", "") + sep;
        }
        out += String.format("%-" + colWidths[i] + "s", "[" + labels[i] + "]") + sep;
    }
    out += "\n";
    int rowIndex = 0;
    // for (Integer row = 0; row < numRowsToShow; row++) {
      for (Integer orden : this.rowOrderMap.keySet()) { //rowOrder = {int :  label}

        int rowPadding = colWidths[0] - orden.toString().length();
        int leftRowPadding = rowPadding / 2;
        int rightRowPadding = rowPadding - leftRowPadding;
        
        out += String.format("%-" + (leftRowPadding + orden.toString().length() + rightRowPadding) + "s", "[Fila: " + orden + "]") + sep;
        for (int i = 0; i < numColumnsToShow; i++) {
            String cellValue = this.columnOrderMap.get(i).getContent().get(rowIndex).toString(); 
            int padding = colWidths[i] - cellValue.length();
            int leftPadding = padding / 2;
            int rightPadding = padding - leftPadding;
            out += String.format("%-" + (leftPadding + cellValue.length() + rightPadding) + "s", cellValue) + sep;
        }
        rowIndex++;
        out += "\n";
    }

    if (this.numRows > 10 && !showAllRows) {
        out += "\n[Mostrando solo las primeras 10 filas de " + this.numRows + "]";
    }

    if (this.numCols > 5 && !showAllColumns) {
        out += "\n[Mostrando solo las primeras 5 columnas de " + this.numCols + "]\n";
    }

    return out;
}
  // Metodo que invoca el readCSV de archivos
  public DataFrame readCSV(String path) throws IOException {
    DataFrame df = Archivos.readCSV(path);
    return df;
  }
  public DataFrame readCSV(String path, String sep) throws IOException {
    DataFrame df = Archivos.readCSV(path, sep);
    return df;
  }

  // Metodo que invoca el exportCSV de archivos //TODO
  

// Metodo para devolver informacion de dataframe
public void info() {
  System.out.println("Información del DataFrame");
  System.out.println("- Cantidad de filas: " + this.getCantFilas());
  System.out.println("- Cantidad de columnas: " + this.getCantColumnas());
  System.out.println("- Etiquetas de filas: " + this.getRowLabels());
  System.out.println("- Etiquetas de columnas: " + this.getColumnLabels());
  // 
  String tipos="";
  for (String tipo : this.getColumnTypes()) {
    tipos += tipo + " - | ";
  }
  System.out.println("- Tipos de datos:        " + tipos);
  
  }

}