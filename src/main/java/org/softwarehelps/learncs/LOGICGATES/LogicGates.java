package org.softwarehelps.learncs.LOGICGATES;

/* This file was automatically generated from a .mac file. */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LogicGates extends Frame
        implements ActionListener, ComponentListener, ItemListener,
                   MouseListener, MouseMotionListener
{
     Button runB, newB, labelB, delB, conB, disconB, clearB, editB, 
            refreshB, loadB, saveB, lineStyleB;
     Choice gatesCH, exampleCH;
     TextField msgTF;
     TextField tempInputField;

     Image buffer;
     Graphics gg;
     Color acolor = new Color(244,202,164);

     Vector gates;

     boolean running = false;
     boolean newing = false;            // making new gates?
     boolean dragging = false;          // dragging a gate?
     boolean deleting = false;          // deleting a gate?
     boolean connecting = false;        // dragging a gate?
     boolean connecting_2 = false;      // are we waiting for the dest gate?
     boolean disconnecting = false;     // disconnecting lines from a gate?
     public boolean diagonal = false;   // are we drawing stepped or diagonal
                                        // lines between boxes?
     boolean labeling = false;          // are we setting labels for boxes?

     int startx = -1;                   // used so we can draw incomplete
     int starty = -1;                   // lines when connecting gates
     int endx, endy;
 
     int distX, distY;                  // when clicked inside a box to begin
                                        // dragging, this shows how far the
                                        // cursor point was from the origin of
                                        // the box, so when we drag, it doesn't
                                        // jump at first

     int runtoggle = 0;                 // causes "Running..." to be colored
                                        // red, blue, red, blue... when
                                        // the simulator is running

     Gate topgate;

     public LogicGates() {
          gates = new Vector();

          setLayout(null);
          setSize(750, 550);
          setLocation(10,10);

          setTitle("Logic Gate Simulator");

          int y = 90;
          int x = 10;
          int buttonHeight = 27;
          int buttonWidth = 78;

          newB = new Button("New");
          newB.setBounds(x, y, 40, buttonHeight);
          newB.addActionListener(this);
          add(newB);

          int tinyx = x + newB.getSize().width+5;
          gatesCH = new Choice();
          gatesCH.addItem("AND");
          gatesCH.addItem("OR");
          gatesCH.addItem("NOT");
          gatesCH.addItem("NAND");
          gatesCH.addItem("NOR");
          gatesCH.addItem("XOR");
          gatesCH.addItem("Switch");
          gatesCH.addItem("Output");
          gatesCH.addItem("Connector");
          gatesCH.addItem("Truthtable");
          gatesCH.setBounds(tinyx, y, 100, buttonHeight);
          gatesCH.addItemListener(this);
          add(gatesCH);

          tinyx += gatesCH.getSize().width+10;

          Label lab1 = new Label("Examples: ");
          lab1.setBounds(tinyx, y-4, 60, buttonHeight);
          lab1.setBackground(acolor);
          add(lab1);

          tinyx += lab1.getSize().width;

          exampleCH = new Choice();
          exampleCH.addItem(" -- examples -- ");
          exampleCH.addItem("1 simple");
          exampleCH.addItem("2 add a connector");
          exampleCH.addItem("3 test nand, nor, xor");
          exampleCH.addItem("4 truth table example");
          exampleCH.addItem("5 SR memory latch");
          exampleCH.setBounds(tinyx, y, 200, buttonHeight);
          exampleCH.addItemListener(this);
          add(exampleCH);

          tinyx += exampleCH.getSize().width + 10;

          msgTF = new TextField(50);
          msgTF.setBounds(tinyx, y, 300, 27);
          msgTF.setBackground(Color.yellow);
          msgTF.addActionListener(this);
          add(msgTF);


          y += newB.getSize().height + 5;

          labelB = new Button("Label");
          labelB.setBounds(x, y, buttonWidth, buttonHeight);
          labelB.addActionListener(this);
          add(labelB);
          y += labelB.getSize().height + 5;

          delB = new Button("Delete");
          delB.setBounds(x, y, buttonWidth, buttonHeight);
          delB.addActionListener(this);
          add(delB);
          y += delB.getSize().height + 5;

          conB = new Button("Connect");
          conB.setBounds(x, y, buttonWidth, buttonHeight);
          conB.addActionListener(this);
          add(conB);
          y += conB.getSize().height + 5;

          disconB = new Button("Disconnect");
          disconB.setBounds(x, y, buttonWidth, buttonHeight);
          disconB.addActionListener(this);
          add(disconB);
          y += disconB.getSize().height + 5;

          clearB = new Button("Clear");
          clearB.setBounds(x, y, buttonWidth, buttonHeight);
          clearB.addActionListener(this);
          add(clearB);
          y += clearB.getSize().height + 5;

          runB = new Button("Run");
          runB.setBounds(x, y, buttonWidth, buttonHeight);
          runB.addActionListener(this);
          add(runB);
          y += runB.getSize().height + 5;

          editB = new Button("Edit TT");
          editB.setBounds(x, y, buttonWidth, buttonHeight);
          editB.addActionListener(this);
          add(editB);
          y += editB.getSize().height + 5;

          loadB = new Button("Load");
          loadB.setBounds(x, y, buttonWidth, buttonHeight);
          loadB.addActionListener(this);
          add(loadB);
          y += loadB.getSize().height + 5;

          saveB = new Button("Save");
          saveB.setBounds(x, y, buttonWidth, buttonHeight);
          saveB.addActionListener(this);
          add(saveB);
          y += saveB.getSize().height + 5;

          refreshB = new Button("Refresh");
          refreshB.setBounds(x, y, buttonWidth, buttonHeight);
          refreshB.addActionListener(this);
          add(refreshB);
          y += refreshB.getSize().height + 5;

          lineStyleB = new Button("Stepped");
          lineStyleB.setBounds(x, y, buttonWidth, buttonHeight);
          lineStyleB.addActionListener(this);
          add(lineStyleB);
          y += lineStyleB.getSize().height + 5;


          addMouseListener(this);
          addMouseMotionListener(this);

          addComponentListener(this);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                         System.exit(1);
                    }
               }    
          );

          setBackground(acolor);
          setVisible(true);
          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();
     }

     public void actionPerformed(ActionEvent e)
     {
          if (tempInputField != null) {
               if (e.getSource() == tempInputField) {
                    topgate.setLabel(tempInputField.getText());
                    remove(tempInputField);
                    tempInputField = null;
                    repaint();
                    return;
               }
               return;   // don't let them do anything else while a label
                         // input field is open.
          }
          if (e.getSource() == msgTF) {
               backDoorControl();
               msgTF.setText("");
               return;
          }
          if (e.getSource() == newB) {
               if (running) {
                    msgTF.setText("Stop running first.");
                    return;
               }
               if (!newing) {
                    newB.setLabel("*New");
                    newing = true;
                    msgTF.setText("Click once where you want the new gate to sit.");
               }
               else {
                    newB.setLabel("New");
                    newing = false;
                    msgTF.setText("");
               }
          }
          else if (e.getSource() == conB) {
               if (running) {
                    msgTF.setText("Stop running first.");
                    return;
               }
               if (!connecting) {
                    clearAllAsterisks();
                    conB.setLabel("*Connect");
                    connecting = true;
                    connecting_2 = false;
                    msgTF.setText("Click on source gate first, then destination gate.");
               }
               else {
                    conB.setLabel("Connect");
                    connecting = false;
                    connecting_2 = false;
                    msgTF.setText("");
               }
          }
          else if (e.getSource() == disconB) {
               if (running) {
                    msgTF.setText("Stop running first.");
                    return;
               }
               if (!disconnecting) {
                    clearAllAsterisks();
                    disconB.setLabel("*Disconnect");
                    disconnecting = true;
                    msgTF.setText("Click on a gate to disconnect.");
               }
               else {
                    disconB.setLabel("Disconnect");
                    disconnecting = false;
                    msgTF.setText("");
               }
          }
          else if (e.getSource() == clearB) {
               if (running) {
                    msgTF.setText("Stop running first.");
                    return;
               }
               gates = new Vector();
               clearAllAsterisks();
               repaint();
          }
          else if (e.getSource() == labelB) {
               if (running) {
                    msgTF.setText("Stop running first.");
                    return;
               }
               if (!labeling) {
                    clearAllAsterisks();
                    labelB.setLabel("*Label");
                    labeling = true;
                    msgTF.setText("Click on a gate to change its description");
               }
               else {
                    clearAllAsterisks();
                    labelB.setLabel("Label");
                    labeling = false;
                    msgTF.setText("");
               }
               repaint();
          }
          else if (e.getSource() == delB) {
               if (running) {
                    msgTF.setText("Stop running first.");
                    return;
               }
               if (!deleting) {
                    clearAllAsterisks();
                    delB.setLabel("*Delete");
                    deleting = true;
                    msgTF.setText("Click on gates to delete.");
               }
               else {
                    delB.setLabel("Delete");
                    deleting = false;
                    msgTF.setText("");
               }
               
               repaint();
          }
          else if (e.getSource() == editB) {
               if (running) {
                    msgTF.setText("Stop running first.");
                    return;
               }
               if (topgate == null) return;
               if (!topgate.type.equals("truthtable")) 
                    new Popup("You can only edit a truthtable's truth table.\n"+
                              "Click on a truthtable gate first.");
               else
                    new EditWindow(topgate);
          }
          else if (e.getSource() == runB) {
               if (running) {
                    running = false;
                    runB.setLabel("Run");
                    repaint();
               }
               else {
                    clearAllAsterisks();
                    running = true;
                    runB.setLabel("Stop");
                    run();
               }
          }
          else if (e.getSource() == loadB) {
               if (running) {
                    msgTF.setText("Stop running first.");
                    return;
               }
               loadCircuit();
          }
          else if (e.getSource() == saveB) {
               if (running) {
                    msgTF.setText("Stop running first.");
                    return;
               }
               saveCircuit();
          }
          else if (e.getSource() == refreshB)
               repaint();
          else if (e.getSource() == lineStyleB) {
               if (diagonal) {
                    diagonal = false;
                    lineStyleB.setLabel("Stepped");
               }
               else {
                    diagonal = true;
                    lineStyleB.setLabel("Diagonal");
               }
               repaint();
          }
     }

     private void clearAllAsterisks() {
          newB.setLabel("New");
          newing = false;

          labelB.setLabel("Label");
          labeling = false;

          conB.setLabel("Connect");
          connecting = false;
          connecting_2 = false;

          disconB.setLabel("Disconnect");
          disconnecting = false;

          delB.setLabel("Delete");
          deleting = false;

          delB.setLabel("Delete");
          deleting = false;
     }

     public void componentResized(ComponentEvent e) {
//        msgTF.setBounds(msgTF.getLocation().x, getSize().height-60, 300, 27);
//        repaint();
     }
     public void componentHidden(ComponentEvent e) {}
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}

     public void itemStateChanged (ItemEvent e) {
          if (e.getSource() == exampleCH) {
               String s = exampleCH.getSelectedItem();
               loadExample(U.atoi(U.getField(s,0)));
          }
          else if (e.getSource() == gatesCH) {
               String s = gatesCH.getSelectedItem();
               if (s.toLowerCase().equals("truthtable"))
                    msgTF.setText("Number of inputs? 2");
          }
     }

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          gg.setColor(acolor);
          gg.fillRect(0,0,getSize().width, getSize().height);

          gg.setColor(Color.blue);
          gg.setFont(new Font("SansSerif", Font.BOLD, 36));
          gg.drawString("LOGIC GATE SIMULATOR", 100, 80);

          Gate.resetMidpoints();

          for (int i=0; i<gates.size(); i++) {
               Gate gate = (Gate)gates.elementAt(i);
               gate.paint(gg, topgate == gate);
          }

          if (startx != -1) {
               gg.setColor(Color.red);
               gg.drawLine(startx, starty, endx, endy);
          }

          if (running) {
               if (runtoggle > 4)
                    gg.setColor(Color.red);
               else
                    gg.setColor(Color.blue);
               runtoggle++;
               if (runtoggle > 8)
                    runtoggle = 0;
               gg.setFont(new Font("SansSerif", Font.BOLD, 12));
               gg.drawString("Running...", 
                              runB.getLocation().x+runB.getSize().width + 10,
                              runB.getLocation().y+8);
          }

          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     public void mouseEntered(MouseEvent e) {}
     public void mouseExited(MouseEvent e) {}

     public void mousePressed(MouseEvent e) {
          // if a label inputfield is open, don't let them do anything else
          if (tempInputField != null) 
               return;

          if (!connecting) {
               for (int i=0; i<gates.size(); i++)  {
                    Gate g = (Gate)gates.elementAt(i);
                    if (g.within(e.getX(), e.getY())) {
                         topgate = g;
                         dragging = true;
                         distX = e.getX() - topgate.x;
                         distY = e.getY() - topgate.y;
                         repaint();
                         return;
                    }
               }
          }
     }

     public void mouseReleased(MouseEvent e) {
          dragging = false;
     }

     public void mouseClicked(MouseEvent e) {
          // if a label inputfield is open, don't let them do anything else
          if (tempInputField != null) 
               return;

          if(!e.isMetaDown())
               if(e.getClickCount() == 2) {
                    doubleClick(e);
                    return;
               }
          singleClick(e);
     }

     // The user clicked in empty space so make a new node here

     public void doubleClick(MouseEvent e) {
     }

     public void singleClick(MouseEvent e) {
          if (newing) {
               String s = gatesCH.getSelectedItem().toLowerCase();
               Gate topgate;
               if (s.equals("truthtable")) {
                    int numinputs = 2;
                    String num = msgTF.getText();
                    int n = num.indexOf("? ");
                    if (n > -1) {
                         num = num.substring(n+2);
                         if (num.length() > 0)
                              numinputs = U.atoi(num);
                    }
                    if (numinputs > 0 && numinputs < 7)
                         topgate = new Gate(this, s, e.getX(), e.getY(), 
                                            numinputs);
                    else
                         return;
               }
               else {
                    topgate = new Gate(this, s, e.getX(), e.getY());
               }
               gates.addElement(topgate);
               repaint();
          }
          else if (deleting) {
               for (int i=0; i<gates.size(); i++) {
                    Gate gate = (Gate)gates.elementAt(i);
                    if (gate.within(e.getX(), e.getY())) {
                         breakConnections(gate);
                         gates.removeElement(gate);
                    }
               }
               repaint();
          }
          else if (labeling) {
               msgTF.setText("Click on a gate to add a descriptive label");
               for (int i=0; i<gates.size(); i++) {
                    Gate gate = (Gate)gates.elementAt(i);
                    if (gate.within(e.getX(), e.getY())) {
                         topgate = gate;
                         tempInputField = new TextField(25);
                         add(tempInputField);
                         tempInputField.addActionListener(this);
                         tempInputField.setBounds(gate.x,gate.y-30,150,25);
                         tempInputField.requestFocus();
                         break;
                    }
               }
               repaint();
          }
          else if (connecting && !connecting_2) {
               topgate = null;
               for (int i=0; i<gates.size(); i++) {
                    Gate gate = (Gate)gates.elementAt(i);
                    if (gate.within(e.getX(), e.getY())) {
                         topgate = gate;
                         connecting_2 = true;
                         startx = gate.x + gate.width/2;
                         starty = gate.y + gate.height/2;
                    }
               }
               repaint();
          }
          else if (connecting && connecting_2) {
               for (int i=0; i<gates.size(); i++) {
                    Gate gate = (Gate)gates.elementAt(i);
                    if (gate.within(e.getX(), e.getY())) {
                         gate.connect(topgate);
                         startx = -1;
                         starty = -1;
                    }
               }
               connecting_2 = false;    // turn off in any event because
                                        // the user may have clicked in empty
                                        // space to stop the connection
               repaint();
          }
          else if (disconnecting) {
               topgate = null;
               for (int i=0; i<gates.size(); i++) {
                    Gate gate = (Gate)gates.elementAt(i);
                    if (gate.within(e.getX(), e.getY())) {
                         gate.inputgates = new Vector();
                         breakConnections(gate);
                         break;
                    }
               }
               repaint();
          }
          else {
               for (int i=0; i<gates.size(); i++) {
                    Gate gate = (Gate)gates.elementAt(i);
                    if (gate.type.equals("switch") &&
                        gate.withinCenter(e.getX(), e.getY())) {
                         gate.flipValue();
                         repaint();
                    }
               }
          }
     }

     public void breakConnections (Gate gate) {
          for (int i=0; i<gates.size(); i++) {
               Gate g2 = (Gate)gates.elementAt(i);
               if (g2.inputgates.contains(gate))
                    g2.inputgates.removeElement(gate);
          }
     }

     public void mouseMoved (MouseEvent e) {
          if (connecting_2) {
               endx = e.getX();
               endy = e.getY();
               repaint();
          }
     }

     public void mouseDragged (MouseEvent e) {
          if (dragging) {
               if (topgate != null) {
                    topgate.x = e.getX()-distX;
                    topgate.y = e.getY()-distY;
                    repaint();
               }
          }
     }

     public Gate findConnected (Gate gate) {
          for (int i=0; i<gates.size(); i++) {
               Gate g2 = (Gate)gates.elementAt(i);
               if (g2.inputgates.contains(gate))
                    return g2;
          }
          return null;
     }

     public boolean verify() {
          for (int i=0; i<gates.size(); i++) {
               Gate gate = (Gate)gates.elementAt(i);
               if (gate.equals("and") || gate.equals("or") ||
                   gate.equals("not") || gate.equals("output") ||
                   gate.equals("connector")) {
                    if (gate.inputgates.size() == 0) {
                         new Popup("Gate "+gate.type+" is missing\n"+
                                   "at least one required input.");
                         return false;
                    }
               }
          }
          return true;
     }

     public void run() {
          new Thread() {
               public void run() {
                    if (!verify()) return;
                    while (running) {
                         for (int i=0; i<gates.size(); i++) {
                              Gate gate = (Gate)gates.elementAt(i);
                              gate.compute();
                         }
                         for (int i=0; i<gates.size(); i++) {
                              Gate gate = (Gate)gates.elementAt(i);
                              gate.currentValue = gate.newValue;
                         }
                         repaint();
                         try {
                              Thread.sleep(200);
                         } catch (InterruptedException ie) {
                         }
                    }
               }
          }.start();
     }

     public void loadExample (int which) {
          // simple, 2 switches with an AND and a not

          if (which == 1) {           
               gates = new Vector();
               int x = 200;
               int y = 200;
               Gate g1 = new Gate(this, "switch", x, y);
               Gate g2 = new Gate(this, "switch", x, y+100);
               Gate g3 = new Gate(this, "and", x+100, y+50);
               Gate g4 = new Gate(this, "not", x+170, y+50);
               Gate g5 = new Gate(this, "output", x+280, y+50);
               g3.connect(g1);
               g3.connect(g2);
               g4.connect(g3);
               g5.connect(g4);
               gates.addElement(g1);
               gates.addElement(g2);
               gates.addElement(g3);
               gates.addElement(g4);
               gates.addElement(g5);
               repaint();
          }

          // 2, add a connector, same as above but two output boxes

          else if (which == 2) {
               gates = new Vector();
               gates.addElement(Gate.parseString(this,"0 switch 120 190"));
               gates.addElement(Gate.parseString(this,"1 switch 120 301"));
               gates.addElement(Gate.parseString(this,"2 and 251 246 0 1")); 
               gates.addElement(Gate.parseString(this,"3 not 397 246 5"));
               gates.addElement(Gate.parseString(this,"4 output 475 246 3"));
               gates.addElement(Gate.parseString(this,"5 connector 316 267 2"));
               gates.addElement(Gate.parseString(this,"6 output 474 383 5"));

               for (int i=0; i<gates.size(); i++) {
                    Gate g = (Gate)gates.elementAt(i);
                    reconnect(g, g.connectionList);
               }
               repaint();
          }

          // 3, test nand, nor, xor

          else if (which == 3) {
               gates = new Vector();

               gates.addElement(Gate.parseString(this,"0 nand 409 191 6 7"));
               gates.addElement(Gate.parseString(this,"1 nor 351 282 6 7"));
               gates.addElement(Gate.parseString(this,"2 xor 377 390 6 7"));
               gates.addElement(Gate.parseString(this,"3 output 508 191 0"));
               gates.addElement(Gate.parseString(this,"4 output 508 282 1"));
               gates.addElement(Gate.parseString(this,"5 output 508 390 2"));
               gates.addElement(Gate.parseString(this,"6 switch 118 171"));
               gates.addElement(Gate.parseString(this,"7 switch 118 397"));

               for (int i=0; i<gates.size(); i++) {
                    Gate g = (Gate)gates.elementAt(i);
                    reconnect(g, g.connectionList);
               }
               repaint();
          }

          // 4, truth table example

          else if (which == 4) {
               gates = new Vector();

               Gate g2;
               gates.addElement(g2 = Gate.parseString(this,
                                                 "0 truthtable 359 232 1 2"));
               g2.truthtable = "0  0  |  1\n"+
                               "0  1  |  1\n"+
                               "1  0  |  0\n"+
                               "1  1  |  1";
               gates.addElement(Gate.parseString(this,"1 switch 169 153"));
               gates.addElement(Gate.parseString(this,"2 switch 170 315"));
               gates.addElement(Gate.parseString(this,"3 output 534 245 0"));

               for (int i=0; i<gates.size(); i++) {
                    Gate g = (Gate)gates.elementAt(i);
                    reconnect(g, g.connectionList);
               }
               repaint();
          }

          // 5, SR memory latch

          else if (which == 5) {
             gates = new Vector();

             gates.addElement(Gate.parseString(this,"0 switch 147 177;S"));
             gates.addElement(Gate.parseString(this,"1 switch 147 374;R"));
             gates.addElement(Gate.parseString(this,"2 output 587 186 8;X"));
             gates.addElement(Gate.parseString(this,"3 output 587 367 10;Xnot"));
             gates.addElement(Gate.parseString(this,"4 nor 323 186 0 6"));
             gates.addElement(Gate.parseString(this,"5 nor 323 367 7 1"));
             gates.addElement(Gate.parseString(this,"6 connector 246 272 10"));
             gates.addElement(Gate.parseString(this,"7 connector 246 341 9"));
             gates.addElement(Gate.parseString(this,"8 connector 450 207 4"));
             gates.addElement(Gate.parseString(this,"9 connector 450 341 8"));
             gates.addElement(Gate.parseString(this,"10 connector 523 388 5"));

             for (int i=0; i<gates.size(); i++) {
                  Gate g = (Gate)gates.elementAt(i);
                  reconnect(g, g.connectionList);
             }
             repaint();
          }
     }

     public void loadCircuit() {
          String text = "";

          FileDialog loadfile = new
               FileDialog(this, "Load Circuit", FileDialog.LOAD);
          loadfile.setFile("*.txt");
          loadfile.show();

          String filename = loadfile.getFile();
          String directory = loadfile.getDirectory();
          if (filename == null)
               return;
          if (!filename.endsWith(".txt")) {
               System.err.println ("A circuit file must have an extension of .txt.");
               return;
          }

          BufferedReader is;        // input stream

          if(filename == null || filename.length() == 0)
               return;

          this.setTitle(filename);
          try {
               is = new BufferedReader(new FileReader(directory+"\\"+filename));
               String line;

               Gate g2;
               while ((line = is.readLine()) != null) {
                    gates.addElement(g2 = Gate.parseString(this,line));
                    if (g2.type.equals("truthtable")) {
                         g2.truthtable = "";
                         while ((line = is.readLine()) != null) {
                              if (line.startsWith("$")) break;
                              g2.truthtable += line + "\n";
                         }
                    }
               }

               is.close();

               for (int i=0; i<gates.size(); i++) {
                    Gate g = (Gate)gates.elementAt(i);
                    reconnect(g, g.connectionList);
               }

               repaint();
          }
          catch(IOException ioe) {
               System.err.println("LoadProgram:  i/o exception.");
               ioe.printStackTrace();
          }
          return;
     }

     public void saveCircuit() {
          FileDialog savefile =
               new FileDialog(this,"Save Circuit",FileDialog.SAVE);
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

                    for (int i=0; i<gates.size(); i++) {
                         Gate g = (Gate)gates.elementAt(i);
                         os.write(g.toString()+"\n");
                         if (g.type.equals("truthtable")) {
                              os.write(g.truthtable+"\n");
                              os.write("$\n");
                         }
                    }
                    os.close();
               }
               catch(IOException ioe) {
                    System.err.println("SaveProgram:  i/o exception.");
               }
          }
     }

     private void reconnect (Gate g, String s) {
          StringTokenizer st = new StringTokenizer(s);
          while (st.hasMoreTokens()) {
               String tok = st.nextToken();
               g.connect(findID(U.atoi(tok)));
          }
     }

     public Gate findID (int id) {
          for (int i=0; i<gates.size(); i++) {
               Gate g = (Gate)gates.elementAt(i);
               if (g.id == id)
                    return g;
          }
          return null;
     }

     private void backDoorControl() {
          // This is just a backdoor for me so I can debug!
          String s = msgTF.getText();
          if (s.equals("help"))
               new Popup("The backdoor command:\n"+
                         "    show gates");
          else if (s.equals("show gates"))
               showGatePositions();
     }

     private void showGatePositions() {
          String shown = "";
          for (int i=0; i<gates.size(); i++) {
               Gate g = (Gate)gates.elementAt(i);
               String typename = g.type;
               if (typename.length() < 10)
                    typename = typename 
                    + "                  ".substring(0,10-typename.length());
               shown = shown + "Gate id = "+ g.id + " " + typename +
                               " x="+ g.x + " y="+g.y+"\n";
          }
          new Popup(shown);
     }
}
