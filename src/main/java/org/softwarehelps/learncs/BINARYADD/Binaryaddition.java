package org.softwarehelps.learncs.BINARYADD;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class Binaryaddition extends Applet 
        implements ActionListener, ComponentListener, TextListener
{
     TextField binaryA, binaryB, binaryC;

     TextField decimalA, decimalB, decimalC;
     Button addB, exampleB, clearB;

     TextField overflow, carry7, carry6, carry5, carry4, carry3, carry2, carry1;

     TextField A0, A1, A2, A3, A4, A5, A6, A7;
     TextField B0, B1, B2, B3, B4, B5, B6, B7;
     TextField C0, C1, C2, C3, C4, C5, C6, C7, CX;
     Image buffer;
     Graphics gg;

     int commonHeight = 26;

     public void init() {
          setLayout(null);
          Color acolor = new Color(246,227,218);

          Label toplab = new Label("Binary addition");
          toplab.setFont(new Font("Serif",Font.BOLD,36));
          toplab.setBackground(acolor);
          toplab.setBounds(230,30,300,50);
          add(toplab);

          Font bigfont = new Font("SansSerif", Font.BOLD, 26);
          Font smallfont = new Font("SansSerif", Font.BOLD, 12);

          int xseparation = 43;
          int x = 6;
          int width = 22;

          overflow=new TextField(40);
          overflow.addActionListener(this);
          overflow.setBounds(x,95,22,25);
          overflow.setBackground(Color.white);
          overflow.setEditable(false);
          overflow.setFont(smallfont);
          add(overflow);
          x += width + xseparation;

          carry7=new TextField(40);
          carry7.addActionListener(this);
          carry7.setBounds(x,95,22,25);
          carry7.setBackground(Color.white);
          carry7.setEditable(false);
          carry7.setFont(smallfont);
          add(carry7);
          x += width + xseparation;

          carry6=new TextField(40);
          carry6.addActionListener(this);
          carry6.setBounds(x,95,22,25);
          carry6.setBackground(Color.white);
          carry6.setEditable(false);
          carry6.setFont(smallfont);
          add(carry6);
          x += width + xseparation;

          carry5=new TextField(40);
          carry5.addActionListener(this);
          carry5.setBounds(x,95,22,25);
          carry5.setBackground(Color.white);
          carry5.setEditable(false);
          carry5.setFont(smallfont);
          add(carry5);
          x += width + xseparation;

          carry4=new TextField(40);
          carry4.addActionListener(this);
          carry4.setBounds(x,95,22,25);
          carry4.setBackground(Color.white);
          carry4.setEditable(false);
          carry4.setFont(smallfont);
          add(carry4);
          x += width + xseparation;

          carry3=new TextField(40);
          carry3.addActionListener(this);
          carry3.setBounds(x,95,22,25);
          carry3.setBackground(Color.white);
          carry3.setEditable(false);
          carry3.setFont(smallfont);
          add(carry3);
          x += width + xseparation;

          carry2=new TextField(40);
          carry2.addActionListener(this);
          carry2.setBounds(x,95,22,25);
          carry2.setBackground(Color.white);
          carry2.setEditable(false);
          carry2.setFont(smallfont);
          add(carry2);
          x += width + xseparation;

          carry1=new TextField(40);
          carry1.addActionListener(this);
          carry1.setBounds(x,95,22,25);
          carry1.setBackground(Color.white);
          carry1.setEditable(false);
          carry1.setFont(smallfont);
          add(carry1);


          xseparation = 25;
          x = 32;
          width = 40;

          A7=new TextField(40);
          A7.addActionListener(this);
          A7.setBounds(x,134,40,40);
          A7.setFont(bigfont);
          A7.setBackground(Color.white);
          A7.setEditable(false);
          add(A7);
          x += width + xseparation;

          A6=new TextField(40);
          A6.addActionListener(this);
          A6.setBounds(x,134,40,40);
          A6.setFont(bigfont);
          A6.setBackground(Color.white);
          A6.setEditable(false);
          add(A6);
          x += width + xseparation;

          A5=new TextField(40);
          A5.addActionListener(this);
          A5.setBounds(x,134,40,40);
          A5.setFont(bigfont);
          A5.setBackground(Color.white);
          A5.setEditable(false);
          add(A5);
          x += width + xseparation;

          A4=new TextField(40);
          A4.addActionListener(this);
          A4.setBounds(x,134,40,40);
          A4.setFont(bigfont);
          A4.setBackground(Color.white);
          A4.setEditable(false);
          add(A4);
          x += width + xseparation;

          A3=new TextField(40);
          A3.addActionListener(this);
          A3.setBounds(x,134,40,40);
          A3.setFont(bigfont);
          A3.setBackground(Color.white);
          A3.setEditable(false);
          add(A3);
          x += width + xseparation;

          A2=new TextField(40);
          A2.addActionListener(this);
          A2.setBounds(x,134,40,40);
          A2.setFont(bigfont);
          A2.setBackground(Color.white);
          A2.setEditable(false);
          add(A2);
          x += width + xseparation;

          A1=new TextField(40);
          A1.addActionListener(this);
          A1.setBounds(x,134,40,40);
          A1.setFont(bigfont);
          A1.setBackground(Color.white);
          A1.setEditable(false);
          add(A1);
          x += width + xseparation;

          A0=new TextField(40);
          A0.addActionListener(this);
          A0.setBounds(x,134,40,40);
          A0.setFont(bigfont);
          A0.setBackground(Color.white);
          A0.setEditable(false);
          add(A0);
          x += width + xseparation;

          Label lab = new Label("Binary");
          lab.setBackground(acolor);
          lab.setBounds(x,98,90,21);
          add(lab);

          lab = new Label("Decimal");
          lab.setBackground(acolor);
          lab.setBounds(x+90+25,98,90,21);
          add(lab);

          binaryA=new TextField(40);
          binaryA.addActionListener(this);
          binaryA.addTextListener(this);
          binaryA.setBounds(x,134,90,commonHeight);
          binaryA.setBackground(Color.white);
          add(binaryA);

          x += binaryA.getSize().width + xseparation;

          decimalA=new TextField(40);
          decimalA.addActionListener(this);
          decimalA.addTextListener(this);
          decimalA.setBounds(x,134,90,commonHeight);
          decimalA.setBackground(Color.white);
          add(decimalA);

          x = 32;

          B7=new TextField(40);
          B7.addActionListener(this);
          B7.setBounds(x,212,40,40);
          B7.setFont(bigfont);
          B7.setBackground(Color.white);
          B7.setEditable(false);
          add(B7);
          x += width + xseparation;

          B6=new TextField(40);
          B6.addActionListener(this);
          B6.setBounds(x,212,40,40);
          B6.setFont(bigfont);
          B6.setBackground(Color.white);
          B6.setEditable(false);
          add(B6);
          x += width + xseparation;

          B5=new TextField(40);
          B5.addActionListener(this);
          B5.setBounds(x,212,40,40);
          B5.setFont(bigfont);
          B5.setBackground(Color.white);
          B5.setEditable(false);
          add(B5);
          x += width + xseparation;

          B4=new TextField(40);
          B4.addActionListener(this);
          B4.setBounds(x,212,40,40);
          B4.setFont(bigfont);
          B4.setBackground(Color.white);
          B4.setEditable(false);
          add(B4);
          x += width + xseparation;

          B3=new TextField(40);
          B3.addActionListener(this);
          B3.setBounds(x,212,40,40);
          B3.setFont(bigfont);
          B3.setBackground(Color.white);
          B3.setEditable(false);
          add(B3);
          x += width + xseparation;

          B2=new TextField(40);
          B2.addActionListener(this);
          B2.setBounds(x,212,40,40);
          B2.setFont(bigfont);
          B2.setBackground(Color.white);
          B2.setEditable(false);
          add(B2);
          x += width + xseparation;

          B1=new TextField(40);
          B1.addActionListener(this);
          B1.setBounds(x,212,40,40);
          B1.setFont(bigfont);
          B1.setBackground(Color.white);
          B1.setEditable(false);
          add(B1);
          x += width + xseparation;

          B0=new TextField(40);
          B0.addActionListener(this);
          B0.setBounds(x,212,40,40);
          B0.setFont(bigfont);
          B0.setBackground(Color.white);
          B0.setEditable(false);
          add(B0);
          x += width + xseparation;

          binaryB=new TextField(40);
          binaryB.addActionListener(this);
          binaryA.addTextListener(this);
          binaryB.setBounds(x,212,90,commonHeight);
          binaryB.setBackground(Color.white);
          add(binaryB);
          x += binaryB.getSize().width + xseparation;

          decimalB=new TextField(40);
          decimalB.addActionListener(this);
          decimalA.addTextListener(this);
          decimalB.setBounds(x,212,90,commonHeight);
          decimalB.setBackground(Color.white);
          add(decimalB);

          CX=new TextField(40);         // This is the pseudo-sum
          CX.addActionListener(this);   // it is really the same value
          CX.setBounds(6,300,20,20);    // as overflow, but it sits down
          CX.setFont(smallfont);        // on the same line as the C7,C6...
          CX.setBackground(Color.white);// boxes
          add(CX);

          x = 32;

          C7=new TextField(40);
          C7.addActionListener(this);
          C7.setBounds(x,280,40,40);
          C7.setFont(bigfont);
          C7.setBackground(Color.white);
          add(C7);
          x += width + xseparation;

          C6=new TextField(40);
          C6.addActionListener(this);
          C6.setBounds(x,280,40,40);
          C6.setFont(bigfont);
          C6.setBackground(Color.white);
          add(C6);
          x += width + xseparation;

          C5=new TextField(40);
          C5.addActionListener(this);
          C5.setBounds(x,280,40,40);
          C5.setFont(bigfont);
          C5.setBackground(Color.white);
          add(C5);
          x += width + xseparation;

          C4=new TextField(40);
          C4.addActionListener(this);
          C4.setBounds(x,280,40,40);
          C4.setFont(bigfont);
          C4.setBackground(Color.white);
          add(C4);
          x += width + xseparation;

          C3=new TextField(40);
          C3.addActionListener(this);
          C3.setBounds(x,280,40,40);
          C3.setFont(bigfont);
          C3.setBackground(Color.white);
          add(C3);
          x += width + xseparation;

          C2=new TextField(40);
          C2.addActionListener(this);
          C2.setBounds(x,280,40,40);
          C2.setFont(bigfont);
          C2.setBackground(Color.white);
          add(C2);
          x += width + xseparation;

          C1=new TextField(40);
          C1.addActionListener(this);
          C1.setBounds(x,280,40,40);
          C1.setFont(bigfont);
          C1.setBackground(Color.white);
          add(C1);
          x += width + xseparation;

          C0=new TextField(40);
          C0.addActionListener(this);
          C0.setBounds(x,280,40,40);
          C0.setFont(bigfont);
          C0.setBackground(Color.white);
          add(C0);
          x += width + xseparation;

          binaryC=new TextField(40);
          binaryC.addActionListener(this);
          binaryC.setBounds(x,280,90,commonHeight);
          binaryC.setBackground(Color.white);
          add(binaryC);
          x += binaryC.getSize().width + xseparation;

          decimalC=new TextField(40);
          decimalC.addActionListener(this);
          decimalC.setBounds(x,280,90,commonHeight);
          decimalC.setBackground(Color.white);
          add(decimalC);

          x = 300;

          addB=new Button("Add");
          addB.addActionListener(this);
          addB.setBounds(x,353,80,40);
          add(addB);
          x += 5 + addB.getSize().width;

          clearB=new Button("Clear");
          clearB.addActionListener(this);
          clearB.setBounds(x,353,80,40);
          add(clearB);
          x += 5 + clearB.getSize().width;

          exampleB=new Button("Example");
          exampleB.addActionListener(this);
          exampleB.setBounds(x,353,80,40);
          add(exampleB);

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);
          setSize(791,500);
          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();
     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == addB) 
               add();
          else if (e.getSource() == clearB) 
               clear();
          else if (e.getSource() == exampleB) 
               makeExample();
     }

     public void textValueChanged(TextEvent e) {
          if (e.getSource() == decimalA) 
               decimalA.setBackground(Color.white);
          if (e.getSource() == decimalB) 
               decimalB.setBackground(Color.white);
          if (e.getSource() == binaryA) 
               binaryA.setBackground(Color.white);
          if (e.getSource() == binaryB) 
               binaryB.setBackground(Color.white);
     }

     public void componentResized(ComponentEvent e) {}
     public void componentHidden(ComponentEvent e) {}
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          g.fillRect(0,0,buffer.getWidth(this),buffer.getHeight(this));
          gg.drawLine(10,260,getSize().width-5,260);
          gg.drawString("+",18,250);
          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     private void clear() {
          binaryC.setText("");
          binaryB.setText("");
          binaryA.setText("");
          decimalC.setText("");
          decimalB.setText("");
          decimalA.setText("");
          overflow.setText("");
          carry6.setText("");
          carry5.setText("");
          carry4.setText("");
          carry3.setText("");
          carry2.setText("");
          carry1.setText("");
          carry7.setText("");
          B0.setText("");
          B1.setText("");
          B2.setText("");
          B3.setText("");
          B4.setText("");
          B5.setText("");
          B6.setText("");
          B7.setText("");
          A0.setText("");
          A1.setText("");
          A2.setText("");
          A3.setText("");
          A4.setText("");
          A5.setText("");
          A6.setText("");
          A7.setText("");
          C0.setText("");
          C1.setText("");
          C2.setText("");
          C3.setText("");
          C4.setText("");
          C5.setText("");
          C6.setText("");
          C7.setText("");
     }

     private void makeExample () {
          decimalA.setText("97");
          decimalB.setText("46");
     }

     private void add() {
          int n1 = U.atoi(decimalA.getText());
          int n2 = U.atoi(decimalB.getText());
          if (n1 > 255 || n1 < 0) {
               decimalA.setBackground(Color.pink);
               new Popup("The decimal numbers to add\n"+
                         "must be between 0 and 255 (inclusive.)\n");
               return;
          }
          if (n2 > 255 || n2 < 0) {
               decimalB.setBackground(Color.pink);
               new Popup("The decimal numbers to add\n"+
                         "must be between 0 and 255 (inclusive.)\n");
               return;
          }
          if (!U.isBinary(binaryA.getText())) {
               binaryA.setBackground(Color.pink);
               new Popup("You must type in a binary number\n"+
                         "here.  Binary numbers contain only\n"+
                         "1s and 0s.\n");
               return;
          }
          if (!U.isBinary(binaryB.getText())) {
               binaryB.setBackground(Color.pink);
               new Popup("You must type in a binary number\n"+
                         "here.  Binary numbers contain only\n"+
                         "1s and 0s.\n");
               return;
          }
          if (binaryA.getText().length() > 8) {
               binaryA.setBackground(Color.pink);
               new Popup("Your binary number is too big!\n"+
                         "It must be 8 bits or less.\n");
               return;
          }
          if (binaryB.getText().length() > 8) {
               binaryB.setBackground(Color.pink);
               new Popup("Your binary number is too big!\n"+
                         "It must be 8 bits or less.\n");
               return;
          }


          new Thread() {
               public void run() {
                    addup();
               }
          }.start();
     }

     public void addup() {

          String binA = "0", binB = "0";

          if (decimalA.getText().length() > 0)
               binA = convert(U.atoi(decimalA.getText()), 2);
          else {
               binA = binaryA.getText();
               decimalA.setText(""+U.deconvert(binA,2));
          }

          if (decimalB.getText().length() > 0)
               binB = convert(U.atoi(decimalB.getText()), 2);
          else {
               binB = binaryB.getText();
               decimalB.setText(""+U.deconvert(binB,2));
          }

          while (binA.length() < 8) 
               binA = "0"+binA;
          while (binB.length() < 8) 
               binB = "0"+binB;

          binaryA.setText(binA);
          binaryB.setText(binB);

          A7.setText(binA.charAt(0)+"");
          A6.setText(binA.charAt(1)+"");
          A5.setText(binA.charAt(2)+"");
          A4.setText(binA.charAt(3)+"");
          A3.setText(binA.charAt(4)+"");
          A2.setText(binA.charAt(5)+"");
          A1.setText(binA.charAt(6)+"");
          A0.setText(binA.charAt(7)+"");
               
          B7.setText(binB.charAt(0)+"");
          B6.setText(binB.charAt(1)+"");
          B5.setText(binB.charAt(2)+"");
          B4.setText(binB.charAt(3)+"");
          B3.setText(binB.charAt(4)+"");
          B2.setText(binB.charAt(5)+"");
          B1.setText(binB.charAt(6)+"");
          B0.setText(binB.charAt(7)+"");

          C7.setText("");
          C6.setText("");
          C5.setText("");
          C4.setText("");
          C3.setText("");
          C2.setText("");
          C1.setText("");
          C0.setText("");

          overflow.setText("");
          carry7.setText("");
          carry6.setText("");
          carry5.setText("");
          carry4.setText("");
          carry3.setText("");
          carry2.setText("");
          carry1.setText("");

          decimalC.setText("");
          binaryC.setText("");


          int carry, sumbit, sum;

          sum = U.atoi(A0.getText()) + U.atoi(B0.getText());
          carry = 0;
          if (sum > 1) {     
               sumbit = sum-2;
               carry = 1;
          }
          else
               sumbit = sum;
          flash (A0, Color.green);
          flash (B0, Color.green);
          C0.setText(sumbit+"");
          flash (C0, Color.yellow);
          carry1.setText(carry+"");
          flash (carry1, Color.yellow);


          sum = U.atoi(A1.getText()) + U.atoi(B1.getText()) + carry;
          carry = 0;
          if (sum > 1) {     
               sumbit = sum-2;
               carry = 1;
          }
          else
               sumbit = sum;
          flash (A1, Color.green);
          flash (B1, Color.green);
          C1.setText(sumbit+"");
          flash (C1, Color.yellow);
          carry2.setText(carry+"");
          flash (carry2, Color.yellow);


          sum = U.atoi(A2.getText()) + U.atoi(B2.getText()) + carry;
          carry = 0;
          if (sum > 1) {     
               sumbit = sum-2;
               carry = 1;
          }
          else
               sumbit = sum;
          flash (A2, Color.green);
          flash (B2, Color.green);
          C2.setText(sumbit+"");
          flash (C2, Color.yellow);
          carry3.setText(carry+"");
          flash (carry3, Color.yellow);


          sum = U.atoi(A3.getText()) + U.atoi(B3.getText()) + carry;
          carry = 0;
          if (sum > 1) {     
               sumbit = sum-2;
               carry = 1;
          }
          else
               sumbit = sum;
          flash (A3, Color.green);
          flash (B3, Color.green);
          C3.setText(sumbit+"");
          flash (C3, Color.yellow);
          carry4.setText(carry+"");
          flash (carry4, Color.yellow);


          sum = U.atoi(A4.getText()) + U.atoi(B4.getText()) + carry;
          carry = 0;
          if (sum > 1) {     
               sumbit = sum-2;
               carry = 1;
          }
          else
               sumbit = sum;
          flash (A4, Color.green);
          flash (B4, Color.green);
          C4.setText(sumbit+"");
          flash (C4, Color.yellow);
          carry5.setText(carry+"");
          flash (carry5, Color.yellow);


          sum = U.atoi(A5.getText()) + U.atoi(B5.getText()) + carry;
          carry = 0;
          if (sum > 1) {     
               sumbit = sum-2;
               carry = 1;
          }
          else
               sumbit = sum;
          flash (A5, Color.green);
          flash (B5, Color.green);
          C5.setText(sumbit+"");
          flash (C5, Color.yellow);
          carry6.setText(carry+"");
          flash (carry6, Color.yellow);


          sum = U.atoi(A6.getText()) + U.atoi(B6.getText()) + carry;
          carry = 0;
          if (sum > 1) {     
               sumbit = sum-2;
               carry = 1;
          }
          else
               sumbit = sum;
          flash (A6, Color.green);
          flash (B6, Color.green);
          C6.setText(sumbit+"");
          flash (C6, Color.yellow);
          carry7.setText(carry+"");
          flash (carry7, Color.yellow);


          sum = U.atoi(A7.getText()) + U.atoi(B7.getText()) + carry;
          carry = 0;
          if (sum > 1) {     
               sumbit = sum-2;
               carry = 1;
          }
          else
               sumbit = sum;
          flash (A7, Color.green);
          flash (B7, Color.green);
          C7.setText(sumbit+"");
          flash (C7, Color.yellow);
          overflow.setText(carry+"");
          flash (overflow, Color.yellow);
          CX.setText(carry+"");

          decimalC.setText("" + (U.atoi(decimalA.getText()) +
                                 U.atoi(decimalB.getText())));

          String binC = C7.getText()+C6.getText()+C5.getText()+
                        C4.getText()+C3.getText()+C2.getText()+
                        C1.getText()+C0.getText();
          binaryC.setText(binC);
     }

     private void flash (TextField tf, Color color) {
          tf.setBackground(color);
          try {
               Thread.sleep(300);
          }
          catch (InterruptedException e) {}
          tf.setBackground(Color.white);
     }

     private String convert (int n, int base) {
          String s = "";
          while (n > 0) {
               int rem = n % base;
               n = n / base;
               s = Character.forDigit(rem, base) + s;
          }
          return s.toUpperCase();
     }
}
