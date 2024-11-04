package test.entities;

import org.junit.jupiter.api.Test;

import entities.TreeToString;
import entities.ExprNode;
import entities.OpNode;
import entities.NumNode;

import static org.junit.jupiter.api.Assertions.*;

class TreeToStringTest {

    //Verifica um nó de número único
    @Test
    void testSingleNumNode() {
        ExprNode node = new NumNode(5);
        String result = TreeToString.toString(node);
        assertEquals("5", result);
    }

    //Verifica uma adição simples
    @Test
    void testSimpleAdditionTree() {
        ExprNode node = new OpNode(new NumNode(3), new NumNode(2), "+");
        String result = TreeToString.toString(node);
        assertEquals("(3 + 2)", result);
    }

    //Verifica uma subtração simples
    @Test
    void testSimpleSubtractionTree() {
        ExprNode node = new OpNode(new NumNode(4), new NumNode(10), "-");
        String result = TreeToString.toString(node);
        assertEquals("(4 - 10)", result);
    }

    //Verifica uma multiplicação
    @Test
    void testMultiplicationTree() {
        ExprNode node = new OpNode(new NumNode(7), new NumNode(6), "*");
        String result = TreeToString.toString(node);
        assertEquals("(7 * 6)", result);
    }

    //Verifica uma divisão
    @Test
    void testDivisionTree() {
        ExprNode node = new OpNode(new NumNode(2), new NumNode(8), "/");
        String result = TreeToString.toString(node);
        assertEquals("(2 / 8)", result);
    }

    //Verifica uma expressão complexa
    @Test
    void testComplexExpressionTree() {
        ExprNode node = new OpNode(new OpNode(new NumNode(3),
		        new NumNode(2),
		        "*"),
                new NumNode(1),
                "+");
        String result = TreeToString.toString(node);
        assertEquals("((3 * 2) + 1)", result);
    }

    @Test
    void testExpressionWithUnaryMinus() {
        ExprNode node = new OpNode(new NumNode(5), new NumNode(0), "-");
        String result = TreeToString.toString(node);
        assertEquals("(5 - 0)", result);
    }

    //Verifica uma expressão com parênteses
    @Test
    void testNestedParenthesesTree() {
        ExprNode node = new OpNode(new NumNode(7),
                new OpNode(new OpNode(new NumNode(3),
				        new NumNode(2),
				        "*"),
                        new NumNode(4),
                        "+"),
                "-");
        String result = TreeToString.toString(node);
        assertEquals("(7 - ((3 * 2) + 4))", result);
    }

    //Verifica uma expressão com parênteses aninhados
    @Test
    void testDeeplyNestedParenthesesTree() {
        ExprNode node = new OpNode(new OpNode(new NumNode(1),
                new NumNode(2),
                "+"),
                new OpNode(new NumNode(3),
                        new NumNode(4),
                        "*"),
                "-");
        String result = TreeToString.toString(node);
        assertEquals("((1 + 2) - (3 * 4))", result);
    }
}
