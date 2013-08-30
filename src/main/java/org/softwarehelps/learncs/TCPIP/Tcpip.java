/*
    This program simulates two stations communicating over a reliable
    connection, like a Tcp connection. One station (#0) sends packets
    to the other (#1).  The packets have sequence numbers on them.
   
    Each packet must be acked right away, or nacked if it was
    corrupted in-flight.  A simple checksum is used to detect
    transmission errors (which are simulated by randomly altering
    some of the packets.)

    If a packet gets lost entirely the transmitter times out and
    resends it.
*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class Tcpip extends Applet 
        implements ActionListener, ComponentListener, ItemListener,
                   MouseListener
{
     TextArea status;          
     TextField inputTF;              // where the user types a message
     TextField tf1, tf2;             // textfields where the outgoing
                                     // message is packetized (tf1) and
                                     // reassembled (tf2)
     Label lab;

     Button runB, sendB, stopB, helpB, exampleB;
     Color acolor = new Color(245,209,232);
     Node[] nodes;

     boolean mustStop;

     int x, y;
     int buttonHeight = 25;

     Image buffer;
     Graphics gg;

     Choice damageCH;
     boolean doDamage;

     public void init() {
          setLayout(null);
          setSize(650, 500);

          nodes = new Node[2];
          nodes[0] = new Node(/* id */ 0, 
                              /* rightbound direction */ 0,
                              /* parent */ this,
                              /* position (x, y) */ 20, 70,
                              /* diameter */ 65);
          nodes[1] = new Node(/* id */ 1, 
                              /* rightbound direction */ 1,
                              /* parent */ this,
                              /* position (x, y) */ 500, 70,
                              /* diameter */ 65);

          x = 20;
          y = 160;

/*
          runB=new Button("Run");
          runB.addActionListener(this);
          runB.setBounds(x,y,40,buttonHeight);
          add(runB);
          x += runB.getSize().width + 5;
*/

          sendB=new Button("Send a message");
          sendB.addActionListener(this);
          sendB.setBounds(x,y,110,buttonHeight);
          sendB.setEnabled(true);
          add(sendB);
          x += sendB.getSize().width + 5;

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

          exampleB=new Button("Example");
          exampleB.addActionListener(this);
          exampleB.setBounds(x,y,70,buttonHeight);
          add(exampleB);
          x += exampleB.getSize().width + 5;

          damageCH = new Choice();
          damageCH.addItem("Leave packets undamaged");
          damageCH.addItem("Damage packets that are touched");
          damageCH.addItem("Delete packets that are touched");
          doDamage = false;
          damageCH.addItemListener(this);
          damageCH.setBounds(x,y,200,25);
          add(damageCH);

          y += exampleB.getSize().height + 35;

          lab = new Label("Your message: ");
          lab.setFont(new Font("SansSerif", Font.PLAIN, 12));
          lab.setBounds(5, y, 90, 30);
          lab.setBackground(acolor);
          add(lab);

          inputTF = new TextField(100);
          inputTF.setBackground(Color.white);
          inputTF.setBounds(lab.getSize().width+5,y,525,30);
          add(inputTF);

          y += inputTF.getSize().height + 5;

          lab = new Label("To be sent: ");
          lab.setFont(new Font("SansSerif", Font.PLAIN, 12));
          lab.setBounds(5, y, 90, 30);
          lab.setBackground(acolor);
          add(lab);

          tf1=new TextField(100);
          tf1.setBackground(Color.white);
          tf1.setBounds(lab.getSize().width+5,y,525,30);
          add(tf1);

          y += tf1.getSize().height + 5;

          lab = new Label("Received: ");
          lab.setFont(new Font("SansSerif", Font.PLAIN, 12));
          lab.setBounds(5, y, 90, 30);
          lab.setBackground(acolor);
          add(lab);

          tf2=new TextField(100);
          tf2.setBackground(Color.white);
          tf2.setBounds(lab.getSize().width+5,y,525,30);
          add(tf2);

          y += tf2.getSize().height + 30;

          lab = new Label("Status: ");
          lab.setFont(new Font("SansSerif", Font.PLAIN, 12));
          lab.setBounds(5, y, 90, 30);
          lab.setBackground(acolor);
          add(lab);

          status=new TextArea(8,35);
          status.setBackground(Color.white);
          status.setBounds(lab.getSize().width+5,y,525,120);
          add(status);

          addMouseListener(this);

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);

          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();
     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == stopB) 
               stop();

          if (e.getSource() == sendB) {
               if (inputTF.getText().length() == 0) {
                    inputTF.setText("ENTER YOUR MESSAGE HERE");
                    inputTF.select(0,23);
                    return;
               }
               else {
                    nodes[0].inject(inputTF.getText());
                    inputTF.setText("");
               }
               run();
          }

          if (e.getSource() == exampleB) 
               loadExample();

          if (e.getSource() == helpB) 
               help();
     }

     public void run() {
          mustStop = false;
          nodes[0].start();
          nodes[1].start();
          sendB.setEnabled(false);
          stopB.setEnabled(true);
     }

     public void stop() {
          mustStop = true;
          sendB.setEnabled(false);
          stopB.setEnabled(false);
          sendB.setEnabled(true);
     }

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
               if(e.getClickCount() == 2) {
                    doubleClick(e);
                    return;
               }
          singleClick(e);
     }

     public void doubleClick(MouseEvent e) {
     }

     public void singleClick(MouseEvent e) {
          for (int i=0; i<2; i++) {
               if (nodes[i].currentPkt != null && 
                   nodes[i].currentPkt.within(e.getX(),e.getY())) {
                    String damage = damageCH.getSelectedItem();
                    if (damage.startsWith("Damage"))
                         nodes[i].currentPkt.damage();
                    else if (damage.startsWith("Delete")) {
                         nodes[i].currentPkt = null;
                    }
               }
          }
     }


     public void itemStateChanged (ItemEvent e) {
     }

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          gg.setColor(acolor);
          gg.fillRect(0,0,getSize().width, getSize().height);

          gg.setColor(Color.black);
          // the wire between the nodes
          int halfDiameter = nodes[0].diameter/2;
          int ypos = nodes[0].y + halfDiameter;
          gg.drawLine (nodes[0].x+nodes[0].diameter,ypos,
                       nodes[1].x,ypos);

          for (int i=0; i<2; i++)
               nodes[i].draw(gg);

          gg.setColor(Color.black);
          gg.setFont(new Font("SansSerif", Font.BOLD, 36));
          gg.drawString("Reliable Connection Simulator", 15, 45);


          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     public void msg (String s) {
          if (status.getText().length() == 0)
               status.setText(s);
          else
               status.setText(status.getText()+"\n"+s);
          int n = status.getText().length();
          status.select(n,n);
     }

     private void delay (int milliseconds) {
          U.sleep(milliseconds);
     }

     private void help () {
          Popup p = new Popup("",100,100,400,550,Color.pink,"TCPIP Help");
          p.add("This applet demonstrates a reliable network connection\n");
          p.add("between two nodes.  Such a connection ensures that a\n");
          p.add("stream of data gets from the source (node 0) to the\n");
          p.add("destination (node 1) with absolutely no errors or\n");
          p.add("missing pieces.\n");
          p.add("\n");
          p.add("The famous TCP protocol, which is part of the TCP/IP\n");
          p.add("protocol suite and used by millions of computers on the\n");
          p.add("Internet, is a realiable connection protocol.  It breaks\n");
          p.add("a message into reasonable sized chunks, called packets,\n");
          p.add("and sends them over the wire.  This breaking-up process\n");
          p.add("is called \"fragmentation.\"\n");
          p.add("\n");
          p.add("At the other end, the packets are reassembled into the\n");
          p.add("original message.  If some packets are damaged, the \n");
          p.add("destination sends a NAK message back, which prompts the\n");
          p.add("sender to resend that packet.  The same happens if a packet\n");
          p.add("is lost, i.e. totally removed from the wire.  In this case\n");
          p.add("the sender waits too long for the ACK message that would\n");
          p.add("otherwise signal the reception of an undamaged packet.\n");
          p.add("Thus, the sender \"times out\" and resends the packet.\n");
          p.add("\n");
          p.add("All of this extra traffic on the wire is invisible to the\n");
          p.add("user who is sitting at the destination node, watching the\n");
          p.add("data pour off the network.  The only perceptible change is\n");
          p.add("that there might be pauses every so often, due to delayed,\n");
          p.add("lost or damaged packets.\n");
          p.add("\n");
          p.add("There are other reliable protocols like TCPIP, though it is\n");
          p.add("the most famous.  Sometimes they are called \"transport\"\n");
          p.add("protocols.  They form the foundation for sending web pages,\n");
          p.add("emails, and whole files, not to mention remote login\n");
          p.add("(telnet.)\n");
          p.add("\n");
          p.add("In this applet, the message that you type into the text area\n");
          p.add("\"Your message:\" is fragmented into packets and sent to\n");
          p.add("node 1.  As the packets move across the wire, you can see\n");
          p.add("their contents, both the part of the message they are carrying\n");
          p.add("as well as the header, which contains important sequencing\n");
          p.add("and error-detection information.\n");
          p.add("\n");
          p.add("As the packets move along the wire, you can either damage\n");
          p.add("the packet or delete it altogether.  Merely click on the\n");
          p.add("moving packet to damage or delete it.  (You don't get a\n");
          p.add("choice of how to damage it.  The applet randomly changes one\n");
          p.add("of the bytes.)\n");
          p.add("\n");
          p.add("Since the ACK and NAK messages are also packets, though\n");
          p.add("moving in the opposite direction, you can damage or delete\n");
          p.add("them, too.  Deleting them will cause the sender to time-out.\n");
          p.add("\n");
          p.add("In real networks, the sender and the destination nodes may\n");
          p.add("not have a single wire between them, multiplying the chances\n");
          p.add("for packets to get damaged or lost while en route!\n");
          p.add("However, the abstraction that this applet presents, of just\n");
          p.add("one wire between sender and receiver, is still valid, even\n");
          p.add("if that single wire is really a set of wires and intermediate\n");
          p.add("routers.\n");
          p.add("\n");
          p.add("Have fun, and see how much havoc you can wreak!\n");
     }

     private void loadExample() {
          inputTF.setText("A very long message will be broken into packets and "+
                          "sent one packet at a time.");
          tf1.setText("");
          tf2.setText("");
          status.setText("");
     }
}

class Packet {
                                 // Fields of the packet
     String data;                // the data to be sent
     String type;                // "DAT" = data, "ACK" = acknowledgmenet,
                                 // "NAK" = negative acknowledgement
     int dest;                   // destination node
     int seq;                    // sequence number
     int chksum;                 // checksum


     int x, y, width, height;
     public final static int XINCREMENT = 10;
     Tcpip parent;
     boolean delivered;

     public Packet (String data, String type, int destination, 
                    int seq, Tcpip parent, int x, int y) 
     {
          this.data = data;
          this.type = type;
          dest = destination;
          this.seq = seq;
          chksum = computeChecksum (data);

          this.parent = parent;
          width = 100;
          height = 45;
          this.x = x;
          this.y = y;
          delivered = false;
     }

     public String toString() {
          return "Packet: /"+data+"/ type="+type+" dest="+dest+" seq="+seq+
                 "chk="+chksum +"\nx="+x+"  y="+y;
     }

     public void advance (int direction) {
          if (delivered)
               return;
          if (direction == 0) {            // to the right
               x += XINCREMENT;
               if (x >= parent.nodes[dest].x) {
                    parent.nodes[dest].deliver(this);
                    delivered = true;
               }
          }
          else if (direction == 1) {            // to the left
               x -= XINCREMENT;
               if (x <= parent.nodes[dest].x) {
                    parent.nodes[dest].deliver(this);
                    delivered = true;
               }
          }
     }

     public void draw (Graphics g) {
          Color hold = g.getColor();
          g.setColor(Color.black);
          g.drawRect(x, y, width, height);
          g.setColor(Color.white);
          g.fillRect(x+1, y+1, width-1, height-1);
          g.setColor(Color.black);
          g.setFont(new Font("SansSerif", Font.PLAIN, 11));
          g.drawString(type+" "+dest+" "+seq+" "+chksum,x+2,y+15);
          g.drawString(data,x+2,y+30);
          g.setColor(hold);
     }

     public static int computeChecksum (String s) {
          int chksum = 0;
          for (int i=0; i<s.length(); i++) {
               chksum += (int)(s.charAt(i));
               chksum = chksum % 256;
          }
          return chksum;
     }

     public boolean within (int x, int y) {
          return (x >= this.x && x <= this.x+width &&
                  y >= this.y && y <= this.y+height);
     }

     public void damage() {
          int pos = (int)(new Random(new Date().getTime()).nextFloat() * data.length());
//        int pos = new Random(new Date().getTime()).nextInt(data.length());
          char ch = data.charAt(pos);
          int newch = (ch + 50)%256;
          data = data.substring(0,pos)+(char)newch+data.substring(pos+1);
     }
}

class Node {
     int nextSeqNum, nextAckExpected;
     String messageToSend;
     String remainingMessage;
     String outgoingData;     // the part just sent, need to keep in case
                              // we need to retransmit
     int id;
     Tcpip parent;
     int x, y, diameter;
     Packet currentPkt, inbox;
     int direction;             // 0 = going right, 1 = going left

     int timer;

     public final static int TRANSITDELAY = 90;
         
     public Node (int id, int direction, Tcpip parent,
                  int xPosition, int yPosition, int diameter) 
     {
          this.id = id;
          this.parent = parent;
          this.diameter = diameter;
          this.direction = direction;
          x = xPosition;
          y = yPosition;
          nextSeqNum = 0;
          nextAckExpected = 0;
          messageToSend = "";
          remainingMessage = "";
     }

     Packet p;
     boolean waitingAck = false;

     public boolean inject (String message) {
          if (remainingMessage.length() > 0)
               return false;
          messageToSend = message;
          remainingMessage = message;
          parent.tf1.setText(message);
          waitingAck = false;
          return true;
     }

     public void deliver (Packet p) {
          inbox = p;
     }

     public void start() {
          if (direction == 0) {
               new Thread() {
                    public void run() {
                         while (true) {
                              if (parent.mustStop) break;
                              if (currentPkt != null) 
                                   currentPkt.advance(direction);

                              if (waitingAck) {
                                   if (inbox != null)
                                        processAck();
                                   if (timer == 0) {
                                        currentPkt = new Packet(outgoingData,
                                              "DAT", 1, nextSeqNum, parent, 
                                              x, y-20);
                                        parent.msg("Timed out, resending packet");
                                        timer = 100;
                                   }
                              }
                              else if (currentPkt == null) {
                                   if (remainingMessage.length() > 0) {
                                        int length = Math.min(remainingMessage.length(),
                                                              10);
                                        outgoingData = remainingMessage.substring(0,length);
                                        remainingMessage = remainingMessage.substring(length);
                                        parent.tf1.setText(remainingMessage);
                                        currentPkt = new Packet(outgoingData,
                                            "DAT", 1, nextSeqNum, parent, x, y-20);
                                        waitingAck = true;
                                        timer = 100;
                                   }
                              }
                              parent.repaint();
                              U.sleep(TRANSITDELAY);
                              timer--;
                         }
                    }
               }.start();
          }
          else {
               waitingAck = true;
               new Thread() {
                    public void run() {
                         while (true) {
                              if (parent.mustStop) break;
                              if (currentPkt != null) 
                                   currentPkt.advance(direction);

                              if (waitingAck) {
                                   if (inbox != null)
                                        processData();
                              }
                              parent.repaint();
                              U.sleep(TRANSITDELAY);
                         }
                    }
               }.start();
          }
     }

     private void processAck() {
          Packet ap = inbox;
          inbox = null;
          parent.nodes[1].clear();
          if (ap.type.equals("ACK")) {
               if (ap.seq == nextSeqNum) {
                    parent.msg("Accepted ACK "+ap.seq);
                    nextSeqNum++;
                    waitingAck = false;
               }
          }
          else if (ap.type.equals("NAK")) {
               currentPkt = new Packet(outgoingData,
                                       "DAT", 1, nextSeqNum, parent, x, y-20);
               parent.msg("Got NAK "+ap.seq+"; resending packet");
          }
          else {
               parent.msg("Received unknown packet: "+ap.type+" "+ap.data);
          }
     }

     private void processData() {
          Packet ap = inbox;
          inbox = null;
          parent.nodes[0].clear();
          if (ap.type.equals("DAT")) {
               if (ap.seq < nextSeqNum) {
                    currentPkt = new Packet("", "ACK", 0, nextSeqNum-1, parent,
                                            this.x, this.y+35);
                    parent.msg("Received duplicate DAT packet, resending ACK");
               }
               else if (ap.seq == nextSeqNum) {
                    if (ap.chksum == Packet.computeChecksum(ap.data)) {
                         parent.tf2.setText(parent.tf2.getText()+ap.data);
                         currentPkt = new Packet("", "ACK", 0, nextSeqNum, parent,
                                                 this.x, this.y+35);
                         nextSeqNum++;
                    }
                    else {
                         parent.msg("Received damaged packet");
                         currentPkt = new Packet("", "NAK", 0, nextSeqNum, parent,
                                                 this.x, this.y+35);
                    }
               }
          }
     }

     public void draw (Graphics g) {
          Color holdcolor = g.getColor();
          Font holdfont = g.getFont();

          if (currentPkt != null)
               currentPkt.draw(g);

          g.setColor(Color.blue);
          g.fillOval (x, y, diameter, diameter);
          g.setColor(Color.white);
          g.setFont(new Font("SansSerif", Font.BOLD, 36));
          g.drawString(id+"", x+20, y+40);

          g.setColor(holdcolor);
          g.setFont(holdfont);
     }

     public void clear() {
          currentPkt = null;
     }
}
