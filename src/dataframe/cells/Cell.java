package dataframe.cells;

public abstract class Cell<T extends Comparable<T>> implements Comparable<Cell<T>>{
//pasé a T extends Comparable<T> para que funcione el compareTo

    protected T value;

    public Cell(){
    }

    public Cell(T value){
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public Cell copy(){
        return null;
    };
 

    public abstract void setValue(T value);

    abstract boolean isBoolean();
    abstract boolean isNumeric();
    abstract boolean isString();
    abstract boolean isNA();
    abstract Boolean asBoolean();
    abstract Number asNumber();
}
