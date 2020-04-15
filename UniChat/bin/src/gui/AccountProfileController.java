package gui;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.Client;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Friend;

/**
 * This class is the controller for account profile page. 
 * 
 * @author Jian Tang
 * @author Rumit Mehta
 */
public class AccountProfileController extends Application{
	
	static Client client;
	FXMLLoader loader;
	
	 public AccountProfileController(UpdateAccountProfileController UpdateAccountProfileController){
	    	client = UpdateAccountProfileController.client;
	    	loader = UpdateAccountProfileController.loader;
	    	try {
				start(UpdateAccountProfileController.stage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	
	@Override
	public void start(Stage arg0) throws Exception {
		try {
			Stage stage = new Stage();
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../AccountProfile.css").toExternalForm());
			stage.setTitle("Account Profile");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
