package src;

public class Identificador {
    String celda;
    public Identificador(String celda){
        this.celda = celda;
    }
    public String getType(){
        if(celda.matches("[a-zA-Z]+")){
            if(celda.equals("True") ||  celda.equals("False") || celda.equals("false") || celda.equals("true")){
                return "BOOLEAN";
            }
            return "STRING";
        }
        if(celda.isEmpty() || celda.trim().isEmpty()){
            return "NA";
        } else {
            try {
                Integer.parseInt(celda);
                return "INTEGER";
            } catch (NumberFormatException e1) {
                try {
                    Float.parseFloat(celda);
                    return "FLOAT";
                } catch (NumberFormatException e2) {
                    return "STRING"; // Si no se puede convertir a int o float, se considera una cadena
                }
            }
        }
    }
}
