import java.util.*;

public class TestDriver {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        //test0();
        //test1();
        //test2();
        //test3();
        //test4();
        test5();
        //test6();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.format("Running time: %dms%n", totalTime);
    }
    
    private static void test0() {
        Table tb = Reader.buildTable("invalid_filename.txt");
        tb.printTable();
    }
    
    private static void test1() {
        Table tb = Reader.buildTable("tests/valid_titles.txt");
        tb.printTable();
    }
    
    private static void test2() {
        Table tb = Reader.buildTable("tests/invalid_titles.txt");
        tb.printTable();
    }
    
    private static void test3() {
        Table tb = Reader.buildTable("tests/valid_content_1012.txt");
        //tb.printTable();
        Object d = new Double(4.5);
        Object s = "row2.1";
        Table[] tbs = tb.split(2, d);
        //tbs[0].printTable();
        //tbs[1].printTable();
    }
    
    private static void test4() {
        Table tb = Reader.buildTable("tests/invalid_content.txt");
        tb.printTable();
    }
    
    private static void test5() {
        Table tb = Reader.buildTable("tests/valid_content_20.txt");
        Map<String, Integer> map = new HashMap<String, Integer>();
        String[] names = tb.getColName();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], i);
        }
        DecisionTree dt = new DecisionTree(tb, map);
        //dt.printTree();
        List<Object> list = new ArrayList<Object>();
        list.add("row3.3");
        list.add(3.4);
        list.add(4.0);
        list.add("row3.4");
        list.add(1.0);
        System.out.println(dt.getCategory(list));
    }
    
    private static void test6() {
        Table tb = Reader.buildTable("tests/valid_content_split.txt");
        int[] ind = new int[1];
        Object[] val = new Object[1];
        Table[] tbs = tb.split(ind, val);
        System.out.println("Split by col: " + ind[0] + ", val: " + val[0]);
        tbs[0].printTable();
        tbs[1].printTable();
    }
}