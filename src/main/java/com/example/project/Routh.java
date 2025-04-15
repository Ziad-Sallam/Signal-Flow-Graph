package com.example.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Routh {
    private double[][] answer;
    private String stability;
    private int rhpPoles;
    public Routh(double[][] answer, String stability, int rhpPoles) {
        this.answer = answer;
        this.stability = stability;
        this.rhpPoles = rhpPoles;
    }
    public Routh() {
        stability = "";
        rhpPoles = 0;
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
    public int getRhpPoles() {
        return rhpPoles;
    }
    public void setRhpPoles(int rhpPoles) {
        this.rhpPoles = rhpPoles;
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
            stability = "unstable (first coefficient is zero)";
            return false;
        }
        for (int i = 1; i < n; i++) {
            if (Math.signum(coeffients[i]) != sign) {
                stability = "unstable (coefficients have different signs)";
                return false;
            }
        }
        stability = "stable";
        return true;
    }
    private boolean checkzerorow(double[][] routh, int row) {
        for (int i = 0; i < routh[0].length; i++) {
            if (Math.abs(routh[row][i]) > 1e-10) {
                return false;
            }
        }
        stability = "critically unstable (entire zero row)";
        return true;
    }
    private boolean derivative(double[][] routh, int row) {
        int prev = row - 1;
        int degree = ((routh.length) - 1) - prev;

        for (int i = 0; i < routh[0].length && degree >= 0; i++) {
            routh[row][i] = routh[prev][i] * degree;
            degree -= 2;
        }
        // Check for duplicate roots on the imaginary axis
        if (prev < routh.length - 3) { // must be quadratic equation.
            double a = routh[prev][0]; // a⋅s^2+b⋅s+c
            double b = routh[prev][1];
            double c = routh[prev][2];
            /*
             * If the discriminant D=b^2−4ac is zero, the polynomial has duplicate roots.
             * Combining these conditions, the equation 2⋅root(a⋅c)−∣b∣<1×10^−10checks if the roots are duplicate and purely imaginary.
             */
            if (2*Math.sqrt(a*c)-Math.abs(b)<1e-10) {
                stability = "critically unstable (duplicate roots on imaginary axis)";
                return false;
            }
        }
        return true;
    }
    private double[][] calculate(double[] coeffients) {
        int n = coeffients.length; // number of rows
        int m = (n + 1) / 2; // number of columns
        double[][] routh = new double[n][m];
        for (int j = 0, i = 0; j < m && i < n; i += 2, j++) {
            routh[0][j] = coeffients[i];
        }
        for (int j = 0, i = 1; j < m && i < n; i += 2, j++) {
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
                double b = routh[i - 2][0];// 1
                double c = routh[i - 1][l + 1]; // 4
                double d = routh[i - 2][l + 1]; // 3
                if (Math.abs(a) < 1e-10) {
                    a = 1e-5;
                }
                routh[i][l]=((a*d)-(b*c))/a;
            }
            if (Math.abs(routh[i][0]) <= 1e-10 && checkzerorow(routh, i)) {
                if (!derivative(routh, i)) {
                    return routh; // System is critically unstable
                }
            }
            if (Math.abs(routh[i][0]) < 1e-10) {
                routh[i][0] = 1e-5;
            }
        }
        return routh;
    }
    private int countRHPPoles(double[][] routh) {
        int signChanges = 0;
        double prevSign = Math.signum(routh[0][0]);
        for (int i = 1; i < routh.length; i++) {
            double currentSign = Math.signum(routh[i][0]);
            if (currentSign != prevSign && Math.abs(currentSign) > 1e-10) {
                signChanges++;
                prevSign = currentSign;
            }
        }
        return signChanges;
    }
    public Routh stable(double[] coeffients) {
        stability = "";
        int n = coeffients.length;
        if (!coffcheck(coeffients)) {
            return new Routh(new double[n][(n + 1) / 2], this.stability, 0);
        }
        double[][] routh = calculate(coeffients);
        int rhpPoles = countRHPPoles(routh);
        if (!signdoesntchange(routh)) {
            this.stability = "unstable";
            this.rhpPoles = rhpPoles;
        }
        return new Routh(routh, this.stability, rhpPoles);
    }
    public String getRoots(double [] coeffients) {
        int n = coeffients.length;
        StringBuilder exp = new StringBuilder();

        exp.append(coeffients[n-1]).append("*x^").append(n-1);
        for (int i = 1; i < n; i++) {
            if (coeffients[i] >= 0) exp.append(" + ");
            else{
                exp.append(" - ");
            }
            exp.append(Math.abs(coeffients[i])).append("*x^").append(n-i-1);
        }

        String expString = exp.toString();
        System.out.println(expString);
        try {

            ProcessBuilder pb = new ProcessBuilder(
                    "python",
                    "-c",
                    "from x import find_polynomial_roots; print(find_polynomial_roots('" + expString + "'));"
            );
            System.out.println("from x import find_polynomial_roots; print(find_polynomial_roots('" + expString + "'));");

            Process p = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line + " yaaay!!");

            }

            int exitCode = p.waitFor();
            System.out.println("Exit Code: " + exitCode);
            return line;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
    // Test
    public static void main(String[] args) {
        Routh routhChecker = new Routh();
//        double[] coefficients1 = {1, 2, 3, 4, 5};
//        Routh result1 = routhChecker.stable(coefficients1);
//        printResult(coefficients1, result1);
//        double[] coefficients2 = {1, 1, 2, 24};
//        Routh result2 = routhChecker.stable(coefficients2);
//        printResult(coefficients2, result2);
//        double[] coefficients3 = {1, 2, 1, 2};
//        Routh result3 = routhChecker.stable(coefficients3);
//        printResult(coefficients3, result3);
//        double[] coefficients4 = {1, 2, 2, 4, 11, 10};
//        Routh result4 = routhChecker.stable(coefficients4);
//        printResult(coefficients4, result4);
//        double[] coefficients5 = {3, 5, 6, 3, 2, 1};
//        Routh result5 = routhChecker.stable(coefficients5);
//        printResult(coefficients5, result5);
//        double[] coefficients6 = {1, 2, 4, 8};
//        Routh result6 = routhChecker.stable(coefficients6);
//        printResult(coefficients6, result6);

        System.out.println(routhChecker.getRoots(new double[]{1,0,0,4}));
    }
    private static void printResult(double[] coefficients, Routh result) {
        System.out.println("\nPolynomial Coefficients: " + Arrays.toString(coefficients));
        System.out.println("Stability: " + result.getStability());
        if (result.getStability().equals("unstable")) {
            System.out.println("Number of poles in the right-half plane (RHP): " + result.getRhpPoles());
        }
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