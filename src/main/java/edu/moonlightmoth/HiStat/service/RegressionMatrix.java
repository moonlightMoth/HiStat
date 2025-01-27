package edu.moonlightmoth.HiStat.service;

import edu.moonlightmoth.HiStat.model.ExponentialRegression;
import edu.moonlightmoth.HiStat.model.LogarithmicRegression;
import edu.moonlightmoth.HiStat.model.PolynomialRegression;
import edu.moonlightmoth.HiStat.model.PowerRegression;

import java.util.Arrays;
import java.util.Objects;

public class RegressionMatrix {

    private double[][] sampling;
    private Regression[][][] regressions;
    private AbnormalVals abnormalVals;

    public RegressionMatrix(BasicStat basicStat)
    {
        this.sampling = basicStat.getSampling();
        calculate(basicStat);
    }

    private void calculate(BasicStat basicStat)
    {
        int n = basicStat.getNumOfVars();

        regressions = new Regression[7][n][n];
        abnormalVals = new AbnormalVals();

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < n; j++)
            {
                for (int k = 0; k < n; k++)
                {
                    if (j != k)
                    {
                        regressions[i][j][k] = PolynomialRegression.calculate(i+1, j, k, basicStat);
                    }
                }
            }
        }


        for (int j = 0; j < n; j++)
        {
            for (int k = 0; k < n; k++)
            {
                if (j != k)
                {
                    regressions[4][j][k] = LogarithmicRegression.calculate(0,j, k, basicStat);
                    regressions[5][j][k] = PowerRegression.calculate(0,j, k, basicStat);
                    regressions[6][j][k] = ExponentialRegression.calculate(0,j, k, basicStat);
                }
            }
        }


//        for (int i = 0; i < 4; i++)
//        {
//            for (int j = 0; j < n; j++)
//            {
//                for (int k = 0; k < n; k++)
//                {
//                    if (j != k && regressions[i][j][k] != null)
//                        abnormalVals.checkSingleXYRegression(regressions[i][j][k], basicStat, j, k);
//                }
//            }
//        }


    }

    public double[][] getSampling()
    {
        return sampling;
    }

    public Regression[][][] getRegressions()
    {
        return regressions;
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
        return Arrays.equals(sampling, that.sampling) && Arrays.equals(regressions, that.regressions) && Objects.equals(abnormalVals, that.abnormalVals);
    }

    @Override
    public int hashCode()
    {
        int result = Objects.hash(abnormalVals);
        result = 31 * result + Arrays.hashCode(sampling);
        result = 31 * result + Arrays.hashCode(regressions);
        return result;
    }
}















