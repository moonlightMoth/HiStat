package edu.moonlightmoth.HiStat.service;

import Jama.Matrix;
import Jama.QRDecomposition;

import java.util.Arrays;
import java.util.Objects;

public class RegressionMatrix {

    private double[][] sampling;
    private PolynomialRegression[][][] polynomialRegressions;
    private AbnormalVals abnormalVals;

    public RegressionMatrix(BasicStat basicStat)
    {
        this.sampling = basicStat.getSampling();
        calculate(basicStat);
    }

    private void calculate(BasicStat basicStat)
    {
        int n = basicStat.getNumOfVars();

        polynomialRegressions = new PolynomialRegression[4][n][n];
        abnormalVals = new AbnormalVals();

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < n; j++)
            {
                for (int k = 0; k < n; k++)
                {
                    if (j != k)
                    {
                        polynomialRegressions[i][j][k] = calculateSingle(i + 1, j, k, basicStat);
                    }
                }
            }
        }

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < n; j++)
            {
                for (int k = 0; k < n; k++)
                {
                    if (j != k && polynomialRegressions[i][j][k] != null)
                        abnormalVals.checkSingleXYRegression(polynomialRegressions[i][j][k], basicStat, j, k);
                }
            }
        }

        return;

    }

    private PolynomialRegression calculateSingle(int power, int x, int y, BasicStat basicStat) {
        int m = basicStat.getNumOfMeasurements();
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
            B = new QRDecomposition(XX).solve(YY);
        }
        catch (Exception e) {
            // singular matrix
            return null;
        }
        return new PolynomialRegression(basicStat.getNames()[x],
                basicStat.getNames()[y], Arrays.copyOf(B.getColumnPackedCopy(), 5));
    }

    public double[][] getSampling()
    {
        return sampling;
    }

    public PolynomialRegression[][][] getPolynomialRegressions()
    {
        return polynomialRegressions;
    }

    public AbnormalVals getAbnormalVals()
    {
        return abnormalVals;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegressionMatrix that = (RegressionMatrix) o;
        return Arrays.equals(sampling, that.sampling) && Arrays.equals(polynomialRegressions, that.polynomialRegressions) && Objects.equals(abnormalVals, that.abnormalVals);
    }

    @Override
    public int hashCode()
    {
        int result = Objects.hash(abnormalVals);
        result = 31 * result + Arrays.hashCode(sampling);
        result = 31 * result + Arrays.hashCode(polynomialRegressions);
        return result;
    }
}















