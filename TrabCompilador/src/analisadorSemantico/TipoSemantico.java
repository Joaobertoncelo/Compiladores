package analisadorSemantico;

public enum TipoSemantico {
    INT,
    FLOAT,
    DOUBLE,
    CHAR,
    STRING,    
    TROLL,
    BOOLEAN,
    VOID, 
    INT_PTR, FLOAT_PTR, 
    DOUBLE_PTR, 
    CHAR_PTR, 
    VARARGS;

    

	 public boolean isCompatibleForComparison(TipoSemantico outro) {
	        
	        if (this.isNumeric() && outro.isNumeric()) {
	            return true;
	        }
	        
	        if (this == outro) {
	            return true;
	        }
	        
	        return false;
	    }

	   
	    public boolean isNumeric() {
	        return this == INT || this == FLOAT || this == DOUBLE;
	    }

	    
    public boolean isCompatible(TipoSemantico other) {
        if (this == other) {
            return true;
        }
       
        if ((this == FLOAT || this == DOUBLE) && (other == FLOAT || other == DOUBLE)) {
            return true;
        }
      
        if (this == INT && (other == FLOAT || other == DOUBLE)) {
            return true;
        }
        if (other == INT && (this == FLOAT || this == DOUBLE)) {
            return true;
        }
        
        return false;
    }
}
