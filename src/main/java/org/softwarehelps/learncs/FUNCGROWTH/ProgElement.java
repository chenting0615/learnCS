import java.util.*;

public class ProgElement {
     String type;         // "statement", "sequence", "if", "while", "define"
     String header;       // for ifs and whiles, this is the condition
                          // for statement, this is the statement itself.
                          // ignored for sequence
     String name;         // if this is a define, this is the func. name
     Vector body;         // this is the body of sequence, while, or define
                          // it is a vector of ProgElements
                          // If this is an "if", the body is the true part
     Vector body2;        // If this is an "if/then/else", this body2 is
                          // the else part
     String params;       // only if this is a function definition

     public ProgElement (String type, String header) {
          this.type = type;
          this.header = header;
          body = new Vector();
          body2 = null;
     }

     public void addToBody (ProgElement pe) {
          body.addElement(pe);
     }

     public void addToBody2 (ProgElement pe) {
          if (body2 == null)
               body2 = new Vector();
          body2.addElement(pe);
     }

     public String toString(String indent) {
          String retval = type+"   header= "+header+"\n";
          for (int i=0; i<body.size(); i++)
               retval += indent+"  "+i+". " +
                  ((ProgElement)body.elementAt(i)).toString(indent+"   ");
          if (type.equals("if") && body2 != null) {
               retval += indent + "  else\n";
               for (int i=0; i<body2.size(); i++)
                    retval += indent+"  "+i+". " +
                       ((ProgElement)body2.elementAt(i)).toString(indent+"   ");
          }
          return retval;
     }

     public Token evaluate (NameDB ndb) {
          Funcgrowth pp = ndb.parent;
          if (type.equals("statement")) {
               Token tok = new Token(Token.INT, 0);  // default
               if (header.startsWith("return")) {
                    String temp = header.substring(6).trim();
                    if (temp.length() == 0)
                         tok = new Token(Token.INT, 0);
                    else
                         tok = new TokenList(temp, ndb).evaluate();
                    tok.returnType = "return";
               }
               else if (header.startsWith("local")) {
                    Functions.saveCurrentVariables(header.substring(5).trim(),
                                                      null, ndb);
                    ndb.push ("-2", null);
               }
               else if (header.startsWith("break")) {
                    String temp = header.substring(5).trim();
                    if (temp.length() == 0)
                         tok = new Token(Token.INT, 0);
                    else
                         tok = new TokenList(temp, ndb).evaluate();
                    tok.returnType = "break";
               }
               else
                    tok = new TokenList(header, ndb).evaluate();
               return tok;
          }
          if (type.equals("sequence")) {
               Token tok = null;
               for (int i=0; i<body.size(); i++) { 
                    tok = ((ProgElement)(body.elementAt(i))).evaluate(ndb);
                    if (tok.returnType.length() > 0)
                         return tok;
               }
               return tok;
          }
          if (type.equals("while")) {
               Token tok = new Token(Token.INT, 0);  //the default
               int condition;
//             int i = 0;
               while (true) {
                    condition = new TokenList(header, ndb).intEval();
                    if (condition == 0) break;
                    tok = ((ProgElement)(body.elementAt(0))).evaluate(ndb);
                    if (tok.returnType.equals("break")) {
                         tok.returnType = ""; 
                         return tok;
                    }
                    if (tok.returnType.equals("return") ||
                        tok.returnType.equals("exit"))
                         return tok;
//                  if (ndb.parent.muststop) break;
               }
               return tok;
          }
          if (type.equals("repeat")) {
               Token tok = new Token(Token.INT, 0);  //the default
               int count = new TokenList(header, ndb).intEval();
               int i = 0;
               while (i < count) {
                    tok = ((ProgElement)(body.elementAt(0))).evaluate(ndb);
                    if (tok.returnType.equals("break")) {
                         tok.returnType = ""; 
                         return tok;
                    }
                    if (tok.returnType.equals("return") ||
                        tok.returnType.equals("exit"))
                         return tok;
//                  if (ndb.parent.muststop) break;
                    i++;
               }
               return tok;
          }
          if (type.equals("if")) {
               int condition;
               condition = new TokenList(header, ndb).intEval();
               if (condition == 0) {
                    if (body2 == null)
                         return new Token(Token.INT, 0);
                    else {
                         return ((ProgElement)(body2.elementAt(0))).evaluate(ndb);
                    }
               }
               else {
                    return ((ProgElement)(body.elementAt(0))).evaluate(ndb);
               }
          }
          if (type.equals("define")) {
               ndb.define(header,(String)params,(ProgElement)body.elementAt(0));
          }
          return new Token(Token.INT, 0);
     }

     private String disassemble() {
          if (type.equals("statement")) {
               return header;
          }
          else if (type.equals("sequence")) {
               return "sequence";
          }
          else if (type.equals("while")) {
               return "while ("+header+") ...";
          }
          else if (type.equals("if")) {
               String s = "if ("+header+") ...";
               if (body2 != null) 
                     s += " else ...";
               return s;
          }
          return "";
     }
}
