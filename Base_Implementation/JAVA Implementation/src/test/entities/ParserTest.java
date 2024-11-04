package test.entities;

import org.junit.jupiter.api.Test;

import entities.ExprNode;
import entities.OpNode;
import entities.Parser;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    //Verifica uma adição simples
    @Test
    void testParseSimpleAddition() {
        Parser parser = new Parser();
        List<String> tokens = Arrays.asList("2", "+", "3");

        ExprNode result = parser.parse(tokens);

        assertNotNull(result);
        assertTrue(result instanceof OpNode);
        assertEquals("(2 + 3)", result.toString());
    }

    //Verifica uma subtração simples
    @Test
    void testParseSimpleSubtraction() {
        Parser parser = new Parser();
        List<String> tokens = Arrays.asList("5", "-", "3");

        ExprNode result = parser.parse(tokens);

        assertNotNull(result);
        assertTrue(result instanceof OpNode);
        assertEquals("(5 - 3)", result.toString());
    }

    //Verifica uma multiplicação
    @Test
    void testParseMultiplication() {
        Parser parser = new Parser();
        List<String> tokens = Arrays.asList("4", "*", "6");

        ExprNode result = parser.parse(tokens);

        assertNotNull(result);
        assertTrue(result instanceof OpNode);
        assertEquals("(4 * 6)", result.toString());
    }

    //Verifica uma divisão
    @Test
    void testParseDivision() {
        Parser parser = new Parser();
        List<String> tokens = Arrays.asList("8", "/", "2");

        ExprNode result = parser.parse(tokens);

        assertNotNull(result);
        assertTrue(result instanceof OpNode);
        assertEquals("(8 / 2)", result.toString());
    }

    //Verifica uma expressão composta
    @Test
    void testParseComplexExpression() {
        Parser parser = new Parser();
        List<String> tokens = Arrays.asList("2", "+", "3", "*", "4");

        ExprNode result = parser.parse(tokens);

        assertNotNull(result);
        assertTrue(result instanceof OpNode);
        assertEquals("(2 + (3 * 4))", result.toString());
    }

    //Verifica uma expressão com múltiplos operadores
    @Test
    void testParseExpressionWithParentheses() {
        Parser parser = new Parser();
        List<String> tokens = Arrays.asList("(", "2", "+", "3", ")", "*", "4");

        ExprNode result = parser.parse(tokens);

        assertNotNull(result);
        assertTrue(result instanceof OpNode);
        assertEquals("((2 + 3) * 4)", result.toString());
    }

    //Verifica uma expressão com espaços em branco
    @Test
    void testParseUnaryMinus() {
        Parser parser = new Parser();
        List<String> tokens = Arrays.asList("-", "5");

        ExprNode result = parser.parse(tokens);

        assertNotNull(result);
        assertTrue(result instanceof OpNode);
        assertEquals("(0 - 5)", result.toString());
    }

    //Verifica uma expressão com parênteses aninhados
    @Test
    void testParseNestedParentheses() {
        Parser parser = new Parser();
        List<String> tokens = Arrays.asList("(", "1", "+", "(", "2", "*", "3", ")", ")", "-", "4");

        ExprNode result = parser.parse(tokens);

        assertNotNull(result);
        assertTrue(result instanceof OpNode);
        assertEquals("((1 + (2 * 3)) - 4)", result.toString());
    }

    @Test
    void testParseExtremelyComplexExpression(){
        Parser parser = new Parser();
        List<String> tokens = Arrays.asList("(", "2", "-", "65", "-", "(", "-", "24", "+", "-", "97", ")", "*", "-", "5", "*", "-", "61", ")", "*", "(", "-", "41", "+", "85", "*", "9", "*", "-", "92", "(", "75", "-", "18", ")", ")");

        ExprNode result = parser.parse(tokens);

        assertNotNull(result);
        assertTrue(result instanceof OpNode);
        assertEquals("(((2 - 65) - ((((0 - 24) + (0 - 97)) * (0 - 5)) * (0 - 61))) * ((0 - 41) + ((85 * 9) * (0 - 92))))", result.toString());
    }
}
