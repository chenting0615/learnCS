package org.softwarehelps.learncs.TSP;

/* This file was automatically generated from a .mac file.*/

/*
    This program simulates many stations and how they route packets
    to each other.
*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class GraphTSP extends Frame
        implements ActionListener, ComponentListener, MouseListener,
                   MouseMotionListener, ItemListener
{
     Button runB, clearB, connectB, delLineB, loadB, saveB,
            newNodeB, delNodeB;

     TextField weightTF;
     Choice exampleCH, animationCH, speedCH;
     Color acolor = new Color(245,209,232);
     Node[] nodes;
     final static int MAXNODES = 25;

     int topnode = -1;             // contains index of top node in nodes[]
     int nextnodename = 65;

     boolean deleting, connecting;
     int sourceNode = -1;

     int x, y;
     int buttonHeight = 25;
     int pointx, pointy;

     Image buffer;
     Graphics gg;

     public GraphTSP() {
          setLayout(null);
          setSize(650, 500);

          setTitle("Traveling SalesPerson Problem");

          x = 7;
          y = 380;

          runB=new Button("Find Path");
          runB.addActionListener(this);
          runB.setBounds(x,y,70,buttonHeight);
          add(runB);

          int littlex = 7;
          loadB=new Button("Load");
          loadB.addActionListener(this);
          loadB.setBounds(littlex,y+runB.getSize().height+5,40,buttonHeight);
          add(loadB);
          littlex += loadB.getSize().width + 5;

          saveB=new Button("Save");
          saveB.addActionListener(this);
          saveB.setBounds(littlex,y+runB.getSize().height+5,40,buttonHeight);
          add(saveB);
          littlex += saveB.getSize().width + 5;

          newNodeB=new Button("New Node");
          newNodeB.addActionListener(this);
          newNodeB.setBounds(littlex,y+runB.getSize().height+5,70,buttonHeight);
          add(newNodeB);
          littlex += newNodeB.getSize().width + 5;

          delNodeB=new Button("Delete Node");
          delNodeB.addActionListener(this);
          delNodeB.setBounds(littlex,y+runB.getSize().height+5,80,buttonHeight);
          add(delNodeB);
          littlex += delNodeB.getSize().width + 5;

          clearB=new Button("Clear");
          clearB.addActionListener(this);
          clearB.setBounds(littlex,y+runB.getSize().height+5,45,buttonHeight);
          add(clearB);

          x += runB.getSize().width + 5;

          weightTF = new TextField(10);
          weightTF.setBackground(Color.white);
          weightTF.setBounds(x,y,45,buttonHeight);
          weightTF.setText("1");
          add(weightTF);
          x += weightTF.getSize().width + 5;

          connectB=new Button("Connect nodes");
          connectB.addActionListener(this);
          connectB.setBounds(x,y,110,buttonHeight);
          add(connectB);
          x += connectB.getSize().width + 5;

          delLineB=new Button("Delete lines");
          delLineB.addActionListener(this);
          delLineB.setBounds(x,y,80,buttonHeight);
          add(delLineB);
          x += delLineB.getSize().width + 5;

          exampleCH = new Choice();
          exampleCH.addItem("  -- examples -- ");
          exampleCH.addItem("Example 1 -- 5 nodes");
          exampleCH.addItem("Example 2 -- 12 nodes");
          exampleCH.setBounds(x,y,175,buttonHeight);
          exampleCH.addItemListener(this);
          add(exampleCH);
          x += exampleCH.getSize().width + 5;

          animationCH = new Choice();
          animationCH.addItem("Animate search for path");
          animationCH.addItem("Don't Animate search");
          animationCH.setBounds(exampleCH.getLocation().x,
                                exampleCH.getLocation().y+buttonHeight+5,
                                175,buttonHeight);
          animationCH.addItemListener(this);
          add(animationCH);
          x += animationCH.getSize().width + 5;

          addMouseListener(this);
          addMouseMotionListener(this);

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);

          deleting = false;
          connecting = false;

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                         System.exit(1);
                    }
               }    
          );

          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();
     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == runB) 
               runit();

          if (e.getSource() == loadB) 
               loadGraph();

          if (e.getSource() == saveB) 
               saveGraph();

          if (e.getSource() == clearB) 
               clear();

          if (e.getSource() == delLineB) {
               if (delLineB.getLabel().equals("Delete lines")) {
                    delLineB.setLabel("Stop deleting");
                    deleting = true;
               }
               else {
                    delLineB.setLabel("Delete lines");
                    deleting = false;
               }
          }

          if (e.getSource() == connectB) {
               if (connectB.getLabel().equals("Connect nodes")) {
                    connectB.setLabel("Stop connecting");
                    connecting = true;
               }
               else {
                    connectB.setLabel("Connect nodes");
                    connecting = false;
               }
          }

          if (e.getSource() == newNodeB) {
               newNode(150, 150);
          }

          if (e.getSource() == delNodeB) {
               if (topnode == -1) {
                    new Popup ("Click on a node first");
                    return;
               }
               delNode(topnode);
               topnode = -1;
          }
     }

     public void itemStateChanged(ItemEvent e) {
          if (e.getSource() == exampleCH) {
               loadExample(); 
               repaint();
          }
     }

     public void componentResized(ComponentEvent e) {}
     public void componentHidden(ComponentEvent e) {}
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}

     public void mouseMoved (MouseEvent e) {
          if (sourceNode != -1) {
               pointx = e.getX();
               pointy = e.getY();
               repaint();
          }
     }

     public void mouseDragged (MouseEvent e) {
          if (dragging) {
               int halfdiam = nodes[current].diameter/2;
               nodes[current].x = e.getX()-halfdiam;
               nodes[current].y = e.getY()-halfdiam;
               repaint();
          }
     }

     int current = -1;     // the index of the node being dragged
     boolean dragging = false;

     public void mouseEntered(MouseEvent e) {}
     public void mouseExited(MouseEvent e) {}
     public void mousePressed(MouseEvent e) {
          if (nodes == null) return;
          for (int i=0; i<nodes.length; i++) 
               if (nodes[i] != null && nodes[i].within(e.getX(), e.getY())) {
                    current = i;
                    dragging = true;
                    topnode = i;
                    return;
               }
     }
     public void mouseReleased(MouseEvent e) {
          dragging = false;
     }

     public void mouseClicked(MouseEvent e) {
          if(!e.isMetaDown())
               if(e.getClickCount() == 2) {
                    doubleClick(e);
                    return;
               }
          singleClick(e);
     }

     // The user clicked in empty space so make a new node here

     public void doubleClick(MouseEvent e) {
          newNode(e.getX(), e.getY());
     }

     public void newNode (int x, int y) {
          if (nodes == null) 
               nodes = new Node[MAXNODES];

          for (int i=0; i<nodes.length; i++) 
               if (nodes[i] == null) {
                     nodes[i] = new Node(this, ""+(char)nextnodename, x, y);
                     topnode = i;
                     nextnodename++;
                     repaint();
                     return;
               }

          // Need to grow the array

          int newpos = nodes.length;
          grow();
          nodes[newpos] = new Node (this, ""+(char)nextnodename, x, y);
          topnode = newpos;
          nextnodename++;
          repaint();
     }

     public void grow() {
          Node[] newnodes = new Node[nodes.length+20];
          for (int i=0; i<nodes.length; i++)
               newnodes[i] = nodes[i];
          nodes = newnodes;
     }

     public void singleClick(MouseEvent e) {
          if (nodes == null) return;
          for (int i=0; i<nodes.length; i++) {
               if (nodes[i] == null) continue;
               if (nodes[i].within(e.getX(), e.getY())) {
                    topnode = i;

                    if (deleting) {
                         for (int k=0; k<nodes[i].connectionTable.length(); k++) {
                              String link = nodes[i].connectionTable.get(k);
                              String nodename = U.getField(link,0);
                              int n = findindex(nodename);
                              if (n == -1) continue;
                              nodes[n].deleteConnection(nodes[i].id);
                         }
                         nodes[i].connectionTable = new StringList();
                         repaint();
                    }
                    else if (connecting) {
                         if (sourceNode == -1) 
                              sourceNode = i;
                         else {
                              if (sourceNode != i) {
                                   nodes[i].connect(sourceNode, 
                                                    U.atoi(weightTF.getText()));
                                   sourceNode = -1;
                                   repaint();
                              }
                         }
                    }
               }
          }
     }

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          gg.setColor(acolor);
          gg.fillRect(0,0,getSize().width, getSize().height);

          if (nodes != null) {
               gg.setColor(Color.black);
               for (int i=0; i<nodes.length; i++)
                    if (nodes[i] != null)
                         nodes[i].paint(gg);
          }

          gg.setColor(Color.black);
          gg.setFont(new Font("SansSerif", Font.BOLD, 36));
          gg.drawString("Traveling Salesperson Problem", 15, 65);

          if (sourceNode != -1) {
               int halfdiam = nodes[sourceNode].diameter/2;
               gg.drawLine (nodes[sourceNode].x+halfdiam, 
                            nodes[sourceNode].y+halfdiam,
                            pointx, pointy);
          }
                            
          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     private void delay (int milliseconds) {
          U.sleep(milliseconds);
     }

     private void help () {
          Popup p = new Popup("HELP\n",100,100,400,350,Color.pink);
          p.add("--------------------------------------------\n");
          p.add("This applet computes the shortest Hamilton circuit.\n");
     }

     public Node find (String id) {
          for (int i=0; i<nodes.length; i++)
               if (nodes[i] != null) {
                    if (nodes[i].id.equals(id))
                         return nodes[i];
               }
          return null;
     }

     public int findindex (String id) {
          for (int i=0; i<nodes.length; i++)
               if (nodes[i] != null) {
                    if (nodes[i].id.equals(id))
                         return i;
               }
          return -1;
     }

     public void delNode (int which) {
          if (nodes == null) return;

          // First delete all its connections

          for (int k=0; k<nodes[which].connectionTable.length(); k++) {
               String link = nodes[which].connectionTable.get(k);
               String nodename = U.getField(link,0);
               int n = findindex(nodename);
               if (n == -1) continue;
               nodes[n].deleteConnection(nodes[which].id);
          }

          // Now delete it from the nodes[] table

          nodes[which] = null;
          deleting = false;
          connecting = false;
          repaint();
     }

     public void clear() {
          nodes = new Node[5];
          nextnodename = 65;
          repaint();
     }

     private void loadExample() {
          tsp = null;
          if (exampleCH.getSelectedItem().startsWith("Example 1")) {
               nodes = new Node[5];
               int sep = 50;
               nodes[0] = new Node(this, "A", 2*sep, 2*sep);
               nodes[1] = new Node(this, "B", 4*sep, 2*sep);
               nodes[2] = new Node(this, "C", 1*sep, (int)(3.2*sep));
               nodes[3] = new Node(this, "D", 4*sep, 4*sep);
               nodes[4] = new Node(this, "E", 2*sep, 5*sep);
               nodes[0].connect(1, 1);
               nodes[0].connect(2, 5);
               nodes[0].connect(3, 3);
               nodes[1].connect(2, 2);
               nodes[1].connect(3, 6);
               nodes[2].connect(3, 4);
               nodes[2].connect(4, 1);
               nodes[3].connect(4, 8);
               nextnodename = 'F';
          }
          else if (exampleCH.getSelectedItem().startsWith("Example 2")) {
               nodes = new Node[12];
               nodes[0] = new Node(this, "A", 200, 180);
               nodes[1] = new Node(this, "B", 358, 138);
               nodes[2] = new Node(this, "C", 396, 225);
               nodes[3] = new Node(this, "D", 303, 282);
               nodes[4] = new Node(this, "E", 85, 298);
               nodes[5] = new Node(this, "F", 75, 119);
               nodes[6] = new Node(this, "G", 540, 104);
               nodes[7] = new Node(this, "H", 259, 98);
               nodes[8] = new Node(this, "I", 166, 344);
               nodes[9] = new Node(this, "J", 600, 292);
               nodes[10] = new Node(this, "K", 502, 355);
               nodes[11] = new Node(this, "L", 24, 218);
               find("A").connect("E", 3);
               find("A").connect("F", 1);
               find("A").connect("I", 5);
               find("A").connect("H", 6);
               find("B").connect("D", 5);
               find("B").connect("G", 5);
               find("B").connect("C", 1);
               find("B").connect("H", 3);
               find("C").connect("K", 2);
               find("C").connect("G", 4);
               find("C").connect("J", 9);
               find("C").connect("B", 1);
               find("C").connect("D", 8);
               find("D").connect("E", 2);
               find("D").connect("H", 3);
               find("D").connect("B", 5);
               find("D").connect("K", 5);
               find("D").connect("C", 8);
               find("E").connect("L", 2);
               find("E").connect("D", 2);
               find("E").connect("A", 3);
               find("E").connect("F", 4);
               find("E").connect("I", 9);
               find("F").connect("L", 2);
               find("F").connect("E", 4);
               find("F").connect("A", 1);
               find("F").connect("H", 9);
               find("G").connect("C", 4);
               find("G").connect("B", 5);
               find("G").connect("J", 5);
               find("H").connect("D", 3);
               find("H").connect("F", 9);
               find("H").connect("B", 3);
               find("H").connect("A", 6);
               find("I").connect("J", 9);
               find("I").connect("E", 9);
               find("I").connect("A", 6);
               find("J").connect("K", 2);
               find("J").connect("C", 9);
               find("J").connect("I", 9);
               find("J").connect("G", 5);
               find("K").connect("C", 2);
               find("K").connect("J", 2);
               find("K").connect("D", 5);
               find("L").connect("F", 2);
               find("L").connect("E", 2);
               nextnodename = 'M';
          }
     }

     public Graph convertToGraph() {
          int count = 0;
          for (int i=0; i<nodes.length; i++)
               if (nodes[i] != null)
                    count++;
          Graph g = new Graph(this, count);

          for (int i=0; i<nodes.length; i++)
               if (nodes[i] != null)
                    g.add(nodes[i].id);

          for (int i=0; i<nodes.length; i++)
               if (nodes[i] != null) {
                    StringList sl = nodes[i].connectionTable;
                    for (int k=0; k<sl.length(); k++) {
                         String name = U.getField(sl.get(k),0);
                         int weight = U.atoi(U.getField(sl.get(k),1));
                         g.add(nodes[i].id, name, weight);
                    }
               }

          return g;
     }

     TSP tsp = null;

     public void runit() {
          Graph g = convertToGraph();
          tsp = new TSP(g, this);
          tsp.findMinCircuit();
          repaint();
     }

     public void displayResults() {
          new Popup("Route = "+tsp.getRoute()+"\n"+
                    "Cost  = "+tsp.getRouteCost()+"\n"+
                    "Time to find = "+tsp.getCount());
     }

     public void loadGraph() {
          String text = "";

          FileDialog loadfile = new
               FileDialog(this, "Load Graph", FileDialog.LOAD);
          loadfile.setFile("*.txt");
          loadfile.show();

          String filename = loadfile.getFile();
          String directory = loadfile.getDirectory();
          if (filename == null)
               return;
          if (!filename.endsWith(".txt")) {
               System.err.println ("An graph file must have an extension of .txt.");
               return;
          }

          BufferedReader is;        // input stream

          if(filename == null || filename.length() == 0)
               return;

          try {

               is = new BufferedReader(new FileReader(directory+"\\"+filename));
               String line;
               line = is.readLine();
               int numnodes = U.atoi(line);
               nodes = new Node[numnodes];
               for (int i=0; i<numnodes; i++) {
                    line = is.readLine();
                    nodes[i] = new Node(this, U.getField(line,0),
                                        U.atoi(U.getField(line,1)),
                                        U.atoi(U.getField(line,2)));
                    nextnodename = 1+(int)(nodes[i].id.charAt(0));
                    StringList ct = new StringList();
                    nodes[i].connectionTable = ct;
                    while (true) {
                         line = is.readLine();
                         if (line.equals("-end-")) break;
                         ct.add(line);
                    }
               }
               is.close();
          }
          catch(IOException ioe) {
               System.err.println("LoadProgram:  i/o exception.");
               ioe.printStackTrace();
          }
          repaint();
          return;
     }

     public void saveGraph() {
          FileDialog savefile =
               new FileDialog(this,"Save Graph",FileDialog.SAVE);
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
               try {
                    os = new BufferedWriter(new FileWriter(fullname, false));

                    int count = 0;
                    for (int i=0; i<nodes.length; i++)
                         if (nodes[i] != null) count++;

                    os.write(count+"\n");
                    for (int i=0; i<count; i++) {
                         if (nodes[i] == null) continue;
                         os.write(nodes[i].id+" "+nodes[i].x+" "+nodes[i].y+"\n");
                         StringList ct = nodes[i].connectionTable;
                         for (int k=0; k<ct.length(); k++)
                              os.write(ct.get(k)+"\n");
                         os.write("-end-\n");
                    }
                    os.close();
               }
               catch(IOException ioe) {
                    System.err.println("SaveProgram:  i/o exception.");
               }
          }
     }
}
