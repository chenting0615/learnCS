package org.softwarehelps.learncs.REALS;

import java.awt.*;
import java.awt.event.*;

class Popup extends Frame {
     TextArea ta;
     
     public Popup (String message) {
          this (message, 200, 150, 250, 300);
     }

     public Popup (String message, int width, int height) {
          this (message, 200, 150, width, height);
     }

     public Popup (String message, int x, int y, int width, int height) {
          super("Real numbers error messages");
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
