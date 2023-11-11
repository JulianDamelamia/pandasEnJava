import dataframe.cells.NumericCell;
import dataframe.cells.BooleanCell;
import dataframe.cells.StringCell;
import utils.Archivos;
import dataframe.Column;
import dataframe.DataFrame;
import dataframe.cells.Cell;
import dataframe.cells.NACell;

import dataframe.DataFrame;
import utils_df.*;

import java.io.IOException;

public class Main {
    
// Estos comentarios son de cuando se mergeo con filtrado, fue necesario mergear esta parte para que ande MAIN
    // public static void main(String[] args) {
    //     // armo unas celdas de prueba	
    //     NumericCell celda1 = new NumericCell(1);
    //     NumericCell celda2 = new NumericCell(2);
    //     NumericCell celda3 = new NumericCell(3);
    //     BooleanCell celda4 = new BooleanCell(true);
    //     BooleanCell celda5 = new BooleanCell(false);
    //     BooleanCell celda6 = new BooleanCell(true);
    //     StringCell celda7 = new StringCell("Hola");
    //     StringCell celda8 = new StringCell("Mundo");
    //     StringCell celda9 = new StringCell("!");
    //     NACell cell = NACell.getInstance();
    //     // armo unas columnas de prueba a partir de las celdas
    //     Column columna1 = new Column(new ArrayList<Cell>(Arrays.asList(celda1, celda2, celda3))); //numerica
    //     Column columna2 = new Column(new ArrayList<Cell>(Arrays.asList(celda4, celda5, celda6)));
    //     Column columna3 = new Column(new ArrayList<Cell>(Arrays.asList(celda7, celda8, celda9)));

    //     // armo dos Maps, uno que linkea las columnas con un label
    //     // y otro que linkea el orden de la columna con la columna en sí
    //     // Decidí no linkear label-orden por la complicación que podría inducir cambiarle el nombre a una columna
    //     Map<Column, String> columnLabelsMap = new HashMap<Column, String>();
    //     columnLabelsMap.put(columna1, "NUMERICA");
    //     columnLabelsMap.put(columna2, "BOOLEANA");
    //     columnLabelsMap.put(columna3, "STRING");
// Estos comentarios son de cuando se mergeo con filtrado, fue necesario mergear esta parte para que ande MAIN

    public static void main(String[] args) throws IOException {
        String filePath = "./examples/libro2.csv";
        DataFrame df = Archivos.readCSV(filePath);
        if (df != null) {
            Archivos.show(df);
            //Archivos.exportCSV("test5.csv", df);
        }
    
        //Creo un random sample de df
        RandomSample randomSample = new RandomSample();
        DataFrame dfSample = randomSample.sample(df); //Cambiar el formato de retornar un DF a metodo nativo.
        
        System.out.println("Random Sample");
        Archivos.show(dfSample);

// Estos comentarios son de cuando se mergeo con filtrado, fue necesario mergear esta parte para que ande MAIN
        // for (Integer key : columnOrderMap.keySet()) {
        //     Column column = columnOrderMap.get(key);
        //     String columnName = columnLabelsMap.get(column);
        //     System.out.println(column);
        // }
        // int numRows = columnOrderMap.get(0).size; // numero de filas. Observacion: columna.size es público de momento
        // for (int i = 0; i < numRows; i++) {
        //     for (Integer key : columnOrderMap.keySet()) {
        //         Column column = columnOrderMap.get(key);
        //         String columnName = columnLabelsMap.get(column);
        //         System.out.print(column.getContent().get(i) + "\t");
        //     }
        //     System.out.println();
        // }
        // System.out.println(columna1);
        // System.out.println(columna2);
        // System.out.println(columna3);
        
        // dataframe.DataFrame df = new dataframe.DataFrame();
        // df.addColumn(columna1, "NUMERICA");
        // df.addColumn(columna2, "BOOLEANA");
        // df.addColumn(columna3);

        // Column columnaClonada = columna1.copy();
        // columnaClonada.setCell(1, 35);
        // System.out.println(columna1);
        // System.out.println(columnaClonada);
// Estos comentarios son de cuando se mergeo con filtrado, fue necesario mergear esta parte para que ande MAIN

       // estos renombres sí andan
        DataFrame df2 = df.copy();
        //df.setColumLabels(columna1, "NUMERICA renombrada");
        Cell newcell = new StringCell("TU MAMA");
        Integer value = 35;
        df.setCell(0, 1, value );
        System.out.println("Dataframe 1");
        System.out.println(df.toString("|"));
        System.out.println("Dataframe 2");
        System.out.println(df2.toString("|"));

        System.out.println("son iguales?");
        System.out.println(df2.equals(df));

        // Con df y dfSample los puedo concatenar
        DataFrame dfConcatenado = DataFrameConcatenator.concatenate(df, dfSample);
        System.out.println("Df Concatenado");

        Archivos.show(dfConcatenado);

        // Veo las primeras filas con el metodo head()
        DataFrame dfHead = Selection.head(df);
        System.out.println("Head");
        Archivos.show(dfHead);

        // Veo las ultimas filas con el metodo tail()
        DataFrame dfTail = Selection.tail(df);
        System.out.println("Tail");
        Archivos.show(dfTail);

    }
}