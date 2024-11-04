package entities;

import java.util.List;

public class Parser {
    private int index = 0;
    private List<String> tokens;

    public ExprNode parse(List<String> tokens) {
        this.tokens = tokens;
        index = 0;
        return parseAddSubtract();
    }

    private ExprNode parseAddSubtract() {
        ExprNode left = parseMultiplyDivide();
        while (index < tokens.size() && (tokens.get(index).equals("+") || tokens.get(index).equals("-"))) {
            String operator = tokens.get(index++);
            ExprNode right = parseMultiplyDivide();
            left = new OpNode(left, right, operator);
        }
        return left;
    }

    private ExprNode parseMultiplyDivide() {
        ExprNode left = parseUnary();
        while (index < tokens.size() && (tokens.get(index).equals("*") || tokens.get(index).equals("/"))) {
            String operator = tokens.get(index++);
            ExprNode right = parseUnary();
            left = new OpNode(left, right, operator);
        }
        return left;
    }

    private ExprNode parseUnary() {
        if (index < tokens.size() && tokens.get(index).equals("-")) {
            index++;
            ExprNode expr = parsePrimary();
            return new OpNode(new NumNode(0), expr, "-");
        }
        return parsePrimary();
    }

    private ExprNode parsePrimary() {
        String token = tokens.get(index++);
        if (token.equals("(")) {
            ExprNode expr = parseAddSubtract();
            index++; 
            return expr;
        } else {
            return new NumNode(Integer.parseInt(token));
        }
    }
}
