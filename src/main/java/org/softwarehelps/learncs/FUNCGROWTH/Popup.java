package org.softwarehelps.learncs.FUNCGROWTH;

import java.awt.*;
import java.awt.event.*;

class Popup extends Frame {
     TextArea ta;
     
     public Popup (String message) {
          this (message, 150, 150, 250, 250);
     }

     public Popup (String message, int width, int height) {
          this (message, 150, 150, width, height);
     }

     public Popup (String message, int x, int y, int width, int height) {
          super("Comparison of functions messages");
          ta = new TextArea(3,40);
          ta.setText(message);
          ta.setBackground (Color.yellow);
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
}
