package analisadorLexico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class analiseLexica {
    private static final String PADRAO_IDENTIFICADOR = "[a-zA-Z_][a-zA-Z0-9_]*";
    private static final String PADRAO_NUMERO = "\\d+(\\.\\d+)?";
    private static final String PADRAO_STRING = "\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"";
    private static final String PADRAO_COMENTARIO_LINHA = "//[^\n]*";
    private static final String PADRAO_COMENTARIO_BLOCO = "/\\*[\\s\\S]*?\\*/";
    private static final String PADRAO_OPERADOR = "[+\\-*/%&|^!~]";  
    private static final String PADRAO_OPERADOR_COMPARACAO = "(==|!=|>=|<=|>|<)";  
    private static final String PADRAO_OPERADOR_INCREMENTO_DECREMENTO = "(\\+\\+|--)"; 
    private static final String PADRAO_PONTUACAO = "[;,\\(\\)\\[\\]\\{\\}]";  
    private static final String PADRAO_PALAVRA_CHAVE = "(int|float|double|char|troll|bang|molotov|rush|smoke|baiter|baita|round|backup|antrush|setup|console|overwatch)";
    private static final String PADRAO_CARACTERES_INVALIDOS = "[^a-zA-Z0-9_+\\-*/%&|^!~;,\\(\\)\\[\\]\\{\\}\"]";  
    private static final int LIMITE_TAMANHO_IDENTIFICADOR = 10;

    private String codigoFonte;

    public analiseLexica(String caminhoArquivo) {
        this.codigoFonte = lerArquivo(caminhoArquivo);
    }

    private String lerArquivo(String caminhoArquivo) {
        StringBuilder conteudo = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                conteudo.append(linha).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        return conteudo.toString();
    }

    public List<Token> analisar() {
        List<Token> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile(
                PADRAO_COMENTARIO_LINHA + "|" +
                PADRAO_COMENTARIO_BLOCO + "|" +
                PADRAO_STRING + "|" +
                PADRAO_IDENTIFICADOR + "|" +
                PADRAO_NUMERO + "|" +
                PADRAO_OPERADOR_COMPARACAO + "|" +
                PADRAO_OPERADOR_INCREMENTO_DECREMENTO + "|" +
                PADRAO_OPERADOR + "|" +
                PADRAO_PONTUACAO + "|" +
                PADRAO_PALAVRA_CHAVE);
        Matcher matcher = pattern.matcher(codigoFonte);

        while (matcher.find()) {
            String valor = matcher.group().trim();
            Token token = identificarToken(valor);
            if (token != null) {
                tokens.add(token);
            }
        }
        return tokens;
    }


    private Token identificarToken(String valor) {
       
        if (temCaracteresInvalidos(valor)) {
            return new Token(TokenType.ERRO, valor);  
        }

        if (isPalavraChave(valor)) {
            return new Token(TokenType.PALAVRA_CHAVE, valor);
        }


        if (ehIdentificador(valor)) {
           
            if (isPalavraChave(valor)) {
                return new Token(TokenType.ERRO, valor); 
                }
            return new Token(TokenType.IDENTIFICADOR, valor);
        }

       
        if (valor.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            if (isPalavraChave(valor)) {
                return new Token(TokenType.ERRO, valor); 
            }
        }

        if (valor.matches(PADRAO_NUMERO)) {
            return new Token(TokenType.NUMERO, valor);
        }

        if (valor.matches(PADRAO_OPERADOR_COMPARACAO)) {
            return new Token(TokenType.OPERADOR, valor);
        }
        if (valor.matches("\\+\\+|--")) {
            return new Token(TokenType.OPERADOR, valor);
        }

        if (valor.matches(PADRAO_OPERADOR)) {
            return new Token(TokenType.OPERADOR, valor);
        }
        if (valor.matches(PADRAO_PONTUACAO)) {
            return new Token(TokenType.PONTUACAO, valor);
        }

        
        return new Token(TokenType.ERRO, valor);  
    }

    private boolean ehIdentificador(String valor) {
        return valor.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    private boolean temCaracteresInvalidos(String valor) {
        return valor.matches(PADRAO_CARACTERES_INVALIDOS);
    }

    private boolean isPalavraChave(String valor) {
        return Arrays.asList("int", "float", "double", "char", "troll", "bang", "molotov", "rush", "smoke", "baiter", "baita", "round", "backup", "antrush", "setup", "console", "overwatch").contains(valor);
    }
}
