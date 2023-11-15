package dataframe.cells;

public class NumericCell extends Cell{
    protected Number value;

    public NumericCell(Number value) {
        super();
        setValue(value);
    }

    public NumericCell(NumericCell original){
        this(original.getValue());
     }

   
    @Override
    public NumericCell copy(){
        return new NumericCell(this);
    } 

    @Override
    public Number getValue() {
        return value;
    }
    

    public void setValue(Number value) {
        this.value = value;
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
    public boolean isNA() {
        return false;
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

    @Override
    public int compareTo(Cell otro)throws RuntimeException{
        if (otro.getClass() != this.getClass()) {
            throw new RuntimeException("No se pueden comparar celdas de distinto tipo");
        } else {
            return this.compareToNumeric((NumericCell) otro);
            }
    };

    public int compareToNumeric(NumericCell otro){
        Double valor1 = this.value.doubleValue();
        Double valor2 = otro.getValue().doubleValue();
       return valor1.compareTo(valor2);
    };
}
