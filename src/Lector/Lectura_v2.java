package Lector;

import java.io.IOException;
import java.util.Scanner;

//Local packages
import Matriz.Builder.MatrixCreator;

public class Lectura_v2 {

    public static void main(String[] args) throws IOException {
        //Create a scanner asking for the name of the file csv
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el nombre del archivo csv: ");
        String nombre = sc.nextLine();
        
        //Complete the path with the name of the file
        String csvFilePath = "./src/utils/" + nombre+ ".csv";
        //String csvFilePath = "./src/utils/industry_sic.csv";
        sc.close();

        //Create a matrix with the csv file
        String[][] matrix = MatrixCreator.createMatrix(csvFilePath);
        //

        //Print the matrix with LecturaMatrix
        LecturaMatrix.printMatrix(matrix);
    }
}
