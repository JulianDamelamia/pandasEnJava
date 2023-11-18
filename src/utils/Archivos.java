package utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dataframe.*;
import dataframe.cells.*;
import utils_df.Identificador;

public class Archivos {    
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

    public static String[][] parserLineas(List<String> lineas, String sep) throws CSVParserException {
        // TODO hacer el check de sep
        int filas = lineas.size();
        String[][] celdas = null;
        for(int i=0; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] campos = linea.split(sep);
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

    public static DataFrame createDfFromParsed(String[][] celdas) {
        DataFrame df = new DataFrame();
        List<Cell> listaCeldas = new ArrayList<Cell>();
        Identificador identif;

        int cantCol = celdas[0].length;
            
        for (String[] fila : celdas) {
            for (String celda : fila) {
                switch (Identificador.getType(celda)) {
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
            }
        }

        for(int j = 0; j < cantCol; j++){
            List<Cell> listaCeldasAux = new ArrayList<Cell>();
            for(int i = 0+j; i < listaCeldas.size(); i += cantCol){
                if(i <= listaCeldas.size()){
                    listaCeldasAux.add(listaCeldas.get(i));

                }
            }
            Column columnaAux = new Column(new ArrayList<Cell>(listaCeldasAux));
            df.addColumn(columnaAux);
        }
        df.setRowLabels();
        return df;
    }

    public static void exportCSV(String filepath, DataFrame df) {
        String texto = df.toString(",", true, true);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))) {
            bufferedWriter.write(texto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO 
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
            celdas = parserLineas(lineasLeidas, ",");
            return createDfFromParsed(celdas);
        } catch (CSVParserException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static DataFrame readCSV(String filePath, String sep) throws IOException {
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
            celdas = parserLineas(lineasLeidas, sep);
            return createDfFromParsed(celdas);
        } catch (CSVParserException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
}