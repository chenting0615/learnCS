import java.awt.*;
import java.util.*;

public class Node {
     String id;            // this is an IP address so it has dots in it
     Network parent;
     public int x, y;
     String messageToSend;
     StringList connectionTable;
     StringList routingTable;
     StringList packetsReceived;
     StringList info;
     String dest;          // to whom are we sending the packets?
     Queue outgoing;       // queue of messages to send
     Color mycolor = Color.blue;
     int numericalID;
     boolean mustGenerate = false;

     final static int SLEEPTIME = 250;

     int numpacketsSent = 0;
     int numpacketsRecd = 0;
     int numpacketsDropt = 0;
     int numpacketsForwarded = 0;

     final static int diameter = 25;
         
     public Node (Network parent, String id, int xPosition, int yPosition) 
     {
          this.id = id;
          numericalID = U.atoi(U.getField(id,0,'.')) +
                        U.atoi(U.getField(id,1,'.')) +
                        U.atoi(U.getField(id,2,'.')) +
                        U.atoi(U.getField(id,3,'.'));
          this.parent = parent;
          x = xPosition;
          y = yPosition;
          messageToSend = "I am from #"+id;
          connectionTable = new StringList();
          routingTable = new StringList("DEFAULT");
          packetsReceived = new StringList();
          info = new StringList();
          dest = "";
          outgoing = new Queue();
     }

     public void paint (Graphics g) {
          Color holdcolor = g.getColor();
          Font holdfont = g.getFont();

          g.setColor(mycolor);
          int halfdiam = diameter/2;
          g.fillOval (x, y, diameter, diameter);
          g.setColor(Color.black);
          g.setFont(new Font("SansSerif", Font.PLAIN, 12));
          g.drawString(id+"", x, y-10);
          g.drawString("pkts="+outgoing.length()+"", x, y+diameter+10);

          for (int i=0; i<connectionTable.length(); i++) {
               String ipAddress = connectionTable.get(i);
               Node other = parent.find(ipAddress);
               if (other != null) 
                    g.drawLine(x+halfdiam, y+halfdiam, 
                               other.x+halfdiam, other.y+halfdiam);
          }

          g.setColor(holdcolor);
          g.setFont(holdfont);
     }

     public void connect (String ipAddress) {
          connectionTable.add(ipAddress);
          Node n = parent.find(ipAddress);
          if (n == null)
               return;
          n.connectionTable.add(id);
     }

     public void clearRoutes () {
          routingTable = new StringList();
     }

     public void addRoute (String destAddress, String intermediate) {
          routingTable.add(destAddress+"               "+intermediate);
     }

     public boolean within (int xx, int yy) {
          return (xx >= x && xx <= x+diameter &&
                  yy >= y && yy <= y+diameter);
     }

     // The following gets the destination field from the packet.
  
     public static String getDest (String packet) {
          String m = U.getField(packet,0,'/');
          if (m != null)
               return m;
          else
               return "";
     }

     // The following gets the source field from the packet.
  
     public static String getSource (String packet) {
          String m = U.getField(packet,1,'/');
          if (m != null)
               return m;
          else
               return "";
     }

     // The following gets the message field from the packet.
  
     public static String getMessage (String packet) {
          String m = U.getField(packet,2,'/');
          if (m != null)
               return m;
          else
               return "";
     }

     // The following merely places a message on the outgoing queue

     public void send (String dest, String message) {
          if (dest.length() == 0) return;
          String pkt = dest+"/"+id+"/"+message;
          outgoing.enqueue (pkt);
          numpacketsSent++;
          info.add("sending: "+pkt);
          mycolor = Color.yellow;
     }

     // The following is called by other nodes when they want this
     // node to accept a message.  All it does it place the message
     // on the outgoing queue

     public void deliver (String packet) {
          outgoing.enqueue (packet);
     }

     // The following is called to deal with the next message on the
     // queue.  There are 2 cases.  If the destination is this node,
     // we move the message to the received area.  If the dest. is
     // someone else, we consult the routing table to find out where
     // to send it to.  It calls findNextHop() to consult the routing
     // table.

     public void route () {
          if (outgoing.isEmpty()) return;
          String p = (String)outgoing.dequeue();
          String dest = getDest(p);
          if (dest.equals(id)) {
               packetsReceived.add(getMessage(p)+"\n");
               mycolor = Color.red;
               numpacketsRecd++;
          }
          else {
               String nexthop = findNextHop(dest);
               if (nexthop == null || nexthop.length() == 0) {
                    packetsReceived.add("UNDELIVERABLE: "+getMessage(p)+"\n");
                    numpacketsDropt++;
               }
               else {
                    Node n = parent.find(nexthop);
                    if (n == null) {
                         packetsReceived.add("UNDELIVERABLE: "+getMessage(p)+"\n");
                         numpacketsDropt++;
                    }
                    else {
                         n.deliver(p);
                         numpacketsForwarded++;
                         if (!getSource(p).equals(id)) {
                              mycolor = Color.green;
                         }
                    }
               }
          }
     }

     public String findNextHop(String ipAddress) {
          for (int i=0; i<connectionTable.length(); i++) 
               if (connectionTable.get(i).equals(ipAddress))
                    return ipAddress;

          if (routingTable.toString().toUpperCase().startsWith("DEFAULT")) {
               return connectionTable.get(0);
          }
         
          if (routingTable.toString().toUpperCase().startsWith("HOT POTATO")) {
               int i = getRandom(connectionTable.length());    
               return connectionTable.get(i);
          }

          for (int i=0; i<routingTable.length(); i++) {
               String route = routingTable.get(i);
               if (route == null) continue;
               String routeDest = U.getField(route, 0);
               String routeNext = U.getField(route, 1);
               if (routeDest == null || routeNext == null) continue;
               if (routeDest.equals(ipAddress) || routeDest.equals("*"))
                    return routeNext;
          }

          return "";
     }

     public void generatePacket(String command) {
          if (command.equals("random")) {
               if (getRandom() == 3) 
                    send(dest, messageToSend);
          }
          else
               send(dest, messageToSend);
     }

     public float xgetRandom() {
          return new Random(new Date().getTime()*numericalID).nextFloat();
     }

     public int getRandom() {
          return (int)(xgetRandom()*5);
     }

     public int getRandom(int limit) {
          return (int)(xgetRandom()*limit);
     }

     public String status() {
          return "Number of packets sent     ="+numpacketsSent+"\n"+
                 "Number of packets received ="+numpacketsRecd+"\n"+
                 "Number of packets forwarded="+numpacketsForwarded+"\n"+
                 "Number of packets dropped  ="+numpacketsDropt;
     }
}
