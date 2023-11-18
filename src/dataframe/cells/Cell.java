package dataframe.cells;

public abstract class Cell implements Comparable<Cell>{
//pasé a T extends Comparable<T> para que funcione el compareTo

    protected Object value;

    public Cell(){
    }

    public Cell(Object value){
        this.value = value;
    }

    protected Object getValue() {
        return value;
    }

    public Cell copy(){
        return null;
    };
 

    public abstract void setValue(Object value);

    abstract boolean isBoolean();
    abstract boolean isNumeric();
    abstract boolean isString();
    abstract boolean isNA();
    abstract Boolean asBoolean();
    abstract Number asNumber();
}
