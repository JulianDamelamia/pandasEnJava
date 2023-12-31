package dataframe.utils_df;

import dataframe.DataFrame;

/**
 * Clase que contiene métodos para seleccionar filas de un DataFrame.
 */
public class Selection {

  /**
   * Retorna un nuevo DataFrame con las primeras 5 filas de cada columna del DataFrame original.
   * @param df El DataFrame original.
   * @return Un nuevo DataFrame con las primeras 5 filas de cada columna del DataFrame original.
   */
  public static DataFrame head(DataFrame df) {
    int numRows = Math.min(5, df.getRowNumber()); // Evita desbordamiento si hay menos de 5 filas
    return selectOperator(df, numRows, true);
  }

  private static DataFrame selectOperator(DataFrame df, int numFilas, boolean primeras) {
    DataFrame dfSeleccionado = new DataFrame();

    // Calcular los índices de las filas que se seleccionarán
    int start, end;
    if (primeras) {
        start = 0;
        end = Math.min(numFilas, df.getRowNumber());
    } else {
        start = Math.max(0, df.getRowNumber() - numFilas);
        end = df.getRowNumber();
    }

    // Seleccionar las filas y columnas correspondientes
    String[] rowLabels = new String[end - start];
    for (int row = start; row < end; row++) {
        rowLabels[row - start] = df.rowLabels()[row];
    }

    String[] colLabels = df.columnLabels();

    dfSeleccionado = df.select(rowLabels, colLabels);

    return dfSeleccionado;
}

  /**
   * Retorna un nuevo DataFrame con las últimas 5 filas de cada columna del DataFrame original.
   * @param df El DataFrame original.
   * @return Un nuevo DataFrame con las últimas 5 filas de cada columna del DataFrame original.
   */
public static DataFrame tail(DataFrame df) {
    int numRows = Math.min(5, df.getRowNumber()); // Evita desbordamiento si hay menos de 5 filas
    return selectOperator(df, numRows, false);
}
}
