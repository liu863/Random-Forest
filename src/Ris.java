
public class Ris {
    public static void main(String[] args) {
        Table tb = Reader.buildTable("training_file.txt");
        RandomForest rf = new RandomForest(tb);
        Reader.examination("testing_file.txt", rf);
    }
}