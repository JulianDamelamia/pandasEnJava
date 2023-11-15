package dataframe.cells;

public class BooleanCell extends Cell<Boolean>{
    protected Boolean value;

    public BooleanCell() {
        super();
    }   
    
    public BooleanCell(Boolean value) {
        super();
        setValue(value);
    }
    
    public BooleanCell(BooleanCell original){
        this(original.getValue());
     }

    @Override
    public BooleanCell copy(){
        return new BooleanCell(this);
    }

    public void setValue(Boolean value){
        this.value = value;
    }

    public void setValue(Object value) {
        try {
            if (value instanceof Boolean) {
                this.value = (Boolean) value;
            } else {
                throw new Exception("Se intentó asignar " + value.getClass() + " a una celda de tipo Boolean");
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    public Boolean getValue(){
        return this.value;
    }

    
    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public boolean isNA() {
        return false;
    }
    @Override
    public Boolean asBoolean() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Number asNumber() {
       if (value){
           return 1;
       }
       else{
           return 0;
       }
    }

    @Override
    public int compareTo(Cell<Boolean> o) {
        if(o.isBoolean()){
            return this.value.compareTo(((BooleanCell)o).getValue());
        }
        else{
            throw new RuntimeException("No se pueden comparar celdas de distinto tipo");
        }
    }
}
