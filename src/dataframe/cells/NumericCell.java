package dataframe.cells;

public class NumericCell extends Cell{
    private Number value;

    public NumericCell(Number value) {
        super();
        setValue(value);
    }

    public NumericCell(NumericCell original){
        this(original.getValue());
     }

   
    @Override //esto no anda, no le den bola
    public NumericCell copy(){
        return new NumericCell(this);
    } 

    @Override
    public Number getValue() {
        return value;
    }
    
    @Override
    public void setValue(Object value) {
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
       if (value.doubleValue() == 0.0){
           return false;
       }
       else{
           return true;
       }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Number asNumber() {
        return this.value;
    }
    
}
