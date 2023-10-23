import dataframe.Dataframe;
import dataframe.cells.NumericCell;
import dataframe.cells.BooleanCell;
import dataframe.cells.StringCell;
import dataframe.Column;
import dataframe.cells.Cell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
public class Main {
    public static void main(String[] args) {
        // armo unas celdas de prueba	
        NumericCell celda1 = new NumericCell(1);
        NumericCell celda2 = new NumericCell(2);
        NumericCell celda3 = new NumericCell(3);
        BooleanCell celda4 = new BooleanCell(true);
        BooleanCell celda5 = new BooleanCell(false);
        BooleanCell celda6 = new BooleanCell(true);
        StringCell celda7 = new StringCell("Hola");
        StringCell celda8 = new StringCell("Mundo");
        StringCell celda9 = new StringCell("!");
        
        // armo unas columnas de prueba a partir de las celdas
        Column columna1 = new Column(new ArrayList<Cell>(Arrays.asList(celda1, celda2, celda3)));
        Column columna2 = new Column(new ArrayList<Cell>(Arrays.asList(celda4, celda5, celda6)));
        Column columna3 = new Column(new ArrayList<Cell>(Arrays.asList(celda7, celda8, celda9)));

        // armo dos Maps, uno que linkea las columnas con un label
        // y otro que linkea el orden de la columna con la columna en sí
        // Decidí no linkear label-orden por la complicación que podría inducir cambiarle el nombre a una columna
        Map<Column, String> labelsMap = new HashMap<Column, String>();
        labelsMap.put(columna1, "NUMERICA");
        labelsMap.put(columna2, "BOOLEANA");
        labelsMap.put(columna3, "STRING");

        Map<Integer, Column> ordenMap = new HashMap<Integer, Column>();
        ordenMap.put(0, columna1);
        ordenMap.put(1, columna2);
        ordenMap.put(2, columna3);
        // System.out.println(labelsMap);
        // System.out.println(ordenMap);

        // for (Integer key : ordenMap.keySet()) {
        //     Column column = ordenMap.get(key);
        //     String columnName = labelsMap.get(column);
        //     System.out.println(column);
        // }
        // int numRows = ordenMap.get(0).size; // numero de filas. Observacion: columna.size es público de momento
        // for (int i = 0; i < numRows; i++) {
        //     for (Integer key : ordenMap.keySet()) {
        //         Column column = ordenMap.get(key);
        //         String columnName = labelsMap.get(column);
        //         System.out.print(column.getContent().get(i) + "\t");
        //     }
        //     System.out.println();
        // }
        // System.out.println(columna1);
        // System.out.println(columna2);
        // System.out.println(columna3);
        
        dataframe.Dataframe df = new dataframe.Dataframe();
        df.addColumn(columna1, "NUMERICA");
        df.addColumn(columna2, "BOOLEANA");
        df.addColumn(columna3);
        System.out.println(df);
    }
}
