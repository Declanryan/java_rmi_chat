
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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

// class for creating interface for logging in to the server
// and starting the thread and client GUI 
public class AdminGui extends Application {

	// Create the Message Label
	static Label messageLbl = new Label(
			"Please enter your login details to start a client");
	
	// create message area for displaying server messages if necessary
	static TextArea adminMessageArea = new TextArea();

	@Override// creates the mainStage for displaying scenes
	public void start(Stage mainStage) throws Exception {
		
		// creates a Timeline to check which threads are still connected
		// and displays a message to clients if a thread is disconnected
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Timeline verifyClients = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	
				for (Thread t : threadSet) {
					if(t.isDaemon()==false) {// Check if thread is a daemon
						if(t.isAlive()==false) {// check if thread is alive
							AdminGui.addTextToAdminMessageArea(t.getName()+" has been deactivataed");// output message
						}
					}
		        
				}
		    }
		}));
        verifyClients.setCycleCount(Timeline.INDEFINITE);// set loop run time
        verifyClients.play();// starts the loop
		
		// create the main login GUI
		final TextField clientName = new TextField();
		clientName.setPromptText("Input your username here");
		final TextField clientPassword = new TextField();
		clientPassword.setPromptText("Input your password here");
		adminMessageArea.appendText("~~ Welcome To RMI Chat Server ~~");
		adminMessageArea.appendText("\n--------Login-Screen------------------\n");
		adminMessageArea.appendText("\n--------------------------------------\n");
		adminMessageArea.appendText("      ~~ RMI Registry Started ~~\n");
		adminMessageArea.appendText("        ~~ Server Started ~~\n");
		adminMessageArea.setEditable(false);
		Locale currentLocale = Locale.getDefault();
		 
		// performs a remote lookup and assigns it to chatinterface
		ChatInterface chatinterface = (ChatInterface) Naming.lookup("rmi://localhost/RMIServer");
		String country =currentLocale.getDisplayCountry();// gets the country and assigns it to a variable
		
		// button to login and start a new thread with a client object
		Button loginBtn = new Button("Login");// create the client thread
		loginBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String client = clientName.getText();// get the username
				String pass = clientPassword.getText();// get the password

				try {
					// check user name and password
					if (chatinterface.checkClientCredintials(chatinterface, client, pass) == true) {
						// create a thread with a client 
						new Thread(new Client(chatinterface, client, pass, country)).start();

					} else {// display error message
						printMessage("Login details are incorrect");
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});

		// Create button to display total connected clients
		Button totalConnectedBtn = new Button("Connected Clients");
		totalConnectedBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					addTextToAdminMessageArea(chatinterface.displayClientList());
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		// Create button to clear the message area
			Button clearBtn = new Button("Clear");
			clearBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					adminMessageArea.clear();
				}
			});
			
		VBox root = new VBox();
		Button exitBtn = new Button("EXIT");// creates the exit
		exitBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				mainStage.close();
				System.exit(0);

			}
		});

		// create box for holding buttons
		HBox buttonBox = new HBox();
		// Add the buttons to the box
		buttonBox.getChildren().addAll(loginBtn, totalConnectedBtn ,exitBtn, clearBtn);
		// Set the spacing between children to 15px
		buttonBox.setSpacing(15);
		root.getChildren().addAll(clientName, clientPassword, buttonBox, messageLbl, adminMessageArea);
		root.setSpacing(15);// set the spacing
		root.setMinSize(350, 250);

		/*
		 * Set the padding of the VBox Set the border-style of the VBox Set the
		 * border-width of the VBox Set the border-insets of the VBox Set the
		 * border-radius of the VBox Set the border-color of the VBox
		 */
		root.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
				+ "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: blue;");

		// Create the Scene
		Scene scene = new Scene(root);// create a scene
		mainStage.setScene(scene);// add scene to stage
		mainStage.setTitle("RMI Login Screen:");// set title
		mainStage.show();// display the stage
	}

	// displays messages to the main gui
	public static void printMessage(String message) {
		// Set the Text of the Label
		messageLbl.setText(message);
	}

	// adds text to the main gui message box
	public static void addTextToAdminMessageArea(String message) {
		// Set the Text of the Label
		adminMessageArea.appendText(message+"\n");
	}

}
