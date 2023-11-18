package utils_df;

import java.util.HashMap;

import java.util.Map;
import java.util.function.Predicate;

import dataframe.cells.Cell;


public class Criterios {
    public Map<String, Predicate<Cell>> operadores;
    public Criterios(Cell valor){
        operadores = new HashMap<>();
        operadores.put("<", e -> e.compareTo(valor) < 0);
        operadores.put(">", e -> e.compareTo(valor) > 0);
        operadores.put("==", e -> e.compareTo(valor) == 0);
        operadores.put("!" , e-> e.compareTo(valor) != 0 );
        operadores.put("<=", e -> e.compareTo(valor) <= 0);
        operadores.put(">=", e -> e.compareTo(valor) >= 0);
    } 
}
