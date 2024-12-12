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
        avancar(); 
        if (tokenAtual.getType() != TokenType.IDENTIFICADOR) {
            throw new SyntaxException("Esperado identificador após o tipo de variável.");
        }
        avancar();

        if (!tokenAtual.getValor().equals(";") && !tokenAtual.getValor().equals("=")) {
            throw new SyntaxException("Esperado ';' ou '=' após o identificador.");
        }

        if (tokenAtual.getValor().equals("=")) {
            avancar(); 
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
        avancar(); 
        if (!tokenAtual.getValor().equals("(")) {
            throw new SyntaxException("Esperado '(' após '" + funcao + "'.");
        }
        avancar();

        
        if (tokenAtual.getType() != TokenType.STRING) {
            throw new SyntaxException("Esperado string como primeiro argumento de '" + funcao + "'.");
        }
        avancar();

        
        while (tokenAtual != null && !tokenAtual.getValor().equals(")")) {
            if (!tokenAtual.getValor().equals(",")) {
                throw new SyntaxException("Esperado ',' entre os argumentos de '" + funcao + "'.");
            }
            avancar(); 

            if (funcao.equals("console") && !tokenAtual.getValor().startsWith("&")) {
                throw new SyntaxException("Esperado '&' antes da variável em '" + funcao + "'.");
            }
            if (funcao.equals("console")) {
                avancar(); 
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
        avancar(); 
        if (!tokenAtual.getValor().equals("(")) {
            throw new SyntaxException("Esperado '(' após 'rush'.");
        }
        avancar();

      
        analisarDeclaracaoVariavel();

        
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

       
        if (tokenAtual.getType() == TokenType.IDENTIFICADOR) {
            avancar(); 
            if (tokenAtual.getType() == TokenType.OPERADOR && tokenAtual.getValor().equals("+")) {
                avancar(); 
                if (tokenAtual == null || !tokenAtual.getValor().equals("+")) {
                    throw new SyntaxException("Esperado '++' no incremento do loop 'rush'.");
                }
                avancar(); 
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
        avancar(); 
        if (!tokenAtual.getValor().equals("(")) {
            throw new SyntaxException("Esperado '(' após 'smoke'.");
        }
        avancar();

        
        if (tokenAtual.getType() != TokenType.IDENTIFICADOR && tokenAtual.getType() != TokenType.NUMERO) {
            throw new SyntaxException("Esperado identificador ou número na condição do 'smoke'.");
        }
        avancar();

      
        if (tokenAtual.getType() == TokenType.OPERADOR || tokenAtual.getType() == TokenType.OPERADOR) {
            
            avancar();

           
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
        avancar(); 
        if (!tokenAtual.getValor().equals("(")) {
            throw new SyntaxException("Esperado '(' após 'bang'.");
        }
        avancar();

       
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

        
        if (!tokenAtual.getValor().equals("{")) {
            throw new SyntaxException("Esperado '{' para o bloco do 'bang'.");
        }
        avancar();

        analisarBloco();

        
        if (!tokenAtual.getValor().equals("}")) {
            throw new SyntaxException("Esperado '}' no final do bloco do 'bang'.");
        }
        avancar();

       
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
        avancar(); 
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

        
        while (tokenAtual != null && !tokenAtual.getValor().equals("}")) {
            if (tokenAtual.getValor().equals("baita")) {
                analisarCase(); 
            } else if (tokenAtual.getValor().equals("setup")) {
                analisarDefault(); 
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
        avancar(); 
        if (tokenAtual.getType() != TokenType.NUMERO && tokenAtual.getType() != TokenType.IDENTIFICADOR) {
            throw new SyntaxException("Esperado valor ou identificador após 'baita'.");
        }
        avancar();

        if (!tokenAtual.getValor().equals(":")) {
            throw new SyntaxException("Esperado ':' após valor do 'baita'.");
        }
        avancar();

        
        analisarListaComandosCase();
    }

    private void analisarDefault() throws SyntaxException {
        avancar(); 
        if (!tokenAtual.getValor().equals(":")) {
            throw new SyntaxException("Esperado ':' após 'setup'.");
        }
        avancar();

        
        analisarListaComandosCase();
    }

    private void analisarListaComandosCase() throws SyntaxException {
       
        
        boolean encontrouAntrush = false;

        while (tokenAtual != null) {
            String valor = tokenAtual.getValor();

           
            if (valor.equals("baita") || valor.equals("setup") || valor.equals("}")) {
                if (!encontrouAntrush) {
                    throw new SyntaxException("Esperado 'antrush;' antes de iniciar outro case ou encerrar o 'baiter'.");
                }
               
                return;
            }

        
            if (valor.equals("antrush")) {
                avancar(); 
                if (!tokenAtual.getValor().equals(";")) {
                    throw new SyntaxException("Esperado ';' após 'antrush'.");
                }
                avancar(); 
                encontrouAntrush = true;
                
                continue;
            }

            
            if (valor.equals("overwatch") || valor.equals("console")) {
                analisarChamadaFuncao(valor); 
                continue;
            }

           
            throw new SyntaxException("Comando não reconhecido no case: " + valor);
        }

       
        throw new SyntaxException("Fim inesperado dentro do 'baiter'. Esperado '}', 'baita' ou 'setup'.");
    }


    private void analisarBreak() throws SyntaxException {
        avancar(); 
        if (!tokenAtual.getValor().equals(";")) {
            throw new SyntaxException("Esperado ';' após 'antrush'.");
        }
        avancar();
    }
    
    private void analisarReturn() throws SyntaxException {
        avancar(); 
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
        
        if (tokenAtual.getType() != TokenType.IDENTIFICADOR) {
            throw new SyntaxException("Esperado identificador no início da atribuição.");
        }
        avancar(); 

       
        if (!tokenAtual.getValor().equals("=")) {
            throw new SyntaxException("Esperado '=' após identificador na atribuição.");
        }
        avancar(); 

        
        if (tokenAtual.getType() != TokenType.IDENTIFICADOR && tokenAtual.getType() != TokenType.NUMERO) {
            throw new SyntaxException("Esperado valor ou identificador após '='.");
        }
        avancar(); 
        if (tokenAtual.getType() == TokenType.OPERADOR) {
           
            avancar();
           
            if (tokenAtual.getType() != TokenType.IDENTIFICADOR && tokenAtual.getType() != TokenType.NUMERO) {
                throw new SyntaxException("Esperado valor após operador na expressão da atribuição.");
            }
            avancar(); 
        }

       
        if (!tokenAtual.getValor().equals(";")) {
            throw new SyntaxException("Esperado ';' ao final da atribuição.");
        }
        avancar(); 
    }


}
