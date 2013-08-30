package org.softwarehelps.learncs.PALGO;

//---------------------------------------------------------------------
/**
 *  Class: DataObj
 *  Last changed: 1/30/2002 by RMM
 *
 *  This class implements the SIVIL data object.  It is also the 
 *  the interface to the SIVIL type system.  These are the valid types 
 *  right now:
 *
 *         int
 *         real
 *         string
 *         list 
 *
 *   The type "any" is used with Ports to allow objects of any type to
 *   pass through.
 *   A "list" is a one-dimensional array of DataObjects.  It is actually
 *   a heterogeneous array, not homogeneous, meaning that not all cells
 *   in the list need be of the same type.
**/
//---------------------------------------------------------------------

import java.util.*;
import java.io.*;

public class DataObj implements Serializable {
     public String type;        // "int", "real", "string", "list"

     long intvalue;              // If it is an elementary type, then
     double realvalue;          // it has to be one of these...
     String stringvalue;

     public DataObj[] cells;    // if this is a list
     public int numcells;       // the number of cells in "cells" though
                                // not all may be occupied.

     //---------------------------------------------------------------------
     /**
      *  METHOD: DataObj
      *  Last changed: 6/29/2000  by RMM
      *  
      *  A constructor that makes an empty string.
     **/
     //---------------------------------------------------------------------

     private DataObj ()  {
          type = "string";
          stringvalue = "";
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: DataObj
      *  Last changed: 6/29/2000  by RMM
      *  
      *  A constructor that makes a simple integer.
     **/
     //---------------------------------------------------------------------

     public DataObj (long n) {
          intvalue = n;
          type = "int";
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: DataObj
      *  Last changed: 6/29/2000  by RMM
      *  
      *  A constructor that makes a simple real number.
     **/
     //---------------------------------------------------------------------

     public DataObj (double r) {
          realvalue = r;
          type = "real";
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: DataObj
      *  Last changed: 6/29/2000  by RMM
      *  
      *  A constructor that makes a string.
     **/
     //---------------------------------------------------------------------

     public DataObj (String s) {
          stringvalue = s;
          type = "string";
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: isPrimitive
      *  Last changed: 3/5/2002 by RMM
      *  
      *  A static method that tells us if a given type is primitive to SIVIL.
     **/
     //---------------------------------------------------------------------

     public static boolean isPrimitive (String typename) {
          return typename.equals("int") ||
                 typename.equals("real") ||
                 typename.equals("string") ||
                 typename.equals("list") ||
                 typename.equals("any");
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: emptyList
      *  Last changed: 6/29/2000  by RMM
      *  
      *  A static method that creates a very useful constant, the
      *  empty list!
     **/
     //---------------------------------------------------------------------

     public static DataObj emptyList() {
          DataObj newone = new DataObj();
          newone.type = "list";
          newone.numcells = 0;
          return newone;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: DataObj
      *  Last changed: 8/27/2000  by RMM
      *  
      *  A constructor that makes a list from an array of integers.
     **/
     //---------------------------------------------------------------------

     public DataObj (int[] array) {
          type = "list";
          numcells = array.length;
          cells = new DataObj[numcells];
          for (int i=0; i<numcells; i++)
               cells[i] = new DataObj(array[i]);
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: DataObj
      *  Last changed: 8/27/2000  by RMM
      *  
      *  A constructor that makes a list from an array of doubles.
     **/
     //---------------------------------------------------------------------

     public DataObj (double[] array) {
          type = "list";
          numcells = array.length;
          cells = new DataObj[numcells];
          for (int i=0; i<numcells; i++)
               cells[i] = new DataObj(array[i]);
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: DataObj
      *  Last changed: 8/27/2000  by RMM
      *  
      *  A constructor that makes a list from an array of Strings.
     **/
     //---------------------------------------------------------------------

     public DataObj (String[] array) {
          type = "list";
          numcells = array.length;
          cells = new DataObj[numcells];
          for (int i=0; i<numcells; i++)
               cells[i] = new DataObj(array[i]);
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: DataObj
      *  Last changed: 8/10/2000  by RMM
      *  
      *  The most general constructor:  It takes a string form
      *  of the data value and the type, and produces the correct
      *  internal DataObj.  Notice that we have to do a strange thing
      *  if we wish to use the static method parseList() because you
      *  can't return anything from a constructor!
     **/
     //---------------------------------------------------------------------

     public DataObj (String newvalue, String newtype) {
          type = newtype;
          if (newtype.equals("string") || newtype.equals("any")) {
               stringvalue = newvalue;
               type = "string";
          }
          else if (newtype.equals("int")) {
               intvalue = 0;
               try {
                    intvalue = Long.parseLong(newvalue);
               } catch (NumberFormatException nfe) {
                    U.error ("Illegal integer: "+newvalue);
                    intvalue = 0;
               }
          }
          else if (newtype.equals("real"))
               try {
                    realvalue = Double.valueOf(newvalue).doubleValue();
               } catch (NumberFormatException nfe) {
                    U.error ("Illegal real: "+newvalue);
                    realvalue = 0;
               }
          else if (newtype.equals("list")) {
               DataObj newdo = parseList(newvalue);
               type = newdo.type;
               intvalue = newdo.intvalue;
               realvalue = newdo.realvalue;
               stringvalue = newdo.stringvalue;
               numcells = newdo.numcells;
               cells = new DataObj[newdo.numcells];
               for (int i=0; i<numcells; i++)
                    cells[i] = newdo.cells[i];
          }
          else 
               U.error ("in DataObj constructor\n"+
                        "Illegal type = "+newtype);
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: typeof
      *  Last changed: 6/29/2000  by RMM
      *  
      *  Returns the type of this data object (as a string.)
     **/
     //---------------------------------------------------------------------

     public String typeof() {
          return type;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: isList
      *  Last changed: 6/29/2000  by RMM
      *  
      *  Tells if this data object is a list or not.
     **/
     //---------------------------------------------------------------------

     public boolean isList() {
          return type.equals("list");
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: toString
      *  Last changed: 7/20/2000  by RMM
      *  
      *  This function writes out the data object as a String.  It shows
      *  the type as well as the data, in tagged form (i.e. preceded by
      *  type= and data=)
     **/
     //---------------------------------------------------------------------

     public String toString() {
          String retval = "DataObj( type="+type+", value=";
          if (type.equals("string") || type.equals("any"))
               retval += stringvalue;
          else if (type.equals("int"))
               retval += intvalue+"";
          else if (type.equals("real"))
               retval += realvalue+"";
          else if (type.equals("list"))
               retval += listToString();
          return retval + " )";
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: toString2
      *  Last changed: 7/20/2000  by RMM
      *  
      *  This function writes out the data object as a String.  It only
      *  shows the data part, and not as a tagged item.  This is primarily
      *  used by the identity box.
     **/
     //---------------------------------------------------------------------

     public String toString2() {
          String retval = "";
          if (type.equals("string") || type.equals("any"))
               retval += stringvalue;
          else if (type.equals("int"))
               retval += intvalue+"";
          else if (type.equals("real"))
               retval += realvalue+"";
          else if (type.equals("list"))
               retval += listToString();
          return retval;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: getintvalue
      *  Last changed: 6/29/2000  by RMM
      *  
      *  This function gets the integer value of this data object.
      *  Note that if the type of this thing is something other than
      *  "int", you will probably get an erroneous int!  Therefore, use
      *  this at your risk!
     **/
     //---------------------------------------------------------------------

     public long getintvalue() {
          return intvalue;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: getrealvalue
      *  Last changed: 6/29/2000  by RMM
      *  
      *  This function gets the real value of this data object.
      *  Note that if the type of this thing is something other than
      *  "real", you will probably get an erroneous real!  Therefore,
      *  use this at your risk!
     **/
     //---------------------------------------------------------------------

     public double getrealvalue() {
          return realvalue;
     }
     
     //---------------------------------------------------------------------
     /**
      *  METHOD: getstringvalue
      *  Last changed: 6/29/2000  by RMM
      *  
      *  This function gets the string value of this data object.
      *  Note that if the type of this thing is something other than
      *  "string", you will probably get an erroneous string!
      *  Therefore, use this at your risk!
     **/
     //---------------------------------------------------------------------

     public String getstringvalue() {
          return stringvalue;
     }
     
     //---------------------------------------------------------------------
     /**
      *  METHOD: getvalue
      *  Last changed: 6/29/2000  by RMM
      *  
      *  This is the most general value accessor method.  It returns a
      *  string no matter what.  It does this by converting ints and
      *  reals to strings.  Notice that if the type is "list" then we
      *  invoke listToString() which parenthesizes the contents of this
      *  dataobject.
     **/
     //---------------------------------------------------------------------

     public String getvalue()  {
          if (type.equals("int"))
               return Long.toString(intvalue);
          else if (type.equals("real"))
               return Double.toString(realvalue);
          else if (type.equals("string"))
               return stringvalue;
          else if (type.equals("list"))
               return listToString();
          return stringvalue;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: copy
      *  Last changed: 8/26/2000  by RMM
      *  
      *  This function makes a copy of this DataObj.
      *  (Fixed a very subtle bug, numcells was getting incorrectly
      *  assigned.)
     **/
     //---------------------------------------------------------------------

     public DataObj copy() {
          DataObj newone = new DataObj();
          newone.type = type;
          newone.intvalue = intvalue;
          newone.realvalue = realvalue;
          newone.stringvalue = stringvalue;
          newone.numcells = numcells;
          if (numcells > 0) {
               newone.cells = new DataObj[numcells];
               for (int i=0; i<numcells; i++)
                    newone.cells[i] = cells[i].copy();
          }
          return newone;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: cast
      *  Last changed: 7/21/2000  by RMM
      *  
      *  Casting is needed by the "cast" primitive box and possibly in
      *  other places, too.  It is very complicated because of all the
      *  rules.  You can convert between lists and strings, but you
      *  can't convert between lists and numerical types.  If you try,
      *  you will get an error box but a reasonable default value will
      *  be returned just to keep the program from crashing.
     **/
     //---------------------------------------------------------------------

     public DataObj cast (String newtype)  {
          if (type == newtype) return this.copy();

          if (type.equals("real")) {
               if (newtype.equals("int"))
                    return new DataObj((long)realvalue);
               else if (newtype.equals("string"))
                    return new DataObj(realvalue+"");
               else if (newtype.equals("list")) {
                    U.error ("Can't convert real to list");
                    return emptyList();
               }
          }
          else if (type.equals("int")) {
               if (newtype.equals("real"))
                    return new DataObj((double)intvalue);
               else if (newtype.equals("string"))
                    return new DataObj(intvalue+"");
               else if (newtype.equals("list")) {
                    U.error ("Can't convert int to list");
                    return emptyList();
               }
          }
          else if (type.equals("string")) {
               if (newtype.equals("int"))  {
                    int temp;
                    try {
                         temp = Integer.parseInt(stringvalue);
                    } catch (NumberFormatException nfe) {
                         U.error ("Illegal integer: "+stringvalue);
                         temp = 0;
                    }
                    return new DataObj(temp);
               }
               else if (newtype.equals("real"))  {
                    double temp;
                    try {
                         temp = Double.valueOf(stringvalue).doubleValue();
                    } catch (NumberFormatException nfe) {
                         U.error ("Illegal real: "+stringvalue);
                         temp = 0.0;
                    }
                    return new DataObj(temp);
               }
               else if (newtype.equals("list")) 
                    if (stringvalue.trim().charAt(0) == '(')
                         return parseList(stringvalue);
                    else
                         return parseList("("+stringvalue+")");
          }
          else if (type.equals("list")) {
               if (newtype.equals("string")) {
                    return new DataObj(listToString());
               }
               else if (newtype.equals("int")) {
                    U.error ("Can't convert list to int");
                    return new DataObj(0);
               }
               else if (newtype.equals("real")) {
                    U.error ("Can't convert list to real");
                    return new DataObj(0.0);
               }
          }

          return this.copy();
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: add
      *  Last changed: 6/29/2000 by RMM
      *  
      *  Adds a new object in the array of cells.  It puts it
      *  anywhere there is an open spot, which is usually at the end of
      *  the array.
     **/
     //---------------------------------------------------------------------

     public void add (DataObj object)  {
          if (cells == null)  {
               cells = new DataObj[10];
               for (int i=0; i<10; i++) cells[i] = null;
               numcells = 0;
          }
          if (numcells == cells.length)            { // grow cells array
               DataObj[] newcells = new DataObj[cells.length+20];
               for (int i=0; i<newcells.length; i++) newcells[i] = null;
               for (int i=0; i<cells.length; i++) newcells[i] = cells[i];
               cells = newcells;
          }
          for (int i=0; i<cells.length; i++)  {
               if (cells[i] == null) {
                    cells[i] = object;
                    break;
               }
          }
          numcells++;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: add
      *  Last changed: 6/29/2000  by RMM
      *  
      *  Add a string to this Data object.
     **/
     //---------------------------------------------------------------------

     public void add (String s) {
          add(new DataObj(s));
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: add
      *  Last changed: 6/29/2000  by RMM
      *  
      *  Add an integer to this Data object.
     **/
     //---------------------------------------------------------------------

     public void add (int i) {
          add(new DataObj(i));
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: add
      *  Last changed: 6/29/2000  by RMM
      *  
      *  Add a real to this Data object.
     **/
     //---------------------------------------------------------------------

     public void add (double r) {
          add(new DataObj(r));
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: set
      *  Last changed: 3/14/2002 by RMM
      *  
      *  This inserts the object at the specified index location.
      *  If the cells array is null, or there aren't this many elements
      *  in the array, it is grown so that there is at least this many
      *  cells.  The current object at the index is lost.
      *  The index numbers are 0-based, like C or Java arrays.
     **/
     //---------------------------------------------------------------------

     public void set (DataObj object, int index) {
          if (cells == null)  {
               cells = new DataObj[index+1];
               for (int i=0; i<cells.length; i++) cells[i] = null;
               cells[index] = object;
               numcells = 1;
          }
          else if (cells.length <= index) {    // grow
               DataObj[] newcells = new DataObj[index+1];
               for (int i=0; i<newcells.length; i++) newcells[i] = null;
               for (int i=0; i<cells.length; i++) newcells[i] = cells[i];
               newcells[index] = object;
               cells = newcells;
               numcells = index+1;
          }
          else
               cells[index] = object;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: set
      *  Last changed: 6/29/2000  by RMM
      *  
      *  This function changes the element at index "index."
      *  The new element is a string.
     **/
     //---------------------------------------------------------------------

     public void set (String s, int index) {
          set(new DataObj(s), index);
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: set
      *  Last changed: 6/29/2000  by RMM
      *  
      *  This function changes the element at index "index."
      *  The new element is an int.
     **/
     //---------------------------------------------------------------------

     public void set (int i, int index) {
          set(new DataObj(i), index);
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: set
      *  Last changed: 6/29/2000  by RMM
      *  
      *  This function changes the element at index "index."
      *  The new element is a real.
     **/
     //---------------------------------------------------------------------

     public void set (double r, int index) {
          set(new DataObj(r), index);
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: get
      *  Last changed: 6/29/2000 by RMM
      *  
      *  This extracts a value from a list at a given index,
      *  and returns the item.
     **/
     //---------------------------------------------------------------------

     public DataObj get (int index)  {
          if (cells != null && index < numcells && cells[index] != null)
               return cells[index];
          else
               return null;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: append
      *  Last changed: 6/29/2000 by RMM
      *  
      *  This method appends another list onto the end of this one, a
      *  rather tedious operation!
      *  This changes the original list; it does not make an entirely new
      *  list.
     **/
     //---------------------------------------------------------------------

     public void append (DataObj somelist)  {
          if (!somelist.isList()) 
               return;
          compress();
          for (int i=0; i<somelist.length(); i++)
               add(somelist.get(i));     
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: remove
      *  Last changed: 6/29/2000 by RMM
      *  
      *  This removes a value from a list at a given index, permanently
      *  altering this list.
     **/
     //---------------------------------------------------------------------

     public void remove (int index) {
          if (cells == null || index >= numcells || cells[index] == null)
               return;
          for (int i=index; i<cells.length-1; i++)
               cells[i] = cells[i+1];
          cells[cells.length-1] = null;
          numcells--;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: listToString
      *  Last changed: 6/29/2000 by RMM
      *  
      *  This method makes a string that contains a string
      *  representation of the list value.  It does not preserve the
      *  indices, but it does preserve the substructure, sort of like a
      *  LISP list.
     **/
     //---------------------------------------------------------------------

     public String listToString()  {
          String result = "(";
          for (int i=0; i<numcells; i++)
               if (cells[i] != null)
                    if (cells[i].type.equals("list"))
                         result += cells[i].listToString();
                    else  {
                         result += cells[i].getvalue();
                         if (i < numcells - 1)
                              result += ", ";
                    }
          result += ")";
          return result;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: compress
      *  Last changed: 8/14/2000 by RMM
      *  
      *  This method squishes out all nulls in the array of cells so
      *  all the non-null entries are concentrated at the starting
      *  end of the array.
     **/
     //---------------------------------------------------------------------

     public void compress ()  {
          if (numcells == 0)
               return;
          if (numcells == cells.length)
               return;
          int j=0;         // position at which to put the next non-null
          for (int i=0; i<cells.length; i++)
               if (cells[i] != null)
                    cells[j++] = cells[i];
          while (j < cells.length)     // clear out the refs at end of
               cells[j++] = null;      // the array
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: length
      *  Last changed: 7/21/2000 by RMM
      *  
      *  This method counts the non-nulls in the array of cells.
      *  I suppose we should have just returned "numcells."
     **/
     //---------------------------------------------------------------------

     public int length()  {
/*
          int count = 0;
          for (int i=0; i<cells.length; i++)
               if (cells[i] != null)
                    count++;
          return count;
*/
          if (type.equals("string"))
               return stringvalue.length();
          else if (type.equals("list"))
               return numcells;
          else
               return 0;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: sort
      *  Last changed: 8/14/2000 by RMM
      *  
      *  This method sorts the data object if it is a list.  If it is
      *  not a list, nothing is done.
      *  The sorting is done based on whether the type of the first
      *  cell is an int, real, or string.
     **/
     //---------------------------------------------------------------------

     public void sort (String sorttype) {
          if (!isList())
               return;
          compress();
          if (sorttype.equals("int")) {
               for (int i=0; i<numcells-1; i++)
                    for (int j=i+1; j<numcells; j++)
                         if (U.atoi(cells[i].getvalue())
                                > U.atoi(cells[j].getvalue())) {
                              DataObj temp = cells[i];
                              cells[i] = cells[j];
                              cells[j] = temp;
                         }
          }
          else if (sorttype.equals("real")) {
               for (int i=0; i<numcells-1; i++)
                    for (int j=i+1; j<numcells; j++)
                         if (U.atod(cells[i].getvalue())
                                > U.atod(cells[j].getvalue())) {
                              DataObj temp = cells[i];
                              cells[i] = cells[j];
                              cells[j] = temp;
                         }
          }
          else if (sorttype.equals("string") || sorttype.equals("any")) {
               for (int i=0; i<numcells-1; i++)
                    for (int j=i+1; j<numcells; j++)
                         if (cells[i].getvalue()
                                 .compareTo(cells[j].getvalue()) > 0) {
                              DataObj temp = cells[i];
                              cells[i] = cells[j];
                              cells[j] = temp;
                         }
          }
          else if (sorttype.equals("list"))
               U.error ("Illegal sort type of \"list\".");
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: find
      *  Last changed: 8/14/2000 by RMM
      *  
      *  This method tries to find a specified data object inside a list.
      *  If not found, it returns -1.  Otherwise, it returns the index
      *  in the list.  This is used right now only by the "contains" box.
     **/
     //---------------------------------------------------------------------

     public int find (DataObj toFind) {
          if (!isList())
               return -1;
          for (int i=0; i<numcells; i++)
               if (DataObj.equal(cells[i], toFind))
                    return i;
          return -1;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: reverse
      *  Last changed: 8/14/2000 by RMM
      *  
      *  This method reverses a list.
     **/
     //---------------------------------------------------------------------

     public void reverse() {
          if (!isList())
               return;
          compress();
          int j=numcells-1;
          for (int i=0; i<numcells/2; i++) {
               DataObj temp = cells[i];
               cells[i] = cells[j];
               cells[j] = temp;
               j--;
          }
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: equal
      *  Last changed: 7/21/2000 by RMM
      *  
      *  The equal function compares two DataObjects for equality, based
      *  on their type, and returns true or false.  If their types are not
      *  the same, they are automatically not equal.  If their types are
      *  "any" or "list", then we do a simple Java string comparison
      *  using the value provided by toString2().
     **/
     //---------------------------------------------------------------------

     public static boolean equal (DataObj d1, DataObj d2) {
          if (!d1.type.equals(d2.type))
               return false;
          if (d1.type.equals("int"))
               return d1.getintvalue() == d2.getintvalue();
          else if (d1.type.equals("real"))
               return d1.getrealvalue() == d2.getrealvalue();
          else if (d1.type.equals("string"))
               return d1.getvalue().equals(d2.getvalue());
          else
               return d1.toString2().equals(d2.toString2());
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: parseList
      *  Last changed: 7/21/2000 by RMM
      *  
      *  This function reads a parenthesized String and forms a list
      *  from it, returning a DataObj whose type is "list."  It can also
      *  make sublists whenever it sees nested parentheses.
      *  
      *  Examples:
      *  ()             ==> the empty list
      *  (x y z)        ==> one list containing 3 elements
      *  a              ==> (a)
      *  a b c          ==> (a b c)
      *  (x y z)(...)   ==> it just reads the first list (x y z)
      *  ((a))          ==> a list with one element, which is a list
      *  (a (b c) d)    ==> a 3-element list whose 2nd elt is a list
      *  
      *  It can't do anything clever like read a string that contains
      *  parentheses.  Maybe later!
     **/
     //---------------------------------------------------------------------

     public static DataObj parseList (String s) {
          DataObj current = null;
          Stack stack = new Stack();
          String[] rets;

          while (s.length() > 0) {
               rets = nextToken(s);
               String token = rets[0];
               s = rets[1];
               if (token.length() == 0) break;
               if (token.equals("(")) {
                    stack.push(current);
                    current = DataObj.emptyList();
               }
               else if (token.equals(")")) {
                    if(stack.empty()) {
                         U.error ("Illegal expression, too many )'s");
                         return DataObj.emptyList();
                    }
                    DataObj underneath = (DataObj) stack.pop();
                    if (underneath == null)
                         return current;
                    underneath.add(current);
                    current = underneath;
               }
               else  {
                    if (current == null) {
                         U.error ("Illegal expression, no ('s");
                         return DataObj.emptyList();
                    }
                    current.add(new DataObj(token));
               }
          }

          if(!stack.empty()) {
               U.error ("Illegal expression: too many ('s");
               return DataObj.emptyList();
          }
          return current;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: explode
      *  Last changed: 8/9/2000 by RMM
      *  
      *  This function creates a list of characters from a String.
      *  Its name is taken from an old LISP function by the same name.
      *  It is only called when a cast box takes in a string and spits
      *  out a list, and the string that comes in does not start with
      *  a left parenthesis.
      *  It actually creates a list of strings, with each string being
      *  one character long.
      *  
      *  Examples:
      *  abcde          ==> (a b c d e)
      *  ==> the empty list
     **/
     //---------------------------------------------------------------------

     public static DataObj explode (String s) {
          DataObj current = DataObj.emptyList();

          if (s.length() == 0)
               return current;

          for (int i=0; i<s.length(); i++)
               current.add (new DataObj(s.charAt(i)+""));
          return current;
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: nextToken
      *  Last changed: 7/21/2000 by RMM
      *  
      *  This is a helper function for the above parseList().
      *  It breaks a string into the next token and whatever follows, and
      *  returns those two pieces in a 2-element array of Strings.
      *  It also skips over leading blanks.
      *  
      *  Examples:
      *  nextToken("   abc ) x zy)"); => {"abc", ") x zy)"}
     **/
     //---------------------------------------------------------------------

     private static String[] nextToken (String s) {
          String newtoken = "";
          while (s.length() > 0 && s.charAt(0) == ' ')
               s = s.substring(1);

          if (s.length() == 0)
               return new String[]{"", ""};

          if (s.charAt(0) == '(' || s.charAt(0) == ')') {
               newtoken += s.charAt(0);
               return new String[]{newtoken, s.substring(1)};
          }

          while (true) {
               if (s.length() == 0)
                    break;
               char ch = s.charAt(0);
               if (ch == '(' || ch == ')' || ch == ' ')
                    break;
               newtoken += ch;
               s = s.substring(1);
          }
          return new String[]{newtoken, s};
     }

     //---------------------------------------------------------------------
     /**
      *  METHOD: rest
      *  Last changed: 6/12/2001 by RMM
      *  
      *  This copies the current DataObj and if it is a string, it chops
      *  off the first character (if length > 0).  If it is a list, it
      *  chops off the first element (if numcells > 0).  Otherwise, it
      *  does nothing but returns the copy.
     **/
     //---------------------------------------------------------------------

     public DataObj rest() {
          DataObj newcopy = this.copy();
          if (newcopy.type.equals("string")) {
               if (newcopy.stringvalue.length() > 0)
                    newcopy.stringvalue = newcopy.stringvalue.substring(1);
          }
          else if (newcopy.type.equals("list")) {
               if (newcopy.numcells > 0) {
                    for (int i=1; i<newcopy.numcells; i++) 
                         newcopy.cells[i-1] = newcopy.cells[i];
                    newcopy.numcells--;
               }
          }
          return newcopy;
     }

     //-------------------------------------------------------------------
     /**
      *  METHOD: toToken
      *  Last changed: 1/30/2002 by RMM
      *  
      *  This builds a new TokenList by working through a DataObj list.
      *  We don't want to let SPL/Token know anything about DataObj,
      *  but obviously DataObj has to know about SPL/Token in order to
      *  convert between the formats.
     **/
     //-------------------------------------------------------------------

     public static Token toToken (DataObj dobj) {
          if (dobj.type.equals("int"))
               return new Token (Token.INT, dobj.intvalue);
          if (dobj.type.equals("real"))
               return new Token (Token.REAL, dobj.realvalue);
          if (dobj.type.equals("string"))
               return new Token (Token.STRING, dobj.stringvalue);

          if (dobj.type.equals("list")) {
               TokenList tl = new TokenList();
               for (int i=0; i<dobj.cells.length; i++)
                    if (dobj.cells[i] != null)
                         tl.add (toToken(dobj.cells[i]));
               return new Token (Token.LIST, tl);
          }
          return new Token (Token.ERROR, "Unknown type in Dataobject="+dobj.type);
     }

     //-------------------------------------------------------------------
     /**
      *  METHOD: toDataObj
      *  Last changed: 1/30/2002 by RMM
      *  
      *  This builds a new DataObj that is a list by working through a
      *  TokenList.
      *  We don't want to let SPL/Token know anything about DataObj,
      *  but obviously DataObj has to know about SPL/Token in order to
      *  convert between the formats.
     **/
     //-------------------------------------------------------------------

     public static DataObj toDataObj (Token tok) {
          if (tok.type == Token.INT)
               return new DataObj (tok.intval);

          if (tok.type == Token.REAL)
               return new DataObj (tok.doubleval);

          if (tok.type == Token.STRING)
               return new DataObj (tok.value);

          if (tok.type == Token.LIST) {
               DataObj dobj = emptyList();
               TokenList tl = tok.sublist;
               for (int i=0; i<tl.numtokens; i++)
                    if (tl.tokens[i] != null)
                         dobj.add (toDataObj(tl.tokens[i]));
               return dobj;
          }
 
          return new DataObj(0);        // default, should never get to this
     }

     public static boolean isValid (String someType) {
          return someType.equals("int") ||
                 someType.equals("real") ||
                 someType.equals("list") ||
                 someType.equals("string") ||
                 someType.equals("any");
     }
}
