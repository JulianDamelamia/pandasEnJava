package dataframe.utils_df;

import dataframe.cells.Cell;
import dataframe.cells.NumericCell;
import dataframe.exceptions.ColumnaNoNumericaException;

import java.util.List;

public class Summarise {

    public static float max(List<Cell> cells) {
        float max = Float.NEGATIVE_INFINITY;
        boolean numericCellsFound = false;

        for (Cell cell : cells) {
            if (cell instanceof NumericCell) {
                NumericCell numericCell = (NumericCell) cell;

                if (numericCell.isFloat()) {
                    float cellValue = numericCell.getFloatValue();
                    max = Math.max(max, cellValue);
                    numericCellsFound = true;
                } else {
                    int cellValue = numericCell.getIntValue();
                    max = Math.max(max, cellValue);
                    numericCellsFound = true;
                }
            }
        }

        if (!numericCellsFound) {
            throw new ColumnaNoNumericaException();
        }

        return max;
    }

    public static float min(List<Cell> cells) {
        float min = Float.POSITIVE_INFINITY;
        boolean numericCellsFound = false;

        for (Cell cell : cells) {
            if (cell instanceof NumericCell) {
                NumericCell numericCell = (NumericCell) cell;

                if (numericCell.isFloat()) {
                    float cellValue = numericCell.getFloatValue();
                    min = Math.min(min, cellValue);
                    numericCellsFound = true;
                } else {
                    int cellValue = numericCell.getIntValue();
                    min = Math.min(min, cellValue);
                    numericCellsFound = true;
                }
            }
        }

        if (!numericCellsFound) {
            throw new ColumnaNoNumericaException();
        }

        return min;
    }

    public static float sum(List<Cell> cells) {
        float sum = 0.0f;
        boolean numericCellsFound = false;

        for (Cell cell : cells) {
            if (cell instanceof NumericCell) {
                NumericCell numericCell = (NumericCell) cell;

                if (numericCell.isFloat()) {
                    float cellValue = numericCell.getFloatValue();
                    sum += cellValue;
                    numericCellsFound = true;
                } else {
                    int cellValue = numericCell.getIntValue();
                    sum += cellValue;
                    numericCellsFound = true;
                }
            }
        }

        if (!numericCellsFound) {
            throw new ColumnaNoNumericaException();
        }

        return sum;
    }

    public static float mean(List<Cell> cells) {
        float sum = 0.0f;
        int count = 0;

        for (Cell cell : cells) {
            if (cell instanceof NumericCell) {
                NumericCell numericCell = (NumericCell) cell;

                if (numericCell.isFloat()) {
                    float cellValue = numericCell.getFloatValue();
                    sum += cellValue;
                    count++;
                } else {
                    int cellValue = numericCell.getIntValue();
                    sum += cellValue;
                    count++;
                }
            }
        }

        if (count == 0) {
            throw new ColumnaNoNumericaException();
        }

        return sum / count;
    }

    public static float variance(List<Cell> cells) {
        float mean = mean(cells);
        float temp = 0.0f;
        int count = 0;

        for (Cell cell : cells) {
            if (cell instanceof NumericCell) {
                NumericCell numericCell = (NumericCell) cell;

                if (numericCell.isFloat()) {
                    float cellValue = numericCell.getFloatValue();
                    temp += (cellValue - mean) * (cellValue - mean);
                    count++;
                } else {
                    int cellValue = numericCell.getIntValue();
                    temp += (cellValue - mean) * (cellValue - mean);
                    count++;
                }
            }
        }

        if (count == 0) {
            throw new ColumnaNoNumericaException();
        }

        return temp / count;
    }

    public static float standardDeviation(List<Cell> cells) {
        return (float) Math.sqrt(variance(cells));
    }

    // Otras funciones similares para mean, variance, standard deviation, etc.
}