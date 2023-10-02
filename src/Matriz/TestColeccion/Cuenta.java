package Matriz.TestColeccion;

import java.util.*;

public class Cuenta {

    public static void main(String[] args){

        /*
         * El CSV ya fue procesado, por lo que cada fila seria un objeto de tipo Persona (en esta prueba)
         */
        Persona p1 = new Persona("Juan", 20, "123456789", 1000);
        Persona p2 = new Persona("Pedro", 30, "987654321", 2000);
        Persona p3 = new Persona("Maria", 40, "456789123", 3000);
        Persona p4 = new Persona("Ana", 50, "321654987", 4000);

        /*Coleccion
        * Set <Persona> personas = new Set<Persona>();
        * Error en tiempo de compilacion, pues Set es una interfaz
        * Set es una interfaz por lo que no se puede isntanciar,
        * por lo que se debe usar una clase que implemente Set (_All Known Implementing Classes_)
        */

        //Solucion usar 
        Set <Persona> personas = new HashSet<Persona>();
        //Agrego las personas
        personas.add(p1);
        personas.add(p2);
        personas.add(p3);
        personas.add(p4);

        //Imprimo las personas
        System.out.println("Personas: ");
        for(Persona p : personas){
            System.out.println(p.getNombre()+" "+p.getEdad()+" "+p.getNumCuenta()+" "+p.getSaldo());
        }
        
        //Guardar el Set en un map
        //Investigar: https://www.geeksforgeeks.org/java-util-hashmap-in-java-with-examples/
        Map <String, Set<Persona>> mapa = new HashMap<String, Set<Persona>>();
        mapa.put("Personas", personas);//Investigar 

        System.out.println("Mapa: ");
               
        for (Map.Entry<String, Set<Persona>> entry : mapa.entrySet()) { //Investigar como funca esto che
            System.out.println(entry.getKey());
            for (Persona persona : entry.getValue()) {
                //Debo iterar otra vez pues sino simplemente itero sobre el Set (dires de memoria)
                System.out.println("  " + persona.getNombre() + " " + persona.getEdad() + " " + persona.getNumCuenta() + " " + persona.getSaldo());
            }
        }
        //Acceder a una persona en especifico solicitado por consola
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el nombre de la persona a buscar: ");
        String nombre = sc.nextLine();
        for (Persona persona : personas) {
            if(persona.getNombre().equals(nombre)){
                System.out.println("Persona encontrada: ");
                System.out.println("  " + persona.getNombre() + " " + persona.getEdad() + " " + persona.getNumCuenta() + " " + persona.getSaldo());
            }
        }
        sc.close();
        



    }
}