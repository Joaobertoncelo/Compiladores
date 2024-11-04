package test.entities;

import org.junit.jupiter.api.Test;

import entities.Lexer;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    //Verifica uma expressão simples
    @Test
    void testTokenizeSimpleExpression() {
        Lexer lexer = new Lexer();
        List<String> tokens = lexer.tokenize("2 + 3");

        assertEquals(3, tokens.size());
        assertEquals("2", tokens.get(0));
        assertEquals("+", tokens.get(1));
        assertEquals("3", tokens.get(2));
    }

    //Verifica uma expressão com parênteses
    @Test
    void testTokenizeWithParentheses() {
        Lexer lexer = new Lexer();
        List<String> tokens = lexer.tokenize("(2 + 3) * 4");

        assertEquals(7, tokens.size());
        assertEquals("(", tokens.get(0));
        assertEquals("2", tokens.get(1));
        assertEquals("+", tokens.get(2));
        assertEquals("3", tokens.get(3));
        assertEquals(")", tokens.get(4));
        assertEquals("*", tokens.get(5));
        assertEquals("4", tokens.get(6));
    }

    //Verifica uma expressão com espaços em branco
    @Test
    void testTokenizeWithWhitespace() {
        Lexer lexer = new Lexer();
        List<String> tokens = lexer.tokenize("  10 +  20 ");

        assertEquals(3, tokens.size());
        assertEquals("10", tokens.get(0));
        assertEquals("+", tokens.get(1));
        assertEquals("20", tokens.get(2));
    }

    //Verifica uma expressão com múltiplos operadores
    @Test
    void testTokenizeWithMultipleOperators() {
        Lexer lexer = new Lexer();
        List<String> tokens = lexer.tokenize("4*5-3/2");

        assertEquals(7, tokens.size());
        assertEquals("4", tokens.get(0));
        assertEquals("*", tokens.get(1));
        assertEquals("5", tokens.get(2));
        assertEquals("-", tokens.get(3));
        assertEquals("3", tokens.get(4));
        assertEquals("/", tokens.get(5));
        assertEquals("2", tokens.get(6));
    }

    //Verifica uma expressão com um operador inválido
    @Test
    void testTokenizeInvalidCharacter() {
        Lexer lexer = new Lexer();
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            lexer.tokenize("2 + 3$");
        });

        assertEquals("Caractere inválido: $", exception.getMessage());
    }

    //Verifica a expressão (2 - 65 - (-24 + -97) * -5 * -61) * (-41 + 85 * 9 * -92 * (75 - 18))
    @Test
    void testTokenizeExtremelyComplexExpression(){
        Lexer lexer = new Lexer();
        List<String> tokens = lexer.tokenize("(2 - 65 - (-24 + -97) * -5 * -61) * (-41 + 85 * 9 * -92 * (75 - 18))");

        assertEquals(37, tokens.size());
        assertEquals("(", tokens.get(0));
        assertEquals("2", tokens.get(1));
        assertEquals("-", tokens.get(2));
        assertEquals("65", tokens.get(3));
        assertEquals("-", tokens.get(4));
        assertEquals("(", tokens.get(5));
        assertEquals("-", tokens.get(6));
        assertEquals("24", tokens.get(7));
        assertEquals("+", tokens.get(8));
        assertEquals("-", tokens.get(9));
        assertEquals("97", tokens.get(10));
        assertEquals(")", tokens.get(11));
        assertEquals("*", tokens.get(12));
        assertEquals("-", tokens.get(13));
        assertEquals("5", tokens.get(14));
        assertEquals("*", tokens.get(15));
        assertEquals("-", tokens.get(16));
        assertEquals("61", tokens.get(17));
        assertEquals(")", tokens.get(18));
        assertEquals("*", tokens.get(19));
        assertEquals("(", tokens.get(20));
        assertEquals("-", tokens.get(21));
        assertEquals("41", tokens.get(22));
        assertEquals("+", tokens.get(23));
        assertEquals("85", tokens.get(24));
        assertEquals("*", tokens.get(25));
        assertEquals("9", tokens.get(26));
        assertEquals("*", tokens.get(27));
        assertEquals("-", tokens.get(28));
        assertEquals("92", tokens.get(29));
        assertEquals("*", tokens.get(30));
        assertEquals("(", tokens.get(31));
        assertEquals("75", tokens.get(32));
        assertEquals("-", tokens.get(33));
        assertEquals("18", tokens.get(34));
        assertEquals(")", tokens.get(35));
        assertEquals(")", tokens.get(36));
    }
}
