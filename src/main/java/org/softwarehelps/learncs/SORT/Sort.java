import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;

public class Sort extends Applet implements ActionListener, ItemListener
{
     TextArea input, output;
     Choice sortTypeChoice, exampleCH;
     Button sortB;
     Color bg = new Color(134,147,230);
     int count;              // count of tries (used for binary search)
     CellArray ca;
     int[] data;

     final static int SLEEPTIME = 450;

     Image buffer;
     Graphics gg;

     public void init() {
          setLayout (null);

          buffer = createImage(getSize().width, getSize().height);
          gg = buffer.getGraphics();

          ca = new CellArray(16, 23, 70, 15, 50);

          Label lab = new Label("Sorting");
          lab.setBounds(180,5,380,38);
          lab.setFont (new Font("SansSerif", Font.BOLD, 36));
          lab.setBackground(bg);
          add(lab);

          int y = 10 + lab.getSize().height;

          input = new TextArea(6,15);
          input.setBounds(120,y,300,200);
          input.setBackground(Color.white);
          input.setText("Your data set goes here.\n"+
                        "It is an unsorted list of numbers,\n"+
                        "one per line. (See examples.)");
          add(input);

          y += input.getSize().height + 5;

          exampleCH = new Choice();
          exampleCH.setBackground(Color.white);
          exampleCH.addItem(" -- examples -- ");
          exampleCH.addItem("Example 1 (from book)");
          exampleCH.addItem("Example 2");
          exampleCH.addItem("Example 3 (already in order)");
          exampleCH.addItem("Example 4 (in reverse order)");
          exampleCH.addItemListener(this);
          exampleCH.setBounds(150,y,200,25);
          add(exampleCH);

          y += exampleCH.getSize().height + 5;

          Label lab1 = new Label("Algorithm:");
          lab1.setBounds(120,y,200,25);
          lab1.setBackground(bg);
          add(lab1);

          y += lab1.getSize().height + 5;

          sortTypeChoice = new Choice();
          sortTypeChoice.addItem("Selection Sort");
          sortTypeChoice.addItem("Bubble Sort");
          sortTypeChoice.addItem("QuickSort");
          sortTypeChoice.setBackground(Color.white);
          add(sortTypeChoice);
          sortTypeChoice.setBounds(120,y,300,50);

          y += sortTypeChoice.getSize().height + 5;

          lab1 = new Label("Messages about the sort:");
          lab1.setBounds(120,y,200,25);
          lab1.setBackground(bg);
          add(lab1);

          y += sortTypeChoice.getSize().height + 5;

          output = new TextArea(10,20);
          output.setBounds(120,y,300,100);
          output.setBackground(Color.white);
          output.setEditable(false);
          add(output);

          y += output.getSize().height + 5;

          sortB = new Button("sort");
          sortB.addActionListener(this);
          sortB.setBounds(200,y,100,25);
          add(sortB);
     }

     public void update (Graphics g) {
          paint (g);
     }
     
     public void paint (Graphics g) {
          gg.setColor(bg);
          gg.fillRect(0, 0, getSize().width, getSize().height);
          ca.paint(gg);
          g.drawImage(buffer, 0, 0, this);
     }
     
     public void actionPerformed(ActionEvent e) {
          if (e.getSource() == sortB) 
               startSort();
     }

     public void itemStateChanged(ItemEvent e) {
          if (e.getSource() == exampleCH)
               makeExample();
     }

     public static int atoi (String s) {
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               return Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     private void startSort() {
          loadData(input.getText());
          String s = (String)sortTypeChoice.getSelectedItem();
          if (s.equals("Selection Sort")) {
               output.setText("");
               doSelection();
          }
          else if (s.equals("Bubble Sort")) {
               output.setText("");
               doBubble();
          }
          else if (s.equals("QuickSort")) {
               output.setText("");
               doQuick();
          }
     }

     private void loadData (String s) {
          StringTokenizer st = new StringTokenizer(s, "\n");
          int numtokens = st.countTokens();
          if (numtokens == 0) {
               new Popup("You must put some integers into the input area.");
               return;
          }
          if (numtokens > 16) {
               new Popup("We can only accomodate up to 16 values.");
               return;
          }
          data = new int[numtokens];
          int i=0;
          while (st.hasMoreTokens()) {
               data[i++] = atoi(st.nextToken());
          }
          ca.clear();
          for (int j=0; j<data.length; j++)
               ca.set(j, data[j]);
     }

     private void doSelection() {
          ca.unsetBgColors();
          new Thread() {
               public void run() {
                    count = 0;
                    selectionSort();
                    output.setText("This sort used "+count+" comparisons.");
               }
          }.start();
     }

     private void doBubble() {
          ca.unsetBgColors();
          new Thread() {
               public void run() {
                    count = 0;
                    bubbleSort();
                    output.setText("This sort used "+count+" comparisons.");
               }
          }.start();
     }

     private void doQuick() {
          ca.unsetBgColors();
          new Thread() {
               public void run() {
                    count = 0;
                    quickSort(0,data.length-1);
                    output.setText(output.getText()+"\n"+
                                   "This sort used "+count+" comparisons.");
                    output.setCaretPosition(output.getText().length());
               }
          }.start();
     }

     private void selectionSort() {
          for (int i=0; i<data.length-1; i++) {
               int current = data[i];
               int smallest = current;
               int indexOfSmallest = i;

               for (int j=i+1; j<data.length; j++) {
                    ca.setBgColor(j, CellArray.PINK);
                    repaint();
                    sleep(150);
                    count++;
                    if (data[j] < smallest) {
                         smallest = data[j];
                         indexOfSmallest = j;
                         ca.setBgColor(j, CellArray.RED);
                         repaint();
                         sleep(150);
                    }
                    ca.setBgColor(j, CellArray.WHITE);
               }

               int temp = data[i];
               data[i] = smallest;
               ca.set(i, smallest);
               data[indexOfSmallest] = temp;
               ca.set(indexOfSmallest, temp);
               ca.setBgColor(i, CellArray.GRAY);
               repaint();
          }
          ca.setBgColor(data.length-1, CellArray.GRAY);
          repaint();
     }

     private void bubbleSort() {
          int current = 0;
          while (true) {
               boolean swap = false;
               for (int index=data.length-1; index>=current+1; index--) {
                    count++;
                    ca.setBgColor(index, CellArray.PINK);
                    repaint();
                    sleep(300);
                    if (data[index] < data[index-1]) {
                         swap = true;
                         int temp = data[index];

                         ca.setBgColor(index, CellArray.YELLOW);
                         ca.setBgColor(index-1, CellArray.RED);
                         repaint();
                         sleep(100);

                         data[index] = data[index-1];
                         ca.set(index, data[index-1]);

                         data[index-1] = temp;
                         ca.set(index-1, temp);

                         ca.setBgColor(index, CellArray.RED);
                         ca.setBgColor(index-1, CellArray.YELLOW);
                         repaint();
                         sleep(100);
                    }
                    ca.setBgColor(index, CellArray.WHITE);
                    ca.setBgColor(index-1, CellArray.WHITE);
               }
               ca.setBgColor(current, CellArray.GRAY);
               repaint();
               sleep(300);
               current++;
               if (!swap) break;
          }
          repaint();
     }

     private void quickSort(int first, int last) {
          if (last < first) 
               return;

          output.setText(output.getText()+"\n"+
                        "starting quicksort first="+first+"  last="+last);
          if (last == first) {
               ca.setBgColor(last, CellArray.GRAY);
               repaint();
               sleep(100);
               return;
          }

          int splitVal = data[first];

          //  Split the list so that splitVal is in the center

          int left = first;
          int right = last + 1;
          do {
               do {
                    left++;
                    if (left < data.length) {
                         ca.setBgColor(left, CellArray.PINK);
                         repaint();
                         sleep(100);
                         ca.setBgColor(left, CellArray.WHITE);
                    }
                    count++;
               } while (left < data.length && data[left] < splitVal && left <= right);
               do {
                    right--;
                    ca.setBgColor(right, CellArray.PINK);
                    repaint();
                    sleep(100);
                    ca.setBgColor(right, CellArray.WHITE);
                    count++;
               } while (data[right] > splitVal && left <= right);
               if (left >= right) 
                    break;
               int temp = data[left];
               data[left] = data[right];
               ca.set(left, data[right]);
               data[right] = temp;
               ca.set(right, temp);
               repaint();
               sleep(200);
          }
          while(left <= right);

          int splitPoint = right;
          output.setText(output.getText()+"\n"+"splitpoint="+splitPoint);
          output.setCaretPosition(output.getText().length());
          ca.setBgColor(splitPoint, CellArray.GRAY);
          int temp = data[first];
          data[first] = data[right];
          ca.set(first, data[right]);
          data[right] = temp;
          ca.set(right, temp);
          ca.setBgColor(splitPoint, CellArray.GRAY);
          repaint();
          sleep(200);

          quickSort(first, splitPoint-1);
          quickSort(splitPoint+1, last);
     }

     private void sleep (long milliseconds) {
          try {
               Thread.sleep(milliseconds);
          } catch (InterruptedException ie) {}
     }

     private void makeExample() {
          String dataset = exampleCH.getSelectedItem();
          ca.clear();
          repaint();
          if (dataset.startsWith("Example 1")) {
               input.setText("9\n"+
                             "20\n"+
                             "6\n"+
                             "10\n"+
                             "14\n"+
                             "8\n"+
                             "60\n"+
                             "11\n");
          }
          else if (dataset.startsWith("Example 2")) {
               input.setText("35\n"+
                             "16\n"+
                             "21\n"+
                             "19\n"+
                             "4\n"+
                             "100\n"+
                             "219\n"+
                             "300\n"+
                             "99\n"+
                             "87\n"+
                             "999\n"+
                             "42\n"+
                             "177\n"+
                             "503\n"+
                             "62\n"+
                             "444\n");
          }
          else if (dataset.startsWith("Example 3")) {
               input.setText("1\n"+
                             "16\n"+
                             "21\n"+
                             "37\n"+
                             "43\n"+
                             "62\n"+
                             "99\n"+
                             "100\n"+
                             "126\n"+
                             "211\n"+
                             "228\n"+
                             "300\n"+
                             "308\n"+
                             "444\n"+
                             "500\n"+
                             "999\n");
          }
          else if (dataset.startsWith("Example 4")) {
               input.setText("999\n"+
                             "500\n"+
                             "444\n"+
                             "300\n"+
                             "308\n"+
                             "228\n"+
                             "211\n"+
                             "126\n"+
                             "100\n"+
                             "99\n"+
                             "62\n"+
                             "43\n"+
                             "37\n"+
                             "21\n"+
                             "16\n"+
                             "1\n");
          }
     }
}

class CellArray {
     int[] numbers;
     int numcells;       // maximum size of array
     int[] bgcolors;     // background colors for cells
     int[] fgcolors;     // foreground colors for cells
 
     int occupiedcells;  // how many actually have numbers now

     int cellheight;
     int cellwidth;
 
     int startx, starty;  // starting screen coords

     public final static int WHITE = 0;
     public final static int BLACK = 1;
     public final static int RED   = 2;
     public final static int BLUE  = 3;
     public final static int YELLOW= 4;
     public final static int ORANGE= 5;
     public final static int GREEN = 6;
     public final static int GRAY  = 7;
     public final static int PINK  = 8;

     public CellArray (int numcells, int cellheight, int cellwidth, 
                       int startx, int starty) 
     {
          this.numcells = numcells;
          numbers = new int[numcells];
          bgcolors = new int[numcells];
          fgcolors = new int[numcells];
          for (int i=0; i<numcells; i++)
               fgcolors[i] = BLACK;
          occupiedcells = 0;
          this.cellheight = cellheight;
          this.cellwidth = cellwidth;
          this.startx = startx;
          this.starty = starty;
     }

     public void paint (Graphics g) {
          paintGrid (g);
          paintBgColors (g);
          paintContents (g);
     }

     public void paintGrid (Graphics g) {
          int x = startx;
          int y = starty;

          g.setColor(Color.black);

          int numberpositionoffset = (cellheight * 3)/4;

          for (int i=0; i<numcells; i++) {
               g.drawString (i+"", x, y+numberpositionoffset);
               g.drawLine(x+20,y,x+20,y+cellheight);
               g.drawLine(x+20+cellwidth,y,x+20+cellwidth,y+cellheight);
               g.drawLine(x+20,y,x+20+cellwidth,y);
               y += cellheight;
          }
          g.drawLine (x+20, y, x+20+cellwidth, y);
     }

     public void paintBgColors (Graphics g) {
          int x = startx + 20;
          int y = starty;

          for (int i=0; i<numcells; i++) {
               g.setColor (translate(bgcolors[i]));
               g.fillRect(x+1, y+1, cellwidth-1, cellheight-1);
               y += cellheight;
          }
     }

     public void paintContents (Graphics g) {
          int x = startx + 20;
          int y = starty;

          for (int i=0; i<numcells; i++) {
               g.setColor (translate(fgcolors[i]));
               g.drawString (numbers[i]+"", x+5, y+cellheight-5);
               y += cellheight;
          }
     }

     public void set (int index, int value) {
          if (index < 0 || index >= numcells)
               return;
          numbers[index] = value;
     }

     public void setFgColor (int index, int color) {
          if (index < 0 || index >= numcells)
               return;
          fgcolors[index] = color;
     }

     public void setBgColor (int index, int color) {
          if (index < 0 || index >= numcells)
               return;
          bgcolors[index] = color;
     }

     public void unsetBgColors () {
          for (int i=0; i<numcells; i++) 
               bgcolors[i] = WHITE;
     }

     public void clear () {
          occupiedcells = 0;
          for (int i=0; i<numcells; i++)
               numbers[i] = 0;
          for (int i=0; i<numcells; i++)
               fgcolors[i] = BLACK;
          for (int i=0; i<numcells; i++)
               bgcolors[i] = WHITE;
     }

     public void add (int value) {
          if (occupiedcells < 0 || occupiedcells >= numcells)
               return;
          numbers[occupiedcells++] = value;
     }

     public Color translate (int colorvalue) {
          switch (colorvalue) {
              case WHITE:  return Color.white;
              case BLACK:  return Color.black;
              case RED:    return Color.red;
              case BLUE:   return Color.blue;
              case YELLOW: return Color.yellow;
              case ORANGE: return Color.orange;
              case GREEN:  return Color.green;
              case GRAY:   return Color.lightGray;
              case PINK:   return Color.pink;
          }
          return Color.white;
     }
}
