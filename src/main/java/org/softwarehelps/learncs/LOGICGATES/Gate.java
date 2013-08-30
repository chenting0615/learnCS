package org.softwarehelps.learncs.LOGICGATES;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class Gate
{
     int id;                    // id number
     String type;               // "and", "or", "not", "switch", "output",
                                // "connector"
     String label;              // a user-defined label

     int x, y;                  // position on the screen

     int height = 50;
     int width = 50;            // smaller for NOTs

     Vector inputgates;         // pointers to the other gates
     String connectionList;     // a string that is a sequence of id numbers
                                // to be used later in forming the inputgates
                                // vector

     int currentValue;          // 1 or 0
     int newValue;              // 1 or 0, this is used during the update
                                // process

     String truthtable;         // list of rows with inputs and output

     static int nextid = 0;
     int[] inputvals;

     LogicGates parent;         // pointer to the parent window that has some
                                // "global" variables


     public Gate (LogicGates parent, String type, int xpos, int ypos, 
                  int numinputs) {
          this.parent = parent;
          this.type = type;
          label = "";
          x = xpos;
          y = ypos;
          currentValue = 0;
          newValue = 0;
          inputgates = new Vector();
          if (type.equals("connector")) {
               height = 8;
               width = 8;
          }
          if (numinputs != -1) {
               truthtable = makeTruthTable(numinputs);
               int numrows = (int) Math.pow(2,numinputs);
               height = 20 + numrows * 14;
               width = 67 + (numinputs-2)*10;
          }
          id = nextid++;
     }

     public Gate (LogicGates parent, String type, int xpos, int ypos) {
          this(parent,type,xpos,ypos,-1);
     }

     private String makeTruthTable(int numinputs) {
           int numrows = (int) Math.pow(2,numinputs);
           String rets = "";
           for (int i=0; i<numrows; i++)
                rets = rets + decToBinary(i, numinputs) + "|  0\n";
           return rets.substring(0,rets.length()-1);   // chop off last newline

           //return "0  0  |  0\n0  1  |  1\n1  0  |  1\n1  1  |  0";
     }

     private String decToBinary (int n, int numsize) {
           String rets = "";
           int numbits = 1;
           while (n != 1 && n != 0) {
                numbits++;
                if (n % 2 == 1)
                     rets = "1  " + rets;
                else
                     rets = "0  " + rets;
                n /= 2;
           }
           rets = n + "  " + rets;
           while (numbits < numsize) {
                rets = "0  " + rets;
                numbits++;
           }
           return rets;
     }

     public void setLabel(String label) {
          this.label = label;
     }

     public int getValue() {
          return currentValue;
     }

     public void flipValue() {
          currentValue = 1- currentValue;
          newValue = currentValue;
     }

     public void compute() {

          if (type.equals("switch")) {
               return;
          }

          if (inputgates.size() == 0) {
               newValue = 0;
               return;
          }

          inputvals = new int[inputgates.size()];

          for (int i=0; i<inputgates.size(); i++) {
               Gate g = (Gate)inputgates.elementAt(i);
               inputvals[i] = g.getValue();
          }

          if (type.equals("and")) {
               newValue = 1;
               for (int i=0; i<inputvals.length; i++) {
                    if (inputvals[i] == 0) {
                         newValue = 0;
                         break;
                    }
               }
          }

          else if (type.equals("or") || type.equals("connector")) {
               newValue = 0;
               for (int i=0; i<inputvals.length; i++) {
                    if (inputvals[i] == 1) {
                         newValue = 1;
                         break;
                    }
               }
          }

          else if (type.equals("nand")) {
               newValue = 0;
               for (int i=0; i<inputvals.length; i++) {
                    if (inputvals[i] == 0) {
                         newValue = 1;
                         break;
                    }
               }
          }

          else if (type.equals("nor")) {
               newValue = 1;
               for (int i=0; i<inputvals.length; i++) {
                    if (inputvals[i] == 1) {
                         newValue = 0;
                         break;
                    }
               }
          }

          else if (type.equals("xor")) {
               newValue = 0;
               for (int i=0; i<inputvals.length; i++) {
                    newValue = (newValue + inputvals[i]) % 2;
               }
          }

          else if (type.equals("not")) {
               newValue = 1-inputvals[0];
          }

          else if (type.equals("output")) {
               newValue = inputvals[0];
          }

          else if (type.equals("truthtable")) {
               newValue = matchTruthTable();
          }
     }

     public int matchTruthTable() {
          StringTokenizer st = new StringTokenizer(truthtable, "\n");
          while (st.hasMoreTokens()) {
               String tok = st.nextToken().trim();
               if (tok.length() == 0) continue;
               if (tok.startsWith("//")) continue;
               tok = U.replaceChar(tok, '|', ' ');
               boolean found = true;
               for (int i=0; i<inputvals.length; i++) {
                    if (inputvals[i] != U.atoi(U.getField(tok,i))) {
                         found = false;
                         break;
                    }
               }
               if (found) 
                    return U.atoi(U.getField(tok, inputvals.length));
          }
          return 0;
     }

     public void paint(Graphics g, boolean isTopgate)
     {
          if (type.equals("and"))
               drawAND(g, x, y, false);

          else if (type.equals("or"))
               drawOR(g, x, y, false);

          else if (type.equals("nand"))
               drawAND(g, x, y, true);

          else if (type.equals("nor"))
               drawOR(g, x, y, true);

          else if (type.equals("xor"))
               drawXOR(g, x, y);

          else if (type.equals("not"))
               drawNOT(g, x, y);

          else if (type.equals("switch"))
               drawSWITCH(g, x, y);

          else if (type.equals("output"))
               drawOUTPUT(g, x, y);

          else if (type.equals("connector"))
               drawCONNECTOR(g, x, y);

          else if (type.equals("truthtable"))
               drawTRUTHTABLE(g, x, y);

          drawLines(g);

          if (isTopgate) {
               g.setColor(Color.red);
               g.drawRect (x, y, width, height);
               g.drawRect (x-1, y-1, width, height);
          }

          g.setColor(Color.red);
          g.setFont(new Font("SansSerif", Font.PLAIN, 12));
          if (!type.equals("output")) 
               g.drawString(""+currentValue,x+width,y+height/2);
          g.drawString(label,x+5,y-15);
     }

     public void drawOR (Graphics g, int xx, int yy, boolean negated) {
          int x = xx-25;
          int y = yy;

          g.setColor(Color.black);
          g.drawArc (x-68, y-25, width*2, height*2, -32, 65);

          g.drawLine (x+25, y, x+37, y+8);
          g.drawLine (x+37, y+8, x+50, y+25);

          g.drawLine (x+25, y+height, x+37, y+42);
          g.drawLine (x+37, y+42, x+50, y+25);

          if (negated)
               g.drawArc (x+width, y+height/2, 5, 5, 0, 360);

          g.setFont(new Font("SansSerif", Font.BOLD, 12));
          g.drawString ("+", x+35, y+28);
     }

     public void drawAND (Graphics g, int xx, int yy, boolean negated) {
          int x = xx-25;
          int y = yy;

          g.setColor(Color.black);
          g.drawArc (x, y, width, height, -90, 180);
          g.drawLine (x+25, y, x+25, y+height);

          if (negated)
               g.drawArc (x+width, y+height/2, 5, 5, 0, 360);

          g.setFont(new Font("SansSerif", Font.BOLD, 12));
          g.drawString ("x", x+35, y+28);
     }

     public void drawNOT (Graphics g, int x, int y) {
          g.setColor(Color.black);

          g.drawLine(x, y, x, y+50);
          g.drawLine(x, y, x+25, y+25);
          g.drawLine(x, y+50, x+25, y+25);
          g.drawArc (x+25, y+22, 5, 5, 0, 360);
     }

     public void drawXOR (Graphics g, int xx, int yy) {
          int x = xx-25;
          int y = yy;

          g.setColor(Color.black);
          g.drawArc (x-68, y-25, width*2, height*2, -32, 65);

          g.drawArc (x-72, y-25, width*2, height*2, -32, 65);

          g.drawLine (x+25, y, x+37, y+8);
          g.drawLine (x+37, y+8, x+50, y+25);

          g.drawLine (x+25, y+height, x+37, y+42);
          g.drawLine (x+37, y+42, x+50, y+25);

          g.setFont(new Font("SansSerif", Font.BOLD, 12));
          g.drawString ("+", x+35, y+28);
     }

     public void drawSWITCH (Graphics g, int x, int y) {
          g.setColor(Color.black);
          g.drawRect (x, y, width, width);

          if (currentValue == 0) {
               g.drawArc (x+5, y+5, 5, 5, 0, 360);
               g.drawLine (x+5, y+5, x+25, y+50);
          }
          else {
               g.drawArc (x+width-5, y+5, 5, 5, 0, 360);
               g.drawLine (x+width-5, y+5, x+25, y+50);
          }

          g.drawArc (x, y+37, 50, 50, 30, 120);

          g.setFont(new Font("SansSerif", Font.BOLD, 18));
          g.drawString (""+currentValue, x+20, y+35);
     }

     public void drawOUTPUT (Graphics g, int x, int y) {
          g.setColor(Color.black);
          g.drawRect (x, y, width, width);
          g.setFont(new Font("SansSerif", Font.BOLD, 18));
          g.drawString (""+currentValue, x+20, y+35);
     }

     public void drawTRUTHTABLE (Graphics g, int x, int y) {
          g.setColor(Color.black);
          g.drawRect (x, y, width, height);
          g.setFont(new Font("SansSerif", Font.PLAIN, 12));
          g.drawString ("Truth Table",x+5,y+12);

          g.drawString ("---------------",x+5,y+18);

          StringTokenizer st = new StringTokenizer(truthtable, "\n");
          int tempy = y+28;
          while (st.hasMoreTokens()) {
               String tok = st.nextToken().trim();
               g.drawString (tok,x+5,tempy);
               tempy += 14;
          }
     }

     public void drawCONNECTOR (Graphics g, int x, int y) {
          g.setColor(Color.black);
          g.drawRect (x, y, width, width);
     }

     // We always draw from the recipient gate, not from the source gate.
     // Another problem we need to try to solve is if a bunch of lines
     // that go into a given box come from other boxes whose starting x
     // points are all the same.  In this case, the middle of each line
     // will be the same, so you'll see a bunch of confusing vertical
     // sections that all overlap.  In this case, we need to move some around
     // to give some visual separation.  However, to do this, we need to
     // remember which lines those are and where the vertical sections are.
     // That is what the following array does.

     static int[] midpoints = new int[100];       // where are the mid points?
     static int nummidpoints = 0;

     public static void resetMidpoints() {
          nummidpoints = 0;
     }

     private void drawLines (Graphics g) {
          g.setColor(Color.black);

          int numinputs = inputgates.size();
          int separation = height / (numinputs+1);

          int endx = x;
          int endy = y + separation;

          if (type.equals("connector")) {
               endy = y + height/2;
               endx = x + width/2;
          }

          for (int i=0; i<numinputs; i++) {
               Gate gate = (Gate)inputgates.elementAt(i);
               int startx;
               if (gate.type.equals("switch") || gate.type.equals("truthtable"))
                    startx = gate.x + gate.width;
               else
                    startx = gate.x + gate.width / 2;
               int starty = gate.y + gate.height / 2;
               drawComplexLine(g, gate, startx, starty, endx, endy);
               endy += separation;
          }
     }

     public void drawComplexLine(Graphics g, Gate source, int startx, 
                                int starty, int endx, int endy) {
          if (starty == endy) {
               g.drawLine(startx, starty, endx, endy);
               return;
          }
          if (parent.diagonal) 
               g.drawLine(startx, starty, endx, endy);
          else if (source.type.equals("connector")) {
               g.drawLine(startx, endy, endx, endy);
               g.drawLine(startx, starty, startx, endy);
          }
          else {
               int halfx = Math.abs((startx - endx)) / 2 + startx;
/*
               for (int i=0; i<nummidpoints; i++)
                    if (halfx == midpoints[i]) {
                         halfx = midpoints[i] - 15;
                         break;
                    }
               midpoints[nummidpoints++] = halfx;
*/
               g.drawLine(startx, starty, halfx, starty);
               g.drawLine(halfx, starty, halfx, endy);
               g.drawLine(halfx, endy, endx, endy);
          }
     }

     public boolean withinCenter (int x, int y) {
          int thirdh = height/3;
          int thirdw = width/3;
          return (x >= this.x+thirdw && x <= this.x+width-thirdw &&
                  y >= this.y+thirdh && y <= this.y+height-thirdh);
     }

     public boolean within(int x, int y) {
          return (x >= this.x && x <= this.x+width &&
                  y >= this.y && y <= this.y+height);
     }

     // The directionality of this is as follows:  The other gate
     // is input to this gate.

     public void connect (Gate other) {
          if (other == this) return;
          if (type.equals("switch")) {
               new Popup("Switches cannot have inputs.");
               return;
          }
          if (other.type.equals("output")) {
               new Popup("Output boxes cannot have outputs.");
               return;
          }
          if (type.equals("not") && inputgates.size() == 1) {
               new Popup("Not gates can only have 1 input.");
               return;
          }
          inputgates.addElement(other);
     }

     public String toString() {
          String s = id+" "+type+" "+x+" "+y+" ";
          for (int i=0; i<inputgates.size(); i++) {
               Gate g = (Gate)inputgates.elementAt(i);
               s += g.id+" ";
          }
          if (label.length() > 0) 
               s += ";"+label;
          return s;
     }

     // Example:
     //   Gate.parseString(this,"0 nand 417 191 6 7;A");
     // 
     //   this  =pointer to the parent window
     //   0     =identifier
     //   nand  =type of logic gate
     //   417   =X position
     //   191   =Y position
     //   6 7   =connection list, ids of boxes that this connects to
     //   ;     =end of connection list (may be absent)
     //   A     =the label (if any)


     public static Gate parseString(LogicGates parent, String s) {
          String tempLabel = "";
          int n = s.indexOf(";");   // break off the label, if any
          if (n > -1) {
               tempLabel = s.substring(n+1);
               s = s.substring(0,n);
          }

          Gate newg = new Gate(parent, U.getField(s,1), U.atoi(U.getField(s,2)),
                                       U.atoi(U.getField(s,3)));
          newg.label = tempLabel;

          if (newg.type.equals("truthtable")) {    // 4 rows, 2 inputs
               newg.height = 20 + 4 * 14;
               newg.width = 67;
          }

          newg.id = U.atoi(U.getField(s,0));
          newg.connectionList = U.skipFields(s,4);
          if (newg.id > nextid)
               nextid = newg.id+1;
          return newg;
     }
}
