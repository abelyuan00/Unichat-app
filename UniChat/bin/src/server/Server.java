package server;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * A simple (multi-threaded) server implementation
 * 
 * @author Jian Tang
 * @author Rumit Mehta
 * @author Hang Shi
 *
 */
public class Server {

	private static ObjectInputStream serverIn;
	private static ObjectOutputStream serverOut;
	private boolean loggedin;
	public static final int MAX_THREADS = 10;
	
	Map<String, ClientHandlerThread> clientMap = new HashMap<String, ClientHandlerThread>(); //<username, socket>
	

	/**
	 * Constructor for the ServerLogin class
	 * 
	 * @param port port number to listen on
	 */
	public Server(int port) {
		
		// initialise ServerSocket object that will be used to accept new clients
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			System.out.println("Creating server listening " + "to port " + port);

			// initialise a fixed size thread pool that can allow up to MAX_THREADS
			// concurrent threads
			ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

			// an infinite loop to accept clients indefinitely (on the main thread)
			while (true) { 
				// System.out.println("Waiting for client to connect");
				// call .accept to wait for a new client to connect
				// a new socket object is returned by .accept when the
				// new client connects successfully
				Socket newClientSocket = serverSocket.accept();
				System.out.println("connected to client with port:" + newClientSocket.getPort());
				
				// pass the socket created for the new client to a separate
				// ClientHandlerThread object and execute it on the thread pool 
				threadPool.execute(new ClientHandlerThread(newClientSocket));
			}

		}

		catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Inner class to represent a dedicated task that handles a particular client
	 * 
	 * @author Jian Tang
	 * @author Rumit Mehta
	 */
	private class ClientHandlerThread implements Runnable {

		private Socket clientSocket;
		boolean connected;

		/**
		 * Simple constructor that takes the socket created by the ServerSocket.accept()
		 * method
		 * 
		 * @param clientSocket the socket created by serverSocket.accept() that
		 *                     communicates to a specific client
		 */
		public ClientHandlerThread(Socket clientSocket) {
			this.clientSocket = clientSocket;
			connected = true;
		}

		/*
		 * every thing that happens inside the run method will execute on a new thread
		 */
		@Override
		public void run() {
			try { 
				serverOut = new ObjectOutputStream(clientSocket.getOutputStream());
				serverIn = new ObjectInputStream(clientSocket.getInputStream());
				
				while (connected) {
					String[] request = (String[]) serverIn.readObject();
					sortRequest(request);
				}
				
				clientSocket.close();
			} catch (IOException e) {
				System.out.println("Connection with client lost.");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		/**
		 * The sortRequest method sorts Looks at the request from the client and figures out what method to call.
		 * 
		 * @param request a string request where the first term dictates the type of the request
		 */
		public void sortRequest(String[] request) {
			if (request[0].equals("Login")) {
				System.out.println("server received login request");
				handleLogin(request);
			} else if (request[0].equals("Close")) {
				System.out.println("server received Close request");
				handleLogout();
			} else if (request[0].equals("Signup")) {
				System.out.println("server received signup request");
				handleSignup(request);
			}	else if (request[0].equals("MessageSend")) {
				System.out.println("server received MessageSend");
				handleMessage(request[1]);
			} else if (request[0].equals("SearchUsers")) {
				System.out.println("server received SearchUsers request");
				handleSearchUsers(request);
			} else if (request[0].equals("SearchGroups")) {
				System.out.println("server received SearchGroups request");
				handleSearchGroups(request);
			} else if (request[0].equals("StartConversation")) {
				System.out.println("server received StartConversation request");
				handleStartConversation(request[1],request[2]);
			} else if (request[0].equals("UserInfo")) {
				System.out.println("sever getting user info");
				handleUserInfo(request[1],request[2]);
			} else if (request[0].equals("findFriends")) {
				handleFindFriends(request);
			} else if (request[0].equals("findGroups")) {
				handleFindGroups(request);
			} 
				return;
		}		
		
		
		/**
		 * The handleLogin method handles the login request from the clients.
		 * 
		 * @param request the request from the client in the format: ["Login", username,
		 *                password]
		 */
		public void handleLogin(String[] request) {
			String sql = "SELECT USER_NAME, USER_PASSWORD FROM USER_INFO WHERE USER_NAME = '" + request[1] + "' AND USER_PASSWORD = '" + request[2] + "'";
			
			try(Connection connection = connectDatabase(); 	
				Statement statement = connection.createStatement()) {
				
				ResultSet resultSet = statement.executeQuery(sql);
				String[] response = new String[] {"LoginResponse",Boolean.toString(resultSet.next())};
				if(response[1] == "true") {
					clientMap.put(request[1],this);
				}
				serverOut.writeObject(response);
				serverOut.flush();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		public void handleUserInfo(String username,  String password) {
			String sql = "SELECT * FROM USER_INFO WHERE USER_NAME = '" + username + "' AND USER_PASSWORD = '" + password + "'" ;
			
			try(Connection connection = connectDatabase(); 	
					Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
					ResultSet resultSet = statement.executeQuery(sql);
					String[] response = new String[8];
					response[0] = "userInfoResponse";
					while(resultSet.next()) {
						response[1] = resultSet.getString(1); // Firstname
						response[2] = resultSet.getString(2); // Surname
						response[3] = resultSet.getString(3); // Username
						response[4] = resultSet.getString(4); // password
						response[5] = resultSet.getString(5); // email
						response[6] = resultSet.getString(6); // course type
						response[7] = resultSet.getString(7); // course name
						
					}
					
					System.out.println("Sending response to serverhandler");
					serverOut.writeObject(response);
					serverOut.flush();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		
		/**
		 * The handleSignup method handles the handle request from the clients.
		 * 
		 * @param request the request from the client 
		 * @author Hang Shi
		 * @author Jian Tang
		 */
		public void handleSignup(String[] request) {
			String sql = "INSERT INTO user_Info (first_name,last_name,user_name,user_password,email,course_type,course_name,create_date,photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try(Connection connection = connectDatabase();
					PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setString(1, request[1]);
					statement.setString(2, request[2]);
					statement.setString(3, request[3]);
					statement.setString(4, request[4]);
					statement.setString(5, request[5]);
					statement.setString(6, request[6]);
					statement.setString(7, request[7]);
					statement.setDate(8, Date.valueOf(LocalDate.now()));
					statement.setBytes(9, request[8].getBytes());	
					int flag = statement.executeUpdate();
					serverOut.writeBoolean(flag > 0);
					serverOut.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		public void handleFindFriends(String[] request) {
			String sql = "SELECT USER_ID FROM USER_INFO WHERE USER_NAME = '" + request[1] + "'";
			String sql2;
			String sql3;
			int clientId;
			int contact_userId;
			try(Connection connection = connectDatabase(); 	
					Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
					Statement statement2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
					Statement statement3 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)
							) {
					ResultSet resultSet = statement.executeQuery(sql);
					
					
					System.out.println("resultSet 1 is closed:  " + resultSet.isClosed());
					if(resultSet.next()) {
						clientId = resultSet.getInt(1);
						sql2 = "SELECT USER2_ID FROM USER_CONTACT WHERE USER1_ID = '" + clientId + "'";
						ResultSet resultSet2 = statement.executeQuery(sql2);
						int counter = 0;
						
						// to find length of response
						while(resultSet2.next()) {
							counter++;
						}
						
						resultSet2.beforeFirst();
						String[] response = new String[(4 * counter) + 1];
						System.out.println(response.length);
						response[0] = "findFriendsResponse";
						int[] contact_userIds = new int[counter ];
						
						counter = 0;
						while(resultSet2.next()) {
							contact_userIds[counter] = resultSet2.getInt(1);
							counter++;
						}
						
						counter = 0;
						for(int contact_id : contact_userIds) {
							sql3 = "SELECT * FROM USER_INFO WHERE USER_ID = " + contact_id;
							ResultSet resultSet3 = statement.executeQuery(sql3);
							if(resultSet3.next()) {
								response[counter + 1] = resultSet3.getString(1); // Firstname
								response[counter + 2] = resultSet3.getString(2); // Surname
								response[counter + 3] = resultSet3.getString(3); // Username							
								response[counter + 4] = resultSet3.getString(7); // course name
								counter = counter + 4;
							}
						
						}		
									
						serverOut.writeObject(response);
						serverOut.flush();						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		public void handleFindGroups(String[] request) {
			String sql = "SELECT GROUP_NAME, COUNT(ui.user_id) NUMBER_OF_MEMBERS FROM USER_INFO "
					+ "ui JOIN USER_GROUP ug ON ui.user_id = ug.user_id JOIN GROUP_INFO gi ON "
					+ "ug.group_id = gi.group_id WHERE ui.user_name = '" + request[1] + "' "
							+ "GROUP BY gi.group_name";
		
			try(Connection connection = connectDatabase(); 	
					Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
					ResultSet resultSet = statement.executeQuery(sql);
					int counter = 0;
					while(resultSet.next()) {
						counter++;
					}
					
					resultSet.beforeFirst();
					String[] response = new String[(2 * counter) + 1];
					response[0] = "findGroupsResponse";
					
					counter = 0;
					while(resultSet.next()) {
						response[counter+1] = resultSet.getString(1); 
						response[counter+2] = String.valueOf(resultSet.getInt(2)); 
						counter = counter + 2;
					}
					
					System.out.println("Sending response to serverhandler");
					serverOut.writeObject(response);
					serverOut.flush();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		}

		public void handleMessage(String message) {
			
		}
		
		
		/**
		 * 
		 * @param request {"searchUsers",firstname, surname, course, email, client_username}
		 */
		public void handleSearchUsers(String[] request) {
			for(int i = 0; i < request.length - 1; i++) { // request length - 1 because request[5] should stay the same
				String word = request[i];
				if(word.isEmpty()) {
					word = "IS NOT NULL";
				} else {
					word = "LIKE '" + word + "%'";
				}
				request[i] = word;
			}
			String sql = "SELECT * FROM USER_INFO WHERE FIRST_NAME " + request[1] + " AND LAST_NAME " + request[2] + " AND COURSE_NAME " + request[3] + " AND USER_NAME " + request[4];
			System.out.println(sql);
	
			try(Connection connection = connectDatabase(); 	
					Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
					ResultSet resultSet = statement.executeQuery(sql);
					int counter = 0;
					while(resultSet.next()) {
						if(!resultSet.getString(3).equals(request[5]) && !isContact(request[5],resultSet.getString(3))) {
							counter++;
						}
						
					}
					resultSet.beforeFirst();
					String[] response = new String[(4 * counter) + 1];
					response[0] = "searchUserResponse";
					counter = 0;
					while(resultSet.next()) {
						if(!resultSet.getString(3).equals(request[5]) && !isContact(request[5],resultSet.getString(3))) {
							response[counter+1] = resultSet.getString(1); //firstname
							response[counter+2] = resultSet.getString(2); //surname
							response[counter+3] = resultSet.getString(3); //username
							response[counter+4] = resultSet.getString(7); //coursename
							counter = counter + 4;
						}
					}
					
					System.out.println("Sending response to serverhandler");
					serverOut.writeObject(response);
					serverOut.flush();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		
		public boolean isContact(String userUsername, String recipientUsername) {
			System.out.println("Adding user contact into database");
			String sql1 = "SELECT * FROM USER_INFO WHERE USER_NAME =  '" + recipientUsername + "'";
			String sql2 = "SELECT * FROM USER_INFO WHERE USER_NAME =  '" + userUsername + "'";
			int recipient_userId = -1;
			int user_userId = -1;
			try(Connection connection = connectDatabase(); 	
					Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
					ResultSet resultSet = statement.executeQuery(sql1);
					while(resultSet.next()) {
						recipient_userId = Integer.parseInt(resultSet.getString(11)); 
					}
					resultSet = statement.executeQuery(sql2);
					while(resultSet.next()) {
						user_userId = Integer.parseInt(resultSet.getString(11)); 
					}
					String sql3 = "SELECT * FROM USER_CONTACT WHERE USER1_ID = '" + user_userId + "' AND USER2_ID = '" + recipient_userId + "'" ;
					resultSet = statement.executeQuery(sql3);
					return resultSet.next(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			return false; //incase the connection fails
		}
		
		/**
		 * 
		 * @param request {"searchGroups",groupName, groupMember}
		 */
		public void handleSearchGroups(String[] request) {	
			String sql = "SELECT GROUP_NAME, COUNT(ui.user_id) NUMBER_OF_MEMBERS "
					+ "FROM USER_INFO ui JOIN USER_GROUP ug ON ui.user_id = ug.user_id "
					+ "JOIN GROUP_INFO gi ON ug.group_id = gi.group_id WHERE GROUP_NAME "
					+ "ILIKE '%" + request[1] + "%' AND ui.user_name ILIKE '%" + request[2] + 
					"%' GROUP BY gi.group_name";
			
			System.out.println(sql);
	
			try(Connection connection = connectDatabase(); 	
					Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
					ResultSet resultSet = statement.executeQuery(sql);
					int counter = 0;
					while(resultSet.next()) {
						counter++;
					}
					
					resultSet.beforeFirst();
					String[] response = new String[(2 * counter) + 1];
					response[0] = "searchGroupResponse";
					
					counter = 0;
					while(resultSet.next()) {
						response[counter+1] = resultSet.getString(1); 
						response[counter+2] = String.valueOf(resultSet.getInt(2)); 
						counter = counter + 2;
					}
					
					System.out.println("Sending response to serverhandler");
					serverOut.writeObject(response);
					serverOut.flush();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		
		/**
		 * This method severs the connection between the client and server.
		 */
		public void handleLogout() {
			System.out.println("Client disconnected");
			loggedin = false;
		}

		public void handleStartConversation(String recipientUsername, String userUsername) {
			System.out.println("Adding user contact into database");
			String sql1 = "SELECT * FROM USER_INFO WHERE USER_NAME =  '" + recipientUsername + "'";
			String sql2 = "SELECT * FROM USER_INFO WHERE USER_NAME =  '" + userUsername + "'";
			int recipient_userId = -1;
			int user_userId = -1;
			try(Connection connection = connectDatabase(); 	
					Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
					ResultSet resultSet = statement.executeQuery(sql1);
					while(resultSet.next()) {
						recipient_userId = Integer.parseInt(resultSet.getString(11)); 
					}
					resultSet = statement.executeQuery(sql2);
					while(resultSet.next()) {
						user_userId = Integer.parseInt(resultSet.getString(11)); 
					}
					updateUserContactTable(recipient_userId, user_userId);
				} catch (Exception e) {
					e.printStackTrace();
				}

		}
		
		public void updateUserContactTable(int recipient_userId, int user_userId) {
			String sql =  "INSERT INTO USER_CONTACT (user1_id, user2_id,uc_create_date) VALUES (?, ?, ?)";
			
			try(Connection connection = connectDatabase();
					PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setInt(1, user_userId );
					statement.setInt(2, recipient_userId);
					statement.setDate(3,Date.valueOf(LocalDate.now()));
					statement.executeUpdate();
					statement.setInt(1, recipient_userId);
					statement.setInt(2, user_userId );
					statement.setDate(3, Date.valueOf(LocalDate.now()));
					statement.executeUpdate();
					serverOut.flush();
					System.out.println("i am here");
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			
		}
		
		/**
		 * The connectDatabase method connects to the database server and returns a Connection object. 
		 * 
		 * @return a Connection object
		 */
		public Connection connectDatabase() throws Exception  {
			String url;
			String username;
			String password;

			try (FileInputStream input = new FileInputStream(new File("db.properties"))) {
				Properties props = new Properties();

				props.load(input);

				// String driver = (String) props.getProperty("driver");
				username = (String) props.getProperty("username");
				password = (String) props.getProperty("password");
				url = (String) props.getProperty("URL");

				// We do not need to load the driver explicitly
				// DriverManager takes cares of that
				// Class.forName(driver);		
				
			}

 
			
			return DriverManager.getConnection(url, username, password);
		
		}
		
		
	}
}
