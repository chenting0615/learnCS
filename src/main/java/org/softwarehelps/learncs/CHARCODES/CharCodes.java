package org.softwarehelps.learncs.CHARCODES;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class CharCodes extends Applet implements ActionListener
{
     Button convert1Button, convert2Button;
     Button example1B, example2B, example3B;
     TextField field1, field2;

     Label Label1, Label2, Label3, Label4;

     Image buffer;
     Graphics gg;

     Color acolor = new Color(246,227,218);
     final static int X=0;
     final static int Y=0;

     public void init() {
          setLayout(null);

          Label2=new Label("Character Codes");
          Label2.setBackground(acolor);
          Label2.setFont(new Font("SansSerif",Font.PLAIN,36));
          Label2.setBounds(X+160,Y+10,501,80);
          add(Label2);

          Label1=new Label("Numeric Code:");
          Label1.setBackground(acolor);
          Label1.setFont(new Font("SansSerif",Font.PLAIN,16));
          Label1.setBounds(X+64,Y+104,120,27);
          add(Label1);

          field1=new TextField(40);
          field1.addActionListener(this);
          field1.setBounds(X+192,Y+104,259,47);
          field1.setFont(new Font("SansSerif",Font.PLAIN,24));
          field1.setBackground(Color.white);
          add(field1);

          convert1Button=new Button("convert to character");
          convert1Button.addActionListener(this);
          convert1Button.setBounds(X+461,Y+104,120,27);
          add(convert1Button);

          Label3=new Label("Corresponding");
          Label3.setBackground(acolor);
          Label3.setFont(new Font("SansSerif",Font.PLAIN,16));
          Label3.setBounds(X+64,Y+180,120,27);
          add(Label3);

          Label4=new Label("Character:");
          Label4.setBackground(acolor);
          Label4.setFont(new Font("SansSerif",Font.PLAIN,16));
          Label4.setBounds(X+64,Y+180+27,120,27);
          add(Label4);

          field2=new TextField(40);
          field2.addActionListener(this);
          field2.setBounds(X+193,Y+180,259,47);
          field2.setFont(new Font("SansSerif",Font.PLAIN,24));
          field2.setBackground(Color.white);
          add(field2);

          convert2Button=new Button("convert to code");
          convert2Button.addActionListener(this);
          convert2Button.setBounds(X+461,Y+180,120,27);
          add(convert2Button);

          int x = X + 200;
          int y = field2.getLocation().y + field2.getSize().height + 15;

          example1B=new Button("Example 1");
          example1B.addActionListener(this);
          example1B.setBounds(x,y,70,27);
          add(example1B);
          x += example1B.getSize().width + 10;
    
          example2B=new Button("Example 2");
          example2B.addActionListener(this);
          example2B.setBounds(x,y,70,27);
          add(example2B);
          x += example2B.getSize().width + 10;

          example3B=new Button("Example 3");
          example3B.addActionListener(this);
          example3B.setBounds(x,y,70,27);
          add(example3B);
          x += example3B.getSize().width + 10;

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);
          setSize(606,299);
          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();
     }

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          gg.setColor(acolor);
          gg.fillRect(0,0,buffer.getWidth(this),buffer.getHeight(this));
          // Write to gg first
          g.drawImage(buffer,0,0,this);
     }
     public void update (Graphics g) {
          paint (g);
     }

     public void actionPerformed(ActionEvent e) {
          if (e.getSource() == field1 || e.getSource() == convert1Button) {
               String s = field1.getText();
               int n;
               if (s.startsWith("0x"))
                    n = deconvert(s.substring(2,s.length()),16);
               else
                    n = atoi(s);
               field2.setText((char)n + "");
          }
          else if (e.getSource() == field2 || e.getSource() == convert2Button) 
               field1.setText(""+(int)field2.getText().charAt(0));
          else if (e.getSource() == example1B) {
               field1.setText("65");
               field2.setText((char)65+"");
          }
          else if (e.getSource() == example2B) {
               field1.setText("0x00D6");
               field2.setText((char)0x00D6+"");
          }
          else if (e.getSource() == example3B) {
               for (int i=33; i<=255; i++) {
                    field1.setText(i+"  (0x00"+Integer.toHexString(i)+")");
                    field2.setText((char)i+"");
                    repaint();
                    try {
                         Thread.sleep(80);
                    } catch (InterruptedException ioe) {}
               }
          }
     }

     private static int atoi (String s) {
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               return Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     private int deconvert (String s, int base) {
          int power = 1;
          int result = 0;

          while (s.length() > 0) {
               char ch = s.charAt(s.length()-1);
               result += Character.digit(ch, base) * power;
               s = s.substring(0,s.length()-1);
               power *= base;
          }

          return result;
     }
}
