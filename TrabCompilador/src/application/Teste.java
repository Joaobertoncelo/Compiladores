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
import analisadorSemantico.AnaliseSemantica;
import analisadorSemantico.SemanticException;

public class Teste {
    public static void main(String[] args) {
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

            System.out.println("Análise sintática concluída com sucesso!");

            
            imprimirAST(arvore, 0);

            
            AnaliseSemantica analisadorSemantico = new AnaliseSemantica();
            analisadorSemantico.analisar(arvore);

            System.out.println("Análise semântica concluída com sucesso!");

            
            analisadorSemantico.imprimirTabelaSimbolos();

           
            exportarASTParaDot(arvore);

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (SyntaxException e) {
            System.out.println("Erro de análise sintática: " + e.getMessage());
        } catch (SemanticException e) {
            System.out.println("Erro de análise semântica: " + e.getMessage());
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

    
    private static void exportarASTParaDot(NodoAST nodo) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph AST {\n");
        exportarASTParaDotRecursivo(nodo, sb);
        sb.append("}\n");
        try {
            Files.write(Paths.get("ast.dot"), sb.toString().getBytes());
            System.out.println("AST exportada para ast.dot");
        } catch (IOException e) {
            System.out.println("Erro ao exportar AST: " + e.getMessage());
        }
    }

    private static void exportarASTParaDotRecursivo(NodoAST nodo, StringBuilder sb) {
        String nodeId = "node" + System.identityHashCode(nodo);
        sb.append(nodeId).append(" [label=\"").append(nodo.getTipo());
        if (nodo.getValor() != null) {
            sb.append(" (").append(nodo.getValor()).append(")");
        }
        sb.append("\"];\n");
        for (NodoAST filho : nodo.getFilhos()) {
            String childId = "node" + System.identityHashCode(filho);
            sb.append(nodeId).append(" -> ").append(childId).append(";\n");
            exportarASTParaDotRecursivo(filho, sb);
        }
    }
}
