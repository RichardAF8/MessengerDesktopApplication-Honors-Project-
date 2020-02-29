

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        TTS_Client client = new TTS_Client("10.0.0.127", 85);
        client.scanner = new Scanner(System.in);

        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());

        Thread recieveMessageThread = new Thread(() -> {
            try {
                client.recieveMessage();
            } catch (IOException e) {
                e.printStackTrace();
             return;
            }
        }
        );

        recieveMessageThread.start();
        client.sendMessage();
    }
}