package com.example.project;
import java.util.Arrays;
public class Routh {
    private double[][] answer;
    private String stability;
    public Routh(double[][] answer, String stability) {
        this.answer = answer;
        this.stability = stability;
    }
    public Routh() {
        stability = "";
    }
    public double[][] getAnswer() {
        return answer;
    }
    public void setAnswer(double[][] answer) {
        this.answer = answer;
    }
    public String getStability() {
        return stability;
    }
    public void setStability(String stability) {
        this.stability = stability;
    }
    private boolean signdoesntchange(double[][] routh) {
        int n = routh.length;
        if (n < 2) return true;
        double sign = Math.signum(routh[0][0]);
        for (int i = 1; i < n; i++) {
            if (Math.abs(routh[i][0]) < 1e-10 || Math.signum(routh[i][0]) != sign) {
                stability = "unstable";
                return false;
            }
        }
        stability = "stable"; 
        return true;
    }
    private boolean coffcheck(double[] coeffients) {
        int n = coeffients.length;
        double sign = Math.signum(coeffients[0]);
        if (Math.abs(sign) < 1e-5) { 
            stability = "unstable"; 
            return false;
        }
        for (int i = 1; i < n; i++) {
            if (Math.signum(coeffients[i]) != sign) {
                stability = "unstable";
                return false;
            }
        }
        stability = "stable"; 
        return true;
    }
    private double[][] calculate(double[] coeffients) {
        int n =coeffients.length; //no rows
        int m =(n+1)/2; //no columns
        double[][] routh = new double[n][m];
        for (int j=0,i=0;j<m&&i<n;i+=2,j++) {
            routh[0][j] = coeffients[i];
        }
        for (int j=0,i=1;j<m&&i<n;i+=2,j++) {
            routh[1][j] = coeffients[i];
        }
        for (int i = 2; i < n; i++) {
            for (int l = 0; l < m - 1; l++) {
                // 1 3 5
                // 2 4 0
                // 1 5
                //-6
                // 5
                double a = routh[i - 1][0]; // 2
                double b = routh[i - 2][0]; // 1
                double c = routh[i - 1][l + 1]; // 4
                double d = routh[i - 2][l + 1]; // 3
                if (Math.abs(a) < 1e-10) {
                    a = 1e-5;
                }
                routh[i][l]=((a*d)-(b*c))/a;
            }
        }
        return routh;
    }
    public Routh stable(double[] coeffients) {
        stability = "";
        int n = coeffients.length;
        if (!coffcheck(coeffients)) {
            return new Routh(new double[n][(n + 1) / 2], this.stability);
        }
        double[][] routh = calculate(coeffients);
        signdoesntchange(routh);
        return new Routh(routh, this.stability);
    }


    //test
    public static void main(String[] args) {
        Routh routhChecker = new Routh();
        double[] coefficients0 = {1,6,11,6}; 
        Routh result0 = routhChecker.stable(coefficients0);
        printResult(coefficients0, result0);
        double[] coefficients1 = {1,2,3,4,5}; 
        Routh result1 = routhChecker.stable(coefficients1);
        printResult(coefficients1, result1);
        double[] coefficients2 = {1,1,2,24}; 
        Routh result2 = routhChecker.stable(coefficients2);
        printResult(coefficients2, result2);
        double[] coefficients3 = {1,2,3,6,5,3}; 
        Routh result3 = routhChecker.stable(coefficients3);
        printResult(coefficients3, result3);
    }
    private static void printResult(double[] coefficients, Routh result) {
        System.out.println("\nPolynomial Coefficients: " + Arrays.toString(coefficients));
        System.out.println("Stability: " + result.getStability());
        double[][] routhArray = result.getAnswer();
        if (routhArray == null || routhArray.length == 0) {
            System.out.println("No valid Routh array (System unstable).");
            return;
        }
        System.out.println("Routh Array:");
        for (double[] row : routhArray) {
            System.out.println(Arrays.toString(row));
        }
    }
}