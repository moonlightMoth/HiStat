package edu.moonlightmoth.HiStat.regression;

import Jama.LUDecomposition;
import Jama.Matrix;
import edu.moonlightmoth.HiStat.service.BasicStat;

import java.util.Arrays;
import java.util.Objects;

public class LogarithmicRegression extends Regression {

    private LogarithmicRegression(String independentName,
                                  String dependentName,
                                  double[] coefficients,
                                  int x,
                                  int y,
                                  BasicStat basicStat)
    {
        super(independentName, dependentName, coefficients, x, y, basicStat,
                value -> coefficients[0] + coefficients[1] * Math.log(basicStat.getSampling()[x][value]));
    }


    static Regression calculate(int power, int x, int y, BasicStat basicStat)
    {
        double sumLns = 0;
        double sumLnSquares = 0;
        double sumYLns = 0;

        double[][] sampling = basicStat.getSampling();
        int m = basicStat.getNumOfMeasurements();
        double[][] lnHelper = new double[2][m];

        for (int i = 0; i < m; i++)
        {
            lnHelper[0][i] = sampling[x][i] == 0 ?
                    Math.log(Double.MIN_VALUE) :
                    Math.log(sampling[x][i]);
            lnHelper[1][i] = lnHelper[0][i] * lnHelper[0][i];

            sumLns = sumLns + lnHelper[0][i];
            sumLnSquares = sumLnSquares + lnHelper[1][i];
            sumYLns = sumYLns + sampling[y][i] * lnHelper[0][i];
        }

        double[][] X = new double[][]{{sumLnSquares, sumLns},{sumLns, m}};
        double[][] Y = new double[][]{{sumYLns}, {basicStat.getV()[0][y]}};

        Matrix B = new LUDecomposition(new Matrix(X)).solve(new Matrix(Y));

        double[] beta = B.getColumnPackedCopy();

        return new LogarithmicRegression(
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
        return RegressionType.LOGARITHMIC;
    }

}
