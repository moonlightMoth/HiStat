package edu.moonlightmoth.HiStat.regression;

import edu.moonlightmoth.HiStat.service.BasicStat;

public class RegressionBuilder {

    public static Regression build(RegressionType regressionType, int x, int y, BasicStat basicStat)
    {
        return switch (regressionType)
        {
            case LINEAR -> PolynomialRegression.calculate(1,x,y,basicStat);
            case QUADRATIC -> PolynomialRegression.calculate(2, x,y,basicStat);
            case CUBIC -> PolynomialRegression.calculate(3,x,y,basicStat);
            case POLYNOMIAL4 -> PolynomialRegression.calculate(4,x,y,basicStat);
            case POWER -> PowerRegression.calculate(0, x, y, basicStat);
            case EXPONENTIAL -> ExponentialRegression.calculate(0,x,y,basicStat);
            case LOGARITHMIC -> LogarithmicRegression.calculate(0,x,y,basicStat);
            default -> throw new RuntimeException("Cannot make UNDEFINED Regression");
        };
    }
}
