package org.softwarehelps.learncs.SCHEDULING;

import java.awt.*;
import java.awt.event.*;

class Popup extends Frame {
     TextArea ta;
     
     public Popup (String message) {
          super("Scheduling of Jobs messages");
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
