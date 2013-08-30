package org.softwarehelps.learncs.LOGICGATES;

import java.util.*;
import java.awt.*;

public class U {
     public static int atoi (String s) {
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               return Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     public static long atol (String s) {
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               return Long.valueOf(s).longValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     public static double atod (String s) {
          try {
               return Double.valueOf(s).doubleValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     public static String[] copy (String array[]) {
          String[] newone = new String[array.length];
          for (int i=0; i<array.length; i++)
               newone[i] = new String(array[i]);
          return newone;
     }

     public static String[] tokenize(String s) {
          StringTokenizer st = new StringTokenizer(s);
          String[] tokens = new String[st.countTokens()];
          int i=0;
          while (st.hasMoreTokens()) {
               String token = st.nextToken();
               tokens[i++] = token;
          }
          return tokens;
     }

     public static String[] tokenize(String s, String delim) {
          StringTokenizer st = new StringTokenizer(s, delim);
          String[] tokens = new String[st.countTokens()];
          int i=0;
          while (st.hasMoreTokens()) {
               String token = st.nextToken();
               tokens[i++] = token;
          }
          return tokens;
     }

     public static String detokenize(String[] tokens) {
          String s = "";
          for (int i=0; i<tokens.length; i++)
               if (tokens[i] != null)
                    s += tokens[i] + " ";
          return s;
     }

     public static boolean equals (String[] list1, String[] list2) {
          if (list1.length != list2.length)
               return false;
          for (int i=0; i<list1.length; i++)
               if (list1[i] != null && list2[i] != null)
                    if (list1[i].equals(list2[i]))
                         return false;
          return true;
     }

     public static void sleep (long milliseconds) {
          try {
               Thread.sleep(milliseconds);
          } catch (InterruptedException ie) {}
     }

     public static int power(int n, int power) {
          if (power < 0)
               return 0;
          int result = 1;
          for (int i=0; i<power; i++)
               result *= n;
          return result;
     }

     public static String convert (int n, int base) {
          String s = "";
          if (n == 0)
               return "0";
          while (n > 0) {
               int rem = n % base;
               n = n / base;
               s = Character.forDigit(rem, base) + s;
          }
          return s.toUpperCase();
     }

     public static boolean isint (String s) {
          char ch = s.charAt(0);
          int starting = 0;
          if (ch == '+' || ch == '-') {
               starting = 1;
               if (s.length() == 1)
                    return false;
          }
          for (int i=0; i<s.length(); i++) {
               ch = s.charAt(i);
               if (!Character.isDigit(ch))
                    return false;
          }
          return true;
     }

     public static String dec2bin (int n) {
          int sign = 1;
          if (n < 0) {
               sign = -1;
               n = -n;
          }
//System.out.println ("sign="+sign);
          String s = "";
          if (n == 0)
               return "0000000000000000";
          while (n > 0) {
               int rem = n % 2;
               n = n / 2;
               if (rem == 1)
                    s = "1" + s;
               else
                    s = "0" + s;
          }
//System.out.println ("s = "+s);
          if (sign == 1)
               return s;
          else {
//System.out.println ("2's comp = "+twoscomplement(s));
               return twoscomplement(s);
}
     }

     public static int bin2dec (String s) {
          int sign = 1;
          if (s.length() >= 16 && s.charAt(0) == '1') {
               sign = -1;
               s = twoscomplement(s);
          }
          int n = 0;
          int len = s.length();
          for (int i=0; i<len; i++) {
               char ch = s.charAt(i);
               n *= 2;
               if (ch == '1')
                    n += 1;
          }
          return sign * n;
     }

     public static String twoscomplement (String s) {
          s = padout(s, '0', 16);
//System.out.println ("inside 2's comp = "+s);
          String news = "";
          for (int i=0; i<16; i++) {
               char ch = s.charAt(i);
               if (ch == '0')
                    news += "1";
               else
                    news += "0";
          }
//System.out.println ("news = "+news);
          String result = "";
          int carry = 1;
          for (int i=15; i>=0; i--) {
               char ch = news.charAt(i);
               if (carry == 1) {
                    if (ch == '1') 
                         result = "0" + result;
                    else {
                         result = "1" + result;
                         carry = 0;
                    }
               }
               else {
                    result = ch + result;
               }
          }
//System.out.println ("result="+result);
          return result;
     }

     public static String padout (String s, char padChar, int max) {
          while (s.length() < max)
               s = padChar + s;
          return s;
     }

     public static String squish (String s, char remch) {
          String news = "";
          for (int i=0; i<s.length(); i++) {
               char ch = s.charAt(i);
               if (ch != remch)
                   news += ch;
          }
          return news;
     }

     public static Color translateColor (String s) {
          if (s.equals("white"))  return Color.white;
          if (s.equals("red"))    return Color.red;
          if (s.equals("yellow")) return Color.yellow;
          if (s.equals("blue"))   return Color.blue;
          if (s.equals("cyan"))   return Color.cyan;
          if (s.equals("green"))  return Color.green;
          if (s.equals("gray"))   return Color.lightGray;
          return Color.black;
     }

     public static String replaceChar (String s, char oldch, char newch) {
          String news = "";
          for (int i=0; i<s.length(); i++) {
               char ch = s.charAt(i);
               if (ch == oldch)
                   news += newch;
               else
                   news += ch;
          }
          return news;
     }

     public static String getField (String s, int fieldnum) {
          return getField (s, fieldnum, ' ');
     }

     public static String getField (String s, int fieldnum, char separator) {
          StringTokenizer st = new StringTokenizer(s, separator+"");
          for (int i=0; i<=fieldnum; i++) {
               if (!st.hasMoreTokens())
                    return null;
               String tok = st.nextToken();
               if (i == fieldnum)
                    return tok;
          }
          return null;
     }

     public static String skipFields (String s, int numfields) {
          StringTokenizer st = new StringTokenizer(s);
          int numtokens = st.countTokens();
          String rets = "";
          for (int i=1; i<=numtokens; i++) {
               if (!st.hasMoreTokens())
                    return "";
               String tok = st.nextToken();
               if (i > numfields) {
                    if (rets.length() == 0)
                         rets += tok;
                    else
                         rets += " "+tok;
               }
          }
          return rets;
     }

     public static void main (String[] args) {
          String s = "abc/xyz/def/jdkfjdkfd";
          System.out.println ("field 0 = "+getField(s,0,'/'));
          System.out.println ("field 1 = "+getField(s,1,'/'));
          System.out.println ("field 2 = "+getField(s,2,'/'));
          System.out.println ("field 3 = "+getField(s,3,'/'));
     }
}
