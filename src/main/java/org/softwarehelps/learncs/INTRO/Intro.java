package org.softwarehelps.learncs.INTRO;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Intro extends Applet implements ActionListener
{
     Button closeB, enterB;
     Label Label1, Label2;
     TextField nameTF, majorTF;
//   TextField responseTF;
     Image buffer;
     Graphics gg;
     Color thecolor = new Color(100,240,240);
     int mywidth = 485;
     int myheight = 250;
     Choice classCH;

     boolean mustStop = true;

     String welcomeString = null;

     public void init() {
          setLayout(null);

          Font theFont = new Font("Sans Serif",Font.BOLD,14);

          int y = 50;
          int x = 20;

          int labelWidth = 70;

          Label1=new Label("Your name:", Label.LEFT);
          Label1.setBackground(thecolor);
          Label1.setBounds(20,y,labelWidth,25);
          add(Label1);

          x += Label1.getSize().width+5;

          nameTF = new TextField(50);
          nameTF.setBounds(x,y,200,25);
          nameTF.setFont(theFont);
          nameTF.addActionListener(this);
          add(nameTF);

          y += 28;
          x = 20;

          Label2=new Label("Your major:", Label.LEFT);
          Label2.setBackground(thecolor);
          Label2.setBounds(20,y,labelWidth,25);
          add(Label2);

          x += Label2.getSize().width+5;

          majorTF = new TextField(50);
          majorTF.setBounds(x,y,200,25);
          majorTF.setFont(theFont);
          majorTF.addActionListener(this);
          add(majorTF);

          y += 28;

          classCH = new Choice();
          classCH.setBounds(Label1.getLocation().x+Label1.getSize().width+5,
                            y,200,25);
          classCH.addItem("--Select one of the following--");
          classCH.addItem("First year");
          classCH.addItem("Sophomore");
          classCH.addItem("Junior");
          classCH.addItem("Senior");
          classCH.addItem("Graduate");
          add(classCH);

/*
          responseTF = new TextField(100);
          responseTF.setBounds(20,90,mywidth-45,25);
          responseTF.setFont(theFont);
          add(responseTF);
*/

          enterB = new Button("Submit Information");
          enterB.setBounds(nameTF.getLocation().x+nameTF.getSize().width+5,
                           50,130,25);
          enterB.addActionListener(this);
          add(enterB);


          setBackground(thecolor);
/*if nonapplet
          setVisible(true);
          setSize(mywidth, myheight);
endif*/
          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();
          repaint();

     }

     public void actionPerformed(ActionEvent e)
     {
          if (e.getSource() == enterB || e.getSource() == nameTF) 
               respond();
     }

     Font bigfont = new Font("Serif",Font.BOLD,72);
     int x = 10;

     public void paint(Graphics g)
     {
          if (buffer == null || gg == null) return;
          gg.setColor(thecolor);
          gg.fillRect(0, 0, buffer.getWidth(this), buffer.getHeight(this));

          if (isError) {
               gg.setColor(Color.red);
               gg.setFont(new Font("Serif",Font.ITALIC,36));
               gg.drawString("Please fill in all information!",x,200);
               mustStop = true;
          }
          else {
               gg.setColor(Color.blue);
               gg.setFont(bigfont);
               if (welcomeString != null) {
                    FontMetrics fm = gg.getFontMetrics();
                    int width = fm.stringWidth(welcomeString);
                    gg.drawString(welcomeString,x,200);
                    x -= 10;
                    if (x < -width)
                         x = this.getSize().width-10;
               }
          }
          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     boolean isError = false;
     String classyear;

     public void respond() {
/*
          String[] tackons = {"How are you today?",
                              "That's a nice name.",
                              "Have fun studying Computer Science.",
                              "Aren't computers something else?"};
          int selection = (int)(new Random().nextFloat()*tackons.length);

          responseTF.setText("Hello, "+nameTF.getText()+ ".  "+
                tackons[selection]);
*/

          classyear = classCH.getSelectedItem().trim();

          if (classyear.equals("--Select one of the following--") ||
              nameTF.getText().trim().length() == 0 ||
              majorTF.getText().trim().length() == 0) {
               isError = true;
               repaint();
               return;
          }

          isError = false;

          mustStop = false;

          new Thread() {
               public void run() {
                    welcomeString = "Welcome, "+nameTF.getText() + "!   " +
                                    "Major: " + majorTF.getText() + "; " +
                                    "Year: " + classyear + "  ";
                    while(!mustStop) {
                         repaint();
                         try {
                              Thread.sleep(40);
                         }
                         catch (InterruptedException e) {}
                    }
               }
          }.start();
     }

     public static void main(String[] args)
     {
          new Intro();
     }

}
