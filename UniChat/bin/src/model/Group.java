package model;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;

public class Group {
	private String groupName;
	private SimpleIntegerProperty numberOfMembers;
	private Button button;
	
	public Group(String groupName, int numberOfMembers) {
		super();
		this.groupName = groupName;
		this.numberOfMembers = new SimpleIntegerProperty(numberOfMembers);
		this.button = new Button("Chat");
		button.setOnAction(e -> {
			// TODO 
		});
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getNumberOfMembers() {
		return numberOfMembers.get();
	}

	public void setNumberOfMembers(SimpleIntegerProperty numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

	
	
	
}
