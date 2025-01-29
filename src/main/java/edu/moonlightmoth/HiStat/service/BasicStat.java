package edu.moonlightmoth.HiStat.service;

import java.util.Arrays;
import java.util.Objects;

public class BasicStat {
    private double[][] sampling;
    private double[][][] m;
    private double[][] v;
    private int[] numOfNaNs;
    private double[] averages;
    private double[] standardDeviations;
    private double[] dispersions;
    private String[] names;
    private int numOfVars;
    private int numOfMeasurements;

    public BasicStat(double[][] sampling, String[] names)
    {
        this.sampling = sampling;
        calculate(sampling, names);
    }

    private void calculate(double[][] sampling, String[] names)
    {
        if (sampling == null || names == null || sampling[0] == null)
            throw new IllegalArgumentException("nulls are not allowed");
        if (names.length != sampling.length)
            throw new IllegalArgumentException("names and sampling must be same length");

        this.sampling = sampling;
        numOfVars = names.length;
        numOfMeasurements = sampling[0].length;

        countNaNs();
        findAverages();
        fillAbsent();
        findStdDeviationsAndDispersions();
        mAndVCalc();

        this.names = Arrays.copyOf(names, names.length);
    }

    private void findStdDeviationsAndDispersions()
    {
        standardDeviations = new double[numOfVars];
        dispersions = new double[numOfVars];

        for (int i = 0; i < numOfVars; i++)
        {
            for (int j = 0; j < numOfMeasurements; j++)
            {
                if (!Double.isNaN(sampling[i][j]))
                    standardDeviations[i] = standardDeviations[i] + Math.pow(sampling[i][j] - averages[i], 2) ;
            }
            dispersions[i] = standardDeviations[i] / (numOfMeasurements-(double)numOfNaNs[i]);
            standardDeviations[i] = Math.sqrt(standardDeviations[i] / (numOfMeasurements-(double)numOfNaNs[i]));
        }
    }

    private void findAverages()
    {
        averages = new double[numOfVars];

        for (int i = 0; i < numOfVars; i++)
        {
            for (int j = 0; j < numOfMeasurements; j++)
            {
                if (!Double.isNaN(sampling[i][j]))
                    averages[i] += sampling[i][j];
            }
            averages[i] = averages[i]/(numOfMeasurements-numOfNaNs[i]);
        }
    }

    private void fillAbsent()
    {
        for (int i = 0; i < numOfVars; i++)
        {
            for (int j = 0; j < numOfMeasurements; j++)
            {
                if (Double.isNaN(sampling[i][j]))
                {
                    sampling[i][j] = averages[i];
                }
            }
        }
    }

    private void countNaNs()
    {
        numOfNaNs = new int[numOfVars];

        for (int i = 0; i < numOfVars; i++)
        {
            for (int j = 0; j < numOfMeasurements; j++)
            {
                if (Double.isNaN(sampling[i][j]))
                    numOfNaNs[i]++;
            }
        }
    }

    private void mAndVCalc()
    {
        m = new double[8][numOfVars][numOfMeasurements];
        v = new double[8][numOfVars];

        for (int i = 0; i < numOfVars; i++)
        {
            for (int j = 0; j < numOfMeasurements; j++)
            {
                m[0][i][j] = sampling[i][j];
                v[0][i] += sampling[i][j];
            }
        }

        for (int i = 1; i < 8; i++)
        {
            for (int j = 0; j < numOfVars; j++)
            {
                for (int k = 0; k < numOfMeasurements; k++)
                {
                    m[i][j][k] = m[i-1][j][k] * m[0][j][k];
                    v[i][j] = v[i][j] + m[i][j][k];
                }
            }
        }
    }

    public double[][][] getM()
    {
        return m;
    }

    public String[] getNames()
    {
        return names;
    }

    public double[][] getV()
    {
        return v;
    }

    public int getNumOfVars()
    {
        return numOfVars;
    }

    public int getNumOfMeasurements()
    {
        return numOfMeasurements;
    }

    public int[] getNumOfNaNs()
    {
        return numOfNaNs;
    }

    public double[] getAverages()
    {
        return averages;
    }

    public double[] getStandardDeviations()
    {
        return standardDeviations;
    }

    public double[] getDispersions()
    {
        return dispersions;
    }

    public double[][] getSampling()
    {
        return sampling;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicStat basicStat = (BasicStat) o;
        return numOfVars == basicStat.numOfVars && numOfMeasurements == basicStat.numOfMeasurements && Arrays.equals(sampling, basicStat.sampling) && Arrays.equals(m, basicStat.m) && Arrays.equals(v, basicStat.v) && Arrays.equals(numOfNaNs, basicStat.numOfNaNs) && Arrays.equals(averages, basicStat.averages) && Arrays.equals(standardDeviations, basicStat.standardDeviations) && Arrays.equals(dispersions, basicStat.dispersions) && Arrays.equals(names, basicStat.names);
    }

    @Override
    public int hashCode()
    {
        int result = Objects.hash(numOfVars, numOfMeasurements);
        result = 31 * result + Arrays.hashCode(sampling);
        result = 31 * result + Arrays.hashCode(m);
        result = 31 * result + Arrays.hashCode(v);
        result = 31 * result + Arrays.hashCode(numOfNaNs);
        result = 31 * result + Arrays.hashCode(averages);
        result = 31 * result + Arrays.hashCode(standardDeviations);
        result = 31 * result + Arrays.hashCode(dispersions);
        result = 31 * result + Arrays.hashCode(names);
        return result;
    }
}
