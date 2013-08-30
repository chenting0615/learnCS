import java.awt.*;
import java.awt.event.*;

class Popup extends Frame {
     TextArea ta;
     
     public Popup (String message) {
          this (message, 150, 150, 350, 250, true);
     }

     public Popup (String message, int width, int height) {
          this (message, 150, 150, width, height, true);
     }

     public Popup (String message, int x, int y, int width, int height,
                   boolean initiallyVisible) {
          super("Palgo messages");
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
          setVisible(initiallyVisible);
     }

     public void add (String s) {
          String olds = ta.getText();
          if (olds.length() == 0)
               ta.setText(s);
          else
               ta.setText(olds+"\n"+s);
     }
}
