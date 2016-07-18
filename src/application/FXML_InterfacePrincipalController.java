/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.table.TableColumn;

//import javax.swing.table.TableColumn;
//import javax.swing.text.TabableView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Mathe
 */
public class FXML_InterfacePrincipalController implements Initializable {

   
    @FXML
    private ListView<Integer> simplesemCode;
    @FXML
    private ListView<Integer> breakpoints;
    @FXML
    private ListView<Integer> dados;
    @FXML
    private ListView<Integer> saida;
//    @FXML
//    private TableView<String> tbCodigo;
//    @FXML
//    private TableColumn tcCodigo;
    		
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Integer> data = FXCollections.observableArrayList(
        null,0,1,2,3,4,5,6,7,8,9,10
        );
//        ObservableList<String> codigo = FXCollections.observableArrayList(
//        		"Teste 1","Teste 2","Teste 3","Teste 4","Teste 5");
//        tbCodigo.setItems(FXCollections.observableArrayList(codigo));
//        tbCodigo.getColumns().add(tcCodigo);
//        tbCodigo.setItems(codigo);
        simplesemCode.setItems(data);
        breakpoints.setItems(data);
        dados.setItems(data);
        saida.setItems(data);
    }    
    
    public void actionButton() {
    	saida.getFocusModel().focusNext();
        System.out.println(saida.getFocusModel().getFocusedItem());
    }
    
    
    
    
    
}
