package model;

import java.util.ArrayList;

public class Dados {
	private ArrayList<Integer> dados;

	public Dados() {
		dados = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			dados.add((Integer) 0);
		}
	}

	public void setDado(int index, int dado) {
		dados.set(index, dado);
	}
	
	public Integer getDado(int index) {
		return dados.get(index);
	}
	
	public ArrayList<Integer> getDados() {
		return dados;
	}
}
