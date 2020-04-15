package model;
import java.util.Date;

public class Conversation {
	String groupName;
	String[] users;
	String[] admins;
	Date creationDate;
	
	public Conversation(String groupName, String[] users, String[] admins){
		this.groupName = groupName;
		this.users = users;
		this.admins = admins;
		this.creationDate = new Date();
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the users
	 */
	public String[] getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(String[] users) {
		this.users = users;
	}

	/**
	 * @return the admins
	 */
	public String[] getAdmins() {
		return admins;
	}

	/**
	 * @param admins the admins to set
	 */
	public void setAdmins(String[] admins) {
		this.admins = admins;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
}
