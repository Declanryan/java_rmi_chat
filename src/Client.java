
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

// class to create and monitor the client objects
public class Client extends UnicastRemoteObject implements ChatInterface, Runnable {

	// class attributes
	private static final long serialVersionUID = 1L;
	private ChatInterface server;
	static boolean chkExit = true;
	boolean chkLog = false;
	private String clientname;
	TextArea clientMessageArea = new TextArea();
	private String clientCountry;
	static Stage clientStage;
	Thread thisThread;

	// constructor for the client class
	protected Client(ChatInterface chatinterface, String clientName, String password, String country)
			throws RemoteException {
		this.server = chatinterface;
		this.clientname = clientName;
		this.clientCountry = country;
		clientGui(chatinterface, clientName, password, clientCountry);
		chkLog = server.checkClientCredintials(this, clientname, password);
	}

	// method to create the client gui object
	public void clientGui(ChatInterface chatinterface, String clientName, String clientPass, String clientCountry) {

		// create a new stage with inputs and button objects
		Stage clientStage = new Stage();
		TextField textInput = new TextField();
		textInput.setPromptText("Input your message here");

		clientMessageArea.appendText(clientName + " has successfully Connected To the RMI Server\n");
		clientMessageArea.appendText("-----------------------------------------------------\n");
		clientMessageArea.setEditable(false);
		clientMessageArea.setPrefColumnCount(20);
		clientMessageArea.setPrefRowCount(10);

		// Create the logout Button
		Button logOutBtn = new Button("LOGOUT");// creates the logout
		logOutBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					chatinterface.removeClient(clientName);// remove client from connected clients
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}

				Client.chkExit = false;// end the thread
				clientStage.close();// close the client window
				try { // display confirmation of logout message
					chatinterface.broadcastMessage(clientCountry, clientName,
							" Successfully Logged Out From The RMI Chat Program\n");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}

			}
		});

		// create a timeline to manage client timeouts
		Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(60), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				logOutBtn.fire();

			}
		}));
		fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
		fiveSecondsWonder.play();// start the timeline

		// Create the send Button
		Button sendBtn = new Button("Send Message");
		sendBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {// send the message to the server to send message to connected clients
					chatinterface.broadcastMessage(clientCountry, clientName, textInput.getText());
					fiveSecondsWonder.playFromStart();// reactivate the timeline
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});

		// Create the client list Button
		Button clientListBtn = new Button("Client List");
		clientListBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					fiveSecondsWonder.playFromStart();// reactivate the timeline
					clientMessageArea.appendText(chatinterface.displayClientList());// add message to the client text
																					// area
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		// Create button to display total connected
		Button clearBtn = new Button("Clear");
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				fiveSecondsWonder.playFromStart();// reactivate the timeline
				clientMessageArea.clear();// clears the message area
			}
		});

		// Create the VBox
		VBox root = new VBox();
		// Create the HBox
		HBox buttonBox = new HBox();
		// Add the children to the HBox
		buttonBox.getChildren().addAll(sendBtn, clientListBtn, clearBtn, logOutBtn);
		// Set the vertical spacing between children to 15px
		buttonBox.setSpacing(15);
		// add the inputs yo the box
		root.getChildren().addAll(new Label("Input:"), textInput, new Label("Messages:"), clientMessageArea, buttonBox);
		// Set the size of the box
		root.setMinSize(350, 250);
		// set the style
		root.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
				+ "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: blue;");

		// Create the Scene
		Scene scene = new Scene(root);
		// Add the scene to the Stage
		clientStage.setScene(scene);
		// Set the title of the Stage

		clientStage.setTitle("A RMI CLIENT: " + clientName);
		clientStage.show();// display the client stage
	}

	// unused
	public void start(Stage stage) throws Exception {
		System.out.println("error: client start");// for testing
	}

	// method to add text to the client message area
	public void sendMessageToClient(String message) throws RemoteException {
		clientMessageArea.appendText(message);
	}

	// unused
	public void broadcastMessage(String clientCountry, String clientname, String message) throws RemoteException {
	}

	// unused
	public boolean checkClientCredintials(ChatInterface chatinterface, String clientname, String password)
			throws RemoteException {
		return true;
	}

	// execute the thread and wait for flag to close
	public void run() {
		
		while (chkExit) {// flag to end thread
			if (chkExit == false) {
				break;
			}

		}
		try {
			Thread.sleep(1000); // allow for check
		} catch (InterruptedException e) {
			System.exit(-1);
		}

	}

	// unused
	public void removeClient(String clientname) throws RemoteException {
	}

	// unused
	public String displayClientList() throws RemoteException {
		return "ERROR";// used for testing

	}
}
