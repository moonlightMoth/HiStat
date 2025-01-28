package edu.moonlightmoth.HiStat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.moonlightmoth.HiStat.regression.PolynomialRegression;
import edu.moonlightmoth.HiStat.regression.Regression;

import java.util.List;
import java.util.Objects;

public class Report {

    private BasicStat basicStat;
    private CorrelationMatrix correlationMatrix;
    private RegressionMatrix regressionMatrix;

    public Report(double[][] sampling, String[] names)
    {
        basicStat = new BasicStat(sampling, names);
        correlationMatrix = new CorrelationMatrix(basicStat);
        regressionMatrix = new RegressionMatrix(basicStat);
    }

    public String toJsonString()
    {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode corr = mapper.createArrayNode();
        double[][] corrMatrix = correlationMatrix.getCorrelationMatrix();

        for (int i = 0; i < corrMatrix.length; i++)
        {
            ArrayNode n = mapper.createArrayNode();

            for (int j = 0; j < corrMatrix[0].length; j++)
            {
                n.add(corrMatrix[i][j]);
            }

            corr.add(n);
        }

        root.set("corr", corr);

        ObjectNode basic = mapper.createObjectNode();
        ArrayNode stdev = mapper.createArrayNode();
        ArrayNode var = mapper.createArrayNode();
        double[] stdevM = basicStat.getStandardDeviations();
        double[] varM = basicStat.getDispersions();

        for (int i = 0; i < varM.length; i++)
        {
            stdev.add(stdevM[i]);
            var.add(varM[i]);
        }

        basic.set("stdev", stdev);
        basic.set("var", var);
        root.set("basic", basic);


        ObjectNode sampling = mapper.createObjectNode();
        ArrayNode names = mapper.createArrayNode();
        String[] namesM = basicStat.getNames();
        double[][] samplingM = basicStat.getSampling();

        for (int i = 0; i < samplingM.length; i++)
        {
            ArrayNode n = mapper.createArrayNode();
            for (int j = 0; j < samplingM[0].length; j++)
            {
                n.add(samplingM[i][j]);
            }
            sampling.set(namesM[i], n);
            names.add(namesM[i]);
        }

        sampling.set("names", names);
        root.set("sampling", sampling);

        ObjectNode regression = mapper.createObjectNode();
        Regression[][][] regressionM = regressionMatrix.getRegressions();


        for (int i = 0; i < basicStat.getNumOfVars(); i++)
        {
            ObjectNode nameIndependent = mapper.createObjectNode();
            for (int j = 0; j < basicStat.getNumOfVars(); j++)
            {
                if (i!=j)
                {
                    ObjectNode dependentName = mapper.createObjectNode();
                    for (int k = 0; k < 7; k++)
                    {
                        Regression reg = regressionM[k][i][j];
                        if (reg == null)
                            continue;

                        ObjectNode type = mapper.createObjectNode();
                        ArrayNode coefs = mapper.createArrayNode();
                        ArrayNode anomsX = mapper.createArrayNode();
                        ArrayNode anomsY = mapper.createArrayNode();

                        for (int l = 0; l < 5; l++)
                        {
                            coefs.add(reg.getCoefficients()[l]);
                        }

                        List<Integer> a = regressionMatrix.getAbnormalVals().getxAnomaly().get(reg);
                        if (a != null)
                            a.forEach(anomsX::add);

                        List<Integer> b = regressionMatrix.getAbnormalVals().getyAnomaly().get(reg);
                        if (b != null)
                            b.forEach(anomsY::add);

                        type.set("coefs", coefs);
                        type.set("anomsx", anomsX);
                        type.set("anomsy", anomsY);
                        type.put("det", reg.getDeterminationCoefficient());

                        switch (reg.getType())
                        {
                            case LINEAR -> dependentName.set("1", type);
                            case QUADRATIC -> dependentName.set("2", type);
                            case CUBIC -> dependentName.set("3", type);
                            case POLYNOMIAL4 -> dependentName.set("4", type);
                            case POWER -> dependentName.set("pow", type);
                            case EXPONENTIAL -> dependentName.set("exp", type);
                            case LOGARITHMIC -> dependentName.set("log", type);
                            case UNDEFINED -> throw new RuntimeException("Cannot convert UNDEFINED to JSON");
                        }


                    }
                    nameIndependent.set(namesM[j], dependentName);
                }
            }
            regression.set(namesM[i], nameIndependent);
        }

        root.set("regression", regression);

        return root.toPrettyString();
    }

    public BasicStat getBasicStat()
    {
        return basicStat;
    }

    public CorrelationMatrix getCorrelationMatrix()
    {
        return correlationMatrix;
    }

    public RegressionMatrix getRegressionMatrix()
    {
        return regressionMatrix;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(basicStat, report.basicStat) && Objects.equals(correlationMatrix, report.correlationMatrix) && Objects.equals(regressionMatrix, report.regressionMatrix);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(basicStat, correlationMatrix, regressionMatrix);
    }
}
