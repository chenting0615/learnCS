//-------------------------------------------------------------------
//   NameDB -- DataBase of names (variables and defined functions).
//
//   This holds pairs of names and values.  
//   If the type is VARiable, the Token value is used.
//   If the type is FUNCtion, the ProgElement and the parameters are used.
//   This class also contains a stack which is used to hold the saved
//   values of the parameters while a function is executing.
//   Another thing I added was a vector that holds open files.
//   I keep everything together in this class for the sake of visibility
//   and consistency. 
//-------------------------------------------------------------------

import java.io.*;

public class NameDB {
     final static int FREE = 0;
     final static int VAR  = 1;
     final static int FUNC = 2;

     class Entry {
          private String name;
          private Token value;
          private int type;               // see below
          private ProgElement body;       // body of function
          private String parameters;


          Entry() {
               this.type = FREE;
          }

          Entry(String name, Token value) {
               this.name = name;
               this.value = value;
               this.type = VAR;
          }

          Entry(String name, String parms, ProgElement body) {
               this.name = name;
               this.parameters = parms;
               this.body = body;
               this.type = FUNC;
          }

          public Entry copy() {
               Entry mycopy = new Entry();
               mycopy.name = new String(name);
               mycopy.value = (Token)value.clone();
               mycopy.type = VAR;
               mycopy.parameters = new String(parameters);
               mycopy.body = body;          // this is not a deep copy
               return mycopy;
          }
     }

     class Stack {
          private Entry[] entries;
          private int numentries;

          Stack() {
               numentries = 0;
               entries = new Entry[50];
          }

          public void push (String name, Token value) {
               if (numentries == entries.length) 
                    grow();
               entries[numentries++] = new Entry(name, value);
          }

          private void grow() {
               Entry[] newentries = new Entry[entries.length+50];
               for (int i=0; i<entries.length; i++)
                    newentries[i] = entries[i];
               entries = newentries;
          }

          public void pop () {
               if (numentries == 0) return;
               entries[--numentries] = null;
          }

          public String topName() {
               if (numentries == 0) return "";
               return entries[numentries-1].name;
          }

          public Token topValue() {
               if (numentries == 0) return new Token(Token.UNKNOWN,0);
               return entries[numentries-1].value;
          }
     }

     private Entry[] entries;
     private Stack stack;
     public Palgo parent;          // pointer back to the parent 

     public NameDB (Palgo parent) {
          entries = new Entry[10];
          stack = new Stack();
          this.parent = parent;
     }

     public void set (String name, Token value) {
          int n = find (name);
          if (n == -1)
               n = findFreeSlot();
          entries[n] = new Entry(name, value);
     }

     public void define (String funcname, String parms, ProgElement body) {
          int n = find (funcname);
          if (n == -1) 
               n = findFreeSlot();
          entries[n] = new Entry(funcname, parms, body);
     }

     public void delete (String name) {
          int n = find (name);
          if (n != -1) {
               entries[n].type = FREE;
               entries[n].name = "";
               entries[n].value = null;
               entries[n].body = null;
               entries[n].parameters = null;
          }
     }

     public boolean nameInUse (String name) {
          return find (name) != -1;
     }

     public Token get (String name) {
          int n = find (name);
          if (n != -1)
               return entries[n].value;
          else
               return new Token(Token.UNKNOWN, 0);
     }

     public String getParameters (String name) {
          int n = find (name);
          if (n != -1 && entries[n].type == FUNC)
               return entries[n].parameters;
          else
               return null;
     }

     public ProgElement getFunction (String name) {
          int n = find (name);
          if (n != -1 && entries[n].type == FUNC)
               return entries[n].body;
          else
               return null;
     }

     public String getString (String name) {
          int n = find (name);
          if (n != -1)
               return entries[n].value.value;
          else
               return "";
     }

     public int getInt (String name) {
          int n = find (name);
          if (n != -1)
               return atoi(entries[n].value.value);
          else
               return 0;
     }

     public double getDouble (String name) {
          int n = find (name);
          if (n != -1)
               return atod(entries[n].value.value);
          else
               return 0;
     }

     private int find (String name) {
          for (int i=0; i<entries.length; i++)
               if (entries[i] != null && entries[i].type != FREE
                   && entries[i].name.equals(name))
                    return i;
          return -1;
     }

     private int findFreeSlot () {
          int n = entries.length;
          for (int i=0; i<entries.length; i++) {
               if (entries[i] == null) {
                    entries[i] = new Entry();
                    return i;
               }
               if (entries[i].type == FREE)
                    return i;
          }
          grow(10);
          entries[n] = new Entry();
          return n;
     }

     private void grow (int increment) {
          Entry[] newentries = new Entry[entries.length+increment];
          for (int i=0; i<entries.length; i++)
               newentries[i] = entries[i];
          entries = newentries;
     }

     private static int atoi (String s) {
          try {
               return Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     private static double atod (String s) {
          try {
               return Double.valueOf(s).doubleValue();
          }
          catch (NumberFormatException nfe) {
               return 0.0;
          }
     }

     public NameDB copy () {
          NameDB mycopy = new NameDB(parent);
          for (int i=0; i<entries.length; i++)
               if (entries[i] != null)
                    mycopy.entries[i] = entries[i].copy();
          return mycopy;
     }

     public String toString() {
          String result = "\n";
          for (int i=0; i<entries.length; i++) {
               if (entries[i] != null) {
                   result += i +". "+entries[i].name+", "+entries[i].type;
                   if (entries[i].type == VAR)
                         result += ", "+entries[i].value+"\n";
                   else
                         result += ", "+entries[i].parameters+"\n";
               }
          }
          return result;
     }

     public void push (String name, Token value) {
          stack.push (name, value);
     }

     public void pop () {
          stack.pop();
     }

     public String topName() {
          return stack.topName();
     }

     public Token topValue() {
          return stack.topValue();
     }
}
