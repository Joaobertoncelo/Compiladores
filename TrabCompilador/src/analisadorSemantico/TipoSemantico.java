package analisadorSemantico;

public enum TipoSemantico {
    INT,
    FLOAT,
    DOUBLE,
    CHAR,
    STRING,    // Adicionado
    TROLL,
    BOOLEAN,
    VOID, 
    INT_PTR, FLOAT_PTR, 
    DOUBLE_PTR, 
    CHAR_PTR, 
    VARARGS;

    /**
     * Verifica se o tipo atual é compatível com outro tipo.
     *
     * @param other O outro tipo a ser verificado.
     * @return true se os tipos forem compatíveis, false caso contrário.
     */
	
	 public boolean isCompatibleForComparison(TipoSemantico outro) {
	        // Comparações entre tipos numéricos são permitidas com promoção
	        if (this.isNumeric() && outro.isNumeric()) {
	            return true;
	        }
	        // Comparações entre tipos idênticos
	        if (this == outro) {
	            return true;
	        }
	        // Adicione outras regras de compatibilidade conforme necessário
	        return false;
	    }

	    /**
	     * Verifica se o tipo é numérico.
	     */
	    public boolean isNumeric() {
	        return this == INT || this == FLOAT || this == DOUBLE;
	    }

	    
    public boolean isCompatible(TipoSemantico other) {
        if (this == other) {
            return true;
        }
        // Regras de compatibilidade
        // Por exemplo, FLOAT e DOUBLE são compatíveis entre si
        if ((this == FLOAT || this == DOUBLE) && (other == FLOAT || other == DOUBLE)) {
            return true;
        }
        // INT pode ser compatível com FLOAT e DOUBLE em certas operações
        if (this == INT && (other == FLOAT || other == DOUBLE)) {
            return true;
        }
        if (other == INT && (this == FLOAT || this == DOUBLE)) {
            return true;
        }
        // Adicione outras regras conforme necessário
        return false;
    }
}
