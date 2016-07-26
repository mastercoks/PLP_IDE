package model;

import java.util.ArrayList;

public class Codigo {
	private ArrayList<String> linhas;
	private String linha;

	public Codigo() {
		linhas = new ArrayList<>();
	}

	public void insereLinha(String linha) {
		linhas.add(linha);
	}

	public String interpretaLinha(int index) {
		String linhaTemp = linhas.get(index);
		char[] caracteres = linhaTemp.toCharArray();
		String argumento = "";
		for (char caracter : caracteres) {
			Character.getType(caracter);
			if (caracter != 32) {
				argumento += caracter;
			} else if (argumento != "") {
				switch (argumento) {
				case "set":
					System.out.println("Instrução set");
					break;
				case "jump":

					System.out.println("Instrução jump");
					break;
				case "jumpt":
					System.out.println("Instrução jumpt");
					break;
				default:
					System.out.println("Instrução desconhecida: " + argumento);
					return null;
				}
			}
		}
		return argumento;
	}

	private boolean lookupString(String lexeme) {
		return lexeme.equalsIgnoreCase("set") || lexeme.equalsIgnoreCase("jump") || lexeme.equalsIgnoreCase("jumpt")
				|| lexeme.equalsIgnoreCase("write") || lexeme.equalsIgnoreCase("read") || lexeme.equalsIgnoreCase("d");
	}

	private boolean lookupChar(char nextChar) {
		return nextChar == '>' || nextChar == '<' || nextChar == '=' || nextChar == '[' || nextChar == ']'
				|| nextChar == ',' || nextChar == '+' || nextChar == '-' || nextChar == '*' || nextChar == '/';
	}

	private Caracter getChar() {
		char charAux;
		Caracter tecla;
		try {
			charAux = linha.charAt(0);
			linha = linha.substring(1, linha.length());

			if (Character.getType(charAux) == 9) {
				// numero
				tecla = new Caracter(charAux, "Digito");
			} else if (Character.getType(charAux) == 2 || Character.getType(charAux) == 1) {
				// letra
				tecla = new Caracter(charAux, "Letra");
			} else if (Character.isSpaceChar(charAux)) {
				tecla = new Caracter(charAux, "Espaco");
			} else {
				tecla = new Caracter(charAux, "Desconhecido");
			}
		} catch (Exception e) {
			tecla = null;
		}

		return tecla;
	}

	private String addChar(String lexeme, char nextChar) {
		lexeme += nextChar;
		return lexeme;
	}

	public boolean analiseLexicaAux(int index) {
		linha = linhas.get(index);
		do {
			String temp = analiseLexica();
			if (temp != null) {
				System.out.print(temp);
			} else {
				System.err.println("\nErro léxico encontrado!");
				return false;
			}
		} while (linha.length() != 0);
		return true;
	}

	private String analiseLexica() {
		String lexeme = "";
		Caracter nextChar = getChar();
		Caracter nextCharAux;
		switch (nextChar.getCharClass()) {
		case "Digito":
			do {
				lexeme = addChar(lexeme, nextChar.getNextChar());
				nextCharAux = getChar();
				if (nextCharAux != null) {
					nextChar = nextCharAux;
				}
			} while (nextCharAux != null && nextChar.getCharClass().equals("Digito"));
			if (nextCharAux != null) {
				linha = nextChar.getNextChar() + linha;
			}
			return lexeme;
		case "Letra":
			do {
				lexeme = addChar(lexeme, nextChar.getNextChar());
				nextCharAux = getChar();
				if (nextCharAux != null) {
					nextChar = nextCharAux;
				}
			} while ((nextCharAux != null && nextChar.getCharClass() == "Digito")
					|| (nextCharAux != null && nextChar.getCharClass() == "Letra"));

			if (lookupString(lexeme)) {
				if (nextChar.getCharClass().equals("Espaco")) {
					lexeme = addChar(lexeme, nextChar.getNextChar());
				} else if (nextCharAux != null) {
					linha = nextChar.getNextChar() + linha;
				}
				return lexeme;
			}
			break;
		case "Espaco":
			// nextChar = getChar();
			return lexeme;
		case "Desconhecido":
			if (lookupChar(nextChar.getNextChar())) {
				lexeme = addChar(lexeme, nextChar.getNextChar());
				return lexeme;
			}
			break;
		}
		return null;
	}

	public static void main(String[] args) {
		Codigo cod = new Codigo();
		// cod.insereLinha("SET 10 , D [ 10 ] ");
		cod.insereLinha("set 0, read");
		cod.insereLinha("set 1, read");
		cod.insereLinha("jumpt 8, d[0]=d[1]");
		cod.insereLinha("jumpt 6, d[0] <= d[1]");
		cod.insereLinha("set 0, d[0] - d[1]");
		cod.insereLinha("jump 7");
		cod.insereLinha("set 1, d[1] -d[0]");
		cod.insereLinha("jump 2");
		cod.insereLinha("set write, d[0]");
		// cod.insereLinha("jumpt teste");
		// cod.interpretaLinha(0);
		System.out.println(" : " + cod.analiseLexicaAux(0));
		System.out.println(" : " + cod.analiseLexicaAux(1));
		System.out.println(" : " + cod.analiseLexicaAux(2));
		System.out.println(" : " + cod.analiseLexicaAux(3));
		System.out.println(" : " + cod.analiseLexicaAux(4));
		System.out.println(" : " + cod.analiseLexicaAux(5));
		System.out.println(" : " + cod.analiseLexicaAux(6));
		System.out.println(" : " + cod.analiseLexicaAux(7));
		System.out.println(" : " + cod.analiseLexicaAux(8));

		// 9 = d�gito
		// 2 = letra minuscula
		// 1 = letra maiscula
		// 25 = caracter especial < > =
		// 21 = abre chaves
		// 22 = fecha chaves
	}
}
