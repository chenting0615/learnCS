import java.util.*;

public class Translator {
     private static String translateline (String s, int indent) {
          if (s.trim().startsWith("//"))
               return s+"\n";

          // Our first task is to see if there is a space between the
          // first token (word) and any parentheses that might follow.
          // If there are spaces, squish them out.  This is because the
          // preprocessor gets confused easily.  For example:
          //     draw  5  6
          // is considered to be draw(), not draw(5,6),
          // but unfortunately draw   (5,6) is also considered to be
          // draw().  See below.  We fix this, however, by squishing
          // out parentheses.

          s = squishSomeBlanks(s);


          // Now we can tokenize!

          String[] tokens = U.tokenize(s);
          if (tokens.length == 0) return "";


          // Our second task is to translate some of the supposedly
          // user unfriendly things to user-friendly things.

          for (int i=0; i<tokens.length; i++) {
               if (tokens[i].equals("input_string") ||
                   tokens[i].equals("input_string()")) {
                    tokens[i] = "input()";
               }
               else if (tokens[i].equals("input_number") ||
                        tokens[i].equals("input_number()")) {
                    tokens[i] = "atoi(input())";
               }
               else if (tokens[i].equals("array") ||
                        tokens[i].equals("array()")) {
                    tokens[i] = "list()";
               }
               else if (tokens[i].equals("table") || 
                        tokens[i].equals("table()")) {
                    tokens[i] = "0";
               }
               else if (tokens[i].equals("clear")) 
                    tokens[i] = "clear()";
               else if (tokens[i].equals("exit")) 
                    tokens[i] = "exit()";
               else if (tokens[i].equals("endfor")) 
                    tokens[i] = "end";
               else if (tokens[i].equals("endwhile")) 
                    tokens[i] = "end";
               else if (tokens[i].equals("endif")) 
                    tokens[i] = "end";
               else if (tokens[i].equals("enddefine")) 
                    tokens[i] = "end";
               else if (tokens[i].equals("endrepeat")) 
                    tokens[i] = "end";
          }

/*  for debugging ONLY!
          System.out.println ("TOKENS...");
          for (int i=0; i<tokens.length; i++) 
               System.out.println (i+". "+tokens[i]);
          System.out.println ("--------");
*/
   
          if (tokens[0].equals("end"))
               return "}\n";


          if (tokens[0].equals("else")) {
               String temp = prefixSpaces("}", indent) + "\n";
               temp += prefixSpaces("else", indent) + "\n";
               temp += prefixSpaces("{", indent) + "\n";
               return temp;
          }

          if (tokens[0].equals("draw")) {
               if (tokens.length == 1)
                    return "draw();\n";
               else if (tokens.length == 3)
                    return "draw("+tokens[1]+","+tokens[2]+");\n";
          }

          if (tokens[0].equals("drawline")) {
               if (tokens.length == 5)
                    return "drawline("+tokens[1]+","+tokens[2]+
                           ","+tokens[3]+","+tokens[4]+");\n";
               else {
                    new Popup("Drawline requires 4 parameters");
                    return "";
               }
          }

          if (tokens[0].equals("circle")) {
               if (tokens.length == 4)
                    return "circle("+tokens[1]+","+tokens[2]+
                           ","+tokens[3]+");\n";
               else {
                    new Popup("Circle requires 3 parameters");
                    return "";
               }
          }

          if (tokens[0].equals("break;")) 
               return "break;\n";

          if (tokens[0].equals("wait")) {
               if (tokens.length == 1)
                    return "wait(1000);";
               return "wait("+tokens[1]+");\n";
          }

          if (tokens[0].equals("down")) {
               if (tokens.length == 1)
                    return "down(1);";
               return "down("+tokens[1]+");\n";
          }

          if (tokens[0].equals("up")) {
               if (tokens.length == 1)
                    return "up(1);";
               return "up("+tokens[1]+");\n";
          }

          if (tokens[0].equals("left")) {
               if (tokens.length == 1)
                    return "left(1);";
               return "left("+tokens[1]+");\n";
          }

          if (tokens[0].equals("right")) {
               if (tokens.length == 1)
                    return "right(1);";
               return "right("+tokens[1]+");\n";
          }

          if (tokens[0].equals("repeat")) {
               if (tokens.length != 3)
                    return "";
               return "repeat("+tokens[1]+") {\n";
          }

          if (tokens[0].equals("while")) {
               String rets = "while (";
               for (int i=1; i<tokens.length; i++)
                    rets += tokens[i]+" ";
               rets += "){\n";
               return rets;
          }

          if (tokens[0].equals("pen")) {
               if (tokens.length != 2)
                    return "";
               return "pen(\""+tokens[1]+"\");\n";
          }

          if (tokens[0].equals("color")) {
               if (tokens.length == 2) {
                    return "color("+tokens[1]+");\n";
               }
               if (tokens.length == 4) 
                    return "color("+tokens[1]+","+tokens[2]+","+
                                      tokens[3]+");\n";
               return "color(\"black\");\n";
          }

          if (tokens[0].equals("numcells")) {
               if (tokens.length == 2) 
                    return "numcells("+tokens[1]+");\n";
               else
                    return "numcells(20);\n";
          }

          if (tokens[0].equals("goto")) {
               if (tokens.length != 3) 
                    return "";
               return "goto("+tokens[1]+","+tokens[2]+");\n";
          }

          if (tokens[0].startsWith("print")) {
               if (tokens[0].equals("print")) {
                    String rets = "print(";
                    for (int i=1; i<tokens.length; i++)
                         rets += tokens[i]+" ";
                    rets += ");\n";
                    return rets;
               }
               else {     // we assume it starts with print(
                    String rets = "print((";
                    tokens[0] = tokens[0].substring(6);
                    for (int i=0; i<tokens.length; i++)
                         rets += tokens[i]+" ";
                    rets += ");\n";
                    return rets;
               }
          }

          if (tokens[0].equals("for")) {
               if (tokens.length < 4) 
                    return "";

               // Several variations... the user may have squished the
               // variable up against the starting value, such as 
               //    for i=5 to 6

               String varname="", starting="", ending="";

               int n = s.indexOf("=");
               String news = s.substring(0,n) + " = " + s.substring(n+1);
               tokens = U.tokenize(news);

               varname = tokens[1];
               starting = tokens[3];
               ending = tokens[5];
                 
               String ret = "for ("+varname+"="+starting+"; "+
                            varname+"<="+ending+"; "+
                            varname+"++) {\n";

               return ret;
          }

          if (tokens[0].equals("if")) {
               // ignore the final then
               if (tokens[tokens.length-1].equals("then"))
                    tokens[tokens.length-1] = "";
               String rets = "if (";
               for (int i=1; i<tokens.length-1; i++)
                    rets += tokens[i]+" ";
               rets += ") {\n";
               return rets;
          }

          if (tokens[0].equals("define")) 
               return s + "{\n";

          // default action

          s = "";
          for (int i=0; i<tokens.length; i++) 
               s = s + " "+tokens[i];

          return s + ";\n";
     }

     public static String translate (String s) {
          String errorCode = findUnmatchedEnd(s);
          if (errorCode.length() != 0) {
               new Popup(errorCode);
               return "";
          }

          String rets = "";
          s = removeCstyleComments(s);
          StringTokenizer st = new StringTokenizer(s, "\n");
          while (st.hasMoreTokens()) {
               String temp = st.nextToken();
               temp = removeComments(temp);
               int n = measureIndentation(temp);
               while (true) {
                    int k = temp.indexOf(":");
                    String firstpart = temp;
                    if (k > -1) {
                         firstpart = temp.substring(0,k);
                         temp = temp.substring(k+1);
                    }
                    String trans = prefixSpaces(translateline(firstpart, n), n);
                    rets += trans;
                    if (k == -1) break;
               }
          }
          return rets;
     }

     private static String removeComments (String s) {
          int i = s.indexOf("//");
          if (i == -1) return s;
          return s.substring(0,i);
     }

     private static int measureIndentation (String s) {
          int count = 0;
          for (int i=0; i<s.length(); i++)
               if (s.charAt(i) == ' ')
                    count++;
               else
                    return count;
          return 0;
     }

     private static String prefixSpaces (String s, int numSpaces) {
          String spaces = "                                        ";
          return spaces.substring(0,numSpaces)+s;
     }

     private static String findUnmatchedEnd (String s) {
          String rets = "";
          s = removeCstyleComments(s);
          StringTokenizer st = new StringTokenizer(s, "\n");
          int count = 0;
          while (st.hasMoreTokens()) {
               String temp = st.nextToken();
               rets += temp + "\n";
               temp = removeComments(temp).trim();
               if (temp.startsWith("define") || temp.startsWith("if") ||
                   temp.startsWith("while") || temp.startsWith("repeat") ||
                   temp.startsWith("for") || temp.startsWith("repeat")) {
                    count++;
               }
               if (temp.startsWith("end"))
                    count--;
               if (count < 0)
                    return "ERROR, too many end statements.\n"+
                           "Incorrect code follows:\n"+
                           "---------------------------------\n"+rets;
               if (count == 0)
                    rets = "";
          }
          if (count > 0)
               return "ERROR, not enough end statements.\n"+
                      "Incorrect code follows:\n"+
                      "---------------------------------\n"+rets;

          return "";          // this means OKAY!
     }

     //------------------------------------------------------------
     //  This routine removes blanks AFTER the first alphanumeric
     //  word and the first left parenthesis that follows it.
     //  If this condition doesn't adhere, it returns the string
     //  unchanged:
     //
     //      squishSomeBlanks("   draw   (5,6)") ==> "   draw(5,6)"
     //------------------------------------------------------------

     private static String squishSomeBlanks (String s) {
          String news = "";
          int i = 0;
          int len = s.length();
          char ch = ' ';

          // first skip over leading space

          while (i < len) {
               ch = s.charAt(i);
               if (ch == ' ') break;
               i++;
          }
          if (i >= len) return s;

          // now skip over the alphabetic word if there is one!

          while (i < len) {
               ch = s.charAt(i);
               if (!Character.isLetterOrDigit(ch)) break;
               i++;
          }
          if (i >= len) return s;

          // now run ahead and see if there is a left paren after
          // some spaces.  If not, just leave without changing s.

          int oldi = i;
          while (i < len) {
               ch = s.charAt(i);
               if (ch != ' ') break;
               i++;
          }
          if (i >= len) return s;

          if (ch == '(') {
               return s.substring(0,oldi) + s.substring(i);
          }
          else
               return s;
     }

     public static String removeCstyleComments(String code) {
          code = code + " ";     // make sure lookahead won't fail
          String newcode = "";
          boolean insideComment = false;

          for (int i=0; i<code.length(); i++) {
               char ch = code.charAt(i);
               if (ch == '/' && code.charAt(i+1) == '*') {
                    insideComment = true;
                    i++;
                    continue;
               }
               if (ch == '*' && code.charAt(i+1) == '/') {
                    insideComment = false;
                    i++;
                    continue;
               }
               if (!insideComment)
                    newcode += ch;
          }

          return newcode;
     }
}
