import java.util.*;

public class TSP implements Runnable
{
     Graph g;
     Route minroute;
     Route temproute;      // this will be shown during the search
     boolean done;         // turns true when reach() has finished
     int count;
     boolean pathExists = false;
     GraphTSP parent;               // need a link back so we can
                                    // repaint during the search

     public TSP(Graph g, GraphTSP parent) {
          this.g = g;
          count = 0;
          this.parent = parent;
     }

     public String getRoute() {
          if (minroute == null)
               return "There is no Hamiltonian Circuit for this graph";
          return minroute.toString();
     }

     public int getRouteCost() {
          if (minroute == null)
               return -1;
          return minroute.cost;
     }

     public int getCount() {
          return count;
     }

     public void findMinCircuit() {
          done = false;
          Thread t = new Thread(this);
          t.start();
     }

     public void run() {
          String animation = (String)parent.animationCH.getSelectedItem();
          reach(g.names[0], new Route(g.names[0], g.size()),
                animation.startsWith("Animate"));
          if (minroute != null)
               pathExists = true;
          done = true;
          parent.displayResults();
          parent.repaint();
     }

     public void reach (String name, Route route, boolean animate) {
          for (int i=0; i<g.size(); i++) {
               count++;
               String iname = g.getName(i);
               int link = g.getLink(route.last(), iname);
               if (link == -1) continue;
               Route newroute = new Route(route);
               newroute.add(iname, link);
               temproute = newroute;
               if (animate) {
                    parent.repaint();
                    U.sleep(50);
               }
               if (!route.inList(iname)) {
                    reach(iname, newroute, animate);
               }
               else if (iname.equals(route.starter())) {
                    route.isComplete = true;
                    if (newroute.length() == g.size()+1) {
                         if (minroute == null)
                              minroute = newroute;
                         else if (newroute.cost < minroute.cost)
                              minroute = newroute;
                    }
               }
          }
     }
}
