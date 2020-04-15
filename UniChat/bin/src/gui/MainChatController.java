package gui;
import bubble.SpeechBox;
import bubble.SpeechDirection;
import client.Client;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class MainChatController extends Application {
	

    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();
   
	@FXML
    private TextField searchBar;

    @FXML
    private ImageView ContactProfilePicture;

    @FXML
    private Label ContactName;

    @FXML
    private Label contactLastMessage;

    @FXML
    private ImageView userProfilePicture;

    @FXML
    private ImageView newConversation;

    @FXML
    private ImageView contactMenu;

    @FXML
    private Line lineSeperator;

    @FXML
    private ImageView currentProfilePicture;

    @FXML
    private Label currentConversationName;

    @FXML
    private ImageView conversationMenu;

    @FXML
    private TextField messageBox;

    @FXML
    private ImageView messageSendButton;

    @FXML ScrollPane conversationPane;
    
    @FXML VBox conversationBox;

	public static Client client;

	    @FXML
	    void contactOptions(MouseEvent event) {

	    }

	    @FXML
	    void conversationOptions(MouseEvent event) {

	    }

	    @FXML
	    void getUserInfo(MouseEvent event) {

	    }

	    @FXML
	    void keyboardMessageSend() {
	    	
	    	String message = messageBox.getText();
	    	if(!message.isEmpty()) {
	    		client.sendMessage(message);
	    		showMessage(message);
	            
	    	}
	    	
	    }

	    @FXML
	    void searchContacts(ActionEvent event) {
	    }
	    
	    public void sendMessage(String message){
	        speechBubbles.add(new SpeechBox(message, SpeechDirection.RIGHT));
	        conversationBox.getChildren().add(new SpeechBox(message, SpeechDirection.RIGHT));
	        conversationPane.vvalueProperty().bind(conversationBox.heightProperty());
	        client.sendMessage(message);
	        
	    }
	    
	    public void recieveMessage(String message) {
	    	speechBubbles.add(new SpeechBox(message, SpeechDirection.LEFT));
	    	conversationBox.getChildren().add(new SpeechBox(message, SpeechDirection.LEFT));
	    	conversationPane.vvalueProperty().bind(conversationBox.heightProperty());
	    }
	    
	    public void showMessage(String message) {
	    	conversationPane.setContent(conversationBox);
	        conversationPane.prefWidthProperty().bind(conversationBox.prefWidthProperty().subtract(1));
	        conversationPane.setFitToWidth(true);
    		sendMessage(message);
	    	System.out.println(messageBox.getText());
            messageBox.setText("");
	    }
    
	    public void start(Stage stage, Client client) {
	    	this.client = client; 
	    	try {
				start(stage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
    public static void passClient(Client client) {
    	MainChatController.client = client;
	}
	    
    @Override
    public void start(Stage stage) throws Exception {
    	try {
    		
			Parent root = FXMLLoader.load(getClass().getResource("../MainChat.fxml"));
			stage.setTitle("Main Chat");	
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        launch(args);
        
    }



}
