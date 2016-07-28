/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.sun.javafx.collections.ElementObservableListDecorator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabBuilder;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javax.print.attribute.IntegerSyntax;

/**
 * FXML Controller class
 *
 * @author Mathe
 */
public class FXML_InterfacePrincipalController implements Initializable {

    ObservableList<Integer> Lines = FXCollections.observableArrayList();
    ObservableList<Integer> breakPoints = FXCollections.observableArrayList();

    boolean fileSaved = false;

    @FXML
    private ListView<Integer> lvCodigo;
    @FXML
    private TextArea TextAreaCodigo;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TableView<LinhaTabela> tabelaDados;
    @FXML
    private TableColumn<LinhaTabela, Integer> id;

    ListView<Integer> selected = new ListView<>();
    @FXML
    private TextArea console;
    @FXML
    private TabPane tablePane;
    @FXML
    private Tab tabMain;

    public ObservableList numLinhaVector(int NumLines) {
        Lines.clear();

        for (int i = 0; i < NumLines; i++) {
            Lines.add(i, i);
        }
        if (Lines.size() == 1) {

            breakPoints.clear();
        }
        return Lines;
    }

    @FXML
    public void sincroniaScrool() {
        // ScrollBar scrollBarv = (ScrollBar) TextAreaCodigo.lookup(".scroll-bar:vertical");
        int size = TextAreaCodigo.getParagraphs().size() * 20;

        if (scrollPane.getHeight() <= size) {
            TextAreaCodigo.setPrefHeight(size);
            scrollPane.setFitToHeight(false);
        } else {

            scrollPane.setFitToHeight(true);
        }
        lvCodigo.setFixedCellSize(19.1);
        scrollPane.setVvalue(scrollPane.getVmax() + 10);
        lvCodigo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvCodigo.setItems(numLinhaVector(TextAreaCodigo.getParagraphs().size()));
        refreshSelection();
        //Lines.add(index, Integer.SIZE);
//        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ObservableList<Integer> data = FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // ObservableList<String> codigo = FXCollections.observableArrayList(
        // "Teste 1","Teste 2","Teste 3","Teste 4","Teste 5");
        // tbCodigo.setItems(FXCollections.observableArrayList(codigo));
        // tbCodigo.getColumns().add(tcCodigo);
        // tbCodigo.setItems(codigo);
        //lvCodigo.setItems(data);
        // lvCodigo.setSelectionModel(new MultipleSelectionModel<Integer>);
        //lvCodigo.setDisable(true);
        ObservableList<LinhaTabela> list = FXCollections.observableArrayList();
        scrollPane.setFitToHeight(true);
        LinhaTabela dadosTabela = new LinhaTabela(0, 0);

        for (int i = 0; i < 10; i++) {
            list.add(new LinhaTabela(i, 0));
        }

        id.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());

        tabelaDados.setItems(list);

    }

    public void refreshSelection() {
        for (int i = 0; i < breakPoints.size(); i++) {

            lvCodigo.getSelectionModel().select(breakPoints.get(i));
        }
    }

    @FXML
    public void salvarArquivo() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salve Arquivo");

        Window stage = null;
        File file = fileChooser.showSaveDialog(stage);

        try {
            if (file != null) {

                fileSaved = true;
                BufferedWriter buffWrite = new BufferedWriter(new FileWriter(file.getPath() + ".txt"));
                buffWrite.append(TextAreaCodigo.getText());
                buffWrite.close();
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

    }

    @FXML
    public void novo() {
        if (fileSaved) {
            TextAreaCodigo.clear();
            refreshSelection();
            fileSaved = false;
            sincroniaScrool();
        } else {
            ButtonType btnSim = new ButtonType("Sim");
            ButtonType btnNao = new ButtonType("Não");

            Alert dialogoExe = new Alert(Alert.AlertType.CONFIRMATION);
            dialogoExe.setTitle("Aviso");
            dialogoExe.setHeaderText("Arquivo Atual não foi salvo!");
            dialogoExe.setContentText("Deseja criar novo Arquivo?");
            dialogoExe.getButtonTypes().setAll(btnSim, btnNao);
            dialogoExe.showAndWait().ifPresent(b -> {
                if (b == btnSim) {
                    TextAreaCodigo.clear();
                    refreshSelection();
                    fileSaved = false;
                    sincroniaScrool();

                } else if (b == btnNao) {

                }
            });

        }

    }

    @FXML
    public void buscarArquivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha seu Arquivo");
        Window stage = null;

        File file = fileChooser.showOpenDialog(stage);

        try {

            if (file != null) {
                FileReader arq = new FileReader(file.getAbsoluteFile());// caminho do aquivo dentro da pasta do projeto
                BufferedReader lerArq = new BufferedReader(arq);

                String linha = lerArq.readLine(); // lê a primeira linha // a variável "linha" recebe o valor "null" quando o processo // de repetição atingir o final do arquivo texto 

                String texto = "";
                while (linha != null) {// caminho do aquivo dentro da pasta do projeto
                    //System.out.printf("%s\n", linha);

                    texto = texto + linha + "\n";
                    TextAreaCodigo.setText(texto);

                    linha = lerArq.readLine(); // lê da segunda até a última linha 

                }
                arq.close();
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        sincroniaScrool();

    }

    @FXML
    private void selectBreakPoints(MouseEvent event) {
        lvCodigo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        if (event.getClickCount() == 1) {
            if (breakPoints.contains(lvCodigo.getSelectionModel().getSelectedIndex())) {
                breakPoints.remove(lvCodigo.getSelectionModel().getSelectedItem());
                lvCodigo.getSelectionModel().clearSelection();
                refreshSelection();
            } else {

                lvCodigo.getSelectionModel().clearSelection(lvCodigo.getSelectionModel().getSelectedIndex());
                refreshSelection();
            }
            console.setText(breakPoints.toString());
        }
        if (event.getClickCount() == 2) {

            breakPoints.add(lvCodigo.getSelectionModel().getSelectedItem());

            refreshSelection();
            console.setText(breakPoints.toString());

//            lvCodigo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
//
//                public void changed(ObservableValue<? extends Integer> obs, Integer ov, Integer nv) {                
//                    // selected.setItems(lvCodigo.getSelectionModel().getSelectedItems());
//                    lvCodigo.getSelectionModel().selectIndices(ov, nv);
//                    
//                }
//            });
        }
    }
}
