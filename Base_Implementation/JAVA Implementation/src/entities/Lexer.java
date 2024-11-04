package entities;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c)) {
                token.append(c);
            } else if (c == '(' || c == ')' || c == '+' || c == '-' || c == '*' || c == '/') {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add(Character.toString(c));
            } else if (Character.isWhitespace(c)) {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
            } else {
                throw new IllegalArgumentException("Caractere inválido: " + c);
            }
        }
        if (token.length() > 0) {
            tokens.add(token.toString());
        }
        return tokens;
    }
}
