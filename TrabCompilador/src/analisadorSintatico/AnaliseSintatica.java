package analisadorSintatico;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import analisadorLexico.Token;
import analisadorLexico.TokenType;

public class AnaliseSintatica {
    private List<Token> tokens;
    private Iterator<Token> iterador;
    private Token tokenAtual;

    public AnaliseSintatica(List<Token> tokens) {
        this.tokens = tokens;
        this.iterador = tokens.iterator();
        avancar();
    }

    public void analisar() throws SyntaxException {
        // Começa analisando pela função principal "round()"
        if (!tokenAtual.getValor().equals("round")) {
            throw new SyntaxException("Esperado 'round' no início do programa.");
        }
        avancar();

        if (!tokenAtual.getValor().equals("(")) {
            throw new SyntaxException("Esperado '(' após 'round'.");
        }
        avancar();

        if (!tokenAtual.getValor().equals(")")) {
            throw new SyntaxException("Esperado ')' após '('.");
        }
        avancar();

        if (!tokenAtual.getValor().equals("{")) {
            throw new SyntaxException("Esperado '{' para iniciar o bloco da função.");
        }
        avancar();

        // Analisa o corpo da função
        analisarBloco();

        if (!tokenAtual.getValor().equals("}")) {
            throw new SyntaxException("Esperado '}' no final do bloco.");
        }
        avancar();

        if (iterador.hasNext()) {
            throw new SyntaxException("Código após o fim do programa não é permitido.");
        }
    }

    private void analisarBloco() throws SyntaxException {
        while (tokenAtual != null && !tokenAtual.getValor().equals("}")) {
            if (tokenAtual.getType() == TokenType.PALAVRA_CHAVE) {
                switch (tokenAtual.getValor()) {
                    case "int":
                    case "float":
                    case "double":
                    case "char":
                    case "troll":
                        analisarDeclaracaoVariavel();
                        break;
                    case "rush":
                        analisarLoopFor();
                        break;
                    case "smoke":
                        analisarLoopWhile();
                        break;
                    case "bang":
                        analisarCondicionalIf();
                        break;
                    case "molotov":
                        throw new SyntaxException("'molotov' encontrado sem um 'bang' anterior.");
                    case "baiter":
                        analisarSwitch();
                        break;
                    case "antrush":
                        analisarBreak();
                        break;
                    case "backup":
                        analisarReturn();
                        break;
                    case "console":
                    case "overwatch":
                        analisarChamadaFuncao(tokenAtual.getValor());
                        break;
                    default:
                        throw new SyntaxException("Palavra-chave inesperada fora do contexto: " + tokenAtual.getValor());
                }
            } else if (tokenAtual.getType() == TokenType.IDENTIFICADOR) {
                // Se encontramos um identificador, pode se tratar de uma atribuição.
                analisarAtribuicao();
            } else {
                throw new SyntaxException("Token inesperado no bloco: " + tokenAtual.getValor());
            }
        }
    }








    
    private void avancar() {
        tokenAtual = iterador.hasNext() ? iterador.next() : null;
    }

    private void analisarDeclaracaoVariavel() throws SyntaxException {
        avancar(); // Tipo (int, float, etc.)
        if (tokenAtual.getType() != TokenType.IDENTIFICADOR) {
            throw new SyntaxException("Esperado identificador após o tipo de variável.");
        }
        avancar();

        if (!tokenAtual.getValor().equals(";") && !tokenAtual.getValor().equals("=")) {
            throw new SyntaxException("Esperado ';' ou '=' após o identificador.");
        }

        if (tokenAtual.getValor().equals("=")) {
            avancar(); // '='
            if (tokenAtual.getType() != TokenType.NUMERO && tokenAtual.getType() != TokenType.IDENTIFICADOR) {
                throw new SyntaxException("Esperado valor ou identificador após '='.");
            }
            avancar();
        }

        if (!tokenAtual.getValor().equals(";")) {
            throw new SyntaxException("Esperado ';' após a declaração da variável.");
        }
        avancar();
    }

    
    private void analisarChamadaFuncao(String funcao) throws SyntaxException {
        avancar(); // Avança para "("
        if (!tokenAtual.getValor().equals("(")) {
            throw new SyntaxException("Esperado '(' após '" + funcao + "'.");
        }
        avancar();

        // Verifica o primeiro argumento: uma string
        if (tokenAtual.getType() != TokenType.STRING) {
            throw new SyntaxException("Esperado string como primeiro argumento de '" + funcao + "'.");
        }
        avancar();

        // Verifica argumentos adicionais, se houver
        while (tokenAtual != null && !tokenAtual.getValor().equals(")")) {
            if (!tokenAtual.getValor().equals(",")) {
                throw new SyntaxException("Esperado ',' entre os argumentos de '" + funcao + "'.");
            }
            avancar(); // Pula a vírgula

            if (funcao.equals("console") && !tokenAtual.getValor().startsWith("&")) {
                throw new SyntaxException("Esperado '&' antes da variável em '" + funcao + "'.");
            }
            if (funcao.equals("console")) {
                avancar(); // Pula o '&'
            }

            if (tokenAtual.getType() != TokenType.IDENTIFICADOR) {
                throw new SyntaxException("Esperado identificador como argumento de '" + funcao + "'.");
            }
            avancar();
        }

        if (!tokenAtual.getValor().equals(")")) {
            throw new SyntaxException("Esperado ')' no fim da chamada de '" + funcao + "'.");
        }
        avancar();

        if (!tokenAtual.getValor().equals(";")) {
            throw new SyntaxException("Esperado ';' após a chamada de '" + funcao + "'.");
        }
        avancar();
    }



    private void analisarLoopFor() throws SyntaxException {
        avancar(); // "rush"
        if (!tokenAtual.getValor().equals("(")) {
            throw new SyntaxException("Esperado '(' após 'rush'.");
        }
        avancar();

        // Declaração inicial do loop
        analisarDeclaracaoVariavel();

        // Condição do loop
        if (tokenAtual.getType() != TokenType.IDENTIFICADOR && tokenAtual.getType() != TokenType.NUMERO) {
            throw new SyntaxException("Esperado valor ou identificador na condição do loop 'rush'. Encontrado: " + tokenAtual.getValor());
        }
        avancar();

        if (tokenAtual.getType() != TokenType.OPERADOR) {
            throw new SyntaxException("Esperado operador de comparação no loop 'rush'. Encontrado: " + tokenAtual.getValor());
        }
        avancar();

        if (tokenAtual.getType() != TokenType.NUMERO && tokenAtual.getType() != TokenType.IDENTIFICADOR) {
            throw new SyntaxException("Esperado valor ou identificador após operador de comparação no loop 'rush'.");
        }
        avancar();

        if (!tokenAtual.getValor().equals(";")) {
            throw new SyntaxException("Esperado ';' após a condição do loop 'rush'.");
        }
        avancar();

        // Incremento do loop
        if (tokenAtual.getType() == TokenType.IDENTIFICADOR) {
            avancar(); // Identificador do incremento (e.g., "i")
            if (tokenAtual.getType() == TokenType.OPERADOR && tokenAtual.getValor().equals("+")) {
                avancar(); // Primeiro operador '+'
                if (tokenAtual == null || !tokenAtual.getValor().equals("+")) {
                    throw new SyntaxException("Esperado '++' no incremento do loop 'rush'.");
                }
                avancar(); // Segundo operador '+'
            } else {
                throw new SyntaxException("Esperado operador de incremento no loop 'rush'.");
            }
        } else {
            throw new SyntaxException("Esperado identificador no incremento do loop 'rush'.");
        }

        if (!tokenAtual.getValor().equals(")")) {
            throw new SyntaxException("Esperado ')' no fim do loop 'rush'.");
        }
        avancar();

        if (!tokenAtual.getValor().equals("{")) {
            throw new SyntaxException("Esperado '{' para o bloco do loop 'rush'.");
        }
        avancar();

        analisarBloco();

        if (!tokenAtual.getValor().equals("}")) {
            throw new SyntaxException("Esperado '}' no fim do bloco do loop 'rush'.");
        }
        avancar();
    }


    private void analisarLoopWhile() throws SyntaxException {
        avancar(); // consome "smoke"
        if (!tokenAtual.getValor().equals("(")) {
            throw new SyntaxException("Esperado '(' após 'smoke'.");
        }
        avancar();

        // Analisar expressão: por exemplo, (distancia < 10)
        // Espera-se Identificador ou Número
        if (tokenAtual.getType() != TokenType.IDENTIFICADOR && tokenAtual.getType() != TokenType.NUMERO) {
            throw new SyntaxException("Esperado identificador ou número na condição do 'smoke'.");
        }
        avancar();

        // Espera-se um operador de comparação ou aritmético, se a expressão for simples assim
        if (tokenAtual.getType() == TokenType.OPERADOR || tokenAtual.getType() == TokenType.OPERADOR) {
            // Pode ser <, >, ==, etc.
            avancar();

            // Agora outro identificador ou número após o operador
            if (tokenAtual.getType() != TokenType.IDENTIFICADOR && tokenAtual.getType() != TokenType.NUMERO) {
                throw new SyntaxException("Esperado número ou identificador após o operador na condição do 'smoke'.");
            }
            avancar();
        }

        if (!tokenAtual.getValor().equals(")")) {
            throw new SyntaxException("Esperado ')' no fim da condição do 'smoke'.");
        }
        avancar();

        if (!tokenAtual.getValor().equals("{")) {
            throw new SyntaxException("Esperado '{' para o bloco do 'smoke'.");
        }
        avancar();

        analisarBloco();

        if (!tokenAtual.getValor().equals("}")) {
            throw new SyntaxException("Esperado '}' no fim do bloco do 'smoke'.");
        }
        avancar();
    }


    private void analisarCondicionalIf() throws SyntaxException {
        avancar(); // "bang"
        if (!tokenAtual.getValor().equals("(")) {
            throw new SyntaxException("Esperado '(' após 'bang'.");
        }
        avancar();

        // Verifica a condição dentro dos parênteses
        if (tokenAtual.getType() != TokenType.IDENTIFICADOR && tokenAtual.getType() != TokenType.NUMERO) {
            throw new SyntaxException("Esperado identificador ou número na condição do 'bang'.");
        }
        avancar();

        if (tokenAtual.getType() != TokenType.OPERADOR) {
            throw new SyntaxException("Esperado operador na condição do 'bang'.");
        }
        avancar();

        if (tokenAtual.getType() != TokenType.NUMERO && tokenAtual.getType() != TokenType.IDENTIFICADOR) {
            throw new SyntaxException("Esperado número ou identificador após o operador na condição do 'bang'.");
        }
        avancar();

        if (!tokenAtual.getValor().equals(")")) {
            throw new SyntaxException("Esperado ')' no fim da condição do 'bang'.");
        }
        avancar();

        // Verifica o início do bloco
        if (!tokenAtual.getValor().equals("{")) {
            throw new SyntaxException("Esperado '{' para o bloco do 'bang'.");
        }
        avancar();

        analisarBloco();

        // Verifica o final do bloco
        if (!tokenAtual.getValor().equals("}")) {
            throw new SyntaxException("Esperado '}' no final do bloco do 'bang'.");
        }
        avancar();

        // Verifica o opcional 'molotov' (else)
        if (tokenAtual != null && tokenAtual.getValor().equals("molotov")) {
            avancar();

            if (!tokenAtual.getValor().equals("{")) {
                throw new SyntaxException("Esperado '{' para o bloco do 'molotov'.");
            }
            avancar();

            analisarBloco();

            if (!tokenAtual.getValor().equals("}")) {
                throw new SyntaxException("Esperado '}' no final do bloco do 'molotov'.");
            }
            avancar();
        }
    }

    
    private void analisarSwitch() throws SyntaxException {
        avancar(); // "baiter"
        if (!tokenAtual.getValor().equals("(")) {
            throw new SyntaxException("Esperado '(' após 'baiter'.");
        }
        avancar();

        if (tokenAtual.getType() != TokenType.IDENTIFICADOR && tokenAtual.getType() != TokenType.NUMERO) {
            throw new SyntaxException("Esperado expressão ou variável no 'baiter'.");
        }
        avancar();

        if (!tokenAtual.getValor().equals(")")) {
            throw new SyntaxException("Esperado ')' após expressão do 'baiter'.");
        }
        avancar();

        if (!tokenAtual.getValor().equals("{")) {
            throw new SyntaxException("Esperado '{' para iniciar o bloco do 'baiter'.");
        }
        avancar();

        // Processa os casos dentro do bloco do baiter
        while (tokenAtual != null && !tokenAtual.getValor().equals("}")) {
            if (tokenAtual.getValor().equals("baita")) {
                analisarCase(); // Trata 'baita'
            } else if (tokenAtual.getValor().equals("setup")) {
                analisarDefault(); // Trata 'setup'
            } else {
                throw new SyntaxException("Token inesperado no bloco do 'baiter': " + tokenAtual.getValor());
            }
        }

        if (!tokenAtual.getValor().equals("}")) {
            throw new SyntaxException("Esperado '}' no final do bloco do 'baiter'.");
        }
        avancar();
    }





    private void analisarCase() throws SyntaxException {
        avancar(); // consume "baita"
        if (tokenAtual.getType() != TokenType.NUMERO && tokenAtual.getType() != TokenType.IDENTIFICADOR) {
            throw new SyntaxException("Esperado valor ou identificador após 'baita'.");
        }
        avancar();

        if (!tokenAtual.getValor().equals(":")) {
            throw new SyntaxException("Esperado ':' após valor do 'baita'.");
        }
        avancar();

        // Agora analisamos a lista de comandos do case
        analisarListaComandosCase();
    }

    private void analisarDefault() throws SyntaxException {
        avancar(); // consume "setup"
        if (!tokenAtual.getValor().equals(":")) {
            throw new SyntaxException("Esperado ':' após 'setup'.");
        }
        avancar();

        // Agora analisamos a lista de comandos do default
        analisarListaComandosCase();
    }

    private void analisarListaComandosCase() throws SyntaxException {
        // Aqui lemos comandos até encontrarmos 'baita', 'setup', '}', ou 'antrush;'
        // Mas note que antrush; deve aparecer em algum momento para encerrar o case
        
        boolean encontrouAntrush = false;

        while (tokenAtual != null) {
            String valor = tokenAtual.getValor();

            // Se encontrar tokens que indicam o fim da lista de comandos do case:
            if (valor.equals("baita") || valor.equals("setup") || valor.equals("}")) {
                if (!encontrouAntrush) {
                    throw new SyntaxException("Esperado 'antrush;' antes de iniciar outro case ou encerrar o 'baiter'.");
                }
                // Não avançamos aqui, pois o switch superior vai lidar com isso
                return;
            }

            // Caso contrário, analisamos um comando. Aqui você pode implementar algo como:
            // analisarComandoSemBloco();
            // um método que identifique comandos válidos: overwatch(...);, console(...);, etc.

            if (valor.equals("antrush")) {
                avancar(); // consome 'antrush'
                if (!tokenAtual.getValor().equals(";")) {
                    throw new SyntaxException("Esperado ';' após 'antrush'.");
                }
                avancar(); // consome ';'
                encontrouAntrush = true;
                // Após encontrar antrush; se vier outro comando é erro, mas pode ter um caso onde você aceita apenas
                // se sair do case. Dependendo da semântica que você quer, pode parar aqui.
                continue;
            }

            // Caso seja um comando do tipo overwatch(...); ou console(...);:
            if (valor.equals("overwatch") || valor.equals("console")) {
                analisarChamadaFuncao(valor); 
                continue;
            }

            // Você pode colocar aqui outras instruções possíveis dentro do case.

            // Se chegar aqui e não reconhecer o token como comando, é um erro.
            throw new SyntaxException("Comando não reconhecido no case: " + valor);
        }

        // Se saiu do while sem encontrar '}', 'baita', 'setup' é porque chegou ao fim dos tokens.
        // Isso é um erro, pois o '}' do 'baiter' ou outro case deveria aparecer.
        throw new SyntaxException("Fim inesperado dentro do 'baiter'. Esperado '}', 'baita' ou 'setup'.");
    }


    private void analisarBreak() throws SyntaxException {
        avancar(); // "antrush"
        if (!tokenAtual.getValor().equals(";")) {
            throw new SyntaxException("Esperado ';' após 'antrush'.");
        }
        avancar();
    }
    
    private void analisarReturn() throws SyntaxException {
        avancar(); // "backup"
        if (tokenAtual.getType() != TokenType.NUMERO && tokenAtual.getType() != TokenType.IDENTIFICADOR) {
            throw new SyntaxException("Esperado valor ou expressão após 'backup'.");
        }
        avancar();

        if (!tokenAtual.getValor().equals(";")) {
            throw new SyntaxException("Esperado ';' após 'backup'.");
        }
        avancar();
    }

    private void analisarAtribuicao() throws SyntaxException {
        // Esperamos um identificador
        if (tokenAtual.getType() != TokenType.IDENTIFICADOR) {
            throw new SyntaxException("Esperado identificador no início da atribuição.");
        }
        avancar(); // consome o identificador

        // Esperamos um sinal de '='
        if (!tokenAtual.getValor().equals("=")) {
            throw new SyntaxException("Esperado '=' após identificador na atribuição.");
        }
        avancar(); // consome '='

        // Esperamos agora uma expressão simples, por exemplo:
        // Para simplificar, aceite IDENTIFICADOR, NUMERO, ou uma soma simples IDENTIFICADOR/NUMERO + IDENTIFICADOR/NUMERO
        // Caso queira algo mais complexo, precisará de um analisador de expressões mais elaborado.
        if (tokenAtual.getType() != TokenType.IDENTIFICADOR && tokenAtual.getType() != TokenType.NUMERO) {
            throw new SyntaxException("Esperado valor ou identificador após '='.");
        }
        avancar(); // consome o primeiro valor

        // Pode haver um operador e outro valor, por exemplo: b + 1.0
        if (tokenAtual.getType() == TokenType.OPERADOR) {
            // Consome o operador
            avancar();
            // Espera outro valor (identificador ou número)
            if (tokenAtual.getType() != TokenType.IDENTIFICADOR && tokenAtual.getType() != TokenType.NUMERO) {
                throw new SyntaxException("Esperado valor após operador na expressão da atribuição.");
            }
            avancar(); // consome esse valor
        }

        // Espera ponto e vírgula para terminar a atribuição
        if (!tokenAtual.getValor().equals(";")) {
            throw new SyntaxException("Esperado ';' ao final da atribuição.");
        }
        avancar(); // consome ';'
    }


}
