package Lector;

public class LecturaMatrix {
    
    public static void printMatrix(String[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            System.out.println();
            System.out.println("Fila " + i + ": ");
            //Salto de linea
            
                    for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(" "+matrix[i][j]);
            }
        }
        System.out.println();
    }
}
