public class Table {
     public String name;
     public String[] fieldnames;
     public String[] types;
     public String[] contents;
     int numfields;
     int numrows;

     final static int MAXFIELDS = 8;

     public Table() {
          name = "newtable";
          fieldnames = new String[MAXFIELDS];
          types = new String[MAXFIELDS];
          contents = new String[MAXFIELDS];
          numfields = 0;
          numrows = 0;
     }

     boolean[] saved;
     int[] selected;
     final int NOT_SELECTED = 0;
     final int REJECTED = -1;
     final int ACCEPTED = 1;

     public void startSaved() {
          saved = new boolean[numrows];
          selected = new int[numrows];
          for (int i=0; i<numrows; i++) {
               selected[i] = NOT_SELECTED;
               saved[i] = false;
          }
     }

     public void endSaved() {
          for (int i=0; i<numrows; i++) 
               saved[i] = (selected[i] == ACCEPTED);
     }

     // If logicalOp is "and" then saved[i] is set to the value of the
     // comparison.  If logicalOp is "or" then saved[i] is or'ed with
     // the current value of saved[i].

     public void saveRows (String fieldname, String op, String other,
                           String logicalOp) 
     {
          int fieldindex = find(fieldname);
          if (fieldindex == -1) return;


          for (int i=0; i<numrows; i++) {
               String tested = U.getLine(contents[fieldindex],i);
               boolean keep = false;
               if (other.charAt(0) == '\'') 
                    keep = compareStringLiteral (tested, op, other);

               else if (U.isInt(U.removeChar(other,',')))
                    keep = compareInteger (tested, op, other);

               else 
                    keep = compareFieldValues (tested, op, other, i);

               if (logicalOp.equals("and")) {
                    if (selected[i] == NOT_SELECTED) {
                         if (keep)
                              selected[i] = ACCEPTED;
                         else
                              selected[i] = REJECTED;
                    }
                    else {
                         if (keep && selected[i] == ACCEPTED)
                              selected[i] = ACCEPTED;
                         else
                              selected[i] = REJECTED;
                    }
               }
               else {       // logicalOp.equals("or")
                    if (selected[i] == NOT_SELECTED) {
                         if (keep)
                              selected[i] = ACCEPTED;
                         else
                              selected[i] = REJECTED;
                    }
                    else {
                         if (keep || selected[i] == ACCEPTED)
                              selected[i] = ACCEPTED;
                         else
                              selected[i] = REJECTED;
                    }
               }

          }
     }

     public void compressRows () {
          int newnumrows = 0;
          int count = 0;
          for (int k=0; k<numfields; k++) {
               String s = "";
               for (int i=0; i<numrows; i++) {
                    if (saved[i]) {
                         count++;
                         if (s.length() == 0)
                              s = U.getLine(contents[k], i);
                         else
                              s = s + "\n" + U.getLine(contents[k], i);
                    }
               }
               contents[k] = s;
               if (k == 0)
                    newnumrows = count;
          }
          numrows = newnumrows;
     }

     public void markRows (String fieldname, String op, String other) {
          int fieldindex = find(fieldname);
          if (fieldindex == -1) return;
          
          for (int i=0; i<numrows; i++) {
               String tested = U.getLine(contents[fieldindex],i);
               if (other.charAt(0) == '\'') 
                    saved[i] = compareStringLiteral (tested, op, other);

               else if (U.isInt(U.removeChar(other,','))) 
                    saved[i] = compareInteger (tested, op, other);

               else 
                    saved[i] = compareFieldValues (tested, op, other, i);

          }
     }

     private boolean compareStringLiteral (String tested, String op, 
                                           String other) {

          // First strip off the single quotes if they are present

          if (other.charAt(0) == '\'') {
               other = other.substring(1);
               if (other.length() == 0)
                    return tested.length() == 0;

               if (other.charAt(other.length()-1) == '\'')
                    other = other.substring(0,other.length()-1);
          }

          if (op.equals("="))
               return tested.equals(other);
          else if (op.equals("!="))
               return !(tested.equals(other));
          else if (op.equals("<"))
               return tested.compareTo(other) < 0;
          else if (op.equals("<="))
               return tested.compareTo(other) <= 0;
          else if (op.equals(">"))
               return tested.compareTo(other) > 0;
          else if (op.equals(">="))
               return tested.compareTo(other) >= 0;
          else if (op.equals("like")) {
               other = U.removeChar(other, '%');
               return tested.indexOf(other) > -1;
          }

          return false;
     }

     private boolean compareInteger (String tested, String op, String other) {

          int n1 = U.atoi(U.removeChar(tested,','));
          int n2 = U.atoi(U.removeChar(other,','));

          if (op.equals("="))
               return n1 == n2;
          else if (op.equals("!="))
               return n1 != n2;
          else if (op.equals("<"))
               return n1 < n2;
          else if (op.equals("<="))
               return n1 <= n2;
          else if (op.equals(">"))
               return n1 > n2;
          else if (op.equals(">="))
               return n1 >= n2;

          return false;
     }

     private boolean compareFieldValues (String tested, String op, 
                                         String other, int rownum) {
 
          int findex = find (other);
          if (findex == -1) return false;

          String othervalue = U.getLine(contents[findex],rownum);

          if (types[findex].equals("Number"))
               return compareInteger (tested, op, othervalue);
          else
               return compareStringLiteral (tested, op, othervalue);
     }

     public void project (String[] saveColumnNames) {
          saved = new boolean[MAXFIELDS];
          for (int k=0; k<numfields; k++) {
               if (U.contains(saveColumnNames, fieldnames[k]))
                    saved[k] = true;
          }
          compressCols();
     }

     private void compressCols () {
          int newcol = 0;
          for (int k=0; k<numfields; k++) {
               if (saved[k]) {
                    fieldnames[newcol] = fieldnames[k];
                    types[newcol] = types[k];
                    contents[newcol] = contents[k];
                    newcol++;
               }
          }
          numfields = newcol;
          while (newcol < MAXFIELDS) {
               fieldnames[newcol] = "";
               types[newcol] = "";
               contents[newcol] = "";
               newcol++;
          }
     }

     public void sort (String field) {
          int findex = find(field);
          if (findex == -1) {
               new Popup("No field named: "+field);
               return;
          }
          String type = types[findex];

          for (int i=0; i<numrows-1; i++)
               for (int j=i+1; j<numrows; j++) {
                    String s1 = get(field, i);
                    String s2 = get(field, j);
                    if (type.equals("String")) {
                         if (s1.compareTo(s2) > 0) 
                              exchangeRows(i, j);
                    }
                    else if (type.equals("Number")) {
                         if (U.atoi(s1) > U.atoi(s2))
                              exchangeRows(i, j);
                    }
               }
     }

     private void exchangeRows (int r1, int r2) {
          for (int i=0; i<numfields; i++) {
               String temp1 = get(i, r1);
               String temp2 = get(i, r2);
               set (i, r1, temp2);
               set (i, r2, temp1);
          }
     }

     private int find (String fieldname) {
          for (int i=0; i<numfields; i++) 
               if (fieldnames[i].equals(fieldname))
                    return i;
          return -1;
     }

     public void deprefixFieldNames() {
          for (int i=0; i<numfields; i++) {
               String temp = fieldnames[i];
               int n = temp.indexOf('.');
               if (n > -1)
                    fieldnames[i] = temp.substring(n+1);
          }
     }

     // This makes sure that every row has the same number of
     // items by putting 0s at the end of short columns.
     // It also sets the numrows variable.

     public void normalizeRows() {
          for (int i=0; i<numfields; i++) {
               int k = U.countLines(contents[i]);
               if (numrows < k)
                    numrows = k;
          }
          for (int i=0; i<numfields; i++) {
               int k = U.countLines(contents[i]);
               while (k < numrows) {
                    contents[i] = contents[i]+"\n0";
                    k++;
               }
          }
     }

     public Table copy() {
          Table ret = new Table();
          ret.name = name;

          ret.fieldnames = new String[fieldnames.length];
          for (int i=0; i<fieldnames.length; i++)
               if (fieldnames[i] != null)
                    ret.fieldnames[i] = new String(fieldnames[i]);

          ret.types = new String[types.length];
          for (int i=0; i<types.length; i++)
               if (types[i] != null)
                    ret.types[i] = new String(types[i]);

          ret.contents = new String[contents.length];
          for (int i=0; i<contents.length; i++)
               if (contents[i] != null)
                    ret.contents[i] = new String(contents[i]);

          ret.numfields = numfields;
          ret.numrows = numrows;
          return ret;
     }

     public String get (int fieldindex, int recordnum) {
          if (fieldindex >= numfields)
               return "";
          if (recordnum >= numrows)
               return "";
          return U.getLine(contents[fieldindex], recordnum);
     }

     public String get (String fieldname, int recordnum) {
          int fieldindex = find(fieldname);
          if (fieldindex == -1)
               return "";
          if (fieldindex >= numfields)
               return "";
          if (recordnum >= numrows)
               return "";
          return U.getLine(contents[fieldindex], recordnum);
     }

     public String getWholeRecord (int recordnum) {
          String rets = "";
          for (int i=0; i<numfields; i++)
               if (i == 0)
                    rets = U.getLine(contents[i], recordnum);
               else
                    rets += "\n" + U.getLine(contents[i], recordnum);
          return rets;
     }

     public void set (int fieldindex, int recordnum, String newvalue) {
          if (fieldindex >= numfields)
               return;
          if (recordnum >= numrows)
               return;
          String newcolumn = "";
          if (recordnum == 0) {
               newcolumn = newvalue;
               for (int i=1; i<numrows; i++) {
                    String temp = U.getLine(contents[fieldindex], i);
                    if (newcolumn.length() == 0)
                         newcolumn = temp;
                    else
                         newcolumn = newcolumn + "\n" + temp;
               }
               contents[fieldindex] = newcolumn;
          }
          else {
               for (int i=0; i<recordnum; i++) {
                    String temp = U.getLine(contents[fieldindex], i);
                    if (newcolumn.length() == 0)
                         newcolumn = temp;
                    else
                         newcolumn = newcolumn + "\n" + temp;
               }
               newcolumn = newcolumn + "\n" + newvalue;
               for (int i=recordnum+1; i<numrows; i++) {
                    String temp = U.getLine(contents[fieldindex], i);
                    if (newcolumn.length() == 0)
                         newcolumn = temp;
                    else
                         newcolumn = newcolumn + "\n" + temp;
               }
               contents[fieldindex] = newcolumn;
          }
     }

     public void suppressMultipleRows() {
          saved = new boolean[numrows];
          for (int i=0; i<numrows; i++)
               saved[i] = true;
          for (int i=0; i<numrows; i++) {
               String rowi = getWholeRecord(i);
               for (int j=i+1; j<numrows; j++) {
                    String rowj = getWholeRecord(j);
                    if (rowi.equals(rowj)) {
                         saved[j] = false;
                    }
               }
          }
          compressRows();
     }

     public static Table join (Table t1, Table t2) {
          if (t1 == null || t2 == null) {
               new Popup("One or more tables do not exist.");
               return null;
          }
          Table t = new Table();
          t.numfields = t1.numfields+t2.numfields;
          t.numrows = t1.numrows * t2.numrows;
          int k = 0;
          for (int i=0; i<t1.numfields; i++) {
               t.fieldnames[k] = t1.name+"."+t1.fieldnames[i];
               t.types[k] = t1.types[i];
               k++;
          }
          for (int i=0; i<t2.numfields; i++) {
               t.fieldnames[k] = t2.name+"."+t2.fieldnames[i];
               t.types[k] = t2.types[i];
               k++;
          }
          int rownum = 0;
          for (int i=0; i<t1.numrows; i++) {
               for (int j=0; j<t2.numrows; j++) {
                    // copy all of t1's fields over
                    for (int m=0; m<t1.numfields; m++) {
                         String temp = t1.get(m, i);
                         if (rownum == 0)
                              t.contents[m] = temp;
                         else
                              t.contents[m] += "\n"+temp;
                    }
                    // copy all of t2's fields over
                    int z = t1.numfields;
                    for (int m=0; m<t2.numfields; m++) {
                         String temp = t2.get(m, j);
                         if (rownum == 0)
                              t.contents[z] = temp;
                         else
                              t.contents[z] += "\n"+temp;
                         z++;
                    }
                    rownum++;
               }
          }
          return t;
     }
}
