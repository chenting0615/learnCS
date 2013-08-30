package org.softwarehelps.learncs.TEXTCODES;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class TextCodes extends Applet implements ActionListener {
     TextArea input, output1, output2;
     Button translateButton;
     Color bg = new Color(0,200,255);
     TextArea bigTA;
     final static int highestNumber = 256;

     public void init() {
          setLayout (null);

          bigTA = new TextArea(30,15);
          bigTA.setBackground(Color.white);
          bigTA.setFont (new Font("Courier", Font.PLAIN, 12));
          bigTA.setBounds(10,10,100,500);
          bigTA.setEditable(false);
          String s = "";

          for (int i=32; i<256; i++)
               s += i + "    "+ (char)i + "\n";

          bigTA.setText(s);

          add(bigTA);


          Label lab = new Label("Text encoding in ASCII");
          lab.setBounds(120,2,500,45);
          lab.setFont(new Font("SansSerif",Font.BOLD,38));
          lab.setBackground(bg);
          add(lab);

          int y = 2 + lab.getSize().height;

          lab = new Label("Your text:");
          lab.setBounds(120,y,200,25);
          lab.setBackground(bg);
          add(lab);
          y += lab.getSize().height + 5;

          input = new TextArea(3,20);
          input.setBounds(120,y,300,100);
          input.setBackground(Color.white);
          add(input);
          y += input.getSize().height + 5;

          lab = new Label("Decimal (ASCII) codes:");
          lab.setBounds(120,y,200,25);
          lab.setBackground(bg);
          add(lab);
          y += lab.getSize().height + 5;

          output1 = new TextArea(3,20);
          output1.setBounds(120,y,300,100);
          output1.setBackground(Color.white);
          output1.setEditable(false);
          add(output1);
          y += output1.getSize().height + 5;

          lab = new Label("Binary (ASCII) codes:");
          lab.setBounds(120,y,200,25);
          lab.setBackground(bg);
          add(lab);
          y += lab.getSize().height + 5;

          output2 = new TextArea(3,20);
          output2.setBounds(120,y,300,100);
          output2.setBackground(Color.white);
          output2.setEditable(false);
          add(output2);
          y += output2.getSize().height + 5;

          translateButton = new Button("translate text");
          translateButton.addActionListener(this);
          translateButton.setBounds(200,y,100,25);
          add(translateButton);

          setBackground(bg);
     }
     
     public void actionPerformed(ActionEvent e) {
          if (e.getSource() == translateButton) {
               translate();
          }
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

     private void translate() {
          String s1 = input.getText();
          String s2 = "";
          String s3 = "";

          for (int i=0; i<s1.length(); i++) {
               char ch = s1.charAt(i);
               if (ch != '\n') {
                    s2 += (int)ch + " ";
                    s3 += convert ((int)ch, 2) + " ";
               }
               else {
                    s2 += "\n";
                    s3 += "\n";
               }
          }

          output1.setText(s2);
          output2.setText(s3);
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
