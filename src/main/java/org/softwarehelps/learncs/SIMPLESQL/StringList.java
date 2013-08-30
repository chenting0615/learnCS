package org.softwarehelps.learncs.SIMPLESQL;

import java.util.*;

public class StringList {
     String[] contents;
     int size;

     public StringList() {
          contents = new String[20];
          size = 0;
     }

     public StringList(String s, String delimiter) {
          StringTokenizer st = new StringTokenizer(s, delimiter);
          contents = new String[st.countTokens()];
          size = contents.length;
          int i = 0;
          while (st.hasMoreTokens()) {
               contents[i++] = st.nextToken();
          }
     }

     public StringList(String s) {
          this(s, " ");
     }

     public int length() {
          return size;
     }

     public StringList shift (int n) {
          StringList ret = new StringList();
          if (n >= size) {
               ret = this.copy();
               size = 0;
               return ret;
          }
          for (int i=0; i<n; i++) {
               ret.add(contents[i]);
          }

          for (int i=0; i<size-n; i++) {
               contents[i] = contents[i+n];
          }
          size -= n;
          return ret;
     }

     public StringList copy () {
          StringList newone = new StringList();
          newone.contents = new String[contents.length];
          newone.size = size;
          for (int i=0; i<contents.length; i++)
               newone.contents[i] = contents[i];
          return newone;
     }

     public int find (String s) {
          for (int i=0; i<size; i++) 
               if (contents[i].equals(s))
                    return i;
          return -1;
     }

     public String get (int n) {
          if (n < 0 || n >= size)
               return null;
          return contents[n];
     }

     public void put (String s, int n) {
          if (n < 0)
               return;
          if (n < size)
               contents[n] = s;
          else {
               grow(n+1);
               contents[n] = s;
          }
          if (n >= size)
               size = n + 1;
     }

     public void add (String s) {
          grow();
          contents[size++] = s;
     }

     public String toString() {
          return toString(' ');
     }

     public void delete (int n) {
          if (n < 0 || n >= size)
               return;
          if (n == size-1) {
               size--;
               return;
          }
          for (int i=n; i<size; i++)
               contents[i] = contents[i+1];
          size--;
     }

     public String toString (char separator) {
          if (size == 0)
               return "";
          String ret = "";
          for (int i=0; i<size; i++) 
               if (contents[i] != null)
                    ret += contents[i] + separator;
          return ret.substring(0,ret.length()-1);
     }

     public void substitute (String[] oldwords, String[] newwords) {
          for (int i=0; i<size; i++) 
               if (contents[i] != null) {
                    for (int j=0; j<oldwords.length; j++) {
                         if (contents[i].equals(oldwords[j]))
                              contents[i] = new String(newwords[j]);
                    }
               }
     }

     private void grow() {
          if (size >= contents.length) {
               String[] newcontents = new String[contents.length+20];
               for (int i=0; i<contents.length; i++)
                    newcontents[i] = contents[i];
               contents = newcontents;
          }
     }

     private void grow(int required) {
          if (required >= contents.length) {
               String[] newcontents = new String[required+20];
               for (int i=0; i<contents.length; i++)
                    newcontents[i] = contents[i];
               contents = newcontents;
          }
     }

     public static void main (String[] args) {
          StringList sl = new StringList("this is the time of our lives.");
          System.out.println ("length="+sl.length());
          System.out.println ("values="+sl.toString());
          sl.shift(2);
          System.out.println ("after shifting, values="+sl.toString());
          int n = sl.find("time");
          System.out.println ("tried to find time, n="+n);
          n = sl.find("our");
          System.out.println ("tried to find our, n="+n);
          n = sl.find("somebody");
          System.out.println ("tried to find somebody, n="+n);
     }
}
