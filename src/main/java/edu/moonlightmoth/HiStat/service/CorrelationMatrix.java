package edu.moonlightmoth.HiStat.service;

import java.util.Arrays;

public class CorrelationMatrix {
    private double[][] correlationMatrix;

    public CorrelationMatrix(BasicStat basicStat)
    {
        calculate(basicStat);
    }

    public void calculate(BasicStat basicStat)
    {
        int n = basicStat.getNumOfVars();
        int m = basicStat.getNumOfMeasurements();
        double[][] sampling = basicStat.getSampling();
        correlationMatrix = new double[n][n];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                int bothNan = 0;
                for (int k = 0; k < m; k++)
                {
                    if (Double.isNaN(sampling[i][k]) && Double.isNaN(sampling[j][k]))
                        bothNan++;
                    else if (!Double.isNaN(sampling[i][k]) && !Double.isNaN(sampling[j][k]))
                        correlationMatrix[i][j] = correlationMatrix[i][j] + sampling[i][k] * sampling[j][k];


                }
                correlationMatrix[i][j] =
                        (correlationMatrix[i][j]/
                        (m - basicStat.getNumOfNaNs()[i] + basicStat.getNumOfNaNs()[j] - bothNan) -
                                basicStat.getAverages()[i] * basicStat.getAverages()[j]) /
                        basicStat.getStandardDeviations()[i] / basicStat.getStandardDeviations()[j];
            }
        }

    }

    public double[][] getCorrelationMatrix()
    {
        return correlationMatrix;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CorrelationMatrix that = (CorrelationMatrix) o;
        return Arrays.equals(correlationMatrix, that.correlationMatrix);
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(correlationMatrix);
    }
}
