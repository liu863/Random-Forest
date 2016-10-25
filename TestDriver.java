import java.util.*;

public class TestDriver {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        //test0();
        //test1();
        //test2();
        //test3();
        //test4();
        //test5();
        //test6();
        //test7();
        //test8();
        //test9();
        //test10();
        //test11();
        //test12();
        //test13();
        //test14();
        test15();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.format("Run time: %dms%n", totalTime);
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
        Table tb = Reader.buildTable("tests/valid_content_1012*6.txt");
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
        Table tb = Reader.buildTable("tests/valid_content_1012*6.txt");
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
    
    private static void test7() {
        Table tb = Reader.buildTable("tests/valid_content_20*6.txt");
        String s1 = "s1";
        String s2 = "s2";
        String s3 = "s3";
        Double d1 = 1.0;
        Double d2 = 2.0;
        Double d3 = 3.0;
        Double d4 = 4.0;
        Double d5 = 5.0;
        Double d6 = 6.0;
        double d7 = 7.0;
        List<Object> list1 = new ArrayList<Object>(); //invalid length
        list1.add((Object)s1);
        list1.add((Object)d1);
        list1.add((Object)d2);
        list1.add((Object)s2);
        list1.add((Object)d3);
        List<Object> list2 = new ArrayList<Object>(); //data type conflict: String
        list2.add((Object)s1);
        list2.add((Object)d1);
        list2.add((Object)d2);
        list2.add((Object)d3);
        list2.add((Object)d4);
        list2.add((Object)d5);
        List<Object> list3 = new ArrayList<Object>(); //data type conflict: Double
        list3.add((Object)s1);
        list3.add((Object)d1);
        list3.add((Object)d2);
        list3.add((Object)s2);
        list3.add((Object)d3);
        list3.add((Object)s3);
        List<Object> list4 = new ArrayList<Object>(); //good data
        list4.add((Object)s1);
        list4.add((Object)d1);
        list4.add((Object)d2);
        list4.add((Object)s2);
        list4.add((Object)d3);
        list4.add((Object)d7);
        tb.addRow(list4);
        tb.printTable();
    }
    
    private static void test8() {
        Table tb = Reader.buildTable("tests/valid_content_2000*20.txt");
        RandomForest rf = new RandomForest(tb);
        rf.construct();
    }
    
    private static void test9() {
        Table tb = Reader.buildTable("tests/valid_content_20*6.txt");
        RandomForest rf = new RandomForest(tb);
        for (int i = 0; i < 5; i++) {
            Table rtable = rf.rowRandomize(tb);
            rtable.printTable();
        }
    }
    
    private static void test10() {
        Table tb = Reader.buildTable("tests/valid_content_5*20.txt");
        RandomForest rf = new RandomForest(tb);
        for (int i = 0; i < 5; i++) 
            rf.colRandomize(tb).printTable();
    }
    
    private static void test11() {
        
        //Table tbt = Reader.buildTable("tests/covs_t_rev.txt");
        //System.out.println("tbt done");
        /*
         Map<String, Integer> mapt = new HashMap<String, Integer>();
         String[] namest = tbt.getColName();
         for (int i = 0; i < namest.length; i++) 
         mapt.put(namest[i], i);
         DecisionTree dtt = new DecisionTree(tbt, mapt);
         System.out.println("dtt done");       
         */
        Table tbv = Reader.buildTable("tests/covs_v_rev.txt");
        System.out.println("tbv done");
        Map<String, Integer> mapv = new HashMap<String, Integer>();
        String[] namesv = tbv.getColName();
        for (int i = 0; i < namesv.length; i++) 
            mapv.put(namesv[i], i);
        DecisionTree dtv = new DecisionTree(tbv, mapv);
        System.out.println("dtv done");
        
        //dtt.printTree();
        //dtv.printTree();
        Table tbt = Reader.buildTable("tests/covs_t_rev.txt");
        System.out.println("tbt done");
        //RandomForest.validate(dtt, tbt, tbt.getRowCount());
        //RandomForest.validate(dtt, tbv, tbv.getRowCount());
        RandomForest.validate(dtv, tbt, tbt.getRowCount() / 10);
        RandomForest.validate(dtv, tbv, tbv.getRowCount());
        
    }
    
    private static void test12() {
        Table tbt = Reader.buildTable("tests/chess_t_rev.txt");
        System.out.println("tbt done");
        Map<String, Integer> mapt = new HashMap<String, Integer>();
        String[] namest = tbt.getColName();
        for (int i = 0; i < namest.length; i++) 
            mapt.put(namest[i], i);
        //DecisionTree dtt = new DecisionTree(tbt, mapt);
        //System.out.println("dtt done");
        RandomForest rft = new RandomForest(tbt);
        
        Table tbv = Reader.buildTable("tests/chess_v_rev.txt");
        System.out.println("tbv done");
        Map<String, Integer> mapv = new HashMap<String, Integer>();
        String[] namesv = tbv.getColName();
        for (int i = 0; i < namesv.length; i++) 
            mapv.put(namesv[i], i);
        //DecisionTree dtv = new DecisionTree(tbv, mapv);
        //System.out.println("dtv done");
        
        rft.construct();
        for (int i = 20; i < 50; i++) {
            System.out.format("%nPredicting row %d%n", i);
            rft.predict(tbv.getRow(i));
        }
    }
    
    private static void test13() {
        Table tb = Reader.buildTable("stocks/002460_modified_category.txt");
        System.out.println("tb done");
        Map<String, Integer> map = new HashMap<String, Integer>();
        String[] names = tb.getColName();
        for (int i = 0; i < names.length; i++) 
            map.put(names[i], i);
        DecisionTree dt = new DecisionTree(tb, map);
        System.out.println("dt done");
        //dt.printTree();
        //RandomForest.validate(dt, tb, tb.getRowCount());
        
        //2016/10/21 31.43 31.60 30.51 31.03 201141.22 -1.87 3.45 31.07 30.17 29.63 -0.10 -0.53 0.85
        List<Object> row1 = new ArrayList<Object>(); //data of 2016/10/21 predict 2016/10/24
        row1.add(31.43);
        row1.add(31.60);
        row1.add(30.51);
        row1.add(31.03);
        row1.add(201141.22);
        row1.add(-1.87);
        row1.add(3.45);
        row1.add(31.07);
        row1.add(30.17);
        row1.add(29.63);
        row1.add(-0.10);
        row1.add(-0.53);
        row1.add(0.85);
        
        //2015/03/17 11.13 11.63 11.04 11.22 239492.02 0.99 5.31 10.92 10.96 10.43 0.58 0.59 -0.03
        List<Object> row2 = new ArrayList<Object>(); //data of 2015/03/17 predict 2015/03/18 (6.0)
        row2.add(11.13);
        row2.add(11.63);
        row2.add(11.04);
        row2.add(11.22);
        row2.add(239492.02);
        row2.add(0.99);
        row2.add(5.31);
        row2.add(10.92);
        row2.add(10.96);
        row2.add(10.43);
        row2.add(0.58);
        row2.add(0.59);
        row2.add(-0.03);
        
        //2015/06/29 13.27 13.27 11.53 11.53 174816.72 -10.06 13.57 13.78 14.80 15.93 -0.24 0.42 -1.31
        List<Object> row3 = new ArrayList<Object>(); //data of 2015/06/29 predict 2015/06/30 (7.0)
        row3.add(13.27);
        row3.add(13.27);
        row3.add(11.53);
        row3.add(11.53);
        row3.add(174816.72);
        row3.add(-10.06);
        row3.add(13.57);
        row3.add(13.78);
        row3.add(14.80);
        row3.add(15.93);
        row3.add(-0.24);
        row3.add(0.42);
        row3.add(-1.31);
        
        //2016/03/29 23.48 24.00 22.68 23.23 296267.30 -2.27 5.55 23.13 22.51 20.70 0.69 0.24 0.90
        List<Object> row4 = new ArrayList<Object>(); //data of 2016/03/29 predict 2016/03/30 (9.0)
        row4.add(23.48);
        row4.add(24.00);
        row4.add(22.68);
        row4.add(23.23);
        row4.add(296267.30);
        row4.add(-2.27);
        row4.add(5.55);
        row4.add(23.13);
        row4.add(22.51);
        row4.add(20.70);
        row4.add(0.69);
        row4.add(0.24);
        row4.add(0.90);
        
        System.out.println(dt.getCategory(row1));
        System.out.println(dt.getCategory(row2));
        System.out.println(dt.getCategory(row3));
        System.out.println(dt.getCategory(row4));
        
        //RandomForest rf = new RandomForest(tb);
        //rf.construct();
    }
    
    private static void test14() {
        Table tb = Reader.buildTable("stocks/002460_modified_category.txt");
        System.out.println("tb done");
        /*
        Map<String, Integer> map = new HashMap<String, Integer>();
        String[] names = tb.getColName();
        for (int i = 0; i < names.length; i++) 
            map.put(names[i], i);
        DecisionTree dt = new DecisionTree(tb, map);
        System.out.println("dt done");
        dt.printTree();
        RandomForest.validate(dt, tb, tb.getRowCount());
        */
        RandomForest rf = new RandomForest(tb);
        rf.construct();
        rf.validateAll();
    }
    
    private static void test15() {
        //multi_thread
        Table tb = Reader.buildTable("stocks/002460_modified_category.txt");
        RandomForest rf = new RandomForest(tb);
        rf.construct();
        rf.validateAll();
        
        //Single thread run time: 165464ms
        //Multiple thread run time: 93459ms
    }
}
