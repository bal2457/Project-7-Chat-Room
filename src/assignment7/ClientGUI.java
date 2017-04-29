//package assignment7;
//Old version of new code (ClientMain.Java)
//
//
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.geometry.Rectangle2D;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.effect.DropShadow;
//import javafx.scene.input.KeyCode;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.stage.Screen;
//import javafx.stage.Stage;
//
//
//public class ClientGUI extends Application {
//
//
//    private String clientUsername;
//    private double windowWidth = 550;
//    private double windowHeight = 400;
//    private boolean loginStatus = false;
//    private GridPane gui;
//    private TextArea ta, ta1;
//
//    private static int clientNumber = 0;
//    private TextArea chatLog = new TextArea();
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//        //TODO: Add network setup method
//        loginSetup(primaryStage);
//
//    }
//
//    private void loginSetup(Stage primaryStage) {
//
//        primaryStage.setTitle("Longhorn Messenger Client");
//        primaryStage.setHeight(windowHeight);
//        primaryStage.setWidth(windowWidth);
//        primaryStage.setResizable(false);
//        gui = new GridPane();
//        gui.setHgap(10);
//        gui.setVgap(10);
//        gui.setPadding(new Insets(10,10,10,10));
//
//        DropShadow ds = new DropShadow();
//        ds.setOffsetY(3.0f);
//        ds.setColor(Color.BLACK);
//
//        Text titleText = new Text("Longhorn Messenger Client");
//        titleText.setEffect(ds);
//        titleText.setFill(Color.BLACK);
//        titleText.setFont(Font.font(null, 40));
//        gui.add(titleText, 0, 0);
//
//        Label usernameLabel = new Label("Enter Username: ");
//        TextField usernameTextField = new TextField();
//        HBox HBUsername = new HBox();
//        HBUsername.getChildren().addAll(usernameLabel,usernameTextField );
//        HBUsername.setSpacing(10);
//        Button chatButton = new Button("Chat!");
//        HBUsername.getChildren().addAll(chatButton);
//        gui.add(HBUsername, 0, 10);
//
//        gui.setBackground(new Background (new BackgroundFill(Color.ORANGE, new CornerRadii(1), null)));
//        chatButton.setOnAction((event) -> {
//            String text;
//            while(true){
//                text = usernameTextField.getText();
//                if(Server.checkUsername(text)){
//                    Label takenUsername = new Label("Sorry, that Username is taken.");
//                    takenUsername.setTextFill(Color.RED);
//                    gui.add(takenUsername, 45, 21);
//                }
//                else{
//                    break;
//                }
//            }
//            clientNumber++;
//            clientUsername = text;
//            Server.addUsername(text, clientNumber);
//            chatStartUp(primaryStage);
//        });
//        primaryStage.setScene(new Scene(gui));
//        primaryStage.show();
//    }
//
//    private void chatStartUp(Stage primaryStage){
//        gui.getChildren().clear();
//        windowHeight = 900; windowWidth = 650;
//        primaryStage.setMinWidth(windowWidth);
//        primaryStage.setMinHeight(windowHeight);
//        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
//        primaryStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth()-windowWidth);
//        primaryStage.setY(primaryScreenBounds.getMinY());
//        gui.setBackground(new Background (new BackgroundFill(Color.DARKORANGE, new CornerRadii(1), null)));
//        // Panel p to hold the label and text field
//        Button mainSend = new Button("Send");
//        BorderPane paneForTextField = new BorderPane();
//        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
//        paneForTextField.setLeft(new Label("Type here to send a message: "));
//        paneForTextField.setRight(mainSend);
//        TextField tf = new TextField();
//        tf.setAlignment(Pos.BOTTOM_LEFT);
//        paneForTextField.setCenter(tf);
//        BorderPane mainPane = new BorderPane();
//        ta = new TextArea();
//        ta.setWrapText(true);
//        ta.setEditable(false);
//        ta.setMaxWidth(windowWidth-10);
//        ta.setPrefHeight(windowHeight-140);
//
//        mainSend.setOnAction((event) -> {
//            tf.clear();
//        //TODO: Add code for what happens when you press send button
//
//        });
//        tf.setOnKeyPressed(event -> {
//            if(event.getCode().equals(KeyCode.ENTER)){
//                tf.clear();
//            //TODO: Add code for what happens when you press enter to send chat
//            }
//        });
//
//        mainPane.setCenter(new ScrollPane(ta));
//        mainPane.setBottom(paneForTextField);
//        // Create a scene and place it in the stage
//        gui.add(mainPane, 0, 0);
//        HBox hb1 = new HBox(50);
//        Button quitChatButton = new Button("Quit chat client");
//        hb1.getChildren().add( quitChatButton);
//        gui.add(hb1, 0, 1);
//        quitChatButton.setOnAction((event) -> Platform.exit());
//    }
//}
