import dataframe.Column;
import dataframe.DataFrame;
import dataframe.cells.Cell;
import dataframe.cells.NumericCell;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;


public class App {

  public static void main(String[] args) throws IOException, IllegalArgumentException, ParseException {
    long startTime = System.nanoTime();
    DataFrame df = new DataFrame();
    DataFrame df2 = new DataFrame();

    Integer[][] m1 = {
        {1, 2, 3, 4},
        {2, 1, 7, 8},
        {9, 1, 3, 9},
        {5, 2, 3, 4},
        {3, 1, 7, 8},
        {12, 1, 7, 8}
    };

    // Demostracion basica, apertura de archivo, visualizacion e informacion
    System.out.println("-> Dataframe leido de un archivo: ");
    df = df.readCSV("./examples/iris.csv");
    df.show();
    df.info();

    // Apertura de archivo con separador personalizado
    System.out.println("-> Dataframe leido de un archivo con separador ;");
    df2 = df2.readCSV("./examples/libro2.csv", ";");
    df2.show();

    // Demostracion de visualizacion
    System.out.println("-> ShowAll: ");
    df.showAll();
    System.out.println("-> ShowAllColumns: ");
    df.showAllColumns();
    System.out.println("-> ShowAllRows: ");
    df.showAllRows();

    // Checklist
    System.out.println("-> Metodo que retorna cantidad de columnas: " + df.getColumnNumber());
    System.out.println("-> Metodo que retorna cantidad de filas: " + df.getRowNumber());
    System.out.println("-> Metodo que retorna las etiquetas de las columnas: " + df.getColumnLabels());
    System.out.println("-> Metodo que retorna las etiquetas de las filas: " + df.getRowLabels());
    System.out.println("-> Tipo de datos de la columna 1: " + df.getColumnType(1));
    System.out.println("-> Toda la columna 1: " + df.getColumn(1));
    System.out.println("-> Toda la fila 1: " + df.getRow(1));
    System.out.println("-> Toda la columna 1: " + df.getColumn("Columna 1"));
    System.out.println("-> Toda la fila 1: " + df.getRow("1"));
    System.out.println("-> Celda (1,1): " + df.getCell(1, 1)); // Cambiar para que reciba las etiquetas
    System.out.println("-> Celda (0,0) solicitada por etiquetas: " + df.getCell("Columna 0", "0" ));

    
    // Dataframe cargado a partir de estructura nativa
    System.out.println("-> Dataframe cargado a partir de estructura nativa: ");
    DataFrame nativo = new DataFrame(m1);
    nativo.show();

    // Dataframe cargado a partir de estructura nativa (CONVIERTE A FLOAT, BUG :[ )
    System.out.println("-> Copia de nativo: ");
    DataFrame copia = nativo.copy();
    copia.show();

    // Dataframe cargado a partir de estructura nativa (CONVIERTE A FLOAT, BUG :[ )
    NumericCell celda1 = new NumericCell(1);
    NumericCell celda2 = new NumericCell(2);
    NumericCell celda3 = new NumericCell(3);
    Column columna1 = new Column(new ArrayList<Cell>(Arrays.asList(celda1, celda2, celda3,celda3,celda3,celda3))); //numerica
    nativo.addColumn(columna1,"NUMERICA");
    System.out.println("-> Agregando una columna: ");
    nativo.show();

    // Eliminaciones de estructura tabular
    System.out.println("-> Con una fila eliminada");
    nativo.deleteRow(2);
    nativo.show();

    System.out.println("-> Con una columna eliminada");
    nativo.deleteColumn(2);
    nativo.show();

    System.out.println("-> Con la celda (2,3 eliminada)");
    nativo.deleteCell(1,2);
    nativo.show();

    // Seleccion especifica de columnas y filas deseadas
    System.out.println("-> Seleccion especifica de columnas y filas deseadas: ");
    nativo.select(new String[] {"0","5"}, new String[] {"Columna 0", "Columna 1"}).show();

    // Head del dataframe IRIS
    System.out.println("-> Dataframe nativo ordenado: ");
    nativo.sort().show();

    // Head del dataframe IRIS
    System.out.println("-> Head del dataframe cargado por archivo: ");
    df.head();

    // Head del dataframe IRIS
    System.out.println("-> Tail del dataframe cargado por archivo: ");
    df.tail();

    // Promedio de la primer columna
    System.out.println("-> Promedio de la primer columna: " + df.mean(1));

    // Varianza de la primer columna
    System.out.println("-> Varianza de la primer columna: " + df.variance(1));

    // Desviacion estandar de la primer columna
    System.out.println("-> Desviacion estandar de la primer columna: " + df.standardDeviation(1));

    // Minimo de la primer columna
    System.out.println("-> Minimo de la primer columna: " + df.min(1));

    // Maximo de la primer columna
    System.out.println("-> Maximo de la primer columna: " + df.max(1));

    // Creo un dataframe con una muestra aleatoria
    System.out.println("\n-> Muestra aleatoria del DF creado a partir de CSV: ");
    df.randomSample().show();

    // Creo un dataframe con una muestra aleatoria con porcentual fijo
    System.out.println("-> Muestra aleatoria del DF en base a porcentaje determinado: ");
    df.randomSample(0.2).show();

    // Creo un dataframe concatenando dos Dataframes
    DataFrame concatenado = df.concatenate(df, df.randomSample(0.5));
    System.out.println("-> Dataframe concatenado: ");
    concatenado.show();
    
    // Filtro el dataframe nativo partiendo de una query
    System.out.println("-> Dataframe filtrado por query [Columna 0 != 9 or Columna 3 > 6]: ");
    nativo.filter("Columna 0 != 9 and Columna 3 > 6").show();
    System.out.println("-> Dataframe filtrado por query [Columna 0 < 5 and Columna 3 = 8]: ");
    nativo.filter("Columna 0 < 5 and Columna 3 = 8").show();

    // Display de tiempo
    long endTime = System.nanoTime();
    System.out.println("[DEBUG: this process took " + (endTime - startTime) + " ns]\n");

    // Exporto a CSV
    df.exportCSV("test.csv");
}


}