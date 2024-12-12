package analisadorSintatico;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import analisadorLexico.Token;
import analisadorLexico.TokenType;
import analisadorSintatico.NodoAST;
import analisadorSintatico.NodoAST.Tipo;

public class AnaliseSintatica {
    private List<Token> tokens;
    private Iterator<Token> iterador;
    private Token tokenAtual;

    public AnaliseSintatica(List<Token> tokens) {
        this.tokens = tokens;
        this.iterador = tokens.iterator();
        avancar();
    }

    public NodoAST analisarPrograma() throws SyntaxException {
        if (!igual("round")) {
            throw new SyntaxException("Esperado 'round' no início do programa.");
        }
        avancar();

        consumir("(");
        consumir(")");
        consumir("{");
        NodoAST blocoPrincipal = analisarBloco();
        consumir("}");

        if (iterador.hasNext()) {
            throw new SyntaxException("Código após o fim do programa não é permitido.");
        }

        NodoAST programa = new NodoAST(Tipo.PROGRAMA);
        programa.adicionarFilho(blocoPrincipal);
        return programa;
    }

    private NodoAST analisarBloco() throws SyntaxException {
        NodoAST bloco = new NodoAST(Tipo.BLOCO);
        while (tokenAtual != null && !igual("}")) {
            NodoAST comando = analisarComando();
            bloco.adicionarFilho(comando);
        }
        return bloco;
    }

    private NodoAST analisarComando() throws SyntaxException {
        if (tokenAtual == null) throw new SyntaxException("Fim inesperado de arquivo.");

        if (tokenAtual.getType() == TokenType.PALAVRA_CHAVE) {
            switch (tokenAtual.getValor()) {
                case "int":
                case "float":
                case "double":
                case "char":
                case "troll":
                    return analisarDeclaracaoVariavel();
                case "rush":
                    return analisarLoopFor();
                case "smoke":
                    return analisarLoopWhile();
                case "bang":
                    return analisarCondicionalIf();
                case "molotov":
                    throw new SyntaxException("'molotov' sem 'bang' anterior.");
                case "baiter":
                    return analisarSwitch();
                case "antrush":
                    return analisarBreak();
                case "backup":
                    return analisarReturn();
                case "console":
                case "overwatch":
                    return analisarChamadaFuncao(tokenAtual.getValor());
                default:
                    throw new SyntaxException("Palavra-chave inesperada: " + tokenAtual.getValor());
            }
        } else if (tokenAtual.getType() == TokenType.IDENTIFICADOR) {
            return analisarAtribuicao();
        } else {
            throw new SyntaxException("Token inesperado no bloco: " + tokenAtual.getValor());
        }
    }

    private NodoAST analisarDeclaracaoVariavel() throws SyntaxException {
        String tipoVar = tokenAtual.getValor();
        avancar();

        if (tokenAtual.getType() != TokenType.IDENTIFICADOR) {
            throw new SyntaxException("Esperado identificador após o tipo de variável.");
        }

        String nomeVar = tokenAtual.getValor();
        avancar();

        NodoAST decl = new NodoAST(Tipo.DECLARACAO_VARIAVEL, nomeVar);
        decl.adicionarFilho(new NodoAST(Tipo.EXPRESSAO_LITERAL, tipoVar));

        if (igual("=")) {
            avancar();
            NodoAST expr = analisarExpressao(); 
            decl.adicionarFilho(expr);
        }

        consumir(";");
        return decl;
    }

    private NodoAST analisarChamadaFuncao(String funcao) throws SyntaxException {
        avancar(); 
        consumir("(");

        if (tokenAtual.getType() != TokenType.STRING) {
            throw new SyntaxException("Esperado string como primeiro argumento de '" + funcao + "'.");
        }
        String stringArg = tokenAtual.getValor();
        avancar();

        NodoAST chamada = new NodoAST(Tipo.CHAMADA_FUNCAO, funcao);
        chamada.adicionarFilho(new NodoAST(Tipo.EXPRESSAO_LITERAL, stringArg));

        while (!igual(")")) {
            consumir(",");
            if (funcao.equals("console")) {
                
                if (tokenAtual.getType() != TokenType.OPERADOR || !tokenAtual.getValor().equals("&")) {
                    throw new SyntaxException("Esperado '&' antes da variável em '" + funcao + "'.");
                }
                avancar(); 

                if (tokenAtual.getType() != TokenType.IDENTIFICADOR) {
                    throw new SyntaxException("Esperado identificador após '&' em '" + funcao + "'.");
                }
                String varRef = tokenAtual.getValor();
                avancar();
                chamada.adicionarFilho(new NodoAST(Tipo.EXPRESSAO_VARIAVEL, varRef));
            } else {
                if (tokenAtual.getType() != TokenType.IDENTIFICADOR) {
                    throw new SyntaxException("Esperado identificador como argumento de '" + funcao + "'.");
                }
                String varArg = tokenAtual.getValor();
                avancar();
                chamada.adicionarFilho(new NodoAST(Tipo.EXPRESSAO_VARIAVEL, varArg));
            }
        }

        consumir(")");
        consumir(";");

        return chamada;
    }


    private NodoAST analisarLoopFor() throws SyntaxException {
        avancar(); 
        consumir("(");
        NodoAST decl = analisarDeclaracaoVariavel(); 
        NodoAST cond = analisarExpressao(); 
        consumir(";");
        
       
        NodoAST incremento;
        if (tokenAtual.getType() == TokenType.IDENTIFICADOR) {
            String varInc = tokenAtual.getValor();
            avancar(); 

            
            if (igual("+")) {
                avancar(); 
                if (!igual("+")) {
                    throw new SyntaxException("Esperado '++' no incremento do loop 'rush'.");
                }
                avancar(); 

                
                incremento = new NodoAST(Tipo.EXPRESSAO_BINARIA, "++");
                incremento.adicionarFilho(new NodoAST(Tipo.EXPRESSAO_VARIAVEL, varInc));
            } else {
        
                throw new SyntaxException("Esperado incremento '++' no loop 'rush'.");
            }
        } else {
            throw new SyntaxException("Esperado identificador no incremento do loop 'rush'.");
        }

        consumir(")");
        consumir("{");
        NodoAST bloco = analisarBloco();
        consumir("}");

        NodoAST loopFor = new NodoAST(Tipo.FOR);
        loopFor.adicionarFilho(decl);
        loopFor.adicionarFilho(cond);
        loopFor.adicionarFilho(incremento);
        loopFor.adicionarFilho(bloco);
        return loopFor;
    }


    private NodoAST analisarLoopWhile() throws SyntaxException {
        avancar(); // smoke
        consumir("(");
        NodoAST cond = analisarExpressao();
        consumir(")");
        consumir("{");
        NodoAST bloco = analisarBloco();
        consumir("}");

        NodoAST w = new NodoAST(Tipo.WHILE);
        w.adicionarFilho(cond);
        w.adicionarFilho(bloco);
        return w;
    }

    private NodoAST analisarCondicionalIf() throws SyntaxException {
        avancar(); // bang
        consumir("(");
        NodoAST cond = analisarExpressao();
        consumir(")");
        consumir("{");
        NodoAST blocoIf = analisarBloco();
        consumir("}");

        NodoAST ifNode = new NodoAST(Tipo.IF);
        ifNode.adicionarFilho(cond);
        ifNode.adicionarFilho(blocoIf);

        if (tokenAtual != null && igual("molotov")) {
            avancar();
            consumir("{");
            NodoAST blocoElse = analisarBloco();
            consumir("}");
            ifNode.adicionarFilho(blocoElse);
        }

        return ifNode;
    }

    private NodoAST analisarSwitch() throws SyntaxException {
        avancar(); // baiter
        consumir("(");
        NodoAST expr = analisarExpressao();
        consumir(")");
        consumir("{");

        NodoAST sw = new NodoAST(Tipo.SWITCH);
        sw.adicionarFilho(expr);

        while (!igual("}")) {
            if (igual("baita")) {
                avancar();
                if (tokenAtual.getType() != TokenType.NUMERO && tokenAtual.getType() != TokenType.IDENTIFICADOR) {
                    throw new SyntaxException("Esperado valor após 'baita'.");
                }
                String valor = tokenAtual.getValor();
                avancar();
                consumir(":");
                List<NodoAST> cmds = analisarListaComandosCase();
                NodoAST caso = new NodoAST(Tipo.CASE, valor);
                for (NodoAST c : cmds) {
                    caso.adicionarFilho(c);
                }
                sw.adicionarFilho(caso);
            } else if (igual("setup")) {
                avancar();
                consumir(":");
                List<NodoAST> cmds = analisarListaComandosCase();
                NodoAST def = new NodoAST(Tipo.DEFAULT);
                for (NodoAST c : cmds) {
                    def.adicionarFilho(c);
                }
                sw.adicionarFilho(def);
            } else {
                throw new SyntaxException("Token inesperado no baiter: " + tokenAtual.getValor());
            }
        }

        consumir("}");
        return sw;
    }

    private List<NodoAST> analisarListaComandosCase() throws SyntaxException {
        List<NodoAST> comandos = new ArrayList<>();
        boolean encontrouAntrush = false;

        while (tokenAtual != null) {
            String valor = tokenAtual.getValor();
            if (valor.equals("baita") || valor.equals("setup") || valor.equals("}")) {
                if (!encontrouAntrush) {
                    throw new SyntaxException("Esperado 'antrush;' antes de outro case ou '}' no switch.");
                }
                return comandos;
            }

            if (igual("antrush")) {
                avancar();
                consumir(";");
                encontrouAntrush = true;
                continue;
            }

            if (igual("overwatch") || igual("console")) {
                comandos.add(analisarChamadaFuncao(tokenAtual.getValor()));
                continue;
            }

            throw new SyntaxException("Comando não reconhecido no case: " + valor);
        }

        throw new SyntaxException("Fim inesperado dentro do 'baiter'.");
    }

    private NodoAST analisarBreak() throws SyntaxException {
        avancar(); 
        consumir(";");
        return new NodoAST(Tipo.BREAK);
    }

    private NodoAST analisarReturn() throws SyntaxException {
        avancar(); // backup
        NodoAST expr = analisarExpressao();
        consumir(";");
        NodoAST ret = new NodoAST(Tipo.RETURN);
        ret.adicionarFilho(expr);
        return ret;
    }

    private NodoAST analisarAtribuicao() throws SyntaxException {
        if (tokenAtual.getType() != TokenType.IDENTIFICADOR) {
            throw new SyntaxException("Esperado identificador no início da atribuição.");
        }
        String nomeVar = tokenAtual.getValor();
        avancar();

        consumir("=");

        
        NodoAST expr = analisarExpressao();

        consumir(";");

        NodoAST atrib = new NodoAST(NodoAST.Tipo.ATRIBUICAO, nomeVar);
        atrib.adicionarFilho(expr);
        return atrib;
    }


    private NodoAST analisarExpressao() throws SyntaxException {
        return analisarExpressaoIgualdade();
    }


    private NodoAST analisarExpressaoIgualdade() throws SyntaxException {
        NodoAST expr = analisarExpressaoRelacional();
        while (igual("==") || igual("!=")) {
            String op = tokenAtual.getValor();
            avancar();
            NodoAST direita = analisarExpressaoRelacional();
            expr = criarExpressaoBinaria(expr, op, direita);
        }
        return expr;
    }

  
    private NodoAST analisarExpressaoRelacional() throws SyntaxException {
        NodoAST expr = analisarExpressaoAditiva();
        while (igual("<") || igual(">") || igual("<=") || igual(">=")) {
            String op = tokenAtual.getValor();
            avancar();
            NodoAST direita = analisarExpressaoAditiva();
            expr = criarExpressaoBinaria(expr, op, direita);
        }
        return expr;
    }

   
    private NodoAST analisarExpressaoAditiva() throws SyntaxException {
        NodoAST expr = analisarExpressaoMultiplicativa();
        while (igual("+") || igual("-")) {
            String op = tokenAtual.getValor();
            avancar();
            NodoAST direita = analisarExpressaoMultiplicativa();
            expr = criarExpressaoBinaria(expr, op, direita);
        }
        return expr;
    }

 
    private NodoAST analisarExpressaoMultiplicativa() throws SyntaxException {
        NodoAST expr = analisarFator();
        while (igual("*") || igual("/") || igual("%")) {
            String op = tokenAtual.getValor();
            avancar();
            NodoAST direita = analisarFator();
            expr = criarExpressaoBinaria(expr, op, direita);
        }
        return expr;
    }

    
    private NodoAST analisarFator() throws SyntaxException {
        if (tokenAtual == null) {
            throw new SyntaxException("Expressão incompleta.");
        }

        if (tokenAtual.getType() == TokenType.IDENTIFICADOR) {
            String nome = tokenAtual.getValor();
            avancar();
            return new NodoAST(Tipo.EXPRESSAO_VARIAVEL, nome);
        } else if (tokenAtual.getType() == TokenType.NUMERO) {
            String valor = tokenAtual.getValor();
            avancar();
            return new NodoAST(Tipo.EXPRESSAO_LITERAL, valor);
        } else if (tokenAtual.getType() == TokenType.STRING) {
            String valor = tokenAtual.getValor();
            avancar();
            return new NodoAST(Tipo.EXPRESSAO_LITERAL, valor);
        } else if (igual("(")) {
            avancar();
            NodoAST expr = analisarExpressao();
            consumir(")");
            return expr;
        }

        throw new SyntaxException("Fator inesperado: " + tokenAtual.getValor());
    }

    private NodoAST criarExpressaoBinaria(NodoAST esquerda, String op, NodoAST direita) {
        NodoAST expr = new NodoAST(Tipo.EXPRESSAO_BINARIA, op);
        expr.adicionarFilho(esquerda);
        expr.adicionarFilho(direita);
        return expr;
    }

    private void consumir(String esperado) throws SyntaxException {
        if (tokenAtual == null || !tokenAtual.getValor().equals(esperado)) {
            throw new SyntaxException("Esperado '" + esperado + "', encontrado: " + (tokenAtual == null ? "EOF" : tokenAtual.getValor()));
        }
        avancar();
    }

    private boolean igual(String valor) {
        return tokenAtual != null && tokenAtual.getValor().equals(valor);
    }

    private void avancar() {
        tokenAtual = iterador.hasNext() ? iterador.next() : null;
    }
}
