# Documentación

La estructura general para documentar codigo en Java al inicio.

``` java
/**
 * Doc comment
 * /
```

## Estructura of JavaDoc
- **La documentacion va antes que el código que se documenta**
- El primer parrafo suele contener una descripcion general
- Hay tags que clasifican la descripción
``` java
/**
 * @param ... parameters of the method
 * @return ... return value of a method 
 * @throws ... exceptions the method may throw
 * @author ... who wrote the class/interface
 * @
 */
```
## Why to use it?
- Minimiza el esfuerzo de crear documentacion.
- Hace que sea facil tener la documentacion actualizada.
- Se usa en todos lados
- _Permite incluir hipervinculos a documentos relacionados_ 


### Ejemplos
``` java

/**
 * Some text describing MyClass
 * 
 * @author Pepe
 * @version 6.0 Build 9000 Jan 7, 2003
 */

public class Myclass{
    //Code
}
```

``` java
/**
 * Short one line description-
 * 
 * If there is a longer desription, this is where it would go
 * 
 * @param describes of the parameter
 * @return description of the return type
 */

public int myMethod(String word){
    ...
}
```
### Profundizando conceptos
``` java
package javaDoc;

/**
 * Point represents a point in a CArtesian Coordinate Systemn
 * Points are inmutable. They cannot be changed once they have been created
 * @author pepe
 */
public class Point{
    private final int x;
    private final int y;

    /**
     *Construcs and initializes a Point on position (x,y)
     *@param x x coordinate
     *@param y y coordinate 
     */
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the distance between this point and another point that was passed as an argument
     *@param authoother point to calculate th disntace from 
     */
    public double distance(Point other){
        return Math.hypot(x - other.x, y - other.y);
    }

    /**
     * Returns the value of the x coordinate.
     * @return the value of the x coordinate.
     */
    public int getX(){
        return x:
    }

    /**
     * Returns the value of the y coordinate.
     * @return the value of the y coordinate.
     */
    public int getY(){
        return y;
    }

    /**
     * Returns a string representation of the Point 
     * object in the form of "(x,y)".
     * 
     * @return a string representation of the Point object.
     */
    @Override
    public String toString(){
        return "(" + x + "," + y +")";
    }

}
```

Creo una instancia, al crer _Point()_ sale la documentacion. Lo mismo si pongo mi cursor sobre Point, la clase. Etc

``` java
package javaDoc;

public class PointApp{

    public static void main(String[] args){
        Point p1 = new Point(2,3);
        Point p2 = new Point(5,7);
        System.out.println(p1.distance(p2))
    }
}
```

### Tip
Se le puede pedir al chat de copilot hacer la documentación.

## TODO
Ver como se exporta en html deesde vscode




