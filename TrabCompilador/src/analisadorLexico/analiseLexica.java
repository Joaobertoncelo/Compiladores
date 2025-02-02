package analisadorLexico;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class analiseLexica {
    private static final String PADRAO_IDENTIFICADOR = "[a-zA-Z_][a-zA-Z0-9_]*";
    private static final String PADRAO_NUMERO = "\\d+(\\.\\d+)?";
    private static final String PADRAO_STRING = "\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\""; // Strings entre aspas
    private static final String PADRAO_COMENTARIO_LINHA = "//[^\n]*"; // Comentários de linha
    private static final String PADRAO_COMENTARIO_BLOCO = "/\\*[\\s\\S]*?\\*/"; // Comentários de bloco
    private static final String PADRAO_OPERADOR = "[+\\-*/%&|^!~]"; // Operadores aritméticos e bitwise
    private static final String PADRAO_OPERADOR_COMPARACAO = "(==|!=|>=|<=|>|<)"; // Operadores de comparação
    private static final String PADRAO_PONTUACAO = "[:;,\\(\\)\\[\\]\\{\\}]"; // Pontuação
    private static final String PADRAO_PALAVRA_CHAVE = "(round|int|float|double|char|troll|bang|molotov|smoke|rush|baiter|baita|backup|antrush|setup|console|overwatch|exec)"; // Palavras-chave
    private static final String PADRAO_PARAR_NUMERO_INICIO = "\\d+[a-zA-Z_]+"; // Identificadores inválidos iniciados por números
    private static final String PADRAO_SINAL_IGUAL = "(=)";

    private String codigoFonte;

    public analiseLexica(String codigoFonte) {
        this.codigoFonte = codigoFonte;
    }

    public List<Token> analisar() {
        List<Token> tokens = new ArrayList<>();

        // Remover espaços em branco e tabs desnecessários
        codigoFonte = codigoFonte.replaceAll("[\\t\\r\\f]+", " "); // Substitui tabs e espaços extras por um espaço único

        Pattern pattern = Pattern.compile(
            PADRAO_COMENTARIO_LINHA + "|" +
            PADRAO_COMENTARIO_BLOCO + "|" +
            PADRAO_STRING + "|" +
            PADRAO_IDENTIFICADOR + "|" +
            PADRAO_PARAR_NUMERO_INICIO + "|" +
            PADRAO_NUMERO + "|" +
            PADRAO_OPERADOR_COMPARACAO + "|" +
            PADRAO_SINAL_IGUAL + "|" +
            PADRAO_OPERADOR + "|" +
            PADRAO_PONTUACAO + "|" +
            PADRAO_PALAVRA_CHAVE
        );

        Matcher matcher = pattern.matcher(codigoFonte);

        int endMatcherPos = 0;

        while (matcher.find()) {
            // Adicionar tokens válidos
            String valor = matcher.group().trim(); // Remove espaços em branco ao redor do token
            Token token = identificarToken(valor);
            if (token != null) {
                tokens.add(token);
            }
            endMatcherPos = matcher.end();
        }

        return tokens;
    }

    private Token identificarToken(String valor) {
        // Verificar se o valor é uma palavra-chave
        if (valor.matches(PADRAO_PALAVRA_CHAVE)) {
            return new Token(TokenType.PALAVRA_CHAVE, valor);
        }
        // Ignorar comentários
        else if (valor.matches(PADRAO_COMENTARIO_LINHA) || valor.matches(PADRAO_COMENTARIO_BLOCO)) {
            return null;
        }
        // Verificar se é uma string
        else if (valor.matches(PADRAO_STRING)) {
            return new Token(TokenType.STRING, valor);
        }
        // Identificadores iniciados por números (erro)
        else if (valor.matches(PADRAO_PARAR_NUMERO_INICIO)) {
            return new Token(TokenType.ERRO, valor);
        }
        // Verificar se é um número
        else if (valor.matches(PADRAO_NUMERO)) {
            return new Token(TokenType.NUMERO, valor);
        }
        // Verificar operadores de comparação
        else if (valor.matches(PADRAO_OPERADOR_COMPARACAO)) {
            return new Token(TokenType.OPERADOR, valor);
        }
        // Verificar sinal de igual
        else if (valor.matches(PADRAO_SINAL_IGUAL)) {
            return new Token(TokenType.ATRIBUICAO, valor);
        }
        // Verificar outros operadores
        else if (valor.matches(PADRAO_OPERADOR)) {
            return new Token(TokenType.OPERADOR, valor);
        }
        // Verificar pontuação
        else if (valor.matches(PADRAO_PONTUACAO)) {
            return new Token(TokenType.PONTUACAO, valor);
        }
        // Verificar identificadores
        else if (valor.matches(PADRAO_IDENTIFICADOR)) {
            return new Token(TokenType.IDENTIFICADOR, valor);
        }
        // Se não for nenhum dos anteriores, retorna erro
        else {
            return new Token(TokenType.ERRO, valor);
        }
    }
}
