package dataframe.cells;

public abstract class Cell {
    private Object value;

    public Cell(){
    }

    public Cell(Object value){
        this.value = value;
    }


    public Object getValue() {
        return value;
    }

    abstract void setValue(Object value);
    abstract boolean isBoolean();
    abstract boolean isNumeric();
    abstract boolean isString();
    abstract Boolean asBoolean();
    abstract Number asNumber();

}
