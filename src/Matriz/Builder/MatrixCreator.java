package Matriz.Builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MatrixCreator {
    private String[][] matrix;

    //Constructor
    public MatrixCreator(String[][] matrix) {
        this.matrix = matrix;
    }
    
    public static String[][] createMatrix(String filePath) throws IOException{
        File file = new File(filePath);

        // Crea un objeto BufferedReader para leer el archivo CSV
        BufferedReader reader = new BufferedReader(new FileReader(file));

        // Crea un objeto ArrayList para almacenar los datos del archivo CSV
        List<String[]> lines = new ArrayList<>();

        // Lee el archivo CSV línea por línea y agrega cada línea al objeto ArrayList
        String line;
        while ((line = reader.readLine()) != null) {
            String[] row = line.split(",");
            lines.add(row);
        }

        // Crea un objeto String[][] con las dimensiones adecuadas para la matriz
        String[][] matrix = new String[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            matrix[i] = new String[lines.get(i).length];
        }

        // Recorre el objeto ArrayList y agrega cada elemento a la matriz en la posición correspondiente
        for (int i = 0; i < lines.size(); i++) {
            String[] elements = lines.get(i);
            for (int j = 0; j < elements.length; j++) {
                matrix[i][j] = elements[j];
            }
        }
        reader.close();

        // Retorna la matriz creada
        return matrix;
    }
    
}
