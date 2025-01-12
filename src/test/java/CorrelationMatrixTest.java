import edu.moonlightmoth.HiStat.service.BasicStat;
import edu.moonlightmoth.HiStat.service.CorrelationMatrix;
import org.junit.jupiter.api.Test;

public class CorrelationMatrixTest {

    double[][] testSampling = new double[][]
            {
                    {8.08, 11.87, 8.95, 19.73, 7.94, 12.89, 16.51, 15.74, 3.48, 7.94, 2.45, 2.10, 17.63, 11.07, 15.34, 6.81, 19.56, 0.71, 0.01, 0.10,},
                    {10.46, 17.80, 13.36, 12.68, 19.70, 8.53, 5.85, 0.25, 13.12, 9.06, 11.84, 17.03, 17.08, 5.95, 13.88, 10.46, 8.75, 12.62, 3.67, 18.44,},
                    {16.42, 19.69, 15.43, 0.86, 11.47, 4.85, 16.17, 7.84, 18.38, 6.68, 1.94, 18.59, 18.29, 15.97, 8.99, 16.86, 17.30, 18.07, 4.35, 1.57,},
                    {11.62, 10.10, 11.85, 4.55, 8.02, 18.16, 16.31, 1.30, 10.59, 11.38, 11.21, 15.05, 2.44, 11.83, 13.18, 9.82, 14.31, 12.58, 0.71, 3.79,}
            };
    String[] testNames = {"v0", "v1", "v2", "v3"};

    @Test
    void calculateTest()
    {
        BasicStat basicStat = new BasicStat(testSampling, testNames);
        CorrelationMatrix correlationMatrix = new CorrelationMatrix(basicStat);

    }
}
