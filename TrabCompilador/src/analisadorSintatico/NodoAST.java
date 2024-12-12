package analisadorSintatico;

import java.util.ArrayList;
import java.util.List;

public class NodoAST {
    public enum Tipo {
        PROGRAMA,
        BLOCO,
        DECLARACAO_VARIAVEL,
        ATRIBUICAO,
        IF,
        ELSE,
        WHILE,
        FOR,
        SWITCH,
        CASE,
        DEFAULT,
        RETURN,
        BREAK,
        CHAMADA_FUNCAO,
        EXPRESSAO_LITERAL,
        EXPRESSAO_VARIAVEL,
        EXPRESSAO_BINARIA,
    }

    private Tipo tipo;
    private String valor; 
    private List<NodoAST> filhos;

    public NodoAST(Tipo tipo) {
        this.tipo = tipo;
        this.filhos = new ArrayList<>();
    }

    public NodoAST(Tipo tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
        this.filhos = new ArrayList<>();
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public List<NodoAST> getFilhos() {
        return filhos;
    }

    public void adicionarFilho(NodoAST filho) {
        filhos.add(filho);
    }
}
