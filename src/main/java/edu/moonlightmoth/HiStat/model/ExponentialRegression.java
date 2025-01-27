package edu.moonlightmoth.HiStat.model;

import Jama.LUDecomposition;
import Jama.Matrix;
import edu.moonlightmoth.HiStat.service.BasicStat;
import edu.moonlightmoth.HiStat.service.Regression;

import java.util.Arrays;
import java.util.Objects;

public class ExponentialRegression implements Regression {

    private final String independentName;
    private final String dependentName;
    private final double[] coefficients;
    private final int power;

    ExponentialRegression(String independentName, String dependentName, double[] coefficients)
    {
        this.dependentName = dependentName;
        this.independentName = independentName;
        this.coefficients = Arrays.copyOf(coefficients,5);

        int k = 0;
        for (int i = 0; i < coefficients.length; i++)
        {
            if (coefficients[i] == 0)
                break;
            k++;
        }
        power = k;
    }

    public static Regression calculate(int power, int x, int y, BasicStat basicStat)
    {
        double sum0 = 0;
        double sum1 = 0;
        double[][] sampling = basicStat.getSampling();
        int m = basicStat.getNumOfMeasurements();
        double[][] v = basicStat.getV();

        for (int i = 0; i < m; i++)
        {
            double lg = Math.log(sampling[y][i]);
            sum0  = sum0 + sampling[x][i] * lg;
            sum1 = sum1 + lg;
        }

        double[][] X = new double[][]{{v[1][x], v[0][x]},{v[0][x], m}};
        double[][] Y = new double[][]{{sum0}, {sum1}};

        Matrix B = new LUDecomposition(new Matrix(X)).solve(new Matrix(Y));

        double[] beta = B.getColumnPackedCopy();
        beta[0] = Math.exp(beta[0]);

        return new ExponentialRegression(basicStat.getNames()[x],
                basicStat.getNames()[y], new double[]{beta[1], beta[0]});
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

    @Override
    public RegressionType getType()
    {
        return RegressionType.EXPONENTIAL;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExponentialRegression that = (ExponentialRegression) o;
        return Objects.equals(independentName, that.independentName) && Objects.equals(dependentName, that.dependentName) && Arrays.equals(coefficients, that.coefficients);
    }

    @Override
    public int hashCode()
    {
        int result = Objects.hash(independentName, dependentName);
        result = 31 * result + Arrays.hashCode(coefficients);
        return result;
    }
}
