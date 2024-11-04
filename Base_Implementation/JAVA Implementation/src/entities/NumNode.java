package entities;

public class NumNode extends ExprNode {
    private final int value;

    public NumNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
