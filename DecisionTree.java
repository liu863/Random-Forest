import java.util.*;
import java.lang.*;

public class DecisionTree {
    
    private class Node {
        boolean isLeaf;
        Object value;
        int category;
        int column;
        Node left, right;
    }
    
    Node root;
    boolean[] col_type;
    String[] col_name;
    
    public DecisionTree(Table tb) {
    
    }
    
    public int getCategory(List<Object> row) {
        return 0;
    }
    
    
    
    public void printTree() {
        printNode(0, root);
    }
    
    private void printNode(int indent, Node node) {
        if (node == null) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) 
            sb.append(' ');
        if (node.isLeaf) {
            sb.append("C: ");
            sb.append(node.category);
        }
        else {
            sb.append(col_name + ": ");
            sb.append(node.value);
        }
        System.out.println(sb.toString());
        printNode(indent + 4, node.left);
        printNode(indent + 4, node.right);
    }
}