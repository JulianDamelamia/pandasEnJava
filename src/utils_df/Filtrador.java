package utils_df;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
interface Validador<T> {
    boolean validar(T elemento);
}

public class Filtrador {

    Validador<Integer> esPositivo = num -> num > 0;
    Validador<String> tieneMinCaracteres = str -> str.length() >= 5;

    public <T> List<T> filtrar(List<T> elementos, Validador<T> criterio) {
        List<T> filtrados = new ArrayList<>();
        for (T elemento : elementos) {
            if (criterio.validar(elemento)) {
                filtrados.add(elemento);
            }
        }
        return filtrados;
    }

}
