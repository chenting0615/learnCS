package org.softwarehelps.learncs.PALGO;

/* This file was automatically generated from a .mac file.*/

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Palgo extends Frame
                   implements MouseListener, ActionListener, ItemListener
{
     final static int PIXELSIZE = 400;
     int numcells = 20;
     int cellsize = PIXELSIZE / numcells;
     final static int XMARGIN = 20;
     final static int YMARGIN = 40;

     Color[][] cells;             // holds colors
     boolean muststop = false;
     boolean running = false;

     Color current;               // the current color
     int currx, curry;            // current position of the pen
     boolean pendown;             // true if the pen is down
     long secretSeed = 97;

     String program;              // the program to run in original C syntax
     String program2;             // the program to run in Palgo-ese
     NameDB ndb;                  // the name database, holds variables
  
     Image buffer;
     Graphics gg;

     public static IOWindow iowin;


     public Palgo() {
          super("Palgo programming system");
          iowin = new IOWindow();

          setLayout(null);

          cells = new Color[numcells][numcells];
          clearCells();

          int xpos = 20;

          addMouseListener(this);

          ndb = new NameDB(this);

          reset();

          new EditWindow(this);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                         // System.exit(1);
                    }
               }    
          );

          setSize(450, 450);
          setVisible(true);
          buffer = createImage (getSize().width, getSize().height);

          repaint();
     }

     public void reset() {
          current = Color.red;
          pendown = true;
          currx = 0;
          curry = 0;
     }

     public void update (Graphics g) {
          paint (g);
     }

     public void paint (Graphics g) {
          if (buffer == null)
               return;
          if (gg == null)
               gg = buffer.getGraphics();
          gg.setColor(Color.white);
          gg.fillRect(0,0,getSize().width, getSize().height);
          gg.setColor(Color.white);
          gg.fillRect(0,0, PIXELSIZE, PIXELSIZE);
          gg.setColor(Color.black);
          drawGrid(gg);
          paintCells(gg);
          g.drawImage(buffer, 0, 0, this);
     }

     public void resize(int newnumcells) {
          numcells = newnumcells;
          cellsize = PIXELSIZE / numcells;
          cells = new Color[numcells][numcells];
          clearCells();
          repaint();
     }

     public void clearCells() {
          for (int i=0; i<numcells; i++)
               for (int j=0; j<numcells; j++)
                    cells[i][j] = Color.white;
          reset();
     }

     private void paintCells (Graphics g) {
          for (int i=0; i<numcells; i++)
               for (int j=0; j<numcells; j++) {
                    g.setColor(cells[i][j]);
                    g.fillRect(i*cellsize+1+XMARGIN, j*cellsize+1+YMARGIN,
                               cellsize-1, cellsize-1);
               }
     }

     private void drawGrid(Graphics g) {
          int x = 0;
          g.setColor(Color.black);
          g.setFont(new Font("Serif", Font.PLAIN, 8));

          // First draw the vertical lines

          for (int i=0; i<numcells; i++) {
               g.drawLine(x+XMARGIN,0+YMARGIN,x+XMARGIN,PIXELSIZE+YMARGIN);
               if (numcells > 40 && i % 2 == 0) {
                    g.drawString(""+i,x+XMARGIN+7,0+YMARGIN);
System.out.println ("i="+i);
               }
               else
                    g.drawString(""+i,x+XMARGIN+7,0+YMARGIN);
               x += cellsize;
          }
          g.drawLine(x+XMARGIN,0+YMARGIN,x+XMARGIN,PIXELSIZE+YMARGIN);

          // Next draw the horizontal lines

          int y = 0;
          for (int i=0; i<numcells; i++) {
               g.drawLine(0+XMARGIN,y+YMARGIN,PIXELSIZE+XMARGIN,y+YMARGIN);
               if (numcells > 40 && i % 2 == 0)
                    g.drawString(""+i,0+XMARGIN-10,y+YMARGIN+12);
               else
                    g.drawString(""+i,0+XMARGIN-10,y+YMARGIN+12);
               y += cellsize;
          }
          g.drawLine(0+XMARGIN,y+YMARGIN,PIXELSIZE+XMARGIN,y+YMARGIN);
     }

     ProgElement sequence;

     public void actionPerformed (ActionEvent e) {
     }

     public void itemStateChanged (ItemEvent e) {
     }

     public void mouseClicked (MouseEvent me) {
     }

     public void mousePressed (MouseEvent me) { }
     public void mouseReleased (MouseEvent me) { }
     public void mouseEntered (MouseEvent me) { }
     public void mouseExited (MouseEvent me) { }

     public void execute() {
          Token result;
          program2 = Translator.translate(program);
          Parser p = new Parser(program2);
          sequence = p.result;
          muststop = false;
          new Thread() {
               public void run() {
                    try {
                         running = true;
                         sequence.evaluate(ndb, true);  // first get the defined funcs
                         sequence.evaluate(ndb);  // now evaluate
                         running = false;
                    } catch (Exception e) {
                         return;
                    }
               }
          }.start();
     }

     private void fillRow (int row, Color c) {
          for (int i=0; i<numcells; i++)
               cells[row][i] = c;
     }

     private void fillColumn(int col, Color c) {
          for (int i=0; i<numcells; i++)
               cells[i][col] = c;
     }

     public long extraSeed() {
          return (secretSeed++);
     }

     public static void print (String s) {
          iowin.setVisible(true);
          iowin.print(s);
     }

     public static String input (Palgo p) {
          return input(p, "");
     }

     public static String input (Palgo p, String prompt) {
          if (prompt.length() == 0)
               prompt = "Requesting input: please type your input below: ";
          iowin.allowInput(true);
          iowin.setPrompt(prompt);
          iowin.setVisible(true);
          iowin.focus();
          String ret = null;
          while (!p.muststop && (ret = iowin.getNext()) == null) {
               try {
                    Thread.sleep(100);
               } catch (InterruptedException ioe) {
               }
          }
          iowin.clearInput();
          iowin.setPrompt("");
          iowin.allowInput(false);
          U.sleep(600);
          return ret;
     }
}
