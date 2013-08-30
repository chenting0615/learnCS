import java.awt.*;
import java.util.*;

public class Node {
     String id;            // this is an IP address so it has dots in it
     GraphTSP parent;
     public int x, y;
     StringList connectionTable;
     StringList info;
     Color mycolor = Color.blue;

     final static int diameter = 25;
         
     public Node (GraphTSP parent, String id, int xPosition, int yPosition) 
     {
          this.id = id;
          this.parent = parent;
          x = xPosition;
          y = yPosition;
          connectionTable = new StringList();
          info = new StringList();
     }

     public void paint (Graphics g) {
          Color holdcolor = g.getColor();
          Font holdfont = g.getFont();

          g.setColor(mycolor);
          int halfdiam = diameter/2;
          g.fillOval (x, y, diameter, diameter);
          g.setColor(Color.black);
          g.setFont(new Font("SansSerif", Font.PLAIN, 12));
          g.drawString(id+"", x, y-5);

          for (int i=0; i<connectionTable.length(); i++) {
               String con = connectionTable.get(i);
               String address = U.getField(con, 0);
               int weight = U.atoi(U.getField(con, 1));
               Node other = parent.find(address);
               if (other != null) {
                    int halfx = (x + other.x)/2;
                    int halfy = (y + other.y)/2;
                    if (parent.tsp != null && parent.tsp.pathExists)
                         g.setColor(Color.green);
                    else if (parent.tsp != null && !parent.tsp.done)
                         g.setColor(Color.black);
                    else
                         g.setColor(Color.black);
                    g.drawLine(x+halfdiam, y+halfdiam, 
                               other.x+halfdiam, other.y+halfdiam);
                    g.drawString(weight+"", halfx, halfy);


                    if (parent.tsp != null) {
                         if (!parent.tsp.done) {
                              if (parent.tsp.temproute != null && 
                                  parent.tsp.temproute.areConnected(id, 
                                                           other.id,false)) 
                              {
                                   g.setColor(Color.yellow);
                                   g.drawLine(x+halfdiam, y+halfdiam,
                                              other.x+halfdiam, other.y+halfdiam);
                                   g.drawString(weight+"", halfx, halfy);
                              }
                         }
                         else if (parent.tsp.pathExists) {
                              if (parent.tsp.minroute.areConnected(id, 
                                                           other.id,true)) 
                              {
                                   g.setColor(Color.red);
                                   g.drawLine(x+halfdiam, y+halfdiam,
                                              other.x+halfdiam, other.y+halfdiam);
                                   g.drawString(weight+"", halfx, halfy);
                              }
                         }
                    }
               }
          }

          g.setColor(holdcolor);
          g.setFont(holdfont);
     }

     public void connect (int othernode, int weight) {
          String address = parent.nodes[othernode].id;
          connectionTable.add(address+" "+weight);
          parent.nodes[othernode].connectionTable.add(id+" "+weight);
     }

     public void connect (String othernode, int weight) {
          Node n = parent.find(othernode);
          if (n == null) return;
          String address = n.id;
          connectionTable.add(address+" "+weight);
          n.connectionTable.add(id+" "+weight);
     }

     public void deleteConnection (String other) {
          for (int i=0; i<connectionTable.length(); i++) {
               if (connectionTable.get(i).startsWith(other)) {
                    connectionTable.delete(i);
                    return;
               }
          }
     }

     public boolean within (int xx, int yy) {
          return (xx >= x && xx <= x+diameter &&
                  yy >= y && yy <= y+diameter);
     }

/*
     public int getRandom(int limit) {
          return (int)(new Random(new Date().getTime()).nextFloat() * limit);
     }
*/
}
