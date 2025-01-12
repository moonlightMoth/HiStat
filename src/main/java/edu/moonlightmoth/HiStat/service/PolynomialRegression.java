package edu.moonlightmoth.HiStat.service;

import java.util.Arrays;
import java.util.Objects;

public class PolynomialRegression {

    private final String independentName;
    private final String dependentName;
    private final double[] coefficients;
    private final int power;

    PolynomialRegression(String independentName, String dependentName, double[] coefficients)
    {
        if (coefficients.length != 5)
            throw new IllegalArgumentException("must be 5 coefficients");

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
