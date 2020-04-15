package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.User;



public class UpdateAddFriends {
	
	@FXML private TableView<User> userTable;
	@FXML private TableColumn<User, String> firstname;
	@FXML private TableColumn<User, String> surname;
	@FXML private TableColumn<User, String> username;
    @FXML private TableColumn<User, String> courseName;
    @FXML private TableColumn<User, Button> button;
    
    @FXML private Button changeToGroup;
    @FXML private Button backButton;
    

    @FXML
    private Button SearchButton;

    @FXML
    private TextField searchFirstname;

    @FXML
    private TextField searchSurname;

    @FXML
    private TextField searchCourseName;

    @FXML
    private TextField searchUsername;

    @FXML
    void changeToGroup(ActionEvent event) {
    	UpdateAddGroups update = new UpdateAddGroups(client);
		new AddGroupsController(update);
		client.updateAddGroups = update;
		changeToGroup.getScene().getWindow().hide();	
    }

	Client client;
	public FXMLLoader loader;
	public Stage stage;
	public ObservableList<User> users; 
	static List<User> userList = new ArrayList<>();
	
	public UpdateAddFriends(Client client){
		this.client = client;
		loader = new FXMLLoader(getClass().getResource("../AddFriends.fxml"));
		loader.setController(this);
		this.stage = new Stage();
		
	}
	
	public void updateTable() {
		userTable.getItems().clear();
		users = FXCollections.observableArrayList(userList);
		firstname.setCellValueFactory(new PropertyValueFactory<User, String> ("firstname"));
		surname.setCellValueFactory(new PropertyValueFactory<User, String> ("surname"));
		username.setCellValueFactory(new PropertyValueFactory<User, String> ("username"));
		courseName.setCellValueFactory(new PropertyValueFactory<User, String> ("courseName"));
		button.setCellValueFactory(new PropertyValueFactory<User, Button>("button"));
		userTable.setItems(users);
	}
	
	public void passUser(List<User> response) {
		for(User eachRow : response) {
			eachRow.getButton().setText("Add");
			UpdateAddFriends.userList.add(eachRow);
		}
		updateTable();
		userList.clear();
    }
	
		
	@FXML
    void findUsers(KeyEvent event) {
    String firstname = searchFirstname.getText();
    String surname = searchSurname.getText();
    String courseName = searchCourseName.getText();
    String username = searchUsername.getText();
    String[] searchConditions = new String[] {"SearchUsers",firstname, surname, courseName, username, client.user.getUsername()};
    client.searchUsers(searchConditions);
    }
	
	@FXML
	void backToAccountProfile(ActionEvent e) throws IOException {
		client.findFriends();
		UpdateAccountProfileController update = new UpdateAccountProfileController(client);
		new AccountProfileController(update);
		client.updateAccountProfileController = update;
		backButton.getScene().getWindow().hide();
	}
	
}
