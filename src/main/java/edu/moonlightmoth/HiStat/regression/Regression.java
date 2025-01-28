package edu.moonlightmoth.HiStat.regression;

import edu.moonlightmoth.HiStat.service.BasicStat;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntToDoubleFunction;
import java.util.function.ToDoubleBiFunction;

public abstract class Regression {

    protected double determinationCoefficient;
    protected String independentName;
    protected String dependentName;
    protected double[] coefficients;
    protected int x;
    protected int y;

    protected Regression(String independentName,
                         String dependentName,
                         double[] coefficients,
                         int x,
                         int y,
                         BasicStat basicStat,
                         IntToDoubleFunction function)
    {

        this.dependentName = dependentName;
        this.independentName = independentName;
        this.coefficients = Arrays.copyOf(coefficients,5);
        this.x = x;
        this.y = y;

        determinationCoefficient = calculateDeterminationCoefficient(basicStat, function);

    }

    public String getIndependentName()
    {
        return independentName;
    }

    public String getDependentName()
    {
        return dependentName;
    }

    public double[] getCoefficients()
    {
        return coefficients;
    }

    public double getDeterminationCoefficient()
    {
        return determinationCoefficient;
    }

    private double calculateDeterminationCoefficient(BasicStat basicStat,
                                                       IntToDoubleFunction function)
    {
        double[][] sampling = basicStat.getSampling();
        int m = basicStat.getNumOfMeasurements();
        double upper = 0;
        double downer = m * basicStat.getDispersions()[y];

        for (int i = 0; i < m; i++) {
            upper = upper + Math.pow(sampling[y][i] - function.applyAsDouble(i),2);
        }

        return 1 - (upper/downer);
    }



    public abstract RegressionType getType();

    static Regression calculate(int power, int x, int y, BasicStat basicStat)
    {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Regression that = (Regression) o;
        return Double.compare(that.determinationCoefficient, determinationCoefficient) == 0 && x == that.x && y == that.y && Objects.equals(independentName, that.independentName) && Objects.equals(dependentName, that.dependentName) && Arrays.equals(coefficients, that.coefficients);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(determinationCoefficient, independentName, dependentName, x, y);
        result = 31 * result + Arrays.hashCode(coefficients);
        return result;
    }
}
