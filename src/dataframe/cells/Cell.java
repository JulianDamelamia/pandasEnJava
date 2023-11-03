package dataframe.cells;

public abstract class Cell{
    private Object value;

    public Cell(){
    }

    public Cell(Object value){
        this.value = value;
    }
    public Object getValue() {
        return value;
    }

    public void setValue(Object value){
        this.value = value;};

    public Cell copy(){
        return null;
    };
    abstract boolean isBoolean();
    abstract boolean isNumeric();
    abstract boolean isString();
    abstract Boolean asBoolean();
    abstract Number asNumber();
}
