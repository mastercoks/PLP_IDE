package model;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import javafx.scene.control.TextInputDialog;

public class Codigo {
	private ArrayList<String> linhas;
	private String linha;
	private Dados dados;

	public Codigo(ArrayList<String> linhas) {
		dados = new Dados();
		this.linhas = linhas;
	}

	public void insereLinha(String linha) {
		linhas.add(linha);
	}

	public ArrayList<Integer> getDados() {
		return dados.getDados();
	}

	// public String interpretaLinha(int index) {
	// String linhaTemp = linhas.get(index);
	// char[] caracteres = linhaTemp.toCharArray();
	// String argumento = "";
	// for (char caracter : caracteres) {
	// Character.getType(caracter);
	// if (caracter != 32) {
	// argumento += caracter;
	// } else if (argumento != "") {
	// switch (argumento) {
	// case "set":
	// System.out.println("Instrução set");
	// break;
	// case "jump":
	//
	// System.out.println("Instrução jump");
	// break;
	// case "jumpt":
	// System.out.println("Instrução jumpt");
	// break;
	// default:
	// System.out.println("Instrução desconhecida: " + argumento);
	// return null;
	// }
	// }
	// }
	// return argumento;
	// }

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

	public boolean analiseSintatica(int index) {
		// this.linha = linha;
		linha = linhas.get(index);
		switch (analiseLexica()) {
		case "set":
			String target = analiseLexica();
			String source;
			if (target.equals("write") && analiseLexica().equals(",")) {
				int num1 = analiseDado(analiseLexica());
				source = analiseLexica();
				while (!source.equals("fim")) {
					int num2 = analiseDado(analiseLexica());
					num1 = realizarOperacao(num1, num2, source);
					source = analiseLexica();
				}
				
				
			} else {
				try {
					int num = Integer.parseInt(target);
					if (analiseLexica().equals(",")) {
						source = analiseLexica();
						int numDado;
						if (source.equals("read")) {
							System.out.println("Digite um numero: ");
							Scanner scanner = new Scanner(System.in);
							numDado = scanner.nextInt();

						} else {
							numDado = analiseDado(source);
						}
						dados.setDado(num, numDado);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return true;
		// break;
		case "jump":
			try {
				int num = Integer.parseInt(analiseLexica());
				if (analiseLexica().equals("fim")) {
					// INSERIR CODIGO AQUI
					System.out.print(num);
					return true;
				}
			} catch (Exception e) {
				System.err.println("Erro sintatico na linha " + index);
			}

			break;
		case "jumpt":
			try {
				int num = Integer.parseInt(analiseLexica());
				if (analiseLexica().equals(",")) {
					int numDado1 = analiseDado(analiseLexica());
					String sinal = analiseLexica();
					int numDado2 = analiseDado(analiseLexica());
					if (analiseLexica().equals("fim")) {
						// INSERIR CODIGO AQUI
						if (checarComparacao(dados.getDado(numDado1), dados.getDado(numDado2), sinal)) {
							System.out.println("Compara\u00E7\u00E3o = true ");
						} else {
							System.out.println("Compara\u00E7\u00E3o = false ");
						}
						System.out.print(num + ", " + numDado1 + "(" + dados.getDado(numDado1) + ")" + sinal + numDado2
								+ "(" + dados.getDado(numDado2) + ")");
						return true;
					}
				}
			} catch (Exception e) {
				System.err.println("Erro sintatico na linha " + index);
			}

			return true;
		// break;
		default:
			break;
		}
		return false;
	}
	
	private int realizarOperacao(int num1, int num2, String sinal) {
		int resultado = 0;
		switch (sinal) {
		case "+":
			resultado = num1 + num2;
			break;
		case "-":
			resultado = num1 - num2;
			break;
		case "*":
			resultado = num1 * num2;
			break;
		case "/":
			resultado = num1 / num2;
		}
		return resultado;
	}

	private boolean checarComparacao(int num1, int num2, String sinal) {
		boolean resultado = false;
		switch (sinal) {
		case "=":
			resultado = num1 == num2;
			break;
		case ">":
			resultado = num1 > num2;
			break;
		case "<":
			resultado = num1 < num2;
			break;
		case "<=":
			resultado = num1 <= num2;
			break;
		case ">=":
			resultado = num1 >= num2;
		}
		return resultado;
	}

	private Integer analiseDado(String d) {
		if (d.equals("d") && analiseLexica().equals("[")) {
			int num = Integer.parseInt(analiseLexica());
			if (analiseLexica().equals("]")) {
				return num;
			}
		}
		return null;
	}

	public boolean analiseLexicaAux(int index) {
		linha = linhas.get(index);
		do {
			String temp = analiseLexica();
			if (temp != null) {
				System.out.print(temp);
			} else {
				System.err.println("\nErro l\u00E9xico encontrado!");
				return false;
			}
		} while (linha.length() != 0);
		return true;
	}

	private String analiseLexica() {
		String lexeme = "";
		Caracter nextChar = getChar();
		Caracter nextCharAux;

		if (nextChar == null) {
			return "fim";
		}

		while (nextChar.getCharClass().equals("Espaco")) {
			nextChar = getChar();
			if (nextChar == null) {
				return "fim";
			}
		}

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
				// if (nextChar.getCharClass().equals("Espaco")) {
				// lexeme = addChar(lexeme, nextChar.getNextChar());
				// } else
				if (nextCharAux != null) {
					linha = nextChar.getNextChar() + linha;
				}
				return lexeme;
			}
			break;
		// case "Espaco":
		// // nextChar = getChar();
		// return lexeme;
		case "Desconhecido":
			if (lookupChar(nextChar.getNextChar())) {
				lexeme = addChar(lexeme, nextChar.getNextChar());
				if (nextChar.getNextChar() == '>' || nextChar.getNextChar() == '<') {
					nextChar = getChar();
					if (nextChar.getNextChar() == '=') {
						lexeme = addChar(lexeme, nextChar.getNextChar());
					} else {
						linha = nextChar.getNextChar() + linha;
					}
				}
				return lexeme;
			}
			break;
		}
		return "error";
	}

	public static void main(String[] args) {
		Codigo cod = new Codigo(new ArrayList<>());
		// cod.insereLinha("SET 10 , D [ 10 ] ");
		// cod.insereLinha("jump 7 ");
		// cod.insereLinha("jump 100000 10");
		// cod.insereLinha("jump 24 ,");
		// cod.insereLinha("jump 10 set");
		// cod.insereLinha("jump read");

//		cod.insereLinha("set 0, read");
//		cod.insereLinha("set 1, read");
//		cod.insereLinha("set 1, d[0] + d[1]");
//		cod.insereLinha("set 1, d[0] - d[1]");
//		cod.insereLinha("set 1, d[0] * d[1]");
//		cod.insereLinha("set 1, d[0] / d[1]");
//		cod.insereLinha("set 1, d[0] + d[1]+ d[2]+ d[3]+ d[4]");
		cod.insereLinha("jumpt 8, d[0]=d[1]");
		cod.insereLinha("jumpt 6, d[0] <= d[1]");
		cod.insereLinha("jumpt 6, d[0] < d[1]");
		cod.insereLinha("jumpt 6, d[0] > d[1]");
		cod.insereLinha("jumpt 6, d[0] >= d[1]");
		cod.insereLinha("set 0, d[0] - d[1]");
		cod.insereLinha("jump 7");
		cod.insereLinha("set 1, d[1] -d[0]");
		cod.insereLinha("jump 2");
		cod.insereLinha("set write, d[0]");
		// cod.insereLinha("jumpt teste");
		// cod.interpretaLinha(0);
		System.out.println(" : " + cod.analiseSintatica(0));
		System.out.println(" : " + cod.analiseSintatica(1));
		System.out.println(" : " + cod.analiseSintatica(2));
		System.out.println(" : " + cod.analiseSintatica(3));
		System.out.println(" : " + cod.analiseSintatica(4));
		System.out.println(" : " + cod.analiseSintatica(5));
		System.out.println(" : " + cod.analiseSintatica(6));
		System.out.println(" : " + cod.analiseSintatica(7));
		System.out.println(" : " + cod.analiseSintatica(8));

		// 9 = d�gito
		// 2 = letra minuscula
		// 1 = letra maiscula
		// 25 = caracter especial < > =
		// 21 = abre chaves
		// 22 = fecha chaves
	}
}
