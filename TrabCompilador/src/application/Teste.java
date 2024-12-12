package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import analisadorLexico.Token;
import analisadorLexico.analiseLexica;
import analisadorSintatico.AnaliseSintatica;
import analisadorSintatico.NodoAST;
import analisadorSintatico.SyntaxException;

public class Teste {
    public static void main(String[] args) throws SyntaxException {
        String caminhoArquivo = "D:\\Faculdade\\teste.txt";

        try {
            String codigoFonte = new String(Files.readAllBytes(Paths.get(caminhoArquivo)));

            analiseLexica analisadorLexico = new analiseLexica(codigoFonte);
            List<Token> tokens = analisadorLexico.analisar();

            System.out.println("Tokens encontrados:");
            for (Token token : tokens) {
                System.out.println(token);
            }

            AnaliseSintatica analisadorSintatico = new AnaliseSintatica(tokens);
            NodoAST arvore = analisadorSintatico.analisarPrograma();
            imprimirAST(arvore, 0);

            System.out.println("Análise sintática concluída com sucesso!");


        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (SyntaxException e) {
            System.out.println("Erro de análise sintática: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }
    
    private static void imprimirAST(NodoAST nodo, int nivel) {
        for (int i = 0; i < nivel; i++) System.out.print("  ");
        System.out.println(nodo.getTipo() + (nodo.getValor() != null ? " (" + nodo.getValor() + ")" : ""));
        for (NodoAST filho : nodo.getFilhos()) {
            imprimirAST(filho, nivel + 1);
        }
    }
}