import java.util.*;
import java.lang.*;

public class DecisionTree {
    
    private class Node {
        public boolean isLeaf;
        public Object value; //store the selection value or category
        public int column, err_index;
        public Node left, right;
        public Node() {
            isLeaf = true;
            err_index = -1; //a value that greater than -1 indicates error node
            left = null;
            right = null;
        }
    }
    
    private Node root;
    private int col_count, err_count; //count and mark index of error node
    private String[] col_name; //column names of current table
    private boolean[] col_type; //column types of current table
    /*store the column name and corresponding column index
    of the original(unrandomized) table*/
    Map<String, Integer> nameMap;
    
    public DecisionTree(Table tb, Map<String, Integer> nameMap) {
        err_count = 0;
        col_count = tb.getColCount();
        col_name = new String[col_count];
        col_type = new boolean[col_count];
        System.arraycopy(tb.getColName(), 0, col_name, 0, col_count);
        System.arraycopy(tb.getColType(), 0, col_type, 0, col_count);
        this.nameMap = nameMap;
        root = buildTree(tb);
    }
    
    /**
     * This function constructs a decision tree according to a given table.
     * @param tb The table that is used to build the decision tree
     *           This table must contain at least one row of data
     *           All information in this table will be stroed in the tree
     * @return Node The root of the decision tree.
     */
    private Node buildTree(Table tb) {
        //System.out.println("Start building a node");
        //tb.printTable();
        Node node = new Node();
        int col = tb.splittable(), col_count = tb.getColCount();
        if (col == -1) {
            //single class
            node.value = tb.getRow(0).get(col_count - 1);
        }
        else if (col == -2) {
            //single value
            //find the categroy with most occurrence
            Map<Double, Integer> map = new HashMap<Double, Integer>();
            Double category = null;
            for (int i = 0; i < tb.getRowCount(); i++) {
                category = (Double)tb.getRow(i).get(col_count - 1);
                if (map.containsKey(category)) {
                    map.put(category, map.get(category) + 1);
                }
                else {
                    map.put(category, 1);
                }
            }
            int max = map.get(category);
            for (Double d : map.keySet()) {
                if (map.get(d) > max) {
                    category = d;
                    max = map.get(d);
                }
            }
            node.value = (Object)category;
        }
        else {
            //split
            node.isLeaf = false;
            int[] ind = new int[1]; //store split column index as return value
            Object[] val = new Object[1]; //store split value as return value
            Table[] tbs = tb.split(ind, val);
            node.column = ind[0];
            node.value = val[0];
            node.left = buildTree(tbs[0]);
            node.right = buildTree(tbs[1]);
        }
        //System.out.format("Finished builder a %s node%n", node.isLeaf ? "leaf" : "root");
        return node;
    }
    
    /**
     * Public interface to get category of a row of data.
     * @param row Searching conditions
     * @return Double Searching result of this dicision tree,
     *                return null if an error node if found.
     */
    public Double getCategory(List<Object> row) {
        return getCategory(root, row);
    }
    
    /**
     * 
     * @param node Root of the decision tree to search.
     * @param row Searching conditions
     * @return Double Category value of leaf node or the 
     *                result of recursive call of child node,
     *                return null if reaching an error node.
     */
    private Double getCategory(Node node, List<Object> row) {
        if (node.isLeaf) {
            if (node.err_index >= 0)
                return null;
            else
                return (Double)node.value;
        }
        else {
            int indexInLargeTable = nameMap.get(col_name[node.column]);
            if (col_type[node.column]) {
                //string column
                if (node.value.equals(row.get(indexInLargeTable))) {
                    return getCategory(node.left, row);
                }
                else {
                    return getCategory(node.right, row);
                }
            }
            else {
                //double column
                if ((Double)row.get(indexInLargeTable) < (Double)node.value) {
                    return getCategory(node.left, row);
                }
                else {
                    return getCategory(node.right, row);
                }
            }
        }
    }
    
    /**
     * Public interface to print entire decision tree.
     */
    public void printTree() {
        printNode(0, root);
    }
    
    /**
     * Print a certain node.
     * @param indent Number of space to print, indicates the level of node
     * @param node Node to print
     */
    private void printNode(int indent, Node node) {
        if (node == null) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) 
            sb.append(' ');
        if (node.isLeaf) {
            sb.append("C: ");
            sb.append(node.value);
        }
        else {
            sb.append(col_name[node.column] + ": ");
            sb.append(node.value);
        }
        System.out.println(sb.toString());
        printNode(indent + 0, node.left);
        printNode(indent + 0, node.right);
    }
}
