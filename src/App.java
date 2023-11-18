import dataframe.Column;
import dataframe.DataFrame;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import utils_df.RandomSample;

public class App {

  public static void main(String[] args) throws IOException, IllegalArgumentException, ParseException {
    DataFrame df = new DataFrame();
    df = df.readCSV("./examples/libro2.csv");
    // df.show();
    // df.info();
    // Checklist
    // obtener filas y columnas
    // df.getCantColumnas();
    // System.out.println(df.getColumnType(1));
    // System.out.println(df.getColumn(1));
    // System.out.println(df.getCell(1, 1)); // Cambiar para que reciba las etiquetas
    // System.out.println(df.getCell("Columna 0", "0" ));
    // System.out.println(df.getCell("Columna 0", "6" ));
    //df.exportCSV("test.csv"); //TODO: arreglar el exportCSV
    
    // Creo desde una estructura nativa un dataframe

// TODO CHEQUEAR ESTO
    Integer[][] m2 = {
        {1, 2, 3, 4},
        {9, 1, 3, 9},
        {1, 1, 7, 8},
    };
    DataFrame nativo2 = new DataFrame(m2);


    nativo2.deleteRow(2);
    // System.out.println("-> Con una fila eliminada");
    // nativo2.show();
    nativo2.deleteColumn(2);
    // System.out.println("-> Con una columna eliminada");
    // nativo2.show();
    nativo2.deleteCell(1,2);
    // System.out.println("-> Con la celda (2,3 eliminada)");
    // nativo2.show();

    Integer[][] m1 = {
        {1, 2, 3, -1},
        {2, 4, 6,-2},
        {4, 6, 9, -3},
        {3, 8, 12, -4},
        {5, 10, 15, -5},
        {6, 12, 18, -6},
        {7, 14, 21, -7},
        {8, 16, 24, -8},
        {9, 18, 27, -9},
        {10, 20, 30, -10}
    };
    DataFrame nativo = new DataFrame(m1);
    // nativo.show();
    String[] viejas = new String[]{"0", "1", "2", "3"};
    String[] nuevas = new String[]{"f0", "f1", "f2", "f3"};
    for (int i = 0; i < viejas.length; i++) {
        nativo.setRowLabel(viejas[i], nuevas[i]);
    }
//     DataFrame nativoDeleteado = nativo.deleteRow(2);
//     System.out.println("-> Con una fila eliminada");
//     nativoDeleteado.show();
//     // nativo.deleteColumn(2);
//     // System.out.println("-> Con una columna eliminada");
//     // nativo.show();
//     // nativo.deleteCell(1,2);
//     // System.out.println("-> Con la celda (2,3 eliminada)");
        nativo.exportCSV("test_nativo.csv");

//     // Realizo una seleccion parcial
//     DataFrame df_seleccion = df.select( new String[]{"0", "1"}, new String[]{"Columna 0", "Columna 1"}); // select(String[] rowLabels, String[] colLabels) 
//     df_seleccion.show();

   
// // TODO cambiar nombre de getColumnLabels y listcolumnLabels

    // // Pruebo head y tail
    System.out.println("TAIL()");
    df.tail();
    System.out.println("\n HEAD()");
    df.head();
    df.show();
    
    DataFrame dfSorted = nativo.sort();
    dfSorted.show();
    dfSorted.exportCSV("test_nativo.csv");
    System.out.println("Rows");
    System.out.println(dfSorted.getRows() + "\n");
    // DataFrame nativoSorted = nativo.sort("Columna 3");
    // nativoSorted.show();
  // nativo.filter("Columna 1", "!=", 4).show();
    
    nativo.filter("Columna 0 = 3 or Columna 3 < -9").show();
    //nativo.exportCSV("test_nativo.csv");
}


}