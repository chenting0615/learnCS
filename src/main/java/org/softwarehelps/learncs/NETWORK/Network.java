/*
    This program simulates many stations and how they route packets
    to each other.
*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class Network extends Applet 
        implements ActionListener, ComponentListener, MouseListener,
                   MouseMotionListener, ItemListener
{
     Button runB, stopB, helpB, newB, editB;
     Choice exampleCH, methodCH;
     Color acolor = new Color(245,209,232);
     Node[] nodes;
     final static int MAXNODES = 25;

     boolean mustStop, running;

     int x, y;
     int buttonHeight = 25;

     int topnode = -1;
     int nextNodeNumber = 1;

     Image buffer;
     Graphics gg;

     public void init() {
          setLayout(null);
          setSize(650, 500);

          x = 7;
          y = 380;

          runB=new Button("Run");
          runB.addActionListener(this);
          runB.setBounds(x,y,40,buttonHeight);
          add(runB);
          x += runB.getSize().width + 5;

          stopB=new Button("Stop");
          stopB.addActionListener(this);
          stopB.setBounds(x,y,40,buttonHeight);
          stopB.setEnabled(false);
          add(stopB);
          x += stopB.getSize().width + 5;

          helpB=new Button("Help");
          helpB.addActionListener(this);
          helpB.setBounds(x,y,40,buttonHeight);
          add(helpB);
          x += helpB.getSize().width + 5;

          exampleCH = new Choice();
          exampleCH.addItem(" -- examples --");
          exampleCH.addItem("Example 1 -- very simple");
          exampleCH.addItem("Example 2 -- chain");
          exampleCH.addItem("Example 3 -- ring");
          exampleCH.addItem("Example 4 -- star");
          exampleCH.addItem("Example 5 -- complex");
          exampleCH.setBounds(x,y,175,buttonHeight);
          exampleCH.addItemListener(this);
          add(exampleCH);
          x += exampleCH.getSize().width + 5;

          methodCH = new Choice();
          methodCH.addItem("Generate packets continuously");
          methodCH.addItem("Generate when I click on a node");
          methodCH.setBounds(x,y,200,buttonHeight);
          add(methodCH);
          x += methodCH.getSize().width + 5;

          x = 7;
          y += runB.getSize().height+5;

          newB=new Button("New Node");
          newB.addActionListener(this);
          newB.setBounds(x,y,70,buttonHeight);
          add(newB);
          x += newB.getSize().width + 5;

          editB=new Button("Edit Node");
          editB.addActionListener(this);
          editB.setBounds(x,y,70,buttonHeight);
          add(editB);
          x += editB.getSize().width + 5;

          addMouseListener(this);
          addMouseMotionListener(this);

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);

          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();
     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == runB) 
               runme();

          if (e.getSource() == stopB) 
               stop();

          if (e.getSource() == helpB) 
               help();

          if (e.getSource() == newB) 
               newNode(150, 150);

          if (e.getSource() == editB) {
               if (topnode > -1)
                    new NodeWindow (nodes[topnode], this);
          }
     }

     public void itemStateChanged(ItemEvent e) {
          if (e.getSource() == exampleCH) {
               loadExample(); 
               repaint();
          }
     }

     public void runme() {
          if (nodes == null || nodes.length == 0) {
               new Popup("You must create some nodes first\n"+
                         "or select and load an example.\n");
               return;
          }
          mustStop = false;
          stopB.setEnabled(true);
          runB.setEnabled(false);
          running = true;
          runaux();
     }

     public void stop() {
          mustStop = true;
          stopB.setEnabled(false);
          runB.setEnabled(true);
          running = false;
     }

     public void componentResized(ComponentEvent e) {}
     public void componentHidden(ComponentEvent e) {}
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}

     public void mouseMoved (MouseEvent e) {
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

     public void doubleClick(MouseEvent e) {
          if (nodes == null) 
               nodes = new Node[MAXNODES];

          for (int i=0; i<nodes.length; i++) 
               if (nodes[i] != null && nodes[i].within(e.getX(), e.getY())) {
                    new NodeWindow (nodes[i], this);
                    return;
               }

          // The user clicked in empty space so make a new node here

          newNode (e.getX(), e.getY());
     }

     public void newNode(int x, int y) {
          if (nodes == null) 
               nodes = new Node[MAXNODES];

          for (int i=0; i<nodes.length; i++) 
               if (nodes[i] == null) {
                     nodes[i] = new Node(this, "0.0.0."+nextNodeNumber,x,y);
                     nextNodeNumber++;
                     repaint();
                     return;
               }
         
          grow();
          nodes[nodes.length-1] = new Node(this,"0.0.0."+nextNodeNumber,x,y);
          nextNodeNumber++;
          repaint();
     }

     public void grow() {
          Node[] newnodes = new Node[nodes.length*2];
          for (int i=0; i<nodes.length; i++)
               newnodes[i] = nodes[i];
          nodes = newnodes;
     }

     public void singleClick(MouseEvent e) {
          if (nodes == null) return;

          for (int i=0; i<nodes.length; i++) {
               if (nodes[i] == null) continue;
               if (nodes[i].within(e.getX(), e.getY())) 
                    topnode = i;
          }

          if (running && methodCH.getSelectedItem().indexOf("click") > -1) {
               for (int i=0; i<nodes.length; i++) {
                    if (nodes[i] == null) continue;
                    if (nodes[i].within(e.getX(), e.getY())) {
                         nodes[i].mustGenerate = true;
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
          gg.drawString("Network Routing", 15, 45);

          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     boolean genContinuously = false;

     public void runaux() {
          if (methodCH.getSelectedItem().indexOf("continuously") > -1)
               genContinuously = true;
          else
               genContinuously = false;
          new Thread() {
               public void run() {
                    while (true) {
                         if (mustStop) break;

                         for (int i=0; i<nodes.length; i++) {
                              if (nodes[i] == null) continue;
                              if (genContinuously)
                                   nodes[i].generatePacket("random");
                              else if (nodes[i].mustGenerate) {
                                   nodes[i].generatePacket("ok");
                                   nodes[i].mustGenerate = false;
                              }
                              nodes[i].route();
                         }

                         repaint();
                         U.sleep(200);

// new
                         for (int i=0; i<nodes.length; i++) {
                              if (nodes[i] == null) continue;
                              nodes[i].mycolor = Color.blue;
                         }

                         repaint();
                         U.sleep(200);
// end new
                    }
               }
          }.start();
     }

     private void delay (int milliseconds) {
          U.sleep(milliseconds);
     }

     private void help () {
          Popup p = new Popup("",100,100,400,550,Color.pink);
          p.add("This applet shows how packets get routed in a network.\n");
          p.add("Each blue disk represents one host and has an IP address.\n");
          p.add("(In network terminology, \"node\" means the same thing\n");
          p.add("as \"host\".)\n");
          p.add("\n");
          p.add("To change its properties, double click on it, or click\n");
          p.add("once on the host and click the \"Edit Node\" button.\n");
          p.add("A separate editing window appears, where you can change\n");
          p.add("characteristics of the node and also see the packets it\n");
          p.add("has received.\n");
          p.add("\n");
          p.add("To experiment, change either the Connection Table or\n");
          p.add("the Routing Table.  The Connection Table says which nodes\n");
          p.add("have a direct line to this node.  A black line is drawn\n");
          p.add("between nodes that are connected directly.\n");
          p.add("\n");
          p.add("The Routing Table is used when routing decisions are made.\n");
          p.add("When a packet arrives at a node, and the destination of\n");
          p.add("the packet is not the same address as that node, the node\n");
          p.add("makes a routing decision.  That is, it picks another node\n");
          p.add("to which it is directly connected and sends the packet out\n");
          p.add("on the wire to that node.\n");
          p.add("\n");
          p.add("However, the best routing decision picks the directly\n");
          p.add("connected node so that the packet will arrive at its\n");
          p.add("destination in the least amount of time (as measured in\n");
          p.add("hops, or wires that it goes across.)\n");
          p.add("\n");
          p.add("This is where the Routing Table is important.  If the\n");
          p.add("Routing Table is incorrect, packets may never arrive at\n");
          p.add("their destination or may take too long.\n");
          p.add("\n");
          p.add("In real systems, such as the Internet's main backbone,\n");
          p.add("routing tables are too important to leave to humans, and\n");
          p.add("they are too complex to write by hand.  Software actually\n");
          p.add("fills in the table's entries, and it continually monitors\n");
          p.add("traffic conditions and makes changes if certain wires get\n");
          p.add("clogged with packets.\n");
          p.add("\n");
          p.add("The nodes in this applet do not dynamically adjust their\n");
          p.add("routing tables.  This is where you come in!  Experiment,\n");
          p.add("and see if you can be a good traffic cop.\n");
          p.add("\n");
          p.add("The other pull-down menu allows you to generate random\n");
          p.add("packets continually, or to give you finer control.\n");
          p.add("In this second option, a packet will only be generated\n");
          p.add("at a node when you click on the sending node.\n");
          p.add("\n");
          p.add("Notice that each node only has one destination (though you\n");
          p.add("can change that.)  This is unrealistic, but it allows you\n");
          p.add("to slow down the process and watch the network, verifying\n");
          p.add("that packets are getting to their right destinations.\n");
     }

     private void loadExample() {
          // simple 2-nodes

          if (exampleCH.getSelectedItem().startsWith("Example 1")) {
               nodes = new Node[MAXNODES];
               nodes[0] = new Node(this, "138.92.0.5", 30, 150);
               nodes[1] = new Node(this, "138.92.6.17", 200, 150);
               nodes[0].connect(nodes[1].id);
               nodes[0].dest = nodes[1].id;
               nodes[1].dest = nodes[0].id;
               repaint();
               return;
          }

          // chain

          if (exampleCH.getSelectedItem().startsWith("Example 2")) {
               nodes = new Node[MAXNODES];
               nodes[0] = new Node(this, "138.92.0.5", 30, 150);
               nodes[1] = new Node(this, "138.92.6.17", 180, 150);
               nodes[2] = new Node(this, "138.92.4.96", 330, 150);
               nodes[0].connect(nodes[1].id);
               nodes[1].connect(nodes[2].id);
               nodes[0].dest = nodes[2].id;
               repaint();
               return;
          }

          // Ring

          if (exampleCH.getSelectedItem().startsWith("Example 3")) {
               nodes = new Node[MAXNODES];
               nodes[0] = new Node(this, "138.92.0.5", 74, 296);
               nodes[1] = new Node(this, "138.92.6.17", 20, 100);
               nodes[2] = new Node(this, "159.121.2.13", 172, 71);
               nodes[3] = new Node(this, "159.121.66.98", 295, 100);
               nodes[4] = new Node(this, "37.61.25.46", 282, 260);
     
               nodes[0].connect(nodes[1].id);
               nodes[1].connect(nodes[2].id);
               nodes[2].connect(nodes[3].id);
               nodes[3].connect(nodes[4].id);
               nodes[4].connect(nodes[0].id);

               nodes[1].dest = nodes[3].id;
               nodes[2].dest = nodes[0].id;

               nodes[0].clearRoutes();
               nodes[0].addRoute ("*", "138.92.6.17");
               nodes[1].clearRoutes();
               nodes[1].addRoute ("*", "159.121.2.13");
               nodes[2].clearRoutes();
               nodes[2].addRoute ("*", "159.121.66.98");
               nodes[3].clearRoutes();
               nodes[3].addRoute ("*", "37.61.25.46");
               nodes[4].clearRoutes();
               nodes[4].addRoute ("*", "138.92.0.5");

               repaint();
               return;
          }

          // Star

          if (exampleCH.getSelectedItem().startsWith("Example 4")) {
               nodes = new Node[MAXNODES];

               nodes[0] = new Node(this, "138.92.0.5", 200, 200);
               nodes[1] = new Node(this, "138.92.6.17", 20, 200);
               nodes[2] = new Node(this, "159.121.2.13", 400, 200);
               nodes[3] = new Node(this, "159.121.66.98", 200, 70);
               nodes[4] = new Node(this, "37.61.25.46", 200, 300);
               nodes[5] = new Node(this, "126.14.5.46", 75, 75);
               nodes[6] = new Node(this, "8.10.20.25", 350, 300);
     
               nodes[0].connect(nodes[1].id);
               nodes[0].connect(nodes[2].id);
               nodes[0].connect(nodes[3].id);
               nodes[0].connect(nodes[4].id);
               nodes[0].connect(nodes[5].id);
               nodes[0].connect(nodes[6].id);

               nodes[2].dest = "126.14.5.46";
               nodes[6].dest = "159.121.66.98";

               repaint();
               return;
          }

          // complex

          if (exampleCH.getSelectedItem().startsWith("Example 5")) {
               nodes = new Node[MAXNODES];
               nodes[0] = new Node(this, "138.92.0.5", 200, 300);
               nodes[1] = new Node(this, "138.92.6.17", 20, 100);
               nodes[2] = new Node(this, "159.121.2.13", 153, 74);
               nodes[3] = new Node(this, "159.121.66.98", 300, 140);
               nodes[4] = new Node(this, "37.61.25.46", 52, 272);
               nodes[5] = new Node(this, "159.121.66.12", 325, 290);
     
               nodes[0].connect("138.92.6.17");
               nodes[0].connect("159.121.2.13");
               nodes[1].connect("159.121.2.13");
               nodes[2].connect("159.121.66.98");
               nodes[0].connect("37.61.25.46");
               nodes[5].connect(nodes[3].id);

               nodes[1].dest = "159.121.66.12";

               repaint();
               return;
          }

     }

     public Node find (String ipAddress) {
          for (int i=0; i<nodes.length; i++)
               if (nodes[i] != null) {
                    if (nodes[i].id.equals(ipAddress))
                         return nodes[i];
               }
          return null;
     }

     public static int convert (String s) {
          if (s.equals("Only when I click on the node"))
               return 0;
          if (s.equals("Continuously"))
               return 1;
          if (s.equals("Randomly"))
               return 2;
          return 0;
     }

     public static String convert (int method) {
          switch (method) {
              case 0: return "Only when I click on the node";
              case 1: return "Continuously";
              case 2: return "Randomly";
              default: return "Randomly";
          }
     }
}
