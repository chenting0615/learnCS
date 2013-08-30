package org.softwarehelps.learncs.COMPRESSOR;

/*
    This does keyword encoding for compression
*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Compressor1 extends Applet implements ActionListener {
     TextArea textarea, codetable, stats;
     Button compressB, decompressB, exampleB;
     Color bg = new Color(0, 180, 255);

     public void init() {
          setLayout (null);

          Label lab = new Label("Text Compression");
          lab.setBounds(130,5,380,38);
          lab.setFont (new Font("SansSerif", Font.BOLD, 36));
          lab.setBackground(bg);
          add(lab);

          int y = 10 + lab.getSize().height;

          textarea = new TextArea(10,40);
          textarea.setBounds(10,y,400, 200);
          textarea.setBackground(Color.white);
          textarea.setText("Enter your text here");
          add(textarea);

          y += textarea.getSize().height + 5;

          codetable = new TextArea(100,20);
          codetable.setBounds(10,y,250,200);
          codetable.setText("Your keyword encodings go here");
          codetable.setBackground(Color.white);
          add(codetable);

          y = 100;

          compressB = new Button("compress");
          compressB.addActionListener(this);
          compressB.setBounds(420,y,90,25);
          add(compressB);

          y += compressB.getSize().height + 2;

          decompressB = new Button("decompress");
          decompressB.addActionListener(this);
          decompressB.setBounds(420,y,90,25);
          add(decompressB);

          y += decompressB.getSize().height + 2;

          exampleB = new Button("example");
          exampleB.addActionListener(this);
          exampleB.setBounds(420,y,90,25);
          add(exampleB);

          y += exampleB.getSize().height + 75;

          stats = new TextArea(2,30);
          stats.setBounds(290, y, 200, 100);
          stats.setText("statistics here");
          stats.setBackground(Color.white);
          stats.setEditable(false);
          add(stats);

          setBackground(bg);
     }
     
     public void actionPerformed(ActionEvent e) {
          if (e.getSource() == compressB) 
               compress(true);
          if (e.getSource() == decompressB)
               compress(false);
          if (e.getSource() == exampleB)
               makeExample();
     }

     public static int atoi (String s) {
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               return Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     private void compress (boolean encode) {
          if (codetable.getText().length() == 0) {
               codetable.setText("You must put some codewords\n"+
                                 "in here first!  For example:\n"+
                                 "*1 the\n"+
                                 "*2 a\n"+
                                 "*3 an\n"+
                                 "*4 any\n"+
                                 "*5 no\n");
               return;
          }
          if (textarea.getText().length() == 0) {
               textarea.setText("You must put some text in here first!");
               return;
          }
          int originalSize = textarea.getText().length();
          String[] list = makeList(textarea.getText());
          for (int i=0; i<list.length; i++) {
               if (list[i] != null) {
                    list[i] = compress1 (list[i], encode);
               }
          }
          textarea.setText(makeString(list));
          int newSize = textarea.getText().length();
          if (encode) {
               double savings = (originalSize-newSize) / (double)originalSize*100;
               savings = round(savings,1);
               stats.setText("original size was "+originalSize+"\n"+
                             "current  size is  "+newSize+"\n"+
                             "Savings was "+savings+"%");
          }
     }

     private String compress1 (String line, boolean encode) {
          StringTokenizer st = new StringTokenizer(line, " ");
          String news = "";
          while (st.hasMoreTokens()) {
               String word = st.nextToken();
               if (encode)
                    news += replaceWord(word)+" ";
               else
                    news += replaceCodeWord(word)+" ";
          }
          int newSize = news.length();
          return news;
     }

     private String replaceWord (String word) {
          StringTokenizer st = new StringTokenizer(codetable.getText(),"\n");
          while (st.hasMoreTokens()) {
               String line = st.nextToken();
               int n = line.indexOf(" ");
               if (n == -1) continue;
               if (line.charAt(0) != '*') continue;
               int m = atoi(line.substring(1,n));
               if (m == 0) continue;
               String plainword = line.substring(n+1).trim();
               if (plainword.length() == 0) continue;
               if (word.equals(plainword))
                    return "*"+m;
          }
          return word;
     }

     private String replaceCodeWord (String codeword) {
          StringTokenizer st = new StringTokenizer(codetable.getText(),"\n");
          while (st.hasMoreTokens()) {
               String line = st.nextToken();
               int n = line.indexOf(" ");
               if (n == -1) continue;
               if (line.charAt(0) != '*') continue;
               String xcodeword = line.substring(0,n);
               String plainword = line.substring(n+1).trim();
               if (xcodeword.length() == 0) continue;
               if (xcodeword.equals(codeword))
                    return plainword;
          }
          return codeword;
     }

     static double round (double n, int numplaces) {
          n += Math.pow(0.1,numplaces)/2;
          n *= Math.pow(10,numplaces);
          long m = (long)n;
          return m / Math.pow(10,numplaces);
     }

     void makeExample() {
          codetable.setText("*1 the\n"+
                            "*2 cat\n"+
                            "*3 on\n"+
                            "*4 any\n"+
                            "*5 no\n"+
                            "*6 and\n");
          textarea.setText("the cat in the hat sat\n"+
                           "on the mat and thought about\n"+
                           "the fish and the mouse and the rat\n");
     }

     String[] makeList(String s) {
          String[] sl = new String[100];
          StringTokenizer st = new StringTokenizer(s,"\n");
          int i = 0;
          while (st.hasMoreTokens()) {
               String line = st.nextToken();
               sl[i++] = line;
          }
          return sl;
     }

     String makeString (String[] list) {
          String s = "";
          for (int i=0; i<list.length; i++) 
               if (list[i] != null)
                    s += list[i] + "\n";
          return s;
     }
}
