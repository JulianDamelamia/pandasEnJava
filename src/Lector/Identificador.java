package Lector;

public class Identificador {
    
    public static String inferDType(String value) {
        // TO DO: change the return value to the right data type 
        /*
         * Evaluar como retornanr como integerar lo que hbalamos hoy
         */

        // Comprobar si es numérico
        if (isNumeric(value)) {
            return "Numeric";
        }
        
        // Comprobar si es booleano
        if (isBoolean(value)) {
            return "Boolean";
        }
        
        // Si no se cumple ninguna regla, considerar como cadena
        return "String";
    }

    private static boolean isNumeric(String value) {
        return value.matches("-?\\d+(\\.\\d+)?"); // Acepta números enteros y decimales
    }

    private static boolean isBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    public static void main(String[] args) {
        String sampleValue = "2021";
        String sampleValue2 = "Hola";
        String sampleValue3 = "true";
       
        String dataType = inferDType(sampleValue);
        System.out.println("Tipo de dato inferido: " + dataType);
     
        String dataType2 = inferDType(sampleValue2);
        System.out.println("Tipo de dato inferido: " + dataType2);
     
        String dataType3 = inferDType(sampleValue3);
        System.out.println("Tipo de dato inferido: " + dataType3);
    }
}
