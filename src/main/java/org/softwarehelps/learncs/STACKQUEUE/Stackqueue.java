package org.softwarehelps.learncs.STACKQUEUE;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

public class Stackqueue extends Applet 
        implements ActionListener, ComponentListener, ItemListener
{
     Choice stackqueueCH;
     Button pushB, popB, clearB, showB, randomfillB;
     TextField pushTF, popTF;
     Label toplabel;
     Image buffer;
     Graphics gg;
     Color acolor = new Color(246,227,218);
     int X=0;
     int Y=0;
     String whichThing = "Stack";
     boolean showing = false;

     public void init() {
          setLayout(null);

          toplabel=new Label("Stacks and Queues");
          toplabel.setBackground(acolor);
          toplabel.setBounds(112,20,300,28);
          toplabel.setFont(new Font("SansSerif", Font.BOLD, 24));
          add(toplabel);

          Y = 60;

          stackqueueCH=new Choice();
          stackqueueCH.addItem("Stack");
          stackqueueCH.addItem("Queue");
          stackqueueCH.addItem("Mystery");
          stackqueueCH.setBounds(180,Y,80,25);
          stackqueueCH.setBackground(Color.white);
          stackqueueCH.addItemListener(this);
          add(stackqueueCH);

          Y += 30;
          X = 10;

          pushB=new Button("Push");
          pushB.addActionListener(this);
          pushB.setBounds(X,Y,84,26);
          add(pushB);

          X += 95;

          pushTF=new TextField(40);
          pushTF.addActionListener(this);
          pushTF.setBounds(X,Y,150,26);
          pushTF.setBackground(Color.white);
          pushTF.setFont(new Font("SansSerif", Font.BOLD, 14));
          add(pushTF);

          X = 10;
          Y += 40;

          popB=new Button("Pop");
          popB.addActionListener(this);
          popB.setBounds(X,Y,84,26);
          add(popB);

          X += 95;

          popTF=new TextField(40);
          popTF.setBounds(X,Y,150,26);
          popTF.setBackground(Color.white);
          popTF.setEditable(false);
          popTF.setFont(new Font("SansSerif", Font.BOLD, 14));
          add(popTF);

          Y += 40;
          X = 10;

          clearB=new Button("Clear");
          clearB.addActionListener(this);
          clearB.setBounds(X,Y,84,26);
          add(clearB);

          Y += 40;
          X = 10;

          randomfillB=new Button("Random Fill");
          randomfillB.addActionListener(this);
          randomfillB.setBounds(X,Y,100,26);
          add(randomfillB);

          Y += 40;
          X = 10;

          showB=new Button("Show Stack");
          showB.addActionListener(this);
          showB.setBounds(X,Y,120,26);
          add(showB);

          setBackground(acolor);
          setVisible(true);
          setLocation(10,10);
          setSize(577,350);
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
          if (showing)
               drawStack(gg);
          // Write to gg first
          g.drawImage(buffer,0,0,this);
     }

     public void update (Graphics g) {
          paint (g);
     }

     public void actionPerformed(ActionEvent e) {
          if (e.getSource() == pushB || e.getSource() == pushTF) {
               String what = pushTF.getText();
               if (what.length() == 0) return;
               push(what);
               pushTF.setText("");
          }
          else if (e.getSource() == randomfillB) {
               cursize = 0;
               Random r = new Random();
               for (int i=0; i<SIZE; i++)
                    push(""+r.nextInt(100));
          }
          else if (e.getSource() == popB) {
               String what = pop();
               popTF.setText(what);
          }
          else if (e.getSource() == clearB) {
               clear();
          }
          else if (e.getSource() == showB) {
               if (showB.getLabel().startsWith("Show")) {
                    showing = true;
                    showB.setLabel("Hide");
               }
               else {
                    showing = false;
                    if (whichThing.equals("Stack"))
                         showB.setLabel("Show Stack");
                    else
                         showB.setLabel("Show Queue");
               }
          }
          repaint();
     }

     public void itemStateChanged (ItemEvent e) {
          if (e.getSource() == stackqueueCH) {
               if (((String)stackqueueCH.getSelectedItem()).equals("Queue")) {
                    pushB.setLabel("Enqueue");
                    popB.setLabel("Dequeue");
                    showB.setLabel("Show Queue");
                    whichThing = "Queue";
               }
               else if(((String)stackqueueCH.getSelectedItem()).equals("Stack"))
               {
                    pushB.setLabel("Push");
                    popB.setLabel("Pop");
                    showB.setLabel("Show Stack");
                    whichThing = "Stack";
               }
               else { // mystery
                    pushB.setLabel("Add");
                    popB.setLabel("Remove");
                    int n = new Random().nextInt(2);
                    if (1 == n) 
                         whichThing = "Stack";
                    else
                         whichThing = "Queue";
               }
          }
          repaint();
     }

     public void sleep (int milliseconds) {
          try{Thread.sleep(milliseconds);}catch(InterruptedException ie){}
     }

     final static int SIZE = 10;
     String[] storage = new String[SIZE];
     int cursize = 0;

     private void push (String s) {
          if (full()) {
               new Popup("FULL!");
               return;
          }
          storage[cursize++] = s;
     }
     private String pop () {
          if (empty()) {
               new Popup("EMPTY!!!");
               return "";
          }
          if (whichThing.equals("Stack"))
               return storage[--cursize];
          else {
               String s = storage[0];
               for (int i=0; i<cursize-1; i++) 
                    storage[i] = storage[i+1];
               cursize--;
               return s;
          }
     }

     private boolean empty() {
          return cursize == 0;
     }

     private boolean full() {
          return cursize == SIZE;
     }

     private void clear() {
          new Thread() {
               public void run() {
                    while (!empty()) {
                         popTF.setText(pop());
                         try{Thread.sleep(200);}catch(InterruptedException ie){}
                         repaint();
                    }
                    popTF.setText("");
                    repaint();
               }
          }.start();
     }

           

     private void drawStack(Graphics gg) {
          int x = 280;
          int y = 60;
          int height = 200;
          int width = 50;
           
          gg.setColor(Color.black);
          gg.drawLine(x,y,x,y+height);
          gg.drawLine(x+width,y,x+width,y+height);

          gg.setFont(new Font("SansSerif", Font.BOLD, 14));

          if (whichThing.equals("Stack")) {
               gg.drawLine(x,y+height,x+width,y+height);
               gg.drawString("<--Top",x+width+5,y+8);
               gg.drawString("<--Bottom",x+width+5,y+height);
          }
          else {
               gg.drawString("<--Head",x+width+5,y+8);
               gg.drawString("<--Tail",x+width+5,y+height);
          }

          int xx = x + 5;
          int yy = y + 10;
          if (whichThing.equals("Queue")) 
               for (int i=0; i<cursize; i++) {
                    gg.drawString(storage[i], xx, yy);
                    yy += 20;
               }
          else 
               for (int i=cursize-1; i>=0; i--) {
                    gg.drawString(storage[i], xx, yy);
                    yy += 20;
               }
     }
}
