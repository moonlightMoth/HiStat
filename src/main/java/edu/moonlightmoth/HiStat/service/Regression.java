package edu.moonlightmoth.HiStat.service;

public interface Regression {

    enum RegressionType
    {
        POLYNOMIAL, LOGARITHMIC, POWER, EXPONENTIAL
    }

    String getIndependentName();

    String getDependentName();

    double[] getCoefficients();

    RegressionType getType();

    static Regression calculate(int power, int x, int y, BasicStat basicStat)
    {
        throw new RuntimeException("NOT IMPLEMENTED");
    }
}
