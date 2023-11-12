package utils_df;



@FunctionalInterface
interface Validador<T> {
    boolean validar(T elemento);
}

public class Validadores {
    public static <T extends Comparable<T>> Validador<T> mayorQue(Object valor) {
        return elemento -> elemento.compareTo((T) valor) > 0;
    }

    public static <T extends Comparable<T>> Validador<T> menorQue(Object valor) {
        return elemento -> elemento.compareTo((T) valor) < 0;
    }

    public static <T extends Comparable<T>> Validador<T> igualA(Object valor) {
        return elemento -> elemento.equals(valor);
    }
    //public static <T extends Comparable<T>> Validador<T> mayorQue(T valor) {
    //    return elemento -> elemento.compareTo(valor) > 0;
    //}
//
    //public static <T extends Comparable<T>> Validador<T> menorQue(T valor) {
    //    return elemento -> elemento.compareTo(valor) < 0;
    //}
//
    //public static <T extends Comparable<T>> Validador<T> igualA(T valor) {
    //    return elemento -> elemento.equals(valor);
    //}

    public static <T extends Comparable<T>> Validador<T> and(Validador<T> validador1, Validador<T> validador2) {
        return elemento -> validador1.validar(elemento) && validador2.validar(elemento);
    }

    public static <T extends Comparable<T>> Validador<T> or(Validador<T> validador1, Validador<T> validador2) {
        return elemento -> validador1.validar(elemento) || validador2.validar(elemento);
    }

    public static <T extends Comparable<T>> Validador<T> not(Validador<T> validador) {
        return elemento -> !validador.validar(elemento);
    }
}

