package org.softwarehelps.learncs.MEMFITTING;

import java.util.*;

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

}
