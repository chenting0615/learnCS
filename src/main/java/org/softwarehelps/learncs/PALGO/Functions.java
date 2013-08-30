package org.softwarehelps.learncs.PALGO;

import java.util.*;
import java.io.*;
import java.awt.*;

public class Functions {

     public static Token evaluate (String funcname, Vector parmvalues,
                                   NameDB ndb) 
     {
          Palgo parent = ndb.parent;

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
          else if (funcname.equals("getx")) {
               int result = parent.currx;
               return new Token (Token.INT, result);
          }
          else if (funcname.equals("gety")) {
               int result = parent.curry;
               return new Token (Token.INT, result);
          }
          else if (funcname.equals("getblack")) {
               int result = 0;
               if (parent.cells[parent.currx][parent.curry] == Color.black)
                    result = 1;
               return new Token (Token.INT, result);
          }
          else if (funcname.equals("getwhite")) {
               int result = 0;
               if (parent.cells[parent.currx][parent.curry] == Color.white)
                    result = 1;
               return new Token (Token.INT, result);
          }
          else if (funcname.equals("getred")) {
               int result = parent.cells[parent.currx][parent.curry].getRed();
               return new Token (Token.INT, result);
          }
          else if (funcname.equals("getgreen")) {
               int result = parent.cells[parent.currx][parent.curry].getGreen();
               return new Token (Token.INT, result);
          }
          else if (funcname.equals("getblue")) {
               int result = parent.cells[parent.currx][parent.curry].getBlue();
               return new Token (Token.INT, result);
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
          else if (funcname.startsWith("print")) 
               return printout (funcname, parmvalues, ndb);
          else if (funcname.equals("truth")) {
               Token param = (Token)parmvalues.elementAt(0);
               if (param.type != Token.INT)
                    return new Token(Token.STRING, "illegal");
               if (param.intval == 1)
                    return new Token(Token.STRING, "true");
               else
                    return new Token(Token.STRING, "false");
          }
          else if (funcname.equals("exists")) {
               Token param = (Token)parmvalues.elementAt(0);
               if (ndb.nameInUse(param.value))
                    return new Token(Token.INT, 1);
               else
                    return new Token(Token.INT, 0);
          }
          else if (funcname.equals("valid")) {
               Token param = (Token)parmvalues.elementAt(0);
               if (ndb.nameInUse(param.value)) {
                    if (param.type == Token.UNKNOWN)
                         return new Token(Token.INT, 0);
                    else
                         return new Token(Token.INT, 1);
               }
               else
                    return new Token(Token.INT, 0);
          }
          else if (funcname.startsWith("numcells")) {
               if (parmvalues.size() == 0) {
                    return new Token(Token.INT, parent.numcells);
               }
               Token param = (Token)parmvalues.elementAt(0);
               int oldnumcells = parent.numcells;
               parent.resize(param.intval);
               return new Token (Token.INT, oldnumcells);
          }
          else if (funcname.equals("power")) {
               if (parmvalues.size() == 2) {
                    Token param1 = (Token)parmvalues.elementAt(0);
                    Token param2 = (Token)parmvalues.elementAt(1);
                    double n1, n2;
                    if (param1.type == Token.INT)
                         n1 = param1.intval;
                    else
                         n1 = param1.doubleval;
                    if (param2.type == Token.INT)
                         n2 = param2.intval;
                    else
                         n2 = param2.doubleval;
                    return new Token(Token.REAL, Math.pow(n1, n2));
               }
               else
                    return new Token(Token.STRING, "Illegal number of arguments to function");
          }
          else if (funcname.equals("legal")) {
               if (parmvalues.size() == 2) {
                    Token param1 = (Token)parmvalues.elementAt(0);
                    Token param2 = (Token)parmvalues.elementAt(1);
                    if (param1.type != Token.STRING)
                         return new Token(Token.STRING, "legal() requires 1st parm to be a string");
                    if (param2.type != Token.STRING)
                         return new Token(Token.STRING, "legal() requires 2nd parm to be a string");
                    if (param2.value.equals("real")) {
                         boolean legal = false;;
                         try {
                              new Double(param1.value).doubleValue();
                              legal = true;
                         }
                         catch (NumberFormatException nfe)
                         {  }
                         if (legal)
                              return new Token(Token.INT, 1);
                         else
                              return new Token(Token.INT, 0);
                    }
                    if (param2.value.equals("int")) {
                         boolean legal = false;;
                         try {
                              new Integer(param1.value).intValue();
                              legal = true;
                         }
                         catch (NumberFormatException nfe)
                         {  }
                         if (legal)
                              return new Token(Token.INT, 1);
                         else
                              return new Token(Token.INT, 0);
                    }
                    return new Token(Token.STRING, "Illegal arguments to legal function");
               }
               else
                    return new Token(Token.STRING, "Illegal number of arguments to function");
          }
          else if (funcname.startsWith("input_number")) {
               if (parmvalues.size() == 0) {
                    String result = Palgo.input(parent);
                    return new Token (Token.REAL, U.atod(result));
               } 
               else if (parmvalues.size() == 1) {
                    Token param = (Token)parmvalues.elementAt(0);
                    String result = Palgo.input(parent, param.value);
                    return new Token (Token.REAL, U.atod(result));
               }
          }
          else if (funcname.startsWith("input")) {
               if (parmvalues.size() == 0) {
                    String result = Palgo.input(parent);
                    return new Token (Token.STRING, result);
               }
               else if (parmvalues.size() == 1) {
                    Token param = (Token)parmvalues.elementAt(0);
                    String result = Palgo.input(parent, param.value);
                    return new Token (Token.STRING, result);
               }
          }
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
               int n = 0;
               if (param.type == Token.REAL)
                    n = (int)param.doubleval;
               else if (param.type == Token.INT)
                    n = (int)param.intval;
               else if (param.type == Token.STRING) {
                    try {
                         n = new Integer(param.value).intValue();
                    }
                    catch (NumberFormatException nfe)
                    {  }
               }
               return new Token(Token.INT, n);
          }
          else if (funcname.equals("real")) {
               Token param = (Token)parmvalues.elementAt(0);
               double n = 0;
               if (param.type == Token.INT)
                    n = (double)param.intval;
               else if (param.type == Token.REAL)
                    n = (double)param.doubleval;
               else if (param.type == Token.STRING) {
                    try {
                         n = new Double(param.value).doubleValue();
                    }
                    catch (NumberFormatException nfe)
                    {  }
               }
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
          else if (funcname.equals("length") || funcname.equals("numrows")) {
               if (parmvalues.size() == 0) 
                    return new Token(Token.ERROR, "this function needs 1 operand");

               Token parm1 = (Token)parmvalues.elementAt(0);

               if (parm1.type == Token.STRING)
                    return new Token(Token.INT, parm1.value.length());
               else if (parm1.type == Token.LIST)
                    return new Token(Token.INT, parm1.sublist.numtokens);
               else if (parm1.type == Token.TABLE)
                    return new Token(Token.INT, parm1.tablevalue.getNumRows());
               else
                    return new Token(Token.ERROR, "illegal operand");
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
          else if (funcname.equals("sort")) {
               Token parm1 = (Token)parmvalues.elementAt(0);

               if (parm1.type != Token.LIST) 
                    return new Token (Token.ERROR, "First param must be a list");

               int size = parm1.sublist.numtokens;

               String type = "int";
               if (parm1.sublist.tokens[0].type == Token.INT) 
                    type = "int";
               else if (parm1.sublist.tokens[0].type == Token.REAL) 
                    type = "real";
               else if (parm1.sublist.tokens[0].type == Token.STRING) 
                    type = "string";
               else
                    return new Token(Token.ERROR, "Cannot sort a list of this type.");

               for (int i=0; i<size-1; i++) {
                    for (int j=i+1; j<size; j++) {
                         if (type.equals("int")) {
                              int x1 = parm1.sublist.tokens[i].intval;
                              int x2 = parm1.sublist.tokens[j].intval;
                              if (x1 > x2) {
                                   int temp = x1;
                                   x1 = x2;
                                   x2 = temp;
                              }
                              parm1.sublist.tokens[i].intval = x1;
                              parm1.sublist.tokens[j].intval = x2;
                         }
                         else if (type.equals("real")) {
                              double x1 = parm1.sublist.tokens[i].doubleval;
                              double x2 = parm1.sublist.tokens[j].doubleval;
                              if (x1 > x2) {
                                   double temp = x1;
                                   x1 = x2;
                                   x2 = temp;
                              }
                              parm1.sublist.tokens[i].doubleval = x1;
                              parm1.sublist.tokens[j].doubleval = x2;
                         }
                         else if (type.equals("string")) {
                              String x1 = parm1.sublist.tokens[i].value;
                              String x2 = parm1.sublist.tokens[j].value;
                              if (x1.compareTo(x2) > 0) {
                                   String temp = x1;
                                   x1 = x2;
                                   x2 = temp;
                              }
                              parm1.sublist.tokens[i].value = x1;
                              parm1.sublist.tokens[j].value = x2;
                         }
                    }
               }
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

          // painting routines, new to Palgo

          else if (funcname.equals("clear")) {
               parent.clearCells();
               parent.repaint();
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
                    parent.current = new Color(parm1.intval, parm2.intval,
                                               parm3.intval);
               }
               return new Token (Token.INT, 1);
          }
          else if (funcname.equals("pen")) {
               Token parm = (Token)parmvalues.elementAt(0);
               if (parm.type == Token.STRING) {
                    if (parm.value.equals("down"))
                         parent.pendown = true;
                    else if (parm.value.equals("up"))
                         parent.pendown = false;
               }
               return new Token (Token.INT, 1);
          }

          // I am not entirely happy with this depiction of a circle 
          // but it is okay for now.

          else if (funcname.equals("circle")) {
//System.out.println ("doing circle");
               if (parmvalues.size() == 3) {
//System.out.println ("right # parms");
                    double pivotpoint = .71;
                    int xstart = ((Token)parmvalues.elementAt(0)).intval;
                    int ystart = ((Token)parmvalues.elementAt(1)).intval;
                    int radius = ((Token)parmvalues.elementAt(2)).intval;
                    
                    int midpoint = (int)(radius * pivotpoint);
                    int y = ystart - midpoint;
                    int x = xstart + radius - (int)(radius * (1.0-pivotpoint));
                    int yy = y;     // these vars used for symmetrical
                    int xx = x;     // curve down, drawn at the same time
                    int length = 1;
                    int xstart2 = xstart * 2;
                    int ystart2 = ystart * 2;

                  outerloop:
                    while (true) {
//System.out.println ("painting while");
                         for (int i=0; i<length; i++) {
//System.out.println ("i="+i+"  x="+x+"  y="+y);

                              // the right-upper quadrant

                              parent.cells[x][y] = parent.current;
                              parent.cells[xx][yy] = parent.current;

                              // the left-upper quadrant

                              parent.cells[xstart2 - x][y] = parent.current;
                              parent.cells[xstart2 - xx][yy] = parent.current;

                              // the right-lower quadrant

                              parent.cells[x][ystart2 - y] = parent.current;
                              parent.cells[xx][ystart2 - yy] = parent.current;

                              // the left-lower quadrant

                              parent.cells[xstart2 - x][ystart2 - y] = parent.current;
                              parent.cells[xstart2 - xx][ystart2 - yy] = parent.current;

                              x--;
                              yy++;
                              if (x < xstart) break outerloop;
                              if (x < 0 || yy >= parent.numcells) 
                                    break outerloop;
                         }
                         length++;
                         y--;
                         xx++;
                    }
                    parent.repaint();
                    return new Token(Token.INT, 0);
               }
               else
                    return new Token(Token.ERROR, "circle needs 3 parameters");
          }
          else if (funcname.equals("drawline")) {
               if (parmvalues.size() == 4) {
                    int xstart = ((Token)parmvalues.elementAt(0)).intval;
                    int ystart = ((Token)parmvalues.elementAt(1)).intval;
                    int xend = ((Token)parmvalues.elementAt(2)).intval;
                    int yend = ((Token)parmvalues.elementAt(3)).intval;
System.out.println("xstart = "+xstart+"   xend  = "+xend);
System.out.println("ystart = "+ystart+"   yend  = "+yend);
                    
                    // Always start with x on the "left"

                    if (xstart > xend) {
                         int temp = xstart;
                         xstart = xend;
                         xend = temp;
                    }

                    int run = xend - xstart + 1;     // always positive
                    int rise;
                    if (xend == xstart)
                         run = 0;
                    if (yend > ystart)
                         rise = yend - ystart + 1;
                    else
                         rise = yend - ystart - 1;
                    if (yend == ystart)
                         rise = 0;
System.out.println("rise="+rise+"   run="+run);
                    double slope = 0;
                    if (run > 0)
                         slope = (double)rise/run;
System.out.println("slope="+slope);

                    if (run == 0) {
                         for (int i=ystart; i<=yend; i++)
                              parent.cells[xstart][i] = parent.current;
                    }
                    else if (rise == 0) {
                         for (int i=xstart; i<=xend; i++)
                              parent.cells[i][ystart] = parent.current;
                    }
                    else if (slope > 0) {
                         int x, y=yend;
                         int lasty = ystart;
                         boolean started = false;
                         for (x=xstart; x<=xend; x++) {
                              double realy = slope*x;
//                            y = (int)realy;
                              y = (int)Math.round(realy) - ystart;
System.out.println("x="+x+"   realy="+realy+"   y="+y);
                              parent.cells[x][y] = parent.current;
                              if (started && y > lasty + 1) {
                                   int tempy = y-1;
                                   while (tempy > lasty) {
                                        parent.cells[x][tempy] = parent.current;
                                        tempy--;
                                   }
                              }
                              started = true;
                              lasty = y;
                         }
                         if (y < yend) {
                              y = yend;
                              parent.cells[x][y] = parent.current;
                              if (y > lasty + 1) {
                                   int tempy = y-1;
                                   while (tempy > lasty) {
                                        parent.cells[x][tempy] = parent.current;
                                        tempy--;
                                   }
                              }
                         }
                    }
                    else if (slope < 0) {
/*
                         slope = -slope;
System.out.println("new slope="+slope);
System.out.println("xend="+xend);
                         int x, y=yend;
                         int lasty = yend;
                         for (x=xstart; x<=xend; x++) {
                              double realy = (parent.numcells-1)-slope*x;
                              y = (int)realy;
//                            y = (int)Math.ceil(realy);
System.out.println("x="+x+"   realy="+realy+"   y="+y+"  lasty="+lasty);
                              parent.cells[x][y] = parent.current;
                              if (y < lasty - 1) {
                                   int tempy = y+1;
System.out.println("triggering extras, tempy="+tempy+"  lasty="+lasty);
                                   while (tempy < lasty) {
System.out.println("tempy="+tempy);
                                        parent.cells[x][tempy] = parent.current;
                                        tempy++;
                                   }
                              }
                              lasty = y;
                         }
                         if (y > yend) {
                              y = yend;
                              parent.cells[x][y] = parent.current;
                              if (y < lasty - 1) {
                                   int tempy = y-1;
                                   while (tempy > lasty) {
                                        parent.cells[x][tempy] = parent.current;
                                        tempy++;
                                   }
                              }
                         }
*/
                    }

                    parent.repaint();
                    return new Token(Token.INT, 0);
               }
               else
                    return new Token(Token.ERROR, "drawline needs 4 parameters");
          }
          else if (funcname.equals("draw")) {
               if (parmvalues.size() == 2) {
                    parent.currx = ((Token)parmvalues.elementAt(0)).intval;
                    parent.curry = ((Token)parmvalues.elementAt(1)).intval;
               }
               if (parent.currx > parent.numcells) {
                    Palgo.print("The position "+parent.currx+"\n"+
                                "is larger than the window size,\n"+
                                "which is "+parent.numcells);
                    Token tok = new Token (Token.INT, 0);
                    tok.returnType = "exit";
                    return tok;
               }
               if (parent.curry > parent.numcells) {
                    Palgo.print("The position "+parent.curry+"\n"+
                                "is larger than the window size,\n"+
                                "which is "+parent.numcells);
                    Token tok = new Token (Token.INT, 0);
                    tok.returnType = "exit";
                    return tok;
               }
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
                         if (parent.pendown)
                              parent.cells[parent.currx][parent.curry] = parent.current;
                         parent.curry++;
                         if (parent.curry >= parent.numcells) 
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
                         if (parent.pendown)
                              parent.cells[parent.currx][parent.curry] = parent.current;
                         parent.curry--;
                         if (parent.curry < 0)
                              parent.curry = parent.numcells-1;
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
                         if (parent.pendown)
                              parent.cells[parent.currx][parent.curry] = parent.current;
                         parent.currx++;
                         if (parent.currx >= parent.numcells)
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
                         if (parent.pendown)
                              parent.cells[parent.currx][parent.curry] = parent.current;
                         parent.currx--;
                         if (parent.currx < 0)
                              parent.currx = parent.numcells-1;
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
          else if (funcname.equals("gettable")) {
               return gettable((Token)parmvalues.elementAt(0),
                               (Token)parmvalues.elementAt(1),
                               (Token)parmvalues.elementAt(2), ndb);
          }
          else if (funcname.equals("settable")) {
               return settable((Token)parmvalues.elementAt(0),
                               (Token)parmvalues.elementAt(1),
                               (Token)parmvalues.elementAt(2), 
                               (Token)parmvalues.elementAt(3), 
                               ndb);
          }

          return new Token (Token.ERROR, "Unknown function name: "+funcname);
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
          else if (param.type == Token.UNKNOWN)
               toPrint = "*unknown*";

          if (newline)
               toPrint += "\n";

          if (funcname.equals("print")) 
               Palgo.print(toPrint);
          else if (funcname.equals("printstdout")) 
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

     //----------------------------------------------------------------
     //  gettable(tablename, row, fieldname)
     //
     //  Sample call:
     //      gettable("xyz", 15, "age")
     //
     //  where "xyz" is the name of the table (in the nameDatabase),
     //        15 is the row number
     //        "age" is the field name,
     //----------------------------------------------------------------

     private static Token gettable (Token token1, Token token2, Token token3,
                                    NameDB ndb)
     {
          String tableName = token1.value;
          long rownumber = token2.intval;
          String fieldName = token3.value;

          Token theTable = ndb.get(tableName);

          if (theTable.type == Token.UNKNOWN || theTable.type != Token.TABLE) 
               return new Token (Token.ERROR, 
                     "gettable() requires a table as 1st parameter.");

          StructList table = theTable.tablevalue;
          if (((int)rownumber) > table.getNumRows()-1)
               return new Token (Token.ERROR, 
                     "attempt to go beyond the end of the table");
          DataObj retval = table.getItem ((int)rownumber, fieldName);
          if (retval.type.equals("int"))
               return new Token (Token.INT, retval.intvalue);
          else if (retval.type.equals("real"))
               return new Token (Token.REAL, retval.realvalue);
          else if (retval.type.equals("string"))
               return new Token (Token.STRING, retval.stringvalue);
          else 
               return new Token (Token.ERROR, 
                     "you can only store ints, reals or strings in a table.");
     }

     //----------------------------------------------------------------
     //  settable(tablename, row, fieldname, newvalue)
     //
     //  Sample call:
     //      settable("xyz", 15, "age", 25)
     //
     //  where "xyz" is the name of the table (in the nameDatabase),
     //        15 is the row number
     //        "age" is the field name,
     //        25 is the new value        
     //----------------------------------------------------------------

     private static Token settable (Token token1, Token token2, Token token3,
                                    Token token4, NameDB ndb)
     {
          String tableName = token1.value;
          long rownumber = token2.intval;
          String fieldName = token3.value;

          Token theTable = ndb.get(tableName);

          if (theTable.type == Token.UNKNOWN ||
              theTable.type != Token.TABLE) {
               ndb.set(tableName, new Token(new StructList()));
               theTable = ndb.get(tableName);
          }

          StructList table = theTable.tablevalue;
          table.setItem ((int)rownumber, fieldName, DataObj.toDataObj(token4));

          return token4;
     }

}
