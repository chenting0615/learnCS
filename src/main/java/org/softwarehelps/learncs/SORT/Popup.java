package org.softwarehelps.learncs.SORT;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

class Popup extends Frame {
     TextArea ta;
     
     public Popup (String message) {
          super ("Search messages");
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

          setSize(300,150);
          setLocation(200,200);
          setVisible(true);
     }
}
