package gui;

import client.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddGroupsController extends Application{

	static Client client;
	FXMLLoader loader;

    public AddGroupsController(UpdateAddGroups updateAddGroups){
    	client = updateAddGroups.client;
    	loader = updateAddGroups.loader;
    	try {
			start(updateAddGroups.stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	public void start(Stage arg0) throws Exception {
		Stage stage = new Stage();
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("../AddGroups.css").toExternalForm());
		stage.setTitle("Add Groups");
		stage.setScene(scene);
		stage.show();		
	}
	

}
