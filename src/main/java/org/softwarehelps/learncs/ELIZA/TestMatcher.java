package org.softwarehelps.learncs.ELIZA;

public class TestMatcher {

     public static void main(String[] args) {
          do1 ("the days of our lives are great",
               "* days * lives $0 $1");
          do1 ("the days of our lives are great",
               "* lives $0");
          do1 ("the days of our lives are great",
               "* days $? $0 $1 *");
          do1 ("the days of our lives are great",
               "* days $? $0 $1");
          do1 ("the cat in the hat",
               "the $0 * the $1");
          do1 ("the cat in the hat",
               "the $0 * the $0");
          do1 ("the cat in the cat",
               "the $0 * the $0");
          do1 ("I have a problem",
               "I have a problem");
     }

     public static void do1(String s1, String s2) {
          StringList input = new StringList (s1);
          StringList patrn = new StringList (s2);
          System.out.println ("input="+s1);
          System.out.println ("patrn="+s2);

          StringList ret = Matcher.match (input, patrn);
          if (ret == null) 
               System.out.println ("No match!");
          else {
               System.out.println ("Match!");
               for (int i=0; i<ret.length(); i++) 
                    System.out.println ("$"+i+"="+ret.get(i));
          }
          System.out.println ("-------------------------------------");
     }
}
