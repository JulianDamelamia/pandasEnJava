package Matriz.TestColeccion;

public class Persona {
    
    private String nombre;
    private int edad;
    private String numCuenta;
    private double saldo;

    public Persona(String nombre, int edad, String numCuenta, double saldo) {
        this.nombre = nombre;
        this.edad = edad;
        this.numCuenta = numCuenta;
        this.saldo = saldo;
    }


    // Getters
    public String getNombre() {
        return nombre;
    }
    public int getEdad() {
        return edad;
    }
    public String getNumCuenta() {
        return numCuenta;
    }
    public double getSaldo() {
        return saldo;
    }
    //Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    
}
