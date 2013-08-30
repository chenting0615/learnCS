package org.softwarehelps.learncs.FUNCGROWTH;

import java.util.*;
import java.io.*;
import java.awt.*;

public class Functions {

     public static Token evaluate (String funcname, Vector parmvalues,
                                   NameDB ndb) 
     {
          Funcgrowth parent = ndb.parent;

          if (userDefined(funcname, ndb))
               return evaluateUserDefined (funcname, parmvalues, ndb);

          if (funcname.equals("exit")) {
               if (parmvalues.size() == 0) {
                    Token tok = new Token (Token.INT, 0);
                    tok.returnType = "exit";
                    return tok;
               }
               else {
                    Token tok = (Token)parmvalues.elementAt(0);
                    tok.returnType = "exit";
                    return tok;
               }
          }
          else if (funcname.equals("type") || funcname.equals("typeof")) {
               Token parm1 = (Token)parmvalues.elementAt(0);
               return new Token (Token.STRING, parm1.getType());
          }
          else if (funcname.equals("sqrt")) {
               Token param = (Token)parmvalues.elementAt(0);
               double result = 0;
               if (param.type == Token.INT)
                    result = Math.sqrt((double)param.intval);
               else
                    result = Math.sqrt(param.doubleval);
               return new Token (Token.REAL, result);
          }
          else if (funcname.equals("log")) {
               Token param = (Token)parmvalues.elementAt(0);
               double result = 0;
               if (param.type == Token.INT)
                    result = log10((double)param.intval);
               else
                    result = log10(param.doubleval);
               return new Token (Token.REAL, result);
          }
          else if (funcname.equals("log2")) {
               Token param = (Token)parmvalues.elementAt(0);
               double result = 0;
               if (param.type == Token.INT)
                    result = log2((double)param.intval);
               else
                    result = log2(param.doubleval);
               return new Token (Token.REAL, result);
          }
          else if (funcname.startsWith("print")) 
               return printout (funcname, parmvalues, ndb);
          else if (funcname.equals("toString")) {
               Token param = (Token)parmvalues.elementAt(0);
               boolean newline = true;

               if (param.type == Token.INT) 
                    return new Token (Token.STRING, ""+param.intval);
               else if (param.type == Token.REAL)
                    return new Token (Token.STRING, ""+param.doubleval);
               else if (param.type == Token.STRING)
                    return new Token (Token.STRING, param.value);
               else if (param.type == Token.LIST)
                    return new Token (Token.STRING, printSublist(param));
               else 
                    return new Token (Token.STRING, "ERROR! "+param.value);
          }
          else if (funcname.equals("int")) {
               Token param = (Token)parmvalues.elementAt(0);
               if (param.type != Token.REAL)
                    return new Token(Token.ERROR, "integer parameter required");
               int n = (int)param.doubleval;
               return new Token(Token.INT, n);
          }
          else if (funcname.equals("real")) {
               Token param = (Token)parmvalues.elementAt(0);
               if (param.type != Token.INT)
                    return new Token(Token.ERROR, "real parameter required");
               double n = (double)param.intval;
               return new Token(Token.REAL, n);
          }
          else if (funcname.equals("atoi")) {
               Token param = (Token)parmvalues.elementAt(0);
               if (param.type != Token.STRING)
                    return new Token(Token.ERROR, "string parameter required");
               int n = Token.atoi(param.value);
               return new Token(Token.INT, n);
          }
          else if (funcname.equals("atod") || funcname.equals("atof") ||
                   funcname.equals("ator")) {
               Token param = (Token)parmvalues.elementAt(0);
               if (param.type != Token.STRING)
                    return new Token(Token.ERROR, "string parameter required");
               double n = Token.atod(param.value);
               return new Token(Token.REAL, n);
          }
          else if (funcname.equals("substring")) {
               Token parm1 = (Token)parmvalues.elementAt(0);
               Token parm2 = (Token)parmvalues.elementAt(1);
               Token parm3 = (Token)parmvalues.elementAt(2);

               if (parm1.type != Token.STRING)
                    return new Token (Token.ERROR, "First param must be a string");
               if (parm2.type != Token.INT)
                    return new Token (Token.ERROR, "Second param must be an int");
               if (parm3.type != Token.INT)
                    return new Token (Token.ERROR, "Third param must be an int");

               String origstring = parm1.value;
               int start = parm2.intval;
               int end = parm3.intval;

               if (end == -1)
                    end = origstring.length();
               if (start < 0)
                    return new Token (Token.ERROR, "Illegal starting value");
               
               String newSubstring = origstring.substring(start,end);

               return new Token(Token.STRING, newSubstring);
          }
          else if (funcname.equals("charAt")) {
               Token parm1 = (Token)parmvalues.elementAt(0);
               Token parm2 = (Token)parmvalues.elementAt(1);

               if (parm1.type != Token.STRING)
                    return new Token (Token.ERROR, "First param must be a string");
               if (parm2.type != Token.INT)
                    return new Token (Token.ERROR, "Second param must be an int");

               String origstring = parm1.value;
               int start = parm2.intval;

               if (start < 0)
                    return new Token (Token.ERROR, "Illegal index value");
               
               String newSubstring = origstring.substring(start,start+1);

               return new Token(Token.STRING, newSubstring);
          }
          else if (funcname.equals("length")) {
               Token parm1 = (Token)parmvalues.elementAt(0);

               if (parm1.type == Token.STRING)
                    return new Token(Token.INT, parm1.value.length());
               else if (parm1.type == Token.LIST)
                    return new Token(Token.INT, parm1.sublist.numtokens);

               return new Token (Token.ERROR, "Param must be string or list");
          }
          else if (funcname.equals("trunc")) {
               Token parm1 = (Token)parmvalues.elementAt(0);

               int retvalue = 0;
               if (parm1.type == Token.REAL)
                    retvalue = (int)parm1.doubleval;
               if (parm1.type == Token.INT)
                    retvalue = parm1.intval;
                
               return new Token(Token.INT, retvalue);
          }
          else if (funcname.equals("round")) {
               Token parm1 = (Token)parmvalues.elementAt(0);
               double realvalue = parm1.doubleval;
               if (parm1.type != Token.REAL)
                    return new Token (Token.ERROR, "First param must be a real");
               int numplaces = 0;

               if (parmvalues.size() == 2) {
                    Token parm2 = (Token)parmvalues.elementAt(1);

                    if (parm2.type != Token.INT)
                         return new Token (Token.ERROR, "Second param must be an int");
                    numplaces = parm2.intval;
               }

               for (int i=0; i<numplaces; i++)
                    realvalue *= 10.0;
               realvalue += 0.5;
               realvalue = (long)realvalue;           // truncate
               for (int i=0; i<numplaces; i++)
                    realvalue /= 10.0;

               return new Token(Token.REAL, realvalue);
          }
          else if (funcname.equals("list")) {
               Token newtok = new Token();
               newtok.type = Token.LIST;
               newtok.sublist = new TokenList();

               for (int i=0; i<parmvalues.size(); i++) {
                    Token parm = (Token)parmvalues.elementAt(i);
                    newtok.sublist.add(parm);
               }

               return newtok;
          }
          else if (funcname.equals("append")) {
               Token parm1 = (Token)parmvalues.elementAt(0);
               Token parm2 = (Token)parmvalues.elementAt(1);
               
               Token newtok = new Token();
               newtok.type = Token.LIST;

               if (parm1.type == Token.LIST && parm2.type != Token.LIST) {
                    newtok.sublist = copyTokens(parm1.sublist);
                    newtok.sublist.add(parm2);
               }
               else if (parm1.type != Token.LIST && parm2.type == Token.LIST) {
                    newtok.sublist = new TokenList();
                    newtok.sublist.add(parm1);
                    append (newtok.sublist, parm2.sublist);
               }
               else if (parm1.type == Token.LIST && parm2.type == Token.LIST) {
                    newtok.sublist = copyTokens(parm1.sublist);
                    append (newtok.sublist, parm2.sublist);
               }

               return newtok;
          }
          else if (funcname.equals("delete")) {
               Token parm1 = (Token)parmvalues.elementAt(0);
               Token parm2 = (Token)parmvalues.elementAt(1);
               
               Token newtok = new Token();
               newtok.type = Token.LIST;

               if (parm1.type != Token.LIST)
                    return new Token(Token.ERROR, "First param must be a list.");
               if (parm2.type != Token.INT)
                    return new Token(Token.ERROR, "Second param must be an int.");

               int index = parm2.intval;
               if (index < 0 || index > parm1.sublist.numtokens) 
                    return new Token(Token.ERROR, "Index out of bounds.");

               TokenList newlist = new TokenList();
               for (int i=0; i<parm1.sublist.numtokens; i++)
                    if (i != index)
                         newlist.add(parm1.sublist.tokens[i]);

               newtok.sublist = newlist;

               return newtok;
          }
          else if (funcname.equals("set")) {
               Token parm1 = (Token)parmvalues.elementAt(0);
               Token parm2 = (Token)parmvalues.elementAt(1);
               Token parm3 = (Token)parmvalues.elementAt(2);

               if (parm1.type != Token.LIST) 
                    return new Token (Token.ERROR, "First param must be a list");
               if (parm2.type != Token.INT) 
                    return new Token (Token.ERROR, "Second parm must be an integer");

               int index = parm2.intval;
               if (index < 0)
                    return new Token (Token.ERROR, "Index out of bounds.");

               while (index >= parm1.sublist.numtokens) 
                    parm1.sublist.add(new Token(Token.INT, 0));
         
               parm1.sublist.tokens[index] = parm3;
          }
          else if (funcname.equals("get")) {
               Token parm1 = (Token)parmvalues.elementAt(0);
               Token parm2 = (Token)parmvalues.elementAt(1);

               if (parm1.type != Token.LIST) 
                    return new Token (Token.ERROR, "First param must be a list");
               if (parm2.type != Token.INT) 
                    return new Token (Token.ERROR, "Second parm must be an integer");
         
               int index = parm2.intval;
               if (index < 0 || index > parm1.sublist.numtokens)
                    return new Token (Token.ERROR, "Index out of bounds");
               return parm1.sublist.tokens[index];
          }
          else if (funcname.equals("fromString")) {
               Token parm1 = (Token)parmvalues.elementAt(0);
               return fromString(parm1.value);
          }
          else if (funcname.equals("eval")) {
               Token parm1 = (Token)parmvalues.elementAt(0);
               Parser p = new Parser (parm1.value);
               ProgElement seq = p.result;
               return seq.evaluate(ndb);
          }

/*
          // painting routines, new to Palgo

          else if (funcname.equals("clear")) {
               parent.clearCells();
               return new Token (Token.INT, 1);
          }
          else if (funcname.equals("color")) {
               if (parmvalues.size() == 1) {
                    Token parm = (Token)parmvalues.elementAt(0);
                    if (parm.type == Token.STRING)
                         parent.current = translateColor(parm.value);
               }
               else if (parmvalues.size() == 3) {
                    Token parm1 = (Token)parmvalues.elementAt(0);
                    Token parm2 = (Token)parmvalues.elementAt(1);
                    Token parm3 = (Token)parmvalues.elementAt(2);
                    int n1 = (int)(parm1.intval / (double)parent.NUMCELLS * 255);
                    int n2 = (int)(parm2.intval / (double)parent.NUMCELLS * 255);
                    int n3 = (int)(parm3.intval / (double)parent.NUMCELLS * 255);
System.out.println ("n1="+n1+"   n2="+n2+"   n3="+n3);
                    parent.current = new Color(parm1.intval, parm2.intval,
                                               parm3.intval);
               }
               return new Token (Token.INT, 1);
          }
          else if (funcname.equals("pen")) {
               Token parm = (Token)parmvalues.elementAt(0);
               if (parm.type == Token.STRING) {
                    if (parm.value.equals("down"))
                         parent.painting = true;
                    else if (parm.value.equals("up"))
                         parent.painting = false;
               }
               return new Token (Token.INT, 1);
          }
          else if (funcname.equals("draw")) {
               parent.cells[parent.currx][parent.curry] = parent.current;
               parent.repaint();
               return new Token (Token.INT, 1);
          }
          else if (funcname.equals("wait")) {
               Token parm = (Token)parmvalues.elementAt(0);
               double result = 0;
               if (parm.type == Token.INT) {
                    try {
                         Thread.sleep(parm.intval);
                    } catch (InterruptedException ie) {}
               }
               parent.repaint();
               return new Token (Token.INT, 1);
          }
          else if (funcname.equals("down")) {
               Token parm = (Token)parmvalues.elementAt(0);
               double result = 0;
               if (parm.type == Token.INT) {
                    for (int i=0; i<parm.intval; i++) {
                         parent.cells[parent.currx][parent.curry] = parent.current;
                         parent.curry++;
                         if (parent.curry >= parent.NUMCELLS) 
                              parent.curry = 0;
                    }
               }
               parent.repaint();
               return new Token (Token.INT, 1);
          }
          else if (funcname.equals("up")) {
               Token parm = (Token)parmvalues.elementAt(0);
               double result = 0;
               if (parm.type == Token.INT) {
                    for (int i=0; i<parm.intval; i++) {
                         parent.cells[parent.currx][parent.curry] = parent.current;
                         parent.curry--;
                         if (parent.curry < 0)
                              parent.curry = parent.NUMCELLS-1;
                    }
               }
               parent.repaint();
               return new Token (Token.INT, 1);
          }
          else if (funcname.equals("right")) {
               Token parm = (Token)parmvalues.elementAt(0);
               double result = 0;
               if (parm.type == Token.INT) {
                    for (int i=0; i<parm.intval; i++) {
                         parent.cells[parent.currx][parent.curry] = parent.current;
                         parent.currx++;
                         if (parent.currx >= parent.NUMCELLS)
                              parent.currx = 0;
                    }
               }
               parent.repaint();
               return new Token (Token.INT, 1);
          }
          else if (funcname.equals("left")) {
               Token parm = (Token)parmvalues.elementAt(0);
               double result = 0;
               if (parm.type == Token.INT) {
                    for (int i=0; i<parm.intval; i++) {
                         parent.cells[parent.currx][parent.curry] = parent.current;
                         parent.currx--;
                         if (parent.currx < 0)
                              parent.currx = parent.NUMCELLS-1;
                    }
               }
               parent.repaint();
               return new Token (Token.INT, 1);
          }
          else if (funcname.equals("goto")) {
               Token parm1 = (Token)parmvalues.elementAt(0);
               Token parm2 = (Token)parmvalues.elementAt(1);
               if (parm1.type == Token.INT && parm2.type == Token.INT) {
                    parent.currx = parm1.intval;
                    parent.curry = parm2.intval;
               }
               return new Token (Token.INT, 1);
          }
          else if (funcname.equals("random")) {
               Random r = new Random();
               if (parmvalues.size() > 0) {
                    Token parm1 = (Token)parmvalues.elementAt(0);
                    if (parm1.type == Token.INT) {
                         long n = new Date().getTime() / 73 * parent.extraSeed();
                         r.setSeed(n);
                         int randomN = (int)(r.nextFloat()*parm1.intval);
                         return new Token (Token.INT, randomN);
                    }
                    else if (parm1.type == Token.REAL) {
                         long n = new Date().getTime() / 73 * parent.extraSeed();
                         r.setSeed(n);
                         double randomN = (double)r.nextFloat()*parm1.doubleval;
                         return new Token (Token.REAL, randomN);
                    }
               }
               else
                    return new Token(Token.REAL, r.nextFloat());
          }
*/

          new Popup("Unknown function name: "+funcname);
          return new Token (Token.ERROR, "Unknown function name");
     }

     private static double log10 (double x) {
          return Math.log(x) / Math.log(10);
     }

     private static double log2 (double x) {
          return Math.log(x) / Math.log(2);
     }

     private static TokenList copyTokens(TokenList oldlist) {
          TokenList newlist = new TokenList();
          for (int i=0; i<oldlist.numtokens; i++)
               newlist.add(oldlist.tokens[i]);
          return newlist;
     }

     private static void append (TokenList list1, TokenList list2) {
          for (int i=0; i<list2.numtokens; i++)
               list1.add(list2.tokens[i]);
     }

     private static String printSublist (Token param) {
          String retval = "";
          if (param.type == Token.LIST) {
               retval += "(";
               TokenList mysublist = param.sublist;
               for (int i=0; i<mysublist.numtokens; i++){
                    retval += printSublist(mysublist.tokens[i]);
                    if (i < mysublist.numtokens-1)
                         retval += ", ";
               }
               retval += ")";
          }
          else if (param.type == Token.INT)
               retval += param.intval;
          else if (param.type == Token.REAL)
               retval += param.doubleval;
          else if (param.type == Token.STRING)
               retval += param.value;
          return retval;
     }

     private static boolean userDefined (String funcname, NameDB ndb) {
          return ndb.getFunction(funcname) != null;
     }

     private static Token evaluateUserDefined (String funcname, 
                                               Vector parmValues, NameDB ndb) 
     {
          //  First, go through the list of parameter names (found in the
          //  name database) and save those values to the stack.
          //  Then associate the parmValues with those names.


          saveCurrentVariables(ndb.getParameters(funcname), parmValues, ndb);

          //  Next, evaluate the sequence that is the body of the function.
          //  Save the return value.

          ProgElement funcbody = ndb.getFunction(funcname);
          if (funcbody == null) 
               return new Token (Token.ERROR, "No such function defined.");

          Token retvalue = funcbody.evaluate(ndb);
          if (!retvalue.returnType.equals("exit"))
               retvalue.returnType = "";     // deactivate any internal return
                                             // unless it is program exit


          //  Almost done!  First check to see if any local variables
          //  were declared.  If there were, "-2" will be on top of the stack.

          if (ndb.topName().equals("-2"))
               restoreCurrentVariables(ndb);

          //  Finally, go through the list of parameter names on the stack
          //  and reassociate them with their former values.

          restoreCurrentVariables(ndb);

          return retvalue;
     }

     public static void saveCurrentVariables (String varlist, Vector newValues,
                                              NameDB ndb)
     {
          StringTokenizer st = new StringTokenizer(varlist);
          int i = 0;
          ndb.push("-1", null);    // marks the end of activation record (-1)
                                   // or bottom of the local variables

          while (st.hasMoreTokens()) {
               String argName = st.nextToken();
               ndb.push (argName, ndb.get(argName));
               if (newValues != null) {
                    Token parmval = (Token)newValues.elementAt(i);
                    ndb.set (argName, parmval);
               }
               else
                    ndb.set (argName, new Token(Token.INT, 0));
               i++;
          }
     }

     public static void restoreCurrentVariables (NameDB ndb) {
          while (true) {
               String argName = ndb.topName();
               if (argName.equals("-1")) break;
               Token argValue = ndb.topValue();
               ndb.pop();
               ndb.set(argName, argValue);
          }
          ndb.pop();      // get rid of the -1 placeholder
     }

     public static Token fromString (String s) {
          return (Token)(fromStringAux(s)[0]);
     }

     public static Object[] fromStringAux (String s) {
          String temp = "";
          int i = 0;
          s = s.trim();
          if (s.length() == 0)
               return new Object[] {new Token (Token.ERROR, "Nothing to convert"),
                                    ""};

          char ch = s.charAt(0);

          if (ch == ',') {
               return new Object[] {new Token (Token.ERROR, "improper comma"),
                                    ""};
          }
          else if (isPartOfNumber(ch)) {
               String newstring = ch+"";
               boolean isReal = false;
               i++;                      // skip over leading double quote
               while (i < s.length()) { 
                    ch = s.charAt(i);
                    if (!isPartOfNumber(ch)) break;
                    if (ch == '.')
                         isReal = true;
                    newstring += ch;
                    i++;
               }
               if (isReal)
                    return new Object[] {new Token (Token.REAL, newstring),
                                         s.substring(i)};
               else
                    return new Object[] {new Token (Token.INT, newstring),
                                         s.substring(i)};
          }
          else if (ch == '(') {
               TokenList tl = new TokenList();
               s = s.substring(1);
               while (true) {
                    Object[] retval = fromStringAux (s);
                    Token tok = (Token)retval[0];
                    if (tok.type == Token.ERROR)
                         return new Object[] {tok, ""};
                    tl.add(tok);
                    s = ((String)retval[1]).trim();
                    if (s.length() == 0) {
                         return new Object[] {tl, ""};
                    }
                    if (s.charAt(0) == ')') {
                         Token wrapper = new Token (Token.LIST, tl);
                         s = (s.substring(1)).trim();
                         if (s.startsWith(","))
                              s = s.substring(1);
                         return new Object[] {wrapper, s};
                    }
                    if (s.charAt(0) == ',') 
                         s = s.substring(1);
               }  
          }
          else if (ch == '"') {          // a properly delimited string
               String newstring = "";
               i++;                      // skip over leading double quote
               while (i < s.length()) { 
                    ch = s.charAt(i);
                    if (ch == '"') break;
                    newstring += ch;
                    i++;
               }
               return new Object[] {new Token (Token.STRING, newstring),
                                    s.substring(i)};
          }
          else {                         // an undelimited string
               String newstring = ch+"";
               i++;                      // skip over leading double quote
               while (i < s.length()) { 
                    ch = s.charAt(i);
                    if (ch == ' ' || ch == ',' || ch == ')') break;
                    newstring += ch;
                    i++;
               }
               return new Object[] {new Token (Token.STRING, newstring),
                                    s.substring(i)};
          }
     }

     public static boolean isPartOfNumber (char ch) {
          return (Character.isDigit(ch) || ch == '-' || ch == '.');
     }

     private static Token printout (String funcname, Vector parmvalues,
                                    NameDB ndb) 
     {
          Token param = (Token)parmvalues.elementAt(0);
          boolean newline = true;
          String font = "normal";

          if (parmvalues.size() > 1) {
               for (int i=1; i<parmvalues.size(); i++) {
                   Token paramx = (Token)parmvalues.elementAt(i);
                   if (paramx.type == Token.STRING &&
                       paramx.value.equals("nobreak"))
                        newline = false;
                   if (paramx.type == Token.STRING &&
                       paramx.value.equals("courier"))
                        font = "courier";
               }
          }

          String toPrint = "";

          if (param.type == Token.INT)
               toPrint = param.intval + "";
          else if (param.type == Token.REAL)
               toPrint = param.doubleval + "";
          else if (param.type == Token.STRING)
               toPrint = param.value;
          else if (param.type == Token.LIST)
               toPrint = printSublist(param);
          else if (param.type == Token.ERROR)
               toPrint = "ERROR! "+param.value;

          if (newline)
               toPrint += "\n";

          if (funcname.equals("print")) 
               System.out.print (toPrint);
          else if (funcname.equals("printwindow")) 
               new Popup(toPrint);

          return param;
     }

     private static Color translateColor (String c) {
          if (c.equals("red"))
               return Color.red;
          if (c.equals("blue"))
               return Color.blue;
          if (c.equals("green"))
               return Color.green;
          if (c.equals("yellow"))
               return Color.yellow;
          if (c.equals("black"))
               return Color.black;
          if (c.equals("white"))
               return Color.white;
          if (c.equals("gray"))
               return Color.gray;
          if (c.equals("orange"))
               return Color.orange;
          if (c.equals("pink"))
               return Color.pink;
          if (c.equals("cyan"))
               return Color.cyan;
          if (c.equals("magenta"))
               return Color.magenta;

          StringTokenizer st = new StringTokenizer(c, ",");
          if (st.countTokens() != 3)
               return Color.white;
          int red = U.atoi(st.nextToken());
          int green = U.atoi(st.nextToken());
          int blue = U.atoi(st.nextToken());
          return new Color(red,green,blue);
     }
}
