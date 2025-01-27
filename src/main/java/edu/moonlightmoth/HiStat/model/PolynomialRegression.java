package edu.moonlightmoth.HiStat.model;

import Jama.LUDecomposition;
import Jama.Matrix;
import edu.moonlightmoth.HiStat.service.BasicStat;
import edu.moonlightmoth.HiStat.service.Regression;

import java.util.Arrays;
import java.util.Objects;

public class PolynomialRegression implements Regression {

    private final String independentName;
    private final String dependentName;
    private final double[] coefficients;
    private final int power;

    private PolynomialRegression(String independentName, String dependentName, double[] coefficients)
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

    public static PolynomialRegression calculate(int power, int x, int y, BasicStat basicStat)
    {
        int m = basicStat.getNumOfMeasurements();
        double[][] sampling = basicStat.getSampling();
        double[][] matrixX = new double[power+1][power+1];
        matrixX[0][0] = m;
        for (int i = 0; i < power+1; i++) {
            for (int j = i; j < power+1; j++) {
                if (i == 0 && j == 0)
                    continue;

                matrixX[i][j] = basicStat.getV()[i+j-1][x];
                matrixX[j][i] = basicStat.getV()[i+j-1][x];
            }
        }
        double[][] matrixY = new double[power+1][1];
        matrixY[0][0] = basicStat.getV()[0][y];
        for (int i = 1; i < power + 1; i++) {
            for (int j = 0; j < m; j++) {
                if (!Double.isNaN(sampling[x][j]) && !Double.isNaN(sampling[y][j]))
                    matrixY[i][0] = matrixY[i][0] + Math.pow(sampling[x][j], i) * sampling[y][j];
            }
        }
        Matrix B;
        try {
            Matrix XX = new Matrix(matrixX);
            Matrix YY = new Matrix(matrixY);
            B = new LUDecomposition(XX).solve(YY);
        }
        catch (Exception e) {
            // singular matrix
            return null;
        }
        return new PolynomialRegression(basicStat.getNames()[x],
                basicStat.getNames()[y], Arrays.copyOf(B.getColumnPackedCopy(), 5));
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

    public int getPower()
    {
        return power;
    }

    @Override
    public RegressionType getType()
    {
        return RegressionType.POLYNOMIAL;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolynomialRegression that = (PolynomialRegression) o;
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
