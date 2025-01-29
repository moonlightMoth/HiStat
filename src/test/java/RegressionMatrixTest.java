import Jama.Matrix;
import edu.moonlightmoth.HiStat.service.BasicStat;
import edu.moonlightmoth.HiStat.service.CorrelationMatrix;
import edu.moonlightmoth.HiStat.service.RegressionMatrix;
import org.ejml.simple.SimpleMatrix;
import org.junit.jupiter.api.Test;

public class RegressionMatrixTest {

    double[][] testSampling = new double[][]
            {
                    {60, 11.87, 8.95, 19.73, 7.94, 12.89, 16.51, 15.74, 3.48, 7.94, 2.45, 2.10, 17.63, 11.07, 15.34, 6.81, 19.56, 0.71, 0.01, 0.10,},
                    {10.46, 17.80, 13.36, 12.68, 19.70, 8.53, 5.85, 0.25, 13.12, 9.06, 11.84, 17.03, 17.08, 5.95, 13.88, 10.46, 8.75, 12.62, 3.67, 18.44,},
                    {16.42, 19.69, 15.43, 0.86, 11.47, 4.85, 16.17, 7.84, 18.38, 6.68, 1.94, 18.59, 18.29, 15.97, 8.99, 16.86, 17.30, 18.07, 4.35, 1.57,},
                    {11.62, 10.10, 11.85, 4.55, 8.02, 18.16, 16.31, 1.30, 10.59, 11.38, 11.21, 15.05, 2.44, 11.83, 13.18, 9.82, 14.31, 12.58, 0.71, 3.79,},
                    {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20},
                    {7, 7.19, 7.1, 6.9, 6.4, 6, 5.7, 5.3, 5, 4.7, 4.7, 4.9, 5, 5.5, 6.5, 7.8, 9.5, 11.4, 13.9, 16, 21}
            };
    String[] testNames = {"v0", "v1", "v2", "v3", "v4", "v5"};

    @Test
    void calculateTest()
    {
        SimpleMatrix sm = new SimpleMatrix(new double[][]{{1000.0,3000.0,9000.0},{3000.0,9000.0,81000000.0}, {9000.0, 81000000.0 ,729000000000.0}});
        SimpleMatrix ym = new SimpleMatrix(new double[][]{{7}, {8}, {9}});
        System.out.println((((sm.transpose().mult(sm)).invert()).mult(sm.transpose())).mult(ym));

        SimpleMatrix step = sm.transpose().mult(sm);
        double det = step.determinant();
        step = step.transpose();
        step = step.divide(det);

        System.out.println(step.mult(sm.transpose()).mult(ym));


        BasicStat basicStat = new BasicStat(testSampling, testNames);
        RegressionMatrix regressionMatrix = new RegressionMatrix(basicStat);

    }

}
