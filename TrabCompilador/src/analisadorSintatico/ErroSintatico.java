package analisadorSintatico;

public class ErroSintatico extends RuntimeException {
    public ErroSintatico(String mensagem) {
        super(mensagem);
    }
}
