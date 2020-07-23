package RSC.FakeNewsProject;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import RSC.FakeNewsProject.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField alpha;

    @FXML
    private TextField beta;

    @FXML
    private TextField verifica;

    @FXML
    private TextField oblio;

    @FXML
    private TextField believerIniziali;

    @FXML
    private ComboBox<String> tipoGrafo;

    @FXML
    private TextField numVertici;

    @FXML
    private TextField numArchi;

    @FXML
    private Button simulatore;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSimula(ActionEvent event) {
    	try {
    	double a = Double.parseDouble(alpha.getText());
    	double b = Double.parseDouble(beta.getText());
    	double pVerify = Double.parseDouble(verifica.getText());
    	double pOblio = Double.parseDouble(oblio.getText());
    	
    	int iniziali = Integer.parseInt(believerIniziali.getText());
    	int numV = Integer.parseInt(numVertici.getText());
    	int numA = Integer.parseInt(numArchi.getText());
    	
    	if (!verifyValues(a))
    		throw new NullPointerException("Il parametro alpha non è stato inserito o il campo non è valido");
    	if (!verifyValues(b))
    		throw new NullPointerException("Il parametro beta non è stato inserito o il campo non è valido");
    	if (!verifyValues(pOblio))
    		throw new NullPointerException("Il parametro della probabilità di Oblio non è stato inserito o il campo non è valido");
    	if (!verifyValues(pVerify))
    		throw new NullPointerException("Il parametro della probabilità di verifica non è stato inserito o il campo non è valido");
    	if (!verifyValues(iniziali))
    		throw new NullPointerException("Il parametro di diffusori iniziali non è stato inserito o il campo non è valido");
    	if (!verifyValues(numV) || !verifyValues(numA))
    		throw new NullPointerException("Il numero di archi o vertici non è stato inserito o il campo non è valido");
    	
    	if (tipoGrafo.getSelectionModel().getSelectedItem().equals("BarabasiAlbert"))
    		model.creaBarabasiAlbertGrafo(numV, numA);
    	else if (tipoGrafo.getSelectionModel().getSelectedItem().equals("ErdosRenyi"))
    		model.creaErdosRenyiGrafo(numV, numA);
    	
    	model.simulate(iniziali, 100, a, b, pVerify, pOblio);
    	
    	int k = 0;
    	for (Map<String, Integer> nodo : model.getSimulatorResults()) {
    		txtResult.appendText(String.format("\t Istante temporale %d \t\n", k++));
    		txtResult.appendText(String.format("Sono presenti:\n"));
    		
    		for (Entry<String, Integer> entry : nodo.entrySet())
    			txtResult.appendText(String.format("%d %s\n", entry.getValue(), entry.getKey()));
    	}
    	
    	} catch (NumberFormatException nfe) {
    		txtResult.setText("Non è stato inserito un valore numerico.");
    	}
    	catch(Exception e) {
    		txtResult.setText(e.getMessage());
    	}
    	
    	
    	

    }
    
    public boolean verifyValues(double value) {
    	if (value > 1.0 || value < 0.0)
    		return false;
    	return true;	
    }
    
    public boolean verifyValues(int value) {
    	if (value <= 0)
    		return false;
    	return true;	
    }

    @FXML
    void initialize() {
        assert alpha != null : "fx:id=\"alpha\" was not injected: check your FXML file 'Scene.fxml'.";
        assert beta != null : "fx:id=\"beta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert verifica != null : "fx:id=\"verifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert oblio != null : "fx:id=\"oblio\" was not injected: check your FXML file 'Scene.fxml'.";
        assert believerIniziali != null : "fx:id=\"believerIniziali\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tipoGrafo != null : "fx:id=\"tipoGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert numVertici != null : "fx:id=\"numVertici\" was not injected: check your FXML file 'Scene.fxml'.";
        assert numArchi != null : "fx:id=\"numArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert simulatore != null : "fx:id=\"simulatore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model m) {
    	this.model = m;
    	this.tipoGrafo.getItems().add("BarabasiAlbert");
    	this.tipoGrafo.getItems().add("ErdosRenyi");
    	this.txtResult.setText("Attenzione: l'applicazione visiva non si comporta in maniera corretta.\nVerificare il codice o utilizzare il terminale per visualizzare risultati consistenti");
    }
}
