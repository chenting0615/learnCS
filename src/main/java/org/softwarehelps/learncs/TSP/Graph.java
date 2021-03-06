package org.softwarehelps.learncs.TSP;

/* This file was automatically generated from a .mac file.*/

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Graph {
     int[][] graph;
     String[] names;
     int numnodes;              // number of nodes in graph
     int nodecount = 0;         // lets us add node names
     GraphTSP parent;

     public Graph(GraphTSP parent, int numnodes) {
          this.parent = parent;
          graph = new int[numnodes][numnodes];
          for (int i=0; i<numnodes; i++)
               for (int j=0; j<numnodes; j++)
                    graph[i][j] = -1;
          names = new String[numnodes];
          this.numnodes = numnodes;
     }

     public void add (String nodename) {
          names[nodecount++] = nodename;
     }

     public boolean add (String node1, String node2, int cost) {
          int n1 = find(node1);
          if (n1 == -1) return false;
          int n2 = find(node2);
          if (n2 == -1) return false;
          graph[n1][n2] = cost;
          return true;
     }

     public int find (String nodename) {
          for (int i=0; i<nodecount; i++) {
               if (nodename.equals(names[i])) return i;
          }
          return -1;
     }

     public int getLink (String node1, String node2) {
          int n1 = find(node1);
          if (n1 == -1) return -1;
          int n2 = find(node2);
          if (n2 == -1) return -1;
          int cost = graph[n1][n2];
          if (cost != -1)
               return cost;
          return graph[n2][n1];
     }

     public String getName (int i) {
          return names[i];
     }

     public int size() {
          return numnodes;
     }

     public String toString() {
          String s = "";
          s += "numnodes  = "+numnodes+"\n";
          s += "nodecount = "+nodecount+"\n";
          for (int i=0; i<numnodes; i++)
               s += i+". "+names[i]+"\n";
          s += "\n";
          for (int i=0; i<numnodes; i++)
               for (int j=0; j<numnodes; j++) 
                    if (graph[i][j] != -1)
                         s += "graph["+i+"]["+j+"]="+graph[i][j]+"\n";
          return s;
     }

     public void loadGraph() {
/*
          String text = "";

          FileDialog loadfile = new
               FileDialog(parent, "Load Graph", FileDialog.LOAD);
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
               numnodes = U.atoi(line);
               for (int i=0; i<numnodes; i++) {
                    add(is.readLine());
               }
               while ((line = is.readLine()) != null) {
                    add(U.getField(line,0), U.getField(line,1),
                        U.atoi(U.getField(line,2)));
               }
               is.close();
          }
          catch(IOException ioe) {
               System.err.println("LoadProgram:  i/o exception.");
               ioe.printStackTrace();
          }
          return;
*/
     }

     public void saveGraph(String text) {
/*
          FileDialog savefile =
               new FileDialog(parent,"Save Graph",FileDialog.SAVE);
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

                    os.write(numnodes+"\n");
                    for (int i=0; i<numnodes; i++)
                         os.write(names[i]+"\n");
                    for (int i=0; i<numnodes; i++)
                         for (int j=i+1; j<numnodes; j++)
                              if (graph[i][j] != -1)
                                   os.write(names[i]+" "+
                                            names[j]+" "+
                                            graph[i][j]+"\n");

                    os.write(text);
                    os.close();
               }
               catch(IOException ioe) {
                    System.err.println("SaveProgram:  i/o exception.");
               }
          }
*/
     }
}
