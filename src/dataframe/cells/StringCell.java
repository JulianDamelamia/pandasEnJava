package dataframe.cells;

public class StringCell extends Cell{
    private String value;

    public StringCell(String value) {
        super();
        setValue(value);
    }

    @Override
    void setValue(Object value) {
        try{
            if(value instanceof String){
                this.value = (String) value;
            }
            else{
                throw new Exception("Se intentó asignar" + value.getClass() + "a una celda de tipo String");
            }
        }catch(Exception e){ e.getMessage();}    
    }
}
