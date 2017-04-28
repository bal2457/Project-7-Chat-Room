package assignment7;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

//import com.sun.javafx.scene.layout.region.BackgroundFill;

/*import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;*/
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ClientMain extends Application{ 
	private BufferedReader reader; 
	private PrintWriter writer;
	private static String myPackage;
	private String name;
    private String clientUsername;
    private double windowWidth = 550;
    private double windowHeight = 400;
    private boolean loginStatus = false;
    private GridPane gui;
    private TextArea ta, ta1;

    private static int clientNumber = 0;
    private TextArea chatLog = new TextArea();

    @Override
    public void start(Stage primaryStage) throws Exception {

        setUpNetworking();
        loginSetup(primaryStage);
        //chatStartUp(primaryStage);

    }

    private void loginSetup(Stage primaryStage) {

        primaryStage.setTitle("Longhorn Messenger Client");
        primaryStage.setHeight(windowHeight);
        primaryStage.setWidth(windowWidth);
        primaryStage.setResizable(false);
        gui = new GridPane();
        gui.setHgap(10);
        gui.setVgap(10);
        gui.setPadding(new Insets(10,10,10,10));

        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.BLACK);

        Text titleText = new Text("Longhorn Messenger Client");
        titleText.setEffect(ds);
        titleText.setFill(Color.BLACK);
        titleText.setFont(Font.font(null, 40));
        gui.add(titleText, 0, 0);

        Label usernameLabel = new Label("Enter Username: ");
        TextField usernameTextField = new TextField();
        HBox HBUsername = new HBox();
        HBUsername.getChildren().addAll(usernameLabel,usernameTextField );
        HBUsername.setSpacing(10);
        Button chatButton = new Button("Chat!");
        HBUsername.getChildren().addAll(chatButton);
        gui.add(HBUsername, 0, 10);

        chatButton.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent event){
            String text;
            while(true){
                text = usernameTextField.getText();
                if(Server.checkUsername(text)){
                    Label takenUsername = new Label("Sorry, that Username is taken.");
                    takenUsername.setTextFill(Color.RED);
                    gui.add(takenUsername, 45, 21);
                }
                else{
                    break;
                }
            }
            clientNumber++;
            clientUsername = text;
            Server.addUsername(text, clientNumber);
            chatStartUp(primaryStage);
        	}
        });
        primaryStage.setScene(new Scene(gui));
        primaryStage.show();
    }

    private void chatStartUp(Stage primaryStage){
        gui.getChildren().clear();
        windowHeight = 900; windowWidth = 650;
        primaryStage.setMinWidth(windowWidth);
        primaryStage.setMinHeight(windowHeight);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth()-windowWidth);
        primaryStage.setY(primaryScreenBounds.getMinY());
//        gui.setBackground(new Background (new BackgroundFill(Color.DARKORANGE, new CornerRadii(1), null)));
        // Panel p to hold the label and text field
        Button mainSend = new Button("Send");
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setLeft(new Label("Type here to send a message: "));
        paneForTextField.setRight(mainSend);
        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_LEFT);
        paneForTextField.setCenter(tf);
        BorderPane mainPane = new BorderPane();
        ta = new TextArea();
        ta.setWrapText(true);
        ta.setEditable(false);
        ta.setMaxWidth(windowWidth-10);
        ta.setPrefHeight(windowHeight-240);

      mainSend.setOnAction(new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent event) {
			writer.println(clientUsername+": "+tf.getText()); 
			writer.flush();
			tf.setText("");
			tf.requestFocus();
		}
      });
        tf.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                tf.clear();
            //TODO: Add code for what happens when you press enter to send chat
            }
        });

        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setBottom(paneForTextField);
        // Create a scene and place it in the stage
        gui.add(mainPane, 0, 0);
        HBox hb1 = new HBox(50);
        Button quitChatButton = new Button("Quit chat client");
        Button makeGroupButton = new Button("Make Group");
//        Button oneOnone=new Button("Individual Chat");
        hb1.getChildren().add( quitChatButton);
        gui.add(makeGroupButton, 0, 1);
//        gui.add(oneOnone, 1, 1);
        makeGroupButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				ClientMain newClient=new ClientMain();
				Stage secondaryStage=new Stage();
				try {
					newClient.start(secondaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
/*		oneOnone.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				
				
			}
			
		});
				
				GridPane gui2=new GridPane();
				Scene view=new Scene(gui2,10,10);
				TextField groupField=new TextField();
				gui2.add(groupField, 0, 0);
				clientNumber++;
				secondaryStage.setScene(view);
				secondaryStage.show();
				Server.addUsername(groupField.getText(), clientNumber);
				chatStartUp(secondaryStage);
				try {
					start(secondaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
        	
        });
        gui.add(hb1, 0, 2);
        quitChatButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
    		public void handle(ActionEvent event) {
                System.exit(0);
    			}
            });
    }
	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource") 
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader); 
		writer = new PrintWriter(sock.getOutputStream()); 
		System.out.println("networking established"); 
		Thread readerThread = new Thread(new IncomingReader()); 
		readerThread.start();
	}
/*	class SendButtonListener implements ActionListener { 
		public void actionPerformed(ActionEvent ev) {
			writer.println(outgoing.getText()); 
			writer.flush();
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}
	class StartConvoListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			Class<?> myClient = null;
			Constructor<?> constructor = null;
			Object instanceOfMyClient = null;
	        String client_class_name=myPackage+"."+clientName.getText();
	        try {
				myClient = Class.forName(client_class_name); 	// Class object of specified name
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				constructor = myClient.getConstructor();		// No-parameter constructor object
				instanceOfMyClient = constructor.newInstance();	// Create new object using constructor
			} catch (InstantiationException e1) {											// various exceptions 
				// Do whatever is needed to handle the various exceptions here -- e.g. rethrow Exception
				e1.printStackTrace();
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
			} 
			catch (SecurityException e) {
				e.printStackTrace();
			}
			catch(IllegalAccessException e2){
				e2.printStackTrace();
			}
			catch(IllegalArgumentException e3){
				e3.printStackTrace();
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			ClientMain me = (ClientMain)instanceOfMyClient;
	        
		}
	}*/
	public static void main(String[] args) {
		try {
			launch(args);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	} 
	
	class IncomingReader implements Runnable {
		public void run() { 
			String message; 
			try {
				while ((message = reader.readLine()) != null) {
					ta.appendText(message + "\n"); 
					}
			} catch (IOException ex) { 
				ex.printStackTrace(); 
			}
		}
	}
}