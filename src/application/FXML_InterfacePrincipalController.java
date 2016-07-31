/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.Codigo;

/**
 * FXML Controller class
 *
 * @author Mathe
 */
public class FXML_InterfacePrincipalController implements Initializable {

    ObservableList<Integer> Lines = FXCollections.observableArrayList();
    ObservableList<Integer> breakPoints = FXCollections.observableArrayList();
    ArrayList<String> ArrayLinha = new ArrayList<String>();
    Codigo codigo = new Codigo(ArrayLinha);
    boolean fileSaved = false;

    @FXML
    public ListView<Integer> lvCodigo;
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
    @FXML
    private TableColumn<LinhaTabela, Integer> dados;
    @FXML
    private Button butonNext;
    @FXML
    private Button butonContinuar;
    @FXML
    private Button butonPlay;
    @FXML
    private Button buttonStop;

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

    int LINHAEMEXECUSSAO;

    @FXML
    public void sincroniaScrool() {
        
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
       
//        
    }

    public void refreshDate() {
        ObservableList<LinhaTabela> list = FXCollections.observableArrayList();
        for (int i = 0; i < codigo.getDados().size(); i++) {
            list.add(new LinhaTabela(i, codigo.getDados().get(i)));
        }

        id.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());

        dados.setCellValueFactory(cellData -> cellData.getValue().getDado().asObject());
        tabelaDados.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        scrollPane.setFitToHeight(true);
        
        refreshDate();
        butonContinuar.setDisable(true);
        butonNext.setDisable(true);
        buttonStop.setDisable(true);

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

    public void refreshArrayLine() {
        ArrayLinha.clear();
        for (int i = 0; i < TextAreaCodigo.getParagraphs().size(); i++) {
            ArrayLinha.add(TextAreaCodigo.getParagraphs().get(i).toString());
        }
    }

    @FXML
    public void play() throws InterruptedException {
        refreshArrayLine();
        System.out.println(ArrayLinha);
        refreshDate();
        breakPoints.sorted();

        Codigo codigo = new Codigo(ArrayLinha);

        if (ArrayLinha.get(ArrayLinha.size() - 1).equals("halt")) {
            if (!ArrayLinha.isEmpty()) {
                buttonStop.setDisable(false);
                butonPlay.setDisable(true);
                TextAreaCodigo.setEditable(false);
                lvCodigo.setDisable(true);
                butonNext.setDisable(true);
                butonContinuar.setDisable(true);
                espera(LINHAEMEXECUSSAO);

            }
        } else {
            Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
            dialogoInfo.setTitle("Diálogo de informação");
            dialogoInfo.setHeaderText("Seu Programa não possui marcador de Fim(halt)");
            dialogoInfo.setContentText("Verifique se a ultima Linha não está em branco");
            dialogoInfo.showAndWait();
        }

    }
    Timeline timer;

    public void espera(int index) {
        lvCodigo.getSelectionModel().select(index);
        timer = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), (e) -> {

            if (breakPoints.contains(index)) {
                butonNext.setDisable(false);
                butonContinuar.setDisable(false);
                LINHAEMEXECUSSAO = index;
                refreshDate();
                //selectLinha(ArrayLinha.get(index));

            } else if (ArrayLinha.size() - 1 != index) {
                LINHAEMEXECUSSAO = index;
                System.out.println(LINHAEMEXECUSSAO);
               
                lvCodigo.getSelectionModel().selectNext();
                lvCodigo.getSelectionModel().clearSelection(index);
                espera(lvCodigo.getSelectionModel().getSelectedIndex());
                refreshDate();
            }else if(ArrayLinha.size() - 1 == index){
               buttonStop.setDisable(true);
               System.out.println("PARADA");
                buttonStop();
            }

        }));
        timer.play();
    }

    @FXML
    public void buttonNext() {
        
        if (breakPoints.contains(LINHAEMEXECUSSAO)) {
           
            lvCodigo.getSelectionModel().selectNext();

            LINHAEMEXECUSSAO = lvCodigo.getSelectionModel().getSelectedIndex();
            refreshDate();
        }
        else if (ArrayLinha.size() - 1 == LINHAEMEXECUSSAO) {
            LINHAEMEXECUSSAO = lvCodigo.getSelectionModel().getSelectedIndex();
            lvCodigo.getSelectionModel().clearSelection();
            butonPlay.setDisable(false);
            lvCodigo.setDisable(false);
            TextAreaCodigo.setEditable(true);
            butonNext.setDisable(false);
            refreshDate();
            refreshArrayLine();
            refreshSelection();
            refreshDate();
        } else {
           

            lvCodigo.getSelectionModel().select(LINHAEMEXECUSSAO + 1);
            lvCodigo.getSelectionModel().clearSelection(LINHAEMEXECUSSAO);
            refreshSelection();
            LINHAEMEXECUSSAO = lvCodigo.getSelectionModel().getSelectedIndex();
            refreshDate();
        }

    }
    @FXML
    public void buttonContinuar(){
        
        esperaContinuar(LINHAEMEXECUSSAO);
        refreshSelection();
        timer.play();
        butonNext.setDisable(true);
        butonPlay.setDisable(true);
        butonContinuar.setDisable(true);
    }
    Timeline timer2;
        public void esperaContinuar(int index) {
        lvCodigo.getSelectionModel().select(index);
        timer2 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), (e) -> {

            if (breakPoints.contains(index+1)) {
                refreshSelection();
                butonNext.setDisable(false);
                butonContinuar.setDisable(false);
                LINHAEMEXECUSSAO = index;
                refreshDate();
               
                //selectLinha(ArrayLinha.get(index));

            } else if (ArrayLinha.size() - 1 != index) {
                refreshSelection();
                LINHAEMEXECUSSAO = index;
                System.out.println(LINHAEMEXECUSSAO);
                
                lvCodigo.getSelectionModel().selectNext();
                lvCodigo.getSelectionModel().clearSelection(index);
                espera(lvCodigo.getSelectionModel().getSelectedIndex());
                refreshSelection();
                refreshDate();
           
            }else if(ArrayLinha.size() - 1 == index){
                refreshSelection();
               buttonStop.setDisable(true);
               System.out.println("PARADA");
                buttonStop();
                timer2.stop();
                
                butonContinuar.setDisable(true);
            }

        }));
        timer2.play();
    }
    @FXML
    public void buttonStop() {
        timer.stop();
        LINHAEMEXECUSSAO = 0;
        lvCodigo.getSelectionModel().clearSelection();
        butonPlay.setDisable(false);
        lvCodigo.setDisable(false);
        TextAreaCodigo.setEditable(true);
        butonNext.setDisable(true);
        refreshDate();
        refreshArrayLine();
        refreshSelection();
    }

    public void selectLinha(String LiString) {

        String linhaRecebida = LiString;
        String texto;
        texto = TextAreaCodigo.getText();

        TextAreaCodigo.setStyle("-fx-highlight-fill: lightgray; -fx-highlight-text-fill: firebrick; ");
        TextAreaCodigo.selectRange(texto.indexOf(linhaRecebida), texto.indexOf(linhaRecebida) + linhaRecebida.length());
    }

    @FXML
    private void selectBreakPoints(MouseEvent event) {
        lvCodigo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        breakPoints.sorted();
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

        }
    }

}
