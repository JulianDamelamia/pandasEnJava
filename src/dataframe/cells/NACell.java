package dataframe.cells;

public class NACell extends Cell{

    protected static final NACell INSTANCE = new NACell();

    public NACell() {
        super();
    }
    public NACell(NACell original){
        this();
     }
    @Override
    public NACell copy(){
        return new NACell(this);
    }

    public static NACell getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isNA() {
        return true;
    }

    @Override
    public String toString() {
        return "NA";
    }
   
    @Override
    public boolean isNumeric() {
        return false;
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
    public Boolean asBoolean() {
        return null;
    }

    @Override
    public Number asNumber() {
        return null;
    }

    public String asString() {
        return null;
    }

    public void setValue(Object value){
        try {
            replaceNA(value);
        } catch (Exception e) {
            e.getMessage();
        }
    }


    public Cell replaceNA(Object value) throws Exception{
        if (value instanceof Boolean){
            return new BooleanCell((Boolean) value);
        }
        else if (value instanceof String){
            return new StringCell((String) value);
        }
        else if (value instanceof Number){
            return new NumericCell(((Number) value).doubleValue());
        }
        else{
            throw new Exception("El tipo de dato " + value.getClass() + " no es soportado");
        }
    }

    @Override
    public int compareTo(Cell otro) {
       if(otro.isNA()){
            return 0;
        }
        else{
            return -1;
        }
    }
}