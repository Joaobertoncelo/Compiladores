package analisadorSemantico;

import java.util.ArrayList;
import java.util.List;

// Se seu pacote for diferente, ajuste.

public class Symbol {
    private String nome;
    private TipoSemantico tipo;
    private List<TipoSemantico> parametros;
    private boolean isFunction;

    // Novos campos para suportar vetores:
    private boolean isArray = false;
    private int arraySize = 0; // 0 => não é array ou desconhecido

    // Construtor para variáveis simples
    public Symbol(String nome, TipoSemantico tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.parametros = new ArrayList<>();
        this.isFunction = false;
    }

    // Construtor para funções
    public Symbol(String nome, TipoSemantico tipo, List<TipoSemantico> parametros) {
        this.nome = nome;
        this.tipo = tipo;
        this.parametros = parametros;
        this.isFunction = true;
    }

    // Getters e setters normais
    public String getNome() {
        return nome;
    }

    public TipoSemantico getTipo() {
        return tipo;
    }

    public List<TipoSemantico> getParametros() {
        return parametros;
    }

    public boolean isFunction() {
        return isFunction;
    }

    // Getters e setters para array
    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        this.isArray = array;
    }

    public int getArraySize() {
        return arraySize;
    }

    public void setArraySize(int arraySize) {
        this.arraySize = arraySize;
    }

    @Override
    public String toString() {
        if (isFunction) {
            return "Função: " + nome + " -> " + tipo + " com parâmetros " + parametros;
        } else {
            // Se quiser exibir se é array, acrescente:
            if (isArray) {
                return "Variável Vetor: " + nome + " -> " + tipo + "[" + arraySize + "]";
            } else {
                return "Variável: " + nome + " -> " + tipo;
            }
        }
    }
}
