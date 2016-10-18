import java.util.*;

public class RandomForest {
    
    private List<DecisionTree> dts;
    private Table tb;
    private Map<String, Integer> nameMap;
    
    public RandomForest(Table tb) {
        dts = new ArrayList<DecisionTree>();
        this.tb = tb;
        String[] names = tb.getColName();
        nameMap = new HashMap<String, Integer>();
        for (int i = 0; i < names.length; i++) 
            nameMap.put(names[i], i);
    }
    
    public void prediction() {
    
    }
}