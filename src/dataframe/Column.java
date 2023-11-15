package dataframe;

import dataframe.cells.Cell;
import dataframe.cells.NACell;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una columna de un DataFrame.
 */
public class Column {

  private int size;
  private ArrayList<Cell> content;

  /**
   * Constructor vacío que inicializa una columna sin elementos.
   */
  public Column() {
    this.size = 0;
    this.content = new ArrayList<Cell>();
  }

  /**
   * Constructor que inicializa una columna con un tamaño específico.
   * @param size el tamaño de la columna.
   */
  public Column(int size) {
    this.size = size;
    this.content = new ArrayList<Cell>();
  }

  /**
   * Constructor que inicializa una columna con una lista de celdas.
   * @param content la lista de celdas que conforman la columna.
   */
  public Column(ArrayList<Cell> content) {
    this.size = content.size();
    this.content = content;
  }

  /**
   * Constructor que inicializa una columna con un tamaño específico y una lista de celdas.
   * @param size el tamaño de la columna.
   * @param content la lista de celdas que conforman la columna.
   */
  public Column(int size, ArrayList<Cell> content) {
    this.size = size;
    this.content = content;
  }

  /**
   * Constructor privado que inicializa una columna a partir de otra columna.
   * @param original la columna original a partir de la cual se creará la nueva columna.
   */
  private Column(Column original) {
    this(original.size(), original.getContent());
  }

  /**
   * Crea una copia de la columna actual.
   * @return una nueva columna que es una copia de la columna actual.
   */
  public Column copy() {
    Column copyColumn = new Column(this);
    copyColumn.content = new ArrayList<Cell>(size);
    for (Cell cell : this.content) {
      Cell copyCell = cell.copy();
      copyColumn.content.add(copyCell);
    }
    return copyColumn;
  }

  /**
   * Establece el valor de una celda en la posición especificada.
   *
   * @param index la posición de la celda a establecer.
   * @param value el valor a establecer en la celda.
   */
  public void setCell(int index, Comparable<Object> value) {
    this.content.get(index).setValue(value);
  }

  /**
   * Establece el contenido de la columna a partir de una lista de celdas.
   * @param content la lista de celdas que conformarán la columna.
   */
  public void setContent(ArrayList<Cell> content) {
    this.content = content;
    this.size = content.size();
  }

  /**
   * Obtiene la celda en la posición especificada.
   * @param index la posición de la celda a obtener.
   * @return la celda en la posición especificada.
   */
  public Cell getCell(int index) {
    return this.content.get(index);
  }

  /**
   * Obtiene el tamaño de la columna.
   * @return el tamaño de la columna.
   */
  public int size() {
    return this.size;
  }

  /**
   * Constructor que inicializa una columna con una lista de celdas.
   * @param cells la lista de celdas que conforman la columna.
   */
  public Column(List<Cell> cells) {
    this.size = cells.size();
    this.content = new ArrayList<>(cells);
  }

  /**
   * Obtiene el contenido de la columna.
   * @return el contenido de la columna.
   */
  public ArrayList<Cell> getContent() {
    return this.content;
  }

  /**
   * Devuelve una representación en cadena de la columna.
   * @return una representación en cadena de la columna.
   */
  @Override
  public String toString() {
    String result = "";
    for (Cell cell : this.content) {
      result += cell.toString() + " ";
    }
    return result;
  }

  protected void removeCell(int index) {
    if (index >= 0 && index < size) {
        content.remove(index);
        size--;
    } else {
        throw new IndexOutOfBoundsException("Índice de celda fuera de rango");
    }
  }

  public void removeCellbyNA(int index) {
    if (index >= 0 && index < size) {
        content.set(index, new NACell()); // Reemplazar la celda en el índice por un objeto NACell
    } else {
        throw new IndexOutOfBoundsException("Índice de celda fuera de rango");
    }
}
}
