package entities;

import java.util.ArrayList;
import java.util.List;

public class Evaluator {
    private List<String> evaluationSteps = new ArrayList<>();

    public List<String> getEvaluationSteps() {
        return evaluationSteps;
    }

    public int evaluateExpr(ExprNode root) {
        evaluationSteps.clear();
        return evaluate(root);
    }

    private int evaluate(ExprNode node) {
        if (node instanceof NumNode) {
            return ((NumNode) node).getValue();
        } else if (node instanceof OpNode) {
            OpNode opNode = (OpNode) node;
            int leftValue = evaluate(opNode.getLeft());
            int rightValue = evaluate(opNode.getRight());
            int result = 0;

            switch (opNode.getOperator()) {
                case "+":
                    result = leftValue + rightValue;
                    break;
                case "-":
                    result = leftValue - rightValue;
                    break;
                case "*":
                    result = leftValue * rightValue;
                    break;
                case "/":
                    if (rightValue != 0) {
                        result = leftValue / rightValue;
                    } else {
                        throw new ArithmeticException("Division by zero");
                    }
                    break;
            }

            // Adiciona a expressão intermediária à lista de passos
            String resultExpression = String.format("%d %s %d = %d", leftValue, opNode.getOperator(), rightValue, result);
            evaluationSteps.add(resultExpression);

            return result;
        }
        return 0;
    }
}
