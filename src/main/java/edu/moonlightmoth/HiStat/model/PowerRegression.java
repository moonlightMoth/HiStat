package edu.moonlightmoth.HiStat.model;

import Jama.LUDecomposition;
import Jama.Matrix;
import edu.moonlightmoth.HiStat.service.BasicStat;
import edu.moonlightmoth.HiStat.service.Regression;

import java.util.Arrays;
import java.util.Objects;

public class PowerRegression implements Regression {

    private final String independentName;
    private final String dependentName;
    private final double[] coefficients;
    private final int power;

    private PowerRegression(String independentName, String dependentName, double[] coefficients)
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
        double sumXYLns = 0;
        double sumYLns = 0;

        double[][] sampling = basicStat.getSampling();
        int m = basicStat.getNumOfMeasurements();
        double[] xLnHelper = new double[m];
        double[] yLnHelper = new double[m];

        for (int i = 0; i < m; i++)
        {
            xLnHelper[i] = Math.log(sampling[x][i]);
            sumLnSquares += xLnHelper[i] * xLnHelper[i];
            sumLns = sumLns + xLnHelper[i];
            yLnHelper[i] = Math.log(sampling[y][i]);
            sumYLns += yLnHelper[i];
            sumXYLns = sumXYLns + yLnHelper[i] * xLnHelper[i];
        }


        double[][] X = new double[][]{{m, sumLns},{sumLns, sumLnSquares}};
        double[][] Y = new double[][]{{sumYLns}, {sumXYLns}};

        Matrix B = new LUDecomposition(new Matrix(X)).solve(new Matrix(Y));

        double[] beta = B.getColumnPackedCopy();
        beta[0] = Math.exp(beta[0]);

        return new PowerRegression(basicStat.getNames()[x],
                basicStat.getNames()[y], new double[]{beta[0], beta[1]});
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
        return RegressionType.POWER;
    }

    public int getPower()
    {
        return power;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerRegression that = (PowerRegression) o;
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
