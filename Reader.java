import java.util.*;
import java.io.*;

public class Reader {
    
    /**
     * Copy all information from a txt file to memory, packed as Table class.
     * @param file Name of file that contains training data
     *             First line of the file is column names
     *             Second line is value type of that column, String or Double
     *             Followed by data content, all columns seperated by space
     *             The last column is the category of that row, as Double
     * @return Table A table contains everything in that file.
     */
    public static Table buildTable(String file) {
        BufferedReader br = null;
        String names = null, types = null;
        try {
            br = new BufferedReader(new FileReader(file));
            names = br.readLine();
            types = br.readLine();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
        if (names == null || types == null) {
            System.err.println("Names and types are incomplete");
            System.exit(1);
        }
        
        String[] col_name = names.split(" "), str_col_type = types.split(" ");
        if (col_name.length != str_col_type.length) {
            System.err.println("Different length of names and types");
            System.exit(1);
        }
        //convert a string array to boolean array
        boolean[] col_type = new boolean[str_col_type.length];
        for (int i = 0; i < col_type.length; i++) {
            col_type[i] = str_col_type[i].equals("S");
        }
        
        Table tb = new Table(col_name.length, col_name, col_type);
        
        try {
            String row;
            int count = 1;
            while ((row = br.readLine()) != null) {
                if (!tb.addRow(row)) {
                    System.err.println("Failed to add row " + count);
                }
                else {
                    //System.out.println("Row " + count + " added");
                }
                count++;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return tb;
    }
    
    public static void examination(String file, RandomForest rf) {
    
    }
}