package Lector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lecturacsv {
     public static void main(String[] args) {
        String csvFilePath = "./src/utils/industry_sic.csv";
    
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            List<String[]> data = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);

                // Inferir el tipo de dato de cada columna
                for (String cell : row) {
                    String dataType = Identificador.inferDType(cell);
                    System.out.println("Tipo de dato inferido: " + dataType);
                }
            }

            // Continúa con el procesamiento de los datos aquí
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




