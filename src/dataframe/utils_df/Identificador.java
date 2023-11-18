package dataframe.utils_df;

/**
 * La clase Identificador se utiliza para identificar el tipo de una celda.
 */
public class Identificador {

  // La celda que se va a identificar
  private String celda;

  /**
   * Constructor de la clase Identificador.
   * @param celda La celda que se va a identificar.
   */
  public Identificador(String celda) {
    this.celda = celda;
  }

  public Identificador(Object objCelda) {
    this.celda = objCelda.toString();
  }

  /**
   * Este método se utiliza para obtener el tipo de la celda.
   * @return String El tipo de la celda. Puede ser "STRING", "BOOLEAN", "INTEGER", "FLOAT" o "NA".
   */
  public String getType() {
    return operadorGetType(this.celda);
  }

  public static String getType(String celda) {
    return operadorGetType(celda);
  }

  private static String operadorGetType(String celda) {
        if (celda.matches("[a-zA-Z]+")) {
      if (
        celda.equals("True") ||
        celda.equals("False") ||
        celda.equals("false") ||
        celda.equals("true")
      ) {
        return "BOOLEAN";
      }
      return "STRING";
    }
    if (celda.isEmpty() || celda.trim().isEmpty()) {
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
