package org.softwarehelps.learncs.NUMSYS;

import java.awt.*;
import java.awt.event.*;

class Explain extends Frame {
     TextArea ta;
     
     public Explain () {
          super("Base conversions messages");

          int x = 400;
          int y = 100;
          int width = 300;
          int height = 400;

          Color acolor = new Color(246,227,218);

          ta = new TextArea(12,50);
          ta.setBackground (acolor);
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

     public void add (String s) {
          ta.setText(ta.getText()+s);
     }
}
