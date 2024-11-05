package application;

import java.util.List;

import analisadorLexico.Token;
import analisadorLexico.analiseLexica;

public class Teste {

	public static void main(String[] args) {
		String codigoFonte =   "int round() {  // Palavra-chave \"round\" para iniciar o main\n" +
	            "            int x = 10;      // Palavra-chave \"int\" para declarar a variável\n" +
	            "            float y = 5.5;   // Palavra-chave \"float\" para declarar a variável\n" +
	            "            char z = 'a';    // Palavra-chave \"char\" para declarar a variável\n" +
	            "\n" +
	            "            bang (x > 5) {     // Palavra-chave \"bang\" (equivalente ao \"if\")\n" +
	            "                molotov;     // Palavra-chave \"molotov\" (equivalente ao \"else\")\n" +
	            "            }\n" +
	            "\n" +
	            "            rush (x < 10) {  // Palavra-chave \"rush\" (equivalente ao \"for\")\n" +
	            "                x = x + 1;\n" +
	            "            }\n" +
	            "\n" +
	            "            smoke (x != 10) {  // Palavra-chave \"smoke\" (equivalente ao \"while\")\n" +
	            "                y = y + 2.0;\n" +
	            "            }\n" +
	            "\n" +
	            "            baiter (x == 5) {  // Palavra-chave \"baiter\" (equivalente ao \"switch\")\n" +
	            "                baita 1:        // Palavra-chave \"baita\" (equivalente ao \"case\")\n" +
	            "                    x = x + 2;\n" +
	            "                    antrush;\n" +
	            "                baita 2:\n" +
	            "                    y = y + 3.0;\n" +
	            "                    antrush;\n" +
	            "                setup:\n" +
	            "                    z = 'b';\n" +
	            "                    antrush;\n" +
	            "            }\n" +
	            "\n" +
	            "            troll;             // Palavra-chave \"troll\" (equivalente ao \"void\")\n" +
	            "            backup x;          // Palavra-chave \"return\" (para retornar um valor)\n" +
	            "        }\n" +
	            "    }\n" +
	            "}\n";
		analiseLexica analisador = new analiseLexica(codigoFonte);
		List<Token> tokens = analisador.analisar();

		for (Token token : tokens) {
			if (token != null) {
				System.out.println(token);
			}
		}

	}

}
