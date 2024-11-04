package entities;

public class TreeToString {
    public static String toString(ExprNode node) {
        if (node instanceof NumNode) {
            return node.toString();
        } else if (node instanceof OpNode) {
            OpNode opNode = (OpNode) node;
            return "(" + toString(opNode.getLeft()) + " " + opNode.getOperator() + " " + toString(opNode.getRight()) + ")";
        }
        return "";
    }
}
