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
import model.Group;



public class UpdateAddGroups {
	
	@FXML private TableView<Group> groupTable;
	@FXML private TableColumn<Group, String> groupName;
	@FXML private TableColumn<Group, Integer> numberOfMembers;
    @FXML private TableColumn<Group, Button> button;
    
    @FXML private Button changeToFriend;
    @FXML private Button SearchButton;
    @FXML private Button backButton;
    @FXML private TextField searchGroupName;
    @FXML private TextField searchGroupMember;

    
    @FXML
    void changeToFriend(ActionEvent event) {
    	UpdateAddFriends update = new UpdateAddFriends(client);
		new AddFriendsController(update);
		client.updateAddFriends = update;
		changeToFriend.getScene().getWindow().hide();
    }

	Client client;
	public FXMLLoader loader;
	public Stage stage;
	public ObservableList<Group> groups; 
	static List<Group> groupList = new ArrayList<>();
	
	public UpdateAddGroups(Client client){
		this.client = client;
		loader = new FXMLLoader(getClass().getResource("../AddGroups.fxml"));
		loader.setController(this);
		this.stage = new Stage();
		
	}
	
	public void updateTable() {
		groupTable.getItems().clear();
		groups = FXCollections.observableArrayList(groupList);
		groupName.setCellValueFactory(new PropertyValueFactory<Group, String> ("groupName"));
		numberOfMembers.setCellValueFactory(new PropertyValueFactory<Group, Integer> ("numberOfMembers"));
		button.setCellValueFactory(new PropertyValueFactory<Group, Button>("button"));
		button.setText("Add");
		groupTable.setItems(groups);
	}
	
	public void passGroup(List<Group> response) {
		for(Group eachRow : response) {
			eachRow.getButton().setText("Add");
			UpdateAddGroups.groupList.add(eachRow);
		}
		updateTable();
		groupList.clear();
    }
		
	
	@FXML
    void findGroups(KeyEvent event) {
		String groupName = searchGroupName.getText();
		String groupMember = searchGroupMember.getText();
		String[] searchConditions = new String[] {"SearchGroups", groupName, groupMember};
		client.searchGroups(searchConditions);
    }
	
	@FXML
	void backToAccountProfile(ActionEvent e) throws IOException {
		AccountProfileController.client = client;
		Stage stage = new Stage();
		Parent root;
		root = FXMLLoader.load(getClass().getResource("../AccountProfile.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Login");
		stage.setScene(scene); 
		stage.show();
		backButton.getScene().getWindow().hide();
	}
	
}
