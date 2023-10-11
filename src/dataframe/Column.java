package dataframe;

public class Column {
    private String label;
    private int order;
    private int size;
    private dType dtype;
    private Object[] data;

 
    // "size" debería surgir del tamaño del csv e inyectarse desde el reader
    // lo mismo "name"
    // order lo mismo, debería surgir del orden de las columnas del csv pero se puede modificar con el Setter
    // dtype debería surgir del tipo de dato de la columna del csv e inyectarse desde un paso intermedio al reader
    // algo como un "typechecker"?
    // el "dtype" se podría usar para generar un array de objetos del tipo correspondiente? serí útil?
    
    public Column(String label, int size, int order, dType dtype, Object[] data) {
        this.label = label;
        this.size = size;
        this.order = order;
        this.dtype = dtype;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getOrder() {
            return order;
        }

    public void setOrder(int order) {
        this.order = order;
    }

    public dType getDtype() {
            return dtype;
        }

    public void setDtype(dType dtype) {
        // implicaría intentar cambiar el tipo de dato de la columna.
        //  Necesita una utilidad auxiliar que realice esta acción y esto sólo llame al método

        // this.dtype = dtype;
    }


}
