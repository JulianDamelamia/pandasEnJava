import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import dataframe.Column;
import dataframe.DataFrame;
import dataframe.cells.BooleanCell;
import dataframe.cells.Cell;
import dataframe.cells.NACell;
import dataframe.cells.NumericCell;
import dataframe.cells.StringCell;
import src.Identificador;

public class Archivos {
    //Importar CSV dentro de DataFrame
    public static List<String> leerLineas(String filepath) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))) {
            String linea;
            List<String> lineas = new LinkedList<>();
            while ((linea = bufferedReader.readLine()) != null) {
                lineas.add(linea);
            }
            return lineas;
        } catch (IOException e) {
            throw e;
        }
    }

    public static String[][] parserLineas(List<String> lineas) throws CSVParserException {
        int filas = lineas.size();
        String[][] celdas = null;
        for(int i=0; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] campos = linea.split(",");
            if (celdas == null) {
                celdas = new String[filas][campos.length];
            }
            if (celdas[0].length != campos.length) {
                throw new CSVParserException(i, celdas[0].length, campos.length);            }
            for(int j=0; j < campos.length; j++) {
                celdas[i][j] = campos[j];
            }
        }
        return celdas;
        
    }

    public static void mostrarLineas(List<String> lineas) {
        for (String linea : lineas) {
            System.out.println(linea);
        }
    }
    //desde celdas importadas a dataframe
    public static DataFrame createDfFromParsed(String[][] celdas) {
        DataFrame df = new DataFrame();
        List<Cell> listaCeldas = new ArrayList<Cell>();
        Identificador identif;

        int cantCol = celdas[0].length;
            
        for (String[] fila : celdas) {
            for (String celda : fila) {
                identif = new Identificador(celda);
                switch (identif.getType()) {
                    case "STRING":
                        StringCell StrCell = new StringCell(celda);
                        listaCeldas.add(StrCell);
                    break;
                    case "FLOAT":
                        NumericCell FltCell = new NumericCell(Float.valueOf(celda));
                        listaCeldas.add(FltCell);
                    break;
                    case "INTEGER":
                        NumericCell IntCell = new NumericCell(Integer.valueOf(celda));
                        listaCeldas.add(IntCell);
                    break;
                    case "NA":
                        NACell NACell = new NACell();
                        listaCeldas.add(NACell);
                    break;
                    case "BOOLEAN":
                        BooleanCell BlnCell = new BooleanCell(Boolean.valueOf(celda));
                        listaCeldas.add(BlnCell);
                    break;
                    default:
                        StringCell DftCell = new StringCell(celda);
                        listaCeldas.add(DftCell);
                    break;
                }
                //System.out.println(celda);
                //listaCeldas.add(cell);
            }
        }

        // for (int i=0; i < celdas.length; i++) {
        //     for (int j=0; j < celdas[0].length; j++) {
        //         StringCell celda = new StringCell(celdas[i][j]);
        //         listaCeldas.add(celda);
        //         System.out.println(celdas[i][j]);
        //     }
        // }

        for(int j = 0; j < cantCol; j++){
            List<Cell> listaCeldasAux = new ArrayList<Cell>();
            for(int i = 0+j; i < listaCeldas.size(); i += cantCol){
                if(i <= listaCeldas.size()){
                    System.out.println(i + " , " +  j + " , " +  listaCeldas.size());
                    System.out.print(listaCeldas.get(i).toString()+"\n");
                    listaCeldasAux.add(listaCeldas.get(i));

                }
            }
            Column columnaAux = new Column(new ArrayList<Cell>(listaCeldasAux));
            df.addColumn(columnaAux);
        }

        return df;
        // Otra forma de iterar...
        // for (int i=0; i < celdas.length; i++) {
        //     for (int j=0; j < celdas[0].length; j++) {
        //         System.out.println(celdas[i][j]);
        //     }
        // }
    }

    public static void exportCSV(String filepath, DataFrame df) {
        String texto = df.toString(",");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))) {
            bufferedWriter.write(texto);
            // Si imprimiera por lineas (String[])
            // for(int i=0; i < lineas.length; i++) {
            //     bufferedWriter.write(lineas[i]);
            //     if (i < lineas.length - 1)
            //         bufferedWriter.newLine();
            // }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DataFrame readCSV(String filePath) throws IOException {
        /*
         * Lee un archivo CSV y lo convierte en un DataFrame
         * 
         * @param filePath: Ruta del archivo CSV
         * 
         * @return DataFrame
         * 
         */
        List<String> lineasLeidas = leerLineas(filePath);
        String[][] celdas;
        try {
            celdas = parserLineas(lineasLeidas);
            return createDfFromParsed(celdas);
        } catch (CSVParserException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void show(DataFrame df) {
        System.out.println(df.toString("|"));
    }
    public static void main(String[] args) throws IOException {
        String filePath = "./utils/libro2.csv";
        DataFrame df = readCSV(filePath);
        if (df != null) {
            show(df);
            System.out.println(df.columnLabelsMap.values());
            // exportCSV("test1.csv", df);
        }
        //Creo un random sample de 10 filas
        // RandomSample randomSample = new RandomSample();
        // DataFrame dfSample = randomSample.sample(df);
        // show(dfSample);
        //show(dfSample.getRowLabels());

        /*
        *   Muestra el dataframe en consola

        *   @param df: DataFrame a mostrar
        *   @return void
        */
        System.out.println(df.toString("|"));
    }
    
}


