package application;

import java.util.List;

import analisadorLexico.Token;
import analisadorLexico.analiseLexica;

public class Teste {

	public static void main(String[] args) {
		 String caminhoArquivo = "D:\\Faculdade\\teste.txt"; 
	        analiseLexica analisador = new analiseLexica(caminhoArquivo);
	        List<Token> tokens = analisador.analisar();
	        for (Token token : tokens) {
	            System.out.println(token);
	        }
	}

}
