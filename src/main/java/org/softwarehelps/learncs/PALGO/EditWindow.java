package org.softwarehelps.learncs.PALGO;

/* This file was automatically generated from a .mac file.*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class EditWindow extends Frame implements ActionListener,
                                            ItemListener, ComponentListener
{
     Button runB, stopB, helpB, translateB, resetB, loadB, saveB;
     TextArea textTA;
     Choice syntaxCH;    
     Choice exampleCH;

     Image buffer;
     Graphics gg;
     Palgo parent;
     Color acolor = Color.blue.brighter().brighter();
     int spacing = 5;
     int buttonHeight = 26;

     public EditWindow (Palgo parent) {
          super("Palgo Editing Window");
          this.parent = parent;
          setLayout(null);

          textTA=new TextArea(5,40);
          textTA.setBounds(14,38,440,339);
          textTA.setText(parent.program);
          textTA.setBackground(Color.white);
          add(textTA);

          int x = 14;

          runB=new Button("Run");
          runB.addActionListener(this);
          runB.setBounds(x,392,35,buttonHeight);
          add(runB);
          x += runB.getSize().width + spacing;

          stopB=new Button("Stop");
          stopB.addActionListener(this);
          stopB.setBounds(x,392,40,buttonHeight);
          add(stopB);
          x += stopB.getSize().width + spacing;


          helpB=new Button("Help");
          helpB.addActionListener(this);
          helpB.setBounds(x,392,40,buttonHeight);
          add(helpB);
          x += helpB.getSize().width + spacing;

          resetB=new Button("Reset cells");
          resetB.addActionListener(this);
          resetB.setBounds(x,392,70,buttonHeight);
          add(resetB);
          x += resetB.getSize().width + spacing;

          translateB=new Button("Show C-like code");
          translateB.addActionListener(this);
          translateB.setBounds(x,392,100,buttonHeight);
          add(translateB);
          x += translateB.getSize().width + spacing;

          loadB=new Button("Load");
          loadB.addActionListener(this);
          loadB.setBounds(x,392,40,buttonHeight);
          add(loadB);
          x += loadB.getSize().width + spacing;

          saveB=new Button("Save");
          saveB.addActionListener(this);
          saveB.setBounds(x,392,40,buttonHeight);
          add(saveB);
          x += saveB.getSize().width + spacing;

          exampleCH = new Choice();
          exampleCH.setBounds(14, 428, 240, 25);
          exampleCH.addItemListener(this);
          setupExamples();
          add(exampleCH);

          syntaxCH = new Choice();
          syntaxCH.addItem("Human friendly syntax");
          syntaxCH.addItem("Underlying C-style syntax");
          syntaxCH.setBounds(exampleCH.getLocation().x+
                             exampleCH.getSize().width + 5,
                             428, 180, 25);
          syntaxCH.setBackground(Color.white);
          add(syntaxCH);

          setBackground(acolor);
          setLocation(250,150);
          setSize(480,400);

          setMajorSize();
          setButtonLocs();

          setVisible(true);

          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();

          addComponentListener(this);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         System.exit(0);
                    }
               }    
          );

     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == stopB) {
               parent.muststop = true;
               parent.running = false;
          }
          else if (e.getSource() == runB) {
               if (parent.running) {
                    new Popup("A program is already running.\n"+
                              "Click on STOP button first.");
                    return;
               }
               setParentProgram();
               parent.execute();
          }
          else if (e.getSource() == helpB) 
               new HelpWindow();
          else if (e.getSource() == translateB)
               new Popup(Translator.translate(textTA.getText()));
          else if (e.getSource() == loadB) {
               if (parent.running) {
                    new Popup("A program is already running.\n"+
                              "Click on STOP button first.");
                    return;
               }
               String newprog = loadProgram();
               if (newprog.length() > 0)
                    textTA.setText(newprog);
          }
          else if (e.getSource() == saveB)
               saveProgram(textTA.getText());
          else if (e.getSource() == resetB) {
               parent.clearCells();
               parent.repaint();
          }
     }
 
     public void setParentProgram () {
          String syntax = syntaxCH.getSelectedItem();
          String s = textTA.getText();
          if (syntax.startsWith("Human")) {
               parent.program = textTA.getText();
               parent.program2 = Translator.translate(s);
          }
          else {
               parent.program = textTA.getText();
               parent.program2 = textTA.getText();
          }
     }

     public void itemStateChanged (ItemEvent e) {
          if (e.getSource() == exampleCH) {
               String temp = exampleCH.getSelectedItem();
               textTA.setText(getExample(temp));
               setParentProgram();
               repaint();
          }
     }

     public void componentHidden(ComponentEvent e) { }
     public void componentMoved(ComponentEvent e) { }
     public void componentShown(ComponentEvent e) { }

     public void componentResized(ComponentEvent e) {
          setMajorSize();
     }

     private void setMajorSize() {
          int windowWidth = getSize().width;
          int windowHeight = getSize().height;
          textTA.setBounds(14, 35, windowWidth-30, windowHeight-110);
          setButtonLocs();
     }

     private void setButtonLocs() {
          int x = 14;
          int spacing = 5;

          int y = textTA.getLocation().y + textTA.getSize().height + 10;

          runB.setBounds(x,y,35,buttonHeight);
          x += runB.getSize().width + spacing;

          stopB.setBounds(x,y,40,buttonHeight);
          x += stopB.getSize().width + spacing;

          helpB.setBounds(x,y,40,buttonHeight);
          x += helpB.getSize().width + spacing;

          resetB.setBounds(x,y,70,buttonHeight);
          x += resetB.getSize().width + spacing;

          translateB.setBounds(x,y,100,buttonHeight);
          x += translateB.getSize().width + spacing;

          loadB.setBounds(x,y,40,buttonHeight);
          x += loadB.getSize().width + spacing;

          saveB.setBounds(x,y,40,buttonHeight);
          x += saveB.getSize().width + spacing;

          exampleCH.setBounds(14, y+buttonHeight+5, 240, 25);
          x += exampleCH.getSize().width + spacing;

          syntaxCH.setBounds(exampleCH.getLocation().x+
                             exampleCH.getSize().width + 5,
                             y+buttonHeight+5, 180, 25);
     }

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          gg.setColor(acolor);
          gg.fillRect(0,0,buffer.getWidth(this),buffer.getHeight(this));
          // Write to gg first
          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     public String loadProgram () {
          String text = "";

          FileDialog loadfile = new
               FileDialog(this, "Load Palgo Program", FileDialog.LOAD);
          loadfile.setFile("*.*");
          loadfile.show();

          String filename = loadfile.getFile();
          String directory = loadfile.getDirectory();
          if (filename == null)
               return "";
          if (!filename.endsWith(".palgo")) 
               System.err.println ("A PALGO program should have an extension of .palgo.");

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
               System.err.println("LoadProgram:  i/o exception.");
          }
          return text;
     }

     public void saveProgram (String text) {
          FileDialog savefile =
               new FileDialog(this,"Save Palgo Program",FileDialog.SAVE);
          savefile.setFile("*.*");
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
                    System.err.println("SaveProgram:  i/o exception.");
               }
          }
     }

     public void setupExamples() {
          exampleCH.addItem(" -- examples -- ");
          exampleCH.addItem("Example 1 -- simple square");
          exampleCH.addItem("Example 2 -- marquee color");
          exampleCH.addItem("Example 3 -- random colored dots");
          exampleCH.addItem("Example 4 -- squares");
          exampleCH.addItem("Example 5 -- more squares");
          exampleCH.addItem("Example 6 -- red and yellow");
          exampleCH.addItem("Example 7 -- GCD");
          exampleCH.addItem("Example 8 -- Sorting");
          exampleCH.addItem("Example 9 -- Table Searching");
     }

     public String getExample(String name) {
          String program = "";
          if (name.startsWith("Example 1"))
               program = "color \"red\"\n"+
                         "pen down\n"+
                         "down 10\n"+
                         "color \"blue\"\n"+
                         "right 10\n"+
                         "color \"green\"\n"+
                         "up 10\n"+
                         "color \"yellow\"\n"+
                         "left 10\n";
          else if (name.startsWith("Example 2"))
               program = "color \"red\"\n"+
                         "pen down\n"+
                         "goto 0 5\n"+
                         "r=255\n"+
                         "g=15\n"+
                         "b=100\n"+
                         "repeat 100 times\n"+
                         "    color r g b\n"+
                         "    r = (r + 30) % 256\n"+
                         "    g = (r + 30) % 256\n"+
                         "    b = (r + 30) % 256\n"+
                         "    draw\n" +
                         "    right 1\n"+
                         "    wait 20\n"+
                         "end\n";
          else if (name.startsWith("Example 3"))
               program = "pen down\n"+
                         "repeat 1000 times\n"+
                         "    r = random(256)\n"+
                         "    g = random(256)\n"+
                         "    b = random(256)\n"+
                         "    color r g b\n"+
                         "    k = random(20)\n"+
                         "    m = random(20)\n"+
                         "    goto k m\n" +
                         "    draw\n" +
                         "    wait 100\n"+
                         "end\n";
          else if (name.startsWith("Example 4"))
               program = ""+
                         "define square (n)\n"+
                         "    pen down\n"+
                         "    n = n - 1\n"+
                         "    down n\n"+
                         "    right n\n"+
                         "    up n\n"+
                         "    left n\n"+
                         "end\n"+
                         "goto 0 0\n"+
                         "color \"red\"\n"+
                         "square(10)\n"+
                         "goto 2 2\n"+
                         "color \"yellow\"\n"+
                         "square(4)\n" +
                         "goto 12 12\n"+
                         "color \"blue\"\n"+
                         "square(5)";
          else if (name.startsWith("Example 5"))
               program = ""+
                         "define square (n)\n"+
                         "    pen down\n"+
                         "    n = n - 1\n"+
                         "    down n\n"+
                         "    right n\n"+
                         "    up n\n"+
                         "    left n\n"+
                         "end\n"+
                         "clear\n"+
                         "r = 255\n"+
                         "g = 150\n"+
                         "b = 40\n"+
                         "color r g b\n"+
                         "goto 7 7\n"+
                         "for i=0 to 5\n"+
                         "    goto (7-i) (7-i)\n" +
                         "    square(10-i)\n" +
                         "    r = (r + 100) % 256\n"+
                         "    g = (g + 100) % 256\n"+
                         "    b = (b + 100) % 256\n"+
                         "    color r g b\n"+
                         "end\n";
          else if (name.startsWith("Example 6"))
               program = ""+
                         "c=\"red\"\n"+
                         "pen down\n"+
                         "goto 0 0\n"+
                         "n = 10\n"+
                         "repeat 100 times\n"+
                         "    color c\n"+
                         "    right n\n"+
                         "    down n\n"+
                         "    left n\n"+
                         "    up n\n"+
                         "    n = n - 1\n"+
                         "    if n == 0 then\n"+
                         "         if c == \"red\" then\n"+
                         "              c=\"yellow\"\n"+
                         "         else\n"+
                         "              c=\"red\"\n"+
                         "         end\n"+
                         "         n = 10\n"+
                         "    end\n"+
                         "    wait 200\n"+
                         "end\n";
          else if (name.startsWith("Example 7"))
               program = ""+
                         "print \"Enter first integer\"\n"+
                         "starting_a = input_number\n"+
                         "print \"Enter second integer\"\n"+
                         "starting_b = input_number\n"+
                         "a = starting_a\n"+
                         "b = starting_b\n"+
                         "while a != b\n"+
                         "    if a < b then\n"+
                         "         b = b - a\n"+
                         "    else\n"+
                         "         a = a - b\n"+
                         "    end\n"+
                         "end\n"+
                         "print \"The gcd of \"+starting_a+\" and \"+starting_b+\"=\"+a\n";
          else if (name.startsWith("Example 8"))
               program = ""+
                         "a = list()\n"+
                         "a[0] = 5\n"+
                         "a[1] = 17\n"+
                         "a[2] = 18\n"+
                         "a[3] = 3\n"+
                         "a[4] = 9\n"+
                         "a[5] = 44\n"+
                         "a[6] = 10\n"+
                         "len = length(a)\n"+
                         "print a\n"+
                         "for i=0 to len-1\n"+
                         "      for j=i+1 to len\n"+
                         "             if a[i] > a[j] then\n"+
                         "                      temp = a[i]\n"+
                         "                      a[i] = a[j]\n"+
                         "                      a[j] = temp\n"+
                         "             end\n"+
                         "      end\n"+
                         "end\n" +
                         "print a";

          else if (name.startsWith("Example 9"))
               program = ""+
                         "person[0].name = \"Mark\"\n"+
                         "person[0].age = 45\n"+
                         "person[0].favorite = \"cats\"\n"+
                         "person[1].name = \"Kathy\"\n"+
                         "person[1].age = 36\n"+
                         "person[1].favorite = \"horses\"\n"+
                         "person[2].name = \"Madeline\"\n"+
                         "person[2].age = 7\n"+
                         "person[2].favorite = \"dogs\"\n"+
                         "person[3].name = \"Anthony\"\n"+
                         "person[3].age = 9\n"+
                         "person[3].favorite = \"cats\"\n"+
                         "\n"+
                         "for i=0 to 3\n"+
                         "     if  person[i].favorite == \"cats\" then\n"+
                         "          print person[i].name\n"+
                         "     end\n"+
                         "end";
          return program;
     }

}
