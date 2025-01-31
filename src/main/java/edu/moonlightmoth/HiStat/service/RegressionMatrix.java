package edu.moonlightmoth.HiStat.service;

import edu.moonlightmoth.HiStat.regression.*;

import java.util.Arrays;
import java.util.Objects;

public class RegressionMatrix {

    private double[][] sampling;
    private Regression[][][] regressions;

    public RegressionMatrix(BasicStat basicStat)
    {
        this.sampling = basicStat.getSampling();
        calculate(basicStat);
    }

    private void calculate(BasicStat basicStat)
    {
        int n = basicStat.getNumOfVars();

        regressions = new Regression[7][n][n];

        for (int j = 0; j < n; j++)
        {
            for (int k = 0; k < n; k++)
            {
                if (j != k)
                {
                    regressions[0][j][k] = RegressionBuilder.
                            build(RegressionType.LINEAR, j, k, basicStat);
                    regressions[1][j][k] = RegressionBuilder.
                            build(RegressionType.QUADRATIC, j, k, basicStat);
                    regressions[2][j][k] = RegressionBuilder.
                            build(RegressionType.CUBIC, j, k, basicStat);
                    regressions[3][j][k] = RegressionBuilder.
                            build(RegressionType.POLYNOMIAL4, j, k, basicStat);
                }
            }
        }


        for (int j = 0; j < n; j++)
        {
            for (int k = 0; k < n; k++)
            {
                if (j != k)
                {
                    regressions[4][j][k] = RegressionBuilder.
                            build(RegressionType.LOGARITHMIC, j,k,basicStat);
                    regressions[5][j][k] = RegressionBuilder.
                            build(RegressionType.POWER, j,k,basicStat);
                    regressions[6][j][k] = RegressionBuilder.
                            build(RegressionType.EXPONENTIAL, j,k,basicStat);
                }
            }
        }
    }

    public double[][] getSampling()
    {
        return sampling;
    }

    public Regression[][][] getRegressions()
    {
        return regressions;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegressionMatrix that = (RegressionMatrix) o;
        return Arrays.equals(sampling, that.sampling) && Arrays.equals(regressions, that.regressions);
    }

    @Override
    public int hashCode()
    {
        int result = Arrays.hashCode(sampling);
        result = 31 * result + Arrays.hashCode(regressions);
        return result;
    }
}















