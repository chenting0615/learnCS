package org.softwarehelps.learncs.PLOTTER;

import java.awt.*;
import java.awt.event.*;

class Popup extends Frame {
     TextArea ta;
     
     public Popup (String message) {
          this (message, 150, 150, 250, 250, Color.pink);
     }

     public Popup (String message, Color c) {
          this (message, 150, 150, 250, 250, c);
     }

     public Popup (String message, int width, int height) {
          this (message, 150, 150, width, height, Color.white);
     }

     public Popup (String message, int width, int height, Color c) {
          this (message, 150, 150, width, height, c);
     }

     public Popup (String message, int x, int y, int width, int height, Color c)
     {
          super("Super Simple CPU Message");
          ta = new TextArea(3,40);
          ta.setText(message);
          ta.setBackground (c);
          ta.setEditable(false);
          add(ta);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                    }
               }    
          );

          setSize(width, height);
          setLocation(x, y);
          setVisible(true);
     }

     public void add (String text) {
          ta.setText(ta.getText()+text);
     }

     public void setFont (Font font) {
          ta.setFont(font);
     }
}
