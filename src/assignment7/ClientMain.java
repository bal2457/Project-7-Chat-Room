/* Project 7 Chat Room <MyClass.java>
 * EE422C Project 7 submission by
 * Bryan Leon
 * bal2457
 * 16238
 * Daniel Laveman
 * del824
 * 16230
 * Slip days used: <0>
 * Spring 2017
 */
package assignment7;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

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
	private int type=0;
	protected BufferedReader reader; 
	protected PrintWriter writer;
	private static String myPackage;
	private String name;
    protected String clientUsername;
    protected double windowWidth = 550;
    protected double windowHeight = 400;
    private boolean group = false;
    private GridPane gui;
    private TextArea ta, ta1;
    public ArrayList<String> clientList=new ArrayList<String>();

    protected static int clientNumber = 0;
    private TextArea chatLog = new TextArea();

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUpNetworking();
        loginSetup(primaryStage);
        //chatStartUp(primaryStage);

    }

    protected void loginSetup(Stage primaryStage) {
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
        if(type==0){
        
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
    }
        else {
   //     	if(type==2)
        		clientUsername=name;
   //     	else if(type==1){
        		
    //    	}
        	clientNumber++;
    	    Server.addUsername(clientUsername, clientNumber);
    	    chatStartUp(primaryStage);
        }
        primaryStage.setScene(new Scene(gui));
        primaryStage.show();
    }

    protected void chatStartUp(Stage primaryStage){
        gui.getChildren().clear();
        windowHeight = 900; windowWidth = 650;
        primaryStage.setMinWidth(windowWidth);
        primaryStage.setMinHeight(windowHeight);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth()-windowWidth);
        primaryStage.setY(primaryScreenBounds.getMinY());
        gui.setBackground(new Background (new BackgroundFill(Color.DARKORANGE, new CornerRadii(1), null)));
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
        TextField groupTF=new TextField();
        groupTF.setPromptText("Enter client name to add");
        TextField indivTF=new TextField();
        indivTF.setPromptText("Who do you want to message?");
        ta = new TextArea();
        ta.setWrapText(true);
        ta.setEditable(false);
        ta.setMaxWidth(windowWidth-10);
        ta.setPrefHeight(windowHeight-240);
		Server.addClient(this);

      mainSend.setOnAction(new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent event) {
			if(!indivTF.getText().equals("")){
				for(ClientMain c:Server.getClients()){
					c.group=false;
					c.clientList.clear();
					c.clientList.add(clientUsername);
					c.clientList.add(indivTF.getText());
				}
			}
			else{
				for(ClientMain c:Server.getClients()){
					c.group=true;
					}
			}
			writer.println(clientUsername+": "+tf.getText()); 
			writer.flush();
			tf.setText("");
			tf.requestFocus();
		}
      });
//

        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setBottom(paneForTextField);
        // Create a scene and place it in the stage
        gui.add(mainPane, 0, 0);
        HBox hb1 = new HBox(50);
        Button quitChatButton = new Button("Quit chat client");
        Button addClientButton = new Button("Add Client");
        //Button oneOnone=new Button("Individual Chat");
        hb1.getChildren().add( quitChatButton);
        gui.add(addClientButton, 1, 2);
        gui.add(groupTF, 0, 2);
        //gui.add(oneOnone, 1, 2);
        gui.add(indivTF, 0, 1);
        addClientButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				ClientMain newClient=new ClientMain();
				newClient.type=1;
				newClient.name=groupTF.getText();
				groupTF.clear();
				Stage secondaryStage=new Stage();
				//oneOnone.setDisable(true);
				try {
					newClient.start(secondaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				
/*				GridPane gui2=new GridPane();
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
        
/*        oneOnone.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				ClientMain newClient= new ClientMain();
				newClient.type=2;
				Stage secondaryStage=new Stage();
				//clientList.addAll(Server.getList());
				if(!clientList.contains(clientUsername)){
				newClient.clientList.add(clientUsername);
				}
				if(!Server.checkUsername(indivTF.getText())){
					newClient.name=indivTF.getText();
					indivTF.clear();
					//clientList.add(newClient.name);
					//newClient.clientList.add(newClient.name);
					//clientNumber++;
				    //Server.addUsername(clientUsername, clientNumber);
				    chatStartUp(secondaryStage);
				    secondaryStage.setScene(new Scene(gui));
				    secondaryStage.show();
				    
					try {
						newClient.start(secondaryStage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            //chatStartUp(secondaryStage);
				}
				
			}
			
		});*/
        gui.add(hb1, 1, 0);
        quitChatButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
    		public void handle(ActionEvent event) {
                System.exit(0);
    			}
            });
    }
	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
        String ipAddress = InetAddress.getLocalHost().getHostAddress();
		Socket sock = new Socket(ipAddress, 4242);
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
					if(clientList.contains(clientUsername)||group)
						ta.appendText(message + "\n"); 
					}
			} catch (IOException ex) { 
				ex.printStackTrace(); 
			}
		}
	}
}