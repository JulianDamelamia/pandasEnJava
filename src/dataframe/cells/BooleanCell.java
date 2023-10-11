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
}
