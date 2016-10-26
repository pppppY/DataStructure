package tree;

/**
 * Created by py on 16-10-25.
 * binary tree node
 */
public class TreeNode {
    private TreeNode left;
    private TreeNode right;
    private char key;
    private int value;
    public TreeNode() {
    }

    public TreeNode(TreeNode left, TreeNode right,char key, int value) {
        this.left = left;
        this.right = right;
        this.value = value;
        this.key = key;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public char getKey() {
        return key;
    }

    public void setKey(char key) {
        this.key = key;
    }
}
