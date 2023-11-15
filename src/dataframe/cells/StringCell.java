package dataframe.cells;

public class StringCell extends Cell<String>{
    protected String value;

    public StringCell(String value) {
        super();
        setValue(value);
    }
    
    public StringCell(StringCell original){
        this(original.getValue());
     }

   
    @Override
    public StringCell copy(){
        return new StringCell(this);
    } 
    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
    public void setValue(Object value) {
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
    public String getValue() {
        return value;
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
    public boolean isNA() {
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

    @Override
    public int compareTo(Cell<String> o) {
        if (o.isString()){
            return this.value.compareTo(((StringCell) o).getValue());
        }
        else{
            throw new RuntimeException("Cannot compare cells of different types");
        }
    }

}
