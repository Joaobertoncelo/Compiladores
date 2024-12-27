package analisadorSemantico;

import java.util.*;

public class SymbolTable {
    private List<Map<String, List<Symbol>>> pilhaEscopos;

    public SymbolTable() {
        pilhaEscopos = new ArrayList<>();
        
        pilhaEscopos.add(new HashMap<>());
    }

    public void entrarEscopo() {
        pilhaEscopos.add(new HashMap<>());
    }

    public void sairEscopo() {
        if (pilhaEscopos.size() > 1) { 
            pilhaEscopos.remove(pilhaEscopos.size() - 1);
        }
    }

    public void adicionarSimbolo(String nome, TipoSemantico tipo) throws SemanticException {
        adicionarSimbolo(nome, tipo, new ArrayList<>());
    }

    public void adicionarSimbolo(String nome, TipoSemantico tipo, List<TipoSemantico> parametros) throws SemanticException {
        Symbol simbolo;
        if (parametros.isEmpty()) {
            simbolo = new Symbol(nome, tipo);
        } else {
            simbolo = new Symbol(nome, tipo, parametros);
        }

        Map<String, List<Symbol>> escopoAtual = pilhaEscopos.get(pilhaEscopos.size() - 1);
        escopoAtual.putIfAbsent(nome, new ArrayList<>());
        escopoAtual.get(nome).add(simbolo);
    }

    public List<Symbol> buscarSimbolo(String nome) {
        List<Symbol> resultado = new ArrayList<>();
        for (int i = pilhaEscopos.size() - 1; i >= 0; i--) {
            Map<String, List<Symbol>> escopo = pilhaEscopos.get(i);
            if (escopo.containsKey(nome)) {
                resultado.addAll(escopo.get(nome));
            }
        }
        return resultado;
    }

    public List<Map<String, List<Symbol>>> getPilhaEscopos() {
        return pilhaEscopos;
    }

    
    public void imprimirTabelaSimbolos() {
        System.out.println("=== Tabela de Símbolos ===");
        for (int i = pilhaEscopos.size() - 1; i >= 0; i--) {
            Map<String, List<Symbol>> escopo = pilhaEscopos.get(i);
            System.out.println("Escopo " + (i + 1) + ":");
            for (Map.Entry<String, List<Symbol>> entry : escopo.entrySet()) {
                String nome = entry.getKey();
                List<Symbol> simbolos = entry.getValue();
                for (Symbol simbolo : simbolos) {
                    System.out.println("  " + simbolo);
                }
            }
        }
        System.out.println("==========================");
    }
}
