package edu.moonlightmoth.HiStat.service;

import Jama.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AbnormalVals {

    private HashMap<PolynomialRegression, List<Integer>> xAnomaly = new HashMap<>();
    private HashMap<PolynomialRegression, List<Integer>> yAnomaly = new HashMap<>();

    public AbnormalVals()
    {

    }

    public void checkSingleXYRegression(PolynomialRegression polynomialRegression, BasicStat basicStat, int x, int y)
    {
        int n = basicStat.getNumOfVars();
        int m = basicStat.getNumOfMeasurements();
        int pow = polynomialRegression.getPower();
        double avg = (polynomialRegression.getPower() + 1)/ (double) n;
        double[][] xnMatrix = new double[m][pow+1];

        for (int i = 0; i < m; i++) {
            xnMatrix[i][0] = 1.0;
        }

        for (int i = 0; i < m; i++) {
            for (int j = 1; j < pow+1; j++) {
                xnMatrix[i][j] = basicStat.getM()[j-1][x][i];
            }
        }
        Matrix Xn;
        Matrix H;

        try {
            Xn = new Matrix(xnMatrix);
            H = Xn.times(Xn.transpose().times(Xn).inverse()).times(Xn.transpose());
        }
        catch (Exception e) {
            return;
        }

        for (int i = 0; i < H.getRowDimension(); i++) {
            if (Math.abs(H.get(i,i)) >= 1.8 * avg) {
                List<Integer> anoms = xAnomaly.getOrDefault(polynomialRegression, new ArrayList<>());
                anoms.add(i);
                xAnomaly.put(polynomialRegression, anoms);
            }
        }

        double[][] sampling = basicStat.getSampling();
        double[] coefs = polynomialRegression.getCoefficients();
        double[] errors = new double[m];
        double errorSum = 0;

        for (int i = 0; i < m; i++) {
            errors[i] = sampling[y][i]
                    - coefs[0]
                    - coefs[1] * sampling[x][i]
                    - coefs[2] * Math.pow(sampling[x][i], 2)
                    - coefs[3] * Math.pow(sampling[x][i], 3)
                    - coefs[4] * Math.pow(sampling[x][i], 4);
            errorSum += errors[i]*errors[i];
        }

        for (int i = 0; i < errors.length; i++) {
            errors[i] = errors[i] *
                    Math.sqrt((m- pow-2)/((1-H.get(i,i))
                            * errorSum - errors[i]*errors[i]));

            if (Math.abs(errors[i]) > StudentsTable.lookupCriticVal(0.475/m, m-pow-1)) {
                List<Integer> anoms = yAnomaly.getOrDefault(polynomialRegression, new ArrayList<>());
                anoms.add(i);
                yAnomaly.put(polynomialRegression, anoms);
            }
        }
    }

    public HashMap<PolynomialRegression, List<Integer>> getxAnomaly()
    {
        return xAnomaly;
    }

    public HashMap<PolynomialRegression, List<Integer>> getyAnomaly()
    {
        return yAnomaly;
    }
}
