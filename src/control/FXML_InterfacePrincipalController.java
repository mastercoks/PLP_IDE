/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

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
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import model.Codigo;
import model.LinhaTabela;

/**
 * FXML Controller class
 *
 * @author Mathe
 */
public class FXML_InterfacePrincipalController implements Initializable {

	private ObservableList<Integer> linhas;
	private ObservableList<Integer> breakPoints;
	private ArrayList<String> arrayLinha;
	private Codigo codigo;
	private boolean fileSaved;
	private boolean visitou;
	private int LINHAEMEXECUSSAO;
	private Timeline timer1;
	private Timeline timer2;
	private final int ESPERA = 0;
	private final int ESPERA_CONTINUAR = 1;
	private final int PROXIMO = 2;

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
	@FXML
	private TextArea console;
	@FXML
	private TabPane tablePane;
	@FXML
	private Tab tabMain;
	@FXML
	private TableColumn<LinhaTabela, Integer> dados;
	@FXML
	private Button buttonNext;
	@FXML
	private Button buttonContinuar;
	@FXML
	private Button buttonPlay;
	@FXML
	private Button buttonStop;

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

	}

	@FXML
	public void salvarArquivo() throws IOException {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Salvar Arquivo");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));

		Window stage = null;
		File file = fileChooser.showSaveDialog(stage);

		try {
			if (file != null) {
				fileSaved = true;
				BufferedWriter buffWrite = new BufferedWriter(new FileWriter(file.getPath()));
				buffWrite.append(TextAreaCodigo.getText());
				buffWrite.close();
			}
		} catch (IOException e) {
			console.insertText(console.getLength(), "Erro na abertura do arquivo: " + e.getMessage() + "\n");
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
			ButtonType btnNao = new ButtonType("N\u00E3o");

			Alert dialogoExe = new Alert(Alert.AlertType.CONFIRMATION);
			dialogoExe.setTitle("Aviso");
			dialogoExe.setHeaderText("Arquivo Atual n\u00E3o foi salvo!");
			dialogoExe.setContentText("Deseja criar novo Arquivo?");
			dialogoExe.getButtonTypes().setAll(btnSim, btnNao);
			dialogoExe.showAndWait().ifPresent(b -> {
				if (b == btnSim) {
					TextAreaCodigo.clear();
					refreshSelection();
					fileSaved = false;
					sincroniaScrool();
				}
			});
		}
	}

	@FXML
	public void buscarArquivo() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Escolha seu Arquivo");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		Window stage = null;

		File file = fileChooser.showOpenDialog(stage);

		try {

			if (file != null) {
				// caminho do aquivo dentro da pasta do projeto
				FileReader arq = new FileReader(file.getAbsoluteFile());
				BufferedReader lerArq = new BufferedReader(arq);

				// a primeira linha a variavel "linha" recebe o valor "null"
				// quando
				// o processo de repeticao atingir o final do arquivo texto
				String linha = lerArq.readLine();
				String texto = "";

				// caminho do aquivo dentro da pasta do projeto
				while (linha != null) {

					if (linha.equals("halt")) {
						texto = texto + linha;
					} else {
						texto = texto + linha + "\n";
					}

					TextAreaCodigo.setText(texto);

					// da segunda ate a ultima linha
					linha = lerArq.readLine();

				}
				arq.close();
			}
		} catch (IOException e) {
			console.insertText(console.getLength(), "Erro na abertura do arquivo: " + e.getMessage() + "\n");
		}
		sincroniaScrool();

	}

	@FXML
	public void play() throws InterruptedException {
		codigo = new Codigo();
		refreshArrayLine();
		refreshDate();
		breakPoints.sorted();

		if (arrayLinha.get(arrayLinha.size() - 1).equals("halt")) {

			if (!arrayLinha.isEmpty()) {
				buttonStop.setDisable(false);
				buttonPlay.setDisable(true);
				TextAreaCodigo.setEditable(false);
				lvCodigo.setDisable(true);
				buttonNext.setDisable(true);
				buttonContinuar.setDisable(true);
				espera(LINHAEMEXECUSSAO);
			}
		} else {
			console.insertText(console.getLength(), "Erro, o programa n\u00E3o possui o marcador halt!\n");
		}

	}

	@FXML
	public void buttonNext() {
		if (arrayLinha.size() - 1 == LINHAEMEXECUSSAO) {// ultimo
			executar(PROXIMO, LINHAEMEXECUSSAO);
			buttonStop();
		} else {
			executar(PROXIMO, LINHAEMEXECUSSAO);
		}

	}

	@FXML
	public void buttonContinuar() {

		esperaContinuar(LINHAEMEXECUSSAO);
		refreshSelection();
		buttonNext.setDisable(true);
		buttonPlay.setDisable(true);
		buttonContinuar.setDisable(true);
	}

	@FXML
	public void buttonStop() {
		timer1.stop();
		LINHAEMEXECUSSAO = 0;
		lvCodigo.getSelectionModel().clearSelection();
		buttonPlay.setDisable(false);
		lvCodigo.setDisable(false);
		TextAreaCodigo.setEditable(true);
		buttonNext.setDisable(true);
		buttonContinuar.setDisable(true);
		buttonStop.setDisable(true);
		refreshDate();
		refreshArrayLine();
		refreshSelection();
	}

	@FXML
	public void buttonLimparConsole() {
		console.clear();
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
		}
		if (event.getClickCount() == 2) {

			breakPoints.add(lvCodigo.getSelectionModel().getSelectedItem());

			refreshSelection();

		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		linhas = FXCollections.observableArrayList();
		breakPoints = FXCollections.observableArrayList();
		arrayLinha = new ArrayList<String>();
		codigo = new Codigo();
		fileSaved = false;
		visitou = false;

		scrollPane.setFitToHeight(true);

		refreshDate();
		buttonContinuar.setDisable(true);
		buttonNext.setDisable(true);
		buttonStop.setDisable(true);

	}

	private void executar(int tipo, int index) {
		LINHAEMEXECUSSAO = index;
		boolean executou = codigo.executarLinha(arrayLinha.get(LINHAEMEXECUSSAO));
		if (!executou) {

			console.insertText(console.getLength(), "Error encontrado na linha " + LINHAEMEXECUSSAO + "\n");
			buttonStop();
		} else {
			console.insertText(console.getLength(), codigo.getResultado());
			int aux = realizarSalto();
			if (aux == LINHAEMEXECUSSAO) {
				lvCodigo.getSelectionModel().selectNext();
			}
			lvCodigo.getSelectionModel().clearSelection(aux);
			switch (tipo) {
			case ESPERA:
				espera(lvCodigo.getSelectionModel().getSelectedIndex());
				break;
			case ESPERA_CONTINUAR:
				timer1.stop();
				esperaContinuar(lvCodigo.getSelectionModel().getSelectedIndex());
				break;
			case PROXIMO:
				if (aux == LINHAEMEXECUSSAO) {
					LINHAEMEXECUSSAO = lvCodigo.getSelectionModel().getSelectedIndex();
				} else {
					lvCodigo.getSelectionModel().select(LINHAEMEXECUSSAO);
				}
				break;
			}
			refreshDate();
		}
	}

	public ObservableList<Integer> numLinhaVector(int NumLines) {
		linhas.clear();
		for (int i = 0; i < NumLines; i++) {
			linhas.add(i, i);
		}
		if (linhas.size() == 1) {
			breakPoints.clear();
		}
		return linhas;
	}

	public void refreshDate() {
		ObservableList<LinhaTabela> list = FXCollections.observableArrayList();
		for (int i = 0; i < codigo.getArrayDados().size(); i++) {
			list.add(new LinhaTabela(i, codigo.getArrayDados().get(i)));
		}

		id.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());

		dados.setCellValueFactory(cellData -> cellData.getValue().getDado().asObject());
		tabelaDados.setItems(list);
	}

	public void refreshSelection() {
		for (int i = 0; i < breakPoints.size(); i++) {

			lvCodigo.getSelectionModel().select(breakPoints.get(i));
		}
	}

	public void refreshArrayLine() {
		arrayLinha.clear();
		for (int i = 0; i < TextAreaCodigo.getParagraphs().size(); i++) {
			arrayLinha.add(TextAreaCodigo.getParagraphs().get(i).toString());
		}
	}

	private int realizarSalto() {
		Integer jump = codigo.getJump();
		if (jump != null) {
			int aux = LINHAEMEXECUSSAO;
			LINHAEMEXECUSSAO = jump;
			lvCodigo.getSelectionModel().select(jump);
			lvCodigo.getSelectionModel().clearSelection(jump + 1);
			refreshSelection();
			return aux;
		}
		return LINHAEMEXECUSSAO;
	}

	public void espera(int index) {
		lvCodigo.getSelectionModel().select(index);
		timer1 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), (e) -> {

			if (breakPoints.contains(index)) {
				buttonNext.setDisable(false);
				buttonContinuar.setDisable(false);
				LINHAEMEXECUSSAO = index;
				refreshDate();

			} else if (arrayLinha.size() - 1 != index) {
				executar(ESPERA, index);
			} else if (arrayLinha.size() - 1 == index) {
				console.insertText(console.getLength(), "Codigo finalizado!\n");
				buttonStop.setDisable(true);
				buttonStop();
			}

		}));
		timer1.play();
	}

	public void esperaContinuar(int index) {
		lvCodigo.getSelectionModel().select(index);
		timer2 = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), (e) -> {
			if (breakPoints.contains(index) && visitou) {
				refreshSelection();
				buttonNext.setDisable(false);
				buttonContinuar.setDisable(false);
				LINHAEMEXECUSSAO = index;
				refreshDate();
				visitou = false;

			} else if (arrayLinha.size() - 1 != index) {
				visitou = true;
				refreshSelection();
				LINHAEMEXECUSSAO = index;
				executar(ESPERA_CONTINUAR, index);
				refreshSelection();
			} else if (arrayLinha.size() - 1 == index) { // PARADA
				executar(ESPERA_CONTINUAR, index);
				timer2.stop();
				buttonStop();
			}

		}));
		timer2.play();
	}

	public void selectLinha(String LiString) {

		String linhaRecebida = LiString;
		String texto;
		texto = TextAreaCodigo.getText();

		TextAreaCodigo.setStyle("-fx-highlight-fill: lightgray; -fx-highlight-text-fill: firebrick; ");
		TextAreaCodigo.selectRange(texto.indexOf(linhaRecebida), texto.indexOf(linhaRecebida) + linhaRecebida.length());
	}

}
