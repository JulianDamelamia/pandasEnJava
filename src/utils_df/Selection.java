package utils_df;

import dataframe.DataFrame;
import dataframe.Column;
import dataframe.cells.Cell;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase Selection proporciona métodos para seleccionar filas de un DataFrame.
 * Los métodos head y tail devuelven un nuevo DataFrame que contiene las primeras o últimas filas del DataFrame original, respectivamente.
 */
public class Selection {
    public static DataFrame head(DataFrame df) {

        DataFrame dfHead = new DataFrame();
    
        int numRows = Math.min(5, df.getCantFilas()); // Evita desbordamiento si hay menos de 5 filas
    
        for (int i = 0; i < df.getCantColumnas(); i++) {
            Column column = df.getColumn(i);
            List<Cell> cells = column.getContent();
            List<Cell> newColumnCells = new ArrayList<>(cells.subList(0, numRows));
            dfHead.addColumn(newColumnCells);
        }
    
        return dfHead;
    }
    
    public static DataFrame tail(DataFrame df) {
        DataFrame dfTail = new DataFrame();
    
        int numRows = Math.min(5, df.getCantFilas()); // Evita desbordamiento si hay menos de 5 filas
    
        for (int i = 0; i < df.getCantColumnas(); i++) {
            Column column = df.getColumn(i);
            List<Cell> cells = column.getContent();
            int startIndex = Math.max(0, cells.size() - numRows); // Asegura que no se inicie desde un índice negativo
            List<Cell> newColumnCells = new ArrayList<>(cells.subList(startIndex, cells.size()));
            dfTail.addColumn(newColumnCells);
        }
    
        return dfTail;
    }   
    
    
}
