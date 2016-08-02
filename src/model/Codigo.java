package model;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Codigo {
	private String linha;
	private Dados dados;
	private String resultado;
	private Integer jump;

	public Codigo() {
		dados = new Dados();
		resultado = "";
	}

	public Dados getDados() {
		return dados;
	}

	public Integer getJump() {
		Integer jump = this.jump;
		this.jump = null;
		return jump;
	}

	public String getResultado() {
		return resultado;
	}

	public ArrayList<Integer> getArrayDados() {
		return dados.getDados();
	}

	private boolean lookupString(String lexeme) {
		return lexeme.equalsIgnoreCase("set") || lexeme.equalsIgnoreCase("jump") || lexeme.equalsIgnoreCase("jumpt")
				|| lexeme.equalsIgnoreCase("write") || lexeme.equalsIgnoreCase("read") || lexeme.equalsIgnoreCase("d")
				|| lexeme.equalsIgnoreCase("halt");
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

	public boolean executarLinha(String l) {

		if (l.equals("")){
			return true;
		}
		linha = l;
		switch (analiseLexica()) {
		case "halt":
			resultado = "Codigo finalizado!\n";
			return true;
		case "set":
			String target = analiseLexica();
			String source;
			if (target.equals("write") && analiseLexica().equals(",")) {
				int num1 = analiseDado(analiseLexica());
				if (analiseLexica().equals("fim")) {
					// System.out.println("write: " + num1);
					resultado = ("d[" + num1 + "] = " + dados.getDado(num1) + "\n");
					return true;
				}
			} else {
				try {
					int targetInt = Integer.parseInt(target);
					if (analiseLexica().equals(",")) {
						source = analiseLexica();
						Integer num1 = null;
						String input = "";
						if (source.equals("read")) {
							while (num1 == null && input != null) {
								try {
									input = JOptionPane.showInputDialog("Digite um numero: ");
									num1 = Integer.parseInt(input);
								} catch (Exception e) {
									resultado = "Erro, Entre com um numero inteiro.\n";
								}
							}

						} else {
							num1 = dados.getDado(analiseDado(source));
							source = analiseLexica();
							while (!source.equals("fim")) {
								int num2 = dados.getDado(analiseDado(analiseLexica()));
								num1 = realizarOperacao(num1, num2, source);
								source = analiseLexica();
							}
						}
						// System.out.println("set " + targetInt + ", " + num1);
						dados.setDado(targetInt, num1);
						return true;
					}
				} catch (Exception e) {
					break;
				}
			}
			break;
		case "jump":
			try {
				int num = Integer.parseInt(analiseLexica());
				if (analiseLexica().equals("fim")) {
					// INSERIR CODIGO AQUI
					jump = num;
					// System.out.print(num);
					return true;
				}
			} catch (Exception e) {
				break;
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
							// System.out.println("Compara\u00E7\u00E3o = true :
							// jump " + num);
							jump = num;
						} else {
							// System.out.println("Compara\u00E7\u00E3o = false
							// ");
						}
						// System.out.print(num + ", " + numDado1 + "(" +
						// dados.getDado(numDado1) + ")" + sinal + numDado2
						// + "(" + dados.getDado(numDado2) + ")");
						return true;
					}
				}
			} catch (Exception e) {
				break;
			}
		}
//		System.err.println("Erro na linha " + index);
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
}
