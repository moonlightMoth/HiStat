package edu.moonlightmoth.HiStat.regression;

import Jama.LUDecomposition;
import Jama.Matrix;
import edu.moonlightmoth.HiStat.service.BasicStat;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntToDoubleFunction;

public class PowerRegression extends Regression {

    private PowerRegression(String independentName,
                            String dependentName,
                            double[] coefficients,
                            int x,
                            int y,
                            BasicStat basicStat)
    {
        super(independentName, dependentName, coefficients, x, y, basicStat,
                value -> coefficients[0]*Math.pow(basicStat.getSampling()[x][value], coefficients[1]));


    }

    static Regression calculate(int power, int x, int y, BasicStat basicStat)
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
            xLnHelper[i] = sampling[x][i] == 0 ?
                    Math.log(Double.MIN_VALUE) :
                    Math.log(sampling[x][i]);
            sumLnSquares += xLnHelper[i] * xLnHelper[i];
            sumLns = sumLns + xLnHelper[i];
            yLnHelper[i] = sampling[y][i] == 0 ?
                    Math.log(Double.MIN_VALUE) :
                    Math.log(sampling[y][i]);
            sumYLns += yLnHelper[i];
            sumXYLns = sumXYLns + yLnHelper[i] * xLnHelper[i];
        }


        double[][] X = new double[][]{{m, sumLns},{sumLns, sumLnSquares}};
        double[][] Y = new double[][]{{sumYLns}, {sumXYLns}};

        Matrix B =  new Matrix(X).inverse().times(new Matrix(Y));

        double[] beta = B.getColumnPackedCopy();
        beta[0] = Math.exp(beta[0]);

        return new PowerRegression(basicStat.getNames()[x],
                basicStat.getNames()[y], new double[]{beta[0], beta[1]}, x, y, basicStat);
    }

    @Override
    public RegressionType getType()
    {
        return RegressionType.POWER;
    }
}
