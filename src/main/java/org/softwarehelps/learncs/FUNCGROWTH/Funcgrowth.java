import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class Funcgrowth extends Applet 
        implements ActionListener, ComponentListener, TextListener
{
     Label Label1, Label2, Label3, Label4, Label5, Label6, Label7, 
           Label8, Label9;

     TextField NTF, logNTF, N2TF, TwoToNTF, NlogNTF, N100TF, NfactorialTF,
               NtoNTF;

     TextField Formula1, Result1,
               Formula2, Result2,
               Formula3, Result3;

     Button computeB, example1B, example2B, example3B, helpB;

     Image buffer;
     Graphics gg;
     Color acolor = new Color(246,227,218);

     int drawy_line;

     public void init() {
          setLayout(null);

          int y = 76;
          int ydistance = 10;

          Label lab = new Label("Comparison of Several Functions");
          lab.setFont(new Font("SansSerif", Font.BOLD, 36));
          lab.setBounds(30,5,575,45);
          lab.setBackground(acolor);
          add(lab);

          Label1=new Label("N");
          Label1.setBackground(acolor);
          Label1.setBounds(16,y,114,23);
          add(Label1);

          NTF=new TextField(40);
          NTF.addActionListener(this);
          NTF.addTextListener(this);
          NTF.setBounds(142,y,473,23);
          NTF.setText("Type a number in here and press RETURN");
          NTF.setBackground(Color.white);
          add(NTF);

          y += 23+ydistance;

          Label9=new Label("log N (truncated)");
          Label9.setBackground(acolor);
          Label9.setBounds(16,y,116,23);
          add(Label9);

          logNTF=new TextField(40);
          logNTF.addActionListener(this);
          logNTF.setBounds(142,y,473,23);
          logNTF.setBackground(Color.white);
          add(logNTF);

          y += 23+ydistance;

          Label2=new Label("N * 100");
          Label2.setBackground(acolor);
          Label2.setBounds(16,y,114,23);
          add(Label2);

          N100TF=new TextField(40);
          N100TF.addActionListener(this);
          N100TF.setBounds(142,y,473,23);
          N100TF.setBackground(Color.white);
          add(N100TF);

          y += 23+ydistance;

          Label4=new Label("N * log(N) (truncated)");
          Label4.setBackground(acolor);
          Label4.setBounds(16,y,120,23);
          add(Label4);

          NlogNTF=new TextField(40);
          NlogNTF.addActionListener(this);
          NlogNTF.setBounds(142,y,473,23);
          NlogNTF.setBackground(Color.white);
          add(NlogNTF);

          y += 23+ydistance;

          Label3=new Label("N^2");
          Label3.setBackground(acolor);
          Label3.setBounds(16,y,114,23);
          add(Label3);

          N2TF=new TextField(40);
          N2TF.addActionListener(this);
          N2TF.setBounds(142,y,473,23);
          N2TF.setBackground(Color.white);
          add(N2TF);

          y += 23+ydistance;

          Label5=new Label("2^N");
          Label5.setBackground(acolor);
          Label5.setBounds(16,y,114,23);
          add(Label5);

          TwoToNTF=new TextField(40);
          TwoToNTF.addActionListener(this);
          TwoToNTF.setBounds(142,y,473,23);
          TwoToNTF.setBackground(Color.white);
          add(TwoToNTF);

          y += 23+ydistance;

          Label6=new Label("N!");
          Label6.setBackground(acolor);
          Label6.setBounds(16,y,114,23);
          add(Label6);

          NfactorialTF=new TextField(40);
          NfactorialTF.addActionListener(this);
          NfactorialTF.setBounds(142,y,473,23);
          NfactorialTF.setBackground(Color.white);
          add(NfactorialTF);

          y += 23+ydistance;

          Label7=new Label("N^N");
          Label7.setBackground(acolor);
          Label7.setBounds(16,y,114,23);
          add(Label7);

          NtoNTF=new TextField(40);
          NtoNTF.addActionListener(this);
          NtoNTF.setBounds(142,y,473,23);
          NtoNTF.setBackground(Color.white);
          add(NtoNTF);

          y += 30+ydistance;
          drawy_line = y;
          y += 5;

          int x = 16;
          Label1=new Label("f(N):");
          Label1.setBackground(acolor);
          Label1.setBounds(x,y,25,23);
          add(Label1);
          x += Label1.getSize().width+5;

          Formula1 = new TextField(20);
          Formula1.setBounds(x,y,200,23);
          Formula1.setBackground(Color.white);
          add(Formula1);
          x += Formula1.getSize().width+5;

          Label1=new Label("=");
          Label1.setBackground(acolor);
          Label1.setBounds(x,y,10,23);
          add(Label1);
          x += Label1.getSize().width+5;

          Result1 = new TextField(20);
          Result1.setBounds(x,y,200,23);
          Result1.setBackground(Color.white);
          add(Result1);

          y += Result1.getSize().height + 5;

          x = 16;
          Label1=new Label("f(N):");
          Label1.setBackground(acolor);
          Label1.setBounds(x,y,25,23);
          add(Label1);
          x += Label1.getSize().width+5;

          Formula2 = new TextField(20);
          Formula2.setBounds(x,y,200,23);
          Formula2.setBackground(Color.white);
          add(Formula2);
          x += Formula2.getSize().width+5;

          Label1=new Label("=");
          Label1.setBackground(acolor);
          Label1.setBounds(x,y,10,23);
          add(Label1);
          x += Label1.getSize().width+5;

          Result2 = new TextField(20);
          Result2.setBounds(x,y,200,23);
          Result2.setBackground(Color.white);
          add(Result2);

          y += Result2.getSize().height + 5;

          x = 16;
          Label1=new Label("f(N):");
          Label1.setBackground(acolor);
          Label1.setBounds(x,y,25,23);
          add(Label1);
          x += Label1.getSize().width+5;

          Formula3 = new TextField(20);
          Formula3.setBounds(x,y,200,23);
          Formula3.setBackground(Color.white);
          add(Formula3);
          x += Formula3.getSize().width+5;

          Label1=new Label("=");
          Label1.setBackground(acolor);
          Label1.setBounds(x,y,10,23);
          add(Label1);
          x += Label1.getSize().width+5;

          Result3 = new TextField(20);
          Result3.setBounds(x,y,200,23);
          Result3.setBackground(Color.white);
          add(Result3);

          y += Result2.getSize().height + 5;

          int tempx = 100;
          computeB = new Button("Compute");
          computeB.addActionListener(this);
          computeB.setBounds(tempx,y,75,25);
          add(computeB);
          tempx += 5 + computeB.getSize().width;

          example1B = new Button("Example 1");
          example1B.addActionListener(this);
          example1B.setBounds(tempx,y,75,25);
          add(example1B);
          tempx += 5 + example1B.getSize().width;

          example2B = new Button("Example 2");
          example2B.addActionListener(this);
          example2B.setBounds(tempx,y,75,25);
          add(example2B);
          tempx += 5 + example2B.getSize().width;

          example3B = new Button("Example 3");
          example3B.addActionListener(this);
          example3B.setBounds(tempx,y,75,25);
          add(example3B);
          tempx += 5 + example3B.getSize().width;

          helpB = new Button("What is a function?");
          helpB.addActionListener(this);
          helpB.setBounds(tempx,y,175,25);
          add(helpB);
          tempx += 5 + helpB.getSize().width;

          y += 23+ydistance;

          NTF.requestFocus();

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);
          setSize(650,500);

          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == NTF || e.getSource() == computeB)
               setValues();
          else if (e.getSource() == example1B) 
               demo1();
          else if (e.getSource() == example2B) 
               demo2();
          else if (e.getSource() == example3B) 
               demo3();
          else if (e.getSource() == helpB) 
               help();
     }

     public void textValueChanged(TextEvent e) {
          NTF.removeTextListener(this);
          NTF.setText("");
     }

     public void componentResized(ComponentEvent e) {}
     public void componentHidden(ComponentEvent e) {}
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          g.setColor(acolor);
          g.fillRect(0,0,buffer.getWidth(this),buffer.getHeight(this));
          gg.drawLine(0,drawy_line,buffer.getWidth(this),drawy_line);
          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     private void setValues() {
          boolean toobig = false;

          long n = U.atol(NTF.getText());
          NTF.setText(""+n);

          long logn = U.round(Math.log(n) / Math.log(10));
          logNTF.setText(""+logn);

          long n_squared;
          long n_times_100;

          if ((double)n*(double)n > Long.MAX_VALUE) 
               N2TF.setText("too big!");
          else {
               n_squared = n * n;
               N2TF.setText(""+n_squared);
          }

          if ((double)n*100 > Long.MAX_VALUE) 
               N100TF.setText("too big!");
          else {
               n_times_100 = n * 100;
               N100TF.setText(""+n_times_100);
          }

          double nlognD = n * (long)(Math.log(n) / Math.log(2));
          long nlognL = (long)nlognD;
          NlogNTF.setText(""+nlognL);
/*
          String s = nlogn+"";
          int m = s.indexOf(".");
          if (m > -1)
               s = s.substring(0,m+2);
          NlogNTF.setText(""+s);
*/


          //-----------------------------------------------
          double twopowernD = 1;
          long twopowernL = 1;
          toobig = false;

          for (int i=1; i<=n; i++) {
               twopowernD *= 2;
               if (twopowernD > Long.MAX_VALUE) {
                    TwoToNTF.setText("too big!");
                    toobig = true;
                    break;
               }
               twopowernL *= 2;
          }

          if (!toobig) 
               TwoToNTF.setText(""+twopowernL);

          //-----------------------------------------------
          double nfactD = 1;
          long nfactL = 1;
          toobig = false;

          for (int i=1; i<=n; i++) {
               nfactD *= i;
               if (nfactD > Long.MAX_VALUE) {
                    NfactorialTF.setText("too big!");
                    toobig = true;
                    break;
               }
               nfactL *= i;
          }

          if (!toobig) 
               NfactorialTF.setText(""+nfactL);

          //-----------------------------------------------
          double nnD = 1;
          long nnL = 1;
          toobig = false;

          for (int i=1; i<=n; i++) {
               nnD *= n;
               if (nnD > Long.MAX_VALUE) {
                    NtoNTF.setText("too big!");
                    toobig = true;
                    break;
               }
               nnL *= n;
          }

          if (!toobig) 
               NtoNTF.setText(""+nnL);

          //-----------------------------------------------


          NameDB ndb = new NameDB(this);

          ndb.set ("N", new Token(Token.REAL, U.atoi(NTF.getText())));

          Token token;
          String formula = Formula1.getText();
          if (formula.length() > 0) {
               token = new TokenList(formula, ndb).evaluate();
               String s = prettify(token.doubleval);
               Result1.setText(s);
          }

          formula = Formula2.getText();
          if (formula.length() > 0) {
               token = new TokenList(formula, ndb).evaluate();
               String s = prettify(token.doubleval);
               Result2.setText(s);
          }

          formula = Formula3.getText();
          if (formula.length() > 0) {
               token = new TokenList(formula, ndb).evaluate();
               String s = prettify(token.doubleval);
               Result3.setText(s);
          }

          //-----------------------------------------------

          int k = NTF.getText().length();
          NTF.select(k,k);
     }

     private static String prettify (double x) {
          if (x < 1.0e18) {
               long n = (long)x;
               if ((double)n == x) {
                    return n+"";
               }
               else {
                    double fraction = x - n;
                    String s = fraction+"";
                    s = s.substring(1);      // remove the leading 0
                    int k = s.indexOf(".");
                    if (s.length() > k+4)
                         s = s.substring(0,k+4);
                    return n+""+s;
               }
          }
          else
               return x+"";
     }

     private void demo1() {
          new Thread() {
               public void run() {
                    for (int i=1; i<80; i++) {
                         NTF.setText(""+i);
                         setValues();
                         repaint();
                         U.sleep(200);
                    }
               }
          }.start();
     }

     private void demo2() {
          new Thread() {
               public void run() {
                    long n = 10;
                    for (int i=1; i<20; i++) {
                         NTF.setText(""+n);
                         setValues();
                         repaint();
                         n *= 10;
                         U.sleep(200);
                    }
               }
          }.start();
     }

     private void demo3() {
          Formula1.setText("1000000.0");
          Formula2.setText("500*(N+5)");
          Formula3.setText("N*N/100");
          NTF.setText("2000");
          new Popup("Click on the compute button\n"+
                    "to see the values of these functions.\n"+
                    "Change N to another value and click\n"+
                    "the compute button again.\n");
     }

     private void help() {
          new Popup("            What is a function?\n\n"+
                    "A function is a rule for producing an output value\n"+
                    "given an input value.  Functions always give a unique\n"+
                    "output value for a specific input value.\n"+
                    "\n"+
                    "For example, log2(N) is a function that computes the\n"+
                    "base 2 logarithm of the input.  N is the input.\n"+
                    "Suppose N is 32.  Then the output is 5 because 5 is\n"+
                    "the base 2 logarithm of 32.  We write\n"+
                    "      log2(32) = 5\n"+
                    "\n"+
                    "Many functions, such as log2(N), have only one input.\n"+
                    "The input is often called the parameter or argument of\n"+
                    "the function.\n"+
                    "\n"+
                    "Some functions are written in a more mathematical\n"+
                    "notation.  For instance, N^2 is the \"square\" function.\n"+
                    "It multiplies its input by itself to produce the answer.\n"+
                    "\n"+
                    "For instance, if N=5, then N^2 is 5^2, which is 25.\n"+
                    "\n"+
                    "You may see a function that has only one input but which\n"+
                    "is used several times in the formula.  \n"+
                    "For example:\n"+
                    "       N * log2(N)\n"+
                    "\n"+
                    "which multiplies N by the base 2 logarithm of N.\n"+
                    "\n"+
                    "Suppose N is 32.  log2(32) is 5, and 32 * 5 gives 160.\n"+
                    "Thus we would write\n"+
                    "\n"+
                    "     32 * log2(32) = 160\n"+
                    "\n"+
                    "Notice that all we do is substitute the actual input for\n"+
                    "the symbolic input, represented by N in all these cases.\n"+
                    "\n"+
                    "\n", 150, 30, 350, 600);
     }
}
