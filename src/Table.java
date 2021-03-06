import java.util.*;
import java.lang.*;

public class Table {
    
    private int col_count, row_count;
    private String[] col_name; //column names, the first line of the file
    private boolean[] col_type; //column types, the second line of the file, ture - string false - double
    private List<List<Object>> data; //file content
    private boolean[] single_value; //whether a column contatins single value
    private List<Object> lastRow; //cache the last row of the table for modifying single_class and single_value
    
    public Table(int count, String[] name, boolean[] type) {
        col_count = count;
        col_name = new String[col_count];
        col_type = new boolean[col_count];
        System.arraycopy(name, 0, col_name, 0, col_count);
        System.arraycopy(type, 0, col_type, 0, col_count);
        data = new ArrayList<List<Object>>();
        single_value = new boolean[col_count];
        Arrays.fill(single_value, true);
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
        //check size
        if (row.size() != col_count) {
            System.err.println("Invalid parameter: wrong length");
            return false;
        }
        //check data type
        for (int i = 0; i < row.size(); i++) {
            if (col_type[i] && !(row.get(i) instanceof String)) {
                System.err.println("Data type conflict, expected String");
                return false;
            }
            if (!col_type[i] && !(row.get(i) instanceof Double)) {
                System.err.println("Data type conflict, expected Double");
                return false;
            }
        }
        //modify single column value
        for (int i = 0; lastRow != null && i < col_count; i++) {
            if (!single_value[i]) continue;
            single_value[i] = row.get(i).equals(lastRow.get(i));
        }
        data.add(row);
        row_count++;
        lastRow = row;
        return true;
    }
    
    /**
     * Calculate the entropy of a list of category, formula as follow:
     * pi = number of a specified category / size of input
     * H = sum(pi * log2(pi)) for pi of all distinct categories.
     * @param list Array of category to be computed
     * @return double The entropy of input categories.
     */
    private double calEntropy(List<Double> list) {
        Map<Double, Integer> map = new HashMap<Double, Integer>();
        for (Double d : list) {
            if (map.containsKey(d)) 
                map.put(d, map.get(d) + 1);
            else 
                map.put(d, 1);
        }
        double entropy = 0.0;
        for (Double d : map.keySet()) {
            double pi = (double)map.get(d) / list.size();
            entropy += pi * Math.log(pi) / Math.log(2);
        }
        return -entropy;
    }
    
    /**
     * Split table into two new tables.
     * @param ind Store the index of column that used to split the table
     * @param val Store the value that used to split the table
     * @return Table[] Size 2, left table at index 0 and right table at index 1.
     */
    public Table[] split(int[] ind, Object[] val) {
        //find best split column and value
        double min_entropy = Double.MAX_VALUE;
        for (int i = 0; i < col_count - 1; i++) {
            Set<Object> used = new HashSet<Object>();
            for (int j = 0; j < row_count; j++) {
                Object value = data.get(j).get(i);
                if (!used.add(value)) continue;
                List<Double> left = new ArrayList<Double>(), right = new ArrayList<Double>();
                for (int k = 0; k < row_count; k++) {
                    if (col_type[i]) {
                        //String column
                        if (data.get(k).get(i).equals(value))
                            left.add((Double)data.get(k).get(col_count - 1));
                        else
                            right.add((Double)data.get(k).get(col_count - 1));
                    }
                    else {
                        //double column
                        if ((Double)data.get(k).get(i) < (Double)value)
                            left.add((Double)data.get(k).get(col_count - 1));
                        else
                            right.add((Double)data.get(k).get(col_count - 1));
                    }
                }
                double sum = calEntropy(left) + calEntropy(right);
                if (sum < min_entropy && !left.isEmpty() && !right.isEmpty()) {
                    //no empty sub-table is allowed since empty sub-tree will cause crash
                    //System.out.println("Entropy: " + sum + ", left size: " + left.size() + ", right size: " + right.size());
                    min_entropy = sum;
                    ind[0] = i;
                    val[0] = value;
                }
            }
        }
        //System.out.println("Split by col: " + ind[0] + ", val: " + val[0]);
        return split(ind[0], val[0]);
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
     * Find whether the table is splittable.
     * @return int 0 if splittable,
     *             -1 if all rows have the same class,
     *             -2 if each column contains single value.
     */
    public int splittable() {
        if (single_value[col_count - 1]) return -1;
        for (int i = 0; i < col_count; i++) {
            if (i == col_count - 1) return -2;
            if (!single_value[i]) break;
        }
        return 0;
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
    
    public void printRow(int line) {
        if (line < 0 || line >= row_count) {
            System.err.println("Invalid line index");
            return;
        }
        List<Object> row = data.get(line);
        for (int i = 0; i < col_count; i++) {
            System.out.print(row.get(i));
            if (i < col_count - 1) 
                System.out.print(' ');
        }
        System.out.println();
    }
    
    public void printTable() {
        System.out.format("This table comtains %d rows, and %d columns%n", row_count, col_count);
        System.out.println("Contains following single value column:");
        for (int i = 0; i < col_count - 1; i++)
            if (single_value[i]) System.out.print(i + " ");
        System.out.println();
        if (single_value[col_count - 1]) 
            System.out.println("Contains single category");
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
        System.out.println();
    }
}
