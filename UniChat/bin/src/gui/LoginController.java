package gui;
import java.io.IOException;

import client.Client;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The controller class of Login page.
 *
 * @author Jian Tang
 * @author Rumit Mehta
 */
public class LoginController extends Application { 

	static Client client;
	
	@FXML
    private Button loginButton;

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Label signupButton;

    @FXML
	public void login() throws IOException {		
		String username = loginUsername.getText();
		String password = loginPassword.getText();
		client.login(username, password);
		
		// If login successfully, jump into the main page. 
		if(client.isLoggedIn) {
			System.out.println("Login Success");
			UpdateAccountProfileController update = new UpdateAccountProfileController(client);
			new AccountProfileController(update);
			this.client.updateAccountProfileController = update;
			client.findFriends();
			// Deletes the login page once logged in
			signupButton.getScene().getWindow().hide();
			System.out.println("end of loginController method");
		} else {
			System.out.println("Login failure");
		}

	}

    @FXML
	public void signup() throws IOException {
    	
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("../Signup.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("../Signup.css").toExternalForm());
		stage.setTitle("Sign Up Page");
		stage.setScene(scene);
		stage.show();	
		SignupController.passClient(client);	
		signupButton.getScene().getWindow().hide(); // Deletes the login page once click sign up button
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../Login.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../Login.css").toExternalForm());
			System.out.println("the login gui has opened");
			stage.setTitle("Login");
			stage.setScene(scene); 
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * When closing the page, this will properly sever the connection from the
	 * client and server
	 */
	@Override
	public void stop() {
		System.out.println("Stopping GUI");
		//if(client != null)
		client.close();
	}
	
	public void passClient(Client client) {
		LoginController.client = client;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
