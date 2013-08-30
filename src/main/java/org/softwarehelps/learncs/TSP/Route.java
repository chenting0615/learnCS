class Route {
     String[] names;
     int cost;
     int numnames;
     boolean isComplete;

     public Route(String starter, int size) {
          names = new String[size+1];
          names[0] = starter;
          cost = 0;
          numnames = 1;
          isComplete = false;
     }

     public Route(Route r) {
          names = new String[r.size()];
          for (int i=0; i<r.numnames; i++)
               names[i] = new String(r.names[i]);
          cost = r.cost;
          numnames = r.numnames;
          isComplete = false;
     }

     public int size() {
          return names.length;
     }

     public int length() {
          return numnames;
     }

     public boolean inList(String name) {
          for (int i=0; i<numnames; i++)
               if (names[i].equals(name)) return true;
          return false;
     }

     public boolean add(String name, int linkcost) {
          if (numnames >= names.length)
               return false;
          names[numnames++] = name;
          cost += linkcost;
          return true;
     }

     public String toString() {
          String s = "";
          for (int i=0; i<numnames; i++)
               s += names[i]+" ";
          return s;
     }

     public String starter() {
          return names[0];
     }

     public String last() {
          return names[numnames-1];
     }

     public boolean areConnected (String id1, String id2,
                                  boolean wrapAround) 
     {
          for (int i=0; i<numnames-1; i++) {
               if (names[i].equals(id1) &&
                   names[i+1].equals(id2)) {
                    return true;
               }
               if (names[i+1].equals(id1) &&
                   names[i].equals(id2)) {
                    return true;
               }
          }
          if (wrapAround) {
               if (names[numnames-1].equals(id1) &&
                   names[0].equals(id2)) {
                    return true;
               }
               if (names[0].equals(id1) &&
                   names[numnames-1].equals(id2)) {
                    return true;
               }
          }
          return false;
     }
}
