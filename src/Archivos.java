import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dataframe.Column;
import dataframe.DataFrame;
import dataframe.cells.Cell;
import dataframe.cells.StringCell;

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
        // List<Cell> listaCeldasAux = new ArrayList<Cell>();
        int cantCol = celdas.length;
            
        for (String[] fila : celdas) {
            for (String celda : fila) {
                StringCell row = new StringCell(celda);
                //System.out.println(celda);
                listaCeldas.add(row);
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

    // public static void exportCSV(String filepath, String[][] celdas) {
    //     String texto = celdasToString(celdas, ",");
    //     try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))) {
    //         bufferedWriter.write(texto);
    //         // Si imprimiera por lineas (String[])
    //         // for(int i=0; i < lineas.length; i++) {
    //         //     bufferedWriter.write(lineas[i]);
    //         //     if (i < lineas.length - 1)
    //         //         bufferedWriter.newLine();
    //         // }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    public static void main(String[] args) throws IOException {
        
        String dirpath = "prueba";
        String filepath = "./utils/libro1.csv";


        File directorio = new File(dirpath);
        if (!directorio.exists())
            directorio.mkdir();
        
        // File archivo = new File(filepath);
        
        // try {
        //     if (!archivo.exists())
        //         archivo.createNewFile();
        // } catch (IOException e) {
        //     System.out.println("Verificar la ruta");
        // }


        List<String> lineasLeidas = leerLineas(filepath);
        String[][] celdas;
        try {
            celdas = parserLineas(lineasLeidas);
            DataFrame df = new DataFrame();
            df = createDfFromParsed(celdas);
            System.out.println(df);

            //exportCSV("IRIS3.csv", celdas);             
        } catch (CSVParserException e) {
            System.out.println(e.getMessage());
        }

        
        // mostrarLineas(lineasLeidas);
        // String[] lineas = {
        //     "linea 1",
        //     "linea 2",
        //     "linea 3",
        //     "linea 4"
        // };

        // try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))) {
        //     for(int i=0; i < lineas.length; i++) {
        //         bufferedWriter.write(lineas[i]);
        //         if (i < lineas.length - 1)
        //             bufferedWriter.newLine();
        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

}

