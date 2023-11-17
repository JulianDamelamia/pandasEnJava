import dataframe.Column;
import dataframe.DataFrame;
import java.io.IOException;
import java.util.Map;

import utils_df.RandomSample;

public class App {

  public static void main(String[] args) throws IOException {
    DataFrame df = new DataFrame();
    df = df.readCSV("./examples/libro2.csv");
    df.show();
    df.info();
    // Checklist
    // obtener filas y columnas
    df.getCantColumnas();
    System.out.println(df.getColumnType(1));
    System.out.println(df.getColumn(1));
    System.out.println(df.getCell(1, 1)); // Cambiar para que reciba las etiquetas
    System.out.println(df.getCell("Columna 0", "0" ));
    System.out.println(df.getCell("Columna 0", "6" ));
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
    System.out.println("-> Con una fila eliminada");
    nativo2.show();
    nativo2.deleteColumn(2);
    System.out.println("-> Con una columna eliminada");
    nativo2.show();
    nativo2.deleteCell(1,2);
    System.out.println("-> Con la celda (2,3 eliminada)");
    nativo2.show();

    Integer[][] m1 = {
        {1, 2, 3, 4},
        {1, 1, 7, 8},
        {9, 1, 3, 9},
        {9, 1, 3, 9},
        {1, 1, 7, 8},
        {9, 1, 3, 9},
        {9, 1, 3, 9},
        {1, 1, 7, 8},
        {9, 1, 3, 9},
        {9, 1, 3, 9}

    };
    DataFrame nativo = new DataFrame(m1);
    nativo.show();

    nativo.deleteRow(2);
    System.out.println("-> Con una fila eliminada");
    nativo.show();
    nativo.deleteColumn(2);
    System.out.println("-> Con una columna eliminada");
    nativo.show();
    nativo.deleteCell(1,2);
    System.out.println("-> Con la celda (2,3 eliminada)");
    nativo.show(); 

    // Realizo una seleccion parcial
    DataFrame df_seleccion = df.select( new String[]{"0", "1"}, new String[]{"Columna 0", "Columna 1"}); // select(String[] rowLabels, String[] colLabels) 
    df_seleccion.show();

   
// TODO cambiar nombre de getColumnLabels y listcolumnLabels

    // // Pruebo head y tail
    df.tail();
    df.head();
    // df.show();
    DataFrame df_select_amldito = df.select(new String[] {"6","7","8","9","10"}, new String[] {"Columna 0", "Columna 1", "Columna 2", "Columna 3"});

    System.out.println("\n"+ df_select_amldito.rowOrderMap.toString());
    System.out.println("\n"+ df_select_amldito.rowLabelsMap.toString());
    System.out.println("\n"+ df_select_amldito.columnOrderMap.toString());
    System.out.println("\n"+ df_select_amldito.columnLabelsMap.toString());



    
  
  }


}