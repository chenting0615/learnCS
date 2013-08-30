/**
     To tokenize an arithmetic expression involving real numbers.
     
     <P>
     <B>Date:</B>         12-29-2000
     
     <P>
     <B>Test level:</B>   0
     
     <P>
     This class contains code that will tokenize an arithmetic
     expression.  It recognizes function calls, variables,
     constants and operators.  But all it produces is a simple list
     of Token objects.  It does not do any precedence calculations.
     
     <P>
     A number can contain one decimal point.  No commas
     are allowed.  The decimal points may be the first or last
     character.
     
     <P>
     <B>Notes:   </B>
     <P>
     The tokenize() function is made public merely for your convenience.
     Normally, it is used internally by parse2().
     For tokenize(), the tokens do not have to be separated
     by blanks.  The only tokens are numbers and alphameric ids, and of
     course single character tokens such as parentheses and operators.
**/

import java.util.*;

public class TokenList {

     public Token[] tokens;
     public int numtokens;
     NameDB nameDatabase;

     public TokenList() {
          numtokens = 0;
     }

     public TokenList(NameDB ndb) {
          numtokens = 0;
          nameDatabase = ndb;
     }

     public TokenList(String s) {
          tokens = tokenize(s);
          numtokens = tokens.length;
          unparenthesize();
          changeFuncalls();
     }

     public TokenList(String s, NameDB ndb) {
          nameDatabase = ndb;
          tokens = tokenize(s);
          numtokens = tokens.length;
          unparenthesize();
          changeFuncalls();
     }

     public Object clone() {
          TokenList newtl = new TokenList(nameDatabase);
          newtl.tokens = new Token[tokens.length];
          newtl.numtokens = numtokens;
          for (int i=0; i<tokens.length; i++) 
               if (tokens[i] != null)
                    newtl.tokens[i] = (Token)tokens[i].clone();
          return newtl;
     }

     public void setDB (NameDB nameDatabase) {
          this.nameDatabase = nameDatabase;
     }

     public int length() {
          return numtokens;
     }

     public void add (Token token) {
          if (numtokens == 0) {
               tokens = new Token[1];
               tokens[0] = token;
               numtokens = 1;
               return;
          }
          if (numtokens == tokens.length) {
               Token[] newtokens = new Token[tokens.length+5];
               for (int i=0; i<tokens.length; i++)
                    newtokens[i] = tokens[i];
               tokens = newtokens;
          }
          tokens[numtokens++] = token;
     }

     /**
      *  This breaks up a string into raw tokens based upon whether they
      *  are numbers, names or special punctuation.
      *  For instance, it considers 47382.53 to be one token, as well as
      *  "joesmith" but a single parenthesis would be its own token.
      *  It knows nothing about operators or precedence.
      *  If a sequence of alphanumerics is preceded by a dollar sign, it
      *  is treated as one token.
      *  In the example below, $abc could have been replaced by just "abc."
      *
      *  @param string
      *       the input String which is a sequence of characters to be
      *       broken up into tokens.
      *       This string should end in a dollar sign but if it does not,
      *       one is appended to it.  A dollar sign is the signal that
      *       the token stream is at an end.
      *  @return
      *       an array of tokens (Token[]), each of which holds one token.
      *  @exception 
      *       none
      *  @examples
      *       "5*($abc-x+537.2)" ==> 5 * ( $abc - x + 5372 )
      */

     public static Token[] tokenize (String string) {
          Token[] tokens;
          int numtokens = 0;
          int maxtokens = 25;
          tokens = new Token[maxtokens];
          string += " ";


          string = metaFix(string);

          string += "  ";

          String temp = "";
          int i=0;
          while (i < string.length()) {
               char ch = string.charAt(i);
               if (ch == ' ') {
                    i++;
               }
               else if (isnumber(ch)) {
                    while (isnumber(ch)) {
                         temp += ch;
                         i++;
                         if (i >= string.length()) break;
                         ch = string.charAt(i);
                    }
                    if (temp.indexOf('.') > -1) {
                         if (Token.validateReal(temp))
                              tokens[numtokens++] = new Token(Token.REAL, temp);
                         else
                              tokens[numtokens++] = new Token(Token.ERROR, 0);
                    }
                    else
                         tokens[numtokens++] = new Token(Token.INT, temp);
                    temp = "";
               }
               else if (isalpha(ch) || ch == '$') {
                    temp = ch + "";
                    i++;
                    ch = string.charAt(i);
                    while (alphanumeric(ch)) {
                         temp += ch;
                         i++;
                         if (i >= string.length()) break;
                         ch = string.charAt(i);
                    }
                    tokens[numtokens++] = new Token(Token.ID, temp);
                    temp = "";
               }
               else if (ch == '"') {
                    temp = "";
                    i++;
                    ch = string.charAt(i);
                    while (ch != '"') {
                         temp += ch;
                         i++;
                         ch = string.charAt(i);
                    }
                    i++;              // ignore the second double quote
                    tokens[numtokens++] = new Token(Token.STRING, temp);
                    temp = "";
               }
               else if (ch == '(') {
                    tokens[numtokens++] = new Token(Token.LPAREN, "(");
                    i++;
               }
               else if (ch == ')') {
                    tokens[numtokens++] = new Token(Token.RPAREN, ")");
                    i++;
               }
               else if (ch == ',') {
                    tokens[numtokens++] = new Token(Token.COMMA, ",");
                    i++;
               }
               else if (ch == ';') {
                    tokens[numtokens++] = new Token(Token.SEMICOLON, ";");
                    i++;
               }
               else if (ch == '=') {
                    if (string.charAt(i+1) == '=') {
                         tokens[numtokens++] = new Token(Token.OP, "==");
                         i+=2;
                    }
                    else {
                         tokens[numtokens++] = new Token(Token.EQUAL, "=");
                         i++;
                    }
               }
               else if (ch == '!') {
                    if (string.charAt(i+1) == '=') {
                         tokens[numtokens++] = new Token(Token.OP, "!=");
                         i+=2;
                    }
                    else {
                         tokens[numtokens++] = new Token(Token.UNKNOWN, "!");
                         i++;
                    }
               }
               else if (ch == '<') {
                    if (string.charAt(i+1) == '=') {
                         tokens[numtokens++] = new Token(Token.OP, "<=");
                         i+=2;
                    }
                    else {
                         tokens[numtokens++] = new Token(Token.OP, "<");
                         i++;
                    }
               }
               else if (ch == '>') {
                    if (string.charAt(i+1) == '=') {
                         tokens[numtokens++] = new Token(Token.OP, ">=");
                         i+=2;
                    }
                    else {
                         tokens[numtokens++] = new Token(Token.OP, ">");
                         i++;
                    }
               }
               else if (ch == '=') {
                    tokens[numtokens++] = new Token(Token.EQUAL, "=");
                    i++;
               }
               else if (isoperator(ch)) {
                    tokens[numtokens++] = new Token(Token.OP, ch+"");
                    i++;
               }
               else {     // any other single character is a token by itself
                    tokens[numtokens++] = new Token(Token.UNKNOWN, ch+"");
                    i++;
               }
               if (numtokens == maxtokens) {
                    int newmaxtokens = maxtokens + 20;
                    Token[] temptokens = new Token[newmaxtokens];
                    for (int k=0; k<maxtokens; k++)
                         temptokens[k] = tokens[k];
                    tokens = temptokens;
                    maxtokens = newmaxtokens;
               }
          }
          Token[] rettokens = new Token[numtokens];
          for (i=0; i<numtokens; i++)
               rettokens[i] = tokens[i];
          return rettokens;
     }

     private static boolean isnumber (char ch) {
          return (ch >= '0' && ch <= '9' || ch == '.');
     }
     
     private static boolean isalpha (char ch) {
          return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') 
                  || ch == '_';
     }
     
     private static boolean alphanumeric (char ch) {
          return (isalpha(ch) || (ch>='0' && ch<='9'));
     }

     private static boolean isoperator (char ch) {
          return ch == '+' || ch == '-' || ch == '*' ||
                 ch == '/' || ch == '%' || ch == '^' || ch == '#';
     }

     //-------------------------------------------------------------------
     //  This method searches for all left parentheses in a list of tokens.
     //  When it finds one left paren, it scans forward to find its match.
     //  It then takes that sequence of tokens between the parentheses,
     //  unparenthesizes it (this is the recursive step), and replaces it
     //  in the original list with a token called SUBEXP that contains the
     //  list of tokens, sans parentheses.
     //
     //  If there was an error, such as a missing right parenthesis, this
     //  method returns -1.  Otherwise, it returns 0.
     //  A nasty complication is that the list might be empty, consisting 
     //  only of ().
     //-------------------------------------------------------------------

     private int unparenthesize () {
          if (numtokens == 0) return 0;
          int leftpos = find(Token.LPAREN);
          while (leftpos != -1) {
               int rightpos = findMatchingRightParen(leftpos);
               if (rightpos == -1)
                    return -1;
               TokenList sublist;
               if (rightpos == leftpos+1) {
                    sublist = new TokenList(nameDatabase);
                    removeTokens (leftpos+1, rightpos);
               }
               else {
                    sublist = extractSublist(leftpos+1, rightpos-1); 
                    removeTokens (leftpos+1, rightpos);
                    sublist.unparenthesize();
               }
               Token t = new Token(Token.SUBEXP, sublist);
               tokens[leftpos] = t;
               leftpos = find(Token.LPAREN);
          }
          return 0;
     }

     private int find (int tokentype) {
          return find(tokentype, 0);
     }

     private int find (int tokentype, int startpos) {
          for (int i=startpos; i<numtokens; i++)
               if (tokens[i].type == tokentype)
                    return i;
          return -1;
     }

     //-------------------------------------------------------------------
     //  Starting at index "pos" in the array tokens, find the next
     //  matching right parenthesis.  tokens[pos] must be a left paren token.
     //  This method skips over embedded parentheses.
     //  It returns the position of the matching right parenthesis.
     //  If the tokens ran out before finding a matching right paren,
     //  -1 is returned as an error signal.
     //  This method does not check to see if tokens[pos] is a left paren
     //  at the outset; it trusts the caller.
     //-------------------------------------------------------------------

     private int findMatchingRightParen (int pos) {
          int count = 1;         // count number of left parens
          pos++;
          while (count > 0 && pos < numtokens) {
               if (tokens[pos].type == Token.LPAREN)
                    count++;
               else if (tokens[pos].type == Token.RPAREN)
                    count--;
               pos++;
          }
          if (pos > numtokens)
               return -1;
          return pos - 1;
     }

     private TokenList extractSublist (int begin, int end) {
          if (begin >= numtokens || end >= numtokens) return null;
          if (begin > end) return null;
          if (begin < 0 || end < 0) return null;
          TokenList newtl = new TokenList(nameDatabase);
          for (int i=begin; i<end+1; i++)
               newtl.add(tokens[i]);
          return newtl;
     }

     private void removeTokens (int begin, int end) {
          if (begin >= numtokens || end >= numtokens) return;
          if (begin > end) return;
          if (begin < 0 || end < 0) return;
          int j = begin;
          for (int i=end+1; i<numtokens; i++) {
               tokens[j++] = tokens[i];
          }
          while (j < numtokens)
               tokens[j++] = null;
          numtokens -= end-begin+1;
     }

/*  The old version
     private void changeFuncalls() {
          if (numtokens == 0) return;
          int startpos = 0;
          while (startpos < numtokens) {
               int leftpos = find(Token.ID, startpos);
               if (leftpos == -1) return;
               if (leftpos == numtokens-1) return;
               if (tokens[leftpos+1].type == Token.SUBEXP) {
                    tokens[leftpos+1].sublist.changeFuncalls();
                    Token t = new Token(Token.FUNCALL, tokens[leftpos].value,
                                        tokens[leftpos+1].sublist);
                    tokens[leftpos] = t;
                    removeTokens(leftpos+1,leftpos+1);
               }
               startpos++;
          }
          return;
     }
*/
     private void changeFuncalls() {
          if (numtokens == 0) return;
          int startpos = 0;
          while (startpos < numtokens) {
               if (tokens[startpos].type == Token.SUBEXP) {
                    tokens[startpos].sublist.changeFuncalls();

                    if (startpos > 0 && tokens[startpos-1].type == Token.ID) {
                         Token t = new Token(Token.FUNCALL, 
                                             tokens[startpos-1].value,
                                             tokens[startpos].sublist);
                         tokens[startpos-1] = t;
                         removeTokens(startpos, startpos);
                    }
               }
               startpos++;
          }
     }

     public String toString () {
          String result = "";
          for (int i=0; i<numtokens; i++)
               result += tokens[i].toString() + "  ";
          return result;
     }

     private static String metaFix (String s) {
          String retval = "";
          String unprotected = "";
          int i = 0;
          while (i < s.length()) {
               char ch = s.charAt(i);
               if (ch == '"') {
                    char ch2 = ' ';       // just keeps Java from complaining
                    retval += fixAll(unprotected);
                    unprotected = "";
                    i++;
                    String safe = ""+ch;
                    while (i < s.length()) {
                         ch2 = s.charAt(i);
                         if (ch2 == '"') {
//                            i++;
                              break;
                         }
                         else
                              safe += ch2;
                         i++;
                    }
                    retval += safe + ch2;
               }
               else
                    unprotected += ch;
               i++;
          }
          return retval + fixAll(unprotected);
     }

     private static String fixAll (String s) {
          return replaceUnaryMinus(replaceSquareBrackets(replaceSpecialOps(s)));
     }

     private static String replaceSpecialOps (String s) {
          while (true) {
               int pos = s.indexOf("++");
               if (pos == -1)
                    break;
               s = replaceOneOp (s, "++", pos);
          }
          while (true) {
               int pos = s.indexOf("--");
               if (pos == -1)
                    break;
               s = replaceOneOp (s, "--", pos);
          }
          return s;
     }

     private static String replaceOneOp (String s, String op, int pos) {
          String operation = "+1";
          if (op.equals("--"))
               operation = "-1";
          if (Character.isLetterOrDigit(s.charAt(pos+2))) {
               int k = pos+2;
               String varname = "";
               char ch = s.charAt(k);
               while (Character.isLetterOrDigit(ch)) {
                    varname += ch;
                    k++;
                    ch = s.charAt(k);
               }
               String part1 = s.substring(0,pos);
               String part2 = s.substring(k);
               s = part1 + "("+varname+"="+varname+operation+")" + part2;
          }
          else {
               int k = pos-1;
               String varname = "";
               char ch = s.charAt(k);
               while (Character.isLetterOrDigit(ch)) {
                    varname = ch + varname;
                    k--;
                    ch = s.charAt(k);
               }
               String part1 = s.substring(0,k);
               String part2 = s.substring(pos+2);
               s = part1 + "("+varname+"="+varname+operation+")" + part2;
          }
          return s;
     }

     private static String replaceSquareBrackets (String s) {
          while (true) {
               int pos = s.indexOf('[');
               if (pos == -1) return s;

               // get the preceding identifier.  There may be spaces
               // between it and the left square bracket, such as 
               //    xyz32   [ ...

               String id = "";
               int i=pos-1;
               while (s.charAt(i) == ' ')
                    i--;

               char ch = s.charAt(i);
               while (Character.isLetterOrDigit(ch)) {
                    id = ch + id;
                    i--;
                    if (i < 0) break;
                    ch = s.charAt(i);
               }

/*
               String part1 = s.substring(0,i+1);
               String part2 = s.substring(pos+1);
               String[] ret = Parser.findMatchingRightElement(part2,'[',']');
               if (ret[1].charAt(0) != '.')
                    s = part1 + "(" + id + "#(" + ret[0] + "))" + ret[1];

               // Now we have a table reference, such as  db[i].name
               // so we convert it to a function call to gettable.
               // Later, when TokenList sees a call like
               //     gettable(....) = 
               // It will convert that to settable(...) = 
               // since there is no way to know know if this will appear on
               // the LHS or RHS.

               else {
                    String fieldname = "";          
                    String rest = ret[1];
                    rest = rest.substring(1);   // ignore the period
                    i = 0;                  
                    ch = rest.charAt(i);
                    while (Character.isLetterOrDigit(ch)) {
                         fieldname += ch;
                         i++;
                         if (i >= rest.length()) break;
                         ch = rest.charAt(i); 
                    }
                    rest = rest.substring(fieldname.length());
                    s = part1 + "gettable(\""+id+"\","+ret[0]+",\""+fieldname+
                                "\")" + rest;
               }
*/
          }
     }

     //--------------------------------------------------------------------
     /**
      *   replaceUnaryMinus
      *
      *   There are only a few places where a unary minus must be "fixed."
      *   The fix is to insert 0 in front of the unary minus:
      *
      *       -5        at the beginning of the expression
      *       (-5       after a left parenthesis
      *       *-5       after an operator
      *       =         after a assignment operator (isOp() doesn't catch this)
      *       ,-5       after a comma (in a parameter list)
      *
      *   It doesn't matter if the - precedes a number, variable or
      *   parenthesized expression.
      *   What about if this is a string??????  "  - 6"
     **/
     //--------------------------------------------------------------------

     private static String replaceUnaryMinus (String s) {
          s = s.trim();
          String frontOK = "";

          while (true) {
               int pos = s.indexOf('-');
               if (pos == -1) 
                    return frontOK + s;

               if (pos == 0) {
                    frontOK += "0 -";
                    s = s.substring(1);
               }
               else {
                    int i=pos-1;                    
                    // skip in front of blanks
                    while (s.charAt(i) == ' ')
                         i--;

                    char ch = s.charAt(i);
                    if (ch == ',' || ch == '=' || ch == '(' || isOp(ch+"")) {
                         frontOK += s.substring(0,i+1) + "0 -";
                         s = s.substring(pos+1);
                    }
                    else {
                         frontOK += s.substring(0,i+1) + "-";
                         s = s.substring(pos+1);
                    }
               }
          }
     }

     //=======================================================
     //   EVALUATION METHODS
     //=======================================================

     public double doubleEval() {
          Token result = evaluate();
          if (result.type == Token.INT)
                return result.intval + 0.0;
          if (result.type == Token.REAL)
                return result.doubleval;
          return 0.0;
     }

     public int intEval() {
          Token result = evaluate();
          if (result.type == Token.INT)
                return result.intval;
          if (result.type == Token.REAL)
                return (int)result.doubleval;
          return 0;
     }

     public Token evaluate () {
          if (numtokens == 0) 
               return new Token (Token.INT, 0);
          Token[] stack = new Token[numtokens+20];
          int nexttoken = 0;
          add (new Token(Token.ENDOFINPUT, 0));
          int top = 0;
          stack[0] = new Token(Token.STACKBOT, 0);

          while (true) {
               if (nexttoken >= numtokens) break;
               if (tokens[nexttoken].type == Token.ENDOFINPUT) break;
               if (tokens[nexttoken].type == Token.COMMA) {
                    top = 0;
                    stack[0] = new Token(Token.STACKBOT, 0);
                    nexttoken++;
                    continue;
               }

               if (tokens[nexttoken].type == Token.SUBEXP) {
                    tokens[nexttoken] = tokens[nexttoken].sublist.evaluate();
                    continue;
               }
               else if (tokens[nexttoken].type == Token.FUNCALL &&
                        nexttoken+1 < numtokens &&
                        tokens[nexttoken+1].type != Token.EQUAL) {
                    tokens[nexttoken] = functionEval(tokens[nexttoken].value,
                                                     tokens[nexttoken].sublist);
                    continue;
               }
               else if (tokens[nexttoken].type == Token.FUNCALL &&
                        nexttoken+1 >= numtokens) {
                    tokens[nexttoken] = functionEval(tokens[nexttoken].value,
                                                     tokens[nexttoken].sublist);
                    continue;
               }

               if (!expressOK(stack[top].type, tokens[nexttoken].type)) {
                    System.out.println ("Illegal expression: stack top="+
                              stack[top]+"  next token="+tokens[nexttoken]);
                    return new Token (Token.ERROR, 0);
               }

               if (top == 0 || tokens[nexttoken].type == Token.ID ||
                   tokens[nexttoken].type == Token.ARRAYREF ||
                   Token.isData(tokens[nexttoken].type) ||
                   mustpush(stack[top-1], tokens[nexttoken])) {
                    stack[++top] = tokens[nexttoken++];
               }
               else {
                    Token temp = compute(stack[top-2],stack[top-1],stack[top]);
                    top -= 3;
                    stack[++top] = temp;
               }
               //printstack(stack, top);
          }
          if (top == 1) {
               if (stack[1].type == Token.ID)
                    stack[1] = resolveVariable(stack[1]);
               return stack[1];
          }
          while (top > 1) {
               Token temp = compute(stack[top-2], stack[top-1], stack[top]);
               top -= 3;
               stack[++top] = temp;
               //printstack(stack, top);
          }
          return stack[top];
     }

     //------------------------------------------------------------
     // evaluate the parameters (separated by commas) and send them
     // out as a Vector
     //------------------------------------------------------------

     private Vector evaluateParameters (TokenList params) {
          Vector parameters = new Vector();    
          if (params.length() > 0) {
               while (true) {
                    int pos = params.find(Token.COMMA);
                    if (pos == -1) break;
                    TokenList temp = params.extractSublist(0, pos-1);
                    params.removeTokens(0, pos);
                    Token temptok = temp.evaluate();
                    if (temptok.type == Token.ARRAYREF)
                         temptok = getArrayValue(temptok);
                    parameters.addElement (temptok);
               }
               Token temptok = params.evaluate();
               if (temptok.type == Token.ARRAYREF) 
                    temptok = getArrayValue(temptok);
               parameters.addElement (temptok);
          }
          return parameters;
     }

     //------------------------------------------------------------
     // evaluate the parameters (separated by commas)
     // then test the function name
     //------------------------------------------------------------

     private Token functionEval (String funcname, TokenList params) {
          Vector parameters = new Vector();    
          if (params.length() > 0) {
               while (true) {
                    int pos = params.find(Token.COMMA);
                    if (pos == -1) break;
                    TokenList temp = params.extractSublist(0, pos-1);
                    params.removeTokens(0, pos);
                    Token temptok = temp.evaluate();
                    if (temptok.type == Token.ARRAYREF)
                         temptok = getArrayValue(temptok);
                    parameters.addElement (temptok);
               }
               Token temptok = params.evaluate();
               if (temptok.type == Token.ARRAYREF) 
                    temptok = getArrayValue(temptok);
               parameters.addElement (temptok);
          }
          Token temp = Functions.evaluate(funcname, parameters, nameDatabase);
//        Token temp = new Token(Token.INT, 0);
          return temp;
     }

     private Token compute (Token token1, Token operator, Token token2) {

          Token t1 = token1;
          Token t2 = token2;

          if (operator.type == Token.EQUAL) {
               if (token1.type != Token.ID && token1.type != Token.ARRAYREF && 
                   token1.type != Token.FUNCALL)
                    return new Token (Token.ERROR, 0);
               if (nameDatabase == null)
                    return new Token (Token.ERROR, 0);
               if (token2.type == Token.ID)
                    token2 = resolveVariable(token2);
               if (token2.type == Token.ARRAYREF)
                    token2 = getArrayValue(token2);
//NEWMAR15
               if (token1.type == Token.FUNCALL) {
                    Vector parameters = evaluateParameters(token1.sublist);

                    if (parameters.size() == 4) 
                         parameters.removeElementAt(3);
                    parameters.addElement(token2);

                    if (!token1.value.equals("settable") &&
                        !token1.value.equals("gettable"))
                          System.out.println ("ERROR: only settable may be on LHS");
                   
                    Functions.evaluate("settable", parameters, nameDatabase);
                    return token2;
               }
//END
               if (token1.type == Token.ARRAYREF)
                    setArrayValue (token1, token2);
               else
                    nameDatabase.set(token1.value, token2);
               return token2;
          }

          if (operator.value.equals("#")) {
               if (token1.type != Token.ID)
                    return new Token (Token.ERROR, 0);
               if (nameDatabase == null)
                    return new Token (Token.ERROR, 0);
               if (token2.type == Token.ID)
                    token2 = resolveVariable(token2);
               
               while (token2.type == Token.ARRAYREF)
                    token2 = getArrayValue(token2);

               return new Token (Token.ARRAYREF, token1.value, token2, true);
          }

          if (token1.type == Token.ID)
               t1 = resolveVariable(token1);
          if (token2.type == Token.ID)
               t2 = resolveVariable(token2);
          if (token1.type == Token.ARRAYREF) 
               t1 = getArrayValue(token1);
          if (token2.type == Token.ARRAYREF)
               t2 = getArrayValue(token2);

          if (t1.type == Token.INT && t2.type == Token.INT) {
               int result = 0;
               int t1val = t1.intval;
               int t2val = t2.intval;
               if (operator.value.equals("+"))
                    result = t1val + t2val;
               else if (operator.value.equals("-"))
                    result = t1val - t2val;
               else if (operator.value.equals("*"))
                    result = t1val * t2val;
               else if (operator.value.equals("/"))
                    result = t1val / t2val;
               else if (operator.value.equals("%"))
                    result = t1val % t2val;
               else if (operator.value.equals("^"))
                    result = exponentiate(t1val, t2val);
               else if (operator.value.equals("==")) {
                    if (t1val == t2val)
                         result = 1;
                    else
                         result = 0;
               }
               else if (operator.value.equals("!=")) {
                    if (t1val != t2val)
                         result = 1;
                    else
                         result = 0;
               }
               else if (operator.value.equals("<")) {
                    if (t1val < t2val)
                         result = 1;
                    else
                         result = 0;
               }
               else if (operator.value.equals("<=")) {
                    if (t1val <= t2val)
                         result = 1;
                    else
                         result = 0;
               }
               else if (operator.value.equals(">")) {
                    if (t1val > t2val)
                         result = 1;
                    else
                         result = 0;
               }
               else if (operator.value.equals(">=")) {
                    if (t1val >= t2val)
                         result = 1;
                    else
                         result = 0;
               }
               return new Token (Token.INT, result);
          }
          else if (t1.type == Token.STRING && Token.isNumber(t2.type)) {
               if (operator.value.equals("+")) {
                    String result = t1.value;
                    if (t2.type == Token.INT)
                         result += t2.intval;
                    else
                         result += t2.doubleval;
                    return new Token (Token.STRING, result);
               }
               else {
                    return new Token (Token.ERROR, "Illegal string operator");
               }
          }
          else if (t2.type == Token.STRING && Token.isNumber(t1.type)) {
               if (operator.value.equals("+")) {
                    String result = t2.value;
                    if (t1.type == Token.INT)
                         result = t1.intval + result;
                    else
                         result = t1.doubleval + result;
                    return new Token (Token.STRING, result);
               }
               else 
                    return new Token (Token.ERROR, "Illegal string operator");
          }
          else if (t1.type == Token.STRING && t2.type == Token.STRING) {
               if (operator.value.equals("+")) 
                    return new Token (Token.STRING, t1.value + t2.value);
               else if (TokenList.isRelOp(operator.value)) {
                    int comparison = t1.value.compareTo(t2.value);
                    int result = 0;
                    if (operator.value.equals("==") && comparison == 0)
                         result = 1;
                    if (operator.value.equals("!=") && comparison != 0)
                         result = 1;
                    if (operator.value.equals("<") && comparison < 0)
                         result = 1;
                    if (operator.value.equals("<=") && comparison <= 0)
                         result = 1;
                    if (operator.value.equals(">") && comparison > 0)
                         result = 1;
                    if (operator.value.equals(">=") && comparison >= 0)
                         result = 1;
                    return new Token (Token.INT, result);
               }
               else 
                    return new Token (Token.ERROR, "Illegal string operator");
          }
          else if (t1.type == Token.REAL || t2.type == Token.REAL) {
               double result = 0;
               double t1val = t1.doubleval;
               double t2val = t2.doubleval;

               if (t1.type == Token.INT)
                    t1val = t1.intval;
               if (t2.type == Token.INT)
                    t2val = t2.intval;

               if (operator.value.equals("+"))
                    result = t1val + t2val;
               else if (operator.value.equals("-"))
                    result = t1val - t2val;
               else if (operator.value.equals("*"))
                    result = t1val * t2val;
               else if (operator.value.equals("/"))
                    result = t1val / t2val;
               else if (operator.value.equals("%"))
                    result = t1val % t2val;
               else if (operator.value.equals("^"))
                    result = Math.pow(t1val, t2val);
               else if (operator.value.equals("==")) {
                    if (t1val == t2val)
                         result = 1;
                    else
                         result = 0;
               }
               else if (operator.value.equals("!=")) {
                    if (t1val != t2val)
                         result = 1;
                    else
                         result = 0;
               }
               else if (operator.value.equals("<")) {
                    if (t1val < t2val)
                         result = 1;
                    else
                         result = 0;
               }
               else if (operator.value.equals("<=")) {
                    if (t1val <= t2val)
                         result = 1;
                    else
                         result = 0;
               }
               else if (operator.value.equals(">")) {
                    if (t1val > t2val)
                         result = 1;
                    else
                         result = 0;
               }
               else if (operator.value.equals(">=")) {
                    if (t1val >= t2val)
                         result = 1;
                    else
                         result = 0;
               }
               return new Token (Token.REAL, result);
          }
          return new Token (Token.ERROR, "Error in TokenList.compute()");
     }

     private void setArrayValue (Token token1, Token token2) {
          String varname = token1.value;
          Token theList = nameDatabase.get(varname);
          if (theList.type != Token.LIST)
               return;
          int index = token1.indexValue.intval;

          if (index < 0)
               return;

          while (index >= theList.sublist.numtokens) 
               theList.sublist.add(new Token(Token.INT, 0));
         
          theList.sublist.tokens[index] = token2;
     }

     private Token getArrayValue (Token token1) {
          String varname = token1.value;
          Token theList = nameDatabase.get(varname);
          if (theList.type != Token.LIST)
               return new Token (Token.ERROR, 0);
          int index = token1.indexValue.intval;

          if (index < 0)
               return new Token (Token.ERROR, "Index out of bounds.");
          if (index >= theList.sublist.numtokens)
               return new Token (Token.INT, 0);

          return theList.sublist.tokens[index];
     }

     public static Token dereference (Token tok, NameDB ndb) {
          if (tok.type != Token.ARRAYREF)
               return new Token (Token.ERROR, "Asked to dereference a non L-value");
          String varname = tok.value;
          Token theList = ndb.get(varname);
          if (theList.type != Token.LIST)
               return new Token (Token.ERROR, 0);
          int index = tok.indexValue.intval;

          if (index < 0)
               return new Token (Token.ERROR, "Index out of bounds.");
          if (index >= theList.sublist.numtokens)
               return new Token (Token.INT, 0);

          return theList.sublist.tokens[index];
     }

     private Token resolveVariable (Token t) {
          if (nameDatabase == null)
               return new Token(Token.INT, 0);
          else {
               String varname = t.value;
               if (!nameDatabase.nameInUse(varname)) 
                    return new Token(Token.INT, 0);

               return nameDatabase.get(varname);
          }
     }

     private boolean expressOK(int stacktoptype, int nexttokentype) {
          boolean retval = false;

/*
          System.out.print ("in expressOK, stacktoptype = "+stacktoptype);
          if (Token.isData(stacktoptype))
               System.out.print (" (isdata) ");
          System.out.print ("   nexttokentype = "+nexttokentype);
          if (Token.isData(nexttokentype))
               System.out.print (" (isdata) ");
          System.out.println();
*/

          if (stacktoptype == Token.STACKBOT) {
               retval = true;
          }
          else if (Token.isData(stacktoptype) || stacktoptype == Token.ID ||
                   stacktoptype == Token.ARRAYREF) {
               if (nexttokentype == Token.OP || nexttokentype == Token.EQUAL)
                    retval = true;
          }
          else if (stacktoptype == Token.FUNCALL && nexttokentype == Token.EQUAL)
               retval = true;
          else if (stacktoptype == Token.OP) {
               if (Token.isData(nexttokentype) || nexttokentype == Token.ID ||
                   nexttokentype == Token.ARRAYREF)
                    retval = true;
          }
          else if (stacktoptype == Token.EQUAL) {
               if (Token.isData(nexttokentype) || nexttokentype == Token.ID ||
                   nexttokentype == Token.ARRAYREF)
                    retval = true;
          }
          return retval;
     }
     
     private int exponentiate (int val1, int val2) {
          int result = 1;
          for (int i=0; i<val2; i++)
               result *= val1;
          return result;
     }

     //------------------------------------------------------------------
     // This is where all the precedence is built into.  
     //    t1 : this is the token that is on top of the stack
     //    t2 : this is the incoming token from the token input stream
     //
     // If t1 < t2 in terms of precedence, we must push.  So this method
     // returns true and the caller pushes t2 onto the stack.
     // For instance, suppose + is on top of the stack and * is incoming.
     // Then we would push *.
     //------------------------------------------------------------------

     private boolean mustpush (Token t1, Token t2) {
          if (t1.type == Token.STACKBOT)
               return true;
          if (t1.type == Token.EQUAL)
               return true;
          if (t1.type == Token.FUNCALL && t2.type == Token.EQUAL)
               return true;
          if (t1.type == Token.INT || t1.type == Token.REAL || 
              t1.type == Token.ID || t1.type == Token.ARRAYREF)
               return true;

          if (isOp(t1.value) && isOp(t2.value)) {
               int level1 = getLevel(t1.value);
               int level2 = getLevel(t2.value);
               if (level1 < level2)
                    return true;
               else if (t1.value.equals("^") && t2.value.equals("^"))
                    return true;
               else if (t1.value.equals("#") && t2.value.equals("#"))
                    return true;
               return false;
          }

/*
          if (t1.value.equals("+") || t1.value.equals("-")) {
               return (t2.value.equals("%") || t2.value.equals("*") ||
                       t2.value.equals("/") || t2.value.equals("^"));
          }
          if (t1.value.equals("*") || t1.value.equals("/") ||
              t1.value.equals("%")) {
               return (t2.value.equals("^"));
          }
          if (t1.value.equals("^"))
               return (t2.value.equals("^"));
*/
          return false;
     }

     private int getLevel (String tokenValue) {
          if (isRelOp(tokenValue))
               return 0;
          if (isAddOp(tokenValue))
               return 1;
          if (isMulOp(tokenValue))
               return 2;
          if (tokenValue.equals("^"))
               return 3;
          if (tokenValue.equals("#"))   // array indexing operator
               return 4;
          return -1;
     }

     public static boolean isOp (String tokenValue) {
          return isRelOp(tokenValue) || isAddOp(tokenValue) ||
                 isMulOp(tokenValue) || tokenValue.equals("^");
     }

     public static boolean isRelOp (String tokenValue) {
          return tokenValue.equals("==") || tokenValue.equals("!=") ||
                 tokenValue.equals("<") || tokenValue.equals(">") ||
                 tokenValue.equals("<=") || tokenValue.equals(">=");
     }
     
     public static boolean isAddOp (String tokenValue) {
          return tokenValue.equals("+") || tokenValue.equals("-");
     }
     
     public static boolean isMulOp (String tokenValue) {
          return tokenValue.equals("*") || tokenValue.equals("/") ||
                 tokenValue.equals("%");
     }


     //DEBUG ONLY----------------------------------------------
     private void printstack (Token[] stack, int top) {
          System.out.println ("============S T A C K==========");
          System.out.println ("top="+top);
          for (int i=top; i>=0; i--)
               System.out.println (stack[i].toString());
          System.out.println ("===============================");
     }
     //--------------------------------------------------------

}
