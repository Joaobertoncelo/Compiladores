package analisadorSemantico;

import java.util.ArrayList;
import java.util.List;

public class Symbol {
    private String nome;
    private TipoSemantico tipo;
    private List<TipoSemantico> parametros;
    private boolean isFunction;

    
    public Symbol(String nome, TipoSemantico tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.parametros = new ArrayList<>();
        this.isFunction = false;
    }

   
    public Symbol(String nome, TipoSemantico tipo, List<TipoSemantico> parametros) {
        this.nome = nome;
        this.tipo = tipo;
        this.parametros = parametros;
        this.isFunction = true;
    }

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

    @Override
    public String toString() {
        if (isFunction) {
            return "Função: " + nome + " -> " + tipo + " com parâmetros " + parametros;
        } else {
            return "Variável: " + nome + " -> " + tipo;
        }
    }
}
