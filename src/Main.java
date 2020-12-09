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
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class Main extends Application implements Initializable {
    public TextArea text;
    public TextArea screen;
    public CheckBox button;
    private static TTS_Client client;
    private String message;
    // ensures the G.U.I starts first
    private static CountDownLatch latch = new CountDownLatch(1);

    // Turns on and off the Text-To-Speech feature
    public void activate() {
        client.setVoiceOn(button.isSelected());

    }

    // Sets up the Stage
    @Override
    public void start(Stage primaryStage) throws Exception {
        // imports the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("sample/sample.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(root, 460.5, 343.0));
        // Closes the entire program on exit
        primaryStage.setOnCloseRequest((e) -> {
            Platform.exit();
            System.exit(0);
        });
        // displays window
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {

        // Connects to Socket
        // input your own ip address
        client = new TTS_Client("10.0.0.126", 85);

        // G.U.I THREAD
        Thread gui = new Thread(() -> {
            launch(args);
        });
        gui.start();
        latch.countDown();
    }

    // Initializer
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Starts the TTS feature
        button.setSelected(true);

        try {
            latch.await();
            screen.setText("Connected to Server: " + client.getInetAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Thread to receive Message
        Thread receiveMessageThread = new Thread(() -> {
            try {
                latch.await();
                client.receiveMessage(screen);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        });
        receiveMessageThread.start();

        // Sends Message when enter key is pressed in TextArea
        text.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    message = text.getText();
                    text.clear();

                    try {
                        latch.await();
                        client.sendMessage(message, screen);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
