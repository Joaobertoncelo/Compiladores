package application;

import entities.Evaluator;
import entities.ExprNode;
import entities.Lexer;
import entities.Parser;
import entities.TreeToString;

import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("Entre com a expressão: ");
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Entrada vazia. Por favor, insira uma expressão.");
                return;
            }

            // Verificar se a entrada possui parênteses balanceados
            if (!areParenthesesBalanced(input)) {
                System.out.println("Erro: Parênteses não estão balanceados.");
                return;
            }

            Lexer lexer = new Lexer();
            List<String> tokens = lexer.tokenize(input);

            // Verificar se a lista de tokens não está vazia
            if (tokens.isEmpty()) {
                System.out.println("Erro: Expressão inválida.");
                return;
            }

            System.out.println("Tokens: " + tokens);

            Parser parser = new Parser();
            ExprNode root;
            try {
                root = parser.parse(tokens);
            } catch (Exception e) {
                System.out.println("Erro ao analisar a expressão: " + e.getMessage());
                return;
            }

            System.out.println("Árvore Inicial: " + TreeToString.toString(root));

            Evaluator evaluator = new Evaluator();
            int result;
            try {
                result = evaluator.evaluateExpr(root);
            } catch (ArithmeticException e) {
                System.out.println("Erro na avaliação: " + e.getMessage());
                return;
            }

            System.out.println("Passos de Avaliação:");
            for (String step : evaluator.getEvaluationSteps()) {
                System.out.println(step);
            }

            System.out.println("Resultado Final: " + result);

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    // Função para verificar se os parênteses estão balanceados
    private static boolean areParenthesesBalanced(String expression) {
        int balance = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
            }
            if (balance < 0) {
                return false; // Mais parênteses fechando do que abrindo
            }
        }
        return balance == 0; // Parênteses devem estar balanceados
    }
}
