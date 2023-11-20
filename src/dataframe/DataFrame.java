package dataframe;

import dataframe.cells.BooleanCell;
import dataframe.cells.Cell;
import dataframe.cells.NACell;
import dataframe.cells.NumericCell;
import dataframe.cells.StringCell;
import dataframe.exceptions.TipoNoIdentificadoException;
import dataframe.utils_df.Criterios;
import dataframe.utils_df.DataFrameConcatenator;
import dataframe.utils_df.GetKeyFromValue;
import dataframe.utils_df.Identificador;
import dataframe.utils_df.RandomSample;
import dataframe.utils_df.Selection;
import dataframe.utils_df.Summarise;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class DataFrame {

  public List<Column> columns; // lista de columnas -> [col1, col2 , ..., colN]
  public Map<String, Column> columnLabelsMap; // labels de columnas-> { 'nombre': col }
  public Map<Integer, Column> columnOrderMap; //  orden de columnas -> { int : col}
  public Map<String, Integer> rowLabelsMap; // labels de filas -> {'nombre' : int posicional}
  public Map<Integer, String> rowOrderMap; // orden de filas -> {int posicional : 'nombre'}
  public int numRows; // numero de filas
  public int numCols; // numero de columnas

  

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

  public DataFrame(Object[][] matriz) throws TipoNoIdentificadoException {
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

public DataFrame filter(String Conditions) throws IllegalArgumentException, ParseException {
    List<String> filasSalida = new ArrayList<>(rowLabelsMap.keySet());
    List<String> filasSalidaAux = new ArrayList<>();
    List<String> filasFiltradas;
    String[] condiciones = Conditions.split("\\b( and | or )\\b");
    Pattern pattern = Pattern.compile("\\b(?:and|or)\\b");
    Matcher matcher = pattern.matcher(Conditions);
    String expresion = null;

    if (matcher.find()) {
      expresion = matcher.group(); // Obtener la palabra "and" o "or" encontrada
    }

    if (expresion == null) {
      throw new IllegalArgumentException("Expresión de filtro inválida");
    }

    if(expresion.equals("and")){
      for (String condicion : condiciones) {
        String[] parts = condicion.split("\\s*(<=|>=|!=|<|>|=)\\s*");
        String colLabel = parts[0];
        String operador = condicion.replaceAll(".*?(<=|>=|!=|<|>|=).*", "$1");
        String tipo = Identificador.getType(parts[1]);
        switch (tipo) {
          case "STRING":
            String auxString = parts[1];
            filasFiltradas = filterSingleCondition(colLabel, operador, auxString);
            filasSalida.retainAll(filasFiltradas);
          break;
          case "FLOAT":
            Number auxFloat = NumberFormat.getInstance().parse(parts[1]);
            filasFiltradas = filterSingleCondition(colLabel, operador, auxFloat);
            filasSalida.retainAll(filasFiltradas);
          break;
          case "INTEGER":
            Number auxInt = NumberFormat.getInstance().parse(parts[1]);
            filasFiltradas = filterSingleCondition(colLabel, operador, auxInt);
            filasSalida.retainAll(filasFiltradas);
          break;
          case "BOOLEAN":
            Boolean auxBoolean = Boolean.parseBoolean(parts[1]);
            filasFiltradas = filterSingleCondition(colLabel, operador, auxBoolean);
            filasSalida.retainAll(filasFiltradas);
          break;
          default:
          break;
        }
      }
    }else if (expresion.equals("or")){
      for (String condicion : condiciones) {
        String[] parts = condicion.split("\\s*(<=|>=|!=|<|>|=)\\s*");
        String colLabel = parts[0];
        String operador = condicion.replaceAll(".*?(<=|>=|!=|<|>|=).*", "$1");
        String tipo = Identificador.getType(parts[1]);
        switch (tipo) {
          case "STRING":
            String auxString = parts[1];
            filasFiltradas = filterSingleCondition(colLabel, operador, auxString);
            filasSalidaAux.addAll(filasFiltradas);
          break;
          case "FLOAT":
            Number auxFloat = NumberFormat.getInstance().parse(parts[1]);
            filasFiltradas = filterSingleCondition(colLabel, operador, auxFloat);
            filasSalidaAux.addAll(filasFiltradas);
          break;
          case "INTEGER":
            Number auxInt = NumberFormat.getInstance().parse(parts[1]);
            filasFiltradas = filterSingleCondition(colLabel, operador, auxInt);
            filasSalidaAux.addAll(filasFiltradas);
          break;
          case "BOOLEAN":
            Boolean auxBoolean = Boolean.parseBoolean(parts[1]);
            filasFiltradas = filterSingleCondition(colLabel, operador, auxBoolean);
            filasSalidaAux.addAll(filasFiltradas);
          break;
          default:
          break;
        }
      }
      filasSalida.retainAll(filasSalidaAux);
      
    }
  
    String[] colLabels = this.columnLabelsMap.keySet().toArray(new String[0]);
    DataFrame dfSalida = this.select(filasSalida.toArray(new String[0]), colLabels);
    return dfSalida;
}


public List<String> filterSingleCondition(String colLabel, String operador, Object valor) throws IllegalArgumentException {
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
    List<String> listaSalida = Arrays.asList(filasSalida); 
    return listaSalida;
  }
  
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
    if (!this.rowLabelsMap.containsKey(label)) {
      throw new IllegalArgumentException("La etiqueta de fila proporcionada no existe en el DataFrame");
    }
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
    System.out.println("sortedf.numrows : " +sortedDf.numRows);
    System.out.println("sortedf.numcols : " +sortedDf.numCols);
    HashMap<Integer, String> sortedMap = quickSort(sortedDf.getRows(), 0, sortedDf.numRows - 1);
    sortedDf.rowOrderMap = sortedMap;
    return sortedDf;
  }
  public DataFrame sort(String colLabel) {
    DataFrame sortedDf = this.shallowCopy();
    Column column = sortedDf.columnLabelsMap.get(colLabel);
    
    if(sortedDf.columnOrderMap.get(0) != column){
      HashMap<Integer, Column> columnOrderAuxiliar = new HashMap<Integer, Column>();
      columnOrderAuxiliar.putAll(this.columnOrderMap);
      for (Map.Entry<Integer, Column> parKV : this.columnOrderMap.entrySet()) {
        if(parKV.getValue().equals(column)){
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

  protected DataFrame shallowCopy() {
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
    if (col < 0 || col >= this.numCols || index < 0 || index >= this.numRows) {
      throw new IllegalArgumentException("Índice de columna o fila fuera de rango");
    }
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
    Column column = this.columnLabelsMap.get(colLabel);
    if (column == null) {
      throw new IllegalArgumentException("No se encontró la columna con la etiqueta especificada");
    }

    if (row < 0 || row >= this.numRows) {
        throw new IllegalArgumentException("Índice de fila fuera de rango");
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
    String[] rowLabels = this.rowLabels();
    String out = "";
    for (String label : rowLabels) {
      out += label + " | ";
    }
    return out;
  }

  /**
   * Devuelve un arreglo de Strings con las etiquetas de las filas del DataFrame.
   * @return arreglo de Strings con las etiquetas de las filas del DataFrame.
   */
  public String[] rowLabels() {
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
    String[] colLabels = this.columnLabels();
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
  public String[] columnLabels() {
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
  public int getColumnNumber() {
    return this.columns.size();
  }

  /**
   * Devuelve la cantidad de filas del DataFrame.
   * @return la cantidad de filas del DataFrame.
   */
  public int getRowNumber() {
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
    int numCols = getColumnNumber();
    int numRows = getRowNumber();
    return new Integer[] { numRows, numCols };
  }

  /**
   * Devuelve el tipo de dato de la columna especificada.
   *
   * @param colNumber el número de la columna cuyo tipo de dato se desea obtener.
   * @return el tipo de dato de la columna especificada.
   * @throws TipoNoIdentificadoException
   */
  public String getColumnType(int colNumber) throws TipoNoIdentificadoException {
    Identificador identificador = null;
    String[] labels = this.columnLabels();

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
  public String[] getColumnTypes() throws TipoNoIdentificadoException {
    String[] labels = this.columnLabels();
    String[] types = new String[labels.length];
    Identificador identificador = null;

    for (int i = 0; i < labels.length; i++) {
      String celda = this.columnOrderMap.get(i).getContent().get(1).toString();
      identificador = new Identificador(celda);
      types[i] = identificador.getType();
    }
    return types;
  }

  public DataFrame deleteRow(int rowIndex) {
    // Elimina la fila específica en cada columna
    DataFrame nuevoDF = new DataFrame();
    Column columnaAuxiliar; 
    for (Column column : this.columns) {
      columnaAuxiliar = column.copy();
      columnaAuxiliar.removeCell(rowIndex);
      String colLabel = GetKeyFromValue.getKey(this.columnLabelsMap, column);
      nuevoDF.addColumn(columnaAuxiliar, colLabel);
    }
    for(String rowLabel: rowLabelsMap.keySet()){
      int indiceActual = rowLabelsMap.get(rowLabel);
      if( indiceActual < rowIndex){
        nuevoDF.rowLabelsMap.put(rowLabel, indiceActual);
      }else{
        nuevoDF.rowLabelsMap.put(rowLabel, indiceActual-1);
      }
      int ordenViejo = GetKeyFromValue.getKey(rowOrderMap, rowLabel);
      if(ordenViejo < rowIndex){
        nuevoDF.rowOrderMap.put(ordenViejo, rowLabel);
      }else{
        nuevoDF.rowOrderMap.put(ordenViejo-1, rowLabel);
      }
      }
    
    nuevoDF.numRows --;
    return nuevoDF;
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

  public void head() {
  DataFrame df = Selection.head(this);
  df.show();
}

  public void tail() {
    DataFrame df = Selection.tail(this);
    df.show();
  }

  public DataFrame concatenate(DataFrame df1, DataFrame df2){
    return DataFrameConcatenator.concatenate(df1, df2);
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
        String label = GetKeyFromValue.getKey(this.columnLabelsMap, originalColumn);

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
  protected String toString(String separador, Boolean showAllRows, Boolean showAllColumns) {
    String out = "";
    String sep = " " + separador + " ";
    String[] labels = this.columnLabels();
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
      for (Integer orden : this.rowOrderMap.keySet()) { //rowOrder = {int :  label}
        if(orden > numRowsToShow && !showAllRows){
            break;
        }
        int rowPadding = colWidths[0] - orden.toString().length();
        int leftRowPadding = rowPadding / 2;
        int rightRowPadding = rowPadding - leftRowPadding;
        
        out += String.format("%-" + (leftRowPadding + orden.toString().length() + rightRowPadding) + "s", "[Fila: " + orden + "]") + sep;
        for (int i = 0; i < numColumnsToShow; i++) {       
            rowIndex = this.rowLabelsMap.get(this.rowOrderMap.get(orden));
            String cellValue = this.columnOrderMap.get(i).getContent().get(rowIndex).toString(); 
            int padding = colWidths[i] - cellValue.length();
            int leftPadding = padding / 2;
            int rightPadding = padding - leftPadding;
            out += String.format("%-" + (leftPadding + cellValue.length() + rightPadding) + "s", cellValue) + sep;
        }
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
  public void exportCSV(String filepath){
    Archivos.exportCSV(filepath,this);
    System.out.println("Archivo guardado con exito.");
  }

// Metodo para devolver informacion de dataframe
public void info() throws TipoNoIdentificadoException {
  System.out.println("Información del DataFrame");
  System.out.println("- Cantidad de filas: " + this.getRowNumber());
  System.out.println("- Cantidad de columnas: " + this.getColumnNumber());
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