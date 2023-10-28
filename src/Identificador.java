
public class Identificador {
    String celda;
    public Identificador(String celda){
        this.celda = celda;
    }

    public String getType(){
        if(celda.matches("[a-zA-Z]+")){
            if(celda.equals("True") || celda.equals("False")){
                return "BOOLEAN";
            }
            return "STRING";
        }if(celda.isEmpty() || celda.trim().isEmpty()){
            return "NA";
        }else{
            return "NUMERIC";
        }
    }
}
