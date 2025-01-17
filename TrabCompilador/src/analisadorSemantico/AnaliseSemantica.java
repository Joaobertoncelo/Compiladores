package analisadorSemantico;

import java.util.*;
import java.util.Map.Entry;

import analisadorSintatico.NodoAST;
import analisadorSintatico.NodoAST.Tipo;
import analisadorSintatico.SyntaxException;

public class AnaliseSemantica {
	private SymbolTable tabelaSimbolos;
	private Stack<ContextType> contextStack;
	private Symbol funcaoAtual;

	public AnaliseSemantica() {
		tabelaSimbolos = new SymbolTable();
		contextStack = new Stack<>();
		inicializarFuncoesEmbutidas();
	}

	private void inicializarFuncoesEmbutidas() {
	    try {
	        
	        List<TipoSemantico> consoleVarArgs = Arrays.asList(
	            TipoSemantico.STRING,
	            TipoSemantico.VARARGS
	        );
	        tabelaSimbolos.adicionarSimbolo("console", TipoSemantico.VOID, consoleVarArgs);

	
	        List<TipoSemantico> parametrosOverwatch1 = Arrays.asList(
	            TipoSemantico.STRING
	        );
	        tabelaSimbolos.adicionarSimbolo("overwatch", TipoSemantico.VOID, parametrosOverwatch1);

	    
	        List<TipoSemantico> parametrosOverwatch5 = Arrays.asList(
	            TipoSemantico.STRING,
	            TipoSemantico.INT,
	            TipoSemantico.FLOAT,
	            TipoSemantico.DOUBLE,
	            TipoSemantico.CHAR
	        );
	        tabelaSimbolos.adicionarSimbolo("overwatch", TipoSemantico.VOID, parametrosOverwatch5);

	        
	        List<TipoSemantico> parametrosOverwatch0 = new ArrayList<>();
	        tabelaSimbolos.adicionarSimbolo("overwatch", TipoSemantico.VOID, parametrosOverwatch0);

	        
	        List<TipoSemantico> parametrosOverwatch2 = Arrays.asList(
	            TipoSemantico.STRING,
	            TipoSemantico.INT
	        );
	        tabelaSimbolos.adicionarSimbolo("overwatch", TipoSemantico.VOID, parametrosOverwatch2);

	     
	        List<TipoSemantico> parametrosOverwatch3 = Arrays.asList(
	            TipoSemantico.STRING,
	            TipoSemantico.INT,
	            TipoSemantico.INT
	        );
	        tabelaSimbolos.adicionarSimbolo("overwatch", TipoSemantico.VOID, parametrosOverwatch3);

	       
	        List<TipoSemantico> parametrosSmoke = Arrays.asList(TipoSemantico.INT);
	        tabelaSimbolos.adicionarSimbolo("smoke", TipoSemantico.VOID, parametrosSmoke);

	        List<TipoSemantico> parametrosRush = Arrays.asList(TipoSemantico.INT, TipoSemantico.INT);
	        tabelaSimbolos.adicionarSimbolo("rush", TipoSemantico.VOID, parametrosRush);

	        List<TipoSemantico> parametrosBang = Arrays.asList(TipoSemantico.STRING);
	        tabelaSimbolos.adicionarSimbolo("bang", TipoSemantico.VOID, parametrosBang);

	        List<TipoSemantico> parametrosMolotov = new ArrayList<>();
	        tabelaSimbolos.adicionarSimbolo("molotov", TipoSemantico.VOID, parametrosMolotov);

	        List<TipoSemantico> somaParams = Arrays.asList(
	            TipoSemantico.INT,
	            TipoSemantico.INT
	        );
	        tabelaSimbolos.adicionarSimbolo("soma", TipoSemantico.INT, somaParams);

	    } catch (SemanticException e) {
	        System.err.println("Erro ao inicializar funções embutidas: " + e.getMessage());
	    }
	}


	public void analisar(NodoAST raiz) throws SemanticException, SyntaxException {
		visitar(raiz);
	}

	private void visitar(NodoAST nodo) throws SemanticException, SyntaxException {
		switch (nodo.getTipo()) {
		case PROGRAMA:
			
			List<TipoSemantico> parametrosMain = new ArrayList<>();
			tabelaSimbolos.adicionarSimbolo("main", TipoSemantico.INT, parametrosMain);

			List<Symbol> simbolosMain = tabelaSimbolos.buscarSimbolo("main");
			if (simbolosMain.isEmpty()) {
				throw new SemanticException("Função 'main' não encontrada após declaração.");
			}
			funcaoAtual = simbolosMain.get(0);

			contextStack.push(ContextType.FUNCTION);
			tabelaSimbolos.entrarEscopo();
			visitarBloco(nodo);
			tabelaSimbolos.sairEscopo();
			contextStack.pop();
			funcaoAtual = null;
			break;

		case BLOCO:
			visitarBloco(nodo);
			break;

		case DECLARACAO_VARIAVEL:
			visitarDeclaracaoVariavel(nodo);
			break;

		case DECLARACAO_VETOR: 
			visitarDeclaracaoVetor(nodo);
			break;

		case ATRIBUICAO:
			visitarAtribuicao(nodo);
			break;

		case ATRIBUICAO_ARRAY: 
			visitarAtribuicaoArray(nodo);
			break;

		case BANG:
			visitarIf(nodo);
			break;

		case MOLOTOV:
			visitarElse(nodo);
			break;

		case SMOKE:
			visitarWhile(nodo);
			break;

		case RUSH:
			visitarFor(nodo);
			break;

		case BAITER:
			visitarSwitch(nodo);
			break;

		case BAITA:
			visitarCase(nodo);
			break;

		case SETUP:
			visitarDefault(nodo);
			break;

		case BACKUP:
			visitarReturn(nodo);
			break;

		case BREAK:
			visitarBreak(nodo);
			break;

		case CHAMADA_FUNCAO:
			visitarChamadaFuncao(nodo);
			break;

		case FUNC_DECLARACAO:
			visitarFuncDeclaracao(nodo);
			break;

		case FUNC_DEFINICAO:
			visitarFuncDefinicao(nodo);
			break;

		
		case EXEC: 
			visitarExec(nodo);
			break;

		
		case EXPRESSAO_BINARIA:
		case EXPRESSAO_LITERAL:
		case EXPRESSAO_VARIAVEL:
		case FUNC_CALL:
			visitarExpressao(nodo);
			break;

		
		default:
			throw new SemanticException("Erro semântico: Tipo de nó desconhecido " + nodo.getTipo());
		}
	}

	
	private void visitarExec(NodoAST execNode) throws SemanticException, SyntaxException {
		
		for (NodoAST filho : execNode.getFilhos()) {
			visitar(filho); 
		}
	}

	private void visitarDeclaracaoVetor(NodoAST declVetor) throws SemanticException, SyntaxException {
		String nomeVetor = declVetor.getValor(); // ex: "nums"
		

		TipoSemantico tipoBase = mapTipo(declVetor.getFilhos().get(0).getValor());
		String tamanhoStr = declVetor.getFilhos().get(1).getValor();

		int tamanho;
		try {
			tamanho = Integer.parseInt(tamanhoStr);
		} catch (NumberFormatException e) {
			throw new SemanticException("Tamanho de vetor inválido: " + tamanhoStr);
		}
		if (tamanho <= 0) {
			throw new SemanticException("Tamanho de vetor deve ser maior que 0, encontrado: " + tamanho);
		}

		
		tabelaSimbolos.adicionarSimbolo(nomeVetor, tipoBase, new ArrayList<>());

		
		List<Symbol> encontrados = tabelaSimbolos.buscarSimbolo(nomeVetor);
		Symbol simboloArmazenado = encontrados.get(encontrados.size() - 1);
		simboloArmazenado.setArray(true);
		simboloArmazenado.setArraySize(tamanho);
	}

	private void visitarAtribuicaoArray(NodoAST atribArrayNode) throws SemanticException, SyntaxException {
		String nomeVetor = atribArrayNode.getValor(); 

		
		NodoAST indiceNode = atribArrayNode.getFilhos().get(0);
		NodoAST exprNode = atribArrayNode.getFilhos().get(1);

		List<Symbol> simbolos = tabelaSimbolos.buscarSimbolo(nomeVetor);
		Symbol vetorSymbol = null;
		for (Symbol s : simbolos) {
			if (!s.isFunction()) {
				vetorSymbol = s;
				break;
			}
		}
		if (vetorSymbol == null) {
			throw new SemanticException("Vetor '" + nomeVetor + "' não declarado.");
		}
		if (!vetorSymbol.isArray()) {
			throw new SemanticException("'" + nomeVetor + "' não é declarado como vetor.");
		}

		
		TipoSemantico tipoIndice = visitarExpressao(indiceNode);
		if (tipoIndice != TipoSemantico.INT) {
			throw new SemanticException("Índice do vetor deve ser INT, encontrado: " + tipoIndice);
		}

		
		TipoSemantico tipoValor = visitarExpressao(exprNode);
		if (!vetorSymbol.getTipo().isCompatible(tipoValor)) {
			throw new SemanticException("Tipo da expressão " + tipoValor + " não compatível com o tipo base do vetor "
					+ vetorSymbol.getTipo());
		}
	}



	private void visitarBloco(NodoAST bloco) throws SemanticException, SyntaxException {
		tabelaSimbolos.entrarEscopo();
		for (NodoAST comando : bloco.getFilhos()) {
			visitar(comando);
		}
		tabelaSimbolos.sairEscopo();
	}

	private void visitarDeclaracaoVariavel(NodoAST decl) throws SemanticException, SyntaxException {
		String nomeVar = decl.getValor();
		NodoAST tipoNodo = decl.getFilhos().get(0);
		TipoSemantico tipoVar = mapTipo(tipoNodo.getValor());

		tabelaSimbolos.adicionarSimbolo(nomeVar, tipoVar);

		if (decl.getFilhos().size() > 1) {
			NodoAST expr = decl.getFilhos().get(1);
			TipoSemantico tipoExpr = visitarExpressao(expr);
			if (!tipoVar.isCompatible(tipoExpr)) {
				throw new SemanticException("Erro semântico: Tipo da expressão '" + tipoExpr
						+ "' não compatível com o tipo da variável '" + tipoVar + "'.");
			}
		}
	}

	private void visitarAtribuicao(NodoAST atrib) throws SemanticException, SyntaxException {
		String nomeVar = atrib.getValor();
		List<Symbol> simbolos = tabelaSimbolos.buscarSimbolo(nomeVar);

		Symbol simbolo = null;
		for (Symbol s : simbolos) {
			if (!s.isFunction()) {
				simbolo = s;
				break;
			}
		}
		if (simbolo == null) {
			throw new SemanticException("Erro semântico: variável '" + nomeVar + "' não declarada.");
		}
		if (simbolo.isArray()) {
			throw new SemanticException("Tentando atribuir diretamente a array '" + nomeVar
					+ "' sem índice. Use ATRIBUICAO_ARRAY para 'nome[indice]' ou declare sem array.");
		}

		NodoAST expr = atrib.getFilhos().get(0);
		TipoSemantico tipoExpr = visitarExpressao(expr);
		if (!simbolo.getTipo().isCompatible(tipoExpr)) {
			throw new SemanticException("Erro semântico: Tipo da expressão '" + tipoExpr
					+ "' não compatível com o tipo da variável '" + simbolo.getTipo() + "'.");
		}
	}

	private TipoSemantico visitarExpressao(NodoAST expr) throws SemanticException, SyntaxException {
	    switch (expr.getTipo()) {
	    
	        case EXPRESSAO_LITERAL:
	            return mapLiteralTipo(expr.getValor());

	        case EXPRESSAO_VARIAVEL:
	            
	            String nomeVar = expr.getValor();
	            List<Symbol> simbolosVar = tabelaSimbolos.buscarSimbolo(nomeVar);
	            Symbol simboloVar = null;
	            for (Symbol s : simbolosVar) {
	                if (!s.isFunction()) {
	                    simboloVar = s;
	                    break;
	                }
	            }
	            if (simboloVar == null) {
	                throw new SemanticException("Erro semântico: variável '" + nomeVar + "' não declarada.");
	            }

	          

	            return simboloVar.getTipo();

	        case EXPRESSAO_BINARIA:
	            return analisarExpressaoBinaria(expr);

	        case FUNC_CALL:
	            return visitarFuncCall(expr);

	        case EXPRESSAO_VETOR:
	            return visitarExpressaoVetor(expr);

	       
	        case EXPRESSAO_PONTEIRO:
	            return visitarExpressaoPonteiro(expr);

	        default:
	            throw new SemanticException(
	                "Erro semântico: Expressão desconhecida (" + expr.getTipo() + ")."
	            );
	    }
	}

	private TipoSemantico visitarExpressaoVetor(NodoAST exprVetor) throws SemanticException, SyntaxException {
		String nomeArray = exprVetor.getValor(); 
		
		List<Symbol> simbolos = tabelaSimbolos.buscarSimbolo(nomeArray);
		Symbol arraySymbol = null;
		for (Symbol s : simbolos) {
			if (!s.isFunction()) {
				arraySymbol = s;
				break;
			}
		}
		if (arraySymbol == null) {
			throw new SemanticException("Vetor '" + nomeArray + "' não declarado.");
		}
		if (!arraySymbol.isArray()) {
			throw new SemanticException("'" + nomeArray + "' não é declarado como vetor.");
		}
		
		NodoAST indiceNode = exprVetor.getFilhos().get(0);
		TipoSemantico tipoIndice = visitarExpressao(indiceNode);
		if (tipoIndice != TipoSemantico.INT) {
			throw new SemanticException("Índice do vetor deve ser INT, encontrado: " + tipoIndice);
		}
		
		return arraySymbol.getTipo();
	}
	
	private TipoSemantico visitarExpressaoPonteiro(NodoAST ponteiroNode) throws SemanticException, SyntaxException {
	    
	    NodoAST base = ponteiroNode.getFilhos().get(0);
	    TipoSemantico baseType = visitarExpressao(base);

	    
	    switch (baseType) {
	        case INT:
	            return TipoSemantico.INT_PTR;
	        case FLOAT:
	            return TipoSemantico.FLOAT_PTR;
	        case DOUBLE:
	            return TipoSemantico.DOUBLE_PTR;
	        case CHAR:
	            return TipoSemantico.CHAR_PTR;
	        
	        default:
	            
	            return TipoSemantico.INT_PTR; 
	    }
	}

	private TipoSemantico analisarExpressaoBinaria(NodoAST expr) throws SemanticException, SyntaxException {
		String op = expr.getValor();
		if (op.equals("++") || op.equals("--") || op.equals("!")) {
			
			if (expr.getFilhos().size() < 1) {
				throw new SemanticException("Operador unário '" + op + "' com operandos incorretos.");
			}
			NodoAST operando = expr.getFilhos().get(0);
			TipoSemantico tipoOperando = visitarExpressao(operando);
			switch (op) {
			case "++":
			case "--":
				verificarTipoIncremento(tipoOperando);
				return tipoOperando;
			case "!":
				verificarTipoUnario(tipoOperando);
				return TipoSemantico.BOOLEAN;
			default:
				throw new SemanticException("Operador unário desconhecido: " + op);
			}
		} else {
			
			if (expr.getFilhos().size() < 2) {
				throw new SemanticException("Operador binário '" + op + "' com operandos incorretos.");
			}
			NodoAST esquerda = expr.getFilhos().get(0);
			NodoAST direita = expr.getFilhos().get(1);
			TipoSemantico tipoEsquerda = visitarExpressao(esquerda);
			TipoSemantico tipoDireita = visitarExpressao(direita);

			switch (op) {
			case "+":
			case "-":
			case "*":
			case "/":
			case "%":
				return verificarTipoAritmetico(tipoEsquerda, tipoDireita);
			case "==":
			case "!=":
			case "<":
			case ">":
			case "<=":
			case ">=":
				verificarCompatibilidadeComparacao(tipoEsquerda, tipoDireita);
				return TipoSemantico.BOOLEAN;
			case "&&":
			case "||":
				verificarTipoBooleano(tipoEsquerda, tipoDireita);
				return TipoSemantico.BOOLEAN;
			default:
				throw new SemanticException("Operador desconhecido: " + op);
			}
		}
	}

	private TipoSemantico visitarFuncCall(NodoAST funcCallNode) throws SemanticException, SyntaxException {
		String nomeFuncao = funcCallNode.getValor();
		List<Symbol> simbolosFunc = tabelaSimbolos.buscarSimbolo(nomeFuncao);

		if (simbolosFunc == null || simbolosFunc.isEmpty()) {
			throw new SemanticException("Função '" + nomeFuncao + "' não declarada.");
		}

		List<NodoAST> argumentos = funcCallNode.getFilhos(); // cada um é uma expressão
		List<TipoSemantico> tiposArgumentos = new ArrayList<>();
		for (NodoAST arg : argumentos) {
			tiposArgumentos.add(visitarExpressao(arg));
		}

		
		for (Symbol simboloFunc : simbolosFunc) {
			List<TipoSemantico> parametros = simboloFunc.getParametros();
			boolean isVarArgs = !parametros.isEmpty() && parametros.get(parametros.size() - 1) == TipoSemantico.VARARGS;

			if (isVarArgs) {
				if (tiposArgumentos.size() < parametros.size() - 1) {
					continue;
				}
				boolean match = true;
				for (int i = 0; i < parametros.size() - 1; i++) {
					if (i >= tiposArgumentos.size()) {
						match = false;
						break;
					}
					if (!parametros.get(i).isCompatible(tiposArgumentos.get(i))) {
						match = false;
						break;
					}
				}
				if (match) {
					return simboloFunc.getTipo();
				}
			} else {
				if (parametros.size() != tiposArgumentos.size()) {
					continue;
				}
				boolean match = true;
				for (int i = 0; i < parametros.size(); i++) {
					if (!parametros.get(i).isCompatible(tiposArgumentos.get(i))) {
						match = false;
						break;
					}
				}
				if (match) {
					return simboloFunc.getTipo();
				}
			}
		}
		throw new SemanticException("Função '" + nomeFuncao + "' chamada com " + tiposArgumentos.size()
				+ " argumentos, mas nenhuma assinatura corresponde.");
	}

	private void visitarIf(NodoAST ifNode) throws SemanticException, SyntaxException {
		NodoAST cond = ifNode.getFilhos().get(0);
		TipoSemantico tipoCond = visitarExpressao(cond);
		if (tipoCond != TipoSemantico.BOOLEAN) {
			throw new SemanticException("Erro semântico: Condição do 'if' deve ser do tipo BOOLEAN.");
		}

		NodoAST blocoIf = ifNode.getFilhos().get(1);
		visitar(blocoIf);

		if (ifNode.getFilhos().size() > 2) {
			NodoAST blocoElse = ifNode.getFilhos().get(2);
			visitarElse(blocoElse);
		}
	}

	private void visitarElse(NodoAST blocoElse) throws SemanticException, SyntaxException {
		if (blocoElse.getTipo() != NodoAST.Tipo.BLOCO) {
			throw new SemanticException("Erro semântico: O 'else' deve conter um bloco de comandos.");
		}
		visitar(blocoElse);
	}

	private void visitarWhile(NodoAST whileNode) throws SemanticException, SyntaxException {
		NodoAST cond = whileNode.getFilhos().get(0);
		TipoSemantico tipoCond = visitarExpressao(cond);
		if (tipoCond != TipoSemantico.BOOLEAN) {
			throw new SemanticException("Erro semântico: Condição do 'while' deve ser do tipo BOOLEAN.");
		}

		contextStack.push(ContextType.LOOP);
		NodoAST bloco = whileNode.getFilhos().get(1);
		visitar(bloco);
		contextStack.pop();
	}

	private void visitarFor(NodoAST forNode) throws SemanticException, SyntaxException {

		NodoAST decl = forNode.getFilhos().get(0);
		visitar(decl);

		NodoAST cond = forNode.getFilhos().get(1);
		TipoSemantico tipoCond = visitarExpressao(cond);
		if (tipoCond != TipoSemantico.BOOLEAN) {
			throw new SemanticException("Erro semântico: Condição do 'for' deve ser do tipo BOOLEAN.");
		}

		NodoAST incremento = forNode.getFilhos().get(2);
		visitar(incremento);

		NodoAST bloco = forNode.getFilhos().get(3);
		contextStack.push(ContextType.LOOP);
		visitar(bloco);
		contextStack.pop();
	}

	private void visitarSwitch(NodoAST switchNode) throws SemanticException, SyntaxException {
		NodoAST expr = switchNode.getFilhos().get(0);
		visitarExpressao(expr);

		contextStack.push(ContextType.SWITCH);
		for (int i = 1; i < switchNode.getFilhos().size(); i++) {
			NodoAST caso = switchNode.getFilhos().get(i);
			visitar(caso);
		}
		contextStack.pop();
	}

	private void visitarCase(NodoAST caseNode) throws SemanticException, SyntaxException {
		for (NodoAST comando : caseNode.getFilhos()) {
			visitar(comando);
		}
	}

	private void visitarDefault(NodoAST defaultNode) throws SemanticException, SyntaxException {
		for (NodoAST comando : defaultNode.getFilhos()) {
			visitar(comando);
		}
	}

	private void visitarReturn(NodoAST returnNode) throws SemanticException, SyntaxException {
		System.out.println("Analisando 'backup' dentro do contexto: " + contextStack);
		if (contextStack.isEmpty() || !contextStack.peek().isFunction()) {
			throw new SemanticException("Erro semântico: 'return' fora de uma função.");
		}

		NodoAST expr = returnNode.getFilhos().get(0);
		TipoSemantico tipoExpr = visitarExpressao(expr);

		if (funcaoAtual == null) {
			throw new SemanticException("Erro semântico: Função atual não encontrada durante análise de 'return'.");
		}

		if (!funcaoAtual.getTipo().isCompatible(tipoExpr)) {
			throw new SemanticException("Erro semântico: Tipo do 'return' (" + tipoExpr
					+ ") não compatível com o tipo da função (" + funcaoAtual.getTipo() + ").");
		}
	}

	private void visitarBreak(NodoAST breakNode) throws SemanticException, SyntaxException {
		if (contextStack.isEmpty()
				|| !(contextStack.peek() == ContextType.LOOP || contextStack.peek() == ContextType.SWITCH)) {
			throw new SemanticException("Erro semântico: 'break' fora de um loop ou switch.");
		}
	}

	private void visitarChamadaFuncao(NodoAST funcaoNode) throws SemanticException, SyntaxException {
		String funcao = funcaoNode.getValor();
		List<Symbol> simbolosFunc = tabelaSimbolos.buscarSimbolo(funcao);

		boolean isFunction = false;
		for (Symbol simbolo : simbolosFunc) {
			if (simbolo.isFunction()) {
				isFunction = true;
				break;
			}
		}

		if (!isFunction) {
			throw new SemanticException("Erro semântico: '" + funcao + "' não é uma função.");
		}

		visitarFuncCall(funcaoNode);
	}

	private void visitarFuncDeclaracao(NodoAST funcDeclNode) throws SemanticException, SyntaxException {
		NodoAST tipoReturnNodo = funcDeclNode.getFilhos().get(0);
		TipoSemantico tipoReturn = mapTipo(tipoReturnNodo.getValor());

		String nomeFunc = funcDeclNode.getValor();

		NodoAST paramsNodo = funcDeclNode.getFilhos().get(1);
		List<TipoSemantico> tiposParametros = visitarParametros(paramsNodo);

		tabelaSimbolos.adicionarSimbolo(nomeFunc, tipoReturn, tiposParametros);
	}

	private void visitarFuncDefinicao(NodoAST funcDefNode) throws SemanticException, SyntaxException {
		NodoAST tipoReturnNodo = funcDefNode.getFilhos().get(0);
		TipoSemantico tipoReturn = mapTipo(tipoReturnNodo.getValor());

		String nomeFunc = funcDefNode.getValor();

		NodoAST paramsNodo = funcDefNode.getFilhos().get(1);
		List<TipoSemantico> tiposParametros = visitarParametros(paramsNodo);

		List<Symbol> simbolosFunc = tabelaSimbolos.buscarSimbolo(nomeFunc);
		for (Symbol simboloFunc : simbolosFunc) {
			if (simboloFunc.isFunction() && simboloFunc.getTipo().equals(tipoReturn)
					&& simboloFunc.getParametros().equals(tiposParametros)) {
				throw new SemanticException(
						"Erro semântico: Função '" + nomeFunc + "' já foi definida com esta assinatura.");
			}
		}

		tabelaSimbolos.adicionarSimbolo(nomeFunc, tipoReturn, tiposParametros);

		List<Symbol> funcs = tabelaSimbolos.buscarSimbolo(nomeFunc);
		Symbol funcSymbol = null;
		for (Symbol s : funcs) {
			if (s.isFunction() && s.getTipo().equals(tipoReturn) && s.getParametros().equals(tiposParametros)) {
				funcSymbol = s;
				break;
			}
		}

		if (funcSymbol == null) {
			throw new SemanticException(
					"Erro semântico: Não foi possível recuperar a função definida '" + nomeFunc + "'.");
		}

		funcaoAtual = funcSymbol;

		contextStack.push(ContextType.FUNCTION);
		tabelaSimbolos.entrarEscopo();

		for (int i = 0; i < tiposParametros.size(); i++) {
			NodoAST paramNodo = paramsNodo.getFilhos().get(i);
			String nomeParam = paramNodo.getValor();
			TipoSemantico tipoParam = tiposParametros.get(i);
			tabelaSimbolos.adicionarSimbolo(nomeParam, tipoParam);
		}

		NodoAST corpoFunc = funcDefNode.getFilhos().get(2);
		visitar(corpoFunc);

		tabelaSimbolos.sairEscopo();
		contextStack.pop();

		funcaoAtual = null;
	}

	private List<TipoSemantico> visitarParametros(NodoAST paramsNodo) throws SemanticException, SyntaxException {
		List<TipoSemantico> tiposParametros = new ArrayList<>();
		for (NodoAST param : paramsNodo.getFilhos()) {
			String tipoStr = param.getFilhos().get(0).getValor();
			TipoSemantico tipo = mapTipo(tipoStr);
			tiposParametros.add(tipo);
		}
		return tiposParametros;
	}

	private TipoSemantico mapLiteralTipo(String valor) throws SemanticException {
		if (isInteger(valor)) {
			return TipoSemantico.INT;
		} else if (isFloat(valor)) {
			return TipoSemantico.FLOAT;
		} else if (isDouble(valor)) {
			return TipoSemantico.DOUBLE;
		} else if (isChar(valor)) {
			return TipoSemantico.CHAR;
		} else if (isBoolean(valor)) {
			return TipoSemantico.BOOLEAN;
		} else if (isString(valor)) {
			return TipoSemantico.STRING;
		} else {
			throw new SemanticException("Erro semântico: Literal desconhecido '" + valor + "'.");
		}
	}

	private TipoSemantico mapTipo(String tipoStr) throws SemanticException {
		switch (tipoStr.toLowerCase()) {
		case "int":
			return TipoSemantico.INT;
		case "float":
			return TipoSemantico.FLOAT;
		case "double":
			return TipoSemantico.DOUBLE;
		case "char":
			return TipoSemantico.CHAR;
		case "troll":
			return TipoSemantico.TROLL;
		case "boolean":
			return TipoSemantico.BOOLEAN;
		case "void":
			return TipoSemantico.VOID;
		case "string":
			return TipoSemantico.STRING;
		default:
			throw new SemanticException("Erro semântico: Tipo desconhecido '" + tipoStr + "'.");
		}
	}

	private TipoSemantico verificarTipoAritmetico(TipoSemantico esquerda, TipoSemantico direita)
			throws SemanticException {
		if (esquerda == TipoSemantico.INT && direita == TipoSemantico.INT) {
			return TipoSemantico.INT;
		} else if ((esquerda == TipoSemantico.INT || esquerda == TipoSemantico.FLOAT)
				&& (direita == TipoSemantico.INT || direita == TipoSemantico.FLOAT)) {
			return TipoSemantico.FLOAT;
		} else if ((esquerda == TipoSemantico.INT || esquerda == TipoSemantico.FLOAT
				|| esquerda == TipoSemantico.DOUBLE)
				&& (direita == TipoSemantico.INT || direita == TipoSemantico.FLOAT
						|| direita == TipoSemantico.DOUBLE)) {
			return TipoSemantico.DOUBLE;
		} else {
			throw new SemanticException("Erro semântico: Tipos incompatíveis para operação aritmética: " + esquerda
					+ " e " + direita + ".");
		}
	}

	private void verificarCompatibilidadeComparacao(TipoSemantico esquerda, TipoSemantico direita)
			throws SemanticException {
		if (!esquerda.isCompatibleForComparison(direita)) {
			throw new SemanticException("Erro semântico: Operador de comparação usado com tipos incompatíveis: "
					+ esquerda + " e " + direita + ".");
		}
	}

	private void verificarTipoBooleano(TipoSemantico esquerda, TipoSemantico direita) throws SemanticException {
		if (esquerda != TipoSemantico.BOOLEAN || direita != TipoSemantico.BOOLEAN) {
			throw new SemanticException(
					"Erro semântico: Operadores lógicos '&&' e '||' requerem operandos do tipo BOOLEAN.");
		}
	}

	private void verificarTipoUnario(TipoSemantico tipo) throws SemanticException {
		if (tipo != TipoSemantico.BOOLEAN) {
			throw new SemanticException("Erro semântico: Operador unário '!' requer operando do tipo BOOLEAN.");
		}
	}

	private void verificarTipoIncremento(TipoSemantico tipo) throws SemanticException {
		if (!(tipo == TipoSemantico.INT || tipo == TipoSemantico.FLOAT || tipo == TipoSemantico.DOUBLE)) {
			throw new SemanticException(
					"Erro semântico: Operador de incremento '++' ou '--' requer operando de tipo INT, FLOAT ou DOUBLE.");
		}
	}

	private boolean isInteger(String valor) {
		try {
			Integer.parseInt(valor);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isFloat(String valor) {
		try {
			Float.parseFloat(valor);
			return valor.contains(".") && !valor.contains("f");
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isDouble(String valor) {
		try {
			Double.parseDouble(valor);
			return valor.contains(".");
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isChar(String valor) {
		return valor.length() == 3 && valor.startsWith("'") && valor.endsWith("'");
	}

	private boolean isBoolean(String valor) {
		return valor.equals("true") || valor.equals("false");
	}

	private boolean isString(String valor) {
		return valor.startsWith("\"") && valor.endsWith("\"");
	}

	public void imprimirTabelaSimbolos() {
		System.out.println("=== Tabela de Símbolos ===");
		List<Map<String, List<Symbol>>> escopos = tabelaSimbolos.getPilhaEscopos();
		for (int i = escopos.size() - 1; i >= 0; i--) {
			Map<String, List<Symbol>> escopo = escopos.get(i);
			System.out.println("Escopo " + (i + 1) + ":");
			for (Map.Entry<String, List<Symbol>> entry : escopo.entrySet()) {
				String nome = entry.getKey();
				List<Symbol> simbolos = entry.getValue();
				for (Symbol simbolo : simbolos) {
					if (simbolo.isFunction()) {
						System.out.println("  Função: " + simbolo.getNome() + " -> " + simbolo.getTipo()
								+ " com parâmetros " + simbolo.getParametros());
					} else {
						System.out.println("  Variável: " + simbolo.getNome() + " -> " + simbolo.getTipo());
					}
				}
			}
		}
		System.out.println("==========================");
	}
}
