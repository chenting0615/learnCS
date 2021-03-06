package org.softwarehelps.learncs.SIMPLESQL;

/* This file was automatically generated from a .mac file.*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class EditWindow extends Frame 
        implements ActionListener, ComponentListener,
                   MouseListener, ItemListener
{
     final static int MAXCOLS = 5;

     Label Label1;
     TextField tablenameTF;
     Button okB, cancelB, loadB, saveB;
     TextArea rownumTF;

     TextField[] fieldnames;
     Choice[] types;
     TextArea[] contents;

     TextArea stats;

     Image buffer;
     Graphics gg;
     Color acolor = new Color(246,227,218);
     final static int X=0;
     final static int Y=0;

     Table t;
     String oldname;

     SimpleSQL parent;

     public EditWindow(SimpleSQL parent, Table t, String oldname) {
          this.parent = parent;
          this.t = t;
          this.oldname = oldname;

          setTitle(t.name);

          setLayout(null);

          Label1=new Label("Table Name:");
          Label1.setBackground(acolor);
          Label1.setFont(new Font("SansSerif",Font.BOLD,12));
          Label1.setBounds(X+68,Y+36,80,25);
          add(Label1);

          tablenameTF=new TextField(40);
          tablenameTF.addActionListener(this);
          tablenameTF.setBackground(Color.white);
          tablenameTF.setBounds(X+150,Y+36,298,25);
          add(tablenameTF);

          tablenameTF.setText(t.name);

          fieldnames = new TextField[MAXCOLS];

          fieldnames[0]=new TextField(40);
          fieldnames[0].addActionListener(this);
          fieldnames[0].setBackground(Color.white);
          fieldnames[0].setBounds(X+95,Y+80,120,25);
          add(fieldnames[0]);

          fieldnames[1]=new TextField(40);
          fieldnames[1].addActionListener(this);
          fieldnames[1].setBackground(Color.white);
          fieldnames[1].setBounds(X+230,Y+80,120,25);
          add(fieldnames[1]);

          fieldnames[2]=new TextField(40);
          fieldnames[2].addActionListener(this);
          fieldnames[2].setBackground(Color.white);
          fieldnames[2].setBounds(X+365,Y+80,120,25);
          add(fieldnames[2]);

          fieldnames[3]=new TextField(40);
          fieldnames[3].addActionListener(this);
          fieldnames[3].setBackground(Color.white);
          fieldnames[3].setBounds(X+500,Y+80,120,25);
          add(fieldnames[3]);

          fieldnames[4]=new TextField(40);
          fieldnames[4].addActionListener(this);
          fieldnames[4].setBackground(Color.white);
          fieldnames[4].setBounds(X+635,Y+80,120,25);
          add(fieldnames[4]);

          types = new Choice[MAXCOLS];

          types[0]=new Choice();
          types[0].setBackground(Color.white);
          types[0].setBounds(X+95,Y+110,120,25);
          types[0].addItem("String");
          types[0].addItem("Number");
          add(types[0]);

          types[1]=new Choice();
          types[1].setBackground(Color.white);
          types[1].setBounds(X+230,Y+110,120,25);
          types[1].addItem("String");
          types[1].addItem("Number");
          add(types[1]);

          types[2]=new Choice();
          types[2].setBackground(Color.white);
          types[2].setBounds(X+365,Y+110,120,25);
          types[2].addItem("String");
          types[2].addItem("Number");
          add(types[2]);

          types[3]=new Choice();
          types[3].setBackground(Color.white);
          types[3].setBounds(X+500,Y+110,120,25);
          types[3].addItem("String");
          types[3].addItem("Number");
          add(types[3]);

          types[4]=new Choice();
          types[4].setBackground(Color.white);
          types[4].setBounds(X+635,Y+110,120,25);
          types[4].addItem("String");
          types[4].addItem("Number");
          add(types[4]);

          rownumTF=new TextArea(5,40);
          rownumTF.setBackground(Color.white);
          rownumTF.setBounds(X+17,Y+140,61,199);
          add(rownumTF);

          contents = new TextArea[MAXCOLS];

          contents[0]=new TextArea(5,40);
          contents[0].setBackground(Color.white);
          contents[0].setBounds(X+95,Y+140,120,200);
          add(contents[0]);

          contents[1]=new TextArea(5,40);
          contents[1].setBackground(Color.white);
          contents[1].setBounds(X+230,Y+140,120,200);
          add(contents[1]);

          contents[2]=new TextArea(5,40);
          contents[2].setBackground(Color.white);
          contents[2].setBounds(X+365,Y+140,120,200);
          add(contents[2]);

          contents[3]=new TextArea(5,40);
          contents[3].setBackground(Color.white);
          contents[3].setBounds(X+500,Y+140,120,200);
          add(contents[3]);

          contents[4]=new TextArea(5,40);
          contents[4].setBackground(Color.white);
          contents[4].setBounds(X+635,Y+140,120,200);
          add(contents[4]);

          int y = contents[3].getLocation().y + contents[3].getSize().height + 10;

          int x = X+275;
          okB=new Button("OK");
          okB.addActionListener(this);
          okB.setBounds(x,y,50,25);
          add(okB);
          x += okB.getSize().width + 5;

          cancelB=new Button("Cancel");
          cancelB.addActionListener(this);
          cancelB.setBounds(x,y,50,25);
          add(cancelB);
          x += cancelB.getSize().width + 5;

          loadB=new Button("Load");
          loadB.addActionListener(this);
          loadB.setBounds(x,y,50,25);
          add(loadB);
          x += loadB.getSize().width + 5;

          saveB=new Button("Save");
          saveB.addActionListener(this);
          saveB.setBounds(x,y,50,25);
          add(saveB);
          x += saveB.getSize().width + 5;

          stats = new TextArea(3,40);
          stats.setBounds(140,
                          cancelB.getLocation().y+cancelB.getSize().height+10,
                          400,80);
          add(stats);

          loadTable();

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);
          setSize(765,560);
          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();

          addWindowListener(
             new WindowAdapter() {
                public void windowClosing (WindowEvent we) {
                     dispose();
                }
             }
          );

     }

     public void loadTable () {
          stats.setText("numrows = "+t.numrows+"   numfields = "+t.numfields);
          tablenameTF.setText(t.name);
          int fieldlimit = Math.min(t.numfields,MAXCOLS);
          for (int i=0; i<fieldlimit; i++) {
               fieldnames[i].setText(t.fieldnames[i]);
               types[i].select(t.types[i]);
               contents[i].setText(t.contents[i]);
          }
          rownumTF.setText("");
          for (int i=1; i<=t.numrows; i++) 
               if (i == 1)
                    rownumTF.setText("1");
               else
                    rownumTF.setText(rownumTF.getText()+"\n"+i);
     }

     public void saveTable () {
          t.name = tablenameTF.getText();
          t.numfields = 0;
          for (int i=0; i<MAXCOLS; i++) {
               if (fieldnames[i].getText().length() == 0) continue;
               t.fieldnames[t.numfields] = fixNewlines(fieldnames[i].getText());
               t.types[t.numfields] = fixNewlines(types[i].getSelectedItem());
               t.contents[t.numfields] = fixNewlines(contents[i].getText());
               t.numfields++;
          }
          t.normalizeRows();
     }


     // This code gets rid of any ASCII 13's that might be
     // lurking in the text.  I discovered that when you manually
     // type numbers into a textarea, pressing return after each
     // number, Windows puts ASCII 13 plus a newline there!  Yuck!

     public String fixNewlines (String s) {

          String news = "";
          for (int i=0; i<s.length(); i++) {
               char ch = s.charAt(i);
               if (ch != 13) news = news + ch;
          }
          return news;
     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == cancelB) 
               dispose();

          else if (e.getSource() == okB) {
               saveTable();
               parent.setTable(t, oldname);
               dispose();
          }

          else if (e.getSource() == loadB) 
               loadTableFromFile();

          else if (e.getSource() == saveB) 
               saveTableToFile();
     }

     public void itemStateChanged(ItemEvent e) {}
     public void componentResized(ComponentEvent e) {}
     public void componentHidden(ComponentEvent e) {}
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}
     public void mouseEntered(MouseEvent e) {}
     public void mouseExited(MouseEvent e) {}
     public void mousePressed(MouseEvent e) {}
     public void mouseReleased(MouseEvent e) {}
     public void mouseClicked(MouseEvent e) {
          if(!e.isMetaDown())
               if(e.getClickCount() == 2)
                    doubleClick(e);
     }
     public void doubleClick (MouseEvent e) {
     }
     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          gg.setColor(acolor);
          // Write to gg first
          g.drawImage(buffer,0,0,this);
     }
     public void update (Graphics g) {
          paint (g);
     }

     public void loadTableFromFile() {
          String text = "";

          FileDialog loadfile = new
               FileDialog(this, "Load Table", FileDialog.LOAD);
          loadfile.setFile("*.txt");
          loadfile.show();

          String filename = loadfile.getFile();
          String directory = loadfile.getDirectory();
          if (filename == null)
               return;
          if (!filename.endsWith(".txt")) {
               System.err.println ("A table file must have an extension of .txt.");
               return;
          }

          BufferedReader is;        // input stream

          if(filename == null || filename.length() == 0)
               return;

          this.setTitle(filename);
          try {
               is = new BufferedReader(new FileReader(directory+"\\"+filename));
               String line = is.readLine();
               t.numfields = U.atoi(line);
               line = is.readLine();
               t.numrows = U.atoi(line);

               for (int i=0; i<t.numfields; i++) {
                    line = is.readLine();
                    t.fieldnames[i] = line;
                    line = is.readLine();
                    t.types[i] = line;

                    line = is.readLine();
                    t.contents[i] = line;
                    while(true) {
                         line = is.readLine();
                         if (line.equals("----")) break;
                         t.contents[i] += "\n"+line;
                    }
               }
               is.close();
               loadTable();
          }
          catch(IOException ioe) {
               System.err.println("LoadProgram:  i/o exception.");
               ioe.printStackTrace();
          }
          return;
     }

     public void saveTableToFile() {
          FileDialog savefile =
               new FileDialog(this,"Save Table",FileDialog.SAVE);
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
                    saveTable();
                    parent.setTable(t, oldname);
                    os.write(t.numfields+"\n");
                    os.write(t.numrows+"\n");
                    for (int i=0; i<t.numfields; i++) {
                         os.write(t.fieldnames[i]+"\n");
                         os.write(t.types[i]+"\n");
                         os.write(t.contents[i]+"\n");
                         os.write("----\n");
                    }
                    os.close();
               }
               catch(IOException ioe) {
                    System.err.println("SaveProgram:  i/o exception.");
               }
          }
     }
}
