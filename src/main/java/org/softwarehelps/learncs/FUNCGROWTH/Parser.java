//--------------------------------------------------------------------
//  Parser.java
//
//  This class takes a String and breaks it into program elements,
//  creating a ProgElement structure.  It does not evaluate them.
//  It uses a crude recursive descent parser.  The actual expressions
//  and conditions are left unparsed because there is an operator
//  precedence parser and evaluate in TokenList.java.
//--------------------------------------------------------------------

import java.util.*;
import java.io.*;

public class Parser {
     public ProgElement result;

     public Parser (String prog) {
          prog = fix(prog);
          result = parse(prog);
     }

     private ProgElement parse (String prog) {
          Object[] parseResult;
          parseResult = parseSequence(prog);
          return (ProgElement)parseResult[0];
     }

     Object[] parseSequence (String code) {
          String[] results;
          ProgElement pe = new ProgElement ("sequence", "");
          Object[] parseResult;

          while (code.length() > 0) {
               code = code.trim();
               if (code.startsWith("if")) {
                    parseResult = parseIf(code);
                    pe.addToBody((ProgElement)parseResult[0]);
                    code = (String)parseResult[1];
               }
               else if (code.startsWith("while")) {
                    parseResult = parseWhile(code);
                    pe.addToBody((ProgElement)parseResult[0]);
                    code = (String)parseResult[1];
               }
               else if (code.startsWith("repeat")) {
                    parseResult = parseRepeat(code);
                    pe.addToBody((ProgElement)parseResult[0]);
                    code = (String)parseResult[1];
               }
               else if (code.startsWith("for")) {
                    parseResult = parseFor(code);
                    pe.addToBody((ProgElement)parseResult[0]);
                    code = (String)parseResult[1];
               }
               else if (code.startsWith("define")) {
                    parseResult = parseDefine(code);
                    pe.addToBody((ProgElement)parseResult[0]);
                    code = (String)parseResult[1];
               }
               else if (code.startsWith("include")) {
                    parseResult = findNextSemicolon(code);
                    String filename = (String)parseResult[0];
                    String substitution = fix(includeFile(filename));
                    code = substitution + (String)parseResult[1];
               }
               else {
                    parseResult = findNextSemicolon(code);

                    pe.addToBody (new ProgElement("statement", 
                                            (String)parseResult[0]));
                    code = (String)parseResult[1];
               }
          }
          return new Object[] {pe, code};
     }

     Object[] parseWhile (String code) {
          // code starts with "while" so ignore it and get the
          // condition (which is in parentheses)

          code = code.substring(5).trim();
          if (!code.startsWith("(")) {
               //this is an error, how do we deal with this?
               return new String[]{"ERROR", code};
          }

          code = code.substring(1);      // chop off leading "("
          String[] retval = findMatchingRightElement(code, '(', ')');

          ProgElement pe = new ProgElement ("while", retval[0]);
          code = retval[1].trim();

          if (code.startsWith("{")) {
               code = code.substring(1);      // chop off leading "{"
               retval = findMatchingRightElement (code, '{', '}');
               pe.addToBody ((ProgElement)(parseSequence(retval[0])[0]));
          }
          else {
               retval = findNextSemicolon (code);
               pe.addToBody (new ProgElement("statement",(String)retval[0]));
          }
          
          return new Object[] {pe, retval[1]};
     }

     Object[] parseRepeat (String code) {
          // code starts with "repeat" so ignore it and get the
          // repetition (which is in parentheses)

          code = code.substring(6).trim();
          if (!code.startsWith("(")) {
               //this is an error, how do we deal with this?
               return new String[]{"ERROR", code};
          }

          code = code.substring(1);      // chop off leading "("
          String[] retval = findMatchingRightElement(code, '(', ')');

          ProgElement pe = new ProgElement ("repeat", retval[0]);
          code = retval[1].trim();

          if (code.startsWith("{")) {
               code = code.substring(1);      // chop off leading "{"
               retval = findMatchingRightElement (code, '{', '}');
               pe.addToBody ((ProgElement)(parseSequence(retval[0])[0]));
          }
          else {
               retval = findNextSemicolon (code);
               pe.addToBody (new ProgElement("statement",(String)retval[0]));
          }
          
          return new Object[] {pe, retval[1]};
     }

     Object[] parseFor (String code) {
          // code starts with "for" so ignore it and get the
          // condition (which is in parentheses)

          code = code.substring(3).trim();
          if (!code.startsWith("(")) {
               //this is an error, how do we deal with this?
               return new String[]{"ERROR", code};
          }

          code = code.substring(1);      // chop off leading "("
          String[] retval = findMatchingRightElement(code, '(', ')');
          String forHeader = (String)retval[0];
          String[] twos = findNextSemicolon (forHeader);
          String forInitialization = twos[0];
          twos = findNextSemicolon (twos[1]);
          String forCondition = twos[0];
          String forContinuation = twos[1];

          code = retval[1].trim();
          ProgElement forbody;

          if (code.startsWith("{")) {
               code = code.substring(1);      // chop off leading "{"
               retval = findMatchingRightElement (code, '{', '}');
               forbody = (ProgElement)(parseSequence(retval[0])[0]);
          }
          else {
               retval = findNextSemicolon (code);
               forbody = new ProgElement("sequence","");
               forbody.addToBody(new ProgElement("statement",(String)retval[0]));
          }
          forbody.addToBody(new ProgElement("statement",forContinuation));

          ProgElement whilepart = new ProgElement ("while", forCondition);
          whilepart.addToBody(forbody);
          
          ProgElement pe = new ProgElement ("sequence", "");
          pe.addToBody(new ProgElement("statement",forInitialization));
          pe.addToBody(whilepart);

          return new Object[] {pe, retval[1]};
     }

     Object[] parseDefine (String code) {
          // code starts with "define" so ignore it and get the
          // list of parameters (which are in parentheses and separated
          // by commas.)

          code = code.substring(6).trim();

          // Next get the name of this function and put that into the
          // header part of the ProgElement.

          String funcname = "";
          while (true) {
               char ch = code.charAt(0);
               if (!Character.isLetterOrDigit(ch)) break;
               funcname += ch;
               code = code.substring(1);
          }
          code = code.trim();

          if (!code.startsWith("(")) {
               //this is an error, how do we deal with this?
               return new String[]{"ERROR", code};
          }

          code = code.substring(1);      // chop off leading "("
          String[] retval = findMatchingRightElement(code, '(', ')');

          ProgElement pe = new ProgElement ("define", funcname);

          // the list of parameters without the intervening commas
          pe.params = ((String)retval[0]).replace(',', ' ');  

          code = retval[1].trim();

          if (code.startsWith("{")) {
               code = code.substring(1);      // chop off leading "{"
               retval = findMatchingRightElement (code, '{', '}');
               pe.addToBody ((ProgElement)(parseSequence(retval[0])[0]));
          }
          else {
               retval = findNextSemicolon (code);
               pe.addToBody (new ProgElement("statement",(String)retval[0]));
          }
          
          return new Object[] {pe, retval[1]};
     }

     Object[] parseIf (String code) {
          // code starts with "if" so ignore it and get the
          // condition (which is in parentheses)

          code = code.substring(2).trim();
          if (!code.startsWith("(")) {
               //this is an error, how do we deal with this?
               System.out.println ("ParseIf:  error 1");
               return new String[]{"ERROR", code};
          }

          code = code.substring(1);      // chop off leading "("
          String[] retval = findMatchingRightElement(code, '(', ')');

          ProgElement pe = new ProgElement ("if", retval[0]);
          code = retval[1].trim();

          if (code.startsWith("{")) {
               code = code.substring(1);      // chop off leading "{"
               retval = findMatchingRightElement (code, '{', '}');
               pe.addToBody ((ProgElement)(parseSequence(retval[0])[0]));
          }
          else {
               retval = findNextSemicolon (code);
               pe.addToBody (new ProgElement("statement",(String)retval[0]));
          }
          code = retval[1].trim();

          // Is this an if/then/else?  If so, we will see "else" next.

          if (code.startsWith("else")) {
               code = code.substring(4).trim();
               if (code.startsWith("{")) {
                    code = code.substring(1);      // chop off leading "{"
                    retval = findMatchingRightElement (code, '{', '}');
                    pe.addToBody2 ((ProgElement)(parseSequence(retval[0])[0]));
               }
               else {
                    retval = findNextSemicolon (code);
                    pe.addToBody2 (new ProgElement("statement",(String)retval[0]));
               }
          }
          else {
               pe.body2 = null;
          }

          return new Object[] {pe, retval[1]};
     }

     public static String[] findMatchingRightElement (String code, char leftch, 
                                                      char rightch){
          String part1 = "";
          int count = 1;   // count of parentheses or curly braces so we can match
          boolean insideString = false;
          while (count > 0) {
               if (code.length() == 0)
                    return new String[] {"ERROR", code};
               char ch = code.charAt(0);
               if (ch == '"')
                    insideString = !insideString;
               if (!insideString) {
                    if (ch == leftch) 
                         count++;
                    else if (ch == rightch)
                         count--;
               }
               part1 += ch;
               code = code.substring(1);
          }
          return new String[] {part1.substring(0,part1.length()-1), code};
     }

     public static String[] findNextSemicolon (String code) {
          String part1 = "";
          boolean insideString = false;
          while (true) {
               if (code.length() == 0)
                    return new String[] {part1, code};
               char ch = code.charAt(0);
               if (ch == '"')
                    insideString = !insideString;
               if (!insideString) {
                    if (ch == ';')
                         return new String[] {part1, code.substring(1)};
               }
               part1 += ch;
               code = code.substring(1);
          }
     }

     public static String fix (String code) {
          String newcode = "";
          boolean insideComment = false;

          code += ' ';      // Make sure that the slash lookahead won't fail

          for (int i=0; i<code.length(); i++) {
               char ch = code.charAt(i);
               if (ch == '\n') {
                    insideComment = false;
                    code += ' ';
                    continue;
               }
               if (insideComment) continue;
               if (ch == '/' && code.charAt(i+1) == '/') {
                    insideComment = true;
                    continue;
               }
               newcode += ch;
          }

          // Now remove all old C-style comments

          code = newcode + ' ';     // make sure lookahead won't fail
          newcode = "";
          insideComment = false;

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

     public static String includeFile (String filename) {
          filename = filename.substring(7).trim();    // remove "include"
          if (filename.charAt(0) == '"') 
               filename = filename.substring(1,filename.length()-1);
          BufferedReader din;
          String codeLines = "";
          try {
               din = new BufferedReader (new FileReader(filename));
          } catch (IOException ioe) {
               System.out.println ("Included file '"+filename+"' not found.");
               return "";
          }
          try {
               String line;
               while ((line = din.readLine()) != null)
                    codeLines += line + "\n";
               din.close();
               return codeLines;
          } catch (IOException ioe) {
               System.out.println ("IO Error in '"+filename);
               System.out.println (ioe.getMessage());
               return "";
          }
     }
}
