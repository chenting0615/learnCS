import java.util.*;

public class StructList {
     private Vector datacols;             // DataObj's in here
     private Vector fieldnames;           // Strings in here
     private Vector fieldtypes;           // Strings in here

     public StructList() {
          datacols = new Vector();
          fieldnames = new Vector();
          fieldtypes = new Vector();
     }

     public StructList(Vector v, Vector f1, Vector f2) {
          datacols = v;
          fieldnames = f1;
          fieldtypes = f2;
     }

     public StructList copy() {
          Vector xdatacols = new Vector();
          Vector xfieldnames = new Vector();
          Vector xfieldtypes = new Vector();

          for (int i=0; i<datacols.size(); i++) {
               DataObj dobj = (DataObj) datacols.elementAt(i);
               xdatacols.addElement(dobj.copy());
          }

          for (int i=0; i<fieldnames.size(); i++) {
               DataObj dobj = (DataObj) fieldnames.elementAt(i);
               xfieldnames.addElement(dobj.copy());
          }

          for (int i=0; i<fieldtypes.size(); i++) {
               DataObj dobj = (DataObj) fieldtypes.elementAt(i);
               xfieldtypes.addElement(dobj.copy());
          }

          return new StructList(xdatacols, xfieldnames, xfieldtypes);
     }

     public int getNumRows() {
          return ((DataObj)datacols.elementAt(0)).length();  
                          // hope they are all the same length!
     }

     public int getNumCols() {
          return datacols.size();
     }

     public DataObj getRow(int rownum) {
          DataObj row = DataObj.emptyList();
          for (int i=0; i<datacols.size(); i++) {
               DataObj dobj = ((DataObj)datacols.elementAt(i)).get(rownum);
               row.add(dobj.copy());
          }
          return row;
     }

     public DataObj getCol(int colnum) {
          return ((DataObj)datacols.elementAt(colnum)).copy();
     }

     public DataObj getCol(String colname) {
          int colnum = find(colname);
          if (colnum == -1)
               return null;
          else {
               if (datacols.size() < colnum) {
                    System.out.println ("ERROR!  No such column: "+colname);
                    return null;
               }
               return ((DataObj)datacols.elementAt(colnum)).copy();
          }
     }

     public DataObj getItem(int rownum, String colname) {
          int colnum;
          if (U.isint(colname))
               colnum = U.atoi(colname);
          else
               colnum = find(colname);
          if (colnum == -1)
               return null;
          else {
               if (datacols.size() < colnum) {
                    System.out.println ("ERROR!  No such column: "+colname);
                    return null;
               }
               if (((DataObj)datacols.elementAt(colnum)).length() < rownum) {
                    System.out.println ("ERROR!  No row #"+rownum);
                    return null;
               }
               return ((DataObj)datacols.elementAt(colnum)).get(rownum).copy();
          }
     }

     public DataObj getItem(int rownum, int colnum) {
          if (datacols.size() < colnum) {
               System.out.println ("ERROR!  No such column: "+colnum);
               return null;
          }
          if (((DataObj)datacols.elementAt(colnum)).length() < rownum) {
               System.out.println ("ERROR!  No row #"+rownum);
               return null;
          }
          return ((DataObj)datacols.elementAt(colnum)).get(rownum).copy();
     }

     public void addCol(String colname, String coltype) {
          int len = 0;
          if (datacols.size() > 0)
               len = ((DataObj)datacols.elementAt(0)).length();
          DataObj newcol = DataObj.emptyList();
          for (int i=0; i<len; i++)
               newcol.add(makePrototype(coltype));
          datacols.addElement(newcol);
          fieldtypes.addElement(coltype);
          fieldnames.addElement(colname);
     }

     public static DataObj makePrototype (String type) {
          String newvalue = "0";
          if (type.equals("string"))
               newvalue = "";
          else if (type.equals("list"))
               newvalue = "()";
          return new DataObj(newvalue, type);
     }

     public void setItem(int rownum, String colname, DataObj newitem) {
          int colnum;
          if (U.isint(colname))
               colnum = U.atoi(colname);
          else
               colnum = find(colname);
          if (colnum == -1)
               addCol(colname, newitem.typeof());
          colnum = find(colname);
          setItem (rownum, colnum, newitem);
     }

     public void setItem(int rownum, int colnum, DataObj newitem) {
          DataObj col = (DataObj)datacols.elementAt(colnum);
          ensureRows(rownum+1);
          col.set(newitem, rownum);
     }

     public StructList union (StructList other) {
          return null;
     }

     public StructList join (StructList other) {
          return null;
     }

     public DataObj toListByRows() {
          return null;
     }

     public DataObj toListByCols() {
          return null;
     }

     public String toString() {
          String ret = "(";
          for (int i=0; i<datacols.size(); i++)
               ret += " "+((DataObj)datacols.elementAt(i)).toString2();
          ret += ")";
          return ret;
     }

     public String toTable(int maxrows, int maxcols) {
          int numrows = ((DataObj)datacols.elementAt(0)).length();
          int numcols = datacols.size();

          boolean showRowDots = false;
          boolean showColDots = false;

          if (maxrows > -1 && maxrows < numrows) {
               numrows = maxrows;
               showRowDots = true;
          }
          if (maxcols > -1 && maxcols < numcols) {
               numcols = maxcols;
               showColDots = true;
          }

          Vector colImages = new Vector();

          for (int i=0; i<numcols; i++) {
               colImages.addElement(getColValues(i, numrows));
          }

          String bigs = "";

          for (int i=0; i<numrows+2; i++) {
               String s = "";
               for (int j=0; j<numcols; j++) {
                    String onerow = (String)((Vector)colImages.elementAt(j)).elementAt(i);
                    s += onerow;
                    if (j == numcols-1 && showColDots)
                         s += "...";
               }
               bigs += s + "\n";
          }
          if (showRowDots) 
               bigs += "     ...     \n";
          return bigs;
     }

     //=================PRIVATE HELPER METHODS=======================

     private Vector getColValues(int colnum, int maxrows) {
          int maxlength = 0;

          DataObj thiscol = (DataObj)datacols.elementAt(colnum);

          for (int i=0; i<thiscol.length(); i++) {
               if (i >= maxrows) break;
               String s = thiscol.get(i).toString2();
               if (s.length() > maxlength)
                    maxlength = s.length();
          }

          int thislen = ((String)fieldnames.elementAt(colnum)).length();
          if (thislen > maxlength)
               maxlength = thislen;

          String dashes = "";
          for (int i=0; i<thislen; i++)
               dashes += "-";

          maxlength++;       // leave 1 blank afterwards

          Vector values = new Vector();
          values.addElement(padout((String)fieldnames.elementAt(colnum), maxlength));
          values.addElement(padout(dashes, maxlength));


          for (int i=0; i<thiscol.length(); i++) {
               if (i >= maxrows) break;
               String s = thiscol.get(i).toString2();
               if (s.length() > maxlength)
                    maxlength = s.length();
               s = padout(s, maxlength);
               values.addElement(s);
          }

          return values;
     }

     private String padout (String s, int len) {
          while (s.length() < len)
               s += "                                                   ";
          return s.substring(0,len);
     }

     private int find (String colname) {
          for (int i=0; i<fieldnames.size(); i++)
               if (fieldnames.elementAt(i).equals(colname))
                    return i;
          return -1;
     }

     private static void pad (DataObj col, int neededSize, String type) {
          while (col.length() < neededSize)
               col.add(makePrototype(type));
     }

     private void ensureRows (int numrows) {
          for (int i=0; i<datacols.size(); i++) {
               DataObj dobj = (DataObj)datacols.elementAt(i);
               pad(dobj, numrows, (String)fieldtypes.elementAt(i));
          }
     }

     private void debug1 () {
          System.out.println ("Size of datacols = "+datacols.size());
          System.out.println ("fieldnames = "+fieldnames.toString());
          System.out.println ("fieldtypes = "+fieldtypes.toString());
     }
}
