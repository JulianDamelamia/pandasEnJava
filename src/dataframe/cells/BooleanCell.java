package dataframe.cells;

public class BooleanCell extends Cell{
    private Boolean value;

    public BooleanCell(Boolean value) {
        super();
        setValue(value);
    }

    @Override
    void setValue(Object value) {
        try{
            if(value instanceof Boolean){
                this.value = (Boolean) value;
            }
            else{
                throw new Exception("Se intentó asignar" + value.getClass() + "a una celda de tipo Boolean");
            }
        }catch(Exception e){ e.getMessage();}    
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
}
