package org.softwarehelps.learncs.REALS;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Reals extends Applet 
        implements ActionListener, ComponentListener, ItemListener,
                   TextListener
{
     TextField realinputTF;
     TextField mantissaTF, exponentTF, signbitTF;
     TextField BexponentTF, BmantissaTF, BsignbitTF;
     TextField BinformatTF;

     Label label1, label2, label3, label4;
     Button displayB;
     Image buffer;
     Graphics gg;
     Image bgImage;
     Font labelFont = new Font("SansSerif", Font.BOLD, 16);

     Choice exampleCH;

     @Override
     public void init() {
          setLayout(null);
          Color acolor = new Color(246,227,218);

//          bgImage = getImage(getCodeBase(), "balloon.jpg");

          Label labx;
          labx=new Label("REAL NUMBER REPRESENTATIONS");
          labx.setBackground(acolor);
          labx.setFont(new Font("SansSerif", Font.BOLD, 24));
          labx.setBounds(80,20,435,40);
          add(labx);

          label1=new Label("A real number:");
          label1.setBackground(acolor);
          label1.setBounds(33,90,120,29);
          label1.setFont(labelFont);
          add(label1);

          realinputTF=new TextField(40);
          realinputTF.setBounds(180,90,180,29);
          realinputTF.addActionListener(this);
          realinputTF.addTextListener(this);
          realinputTF.setBackground(Color.white);
          add(realinputTF);

          exampleCH = new Choice();
          exampleCH.addItem("1.0");
          exampleCH.addItem("2.0");
          exampleCH.addItem("20.0");
          exampleCH.addItem("0.5");
          exampleCH.addItem("0.6");
          exampleCH.addItem("0.1");
          exampleCH.addItem("0.75");
          exampleCH.addItem("0.9999");
          exampleCH.setBounds(420,90,80,29);
          exampleCH.addItemListener(this);
          add(exampleCH);

          label1=new Label("Examples:");
          label1.setBackground(acolor);
          label1.setBounds(420,60,80,29);
//          label1.setFont(labelFont);
          add(label1);


          displayB=new Button("Display Representations");
          displayB.setBounds(realinputTF.getLocation().x,
                             realinputTF.getLocation().y+realinputTF.getSize().height+7,
                             206,31);
          displayB.addActionListener(this);
          add(displayB);

          int yy = 200;
          int xx = 51;

          label2=new Label("Scientific Notation:");
          label2.setBackground(acolor);
          label2.setBounds(xx-8,yy-30,150,29);
          add(label2);

          signbitTF=new TextField(40);
          signbitTF.setBounds(xx,yy,20,27);
          signbitTF.setBackground(Color.white);
          add(signbitTF);
          xx += signbitTF.getSize().width+10;

          mantissaTF=new TextField(40);
          mantissaTF.setBounds(xx,yy,366,27);
          mantissaTF.setBackground(Color.white);
          add(mantissaTF);
          xx += mantissaTF.getSize().width+48;

          exponentTF=new TextField(40);
          exponentTF.setBounds(xx,yy,125,27);
          exponentTF.setBackground(Color.white);
          add(exponentTF);

          yy = 300;
          xx = 51;

          label3=new Label("Decimal raised to a power of 2:");
          label3.setBackground(acolor);
          label3.setBounds(xx-8,yy-30,200,29);
          add(label3);

          BsignbitTF=new TextField(40);
          BsignbitTF.setBounds(xx,yy,20,27);
          BsignbitTF.setBackground(Color.white);
          add(BsignbitTF);
          xx += BsignbitTF.getSize().width+10;

          BmantissaTF=new TextField(40);
          BmantissaTF.setBounds(xx,yy,366,27);
          BmantissaTF.setBackground(Color.white);
          add(BmantissaTF);
          xx += BmantissaTF.getSize().width+48;

          BexponentTF=new TextField(40);
          BexponentTF.setBounds(xx,yy,125,27);
          BexponentTF.setBackground(Color.white);
          add(BexponentTF);

          label4=new Label("Binary:");
          label4.setBackground(acolor);
          label4.setBounds(120,yy+60-25,100,27);
          add(label4);

          BinformatTF=new TextField(50);
          BinformatTF.setBounds(120,yy+65,300,27);
          BinformatTF.setBackground(Color.white);
          add(BinformatTF);

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);
          setSize(650,500);

          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();

          repaint();
     }

    /**
     * Called when 'Display Representations' button displayB is pressed, or 
     * Enter key pressed in 'real number' text entry field realinputTF.
     * 
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == realinputTF
                || e.getSource() == displayB) {
            if (realinputTF.getText().length() == 0) {
                return;
            }
            if (!toScientificNotation()) {
                return;
            }
            toBinary();
        }
    }

     public void itemStateChanged (ItemEvent e) {
          realinputTF.setText(exampleCH.getSelectedItem());
     }

     public void textValueChanged (TextEvent e) {
          realinputTF.setBackground(Color.white);
     }

     public void componentResized(ComponentEvent e) {}
     public void componentHidden(ComponentEvent e) {}
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}

     @Override
     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          g.fillRect(0,0,buffer.getWidth(this),buffer.getHeight(this));
//        gg.drawImage(bgImage, 0,0, this);
          gg.setColor(Color.black);
          gg.setFont(new Font("SansSerif", Font.BOLD, 24));
          int y = 210;
          gg.drawString("x",450,y+10);
          gg.drawString("10",465,y+25);
          gg.drawString("x",460,y+110);
          gg.drawString("2",479,y+110+15);
          g.drawImage(buffer,0,0,this);
     }

     @Override
     public void update (Graphics g) {
          paint (g);
     }

     /**
      * Convert value in realinputTF text field, to base 
      * 10 mantissa & exponent.
      * @return 
      */
     private boolean toScientificNotation() {
          if (!U.isReal(realinputTF.getText())) {
               realinputTF.setBackground(Color.pink);
               new Popup("Invalid real number.");
               return false;
          }

          double x = U.atod(realinputTF.getText());         
          int sign = 0;
          if (x < 0) {
               sign = 1;
               x = -x;
          }

          int exponent = 0;

          if (x != 0) {
               if (x > 10) {
                    while (x > 10) {
                         exponent++;
                         x /= 10.0;
                    }
               }
               else if (x < 1) {
                    while (x < 1) {
                         exponent--;
                         x *= 10.0;
                    }
               }
          }
     
          signbitTF.setText(sign+"");
          mantissaTF.setText(x+"");
          exponentTF.setText(exponent+"");

          return true;
     }

     /**
      * Convert value in realinputTF text field, to base 
      * 10 mantissa & exponent.
      * @return 
      */
     private void toBinary() {
          double x = U.atod(realinputTF.getText());         
          int sign = 0;
          if (x < 0) {
               sign = 1;
               x = -x;
          }

          int exponent = 0;
          if (x != 0) {
               if (x >= 1) {
                    while (x >= 1) {
                         exponent++;
                         x /= 2.0;
                    }
               }
               else if (x < .5) {
                    while (x < .5) {
                         exponent--;
                         x *= 2.0;
                    }
               }
          }

          BsignbitTF.setText(sign+"");
          BmantissaTF.setText(x+"");
          BexponentTF.setText(exponent+"");

          String exponentString = "";
          if (exponent < 0)
               exponentString = "1" + pad(convert(-exponent),5,'0');
          else
               exponentString = "0" + pad(convert(exponent),5,'0');

          String binformat = sign+"  "+
                             convertToBinPoint(x)+"  "+
                             exponentString;
          BinformatTF.setText(binformat);
     }
 
     private String convertToBinPoint(double x) {
          double powerNeg2 = .5;
          int numdigits = 0;
          int numDigitsWanted = 10;
          String s ="0.";

          if (x == 1.0)
               s = "1.0";
          else {
               while (x > 0 && numdigits < numDigitsWanted) {
                    if (x >= powerNeg2) {
                         s += "1";
                         x -= powerNeg2;
                    }
                    else
                         s += "0";
                    powerNeg2 /= 2;
                    numdigits++;
               }
          }
          while (s.length() < numDigitsWanted+2) 
               s += "0";
          return s;
     }

     public String convert (int n) {
          String s = "";
          int rem = 0;
          if (n == 0)
               return "0";
          while (n > 0) {
               rem = n % 2;
               n = n / 2;
               s = Character.forDigit(rem, 2) + s;
          }
          return s;
     }

     public static String pad (String s, int desiredLength, char ch) {
          while (s.length() < desiredLength)
               s = ch + s;
          return s;
     }
}
