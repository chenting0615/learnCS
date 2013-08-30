import java.awt.*;
import java.awt.event.*;

class Input extends Frame implements ActionListener {
     TextArea ta;
     boolean iamready;
     Button okB, cancelB;
     
     public Input (String prompt) {
          this (prompt, 150, 150, 250, 250, Color.green);
     }

     public Input (String prompt, int x, int y, int width, int height, Color c) {
          setLayout(new FlowLayout(FlowLayout.LEFT,5,5));

          iamready = false;

          Label lab = new Label(prompt);
          add(lab);

          ta = new TextArea(3,40);
          ta.setBackground (Color.white);
          add(ta);

          okB = new Button("OK");
          okB.addActionListener(this);
          add(okB);

          cancelB = new Button("Cancel");
          cancelB.addActionListener(this);
          add(cancelB);

/*
          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                    }
               }    
          );
*/

          setSize(width, height);
          setLocation(x, y);
          setVisible(true);

          ta.requestFocus();
     }

     public void actionPerformed (ActionEvent e) {
          if (e.getSource() == okB) {
               iamready = true;
          }
          else if (e.getSource() == cancelB) {
               iamready = false;
          }
     }

     public boolean ready() {
          return iamready;
     }

     public String get () {
          String s = ta.getText();
          dispose();
          return s;
     }
}
