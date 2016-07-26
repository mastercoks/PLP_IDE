/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.sun.javafx.collections.ElementObservableListDecorator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javax.swing.JTextArea;

/**
 * FXML Controller class
 *
 * @author Mathe
 */
public class FXML_InterfacePrincipalController implements Initializable {

    
    ObservableList<Integer> Lines =  FXCollections.observableArrayList() ;
    
    @FXML
	private ListView<Integer> lvCodigo;
    @FXML
    private TextArea TextAreaCodigo;
    
    private JTextArea TempText = new JTextArea();
    private ScrollPane scrool;
    
    
    
    public  ObservableList numLinhaVector(int NumLines){
        Lines.clear();
        for (int i = 0; i < NumLines; i++) {
            Lines.add(i,i);
        }
        return Lines;
    }
    
   
    
    @FXML
    public void sincroniaScrool(){
        
       
        
 
               
        lvCodigo.setFixedCellSize(20.1);
        TempText.setText(TextAreaCodigo.getText());
       
        numLinhaVector(TempText.getLineCount());
        lvCodigo.setItems(Lines);
               
        
        //Lines.add(index, Integer.SIZE);
        lvCodigo.scrollTo((int) TextAreaCodigo.getScrollTop());
        
      
    }
    
    
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ObservableList<Integer> data = FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		// ObservableList<String> codigo = FXCollections.observableArrayList(
		// "Teste 1","Teste 2","Teste 3","Teste 4","Teste 5");
		// tbCodigo.setItems(FXCollections.observableArrayList(codigo));
		// tbCodigo.getColumns().add(tcCodigo);
		// tbCodigo.setItems(codigo);
		//lvCodigo.setItems(data);
                 
                
	}
        

}
