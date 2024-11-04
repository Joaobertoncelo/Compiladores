package test.application;

import org.junit.jupiter.api.*;

import application.Program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ProgramTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    //Testa uma expressão válida sem parênteses
    @Test
    void testValidExpression() {
        String input = "1 + 2 * 3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Program.main(new String[]{});

        String expectedOutput = 
            "Entre com a expressão: \n" +
            "Tokens: [1, +, 2, *, 3]\n" +
            "Árvore Inicial: (1 + (2 * 3))\n" +
            "Passos de Avaliação:\n" +
            "2 * 3 = 6\n" +
            "1 + 6 = 7\n" +
            "Resultado Final: 7";


            assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    //Testa uma expressão válida com parênteses
    @Test
    void testExpressionWithParentheses() {
        String input = "(1 + 2) * 3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Program.main(new String[]{});

        String expectedOutput = 
            "Entre com a expressão: \n" +
            "Tokens: [(, 1, +, 2, ), *, 3]\n" +
            "Árvore Inicial: ((1 + 2) * 3)\n" +
            "Passos de Avaliação:\n" +
            "1 + 2 = 3\n" +
            "3 * 3 = 9\n" +
            "Resultado Final: 9\n";

        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    //Testa uma expressão faltando fechar parênteses
    @Test
    void testUnbalancedParentheses() {
        String input = "1 + (2 * 3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Program.main(new String[]{});

        String expectedOutput = 
            "Entre com a expressão: \n" +
            "Erro: Parênteses não estão balanceados.\n";

        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    //Testa uma expressão inválida(divisão por zero)
    @Test
    void testDivisionByZero() {
        String input = "10 / 0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Program.main(new String[]{});

        String expectedOutput = 
            "Entre com a expressão: \n" +
            "Tokens: [10, /, 0]\n" +
            "Árvore Inicial: (10 / 0)\n" +
            "Erro na avaliação: Division by zero\n";

        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    //Testa uma expressão inválida com operador vazio
    @Test
    void testEmptyInput() {
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Program.main(new String[]{});

        String expectedOutput = 
            "Entre com a expressão: \n" +
            "Entrada vazia. Por favor, insira uma expressão.\n";

        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    //Testa uma expressão inválida faltando operador
    @Test
    void testInvalidExpression() {
        String input = "1 + \n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Program.main(new String[]{});

        String expectedOutput = 
            "Entre com a expressão: \n" +
            "Tokens: [1, +]\n" +
            "Erro ao analisar a expressão: Index: 2, Size: 2\n";

        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    //Testa a expressão (2 - 65 - (-24 + -97) * -5 * -61) * (-41 + 85 * 9 * -92 * (75 - 18))
    @Test
    void testExtremelyComplexExpression(){
        String input = "(2 - 65 - (-24 + -97) * -5 * -61) * (-41 + 85 * 9 * -92 * (75 - 18))\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Program.main(new String[]{});

        String expectedOutput = 
            "Entre com a expressão: \n" +
            "Tokens: [(, 2, -, 65, -, (, -, 24, +, -, 97, ), *, -, 5, *, -, 61, ), *, (, -, 41, +, 85, *, 9, *, -, 92, *, (, 75, -, 18, ), )]\n" +
            "Árvore Inicial: (((2 - 65) - ((((0 - 24) + (0 - 97)) * (0 - 5)) * (0 - 61))) * ((0 - 41) + (((85 * 9) * (0 - 92)) * (75 - 18))))\n" +
            "Passos de Avaliação:\n" +
            "2 - 65 = -63\n" +
            "0 - 24 = -24\n" +
            "0 - 97 = -97\n" +
            "-24 + -97 = -121\n" +
            "0 - 5 = -5\n" +
            "-121 * -5 = 605\n" +
            "0 - 61 = -61\n" +
            "605 * -61 = -36905\n" +
            "-63 - -36905 = 36842\n" +
            "0 - 41 = -41\n" +
            "85 * 9 = 765\n" +
            "0 - 92 = -92\n" +
            "765 * -92 = -70380\n" +
            "75 - 18 = 57\n" +
            "-70380 * 57 = -4011660\n" +
            "-41 + -4011660 = -4011701\n" +
            "36842 * -4011701 = -147799088242\n" +
            "Resultado Final: -147799088242\n";//Pelo tamanho, o resultado acaba saindo errado, o resultado dos testes é -1770200178

        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }
}
