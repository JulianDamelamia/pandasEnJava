import dataframe.DataFrame;
import java.io.IOException;
import java.text.ParseException;


public class App {

  public static void main(String[] args) throws IOException, IllegalArgumentException, ParseException {
    long startTime = System.nanoTime();
    DataFrame df = new DataFrame();
    df = df.readCSV("./examples/anticoncepcion_indonesia.csv");
    df.show();
    df.info();
    // Checklist
    // obtener filas y columnas
    df.getColumnNumber();
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
        {1, 2, 3, 4},
        {1, 1, 7, 8},
        {1, 1, 7, 8}
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

    // Exporto a CSV
    //nativo.exportCSV("test_nativo.csv");

    nativo.select(new String[] {"0","5"}, new String[] {"Columna 0", "Columna 1"}).show();

    // Creo un dataframe con una muestra aleatoria
    df.show();
    df.randomSample().show();
    df.randomSample(0.2).show();

    DataFrame concatenado = df.concatenate(df, df.randomSample(0.5));
    System.out.println("DataFrame concatenado!");
    concatenado.show();
    
    nativo.filter("Columna 0 != 10 and Columna 3 != 8").show();
    nativo.show();
    //nativo.exportCSV("test_nativo.csv");
    long endTime = System.nanoTime();
    System.out.println("[DEBUG: this process took " + (endTime - startTime) + " ns]");
  
}


}