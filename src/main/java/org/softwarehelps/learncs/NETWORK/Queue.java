package org.softwarehelps.learncs.NETWORK;

import java.util.*;
import java.io.*;

public class Queue implements Serializable, Cloneable {
     Vector theQueue;

     public Queue() {
          theQueue = new Vector();
     }
     
     public boolean isEmpty() {
          if (theQueue.size() <= 0)
               return true;
          return false;
     }
     
     public void enqueue(Object data) {
          theQueue.addElement(data);
     }
     
     public Object dequeue() {
          Object temp = theQueue.elementAt(0);
          
          if (temp != null) {
               theQueue.removeElementAt(0);
               return temp;
          }
               
          return null;
     }
     
     public Object top() {
          return theQueue.elementAt(0);
     }
     
     public int length() {
          return theQueue.size();
     }
     
     public Object clone() {
         Queue copy = new Queue();
         
         try {
              copy = (Queue) super.clone();
         }
         catch (CloneNotSupportedException cnse) { }
         
         return copy;
     }
    
     public static void main(String args[]) {
          Queue q = new Queue();
          
          for (int i = 0; i < 4; i++)
               q.enqueue(new Integer(i));
          
          while (!(q.isEmpty())) {
               Integer temp = (Integer)q.dequeue();
               System.out.println(temp.intValue());
          }
     }
}