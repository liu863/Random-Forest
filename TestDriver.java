
public class TestDriver {
    public static void main(String[] args) {
        //test0();
        //test1();
        //test2();
        test3();
        //test4();
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
        Table tb = Reader.buildTable("tests/valid_content.txt");
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
}