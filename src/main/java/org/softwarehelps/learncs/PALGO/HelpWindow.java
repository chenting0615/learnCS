package org.softwarehelps.learncs.PALGO;

import java.awt.*;
import java.awt.event.*;

class HelpWindow extends Frame {
     TextArea ta;
     
     public HelpWindow () {
          super ("Palgo Help Window");
          ta = new TextArea(20,45);
          ta.setText(helptext());
          ta.setBackground (Color.pink);
          add(ta);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                    }
               }    
          );

          setSize(400,400);
          setLocation(100,100);
          setVisible(true);
     }

     public String helptext() {
          return "COMMANDS YOU CAN USE...(examples)\n"+
                 "    var = expression     (assign an expression's value to a variable)\n" +
                 "    var = input          (input a string)\n"+
                 "    var = input_number   (input a number)\n"+
                 "    var = array          (create a new array)\n"+
                 "    var = table          (create a new table)\n"+
                 "    print expression\n"+
                 "    pen down\n" +
                 "    pen up\n" +
                 "    color \"red\"\n" +
                 "    color 158 207 96\n" +
                 "    goto 5 6\n" +
                 "    down 8\n" +
                 "    up 8\n" +
                 "    left 8\n" +
                 "    right 8\n" +
                 "    draw\n" +
                 "    wait 500\n" +
                 "    numcells 40\n" +
                 "\n"+
                 "STRUCTURES YOU CAN USE...\n" +
                 "    repeat 20 times \n"+
                 "        ...\n"+
                 "    end\n\n" +
                 "    while i < 10\n"+
                 "        ...\n"+
                 "    end\n\n" +
                 "    if n < 5 then\n"+
                 "        ...\n"+
                 "    end\n\n" +
                 "    if n < 5 then\n"+
                 "        ...\n"+
                 "    else\n"+
                 "        ...\n"+
                 "    end\n\n" +
                 "    for i = 0 to 10\n"+
                 "        ...\n"+
                 "    end\n\n"+
                 "    define square (n)\n"+
                 "        ...\n"+
                 "    end\n";
     }
}
