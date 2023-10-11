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
    
    
}
