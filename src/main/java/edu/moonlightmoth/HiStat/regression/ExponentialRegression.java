package edu.moonlightmoth.HiStat.regression;

import Jama.LUDecomposition;
import Jama.Matrix;
import edu.moonlightmoth.HiStat.service.BasicStat;

import java.util.Arrays;
import java.util.Objects;

public class ExponentialRegression extends Regression {

    private ExponentialRegression(String independentName,
                            String dependentName,
                            double[] coefficients,
                            int x,
                            int y,
                            BasicStat basicStat)
    {
        super(independentName, dependentName, coefficients, x, y, basicStat,
                value -> coefficients[0]*Math.pow(Math.E, coefficients[1] * basicStat.getSampling()[x][value]));


    }

    static Regression calculate(int power, int x, int y, BasicStat basicStat)
    {
        double sum0 = 0;
        double sum1 = 0;
        double[][] sampling = basicStat.getSampling();
        int m = basicStat.getNumOfMeasurements();
        double[][] v = basicStat.getV();

        for (int i = 0; i < m; i++)
        {

            double lg = sampling[y][i] == 0 ?
                    Math.log(Double.MIN_VALUE) :
                    Math.log(sampling[y][i]);
            sum0  = sum0 + sampling[x][i] * lg;
            sum1 = sum1 + lg;
        }

        double[][] X = new double[][]{{v[1][x], v[0][x]},{v[0][x], m}};
        double[][] Y = new double[][]{{sum0}, {sum1}};

        Matrix B = new LUDecomposition(new Matrix(X)).solve(new Matrix(Y));

        double[] beta = B.getColumnPackedCopy();
        beta[1] = Math.exp(beta[1]);

        return new ExponentialRegression(
                basicStat.getNames()[x],
                basicStat.getNames()[y],
                new double[]{beta[1], beta[0]},
                x,
                y,
                basicStat);
    }

    @Override
    public RegressionType getType()
    {
        return RegressionType.EXPONENTIAL;
    }
}
