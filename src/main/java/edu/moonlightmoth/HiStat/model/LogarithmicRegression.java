package edu.moonlightmoth.HiStat.model;

import Jama.LUDecomposition;
import Jama.Matrix;
import edu.moonlightmoth.HiStat.service.BasicStat;
import edu.moonlightmoth.HiStat.service.Regression;

import java.util.Arrays;
import java.util.Objects;

public class LogarithmicRegression implements Regression {

    private final String independentName;
    private final String dependentName;
    private final double[] coefficients;
    private final int power;

    private LogarithmicRegression(String independentName, String dependentName, double[] coefficients)
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
        double sumLns = 0;
        double sumLnSquares = 0;
        double sumYLns = 0;

        double[][] sampling = basicStat.getSampling();
        int m = basicStat.getNumOfMeasurements();
        double[][] lnHelper = new double[2][m];

        for (int i = 0; i < m; i++)
        {
            lnHelper[0][i] = Math.log(sampling[x][i]);
            lnHelper[1][i] = lnHelper[0][i] * lnHelper[0][i];
        }

        for (int i = 0; i < m; i++)
        {
            sumLns = sumLns + lnHelper[0][i];
            sumLnSquares = sumLnSquares + lnHelper[1][i];
            sumYLns = sumYLns + sampling[y][i] * lnHelper[0][i];
        }


        double[][] X = new double[][]{{sumLnSquares, sumLns},{sumLns, m}};
        double[][] Y = new double[][]{{sumYLns}, {basicStat.getV()[0][y]}};

        Matrix B = new LUDecomposition(new Matrix(X)).solve(new Matrix(Y));

        double[] beta = B.getColumnPackedCopy();

        return new LogarithmicRegression(basicStat.getNames()[x],
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
        return RegressionType.LOGARITHMIC;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogarithmicRegression that = (LogarithmicRegression) o;
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
