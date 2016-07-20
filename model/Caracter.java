package model;

public class Caracter {
	private char nextChar;
	private String charClass;
	
	public Caracter(char nextChar, String charClass) {
		this.nextChar = nextChar;
		this.charClass = charClass;
	}

	public char getNextChar() {
		return nextChar;
	}

	public void setNextChar(char nextChar) {
		this.nextChar = nextChar;
	}

	public String getCharClass() {
		return charClass;
	}

	public void setCharClass(String charClass) {
		this.charClass = charClass;
	}
	
	

}
