package assignment7;


import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.net.InetAddress;

public class ServerWindow extends Application{


    //Generates IP of machine running server and displays in window
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Server IP");
        GridPane gui = new GridPane();
        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        TextArea ta = new TextArea(ipAddress);
        gui.add(ta, 0, 0);
        primaryStage.setScene(new Scene(gui));
        primaryStage.show();
    }

}
