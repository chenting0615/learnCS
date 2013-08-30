package org.softwarehelps.learncs.SEARCH;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Search extends Applet implements ActionListener, ItemListener,
                                              TextListener
{
     TextArea output;
     TextField input;
     Choice searchTypeCH, exampleCH;
     Button searchB, example1B, example2B;
     Color bg = new Color(134,147,230);
     TextArea bigTA;
     int count;              // count of tries (used for binary search)
     CellArray ca;
     int[] data = new int[]{5, 16, 19, 25, 37, 44, 56, 62, 79, 81,
                            99, 100, 105, 117, 200, 300};
     int searchval;      // what to find

     final static int SLEEPTIME = 450;

     Image buffer;
     Graphics gg;

     public void init() {
          setLayout (null);

          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();

          ca = new CellArray(16,         // number of cells
                             23,         // cell height (in pixels)
                             70,         // cell width (in pixels)
                             15,         // starting x position
                             50);        // starting y position

          for (int i=0; i<data.length; i++)
               ca.add(data[i]);
          //ca.setBgColor(2, CellArray.RED);
          //ca.setBgColor(3, CellArray.BLUE);

          Label lab = new Label("Searching");
          lab.setBounds(150,5,380,38);
          lab.setFont (new Font("SansSerif", Font.BOLD, 36));
          lab.setBackground(bg);
          add(lab);

          int y = 40 + lab.getSize().height;

          Label lab2 = new Label("The number to search for: ");
          lab2.setBounds(120,y-30,300,30);
          lab2.setBackground(bg);
          add(lab2);

          input = new TextField(20);
          input.setBounds(120,y,300,30);
          input.setBackground(Color.white);
          input.addActionListener(this);
          input.addTextListener(this);
          add(input);

          y += 5 + input.getSize().height;

          Label lab3 = new Label("Algorithm: ");
          lab3.setBounds(120,y,80,25);
          lab3.setBackground(bg);
          add(lab3);

          y += 5 + lab3.getSize().height;

          searchTypeCH = new Choice();
          searchTypeCH.addItem("Sequential search");
          searchTypeCH.addItem("Binary search");
          searchTypeCH.setBackground(Color.white);
          add(searchTypeCH);
          searchTypeCH.setBounds(120,y,300,50);

          y += 45 + searchTypeCH.getSize().height;

          Label lab4 = new Label("Messages about the search:");
          lab4.setBounds(120,y-30,300,30);
          lab4.setBackground(bg);
          add(lab4);

          output = new TextArea(3,20);
          output.setBounds(120,y,300,100);
          output.setBackground(Color.white);
          add(output);

          y += 10 + output.getSize().height;

          searchB = new Button("search");
          searchB.addActionListener(this);
          searchB.setBounds(200,y,100,25);
          add(searchB);

          y += 5 + searchB.getSize().height;

          exampleCH = new Choice();
          exampleCH.setBackground(Color.white);
          exampleCH.addItem(" -- examples -- ");
          exampleCH.addItem("Example 1: something in the list (sequential)");
          exampleCH.addItem("Example 2: something not in the list (sequential)");
          exampleCH.addItem("Example 3: something in the list (binary)");
          exampleCH.addItem("Example 4: something not in the list (binary)");
          exampleCH.addItemListener(this);
          exampleCH.setBounds(140, y, 300, 25);
          add(exampleCH);
     }

     public void paint (Graphics g) {
          gg.setColor(bg);
          gg.fillRect(0, 0, getSize().width, getSize().height);
          ca.paint(gg);
          g.drawImage(buffer, 0, 0, this);
     }

     public void update (Graphics g) {
          paint (g);
     }
     
     public void actionPerformed(ActionEvent e) {
          if (e.getSource() == searchB || e.getSource() == input)
               startSearch();
     }

     public void itemStateChanged(ItemEvent e) {
          if (e.getSource() == exampleCH)
               makeExample();
     }

     public void textValueChanged(TextEvent e) {
          if (e.getSource() == input)
               input.setBackground(Color.white);
     }

     private void makeExample() {
          String s = exampleCH.getSelectedItem();
          if (s.startsWith("Example 1")) {
               input.setText("99");
               searchTypeCH.select("Sequential search");
               startSearch();
          }
          else if (s.startsWith("Example 2")) {
               input.setText("20");
               searchTypeCH.select("Sequential search");
               startSearch();
          }
          else if (s.startsWith("Example 3")) {
               input.setText("19");
               searchTypeCH.select("Binary search");
               startSearch();
          }
          else if (s.startsWith("Example 4")) {
               input.setText("3789");
               searchTypeCH.select("Binary search");
               startSearch();
          }
     }

     public static int atoi (String s) {
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               return Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     private void startSearch() {
          if (input.getText().length() == 0) {
               input.setBackground(Color.pink);
               new Popup("Type a number to search for");
               return;
          }
          count = 0;
          String s = (String)searchTypeCH.getSelectedItem();
          if (s.equals("Binary search")) {
               output.setText("");
               searchval = atoi(input.getText());
               doBinary();
          }
          else {
               output.setText("");
               searchval = atoi(input.getText());
               doSequential();
          }
     }

     private void doBinary () {
          ca.unsetBgColors();
          repaint();
          new Thread() {
               public void run() {
                    binarySearch (0, data.length-1);
               }
          }.start();
     }

     private void doSequential () {
          ca.unsetBgColors();
          new Thread() {
               public void run() {
                    sequentialSearch();
               }
          }.start();
     }

     private void sequentialSearch () {
          for (int i=0; i<data.length; i++) {
               count++;
               ca.setBgColor(i, CellArray.PINK);
               repaint();
               if (data[i] == searchval) {
                    output.setText("Found in cell "+i+" after "+count+
                                    " tries.");
                    return;
               }
               try {
                    Thread.sleep(SLEEPTIME);
               } catch (InterruptedException ie) {}
               Thread.yield();
               ca.setBgColor(i, CellArray.GRAY);
          }
          output.setText("Not found after "+count+" tries.");
          repaint();
     }

     private void binarySearch (int first, int last) {
          int middle;

          if (first > last) {
               output.setText("Not found after "+count+" tries.");
               repaint();
          }
          else {
               middle = (first + last) / 2;
               count++;
//             ca.unsetBgColors();
               ca.setBgColor(middle, CellArray.PINK);
               repaint();
               try {
                    Thread.sleep(SLEEPTIME);
               } catch (InterruptedException ie) {}
               ca.setBgColor(middle, CellArray.WHITE);
               if (data[middle] == searchval) {
                    output.setText("Found in cell "+middle+" after "+count+
                                    " tries.");
                    return;
               }
               if (data[middle] > searchval) {
                    for (int i=middle; i<=last; i++)
                         ca.setBgColor(i, CellArray.GRAY);
                    binarySearch (first, middle-1);
               }
               else {
                    for (int i=0; i<=middle; i++)
                         ca.setBgColor(i, CellArray.GRAY);
                    binarySearch (middle+1, last);
               }
          }
     }


     private void makeData() {
     }
}

class CellArray {
     int[] numbers;
     int numcells;       // maximum size of array
     int[] bgcolors;     // background colors for cells
     int[] fgcolors;     // foreground colors for cells
 
     int occupiedcells;  // how many actually have numbers now

     int cellheight;
     int cellwidth;
 
     int startx, starty;  // starting screen coords

     public final static int WHITE = 0;
     public final static int BLACK = 1;
     public final static int RED   = 2;
     public final static int BLUE  = 3;
     public final static int YELLOW= 4;
     public final static int GREEN = 5;
     public final static int GRAY  = 6;
     public final static int PINK  = 7;

     public CellArray (int numcells, int cellheight, int cellwidth, 
                       int startx, int starty) 
     {
          this.numcells = numcells;
          numbers = new int[numcells];
          bgcolors = new int[numcells];
          fgcolors = new int[numcells];
          for (int i=0; i<numcells; i++)
               fgcolors[i] = BLACK;
          occupiedcells = 0;
          this.cellheight = cellheight;
          this.cellwidth = cellwidth;
          this.startx = startx;
          this.starty = starty;
     }

     public void paint (Graphics g) {
          paintGrid (g);
          paintBgColors (g);
          paintContents (g);
     }

     public void paintGrid (Graphics g) {
          int x = startx;
          int y = starty;

          g.setColor(Color.black);

          int numberpositionoffset = (cellheight * 3)/4;

          for (int i=0; i<numcells; i++) {
               g.drawString (i+"", x, y+numberpositionoffset);
               g.drawLine(x+20,y,x+20,y+cellheight);
               g.drawLine(x+20+cellwidth,y,x+20+cellwidth,y+cellheight);
               g.drawLine(x+20,y,x+20+cellwidth,y);
               y += cellheight;
          }
          g.drawLine (x+20, y, x+20+cellwidth, y);
     }

     public void paintBgColors (Graphics g) {
          int x = startx + 20;
          int y = starty;

          for (int i=0; i<numcells; i++) {
               g.setColor (translate(bgcolors[i]));
               g.fillRect(x+1, y+1, cellwidth-1, cellheight-1);
               y += cellheight;
          }
     }

     public void paintContents (Graphics g) {
          int x = startx + 20;
          int y = starty;

          for (int i=0; i<numcells; i++) {
               g.setColor (translate(fgcolors[i]));
               g.drawString (numbers[i]+"", x+5, y+cellheight-5);
               y += cellheight;
          }
     }

     public void set (int index, int value) {
          if (index < 0 || index >= numcells)
               return;
          numbers[index] = value;
     }

     public void setFgColor (int index, int color) {
          if (index < 0 || index >= numcells)
               return;
          fgcolors[index] = color;
     }

     public void setBgColor (int index, int color) {
          if (index < 0 || index >= numcells)
               return;
          bgcolors[index] = color;
     }

     public void unsetBgColors () {
          for (int i=0; i<numcells; i++) 
               bgcolors[i] = WHITE;
     }

     public void clear () {
          occupiedcells = 0;
          for (int i=0; i<numcells; i++)
               numbers[i] = 0;
          for (int i=0; i<numcells; i++)
               fgcolors[i] = BLACK;
          for (int i=0; i<numcells; i++)
               bgcolors[i] = WHITE;
     }

     public void add (int value) {
          if (occupiedcells < 0 || occupiedcells >= numcells)
               return;
          numbers[occupiedcells++] = value;
     }

     public Color translate (int colorvalue) {
          switch (colorvalue) {
              case WHITE:  return Color.white;
              case BLACK:  return Color.black;
              case RED:    return Color.red;
              case BLUE:   return Color.blue;
              case YELLOW: return Color.yellow;
              case GREEN:  return Color.green;
              case GRAY:   return Color.lightGray;
              case PINK:   return Color.pink;
          }
          return Color.white;
     }
}
