import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable {
    public TextArea text;
    public TextArea screen;
    public CheckBox button;
    private static TTS_Client client;
    private String message;

    //Turns on and off the Text-To-Speech feature
    public void activate() {
        client.setVoiceOn(button.isSelected());

    }
    //Sets up the Stage
    @Override
    public void start(Stage primaryStage) throws Exception {
        //imports the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("sample/sample.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(root, 460.5, 343.0));
        //Closes the entire program on exit
        primaryStage.setOnCloseRequest((e)-> {Platform.exit(); System.exit(0);});
        //displays window
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {

        //Connects to Socket
        client = new TTS_Client("10.0.0.127", 85);

        // G.U.I THREAD
        Thread gui = new Thread(() -> {
            launch(args);
        });
        gui.start();
    }

    //Initiliazer
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Sends Message when enter key is pressed in TextArea
        text.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    message = text.getText();
                    text.clear();

                    try {
                        client.sendMessage(message, screen);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //Starts the TTS feature
        button.setSelected(true);

        //Welcome Message for Chat
        screen.setText("Connected to Server: " + client.socket.getInetAddress());

        //Thread to receive Message
        Thread receiveMessageThread = new Thread(() -> {
            try {

                client.receiveMessage(screen);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        });
        receiveMessageThread.start();
    }
}