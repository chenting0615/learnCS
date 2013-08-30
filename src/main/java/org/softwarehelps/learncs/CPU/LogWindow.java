import java.awt.*;
import java.awt.event.*;

class LogWindow extends Frame {
     TextArea ta;
     
     public LogWindow (String message, int x, int y, int width, int height, 
                       Color c) 
     {
          super ("Super Simple CPU log");
          ta = new TextArea(3,40);
          ta.setText(message);
          ta.setBackground (c);
          ta.setFont(new Font("Courier", Font.PLAIN, 11));
          add(ta);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         setVisible(false);
                    }
               }    
          );

          setSize(width, height);
          setLocation(x, y);
          setVisible(false);
     }

     public void add (String text) {
          if (ta.getText().length() == 0)
               ta.setText(text);
          else
               ta.setText(ta.getText()+"\n"+text);
          int n = ta.getText().length();
          ta.select(n,n);
     }

     public void addx (String text) {
          ta.setText(ta.getText()+text);
          int n = ta.getText().length();
          ta.select(n,n);
     }
}
