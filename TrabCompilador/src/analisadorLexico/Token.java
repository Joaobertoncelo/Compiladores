package analisadorLexico;

public class Token {
    private TokenType type;
    private String valor;

    public Token(TokenType type, String valor) {
        this.type = type;
        this.valor = valor;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", valor='" + valor + '\'' +
                '}';
    }
}