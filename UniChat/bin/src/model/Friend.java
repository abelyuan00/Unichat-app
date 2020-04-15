package model;

import gui.MainChatController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Friend {
	// profile
	private String firstname;
	private String surname;
	private String courseName;
	private String username;
	private Button button;
	
	public Friend(String firstname, String surname, String username, String courseName) {
		super();
		this.firstname = firstname;
		this.surname = surname;
		this.courseName = courseName;
		this.username = username;
		
		this.button = new Button("chat");
		button.setOnAction(this::beginChat);
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String course) {
		this.courseName = course;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}	
	
	public void beginChat(ActionEvent e) {
		Stage stage = new Stage();
			try {
				new MainChatController().start(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	} 
	
}
