package analisadorSemantico;

public enum ContextType {
    FUNCTION,
    LOOP,
    SWITCH;

    public boolean isFunction() {
        return this == FUNCTION;
    }
}
