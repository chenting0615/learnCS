import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Colors extends Applet implements AdjustmentListener,
                                              ActionListener,
                                              ItemListener,
                                              TextListener
{
     int redvalue, greenvalue, bluevalue;
     float huevalue, satvalue, brightvalue;

     TextField redTF, greenTF, blueTF;
     TextField hueTF, satTF, brightTF;
     Scrollbar redSB, greenSB, blueSB;
     Scrollbar hueSB, satSB, brightSB;

     Choice colorNames;
     Choice fontColor;
     String fontMethod = "auto";

     Label lab1, lab2, lab3, lab4, lab5, lab6, lab7;

     public void init() {
          setLayout (null);
          setSize(600,350);
          setLocation(200,150);
          setBackground(Color.cyan);

          brightvalue = (float)1.0;

          int scrollBarHeight = 140;
          int scrollBarWidth  =  20;

          lab7 = new Label("Color Maker");
          lab7.setFont(new Font("SansSerif",Font.BOLD,36));
          lab7.setBounds(180,15,350,50);
          add(lab7);

          int startingY = 60;

          int x = 20;
          int y = startingY;
          int X = 0;

          lab1 = new Label("Red");         
          lab1.setBounds(X+x,y,35,25);
          add(lab1);

          redTF = new TextField(10);
          redTF.setBackground(Color.white);
          redTF.addActionListener(this);
          redTF.setBounds(X+x,y+30,40,25);
          add(redTF);

          int offset = 8;

          redSB = new Scrollbar(Scrollbar.VERTICAL, 0, 0, 0, 256);
          redSB.setBackground(Color.white);
          redSB.addAdjustmentListener(this);
          redSB.setBounds(X+x+offset, y+60, scrollBarWidth, scrollBarHeight);
          add(redSB);

          x += 60;

          lab2 = new Label("Green");
          lab2.setBounds(X+x,y,35,25);
          add(lab2);

          greenTF = new TextField(10);
          greenTF.setBackground(Color.white);
          greenTF.addActionListener(this);
          greenTF.setBounds(X+x,y+30,40,25);
          add(greenTF);

          greenSB = new Scrollbar(Scrollbar.VERTICAL, 0, 0, 0, 256);
          greenSB.setBackground(Color.white);
          greenSB.addAdjustmentListener(this);
          greenSB.setBounds(X+x+offset, y+60, scrollBarWidth, scrollBarHeight);
          add(greenSB);

          x += 60;

          lab3 = new Label("Blue");
          lab3.setBounds(X+x,y,35,25);
          add(lab3);

          blueTF = new TextField(10);
          blueTF.setBackground(Color.white);
          blueTF.addActionListener(this);
          blueTF.setBounds(X+x,y+30,40,25);
          add(blueTF);

          blueSB = new Scrollbar(Scrollbar.VERTICAL, 0, 0, 0, 256);
          blueSB.setBackground(Color.white);
          blueSB.addAdjustmentListener(this);
          blueSB.setBounds(X+x+offset, y+60, scrollBarWidth, scrollBarHeight);
          add(blueSB);

          y += scrollBarHeight + y + 20;

          y =  blueSB.getLocation().y + blueSB.getSize().height+20;

          colorNames = new Choice();
          colorNames.addItem("Black");
          colorNames.addItem("White");
          colorNames.addItem("Red");
          colorNames.addItem("Blue");
          colorNames.addItem("Green");
          colorNames.addItem("Yellow");
          colorNames.addItem("Orange");
          colorNames.addItem("Pink");
          colorNames.addItem("Cyan");
          colorNames.addItem("Magenta");
          colorNames.addItem("Gray");
          colorNames.addItem("Light Gray");
          colorNames.addItem("Dark Gray");
          colorNames.addItemListener(this);
          colorNames.setBounds(250,90,100,25);
          colorNames.select("Cyan");
          add(colorNames);

          fontColor = new Choice();
          fontColor.addItem("set font color automatically");
          fontColor.addItem("set font color to black");
          fontColor.addItem("set font color to white");
          fontColor.addItemListener(this);
          fontColor.setBounds(210,130,190,25);
          add(fontColor);


          x += colorNames.getSize().width + 15;

          X = 410;
          x = 0;
          y = startingY;

          lab4 = new Label("Hue");
          lab4.setBounds(X+x,y,35,25);
          add(lab4);

          hueTF = new TextField(10);
          hueTF.setBackground(Color.white);
          hueTF.addActionListener(this);
          hueTF.setBounds(X+x,y+30,40,25);
          add(hueTF);

          hueSB = new Scrollbar(Scrollbar.VERTICAL, 0, 0, 0, 100);
          hueSB.addAdjustmentListener(this);
          hueSB.setBounds(X+x+offset, y+60, scrollBarWidth, scrollBarHeight);
          add(hueSB);

          x += 60;

          lab5 = new Label("Sat");
          lab5.setBounds(X+x,y,35,25);
          add(lab5);

          satTF = new TextField(10);
          satTF.setBackground(Color.white);
          satTF.addActionListener(this);
          satTF.setBounds(X+x,y+30,40,25);
          add(satTF);

          satSB = new Scrollbar(Scrollbar.VERTICAL, 0, 0, 0, 100);
          satSB.addAdjustmentListener(this);
          satSB.setBounds(X+x+offset, y+60, scrollBarWidth, scrollBarHeight);
          add(satSB);

          x += 60;

          lab6 = new Label("Bright");
          lab6.setBounds(X+x,y,45,25);
          add(lab6);

          brightTF = new TextField(10);
          brightTF.setBackground(Color.white);
          brightTF.addActionListener(this);
          brightTF.setBounds(X+x,y+30,40,25);
          add(brightTF);

          brightSB = new Scrollbar(Scrollbar.VERTICAL, 0, 0, 0, 100);
          brightSB.addAdjustmentListener(this);
          brightSB.setBounds(X+x+offset, y+60, scrollBarWidth, scrollBarHeight);
          add(brightSB);

          y += scrollBarHeight + y + 20;
          setColors(Color.cyan);
          setVisible(true);
     }

     public void adjustmentValueChanged(AdjustmentEvent ae) {
          boolean rgbset = true;

          if (ae.getSource() == redSB) 
               redvalue = (int)redSB.getValue();
          else if (ae.getSource() == greenSB) 
               greenvalue = (int)greenSB.getValue();
          else if (ae.getSource() == blueSB) 
               bluevalue = (int)blueSB.getValue();

          else if (ae.getSource() == hueSB) {
               huevalue = (float)hueSB.getValue()/(float)100.0;
               rgbset = false;
          }
          else if (ae.getSource() == satSB) {
               satvalue = (float)satSB.getValue()/(float)100.0;
               rgbset = false;
          }
          else if (ae.getSource() == brightSB) {
               brightvalue = (float)brightSB.getValue()/(float)100.0;
               rgbset = false;
          }
 
          if (rgbset) {
               redTF.setText(redvalue+"");
               greenTF.setText(greenvalue+"");
               blueTF.setText(bluevalue+"");
               changeBg(new Color(redvalue,greenvalue,bluevalue));
               setHSB();
          }
          else {
               hueTF.setText(huevalue+"");
               satTF.setText(satvalue+"");
               brightTF.setText(brightvalue+"");
               setRGB();
          }

          repaint();
     }

     public void changeBg (Color c) {
          setBackground(c);
          lab1.setBackground(c);
          lab2.setBackground(c);
          lab3.setBackground(c);
          lab4.setBackground(c);
          lab5.setBackground(c);
          lab6.setBackground(c);
          lab7.setBackground(c);
     }

     public void actionPerformed (ActionEvent e) {
          boolean rgbset = true;

          if (e.getSource() == redTF || e.getSource() == greenTF ||
              e.getSource() == blueTF) {
               getColorValuesFromRGBField();
               setColors(current());
          }
          else if (e.getSource() == hueTF) {
               huevalue = new Float(hueTF.getText()).floatValue();
               rgbset = false;
          }
          else if (e.getSource() == satTF) {
               satvalue = new Float(satTF.getText()).floatValue();
               rgbset = false;
          }
          else if (e.getSource() == brightTF) {
               brightvalue = new Float(brightTF.getText()).floatValue();
               rgbset = false;
          }
          if (rgbset) {
               changeBg (new Color(redvalue, greenvalue, bluevalue));
               redSB.setValue(redvalue);
               greenSB.setValue(greenvalue);
               blueSB.setValue(bluevalue);
               setHSB();
          }
          else {
               hueTF.setText(huevalue+"");
               satTF.setText(satvalue+"");
               brightTF.setText(brightvalue+"");
               setRGB();
          }
          repaint();
     }

     public void itemStateChanged (ItemEvent ie) {
          String desiredColor = colorNames.getSelectedItem();
          if (desiredColor.equals("Black"))
               setColors (Color.black);
          else if (desiredColor.equals("White"))
               setColors (Color.white);
          else if (desiredColor.equals("Red"))
               setColors (Color.red);
          else if (desiredColor.equals("Yellow"))
               setColors (Color.yellow);
          else if (desiredColor.equals("Blue"))
               setColors (Color.blue);
          else if (desiredColor.equals("Green"))
               setColors (Color.green);
          else if (desiredColor.equals("Orange"))
               setColors (Color.orange);
          else if (desiredColor.equals("Pink"))
               setColors (Color.pink);
          else if (desiredColor.equals("Cyan"))
               setColors (Color.cyan);
          else if (desiredColor.equals("Magenta"))
               setColors (Color.magenta);
          else if (desiredColor.equals("Gray"))
               setColors (Color.gray);
          else if (desiredColor.equals("Light Gray"))
               setColors (Color.lightGray);
          else if (desiredColor.equals("Dark Gray"))
               setColors (Color.darkGray);

          String desiredFontColor = fontColor.getSelectedItem();
          if (desiredFontColor.equals("set font color automatically")) 
               fontMethod = "auto";
          else if (desiredFontColor.equals("set font color to black")) 
               fontMethod = "black";
          else if (desiredFontColor.equals("set font color to white")) 
               fontMethod = "white";

          repaint();
     }

     public void getColorValuesFromRGBField() {
          String contents = redTF.getText().toUpperCase();
          try {
               if (contents.startsWith("0X")) {
                    contents = contents.substring(2);
                    redvalue = Integer.valueOf(contents, 16).intValue();
               }
               else if (contents.startsWith("X")) {
                    contents = contents.substring(1);
                    redvalue = Integer.valueOf(contents, 16).intValue();
               }
               else
                    redvalue = new Integer(redTF.getText()).intValue();
               if (redvalue > 255) {
                    redvalue = 255;
                    redTF.setText("255");
               }
          }
          catch (NumberFormatException nfe) {
               redvalue = 255;
               redTF.setText("255");
          }

          contents = greenTF.getText().toUpperCase();
          try {
               if (contents.startsWith("0X")) {
                    contents = contents.substring(2);
                    greenvalue = Integer.valueOf(contents, 16).intValue();
               }
               else if (contents.startsWith("X")) {
                    contents = contents.substring(1);
                    greenvalue = Integer.valueOf(contents, 16).intValue();
               }
               else
                    greenvalue = new Integer(greenTF.getText()).intValue();
               if (greenvalue > 255) {
                    greenvalue = 255;
                    greenTF.setText("255");
               }
          }
          catch (NumberFormatException nfe) {
               greenvalue = 255;
               greenTF.setText("255");
          }

          contents = blueTF.getText().toUpperCase();
          try {
               if (contents.startsWith("0X")) {
                    contents = contents.substring(2);
                    bluevalue = Integer.valueOf(contents, 16).intValue();
               }
               else if (contents.startsWith("X")) {
                    contents = contents.substring(1);
                    bluevalue = Integer.valueOf(contents, 16).intValue();
               }
               else
                    bluevalue = new Integer(blueTF.getText()).intValue();
               if (bluevalue > 255) {
                    bluevalue = 255;
                    blueTF.setText("255");
               }
          }
          catch (NumberFormatException nfe) {
               bluevalue = 255;
               blueTF.setText("255");
          }
     }

     public void textValueChanged (TextEvent te) {
     }

     private Color current() {
          return new Color(redvalue, greenvalue, bluevalue);
     }

     public Color tempColor;

     private void setColors (Color c) {
          tempColor = c;
          new Thread() {
               public void run() {
                    redvalue = tempColor.getRed();
                    redTF.setText(redvalue+"");
                    greenvalue = tempColor.getGreen();
                    greenTF.setText(greenvalue+"");
                    bluevalue = tempColor.getBlue();
                    blueTF.setText(bluevalue+"");
                    redSB.setValue(redvalue);
                    greenSB.setValue(greenvalue);
                    blueSB.setValue(bluevalue);
                    changeBg (tempColor);
                    setHSB();
                    repaint();
               }
          }.start();
     }

     private void setHSB() {
          float[] hsb = new float[3];
          Color.RGBtoHSB(redvalue, greenvalue, bluevalue, hsb);
          hueTF.setText(hsb[0]+"");
          satTF.setText(hsb[1]+"");
          brightTF.setText(hsb[2]+"");
          huevalue = hsb[0];
          satvalue = hsb[1];
          brightvalue = hsb[2];
          setHSBbars();
     }

     private void setRGB() {
          new Thread() {
               public void run() {
                    int colint = Color.HSBtoRGB(huevalue, satvalue, brightvalue);
                    Color c = new Color(colint);
                    redvalue = c.getRed();
                    redTF.setText(redvalue+"");
                    redSB.setValue(redvalue);
                    greenvalue = c.getGreen();
                    greenTF.setText(greenvalue+"");
                    greenSB.setValue(greenvalue);
                    bluevalue = c.getBlue();
                    blueTF.setText(bluevalue+"");
                    blueSB.setValue(bluevalue);
                    changeBg (new Color(redvalue, greenvalue, bluevalue));
               }
          }.start();
     }

     private void setHSBbars() {
          hueSB.setValue((int)(huevalue*100));
          satSB.setValue((int)(satvalue*100));
          brightSB.setValue((int)(brightvalue*100));
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

     public void paint (Graphics g) {
          g.drawRect(0,0,getSize().width-1,getSize().height-1);
          Color c = Color.white;

          if (fontMethod.equals("auto")) {
               if (brightvalue < .5) 
                    c = Color.white;
               else 
                    c = Color.black;
          }
          else if (fontMethod.equals("black"))
               c = Color.black;
          else if (fontMethod.equals("white"))
               c = Color.white;

          lab1.setForeground(c);
          lab2.setForeground(c);
          lab3.setForeground(c);
          lab4.setForeground(c);
          lab5.setForeground(c);
          lab6.setForeground(c);
          lab7.setForeground(c);
          g.setColor(c);
/*
          g.drawString("0x"+Integer.toHexString(redvalue).toUpperCase(),
                       redSB.getLocation().x, 
                       redSB.getLocation().y + redSB.getSize().height + 15);
          g.drawString("0x"+Integer.toHexString(greenvalue).toUpperCase(),
                       greenSB.getLocation().x, 
                       greenSB.getLocation().y + greenSB.getSize().height + 15);
          g.drawString("0x"+Integer.toHexString(bluevalue).toUpperCase(),
                       blueSB.getLocation().x, 
                       blueSB.getLocation().y + blueSB.getSize().height + 15);
*/
          g.drawString(toHex(redvalue),
                       redSB.getLocation().x, 
                       redSB.getLocation().y + redSB.getSize().height + 15);
          g.drawString(toHex(greenvalue),
                       greenSB.getLocation().x, 
                       greenSB.getLocation().y + greenSB.getSize().height + 15);
          g.drawString(toHex(bluevalue),
                       blueSB.getLocation().x, 
                       blueSB.getLocation().y + blueSB.getSize().height + 15);
     }

     public static String toHex (int n) {
          String s = Integer.toHexString(n).toUpperCase();
          if (s.length() == 1)
               s = "0" + s.charAt(0);
          return "0x" + s;
     }
}
