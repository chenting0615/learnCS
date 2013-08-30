/*
    This does Huffman encoding compression
*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Compressor2 extends Applet implements ActionListener {
     TextArea textarea, codetable, stats;
     Button compressB, decompressB, exampleB, example2B;
//   Color bg = new Color(134,147,230);   // this isn't same as Compressor1
     Color bg = new Color(0, 180, 255);

     public void init() {
          setLayout (null);

          Label lab = new Label("Text Compression");
          lab.setBounds(120,5,380,38);
          lab.setFont (new Font("SansSerif", Font.BOLD, 36));
          lab.setBackground(bg);
          add(lab);

          int y = 2 + lab.getSize().height;

          lab = new Label("Huffman Encoding");
          lab.setBounds(200,y,380,27);
          lab.setFont (new Font("SansSerif", Font.BOLD, 18));
          lab.setBackground(bg);
          add(lab);

          y += lab.getSize().height + 5;
          int y2 = y;

          textarea = new TextArea(10,40);
          textarea.setBounds(10,y,400, 200);
          textarea.setBackground(Color.white);
          textarea.setText("Your text goes here");
          add(textarea);

          y += textarea.getSize().height + 8;

          codetable = new TextArea(100,20);
          codetable.setBounds(10,y,250,200);
          codetable.setBackground(Color.white);
          add(codetable);

          Label lab2 = new Label("Statistics:");
          lab2.setBackground(bg);
          lab2.setBounds(320, y, 100, 25);
          add(lab2);

          stats = new TextArea(2,30);
          stats.setBounds(320, y+30, 250, 80);
          stats.setBackground(Color.white);
          stats.setEditable(false);
          add(stats);

          y = y2;

          compressB = new Button("compress");
          compressB.addActionListener(this);
          compressB.setBounds(420,y,90,25);
          add(compressB);

          y += compressB.getSize().height + 3;

          decompressB = new Button("decompress");
          decompressB.addActionListener(this);
          decompressB.setBounds(420,y,90,25);
          add(decompressB);

          y += decompressB.getSize().height + 3;

          exampleB = new Button("example");
          exampleB.addActionListener(this);
          exampleB.setBounds(420,y,90,25);
          add(exampleB);

          y += exampleB.getSize().height + 3;

          example2B = new Button("example2");
          example2B.addActionListener(this);
          example2B.setBounds(420,y,90,25);
          add(example2B);

          y += example2B.getSize().height + 3;

          codetable.setText("a 00\n"+
                            "e 01\n"+
                            "l 100\n"+
                            "o 110\n"+
                            "r 111\n"+
                            "b 1010\n"+
                            "d 1011");

          setBackground(bg);
     }
     
     public void actionPerformed(ActionEvent e) {
          if (e.getSource() == compressB) 
               compress();
          if (e.getSource() == decompressB)
               decompress();
          if (e.getSource() == exampleB)
               makeExample1();
          if (e.getSource() == example2B)
               makeExample2();
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

     private void compress () {
          if (textarea.getText().length() == 0) {
               textarea.setText("You must put some text in here first!");
               return;
          }
          int originalSize = textarea.getText().length() * 8;
          String news = "";
          String temp = textarea.getText();
          for (int i=0; i<temp.length(); i++) {
               char ch = temp.charAt(i);
               news += replaceChar (ch);
          }
          textarea.setText(news);
          int newSize = textarea.getText().length();
          double savings = (originalSize-newSize) / (double)originalSize*100;
          savings = round(savings,1);
          stats.setText("original size was "+originalSize+"\n"+
                        "current  size is  "+newSize+"\n"+
                        "Savings was "+savings+"%");
     }

     private String replaceChar (char ch) {
          StringTokenizer st = new StringTokenizer(codetable.getText(),"\n");
          while (st.hasMoreTokens()) {
               String line = st.nextToken();
               char plainch = line.charAt(0);
               if (plainch == ch) {
                    String code = line.substring(2).trim();
                    return code;
               }
          }
          return ch+"";
     }

     private void decompress () {
          String news = "";
          String temp = textarea.getText();

          while (temp.length() > 0) {
               String combined = findReplacement (temp);
               if (combined == null) {
                    textarea.setText("ERROR:  cannot decode!");
                    return;
               }
               char ch = combined.charAt(0);
               news += ch;
               int length = combined.length() - 1;
               temp = temp.substring(length);
          }

          textarea.setText(news);
     }

     private String findReplacement (String s) {
          StringTokenizer st = new StringTokenizer(codetable.getText(),"\n");
          while (st.hasMoreTokens()) {
               String line = st.nextToken();
               char plainch = line.charAt(0);
               String code = line.substring(2).trim();
               if (s.startsWith(code)) 
                    return plainch + code;
          }
          return null;
     }

     static double round (double n, int numplaces) {
          n += Math.pow(0.1,numplaces)/2;
          n *= Math.pow(10,numplaces);
          long m = (long)n;
          return m / Math.pow(10,numplaces);
     }

     void makeExample1() {
          textarea.setText("DOORBELL");
          codetable.setText("A 00\n"+
                            "E 01\n"+
                            "L 100\n"+
                            "O 110\n"+
                            "R 111\n"+
                            "B 1010\n"+
                            "D 1011");
     }

     void makeExample2() {
          textarea.setText("THIS IS A TEST OF THE "+
                           "EMERGENCY BROADCAST SYSTEM");
          codetable.setText(
"   00\n"+
"R  0100\n"+
"N  0101\n"+
"O  0110\n"+
"D  01110\n"+
"G  011110\n"+
"B  011111\n"+
"I  1000\n"+
"S  1001\n"+
"A  1010\n"+
"L  10110\n"+
"C  10111\n"+
"E  1100\n"+
"U  110100\n"+
"P  110101\n"+
"M  110110\n"+
"W  1101110\n"+
"Y  1101111\n"+
"T  1110\n"+
"H  11110\n"+
"F  111110\n"+
"V  1111110\n"+
"K  11111110\n"+
"X  111111110\n"+
"Q  1111111110\n"+
"Z  11111111110\n"+
"J  11111111111\n"
);
     }
}
