package it.polito.tdp.meteo;

import java.net.URL;
import java.time.Month;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {
	private Model model;
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Month> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		txtResult.clear();
		Month mese = boxMese.getValue();
		
		if (mese == null) {
			txtResult.setText("Selezionare un mese.");
			return;
		}
		int numeroMese=mese.getValue();

		String s = "";
		s= model.trovaSequenza(numeroMese);
		txtResult.appendText(s);

//
	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		txtResult.clear();
		
			Month mese = boxMese.getValue();

			
			if (mese == null) {
				txtResult.setText("Selezionare un mese.");
				return;
			}
			int numeroMese=mese.getValue();

			String s = "";
			s= model.getUmiditaMedia(numeroMese);
//			StringBuilder sb = new StringBuilder();
//			for (Studente studente : studenti) {
//				sb.append(String.format("%-10s ", studente.getMatricola()));
//				sb.append(String.format("%-20s ", studente.getCognome()));
//				sb.append(String.format("%-20s ", studente.getNome()));
//				sb.append(String.format("%-10s ", studente.getCDS()));
//				sb.append("\n");
//			}
	

			txtResult.appendText(s);
	
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
		for(int mese = 1; mese <= 12 ; mese ++)
			boxMese.getItems().add(Month.of(mese)) ;
		txtResult.setStyle("-fx-font-family: monospace");
		
	}
    void setModel(Model model) {
        
    	this.model=model;
    	
    }



}
