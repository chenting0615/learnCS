public class Matcher {

     public static StringList match (String input, String pattern) {
          return match (new StringList(input), new StringList(pattern));
     }

     public static StringList match (StringList input, StringList pattern) {
          StringList matches = new StringList();

          while (input.length() > 0 && pattern.length() > 0) {
System.out.println("pattern="+pattern);
               String nextword = pattern.get(0);
System.out.println("nextword="+nextword);
               if (nextword.startsWith("*")) {
                    if (pattern.length() == 1) {
                         if (nextword.startsWith("*(")) {
                              StringList temp = input;
                              int m = nextword.indexOf(")");
                              if (m != -1) {
                                   int index = atoi(nextword.substring(3,m));
                                   matches.put(temp.toString(), index);
                              }
                         }
                         return matches;
                    }
                    if (pattern.get(1).startsWith("$")) {
                         System.out.println ("ERROR: $ var cannot follow *");
                         return null;
                    }
                    String word = pattern.get(1);
                    int n = input.find(word);
System.out.println("found = "+n);
                    if (n == -1) 
                         return null;
                    if (nextword.startsWith("*(")) {
                         StringList temp = input.shift(n);
                         int m = nextword.indexOf(")");
                         if (m != -1) {
                              int index = atoi(nextword.substring(3,m));
                              matches.put(temp.toString(), index);
                         }
                         temp = input.shift(1);
                    }
                    else 
                         input.shift(n+1);
                    pattern.shift(2);
               }
               else if (nextword.equals("$?")) {
                    input.shift(1);
                    pattern.shift(1);
               }
               else if (nextword.startsWith("$")) {

// new
                    int slashpos = nextword.indexOf("/");
                    StringList stopwords = null;
                    if (slashpos > -1) {
                         stopwords = 
                            new StringList(nextword.substring(slashpos+1),",");
                         nextword = nextword.substring(0,slashpos);
                    }
//
                    String theword = input.get(0);
// new
                    if (stopwords != null && stopwords.find(theword) != -1)
                         return null;
//
                    int number = atoi(nextword.substring(1));
                    if (matches.get(number) == null) {
                         matches.put(theword, number);
                         input.shift(1);
                         pattern.shift(1);
                    }
                    else if (matches.get(number).equals(theword)) {
                         input.shift(1);
                         pattern.shift(1);
                    }
                    else
                         return null;
               }
               else if (input.get(0).equals(nextword)) {
                    input.shift(1);
                    pattern.shift(1);
               }
               else 
                    return null;
          }
          if (input.length() == 0) {
               if (pattern.length() == 0) 
                    return matches;
               if (pattern.length() == 1) {
                    if (pattern.get(0).equals("*"))
                         return matches;
                    else
                         return null;
               }
               else
                    return null;
          }
          else
               return null;
     }

     // The following does a substitution on $NNN variables.

     public static String substitute (String input, StringList subs) {
          StringList tokens = new StringList(input);
          String ret = "";
          for (int i=0; i<tokens.length(); i++) {
               String token = tokens.get(i);
               if (token.startsWith("$")) {
                    String trailings = trimNondigits(token.substring(1));
                    int number = atoi(token.substring(1));
                    String replacement = subs.get(number);
                    if (replacement != null)
                         ret += replacement + trailings + " ";
               }
               else
                    ret += token + " ";
          }
          return ret;
     }

     private static int atoi (String s) {
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               return Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     private static String trimNondigits (String s) {
          while (s.length() > 0) {
               if (Character.isDigit(s.charAt(0)))
                    s = s.substring(1);
               else
                    break;
          }
          return s;
     }
}
