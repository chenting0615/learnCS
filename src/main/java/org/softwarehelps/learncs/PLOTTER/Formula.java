package org.softwarehelps.learncs.PLOTTER;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;
import java.text.*;
import java.util.*;


class Formula {
     double[] factors = new double[100];
     double[] powers = new double[100];
     int[] types = new int[100];       // 0 = x, 1 = sin, 2 = cos
     int size = 0;
     public String error = "";
     String formula;
     boolean debug = true;
     int numCharsToChop = 0;     // this is an ugly expedient, because
                                 // getNextNum() can't return two things:
                                 // a string, and the number of chars to chop

     public Formula (String formula) {
          size = 0;
          parse(formula);
     }

     public void parse(String formula) {
          this.formula = formula;

          // squish out all blanks
          formula = U.squish(formula, ' ');

          if (Character.isLetter(formula.charAt(0)))
               formula = "1" + formula;
          if (Character.isDigit(formula.charAt(0)))
               formula = "+" + formula;

          formula = formula + ';';

          boolean endsWithConstant = false;

          
          while (formula.length() > 1) {
               String num1 = getNextNumber(formula);   // this also sets
                                                       // numCharsToChop

               if (!U.isreal(num1)) {
                    error = "Real number expected here: "+formula;
                    return;
               }
               formula = formula.substring(numCharsToChop);

               if (formula.startsWith("x^")) {
                    types[size] = 0;
                    formula = formula.substring(2);
               }
               else if (formula.startsWith("sin")) {
                    types[size] = 1;
                    formula = formula.substring(3);
               }
               else if (formula.startsWith("cos")) {
                    types[size] = 2;
                    formula = formula.substring(3);
               }
               else if (formula.startsWith("x")) {   // such as +x-3...
                    types[size] = 0;
                    formula = formula.substring(1);
               }
               else if (formula.startsWith(";")) 
                    endsWithConstant = true;
               else {
                    error = "x, sin or cos expected here: "+formula;
                    return;
               }

               String num2;
               if (!endsWithConstant) {
                    num2 = getNextNumber(formula);
                    if (num2.length() == 0){
                         // this means they entered an angle of 1x radians,
                         // such as    -3cos x
                         // or the polynomial formula merely ended with x

                         num2 = "1";
                         numCharsToChop = 0;
                    }
                    if (!U.isreal(num2)) {
                         error = "Real number expected here: "+formula;
                         return;
                    }
                    formula = formula.substring(numCharsToChop);

                    if (types[size] > 0) {
                         // we expect an "x" here
                         if (formula.charAt(0) != 'x') {
                              error = "x expected here: "+formula;
                              return;
                         }
                         else
                              formula = formula.substring(1);  // chop off 'x'
                    }
               }
               else
                    num2 = "0";

               factors[size] = U.atod(num1);
               powers[size] = U.atod(num2);
               size++;
          }
     }

     private String getNextNumber (String s) {
          String num = "";
          char ch = s.charAt(0);
          int i = 1;
          if (ch == '(') {
               int k = s.indexOf(")");
               if (k == -1)
                    return "";    // signal an error
               num = s.substring(1,k);
               numCharsToChop = num.length() + 2;
               return num;
          }

          if (ch != '+' && ch != '-' && ch != '.' && !Character.isDigit(ch))
               return "";
          num = num + ch;

          ch = s.charAt(i);
          while (ch == '.' || Character.isDigit(ch)) {
               num = num + ch;
               i++;
               ch = s.charAt(i);
          }
          numCharsToChop = num.length();

          if (num.startsWith("(") && num.endsWith(")"))
               num = num.substring(1,num.length()-1);

          if (num.equals("+") || num.equals("-"))
               num = num + "1";
          return num;
     }


     public double evaluate(double x) {
          double result = 0;
          for (int i=0; i<size; i++) {
               switch (types[i]) {
                    case 0: result += factors[i] * Math.pow(x, powers[i]);
                            break;
                    case 1: result += factors[i] * Math.sin(x * powers[i]);
                            break;
                    case 2: result += factors[i] * Math.cos(x * powers[i]);
                            break;
               }
          }
          return result;
     }

     public void show() {
          for (int i=0; i<size; i++) {
               System.out.println (i+".  "+factors[i]+"  "+powers[i]);
          }
     }

     public static void main (String[] args) {
//        Formula fe = new Formula("+5.23x^4.6-2x^3-8.7");       // okay
//        Formula fe = new Formula("+5.23x^4.6-2x^(-4)-8.7");    // okay
//        Formula fe = new Formula("+5.23x^.6-2x^(-.1)");        // okay
//        Formula fe = new Formula("x^4+x^2-6");                 // okay
//        Formula fe = new Formula("3cos1x - 0.5cos5x");         // okay
          Formula fe = new Formula("3cosx - 0.5cos5x");         // okay
//        fe.show();
          if (fe.error.length() > 0)
               System.out.println ("ERROR: "+ fe.error);
          System.out.println (fe.evaluate(8.2));
     }
}
