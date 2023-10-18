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

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public Boolean asBoolean() {
       if (value.isEmpty()){
           return false;
       }
       else{
           return true;
       }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Double asNumber() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot convert String to Number: " + value);
        }
    }
}
