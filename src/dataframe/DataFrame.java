package dataframe;

import dataframe.cells.Cell;
import utils_df.Identificador;
import utils_df.Validadores;

import java.util.function.Predicate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFrame {

  public List<Column> columns; // lista de columnas -> [col1, col2 , ..., colN]
  public Map<Column, String> columnLabelsMap; // labels de columnas-> { col : 'nombre'}
  public Map<Integer, Column> columnOrderMap; //  orden de columnas -> { int : col}
  public Map<String, Integer> rowLabelsMap; // labels de columnas-> {'nombre' : int posicional}
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
    this.columnLabelsMap = new HashMap<Column, String>();
    this.columnOrderMap = new HashMap<Integer, Column>();
    this.rowLabelsMap = new HashMap<String, Integer>();
    this.numRows = this.columnOrderMap.size();
    this.numCols = this.columnLabelsMap.size();
  }

  /**
   * Agrega una nueva columna al DataFrame con las celdas especificadas.
   * 
   * @param cells la lista de celdas que conforman la nueva columna
   */
  public void addColumn(List<Cell> cells) {
    Column column = new Column(cells);
    this.columns.add(column);
    this.columnLabelsMap.put(column, "Columna " + this.columnLabelsMap.size());
    this.columnOrderMap.put(this.columnOrderMap.size(), column);
    this.numCols = this.columnLabelsMap.size();
    this.numRows = this.columnOrderMap.get(0).size();
  }

  /**
   * Agrega una columna al DataFrame.
   * 
   * @param column la columna a agregar
   */
  public void addColumn(Column column) {
    this.columns.add(column);
    this.columnLabelsMap.put(column, "Columna " + this.columnLabelsMap.size());
    this.columnOrderMap.put(this.columnOrderMap.size(), column);
    this.numCols = this.columnLabelsMap.size();
    this.numRows = this.columnOrderMap.get(0).size();
  }

  /**
   * Agrega una columna al DataFrame.
   * 
   * @param column la columna a agregar
   * @param label la etiqueta de la columna
   */
  public void addColumn(Column column, String label) {
    this.columns.add(column);
    this.columnLabelsMap.put(column, label);
    this.columnOrderMap.put(this.columnOrderMap.size(), column);
    this.numCols = this.columnLabelsMap.size();
    this.numRows = this.columnOrderMap.get(0).size();
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

  /**
   * Establece el valor de una celda en la posición especificada.
   * 
   * @param col la columna de la celda
   * @param row la fila de la celda
   * @param cell el valor de la celda
   */
  public void setCell(int col, int row, Cell cell) {
    Column column = this.columnOrderMap.get(col);
    column.setCell(row, cell);
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
    Column column = null;
    for (Map.Entry<Column, String> entry : this.columnLabelsMap.entrySet()) {
      if (entry.getValue().equals(colLabel)) {
        column = entry.getKey();
        break;
      } else {
        System.out.println("No se encontró la columna");
      }
    }
    if (column != null) {
      column.setCell(row, cell);
    }
  }

  /**
   * Limpia el mapa de etiquetas de fila y luego lo llena con las etiquetas de fila correspondientes a cada índice de fila.
   */
  public void setRowLabels() {
    rowLabelsMap.clear();
    for (Integer i = 0; i < columns.get(0).size(); i++) {
      rowLabelsMap.put(i.toString(), i);
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
    out += "\n";
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
        columnLabelsMap.put(this.columns.get(i), columnName);
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
      columnLabelsMap.put(column, columnName);
    }
  }

  /**
   * Establece el nombre de la columna especificada en el mapa de etiquetas de columna.
   * @param column la columna a la que se le asignará el nombre de la etiqueta.
   * @param columnName el nombre de la etiqueta de la columna.
   */
  public void setColumLabels(Column column, String columnName) {
    columnLabelsMap.put(column, columnName);
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
    out += "\n";
    return out;
  }

  /** //TODO definir si es un getter...
   * Retorna un arreglo de Strings con los nombres de las columnas del DataFrame.
   * 
   * @return un arreglo de Strings con los nombres de las columnas del DataFrame.
   */
  private String[] listColumnLabels() {
    String[] labels = new String[this.columnLabelsMap.size()];
    for (Integer key : this.columnOrderMap.keySet()) {
      Column column = this.columnOrderMap.get(key);
      String columnName = this.columnLabelsMap.get(column);
      labels[key] = columnName;
    }
    return labels;
  }

  
  /**
   * Retorna el índice de la columna con la etiqueta especificada.
   * 
   * @param label la etiqueta de la columna cuyo índice se desea obtener.
   * @return el índice de la columna con la etiqueta especificada.
   */
  public int getColumnIndex(String label) {
    int index = -1;
    for (Integer key : this.columnOrderMap.keySet()) {
      Column column = this.columnOrderMap.get(key);
      String columnName = this.columnLabelsMap.get(column);
      if (columnName.equals(label)) {
        index = key;
        break;
      }
    }
    return index;
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


  // metodo que implimenta RandomSample
  //public DataFrame randomSample(float p) {
    //TODO: IMPLEMTNEAR
    /*quiero llamar a la funcion sample del paquete utils_df y la clase randomsampe
    *    if (p == null) {
    *      float localP = (float) Math.random();
    *    }
    *
    *    // llamo al medotod sample del package utils_df en la clase RandomSample
    *    DataFrame df = utils_df.RandomSample.sample(this, p);
    *
    *    return 
    *  }
    */
      
  //SHALLOW COPY
  private DataFrame(DataFrame dataframe) {
    this.columns = new ArrayList<Column>();
    this.columnLabelsMap =
      new HashMap<Column, String>(dataframe.columnLabelsMap);
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
    DataFrame CopyDataFrame = new DataFrame();

    for (Column columnaOriginal : this.columns) {
      Column columnaCopia;
      List<Cell> contenidoCopia = new ArrayList<Cell>();
      for (Cell celdaOriginal : columnaOriginal.getContent()) {
        // Utiliza el método copy() de Cell para realizar una CopyDataFrame profunda de las celdas
        Cell celdaCopia = celdaOriginal.copy();
        contenidoCopia.add(celdaCopia);
      }
      columnaCopia = new Column(contenidoCopia);
      CopyDataFrame.addColumn(columnaCopia);

      // Copia los mapeos de etiquetas y órdenes de las columnas
      String label = this.columnLabelsMap.get(columnaOriginal);
      CopyDataFrame.columnLabelsMap.put(columnaCopia, label);

      Integer order =
        this.columnOrderMap.entrySet()
          .stream()
          .filter(entry -> entry.getValue().equals(columnaOriginal))
          .map(Map.Entry::getKey)
          .findFirst()
          .orElse(null);
      if (order != null) {
        CopyDataFrame.columnOrderMap.put(order, columnaCopia);
      }
    }

    // Copia el mapeo de etiquetas de filas
    CopyDataFrame.rowLabelsMap.putAll(this.rowLabelsMap);

    // Copia el número de filas y columnas
    CopyDataFrame.numRows = this.numRows;
    CopyDataFrame.numCols = this.numCols;

    return CopyDataFrame;
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

  // Filtrado
  // TODO WIP FILTRADO
  public DataFrame filtrarPorCriterio(String nombreColumna, String operador, Object valor) {
    DataFrame resultado = new DataFrame();
    int columnIndex = getColumnIndex(nombreColumna);  
    if (columnIndex == -1) {
      // La columna no existe, puedes manejar este caso según tus necesidades
      return resultado;

    Column columna = columns.get(columnIndex);
    // Creo una columna temporal para guardar las cells que pasen el test
    Column columnaFiltrada = new Column(new ArrayList<Cell>());
    for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
      Cell cell = columna.getCell(rowIndex);
      Predicate<Cell> criterio;
      switch (operador) {
        case ">":
          criterio = c -> ((Comparable<T>) valor).compareTo((T) c.getValue()) < 0;
          break;
        case "<":
          criterio = c -> ((Comparable<T>) valor).compareTo((T) c.getValue()) > 0;
          break;
        case "==":
          criterio = c -> ((Comparable<T>) valor).compareTo((T) c.getValue()) == 0;
          break;
        default:
          throw new IllegalArgumentException("Operador no soportado: " + operador);
      }
      if (criterio.test(cell)) {
        for (Column col : columns) {
          // Agregar celdas de todas las columnas para la fila actual
          columnaFiltrada.addCell(col.getCell(rowIndex));
        }
      }
    }
    resultado.addColumn(columnaFiltrada);
      return resultado;
   }
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
  public String toString(String separador) {
    if (separador == null) {
      separador = " | ";
    }

    String out = "";
    String sep = " " + separador + " ";
    String[] labels = this.listColumnLabels();
    int[] colWidths = new int[labels.length];

    for (int i = 0; i < labels.length; i++) {
      colWidths[i] = ("[" + labels[i] + "]").length();
      for (int j = 0; j < this.numRows; j++) {
        String cellValue =
          this.columnOrderMap.get(i).getContent().get(j).toString();
        colWidths[i] = Math.max(colWidths[i], cellValue.length());
      }
    }

    for (int i = 0; i < labels.length; i++) {
      if (i == 0) {
        out += String.format("%-" + colWidths[i] + "s", "") + sep;
      }
      out +=
        String.format("%-" + colWidths[i] + "s", "[" + labels[i] + "]") + sep;
    }
    out += "\n";

    for (Integer row = 0; row < this.numRows; row++) {
      this.rowLabelsMap.put(row.toString(), row);
      int rowPadding = colWidths[0] - row.toString().length();
      int leftRowPadding = rowPadding / 2;
      int rightRowPadding = rowPadding - leftRowPadding;
      out +=
        String.format(
          "%-" +
          (leftRowPadding + row.toString().length() + rightRowPadding) +
          "s",
          "[Fila: " + row + "]"
        ) +
        sep;
      for (int i = 0; i < labels.length; i++) {
        String cellValue =
          this.columnOrderMap.get(i).getContent().get(row).toString();
        int padding = colWidths[i] - cellValue.length();
        int leftPadding = padding / 2;
        int rightPadding = padding - leftPadding;

        out +=
          String.format(
            "%-" + (leftPadding + cellValue.length() + rightPadding) + "s",
            cellValue
          ) +
          sep;
      }
      out += "\n";
    }

    return out;
  }
}
