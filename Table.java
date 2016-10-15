import java.util.*;
import java.lang.*;

public class Table {
    
    private int col_count, row_count;
    private String[] col_name; //column names, the first line of the file
    private boolean[] col_type; //column types, the second line of the file, ture - string false - double
    private List<List<Object>> data; //file content
    private boolean single_class; //whether this table continas only one category value
    private boolean single_value; //whether each column contains single data value                                        
    private List<Object> lastRow; //cache the last row of the table for modifying single_class and single_value
    
    public Table(int count, String[] name, boolean[] type) {
        col_count = count;
        col_name = new String[col_count];
        col_type = new boolean[col_count];
        System.arraycopy(name, 0, col_name, 0, col_count);
        System.arraycopy(type, 0, col_type, 0, col_count);
        data = new ArrayList<List<Object>>();
        single_class = true;
        single_value = true;
        lastRow = null;
    }
    
    /**
     * Add a row as a string to table.
     * @param str Row content, all fields seperated by space
     * @return boolean Whether this row is added.
     */
    public boolean addRow(String str) {
        String[] fields = str.split(" ");
        if (fields.length != col_count) {
            System.err.println("Invalid number of fields");
            return false;
        }
        List<Object> row = new ArrayList<Object>();
        for (int i = 0; i < col_count; i++) {
            if (col_type[i]) {
                //add String
                row.add(fields[i]);
            }
            else {
                //add double or int
                try {
                    row.add(Double.parseDouble(fields[i]));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    return false;
                }
            }
        }
        return addRow(row);
    }
    
    /**
     * Add a row as a list of Object to table.
     * @param row Row content
     * @return boolean Whether this row is added.
     */
    public boolean addRow(List<Object> row) {
        if (row == null) {
            System.err.println("Invalid parameter: null pointer");
            return false;
        }
        //compare category of new row with last row
        if (single_class && lastRow != null) {
            single_class = lastRow.get(col_count - 1).equals(row.get(col_count - 1));
        }
        //compare data fields of new row with last row
        if (single_value && lastRow != null) {
            for (int i = 0; single_value && i < col_count - 1; i++)
                if (!lastRow.get(i).equals(row.get(i))) 
                    single_value = false;
        }
        data.add(row);
        row_count++;
        lastRow = row;
        return true;
    }
    
    /**
     * Split table into two new tables.
     * @param col The column that contains values to be compared
     * @param value The splite condition
     *              In case of String, equal values go to left, others to right
     *              In case of Double, less values go to left, other to right
     * @return Table[] Size 2, left table at index 0 and right table at index 1.
     */
    public Table[] split(int col, Object value) {
        if (col_type[col] != (value instanceof String)) {
            System.err.println("Value type conflicts with column type");
            System.exit(1);
        }
        if (col == col_count - 1) {
            System.err.println("Cannot split table by category");
            System.exit(1);
        }
        Table[] tbs = new Table[2];
        //tbs[0] is left table, tbs[1] is right table
        tbs[0] = new Table(col_count, col_name, col_type);
        tbs[1] = new Table(col_count, col_name, col_type);
        for (int i = 0; i < row_count; i++) {
            List<Object> row = new ArrayList<Object>(data.get(i));
            if (col_type[col]) {
                //type of string
                if (row.get(col).equals(value)) {
                    tbs[0].addRow(row);
                }
                else {
                    tbs[1].addRow(row);
                }
            }
            else {
                //type of double
                if ((Double)row.get(col) < (Double)value) {
                    tbs[0].addRow(row);
                }
                else {
                    tbs[1].addRow(row);
                }
            }
        }
        return tbs;
    }
    
    /**
     * Find the column with smallest entropy and return its index.
     * @return int The index of column that has the smallest entropy,
     *             -1 if all rows have the same class,
     *             -2 if each column contains single value.
     */
    public int minEntropyColumn() {
        if (single_class) return -1;
        if (single_value) return -2;
        return 0;
    }
    
    public Object findSplitValue(int col) {
        return null;
    }
    
    public int getColCount() {
        return col_count;
    }
    
    public int getRowCount() {
        return row_count;
    }
    
    public String[] getColName() {
        String[] ret = new String[col_count];
        System.arraycopy(col_name, 0, ret, 0, col_count);
        return ret;
    }
    
    public boolean[] getColType() {
        boolean[] ret = new boolean[col_count];
        System.arraycopy(col_type, 0, ret, 0, col_count);
        return ret;
    }
    
    public List<Object> getRow(int line) {
        return new ArrayList<Object>(data.get(line));
    }
    
    public void printTable() {
        System.out.format("This table comtains %d rows, and %d columns%n", row_count, col_count);
        System.out.println("Contians single class: " + single_class + ", single value: " + single_value);
        for (String n : col_name) {
            System.out.format("%-10s", n);
        }
        System.out.println();
        for (boolean t : col_type) {
            System.out.format("%-10s", t ? "String" : "Double");
        }
        System.out.println();
        for (int i = 0; i < row_count; i++) {
            List<Object> row = data.get(i);
            for (int j = 0; j < col_count; j++) {
                if (col_type[j]) {
                    System.out.format("%-10s", row.get(j));
                }
                else {
                    System.out.format("%-10.3f", row.get(j));
                }
            }
            System.out.println();
        }
    }
}
