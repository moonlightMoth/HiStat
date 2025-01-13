package edu.moonlightmoth.HiStat.service;

public class StudentsTable {
    private static final double[][] table = {
            {6.31, 12.7, 31.8, 63.7, 318.3, 637},
            {2.92, 4.3,	6.97,9.92,22.33,31.6},
            {2.35, 3.18,4.54,5.84,10.22,12.9},
            {2.13, 2.78,3.75,4.6,7.17,8.61},
            {2.01, 2.57,3.37,4.03,5.89,6.86},
            {1.94, 2.45,3.14,3.71,5.21,5.96},
            {1.89, 2.36,3,3.5,4.79,5.4},
            {1.86, 2.31,2.9,3.36,4.5,5.04},
            {1.83, 2.26,2.82,3.25,4.3,4.78},
            {1.81, 2.23,2.76,3.17,4.14,4.59},
            {1.8, 2.2,2.72,3.11,4.03,4.44},
            {1.78, 2.18,2.68,3.05,3.93,4.32},
            {1.77, 2.16,2.65,3.01,3.85,4.22},
            {1.76, 2.14,2.62,2.98,3.79,4.14},
            {1.75, 2.13,2.6	,2.95,3.73,4.07},
            {1.75, 2.12,2.58,2.92,3.69,4.01},
            {1.74, 2.11,2.57,2.9,3.65,3.96},
            {1.73, 2.1,	2.55,2.88,3.61,3.92},
            {1.73, 2.09,2.54,2.86,3.58,3.88},
            {1.73, 2.09,2.53,2.85,3.55,3.85},
            {1.72, 2.08,2.52,2.83,3.53,3.82},
            {1.72, 2.07,2.51,2.82,3.51,3.79},
            {1.71, 2.07,2.5,2.81,3.49,3.77},
            {1.71, 2.06,2.49,2.8,3.47,3.74},
            {1.71, 2.06,2.49,2.79,3.45,3.72},
            {1.71, 2.06,2.48,2.78,3.44,3.71},
            {1.71, 2.05,2.47,2.77,3.42,3.69},
            {1.7, 2.05,	2.46,2.76,3.4,3.66},
            {1.7, 2.05,	2.46,2.76,3.4,3.66},
            {1.7, 2.04,	2.46,2.75,3.39,3.65},
            {1.68, 2.02,2.42,2.7,3.31,3.55},
            {1.67, 2,2.39,2.66,3.23,3.46},
            {1.66, 1.98,2.36,2.62,3.17,3.37},
            {1.64, 1.96,2.33,2.58,3.09,3.29},
    };


    public static double lookupCriticVal(double significance, int freedomLevel)
    {
        if (freedomLevel < 0)
            throw new IllegalArgumentException("Wrong freedom level. Too few measurements");

        if (significance < 0.0000001)
            throw new IllegalArgumentException("Wrong significance Level. Too few measurements");

        int freedomIdx;

        if (freedomLevel > 30 && freedomLevel <= 40)
            freedomIdx = 30;
        else if (freedomLevel > 40 && freedomLevel <= 60)
            freedomIdx = 31;
        else if (freedomLevel>60 && freedomLevel <= 120)
            freedomIdx = 32;
        else if (freedomLevel > 120)
            freedomIdx = 33;
        else
            freedomIdx = Math.max(freedomLevel - 1, 0);

        int significanceIdx;

        if (significance >= 0.1)
            significanceIdx = 0;
        else if (significance >= 0.05)
            significanceIdx = 1;
        else if (significance >= 0.02)
            significanceIdx = 2;
        else if (significance >= 0.01)
            significanceIdx = 3;
        else if (significance >= 0.0002)
            significanceIdx = 4;
        else
            significanceIdx = 5;

        return table[freedomIdx][significanceIdx];
    }
}
