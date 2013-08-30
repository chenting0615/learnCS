package org.softwarehelps.learncs.GAMEOFLIFE;

/* This file was automatically generated from a .mac file. */

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Gameoflife extends Frame
                implements MouseListener, ActionListener
{
     final static int CELLSIZE = 20;
     final static int NUMCELLS = 20;
     final static int PIXELSIZE = CELLSIZE*NUMCELLS;
     int[][] cells, oldcells;
     boolean muststop = false;
     boolean running = false;
     int time = 0;          // shown on screen so you can see the number of
                            // iterations that have occurred

     Label runningStatus, timeCount;
  
     Image buffer;
     Graphics gg;

     Button playgameB, onestepB, stopB, clearB, exampleB,
            loadB, saveB;
     int startingX, startingY;

     public Gameoflife() {
          setLayout(null);

          setTitle("Game of Life");
          cells = new int[NUMCELLS][NUMCELLS];
          oldcells = new int[NUMCELLS][NUMCELLS];
          clearCells();

          int xpos = 5;
          startingX = 10;

          int y = 25;

          Label lab = new Label("The Game of Life");
          lab.setFont(new Font("SansSerif", Font.BOLD, 36));
          lab.setBackground(Color.white);
          lab.setBounds(60,y,350,45);
          add(lab);
          startingY = lab.getSize().height+y;

          playgameB = new Button("Play Game");
          playgameB.addActionListener(this);
          add(playgameB);
          playgameB.setBounds(xpos, startingY+PIXELSIZE+5, 90, 25);
          xpos += playgameB.getSize().width+5;

          onestepB = new Button("1 step");
          onestepB.addActionListener(this);
          add(onestepB);
          onestepB.setBounds(xpos, startingY+PIXELSIZE+5, 40, 25);
          xpos += onestepB.getSize().width+5;

          stopB = new Button("Stop");
          stopB.addActionListener(this);
          add(stopB);
          stopB.setBounds(xpos, startingY+PIXELSIZE+5, 40, 25);
          xpos += stopB.getSize().width+5;

          runningStatus = new Label("");
          runningStatus.setBackground(Color.white);
          add(runningStatus);
          runningStatus.setBounds(stopB.getLocation().x,
                startingY+PIXELSIZE+5+stopB.getSize().height,90,25);

          timeCount = new Label("");
          timeCount.setBackground(Color.white);
          add(timeCount);
          timeCount.setBounds(runningStatus.getLocation().x +
                              runningStatus.getWidth() + 2,
                startingY+PIXELSIZE+5+stopB.getSize().height,90,25);

          loadB = new Button("Load");
          clearB = new Button("Clear Cells");
          clearB.addActionListener(this);
          add(clearB);
          clearB.setBounds(xpos, startingY+PIXELSIZE+5, 70, 25);
          xpos += clearB.getSize().width+5;

          exampleB = new Button("Example");
          exampleB.addActionListener(this);
          add(exampleB);
          exampleB.setBounds(xpos, startingY+PIXELSIZE+5, 70, 25);
          xpos += exampleB.getSize().width+5;

          loadB.addActionListener(this);
          add(loadB);
          loadB.setBounds(xpos, startingY+PIXELSIZE+5, 50, 25);
          xpos += loadB.getSize().width+5;

          saveB = new Button("Save");
          saveB.addActionListener(this);
          add(saveB);
          saveB.setBounds(xpos, startingY+PIXELSIZE+5, 50, 25);
          xpos += saveB.getSize().width+5;

          addMouseListener(this);

          setVisible(true);
          setSize(650, 560);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                         System.exit(1);
                    }
               }    
          );

          buffer = createImage (getSize().width, getSize().height);

          repaint();
     }

     public void update (Graphics g) {
          paint (g);
     }

     public void paint (Graphics g) {
          if (buffer == null) 
               return;
          if (buffer != null) {
               if (gg == null)
                    gg = buffer.getGraphics();
          }
          gg.setColor(Color.white);
          gg.fillRect(0,0,getSize().width, getSize().height);
          gg.setColor(Color.white);
          gg.fillRect(0,0, PIXELSIZE, PIXELSIZE);
          gg.setColor(Color.black);
          drawGrid(gg);
          paintCells(gg);
          if (running)
               timeCount.setText(time+"");
          g.drawImage(buffer, 0, 0, this);
     }

     private void clearCells() {
          for (int i=0; i<NUMCELLS; i++)
               for (int j=0; j<NUMCELLS; j++)
                    cells[i][j] = 0;
     }

     private void paintCells (Graphics g) {
          int X = startingX;
          int Y = startingY;
          for (int i=0; i<NUMCELLS; i++)
               for (int j=0; j<NUMCELLS; j++)
                    if (cells[i][j] == 1) {
                         g.setColor(Color.black);
                         g.fillRect(X+i*CELLSIZE, Y+j*CELLSIZE, CELLSIZE, CELLSIZE);
                    }
     }

     private void drawGrid(Graphics g) {
          int x = startingX;
          int Y = startingY;
          for (int i=0; i<NUMCELLS; i++) {
               g.drawLine(x,Y+0,x,Y+PIXELSIZE);
               x += CELLSIZE;
          }
          g.drawLine(x,Y+0,x,Y+PIXELSIZE);

          int y = 0;
          for (int i=0; i<NUMCELLS; i++) {
               g.drawLine(startingX,Y+y,PIXELSIZE+startingX,Y+y);
               y += CELLSIZE;
          }
          g.drawLine(startingX,Y+y,PIXELSIZE+startingX,Y+y);
     }

     public void actionPerformed (ActionEvent e) {
          if (e.getSource() == clearB) {
               clearCells();
               repaint();
          }
          else if (e.getSource() == onestepB) {
               muststop = false;
               runningStatus.setText("Running...");
               try {
                    Thread.sleep(50);
               } catch (InterruptedException ie) {}
               simulate(1);
               runningStatus.setText("");
               repaint();
          }
          else if (e.getSource() == playgameB) {
               muststop = false;
               runningStatus.setText("Running...");
               playgameB.setEnabled(false);
               stopB.setEnabled(true);
               simulate(-1);
               repaint();
          }
          else if (e.getSource() == stopB) {
               playgameB.setEnabled(true);
               stopB.setEnabled(false);
               runningStatus.setText("");
               muststop = true;
          }
          else if (e.getSource() == exampleB) {
               makeExample();
               repaint();
          }
          else if (e.getSource() == loadB) {
               String s = loadPicture();
               decodePicture (s, cells, NUMCELLS);
               repaint();
          }
          else if (e.getSource() == saveB) {
               String s = encodePicture (cells, NUMCELLS);
               savePicture(s);
          }
     }

     public void mouseClicked (MouseEvent me) {
          int x = me.getX();
          int y = me.getY();
          touchCell(x-startingX, y-startingY);
          repaint();
     }
     public void mousePressed (MouseEvent me) {
     }
     public void mouseReleased (MouseEvent me) {
     }
     public void mouseEntered (MouseEvent me) {
     }
     public void mouseExited (MouseEvent me) {
     }

     private void touchCell (int xpos, int ypos) {
          int x = xpos / CELLSIZE;
          int y = ypos / CELLSIZE;
          if (x < 0 || x >= NUMCELLS) return;
          if (y < 0 || y >= NUMCELLS) return;
          if (cells[x][y] == 1)
               cells[x][y] = 0;
          else
               cells[x][y] = 1;
     }

     private int tempnumsteps;

     private void simulate(int numsteps) {
          tempnumsteps = numsteps;
          running = true;
          new Thread() {
               public void run() {
                    time = 0;
                    while ((tempnumsteps > 0 && !muststop) ||
                           (tempnumsteps == -1 && !muststop)) 
                    {
                         onestep();
                         time++;
                         repaint();
                         if (tempnumsteps > 0)
                              tempnumsteps--;
                         try {
                              Thread.sleep(100);
                         } catch (InterruptedException ie) {}
                    }
                    running = false;
               }
          }.start();
     }

     private void onestep() {
          for (int i=0; i<NUMCELLS; i++)
               for (int j=0; j<NUMCELLS; j++)
                    oldcells[i][j] = cells[i][j];

          for (int i=1; i<NUMCELLS-1; i++)
               for (int j=1; j<NUMCELLS-1; j++)
                    cells[i][j] = applyRule (i, j);
     }

     private int applyRule (int x, int y) {
          int sum = oldcells[x-1][y] +
                    oldcells[x+1][y] +
                    oldcells[x][y-1] +
                    oldcells[x][y+1] +
                    oldcells[x-1][y-1] +
                    oldcells[x+1][y-1] +
                    oldcells[x-1][y+1] +
                    oldcells[x+1][y+1];
          if (sum < 2 || sum > 3)
               return 0;
          else if (sum == 3)
               return 1;
          else
               return oldcells[x][y];
     }

     private void makeExample() {
          cells[3][2] = 1;
          cells[2][3] = 1;
          cells[3][3] = 1;
          cells[4][3] = 1;
          cells[2][4] = 1;
     }

     private void fillRow (int row) {
          for (int i=0; i<NUMCELLS; i++)
               cells[row][i] = 1;
     }

     private void fillColumn(int col) {
          for (int i=0; i<NUMCELLS; i++)
               cells[i][col] = 1;
     }

     private String encodePicture (int[][] cells, int numcells) {
          String s = "";
          for (int i=0; i<numcells; i++) {
               for (int j=0; j<numcells; j++) {
                    s += cells[j][i];
               }
               s += "\n";
          }
          return s;
     }

     private void decodePicture (String s, int[][] cells, int numcells) {
          String s2 = "";

          for (int i=0; i<s.length(); i++) {
               char ch = s.charAt(i);
               if (ch != '\n') 
                    s2 += ch;
          }
          
          int k = 0;
          for (int i=0; i<numcells; i++) {
               for (int j=0; j<numcells; j++) {
                    char ch = s2.charAt(k++);
                    if (ch == '0') 
                         cells[j][i] = 0;
                    else
                         cells[j][i] = 1;
               }
          }
     }

     public String loadPicture () {
          String text = "";

          FileDialog loadfile = new
               FileDialog(this, "Load Gameoflife picture", FileDialog.LOAD);
          loadfile.setFile("*.txt");
          loadfile.show();

          String filename = loadfile.getFile();
          String directory = loadfile.getDirectory();
          if (filename == null)
               return "";
          if (!filename.endsWith(".txt")) {
               System.err.println ("A picture file must have an extension of .txt.");
               return "";
          }

          BufferedReader is;        // input stream

          if(filename == null || filename.length() == 0)
               return "";

          this.setTitle(filename);
          try {
               is = new BufferedReader(new FileReader(directory+"\\"+filename));
               String line;
               while ((line = is.readLine()) != null) {
                    if (text.length() == 0)
                         text += line;
                    else
                         text += "\n"+line;
               }
               is.close();
          }
          catch(IOException ioe) {
               System.err.println("Load picture:  i/o exception.");
          }
          return text;
     }

     public void savePicture (String text) {
          FileDialog savefile =
               new FileDialog(this,"Save Gameoflife Picture",FileDialog.SAVE);
          savefile.setFile("*.txt");
          savefile.show();

          String filename = savefile.getFile();
          String directory = savefile.getDirectory();
          String fullname = directory + "\\"+filename;
          
          if(filename == null)
               return;

          BufferedWriter os;        // output stream

          if(filename == null || filename.length() == 0)
               return;
          else {
               this.setTitle(filename);
               try {
                    os = new BufferedWriter(new FileWriter(fullname, false));

                    os.write(text);
                    os.close();
               }
               catch(IOException ioe) {
                    System.err.println("Save Picture:  i/o exception.");
               }
          }
     }
}
