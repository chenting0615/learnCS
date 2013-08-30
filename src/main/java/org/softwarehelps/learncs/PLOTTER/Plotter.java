import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;
import java.text.*;
import java.util.*;

public class Plotter extends Applet 
        implements ActionListener, ComponentListener, ItemListener
{
     boolean debug = true;

     Label label1, label2;
     TextField minxTF, maxxTF, minyTF, maxyTF, numpointsTF,
               userformulaTF;
     Choice actionCH, formulaCH;
     String list = "";         // list of functions to paint

     Formula form;      // later we need an array of these
     String errorString = "";  // error message from evaluator
                               // that we paint on the applet

     boolean hidingControls = false;
     boolean hidingAxisboxes = false;
     boolean hidingAxes = false;

     Image buffer;
     Graphics gg;

     int width = 500;        // size of the Java window
     int height = 500;
     int halfWidth = width/2;
     int halfHeight = height/2;

     double minx = -8;     // dimensions of the axes for plotting
     double maxx = 8;
     double miny = -8;
     double maxy = 8;

     NumberFormat nf;

     public void init() {
          nf = NumberFormat.getNumberInstance();
          nf.setGroupingUsed(false);
          nf.setMaximumFractionDigits(1);
          nf.setMinimumFractionDigits(1);

          setLayout(null);

          int x = 3;
          int y = 3;

          actionCH = new Choice();
          actionCH.addItem("--Action menu--");
          actionCH.setBackground(Color.pink);
          actionCH.addItemListener(this);
          actionCH.setBounds(x, y, 150, 25);
          add(actionCH);

          actionCH.addItem("Clear");
          actionCH.addItem("Help");
          actionCH.addItem("Hide/show Controls");
          actionCH.addItem("Hide/show Axis boxes");
          actionCH.addItem("Hide/show Axes");
          actionCH.addItem("Reset to defaults");

          y += 26;

          formulaCH = new Choice();
          formulaCH.addItem("--Choose function--");
          formulaCH.setBackground(Color.pink);
          formulaCH.addItemListener(this);
          formulaCH.setBounds(x, y, 200, 25);
          add(formulaCH);

          formulaCH.addItem("x  |  a diagonal line (y=x)");
          formulaCH.addItem("1/x  |  x to the minus 1 power");
          formulaCH.addItem("2x");
          formulaCH.addItem("100x");
          formulaCH.addItem("log2(x) | logarithm base 2");
          formulaCH.addItem("log10(x) | logarithm base 10");
          formulaCH.addItem("ln x | natural logarithm (base e)");
          formulaCH.addItem("x ln x | x times logarithm of x");
          formulaCH.addItem("x^2 | x squared");
          formulaCH.addItem("x^3 | x cubed");
          formulaCH.addItem("x^4");
          formulaCH.addItem("2^x | exponential");
          formulaCH.addItem("x^x | hyper-exponential");
          formulaCH.addItem("sin x | trigonometric sine");
          formulaCH.addItem("cos x | trigonometric cosine");
          formulaCH.addItem("tan x | trigonometric tangent");
          formulaCH.addItem("Example formula 1");
          formulaCH.addItem("Example formula 2");
          formulaCH.addItem("Example formula 3");
          formulaCH.addItem("Example formula 4");

          y += 26;

          label1 = new Label("Number of plot points: ");
          label1.setBackground(Color.white);
          label1.setBounds(x, y, 125, 25);
          add(label1);

          x += label1.getWidth() + 2;

          numpointsTF = new TextField(6);
          numpointsTF.setBackground(Color.pink);
          numpointsTF.addActionListener(this);
          numpointsTF.setBounds(x, y, 50, 25);
          numpointsTF.setText("300");
          add(numpointsTF);

          x = 3;
          y += 26;

          label2 = new Label("Your formula:");
          label2.setBackground(Color.white);
          label2.setBounds(x, y, 75, 25);
          add(label2);

          x += label2.getWidth() + 2;

          userformulaTF = new TextField(25);
          userformulaTF.setBackground(Color.pink);
          userformulaTF.addActionListener(this);
          userformulaTF.setBounds(x, y, 160, 25);
          //userformulaTF.setText("5x^4-2.7x^3+x^2-4");
          add(userformulaTF);

          minxTF = new TextField(6);
          minxTF.setBackground(Color.yellow);
          minxTF.addActionListener(this);
          minxTF.setBounds(5, halfHeight+12, 50, 25);
          minxTF.setText(minx+"");
          add(minxTF);

          maxxTF = new TextField(6);
          maxxTF.setBackground(Color.yellow);
          maxxTF.addActionListener(this);
          maxxTF.setBounds(width-52, halfHeight+12, 50, 25);
          maxxTF.setText(maxx+"");
          add(maxxTF);

          minyTF = new TextField(6);
          minyTF.setBackground(Color.yellow);
          minyTF.addActionListener(this);
          minyTF.setBounds(halfWidth-50, height-25, 50, 25);
          minyTF.setText(miny+"");
          add(minyTF);

          maxyTF = new TextField(6);
          maxyTF.setBackground(Color.yellow);
          maxyTF.addActionListener(this);
          maxyTF.setBounds(halfWidth-50, 2, 50, 25);
          maxyTF.setText(maxy+"");
          add(maxyTF);

          addComponentListener(this);

          setBackground(Color.white);
          setVisible(true);
          setLocation(10,10);
          setSize(width,height);
          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();
     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == minxTF ||
              e.getSource() == maxxTF ||
              e.getSource() == minyTF ||
              e.getSource() == maxyTF) 
                setBoundaries();
          else if (e.getSource() == userformulaTF) {
               String formula = userformulaTF.getText();
               setUserFormula(formula);
          }
          repaint();
     }

     private void setUserFormula(String formula) {
          form = new Formula (formula);
          if (form.error.length() == 0) {
               addToList("#" + formula);
               errorString = "";
          }
          else
               errorString = form.error;
     }

     private void addToList (String s) {
          if (list.length() == 0)
               list = s;
          else
               list = list + ";" + s;
     }

     public void itemStateChanged (ItemEvent e) {
          if (e.getSource() == actionCH) {
               String selected = actionCH.getSelectedItem();
               if (selected.equals("Clear"))
                    list = "";
               else if (selected.equals("Help"))
                    help();
               else if (selected.equals("Hide/show Controls"))
                    hidingControls = !hidingControls;
               else if (selected.equals("Hide/show Axis boxes"))
                    hidingAxisboxes = !hidingAxisboxes;
               else if (selected.equals("Hide/show Axes"))
                    hidingAxes = !hidingAxes;
               else if (selected.equals("Reset to defaults")) {
                    list = "";
                    form = null;
                    hidingAxes = false;
                    hidingControls = false;
                    hidingAxisboxes = false;
                    errorString = "";
                    numpointsTF.setText("300");
                    minxTF.setText("-8");
                    maxxTF.setText("8");
                    minyTF.setText("-8");
                    maxyTF.setText("8");
               }
               minxTF.setVisible(!hidingAxisboxes);
               maxxTF.setVisible(!hidingAxisboxes);
               minyTF.setVisible(!hidingAxisboxes);
               maxyTF.setVisible(!hidingAxisboxes);
               formulaCH.setVisible(!hidingControls);
               label1.setVisible(!hidingControls);
               numpointsTF.setVisible(!hidingControls);
               label2.setVisible(!hidingControls);
               userformulaTF.setVisible(!hidingControls);
               repaint();
          }
          else if (e.getSource() == formulaCH) {
               String selected = formulaCH.getSelectedItem();
               if (selected.equals("Example formula 1")) {
                    userformulaTF.setText("5x^4 - 2.7x^3 + x^2 - 4");
                    setUserFormula(userformulaTF.getText());
               }
               else if (selected.equals("Example formula 2")) {
                    userformulaTF.setText("x^0.5");
                    setUserFormula(userformulaTF.getText());
               }
               else if (selected.equals("Example formula 3")) {
                    userformulaTF.setText("x^(-4)");
                    setUserFormula(userformulaTF.getText());
               }
               else if (selected.equals("Example formula 4")) {
                    userformulaTF.setText("3cosx - 4cos0.5x");
                    setUserFormula(userformulaTF.getText());
               }
               else {
                    int n = selected.indexOf("|");
                    if (n > -1)
                         selected = selected.substring(0,n);
                    addToList(selected);
               }
               repaint();
          }
     }

     private void setBoundaries() {
          double newminx = U.atod(minxTF.getText());
          double newmaxx = U.atod(maxxTF.getText());
          double newminy = U.atod(minyTF.getText());
          double newmaxy = U.atod(maxyTF.getText());

          if (newminx >= newmaxx) {
               errorString = "Minimum X must be less than Maximum X.";
               return;
          }
          if (newminy >= newmaxy) {
               errorString = "Minimum Y must be less than Maximum Y.";
               return;
          }

          minx = newminx;
          maxx = newmaxx;
          miny = newminy;
          maxy = newmaxy;
     }

     public void componentResized(ComponentEvent e) {
          width = getWidth();
          height = getHeight();
          halfWidth = width/2;
          halfHeight = height/2;
     }
     public void componentHidden(ComponentEvent e) {}
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          gg.setColor(Color.white);
          gg.fillRect(0,0,buffer.getWidth(this),buffer.getHeight(this));

          gg.setColor(Color.red);
          gg.setFont(new Font("SansSerif", Font.PLAIN, 14));
          gg.drawString(errorString, 5, 140);
          gg.setColor(Color.black);

          if (!hidingAxes)
               drawAxes();

          int numplotpoints = U.atoi(numpointsTF.getText());

          String[] funcs = list.split(";");
          for (int i=0; i<funcs.length; i++) {
               String s = funcs[i].trim();
               plotFunction(gg,s, minx, maxx, numplotpoints);
               plotFunction(gg,s, 0, maxx, numplotpoints);
          }

          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     private void drawAxes() {

          double xRange = maxx-minx;
          double yRange = maxy-miny;
          double xDivider = Math.abs(minx) / (double)(maxx-minx);
          double yDivider = Math.abs(maxy) / (double)(maxy-miny);

          int xPosition = (int)(Math.abs(minx) / (double)(maxx-minx) * width);
          int yPosition = (int)(Math.abs(maxy) / (double)(maxy-miny) * height);

          gg.setColor(Color.black);
          gg.drawLine(0,yPosition,width,yPosition);
          gg.drawLine(xPosition,0,xPosition,height);

          // draw the positive X ticks

          gg.setFont(new Font("SansSerif", Font.PLAIN, 9));
          String temp;

          double xIncrement = xRange / 20;

          double xValue = 0;
          int x = xPosition;
          for (int i=0; i<10; i++) {
               gg.drawLine(x, yPosition-2, x, yPosition+2);
               temp = format(xValue);
               gg.drawString(temp, x, yPosition+10);
               xValue += xIncrement;
               x += (width - xPosition) / 10;
          }

          // draw the negative X ticks

          xValue = 0;
          x = xPosition;
          for (int i=0; i<10; i++) {
               gg.drawLine(x, yPosition-2, x, yPosition+2);
               temp = format(xValue);
               gg.drawString(temp, x, yPosition+10);
               xValue -= xIncrement;
               x -= xPosition / 10;
          }
          // there's room at the left edge...
          temp = format(xValue);
          gg.drawString(temp, x, yPosition+10);
        
          // draw the positive Y ticks
          // but not the value 0.0

          gg.setFont(new Font("SansSerif", Font.PLAIN, 9));

          double yIncrement = (maxy - miny) / 2 / 10;
          double yValue = yIncrement;
          int y = yPosition - yPosition / 10;
          for (int i=1; i<10; i++) {
               gg.drawLine(xPosition-2, y, xPosition+2, y);
               temp = format(yValue);
               gg.drawString(temp, xPosition+10, y);
               yValue += yIncrement;
               y -= yPosition / 10;
          }
        
          // draw the negative Y ticks

          yValue = -yIncrement;
          y = yPosition + yPosition / 10;
          for (int i=1; i<10; i++) {
               gg.drawLine(xPosition-2, y, xPosition+2, y);
               temp = format(yValue);
               gg.drawString(temp, xPosition+10, y);
               yValue -= yIncrement;
               y += (height - yPosition) / 10;
          }
     }


     private String format(double x) {
          return nf.format(x);
     }

     private void plotFunction (Graphics gg, String function,
                                double startx, double endx, int numIntervals) {
          if (endx <= startx) return;
          double x = startx;
          double tickWidth = (endx - startx) / numIntervals;
          gg.setColor(Color.black);
          for (int i=0; i<numIntervals; i++) {
               double fx = f(function, x);
               plot(gg, x, fx);
               x += tickWidth;
          }
     }

     private double f (String function, double x) {
          if (function.startsWith("#")) {
               return form.evaluate(x);
          }
          if (function.startsWith("log10"))
               return logg(x, 10.0);
          if (function.startsWith("log2"))
               return logg(x, 2.0);
          if (function.startsWith("ln"))
               return Math.log(x);
          if (function.startsWith("x ln"))
               return x * Math.log(x);
          if (function.equals("x"))
               return x;
          if (function.equals("2x"))
               return 2*x;
          if (function.equals("2^x"))
               return Math.pow(2.0, x);
          if (function.equals("1/x"))
               return 1/x;
          if (function.equals("x^2"))
               return x * x;
          if (function.equals("x^3"))
               return x * x * x;
          if (function.equals("x^4"))
               return x * x * x * x;
          if (function.equals("x^x"))
               return Math.pow(x,x);
          if (function.equals("sin x"))
               return Math.sin(x);
          if (function.equals("tan x"))
               return Math.tan(x);
          if (function.equals("cos x"))
               return Math.cos(x);
          return 0;
     }

     private double fact(double x) {
//        if ((int)x != x) return 0;
          int n = (int)x;
          int result = 1;
          for (int i=2; i<n; i++)
               result *= i;
          return (double)result;
     }

     private double logg (double x, double base) {
          return Math.log(x) / Math.log(base);
     }

     private void plot(Graphics gg, double x, double y) {
          int screenx = (int) ((x - minx) / (maxx - minx) * width);
          int screeny = height - (int) ((y - miny) / (maxy - miny) * height);
          gg.drawLine(screenx, screeny, screenx, screeny);
     }

     private void help() {
          String text = "Mathematical function plotter\n"+
                        "----------------------------------\n\n"+
          "This applet lets you see the shape of various functions.\n"+
          "Control this applet by means of the Action menu is the top\n"+
          "left corner.\n"+
          "There are a number of example functions from the Choose function\n"+
          "menu.\n"+
          "If you wish to enter your own, you may do so in the Your formula\n"+
          "area.  You can only enter polynomials (powers of x) or as \n"+
          "Fourier expansions of periodic functions (multiples of sin and\n"+
          "cos.)  Ample examples are given.\n\n"+
          "If you wish to raise x to a signed power, such as x to the -5 power,\n"+
          "you must enclose the power in parentheses, as x^(-5).\n\n"+
          "To enter your own formula, type it directly in the Your formula\n"+
          "field, and press RETURN.\n\n"+
          "To get a more accurate picture of the function, enter a larger\n"+
          "number in the field labeled Number of plot points.  A value like\n"+
          "10000 will usually give a very smooth, filled-in curve.\n\n"+
          "You can resize the axes by typing a new maximum x or y or minimum\n"+
          "x or y in any of the four yellow boxes and pressing RETURN.\n"+
          "You can also hide the boxes or the axes by selecting the appropriate\n"+
          "item from the Action menu.\n"+
          "";

          new Popup(text, 500, 425, Color.yellow);
     }
}
