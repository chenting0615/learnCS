package org.softwarehelps.learncs.NEGNUMS;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class Negatives extends Applet 
        implements ActionListener, ComponentListener, TextListener
{
     TextField binary1TF, binary2TF, decimalTF;
     TextField numDigitsTF;
     Button convertB;
     Label Label1, Label2, Label3, Label4;
     Image buffer;
     Graphics gg;
     Font labelFont = new Font("SansSerif", Font.BOLD, 16);

     public void init() {
          setLayout(null);
          Color acolor = new Color(246,227,218);

          Label toplab = new Label("Negative Number Representations");
          toplab.setFont(new Font("SansSerif",Font.BOLD,36));
          toplab.setBackground(acolor);
          toplab.setBounds(20,30,590,50);
          add(toplab);

          Label1=new Label("Your decimal number:");
          Label1.setBackground(acolor);
          Label1.setBounds(10,107,180,25);
          Label1.setFont(labelFont);
          add(Label1);

          decimalTF=new TextField(40);
          decimalTF.addActionListener(this);
          decimalTF.addTextListener(this);
          decimalTF.setBounds(200,107,266,26);
          decimalTF.setBackground(Color.white);
          add(decimalTF);

          convertB = new Button("Convert to Signed Binary");
          convertB.addActionListener(this);
          convertB.setBounds(decimalTF.getLocation().x+decimalTF.getSize().width+5,
                             decimalTF.getLocation().y,150,25);
          add(convertB);

          Label4=new Label("Number of digits in binary number:");
          Label4.setBackground(acolor);
          Label4.setBounds(95,168,203,26);
          Label4.setFont(labelFont);
          add(Label4);

          numDigitsTF=new TextField(40);
          numDigitsTF.addActionListener(this);
          numDigitsTF.setBounds(316,167,119,27);
          numDigitsTF.setText("8");
          numDigitsTF.setBackground(Color.white);
          add(numDigitsTF);

          Label2=new Label("Sign-magnitude form:");
          Label2.setBackground(acolor);
          Label2.setBounds(10,235,190,26);
          Label2.setFont(labelFont);
          add(Label2);

          binary1TF=new TextField(65);
          binary1TF.addActionListener(this);
          binary1TF.setBounds(200,235,400,26);
          binary1TF.setBackground(Color.white);
          binary1TF.setEditable(false);
          add(binary1TF);

          Label3=new Label("2's complement form:");
          Label3.setBackground(acolor);
          Label3.setBounds(10,297,190,26);
          Label3.setFont(labelFont);
          add(Label3);

          binary2TF=new TextField(65);
          binary2TF.addActionListener(this);
          binary2TF.setBounds(200,297,400,26);
          binary2TF.setBackground(Color.white);
          binary2TF.setEditable(false);
          add(binary2TF);

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);
          setSize(650,350);
          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == decimalTF || e.getSource() == convertB)
               convertBinary();
          if (e.getSource() == numDigitsTF) {
               convertBinary();
          }
     }

     public void textValueChanged (TextEvent e) {
          decimalTF.setBackground(Color.white);
     }
  
     public void componentResized(ComponentEvent e) {}
     public void componentHidden(ComponentEvent e) {}
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          g.fillRect(0,0,buffer.getWidth(this),buffer.getHeight(this));
          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     private void convertBinary() {
          if (decimalTF.getText().length() == 0)
               return;

          if (!U.isint(decimalTF.getText())) {
               decimalTF.setBackground(Color.pink);
               new Popup("You must enter a valid decimal integer\n"+
                         "in this field.\n");
               return;
          }

          int numbits = U.atoi(numDigitsTF.getText());
          long dec = U.atol(decimalTF.getText());
          boolean isNegative;
          boolean tooBig = false;
          boolean do2sCompOnly = false; // we can only show 2's complement for
                                        // numbers like -128 (which is 10000000
                                        // in 8 bits.)

          binary1TF.setText("");
          binary2TF.setText("");

          if (dec < 0) {
               isNegative = true;
               dec = -dec;
               if (dec == U.power(2,numbits-1))
                    do2sCompOnly = true;
          }
          else
               isNegative = false;

          String bin = convert (dec, 2);

          if (bin.length() > numbits-1) {
               tooBig = true;
               if (do2sCompOnly) {
                    new Popup("Only the 2s complement form will fit.");
               }
               else if (!do2sCompOnly) {
                    new Popup("This number can't fit in "+numbits+" bits.");
                    return;
               }
          }

          if (!do2sCompOnly) {
               while (bin.length() < numbits-1)
                    bin = "0"+bin;

               String signedbin;
               if (isNegative)
                    signedbin = "1"+bin;
               else
                    signedbin = "0"+bin;
     
               binary1TF.setText(signedbin);
               binary1TF.select(0,1);
          }

          if (!tooBig) 
               binary2TF.setText(make2sComplement(bin, isNegative));
          else if (do2sCompOnly) 
               binary2TF.setText(make2sComplement(bin, isNegative).substring(1));
          binary2TF.select(0,1);
     }

     private String convert (long n, int base) {
          String s = "";
          if (n == 0)
               return "0";
          while (n > 0) {
               long rem = n % base;
               n = n / base;
               s = Character.forDigit((int)rem, base) + s;
          }
          return s.toUpperCase();
     }

     private String make2sComplement (String s, boolean isneg) {
          char[] number = ("0"+s).toCharArray();
          if (isneg) {
               for (int i=0; i<number.length; i++)
                    if (number[i] == '0')
                         number[i] = '1';
                    else
                         number[i] = '0';
               boolean carry = true;
               for (int i=number.length-1; i>=0; i--) {
                    if (number[i] == '0') {
                         if (carry)
                              number[i] = '1';
                         carry = false;
                    }
                    else {
                         if (carry) {
                              number[i] = '0';
                              carry = true;
                         }
                         else 
                              carry = false;
                    }
               }
          }
          return new String(number);
     }
}
