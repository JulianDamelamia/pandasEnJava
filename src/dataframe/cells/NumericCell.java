package dataframe.cells;

public class NumericCell extends Cell{
    private Number value;

    public NumericCell(Number value) {
        super();
        setValue(value);
    }
    
    @Override
    void setValue(Object value) {
        try{
            if(value instanceof Number){
                this.value = (Number) value;
            }
            else{
                throw new Exception("Se intentó asignar" + value.getClass() + "a una celda de tipo Numeric");
            }
        }catch(Exception e){ e.getMessage();}    
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isNumeric() {
        return true;
    }

    @Override
    public Boolean asBoolean() {
       if (value.doubleValue() == 0){
           return false;
       }
       else{
           return true;
       }
    }

    @Override
    public String asString() {
        return value.toString();
    }

    @Override
    public Number asNumber() {
        return this.value;
    }
    
}
