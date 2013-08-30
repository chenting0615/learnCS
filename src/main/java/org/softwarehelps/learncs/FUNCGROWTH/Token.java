package org.softwarehelps.learncs.FUNCGROWTH;

//  Issues to resolve... what is the connection between intval
//  and doubleval and value itself?  I'm confused!

public class Token {
     public int type;
     public final static int INT         = 0;
     public final static int REAL        = 1;
     public final static int STRING      = 2;
     public final static int LIST        = 3;
     public final static int OP          = 4;
     public final static int ID          = 5;
     public final static int LPAREN      = 6;
     public final static int RPAREN      = 7;
     public final static int COMMA       = 8;
     public final static int SUBEXP      = 9;
     public final static int FUNCALL     =10;
     public final static int EQUAL       =11;
     public final static int SEMICOLON   =12;
     public final static int ARRAYREF    =13;
     public final static int TABLE       =14;
     public final static int UNKNOWN     =66;
     public final static int STACKBOT    =77;
     public final static int ENDOFINPUT  =88;
     public final static int ERROR       =99;

     public String value;
     public TokenList sublist;
     public int intval;
     public double doubleval;
     public String returnType;
     public Token indexValue;

     public Token() {
          this.type = INT;
          this.value = "";
          this.sublist = null;
          this.intval = 0;
          this.doubleval = 0.0;
          this.returnType = "";
          this.indexValue = null;
     }

     public Token(int type, int xvalue) {   // mostly used for non-numerics
          this(type, xvalue+"", null);
     }

     public Token(int type, double xvalue) {
          this.type = type;    
          this.value = value;
          this.sublist = sublist;
          this.intval = (int)xvalue;
          this.doubleval = xvalue;
          this.returnType = "";
          this.indexValue = null;
     }

     public Token(int type, String value) {
          this(type, value, null);
     }

     public Token(int type, TokenList sublist) {
          this(type, "", sublist);
     }

     public Token(int type, String value, TokenList sublist) {
          this.type = type;
          this.value = value;
          this.sublist = sublist;
          if (type == INT)
               this.intval = atoi(value);
          if (type == REAL)
               this.doubleval = atod(value);
          this.returnType = "";
     }

     // The following constructor will be used for error messages.
     // The result value is typically 0, and the type is Token.ERROR.

     public Token(int type, int result, String value) {
          this(type, result);
     }

     public Token(int type, String varname, Token indexValue, boolean isarray) {
          this.type = type;
          this.value = varname;
          this.sublist = null;
          this.intval = 0;
          this.doubleval = 0.0;
          this.returnType = "";
          this.indexValue = indexValue;
     }

//NEWMARCH15

     public Object clone() {
          Token t = new Token(type, value);
          t.sublist = (TokenList)sublist.clone();
          t.intval = intval;
          t.doubleval = doubleval;
          t.returnType = returnType;
          return t;
     }

     public static boolean validateReal (String token) {
          int countPoints = 0;    // count the number of decimal points
          int countDigits = 0;   // count the number of digits
          for (int i=0; i<token.length(); i++) {
               if (token.charAt(i) == '.') countPoints++;
               if (token.charAt(i) != '.') countDigits++;
          }
          if (countPoints > 1) return false;
          if (countDigits < 1) return false;
          return true;
     }

     public static boolean isData (int type) {
          return type == INT || type == REAL || type == STRING ||       
                 type == LIST;
     }

     public static boolean isNumber (int type) {
          return type == INT || type == REAL;
     }

     public String toString() {
          String stype = getType().toUpperCase();
          if (type == SUBEXP)
               return "<SUBEXP, ["+sublist.toString()+"]>";
          else if (type == FUNCALL)
               return "<FUNCALL, "+value+" ("+sublist.toString()+")>";
          else if (type == INT)
               return "<"+stype+", "+intval+">";
          else if (type == REAL)
               return "<"+stype+", "+doubleval+">";
          else if (type == LIST)
               return "<LIST, ("+sublist.numtokens+" tokens)>";
          else 
               return "<"+stype+", "+value+">";
     }

     public String getType() {
          String stype;
          switch (type) {
               case INT: stype = "int";
               break;
               case REAL: stype = "real";
               break;
               case STRING: stype = "string";
               break;
               case LIST: stype = "list";
               break;
               case OP: stype = "op";
               break;
               case ID: stype = "id";
               break;
               case LPAREN: stype = "lparen";
               break;
               case RPAREN: stype = "rparen";
               break;
               case COMMA: stype = "comma";
               break;
               case SUBEXP: stype = "subexp";
               break;
               case FUNCALL: stype = "funcall";
               break;
               case EQUAL: stype = "equal";
               break;
               case SEMICOLON: stype = "semicolon";
               break;
               case ARRAYREF: stype = "arrayref";
               break;
               case TABLE: stype = "table";
               break;
               case UNKNOWN: stype = "unknown";
               break;
               case STACKBOT: stype = "stackbot";
               break;
               case ENDOFINPUT: stype = "endofinput";
               break;
               case ERROR: stype = "error";
               break;
               default: stype = "???";
          }
          return stype;
     }

     //------------------------------------------------------------------
     /**
      *  This function converts a String to an int.  That is, it reads
      *  the contents of a string and interprets it as an integer.  If
      *  the string is not a valid double, 0.0 is returned, but no
      *  exception is thrown.
      *
      *  Last changed: 6/29/2000  by RMM
     **/
     //------------------------------------------------------------------

     public static int atoi (String s) {
          try {
               return Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe)
          {
               return 0;
          }
     }

     //------------------------------------------------------------------
     /**
      *  This function converts a String to a double.  That is, it reads
      *  the contents of a string and interprets it as a double.  If the
      *  string is not a valid double, 0.0 is returned, but no exception
      *  is thrown.
      *
      *  Last changed: 6/29/2000  by RMM
     **/
     //------------------------------------------------------------------

     public static double atod (String s) {
          try {
               return Double.valueOf(s).doubleValue();
          }
          catch (NumberFormatException nfe)
          {
               return 0.0;
          }
     }
}
