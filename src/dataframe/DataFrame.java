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

  

  /**
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
  public DataFrame() {
    
    this.columns = new ArrayList<Column>(1);
    this.columnLabelsMap = new HashMap<String, Column>();
    this.columnOrderMap = new HashMap<Integer, Column>();
    this.rowLabelsMap = new HashMap<String, Integer>();
    this.rowOrderMap = new HashMap<Integer, String>();
    this.numRows = this.columnOrderMap.size();
    this.numCols = this.columnLabelsMap.size();
  }

  /**
   * Clase que representa un DataFrame en Java.
   * Un DataFrame es una estructura de datos tabular que contiene filas y columnas.
   * Cada columna puede contener diferentes tipos de datos, como cadenas, números, booleanos, etc.
   * Esta clase proporciona métodos para manipular y analizar los datos en el DataFrame.
   * 
   * @param matriz una matriz de objetos que representan los datos del DataFrame.
   */
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

/**
 * Clase que representa un DataFrame en Java.
 * Un DataFrame es una estructura de datos tabular que organiza los datos en filas y columnas.
 * Proporciona métodos para filtrar y seleccionar datos.
 * 
 * @param Condicion una query que representa la condición de filtrado.
 */
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


/**
 * Filtra las filas del DataFrame según una condición dada en una columna específica.
 * 
 * @param colLabel el nombre de la columna en la que se aplicará la condición de filtrado
 * @param operador el operador de comparación a utilizar en la condición de filtrado
 * @param valor el valor con el que se compararán los elementos de la columna
 * @return una lista de las filas que cumplen con la condición de filtrado
 * @throws IllegalArgumentException si el valor no es un número, un string o un booleano
 */
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
  
  /**
  * Clase que representa un DataFrame en Java.
  *
  * Filtra el DataFrame en base a un valor dado en una columna específica.
   * 
   * @param colLabel  Etiqueta de la columna en la que se realizará el filtro.
   * @param operador  Operador de comparación a utilizar en el filtro.
   * @param valor     Valor a comparar en el filtro.
   * @return          Un nuevo DataFrame que contiene solo las filas que cumplen con el filtro.
   * @throws IllegalArgumentException Si el valor no es un número, un string o un booleano.
   */
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

  /**
   * Filters the array based on the specified column label, operator, and value.
   * 
   * @param colLabel the label of the column to filter
   * @param operador the operator to use for filtering (e.g., "==", "<", ">")
   * @param valor the value to compare against
   * @return an array of filtered values
   * @throws IllegalArgumentException if the column label is invalid or the operator is not supported
   */
  public String[] filteredArray(String colLabel, String operador, Number valor) throws IllegalArgumentException{
    Cell celdaAuxiliar = new NumericCell(valor);
    return filterSetup(colLabel, operador, celdaAuxiliar).toArray(String[]::new);
  }

  /**
   * Filters the array based on the specified column label, operator, and value.
   * 
   * @param colLabel the label of the column to filter
   * @param operador the operator to use for filtering (e.g., "=", "<", ">")
   * @param valor the value to compare against
   * @return the filtered array as an array of strings
   * @throws IllegalArgumentException if the column label is invalid or the operator is not supported
   */
  public String[] filteredArray(String colLabel, String operador, String valor) throws IllegalArgumentException{
    Cell celdaAuxiliar = new StringCell(valor);
    return filterSetup(colLabel, operador, celdaAuxiliar).toArray(String[]::new);
  }

  /**
   * Filters the array based on the specified column label, operator, and value.
   * 
   * @param colLabel the label of the column to filter
   * @param operador the operator to use for filtering
   * @param valor the value to compare against
   * @return the filtered array as an array of strings
   * @throws IllegalArgumentException if the column label is invalid or the operator is not supported
   */
  public String[] filteredArray(String colLabel, String operador, Boolean valor) throws IllegalArgumentException{
    Cell celdaAuxiliar = new BooleanCell(valor);
    return filterSetup(colLabel, operador, celdaAuxiliar).toArray(String[]::new);
  }

  /**
   * Filtra las filas del DataFrame según un criterio de comparación en una columna específica.
   * 
   * @param colLabel el nombre de la columna en la que se realizará la comparación
   * @param operador el operador de comparación a utilizar
   * @param celdaAuxiliar la celda auxiliar que contiene el valor de referencia para la comparación
   * @return una lista de las filas que cumplen con el criterio de comparación
   * @throws IllegalArgumentException si el operador de comparación no es válido
   */
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

  /**
   * Represents a row in a DataFrame.
   */
  public Row getRow(int index){
    Row row = new Row();
    row.setLabel(this.rowOrderMap.get(index));
    for (int j = 0; j < this.numCols; j++) {
      row.addCell(this.getCell(j, index));
    }
    return row;
  }

  /**
   * Represents a row in a DataFrame.
   */
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

  /**
   * Sets the label of a row in the DataFrame.
   * 
   * @param vieja The old label of the row.
   * @param nueva The new label to assign to the row.
   */
  public void setRowLabel(String vieja, String nueva){
    Integer index = this.rowLabelsMap.get(vieja); // {"0" : 0, "1" : 1, "2" : 2} --> vieja = "0" --> index = 0
    this.rowLabelsMap.remove(vieja); // {"1" : 1, "2" : 2}
    this.rowLabelsMap.put(nueva, index); // {"1" : 1, "2" : 2, "nueva" : 0}
    this.rowOrderMap.put(index, nueva);// rowOrderMap = {0 : "0", 1 : "1", 2 : "2"} --> rowOrderMap = {0 : "nueva", 1 : "1", 2 : "2"}
  }

  /**
   * Returns an ArrayList of Row objects representing the rows in the DataFrame.
   * Each Row object contains the label and cells of a row in the DataFrame.
   *
   * @return An ArrayList of Row objects representing the rows in the DataFrame.
   */
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

  /**
   * Sorts the DataFrame in ascending order.
   * This method creates a new DataFrame object with the sorted data.
   * The sorting is performed based on the values in the rows.
   * 
   * @return A new DataFrame object with the sorted data.
   */
  public DataFrame sort() {
    DataFrame sortedDf = this.shallowCopy();
    System.out.println("sortedf.numrows : " +sortedDf.numRows);
    System.out.println("sortedf.numcols : " +sortedDf.numCols);
    HashMap<Integer, String> sortedMap = quickSort(sortedDf.getRows(), 0, sortedDf.numRows - 1);
    sortedDf.rowOrderMap = sortedMap;
    return sortedDf;
  }
  /**
   * Crea una copia del DataFrame ordenado según la columna especificada.
   * 
   * @param colLabel Etiqueta de la columna por la que se ordenará el DataFrame.
   * @return Una nueva instancia de DataFrame ordenada por la columna especificada.
   */
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
  /**
   * Ordena recursivamente una lista de filas utilizando el algoritmo QuickSort.
   * 
   * @param rows Lista de filas a ordenar.
   * @param low Índice inferior del rango a ordenar.
   * @param high Índice superior del rango a ordenar.
   * @return Un mapa ordenado de enteros (índices) y etiquetas de fila.
   */
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
  /**
   * Realiza la partición en una lista de filas para el algoritmo QuickSort.
   * 
   * @param rows Lista de filas a particionar.
   * @param low Índice inferior del rango de partición.
   * @param high Índice superior del rango de partición.
   * @return Índice de la fila pivote después de la partición.
   */
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
  /**
   * Crea y devuelve una vista superficial del DataFrame original basada en las etiquetas de fila y columna proporcionadas.
   * 
   * @param rowLabels Arreglo de etiquetas de fila para la selección.
   * @param colLabels Arreglo de etiquetas de columna para la selección.
   * @return Una vista superficial del DataFrame original basada en las etiquetas proporcionadas.
   * @throws IllegalArgumentException Si alguna de las etiquetas de fila o columna no existe en el DataFrame original.
   */
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


  /**
   * Realiza una copia superficial del DataFrame actual.
   * 
   * @return Una copia superficial del DataFrame actual.
   */
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
  /**
   * Obtiene la celda ubicada en la intersección de la columna y fila especificadas por las etiquetas dadas.
   * 
   * @param colLabel La etiqueta de la columna de la celda.
   * @param rowLabel La etiqueta de la fila de la celda.
   * @return La celda ubicada en la intersección de la columna y fila especificadas.
   */
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
   * Establece las etiquetas de fila para la estructura tabular.
   * Las etiquetas de fila se asignan de manera consecutiva, comenzando desde cero hasta el número de filas menos uno.
   * Este método se utiliza internamente para inicializar las etiquetas de fila cuando se crea un nuevo DataFrame.
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

  /** 
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

  /**
   * Devuelve una lista de los tipos de datos de las columnas de la estructura tabular.
   * Se identifica el tipo de dato de cada columna basándose en el contenido de la segunda celda de cada columna.
   * Se utiliza un objeto Identificador para identificar y determinar el tipo de dato de cada celda.
   *
   * @return Un array de Strings con los tipos de datos de las columnas.
   * @throws TipoNoIdentificadoException Si no se puede identificar el tipo de dato de alguna columna.
   */
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
  

  
  /**
   * Deletes a specific row from the DataFrame.
   *
   * @param rowIndex The index of the row to be deleted.
   * @return A new DataFrame with the specified row removed.
   * @throws IndexOutOfBoundsException If the rowIndex is out of bounds.
   */
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

  /**
   * Deletes a specific column from the DataFrame.
   *
   * @param columnIndex the index of the column to be deleted
   * 
   * @throws IndexOutOfBoundsException if the columnIndex is out of range
   */
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

  /**
   * Deletes the specific cell at the given row and column index.
   *
   * @param rowIndex    the index of the row containing the cell to be deleted
   * @param columnIndex the index of the column containing the cell to be deleted
   * @throws IndexOutOfBoundsException if the row or column index is out of bounds
   */
  public void deleteCell(int rowIndex, int columnIndex) {
      // Elimina la celda específica en la columna y fila indicadas
      columns.get(columnIndex).removeCellbyNA(rowIndex);
  }
  
  /**
   * Genera un dataframe con una muestra aleatoria
   * 
   * @return a DataFrame object representing the random sample.
   */
  public DataFrame randomSample(){
      DataFrame df = RandomSample.sample(this);
      return df;
    }

  /**
   * Genera un dataframe con una muestra aleatoria en base a un porcentaje dado
   * 
   * @return a DataFrame object representing the random sample.
  */
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

  /**
   * Muestra las 5 primeras filas del dataframe.
   *
   * 
   * @throws IllegalArgumentException if n is negative
   */
  public void head() {
  DataFrame df = Selection.head(this);
  df.show();
  }

  /**
   * Muestra las últimas 5 filas del dataframe.
   *
   * 
   * @throws IllegalArgumentException if n is negative
   */
  public void tail() {
    DataFrame df = Selection.tail(this);
    df.show();
  }

  /**
   * Concatenacion de dos DataFrames.
   *
   * @param df1 the first DataFrame to be concatenated
   * @param df2 the second DataFrame to be concatenated
   * @return the concatenated DataFrame
   * @throws NullPointerException if either df1 or df2 is null
   */
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
  /**
  * Metodo para obtener la columna
  *
  * @param label: nombre de la columna
  *
  * @return column
  *
  **/
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

  /**
   * Calculates the sum of the values in the specified column.
   *
   * @param col the index of the column to calculate the sum for
   * @return the sum of the values in the specified column
   * @throws NullPointerException if the column is null
   */
  public float sum(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.sum(cells);
  }

  /**
   * Calculates the sum of the values in the specified column.
   *
   * @param colLabel the label of the column to calculate the sum for
   * @return the sum of the values in the column
   * @throws NullPointerException if the specified column label is not found
   */
  public float sum(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.sum(cells);
  }

  /**
   * Returns the maximum value in the specified column of the DataFrame.
   *
   * @param colLabel the label of the column to find the maximum value in
   * @return the maximum value in the specified column
   * @throws NullPointerException if the specified column label is not found in the DataFrame
   */
  public float max(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.max(cells);
  }

  /**
   * Returns the maximum value in the specified column of the DataFrame.
   *
   * @param col the index of the column
   * @return the maximum value in the column
   * @throws IndexOutOfBoundsException if the column index is out of range
   */
  public float max(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.max(cells);
  }

  /**
   * Returns the minimum value of the specified column in the DataFrame.
   *
   * @param colLabel the label of the column to calculate the minimum value from
   * @return the minimum value of the specified column
   * @throws NullPointerException if the specified column label is not found in the DataFrame
   */
  public float min(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.min(cells);
  }

  /**
   * Returns the minimum value of the specified column in the DataFrame.
   *
   * @param col the index of the column
   * @return the minimum value of the column
   * @throws IndexOutOfBoundsException if the column index is out of range
   */
  public float min(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.min(cells);
  }

  /**
   * Calculates the mean value of a specified column in the DataFrame.
   *
   * @param colLabel the label of the column to calculate the mean value for
   * @return the mean value of the specified column
   * @throws NullPointerException if the column label is not found in the DataFrame
   */
  public float mean(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.mean(cells);
  }

  /**
   * Calculates the mean value of a column in the DataFrame.
   *
   * @param col The index of the column to calculate the mean value.
   * @return The mean value of the specified column.
   * @throws IndexOutOfBoundsException If the column index is out of range.
   */
  public float mean(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.mean(cells);
  }

  /**
   * Calculates the variance of a column in the DataFrame.
   *
   * @param colLabel the label of the column to calculate the variance for
   * @return the variance of the column
   * @throws NullPointerException if the column label is not found in the DataFrame
   */
  public float variance(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.variance(cells);
  }

  /**
   * Calculates the variance of a column in the DataFrame.
   *
   * @param col The index of the column to calculate the variance for.
   * @return The variance of the column.
   * @throws IndexOutOfBoundsException If the column index is out of bounds.
   */
  public float variance(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.variance(cells);
  }

  /**
   * Calculates the standard deviation of the values in the specified column.
   *
   * @param colLabel the label of the column to calculate the standard deviation for
   * @return the standard deviation of the values in the specified column
   * @throws NullPointerException if the specified column label is null
   */
  public float standardDeviation(String colLabel) {
    Column column = this.columnLabelsMap.get(colLabel);
    List<Cell> cells = column.getContent();
    return Summarise.standardDeviation(cells);
  }

  /**
   * Calculates the standard deviation of a column in the DataFrame.
   *
   * @param col the index of the column to calculate the standard deviation for
   * @return the standard deviation of the column
   * @throws NullPointerException if the column is null
   */
  public float standardDeviation(int col) {
    Column column = this.columnOrderMap.get(col);
    List<Cell> cells = column.getContent();
    return Summarise.standardDeviation(cells);
  }

  /**
   * Displays the DataFrame by printing it to the console.
   *
   * @param None
   * @return None
   * @throws None
   */
  public void show() {
    System.out.println(this.toString("|", false, false));
  }

  /**
   * Prints all columns of the DataFrame.
   *
   * @param None
   * @return None
   * @throws None
   */
  public void showAllColumns() {
    System.out.println(this.toString("|", false, true));
  }

  /**
   * Displays all rows of the DataFrame.
   */
  public void showAllRows(){
        System.out.println(this.toString("|", true, false));
  }
  
  /**
   * Prints the contents of the DataFrame.
   *
   */
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
  
  /**
   * Reads a CSV file and returns a DataFrame object.
   *
   * @param path the path of the CSV file to be read
   * @return the DataFrame object representing the data in the CSV file
   * @throws IOException if an I/O error occurs while reading the file
   */
  public DataFrame readCSV(String path) throws IOException {
    DataFrame df = Archivos.readCSV(path);
    return df;
  }

  /**
   * Reads a CSV file and returns a DataFrame object.
   *
   * @param path the path of the CSV file to be read
   * @param sep the separator used in the CSV file
   * @return a DataFrame object containing the data from the CSV file
   * @throws IOException if an I/O error occurs while reading the file
   */
  public DataFrame readCSV(String path, String sep) throws IOException {
    DataFrame df = Archivos.readCSV(path, sep);
    return df;
  }

  /**
   * Exporta el DataFrame a un archivo CSV en la ubicación especificada.
   * 
   * @param filepath la ruta del archivo CSV donde se guardará el DataFrame
   * @throws IOException si ocurre un error al escribir en el archivo
   */
  public void exportCSV(String filepath){
    Archivos.exportCSV(filepath,this);
    System.out.println("Archivo guardado con exito.");
  }


  /**
   * Imprime información del DataFrame.
   *
   * @throws TipoNoIdentificadoException si se encuentra un tipo de dato no identificado.
   */
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