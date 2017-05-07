import java.util.*;

public class RandomForest {
    
    private final int SIZE = 100; //number of trees in this forest
    private List<DecisionTree> dts;
    private Table tb;
    private int col_count, row_count;
    private String[] col_name;
    private boolean[] col_type;
    private Map<String, Integer> nameMap;
    
    private final boolean multiThread = true;
    
    public RandomForest(Table tb) {
        dts = new ArrayList<DecisionTree>();
        this.tb = tb;
        col_count = tb.getColCount();
        row_count = tb.getRowCount();
        col_name = tb.getColName();
        col_type = tb.getColType();
        nameMap = new HashMap<String, Integer>();
        for (int i = 0; i < col_name.length; i++) 
            nameMap.put(col_name[i], i);
    }
    
    public Table rowRandomize(Table tb) {
        Table randomTb = new Table(tb.getColCount(), tb.getColName(), tb.getColType());
        Random r = new Random();
        for (int i = 0; i < row_count; i++) {
            int index = r.nextInt(row_count);
            randomTb.addRow(tb.getRow(index));
        }
        return randomTb;
    }
    
    public Table colRandomize(Table tb) {
        int oldSize = tb.getColCount(), newSize = (oldSize - 1) * 2 / 3 + 1;
        List<Integer> list = new ArrayList<Integer>();
        int[] indexes = new int[newSize];
        for (int i = 0; i < oldSize; i++) 
            list.add(i);
        Random r = new Random();
        for (int i = 0; i < newSize; i++) {
            int index = r.nextInt(list.size());
            indexes[i] = list.remove(index);
        }
        Arrays.sort(indexes);
        String[] oldNames = tb.getColName(), newNames = new String[newSize + 1];
        boolean[] oldTypes = tb.getColType(), newTypes = new boolean[newSize + 1];
        for (int i = 0; i < newSize; i++) {
            newNames[i] = oldNames[indexes[i]];
            newTypes[i] = oldTypes[indexes[i]];
        }
        newNames[newSize] = oldNames[oldSize - 1];
        newTypes[newSize] = oldTypes[oldSize - 1];
        Table randomTb = new Table(newSize + 1, newNames, newTypes);
        for (int i = 0; i < tb.getRowCount(); i++) {
            List<Object> oldRow = tb.getRow(i), newRow = new ArrayList<Object>();
            for (int j = 0; j < newSize; j++) 
                newRow.add(oldRow.get(indexes[j]));
            newRow.add(oldRow.get(oldSize - 1));
            randomTb.addRow(newRow);
        }
        return randomTb;
    }
    
    private Object lock = new Object();
    private class Worker extends Thread {
        @Override
        public void run() {
            while (dts.size() < SIZE) {
                //System.out.format("%s constructing %dth tree%n", Thread.currentThread().getName(), dts.size());
                DecisionTree dt = new DecisionTree(colRandomize(rowRandomize(tb)), nameMap);
                synchronized (lock) {
                    if (dts.size() < SIZE) {
                        dts.add(dt);
                        if (dts.size() % 10 == 0) 
                            System.out.format("%d trees completed%n", dts.size());
                    }
                }
            }
        }
    }
    
    public void construct() {
        if (multiThread) {
            for (int i = 0; i < 4; i++) {
                Worker w = new Worker();
                w.start();
            }
            while (dts.size() < SIZE) {
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        else {
            while (dts.size() < SIZE) {
                dts.add(new DecisionTree(colRandomize(rowRandomize(tb)), nameMap));
                if (dts.size() % 10 == 0)
                    System.out.format("%d trees completed%n", dts.size());
            }
        }
        System.out.format("Construction done, %d trees built%n", dts.size());
    }
    
    public void validateAll() {
        int total = 0, correct = 0;
        for (int i = 0; i < dts.size(); i++) {
            total += row_count;
            correct += RandomForest.validate(dts.get(i), tb, row_count);
        }
        System.out.format("%d predicts, %d correct%n", total, correct);
    }
    
    public static int validate(DecisionTree dt, Table tb, int testSize) {
        int colCount = tb.getColCount(), rowCount = tb.getRowCount(), correct = 0;
        List<Integer> list = new ArrayList<Integer>();
        int[] indexes = new int[testSize];
        for (int i = 0; i < rowCount; i++) 
            list.add(i);
        Random r = new Random();
        for (int i = 0; i < testSize; i++) {
            int index = r.nextInt(list.size());
            indexes[i] = list.remove(index);
        }
        for (int i = 0; i < testSize; i++) {
            List<Object> row = tb.getRow(indexes[i]);
            Double res = dt.getCategory(row);
            if (res.equals(row.get(colCount - 1))) 
                correct++;
            else {
                //tb.printRow(indexes[i]);
            }
        }
        System.out.format("Total test: %d, correct: %d%n", testSize, correct);
        return correct;
    }
    
    public void predict(List<Object> row) {
        Map<Double, Integer> map = new HashMap<Double, Integer>();
        for (int i = 0; i < SIZE; i++) {
            Double d = dts.get(i).getCategory(row);
            if (map.containsKey(d)) 
                map.put(d, map.get(d) + 1);
            else
                map.put(d, 1);
        }
        System.out.format("%d categories in total%n", map.size());
        for (Double d : map.keySet()) 
            System.out.format("Category: %.2f, count: %d%n", d, map.get(d));
    }
}