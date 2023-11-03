import dataframe.DataFrame;
import utils_df.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String filePath = "./utils/libro2.csv";
        DataFrame df = Archivos.readCSV(filePath);
        if (df != null) {
            Archivos.show(df);
            //Archivos.exportCSV("test1.csv", df);
        }
    

        //Creo un random sample de df
        RandomSample randomSample = new RandomSample();
        DataFrame dfSample = randomSample.sample(df);
        System.out.println("Random Sample");
        Archivos.show(dfSample);

        // Con df y dfSample los puedo concatenar
        DataFrame dfConcatenado = DataFrameConcatenator.concatenate(df, dfSample);
        System.out.println("Df Concatenado");

        Archivos.show(dfConcatenado);

        // Veo las primeras filas con el metodo head()
        DataFrame dfHead = Selection.head(df);
        System.out.println("Head");
        Archivos.show(dfHead);

        // Veo las ultimas filas con el metodo tail()
        DataFrame dfTail = Selection.tail(df);
        System.out.println("Tail");
        Archivos.show(dfTail);
    }
}