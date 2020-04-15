package model;

import javafx.scene.control.Button;
import client.Client;
import gui.MainChatController;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * 
 * @author Rumit Mehta
 *
 */
public class User {
	private Client client;
	private String username;
	private String password;
	private String firstname;
	private String surname;
	private String email;
	private String photo;
	private String courseType;
	private String courseName;
	private Button button;
	
	public User(String firstname, String surname, String username, String password, String email, String courseType, String courseName) {
		super();
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.surname = surname;
		this.email = email;
		this.courseType = courseType;
		this.courseName = courseName;		
		this.button = new Button("Add");
		button.setOnAction(click -> {this.addUser(click);
			});
	}
	
	public User(String firstname, String surname, String username, String courseName, Client client) {
		super();
		this.firstname = firstname;
		this.surname = surname;
		this.username = username;
		this.courseName = courseName;
		this.button = new Button("add");
		this.client = client;
		button.setOnAction(this::addUser);
	}


	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	
	

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String course) {
		this.courseType = course;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the photo
	 */
	public String getPhoto() {
		return photo;
	}
	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}	
	
	public void addUser(ActionEvent e) {
		Stage stage = new Stage();
			try {
				client.startConversation(getUsername());
				button.setDisable(true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", firstname=" + firstname + ", surname="
				+ surname + ", courseName=" + courseName + "]";
	}
	
}
