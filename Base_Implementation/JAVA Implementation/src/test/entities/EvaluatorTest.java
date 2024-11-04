package test.entities;

import org.junit.jupiter.api.Test;

import entities.Evaluator;
import entities.ExprNode;
import entities.OpNode;
import entities.NumNode;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EvaluatorTest {

	//Verifica uma adição
    @Test
    void testEvaluateSimpleAddition() {
        ExprNode expr = new OpNode(new NumNode(2), new NumNode(1), "+");
        Evaluator evaluator = new Evaluator();

        int result = evaluator.evaluateExpr(expr);
        List<String> steps = evaluator.getEvaluationSteps();

        assertEquals(3, result);
        assertEquals(1, steps.size());
        assertEquals("2 + 1 = 3", steps.get(0));
    }

    //Verifica uma subtração
    @Test
    void testEvaluateSimpleSubtraction() {
        ExprNode expr = new OpNode(new NumNode(3), new NumNode(5), "-");
        Evaluator evaluator = new Evaluator();

        int result = evaluator.evaluateExpr(expr);
        List<String> steps = evaluator.getEvaluationSteps();

        assertEquals(-2, result);
        assertEquals(1, steps.size());
        assertEquals("3 - 5 = -2", steps.get(0));
    }

    //verifica uma multiplicação
    @Test
    void testEvaluateMultiplication() {
        ExprNode expr = new OpNode(new NumNode(5), new NumNode(4), "*");
        Evaluator evaluator = new Evaluator();

        int result = evaluator.evaluateExpr(expr);
        List<String> steps = evaluator.getEvaluationSteps();

        assertEquals(20, result);
        assertEquals(1, steps.size());
        assertEquals("5 * 4 = 20", steps.get(0));
    }

    //verifica uma divisão
    @Test
    void testEvaluateDivision() {
        ExprNode expr = new OpNode(new NumNode(2), new NumNode(10), "/");
        Evaluator evaluator = new Evaluator();

        int result = evaluator.evaluateExpr(expr);
        List<String> steps = evaluator.getEvaluationSteps();

        assertEquals(0, result);
        assertEquals(1, steps.size());
        assertEquals("2 / 10 = 0", steps.get(0)); //Por motivo de tipagem, a divisão de 2 por 10 é 0
    }

    //verifica uma expressão com multiplicação e adição
    @Test
    void testEvaluateComplexExpression() {
        ExprNode expr = new OpNode(new NumNode(4), 
            new OpNode(new NumNode(3), new NumNode(2), "+"), 
            "*");
        Evaluator evaluator = new Evaluator();

        int result = evaluator.evaluateExpr(expr);
        List<String> steps = evaluator.getEvaluationSteps();

        assertEquals(20, result);
        assertEquals(2, steps.size());
        assertEquals("3 + 2 = 5", steps.get(0));
        assertEquals("4 * 5 = 20", steps.get(1));
    }

    //Verifica se não há divisão por zero
    @Test
    void testDivisionByZero() {
        ExprNode expr = new OpNode(new NumNode(10), new NumNode(0), "/");
        Evaluator evaluator = new Evaluator();

        Exception exception = assertThrows(ArithmeticException.class, () -> {
            evaluator.evaluateExpr(expr);
        });

        assertEquals("Division by zero", exception.getMessage());
    }

    //Verifica 58 - -8 * (58 + 31) / -14(colocado uma divisão para maior completude)
    @Test
    void testEvaluateComplexExpression2() {
        ExprNode expr = 
        new OpNode(new NumNode(58), 
            new OpNode(new NumNode(-8), 
                new OpNode(new NumNode(58), new NumNode(31), "+"), 
                "*"), 
            "-");
        Evaluator evaluator = new Evaluator();

        int result = evaluator.evaluateExpr(expr);
        List<String> steps = evaluator.getEvaluationSteps();

        assertEquals(770, result);
        assertEquals(3, steps.size());
        assertEquals("58 + 31 = 89", steps.get(0));
        assertEquals("-8 * 89 = -712", steps.get(1));
        assertEquals("58 - -712 = 770", steps.get(2));
    }
}
