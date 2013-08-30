import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class Numsystems extends Applet 
        implements ActionListener, ComponentListener, TextListener
{
     Choice basechoice;
     Button convert1B, convert2B, clearB;
     TextField field1, field2;
     Label toplabel, p1, p2;
     Image buffer;
     Graphics gg;
     Color acolor = new Color(246,227,218);
     int X=0;
     int Y=0;
     Explain explain;

     public void init() {
          setLayout(null);

          toplabel=new Label("Conversions between bases");
          toplabel.setBackground(acolor);
          toplabel.setBounds(X+112,Y+20,466,28);
          toplabel.setFont(new Font("SansSerif", Font.BOLD, 24));
          add(toplabel);

          Y = 80;

          p1=new Label("Base 10:");
          p1.setBackground(acolor);
          p1.setBounds(X+76,Y,76,26);
          p1.setFont(new Font("SansSerif", Font.BOLD, 12));
          add(p1);

          field1=new TextField(40);
          field1.addActionListener(this);
          field1.addTextListener(this);
          field1.addTextListener(this);
          field1.setBounds(X+154,Y,247,26);
          field1.setBackground(Color.white);
          add(field1);

          convert1B=new Button("Convert");
          convert1B.addActionListener(this);
          convert1B.setBounds(X+420,Y,84,26);
          add(convert1B);

          Y += 60;

          p2=new Label("Base X:");
          p2.setBackground(acolor);
          p2.setBounds(X+76,Y,76,26);
          p2.setFont(new Font("SansSerif", Font.BOLD, 12));
          add(p2);

          field2=new TextField(40);
          field2.addActionListener(this);
          field2.addTextListener(this);
          field2.addTextListener(this);
          field2.setBounds(X+154,Y,247,26);
          field2.setBackground(Color.white);
          add(field2);

          convert2B=new Button("Convert");
          convert2B.addActionListener(this);
          convert2B.setBounds(X+420,Y,84,26);
          add(convert2B);

          Y += 50;

          basechoice=new Choice();
          basechoice.addItem("Binary");
          basechoice.addItem("Ternary");
          basechoice.addItem("Base 5");
          basechoice.addItem("Octal");
          basechoice.addItem("Hexadecimal (base 16)");
          basechoice.setBounds(X+170,Y,218,25);
          basechoice.setBackground(Color.white);
          add(basechoice);

          clearB=new Button("Clear");
          clearB.addActionListener(this);
          clearB.setBounds(X+420,Y,84,26);
          add(clearB);

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);
          setSize(577,260);
          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();
     }

     public void componentResized(ComponentEvent e) {}
     public void componentHidden(ComponentEvent e) {}
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}

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
          if (e.getSource() == field1 || e.getSource() == convert1B) {
               String ret;
               ret = convertFromDecimal(field1.getText(),
                              (String)basechoice.getSelectedItem());
               if (ret.startsWith("*")) {
                    new Popup (ret.substring(1));
                    field1.setBackground(Color.pink);
                    repaint();
               }
               else
                    field2.setText(ret);
          }
          else
          if (e.getSource() == field2 || e.getSource() == convert2B) {
               int base = baseNum((String)basechoice.getSelectedItem());
               if (!validate(field2.getText(), base)) {
                    new Popup ("INVALID BASE "+base+" NUMBER!");
                    field2.setBackground(Color.pink);
                    repaint();
               }
               else {
                    field1.setText(""+ convertToDecimal(field2.getText(),
                                       (String)basechoice.getSelectedItem()));
                    field1.setBackground(Color.white);
                    repaint();
               }
          }
          else
          if (e.getSource() == clearB) {
               field1.setText("");
               field2.setText("");
          }
     }

     public void textValueChanged (TextEvent te) {
          field1.setBackground(Color.white);
          field2.setBackground(Color.white);
     }

     private String convertFromDecimal (String s, String newbase) {
          int decimal = 0;
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               decimal = Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe) {
               return "*This is an invalid decimal number.\n"+
                       "It may be too large or\n"+
                       "there might be illegal characters\n"+
                       "in it like letters.\n"+
                       "The largest allowed number is 2,147,483,467\n"+
                       "which is 2^31-1, and is the largest positive\n"+
                       "integer that fits into 32 bits (using 2's\n"+
                       "complement representation.\n";
          }

          if (decimal < 0)
               return "*Only positives are allowed.";

          if (newbase.equals("Binary"))
               return convert (decimal, 2);
          else if (newbase.startsWith("Hex"))
               return convert (decimal, 16);
          else if (newbase.startsWith("Ternary"))
               return convert (decimal, 3);
          else if (newbase.startsWith("Base 5"))
               return convert (decimal, 5);
          else if (newbase.startsWith("Octal"))
               return convert (decimal, 8);
          return "*UNKNOWN BASE";
     }

     private int convertToDecimal (String s, String currentbase) {
          if (currentbase.startsWith("Hex"))
               return deconvert(s, 16);
          else if (currentbase.startsWith("Binary"))
               return deconvert(s, 2);
          else if (currentbase.startsWith("Ternary"))
               return deconvert(s, 3);
          else if (currentbase.startsWith("Base 5"))
               return deconvert(s, 5);
          else if (currentbase.startsWith("Octal"))
               return deconvert(s, 8);
          return 0;
     }

     // This one goes from base 10 to the new base in "base"

     private String convert (int n, int base) {
          String s = "";
          char divide = 247;
          explain = new Explain();
          while (n > 0) {
               int rem = n % base;
               String explanation = n + " "+ divide + " "+base+" = "+
                              (n / base) + " with a remainder of "+rem+"\n";
               explain.add(explanation);
               sleep(100);
               n = n / base;
               s = Character.forDigit(rem, base) + s;
          }

          explain.add("\nNow collect the remainders from the bottom\n");
          explain.add("up and put them together to form the \n");
          explain.add("base "+base+" number.\n");
          explain.add("\n");
          explain.add("The number is "+s+"\n");

          return s.toUpperCase();
     }

     // This one goes to base 10, given that "s" in in "base".

     private int deconvert (String s, int base) {
          int power = 1;
          int result = 0;
          int exponent = 0;
          explain = new Explain();

          explain.add("To convert "+s+" from base "+base+"\n");
          explain.add("to decimal (base 10), multiply the\n");
          explain.add("digits by powers of the base.\n");
          explain.add("\n");
          explain.add("Start with the rightmost digit and\n");
          explain.add("multiply by "+base+" to the 0th power\n");
          explain.add("(which is 1.)  Then multiply the next\n");
          explain.add("digit by "+base+" to the 1st power\n");
          explain.add("and so on.  Add up all the products.\n\n");

          while (s.length() > 0) {
               char ch = s.charAt(s.length()-1);
               int temp = Character.digit(ch, base) * power;
               result += temp;
               String explanation = ch+" x "+base+ 
                                    " raised to the power "+exponent+
                                    " ("+power+")="+temp+"\n";
               explain.add(explanation);
               s = s.substring(0,s.length()-1);
               power *= base;
               exponent++;
          }

          explain.add("\n                 the resulting sum = "+result);

          return result;
     }

     //  This returns true if the string in "s" is valid for base "base."
     //  For example, "3142" is invalid for base 3, but valid for base 5.
     //  Hexadecimal is a special case.

     private boolean validate (String s, int base) {
          if (base == 16) {
               s = s.toUpperCase();
               for (int i=0; i<s.length(); i++) {
                    char ch = s.charAt(i);
                    if (!Character.isDigit(ch)) {
                         if (ch < 'A' || ch > 'F')
                              return false;
                    }
               }
               return true;
          }
          else {
               for (int i=0; i<s.length(); i++) {
                    char ch = s.charAt(i);
                    if (!Character.isDigit(ch))
                         return false;
                    int chx = ch - '0';
                    if (chx >= base)
                         return false;
               }
               return true;
          }
     }

     private int baseNum (String baseName) {
          if (baseName.equals("Binary"))
               return 2;
          else if (baseName.equals("Ternary"))
               return 3;
          else if (baseName.equals("Base 5"))
               return 5;
          else if (baseName.equals("Octal"))
               return 8;
          else if (baseName.startsWith("Hex"))
               return 16;
          return 10;
     }

     public void sleep (int milliseconds) {
          try {
               Thread.sleep(milliseconds);
          }
          catch (InterruptedException ie) {
          }
     }
}
