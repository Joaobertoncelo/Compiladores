package analisadorSintatico;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import analisadorLexico.Token;
import analisadorLexico.TokenType;

public class analisadorSintatico {
    private final List<Token> tokens;
    private int pos = 0;
    private Token tokenAtual;
    private final TabelaDeSimbolos tabelaDeSimbolos = new TabelaDeSimbolos();

    public analisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
        if (!tokens.isEmpty()) {
            tokenAtual = tokens.get(0);
        }
    }

    public void analisar() {
        try {
            programa();
            if (pos < tokens.size()) {
                throw new ErroSintatico("Tokens após o fim do programa não são permitidos.");
            }
            System.out.println("Análise Sintática concluída sem erros.");
        } catch (ErroSintatico e) {
            System.err.println("Erro Sintático: " + e.getMessage());
        }
    }

    private void programa() {
        if (match(TokenType.PALAVRA_CHAVE, "int")) {
            if (!tokenAtual.getValor().equals("round")) {
                throw new ErroSintatico("Esperado 'round' como nome do ponto de entrada do programa. Encontrado: " + tokenAtual.getValor());
            }
            consumir(TokenType.PALAVRA_CHAVE, "Esperado o identificador 'round' para a entrada do programa.");
            consumirComValor(TokenType.PONTUACAO, "(", "Esperado '(' após 'round'.");
            consumirComValor(TokenType.PONTUACAO, ")", "Esperado ')' após '('.");
            consumirComValor(TokenType.PONTUACAO, "{", "Esperado '{' para início do bloco principal.");
            while (!match(TokenType.PONTUACAO, "}")) {
                declaracao();
            }
            System.out.println("Ponto de entrada reconhecido e analisado com sucesso.");
        } else {
            throw new ErroSintatico("Esperado 'int round()' no início do programa.");
        }
        if (pos < tokens.size()) {
            throw new ErroSintatico("Tokens após o fim do programa não são permitidos.");
        }
    }


    private void declaracao() {
        if (match(TokenType.PALAVRA_CHAVE, "int", "float", "char", "double", "round", "bang")) {
            declaracaoVariavelOuFuncao();
        } else if (match(TokenType.IDENTIFICADOR)) {
            atribuicaoOuChamada();
        } else {
            erroEsperado("Declaração de variável, função ou atribuição.");
        }
    }

    private void declaracaoVariavelOuFuncao() {
        Token tipo = tokenAtual;
        consumir(TokenType.IDENTIFICADOR, "Esperado nome da variável ou função.");
        
        if (tokenAtual.getValor().equals("round")) {
            tabelaDeSimbolos.adicionarFuncao(tokenAtual.getValor(), tipo.getValor()); 
            declaracaoFuncao();
        } else if (match(TokenType.PONTUACAO, "(")) {
            tabelaDeSimbolos.adicionarFuncao(tokenAtual.getValor(), tipo.getValor());
            declaracaoFuncao();
        } else if (match(TokenType.PONTUACAO, ";")) {
            tabelaDeSimbolos.adicionarVariavel(tokenAtual.getValor(), tipo.getValor());
        } else {
            erroEsperado("( para função ou ; para variável.");
        }
    }

    private void declaracaoFuncao() {
        consumir(TokenType.PONTUACAO, "(");
        parametros();
        consumir(TokenType.PONTUACAO, ")");
        bloco();
    }

    private void parametros() {
        if (match(TokenType.PALAVRA_CHAVE, "int", "float", "char", "double")) {
            consumir(TokenType.IDENTIFICADOR, "Esperado nome do parâmetro.");
            while (match(TokenType.PONTUACAO, ",")) {
                consumir(TokenType.PALAVRA_CHAVE, "Esperado tipo do parâmetro.");
                consumir(TokenType.IDENTIFICADOR, "Esperado nome do parâmetro.");
            }
        }
    }

    private void bloco() {
        consumir(TokenType.PONTUACAO, "{");
        while (!match(TokenType.PONTUACAO, "}")) {
            declaracao();
        }
        consumir(TokenType.PONTUACAO, "}");
    }

    private void atribuicaoOuChamada() {
        if (match(TokenType.PONTUACAO, "=")) {
            expressao();
            consumir(TokenType.PONTUACAO, ";");
        } else if (match(TokenType.PONTUACAO, "(")) {
            chamadaDeFuncao();
            consumir(TokenType.PONTUACAO, ";");
        } else {
            erroEsperado("= para atribuição ou ( para chamada de função.");
        }
    }

    private void chamadaDeFuncao() {
        consumir(TokenType.PONTUACAO, "(");
        if (!match(TokenType.PONTUACAO, ")")) {
            expressao();
            while (match(TokenType.PONTUACAO, ",")) {
                expressao();
            }
            consumir(TokenType.PONTUACAO, ")");
        }
    }

    private void expressao() {
        termo();
        while (match(TokenType.OPERADOR, "+", "-")) {
            termo();
        }
    }

    private void termo() {
        fator();
        while (match(TokenType.OPERADOR, "*", "/")) {
            fator();
        }
    }

    private void fator() {
        if (match(TokenType.NUMERO) || match(TokenType.IDENTIFICADOR)) {
        } else if (match(TokenType.PONTUACAO, "(")) {
            expressao();
            consumir(TokenType.PONTUACAO, ")");
        } else {
            erroEsperado("Número, identificador ou ( para expressões.");
        }
    }

    private boolean match(TokenType tipo, String... valores) {
        if (tokenAtual.getType() == tipo && (valores.length == 0 || contains(valores, tokenAtual.getValor()))) {
            consumir();
            return true;
        }
        return false;
    }

    private void consumir() {
    	System.out.println("Consumi token: " + tokenAtual);
        pos++;
        if (pos < tokens.size()) {
            tokenAtual = tokens.get(pos);
        }
    }

    private void consumir(TokenType tipo, String mensagemErro) {
        if (tokenAtual.getType() == tipo) {
            consumir();
        } else {
            throw new ErroSintatico(mensagemErro + " Encontrado: " + tokenAtual);
        }
    }
    
    private void consumirComValor(TokenType tipoEsperado, String valorEsperado, String mensagemErro) {
        if (tokenAtual.getType() == tipoEsperado && tokenAtual.getValor().equals(valorEsperado)) {
            pos++;
            if (pos < tokens.size()) {
                tokenAtual = tokens.get(pos);
            }
        } else {
            throw new ErroSintatico(mensagemErro + " Encontrado: " + tokenAtual);
        }
    }



    private boolean contains(String[] array, String valor) {
        for (String s : array) {
            if (s.equals(valor)) {
                return true;
            }
        }
        return false;
    }

    private void erroEsperado(String esperado) {
        throw new ErroSintatico("Esperado: " + esperado + ". Encontrado: " + tokenAtual);
    }

    private class TabelaDeSimbolos {
        private final Map<String, String> variaveis = new HashMap<>();
        private final Map<String, String> funcoes = new HashMap<>();

        public void adicionarVariavel(String nome, String tipo) {
            variaveis.put(nome, tipo);
        }

        public void adicionarFuncao(String nome, String tipo) {
            funcoes.put(nome, tipo);
        }

        public boolean existeFuncao(String nome) {
            return funcoes.containsKey(nome);
        }

        public boolean existeVariavel(String nome) {
            return variaveis.containsKey(nome);
        }
    }
}
