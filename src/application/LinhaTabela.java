/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Wandeson
 */
public class LinhaTabela {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty dado;
    public LinhaTabela(int id, int dado) {
        this.id = new SimpleIntegerProperty(id);
        
        this.dado = new SimpleIntegerProperty(dado);
        
    }
    public SimpleIntegerProperty getId() {
        return id;
    }

    public void setId(SimpleIntegerProperty id) {
        this.id = id;
    }

    public SimpleIntegerProperty getDado() {
        return dado;
    }

    public void setDado(SimpleIntegerProperty dado) {
        this.dado = dado;
    }
   

    
}
