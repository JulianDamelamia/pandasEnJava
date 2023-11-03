package utils_df;
import java.util.ArrayList;
import java.util.List;

import dataframe.Column;
import dataframe.DataFrame;
import dataframe.cells.Cell;


public class DataFrameConcatenator {
    public static DataFrame concatenate(DataFrame df1, DataFrame df2) {
        
        // Verificar si las estructuras tabulares tienen las mismas columnas y etiquetas de fila
        if (!haveSameColumnsAndRowLabels(df1, df2)) {
            // Enviar una indicación de que las estructuras no son compatibles
            System.err.println("No se pueden concatenar las estructuras tabulares. Las columnas o etiquetas de fila son diferentes.");
            return null;
        }

        // Crear un nuevo DataFrame para la concatenación
        DataFrame concatenatedDataFrame = new DataFrame();

        
        //Iterar sobre las columnas de los dos dataframes y crear una nueva columna en el dataframe concatenado
        for (int i = 0; i < df1.getCantColumnas(); i++) {
            // Obtener la columna del dataframe 1
            Column column1 = df1.getColumn(i);
            // Obtener la columna del dataframe 2
            Column column2 = df2.getColumn(i);

          

            // Obtener las celdas de la columna 1
            List<Cell> cells1 = column1.getContent();
            // Obtener las celdas de la columna 2
            List<Cell> cells2 = column2.getContent();

            // Crear una lista de celdas para la nueva columna
            List<Cell> newColumnCells = new ArrayList<Cell>();

            // Agregar las celdas de la columna 1 a la nueva columna
            newColumnCells.addAll(cells1);
            // Agregar las celdas de la columna 2 a la nueva columna
            newColumnCells.addAll(cells2);

            // Agregar la nueva columna al dataframe concatenado
            concatenatedDataFrame.addColumn(newColumnCells);
        }

        return concatenatedDataFrame;
    }

    private static boolean haveSameColumnsAndRowLabels(DataFrame df1, DataFrame df2) {
        // Verificar si las estructuras tienen la misma cantidad de columnas
        if (df1.getCantColumnas() != df2.getCantColumnas()) {
            // Imprimo en consola la cantidad de columnas de cada df
            System.out.println("Cantidad de columnas de df1: " + df1.getCantColumnas());
            System.out.println("Cantidad de columnas de df2: " + df2.getCantColumnas());

            return false;
        }

    

        // Verificar si las columnas tienen los mismos nombres
        /*for (int i = 0; i < df1.getCantColumnas(); i++) {
            if (!df1.getColumnLabels().equals(df2.getColumnLabels())) {
                return false;
            }
        }
        */

        return true;
    }

    
}
