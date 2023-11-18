import dataframe.Condition;
import dataframe.DataFrame;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import utils_df.RandomSample;

public class App {

  public static void main(String[] args) throws IOException, IllegalArgumentException, ParseException {
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
        {1, 1, 2, 3},
        {1, 2, 4, 6},
        {9, 3, 6, 9},
        {9, 4, 8, 12},
        {1, 5, 10, 15},
        {9, 6, 12, 18},
        {9, 7, 14, 21},
        {1, 8, 16, 24},
        {9, 9, 18, 27},
        {9, 10, 20, 30}

    };
    DataFrame nativo = new DataFrame(m1);
    nativo.show();

    // nativo.deleteRow(2);
    // System.out.println("-> Con una fila eliminada");
    // nativo.show();
    // nativo.deleteColumn(2);
    // System.out.println("-> Con una columna eliminada");
    // nativo.show();
    // nativo.deleteCell(1,2);
    // System.out.println("-> Con la celda (2,3 eliminada)");
    // nativo.show(); 

    //DataFrame resultado = nativo.filter("Columna 1 > 4 and Columna 2 > 5");
    DataFrame resultado = nativo.filter("Columna 1", ">", 5);
    resultado.show();
  }
}