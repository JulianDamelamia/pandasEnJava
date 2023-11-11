package utils_df;
import java.util.ArrayList;
import java.util.Random;

import dataframe.DataFrame;
import dataframe.cells.Cell;
import dataframe.Column;

public class RandomSample {

    // Método que a partir de un DataFrame y un porcentaje de filas, devuelve un DataFrame con las filas al azar
    public DataFrame sample(DataFrame df, float p) {
        // Verificar que el porcentaje esté en el rango [0, 1]
        
        if (p < 0 || p > 1) {
            System.err.println("El porcentaje debe estar en el rango [0, 1]");
            return null;
        }

        // Obtener el número de filas del DataFrame original
        int numRows = df.getCantFilas();

        // Calcular cuántas filas se seleccionarán
        int numSampleRows = (int) (numRows * p);

        // Crear un DataFrame para almacenar la muestra
        DataFrame sample = new DataFrame();

        // Generar una lista de índices aleatorios únicos
        ArrayList<Integer> randomIndices = generateRandomIndices(numRows, numSampleRows);

        // Agregar las columnas al DataFrame de la muestra
        for (int i = 0; i < df.getCantColumnas(); i++) {
            // Crear una columna para la muestra
            Column sampleColumn = new Column();
            ArrayList<Cell> cells = new ArrayList<Cell>();  // Create a new list of cells for each column

            // Agregar las celdas correspondientes a la muestra
            for (int rowIndex : randomIndices) {
                Cell cell = df.getCell(i, rowIndex);
                // Guardo las celdas en la list for this column
                cells.add(cell);
            }

            // Agregar the cells to the column
            sampleColumn.setContent(cells);

            // Agregar la columna al DataFrame de la muestra
            sample.addColumn(sampleColumn);
        }

        return sample;
    }
    public DataFrame sample(DataFrame df) {
        // Verificar que el porcentaje esté en el rango [0, 1]

        float p = (float) Math.random();
        if (p < 0 || p > 1) {
            System.err.println("El porcentaje debe estar en el rango [0, 1]");
            return null;
        }

        // Obtener el número de filas del DataFrame original
        int numRows = df.getCantFilas();

        // Calcular cuántas filas se seleccionarán
        int numSampleRows = (int) (numRows * p);

        // Crear un DataFrame para almacenar la muestra
        DataFrame sample = new DataFrame();

        // Generar una lista de índices aleatorios únicos
        ArrayList<Integer> randomIndices = generateRandomIndices(numRows, numSampleRows);

        // Agregar las columnas al DataFrame de la muestra
        for (int i = 0; i < df.getCantColumnas(); i++) {
            // Crear una columna para la muestra
            Column sampleColumn = new Column();
            ArrayList<Cell> cells = new ArrayList<Cell>();  // Create a new list of cells for each column

            // Agregar las celdas correspondientes a la muestra
            for (int rowIndex : randomIndices) {
                Cell cell = df.getCell(i, rowIndex);
                // Guardo las celdas en la list for this column
                cells.add(cell);
            }

            // Agregar the cells to the column
            sampleColumn.setContent(cells);

            // Agregar la columna al DataFrame de la muestra
            sample.addColumn(sampleColumn);
        }

        return sample;
    }

    // Método para generar una lista de índices aleatorios únicos
    private ArrayList<Integer> generateRandomIndices(int maxIndex, int numIndices) {
        ArrayList<Integer> randomIndices = new ArrayList<>();
        Random random = new Random();

        // Asegurarse de no generar índices duplicados
        while (randomIndices.size() < numIndices) {
            int randomIndex = random.nextInt(maxIndex);
            if (!randomIndices.contains(randomIndex)) {
                randomIndices.add(randomIndex);
            }
        }

        return randomIndices;
    }
}
