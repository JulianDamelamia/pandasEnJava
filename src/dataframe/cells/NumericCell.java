package dataframe.cells;

public class NumericCell<N extends Number & Comparable<N>> extends Cell<N> {
    protected N value;

    public NumericCell(N value) {
        super();
        setValue(value);
    }

    public NumericCell(NumericCell<N> original){
        this(original.getValue());
     }

   
    @Override
    public NumericCell<N> copy(){
        return new NumericCell<>(this);
    } 

    @Override
    public N getValue() {
        return value;
    }
    
    @Override
    public void setValue(N value) {
        this.value = value;
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
    public int compareTo(Cell<N> otro)throws RuntimeException{
        if (otro.getClass() != this.getClass()) {
            throw new RuntimeException("No se pueden comparar celdas de distinto tipo");
        } else {
            return this.compareToNumeric((NumericCell<N>) otro);
            }
    };

    public int compareToNumeric(NumericCell<N> otro){
        Double valor1 = this.value.doubleValue();
        Double valor2 = otro.getValue().doubleValue();
       return valor1.compareTo(valor2);
    };
}
